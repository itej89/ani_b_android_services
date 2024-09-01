package Framework.JOBS;

import android.os.Handler;
import android.os.ParcelUuid;

import java.util.ArrayList;
import java.util.UUID;

import Framework.DataTypes.Constants.ActuatorAttachStates;
import Framework.DataTypes.Constants.CommandLabels;
import Framework.DataTypes.Constants.JOB_PRIORITY;
import Framework.DataTypes.Delegates.JobConvey;
import Framework.DataTypes.Job;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequest;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestAttachLockServo;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestDelay;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestEasingInOut;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestLeftLockServoAngle;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestPowerOnLockServo;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestRightLockServoAngle;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestServoEasing;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestTiming;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestTrigger;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponse;
import FrameworkInterface.InterfaceImplementation.KineticComms;
import FrameworkInterface.PublicTypes.Constants.Actuator;
import FrameworkInterface.PublicTypes.Delegates.KineticsResponseConvey;

public class AttachActuatorJob extends Job implements KineticsResponseConvey
{

    ParcelUuid WaitingForKineticsRequestAck = new ParcelUuid(UUID.randomUUID());
    ArrayList<KineticsResponse> LastKineticsResponse = new ArrayList<KineticsResponse>();

    ArrayList<KineticsRequest> kineticsRequest = new ArrayList<KineticsRequest>();

    Actuator actuatorType;

    //KineticsResponseConvey delegate
    public void CommsLost() {

        super.delegate.notify_LostResource( ID);
    }

    public void MachineResponseRecieved(ArrayList<KineticsResponse> responeData, UUID _Acknowledgement) {

        if(_Acknowledgement.toString().equals(WaitingForKineticsRequestAck.toString())){
            WaitingForKineticsRequestAck = new ParcelUuid(UUID.randomUUID());
            LastKineticsResponse = responeData;
            super.delegate.notify_NextStep( ID);
        }
    }
    //End KineticsResponseConvey delegate


    ActuatorAttachStates.STATES CURRENT_STATE = ActuatorAttachStates.STATES.NA;
    public AttachActuatorJob(Actuator actuator) {
        super();
        actuatorType = actuator;
        PRIORITY = JOB_PRIORITY.USER_CRITICAL;
    }

    //Job Override
    @Override
    public void TakeOverResources(JobConvey _delegate) {
        KineticComms.Instance.SetKineticsResposeListener(this);
        super.TakeOverResources(_delegate);
        CURRENT_STATE = ActuatorAttachStates.STATES.PING;
    }
    @Override
    public void Pause() {
        KineticComms.Instance.SetKineticsResposeListener( null);
        KineticComms.Instance.ResetCommsContext();

        super.Pause();
        super.delegate.notify_Finish( ID);
    }
    @Override
    public void Resume() {

        DOSTEP();
    }
//End Job Override

    public void MachiResponseTimeout(ArrayList<KineticsResponse> partialResponse, UUID _Acknowledgement) {
        //Retry if any communication fail
        CURRENT_STATE = ActuatorAttachStates.STATES.PING;
        DOSTEP();
    }

    void DOSTEP()
    {
        switch(actuatorType)
        {
            case LEAN:
                ConnectFirst();
                break;
            case LIFT:
                ConnectFirst();
                break;
            case TILT:
                ConnectFirst();
                break;
            case TURN:
                ConnectFirst();
                break;
        }
    }

    void ConnectFirst()
    {
        switch(CURRENT_STATE) {

            case PING: {
                CURRENT_STATE = ActuatorAttachStates.STATES.VALIDATE_PING;
                WaitingForKineticsRequestAck = KineticComms.Instance.StartMotion();
                break;
            }
            case VALIDATE_PING: {
                if (LastKineticsResponse.size() == 1 && LastKineticsResponse.get(0).ResponseType == CommandLabels.CommandTypes.TRG) {
                    CURRENT_STATE = ActuatorAttachStates.STATES.IS_Actuator_CONNECTED;
                    super.delegate.notify_NextStep(ID);
                }
                break;
            }

            case IS_Actuator_CONNECTED: {
                CURRENT_STATE = ActuatorAttachStates.STATES.CONNECT_Actuator;
                WaitingForKineticsRequestAck = KineticComms.Instance.ReadActuatorPowerStatus(actuatorType);
                break;

            }
            case CONNECT_Actuator: {
                if (KineticComms.Instance.CheckActuatorPowerStatus(LastKineticsResponse)) {
                    CURRENT_STATE = ActuatorAttachStates.STATES.IS_Actuator_ATTACHED;
                    super.delegate.notify_NextStep(ID);
                } else {

                    CURRENT_STATE = ActuatorAttachStates.STATES.IS_Actuator_ATTACHED;
                    WaitingForKineticsRequestAck = KineticComms.Instance.PowerOnMotor(actuatorType);
                }
                break;
            }
            case IS_Actuator_ATTACHED: {
                CURRENT_STATE = ActuatorAttachStates.STATES.READ_DEGREE_Actuator;
                WaitingForKineticsRequestAck = KineticComms.Instance.ReadActuatorSignalStatus(actuatorType);
                break;
            }
            case READ_DEGREE_Actuator: {
                if (KineticComms.Instance.CheckActuatorSignalStatus(LastKineticsResponse)) {
                    kineticsRequest.clear();
                    CURRENT_STATE = ActuatorAttachStates.STATES.TRIGEGER_ANGLES;
                    super.delegate.notify_NextStep(ID);
                } else {
                    kineticsRequest.addAll(new ArrayList<KineticsRequest>() {{
                        add(new KineticsRequestTiming(100, actuatorType));
                        add(new KineticsRequestDelay(0, actuatorType));
                    }});
                    CURRENT_STATE = ActuatorAttachStates.STATES.SET_DEGREE_Actuator;
                    WaitingForKineticsRequestAck = KineticComms.Instance.ReadDegree(actuatorType);
                }
                break;
            }
            case SET_DEGREE_Actuator: {
                CURRENT_STATE = ActuatorAttachStates.STATES.TRIGEGER_ANGLES;

                final KineticsRequest request = KineticComms.Instance.GetKineticRequestAngleFromDegreeResponse(actuatorType, LastKineticsResponse);

                if (request != null) {
                    WaitingForKineticsRequestAck = KineticComms.Instance.SetParameters(
                            new ArrayList<KineticsRequest>() {{
                                add(new KineticsRequestServoEasing(CommandLabels.EasingFunction.QAD, actuatorType));
                                add(new KineticsRequestEasingInOut(CommandLabels.EasingType.IN, actuatorType));
                                add(request);
                            }});
                }

                break;
            }
            case TRIGEGER_ANGLES: {
                CURRENT_STATE = ActuatorAttachStates.STATES.TRIGEGER_ANGLES_CONFIRM;

                ArrayList<KineticsRequest> _TriggerAngles = new ArrayList<KineticsRequest>();

                _TriggerAngles.addAll(kineticsRequest);

                if (_TriggerAngles.size() > 0) {
                    _TriggerAngles.add(new KineticsRequestTrigger());
                    WaitingForKineticsRequestAck = KineticComms.Instance.SetParameters(
                            _TriggerAngles);
                } else {
                    super.delegate.notify_NextStep(ID);
                }

                break;
            }
            case TRIGEGER_ANGLES_CONFIRM: {
                CURRENT_STATE = ActuatorAttachStates.STATES.ATTACH_Actuator;

                ArrayList<KineticsRequest> _TriggerAngles = new ArrayList<KineticsRequest>();


                    _TriggerAngles.add(new KineticsRequestTrigger());
                    WaitingForKineticsRequestAck = KineticComms.Instance.SetParameters(
                            _TriggerAngles);


                break;
            }

            case ATTACH_Actuator: {
                CURRENT_STATE = ActuatorAttachStates.STATES.Finish;
                if (kineticsRequest.size() > 0) {
                    WaitingForKineticsRequestAck = KineticComms.Instance.AttachSignalToActuator(
                            actuatorType);
                } else {
                    super.delegate.notify_NextStep(ID);
                }
                break;

            }
            case Finish:
                Pause();
                break;
            default:
                break;
        }

    }

    void ReadFirst()
    {
        switch(CURRENT_STATE) {

            case PING: {
                CURRENT_STATE = ActuatorAttachStates.STATES.VALIDATE_PING;
                WaitingForKineticsRequestAck = KineticComms.Instance.StartMotion();
                break;
            }
            case VALIDATE_PING: {
                if (LastKineticsResponse.size() == 1 && LastKineticsResponse.get(0).ResponseType == CommandLabels.CommandTypes.TRG) {
                    CURRENT_STATE = ActuatorAttachStates.STATES.IS_Actuator_CONNECTED;
                    super.delegate.notify_NextStep(ID);
                }
                break;
            }
            case IS_Actuator_CONNECTED: {
                CURRENT_STATE = ActuatorAttachStates.STATES.IS_Actuator_ATTACHED;
                WaitingForKineticsRequestAck = KineticComms.Instance.ReadActuatorPowerStatus(actuatorType);
                break;

            }
            case IS_Actuator_ATTACHED: {
                if (KineticComms.Instance.CheckActuatorPowerStatus(LastKineticsResponse)) {
                    CURRENT_STATE = ActuatorAttachStates.STATES.Finish;
                    super.delegate.notify_NextStep(ID);
                }
                else {
                    CURRENT_STATE = ActuatorAttachStates.STATES.ATTACH_Actuator;
                    WaitingForKineticsRequestAck = KineticComms.Instance.ReadActuatorSignalStatus(actuatorType);
                }
                break;
            }
            case ATTACH_Actuator: {
                if (KineticComms.Instance.CheckActuatorSignalStatus(LastKineticsResponse)) {
                    kineticsRequest.clear();
                    CURRENT_STATE = ActuatorAttachStates.STATES.CONNECT_Actuator;
                    super.delegate.notify_NextStep(ID);
                } else {
                    CURRENT_STATE = ActuatorAttachStates.STATES.DUMMY_READ_DEGREE_Actuator;
                    WaitingForKineticsRequestAck = KineticComms.Instance.AttachSignalToActuator(
                            actuatorType);
//                    if (kineticsRequest.size() > 0) {
//
//                    } else {
//                        super.delegate.notify_NextStep(ID);
//                    }
                }
                break;
            }

            case DUMMY_READ_DEGREE_Actuator:
                 {
                    CURRENT_STATE = ActuatorAttachStates.STATES.READ_DEGREE_Actuator;
                    WaitingForKineticsRequestAck = KineticComms.Instance.ReadDegree(actuatorType);
                }
                break;
            case READ_DEGREE_Actuator: {


                    kineticsRequest.addAll(new ArrayList<KineticsRequest>() {{
                        add(new KineticsRequestTiming(100, actuatorType));
                        add(new KineticsRequestDelay(0, actuatorType));
                    }});
                    CURRENT_STATE = ActuatorAttachStates.STATES.SET_DEGREE_Actuator;
                    WaitingForKineticsRequestAck = KineticComms.Instance.ReadDegree(actuatorType);

                break;
            }
            case SET_DEGREE_Actuator: {
                CURRENT_STATE = ActuatorAttachStates.STATES.TRIGEGER_ANGLES;

                final KineticsRequest request = KineticComms.Instance.GetKineticRequestAngleFromDegreeResponse(actuatorType, LastKineticsResponse);

                if (request != null) {
                    WaitingForKineticsRequestAck = KineticComms.Instance.SetParameters(
                            new ArrayList<KineticsRequest>() {{
                                add(new KineticsRequestServoEasing(CommandLabels.EasingFunction.QAD, actuatorType));
                                add(new KineticsRequestEasingInOut(CommandLabels.EasingType.IN, actuatorType));
                                add(request);
                            }});
                }

                break;
            }
            case TRIGEGER_ANGLES: {
                CURRENT_STATE = ActuatorAttachStates.STATES.TRIGEGER_ANGLES_CONFIRM;

                ArrayList<KineticsRequest> _TriggerAngles = new ArrayList<KineticsRequest>();

                _TriggerAngles.addAll(kineticsRequest);

                if (_TriggerAngles.size() > 0) {
                    _TriggerAngles.add(new KineticsRequestTrigger());
                    WaitingForKineticsRequestAck = KineticComms.Instance.SetParameters(
                            _TriggerAngles);
                } else {
                    super.delegate.notify_NextStep(ID);
                }

                break;
            }
            case TRIGEGER_ANGLES_CONFIRM: {
                CURRENT_STATE = ActuatorAttachStates.STATES.CONNECT_Actuator;

                ArrayList<KineticsRequest> _TriggerAngles = new ArrayList<KineticsRequest>();


                    _TriggerAngles.add(new KineticsRequestTrigger());
                    WaitingForKineticsRequestAck = KineticComms.Instance.SetParameters(
                            _TriggerAngles);


                break;
            }


            case CONNECT_Actuator: {
                    CURRENT_STATE = ActuatorAttachStates.STATES.Finish;
                    WaitingForKineticsRequestAck = KineticComms.Instance.PowerOnMotor(actuatorType);

                break;
            }

            case Finish:
                Pause();
                break;
            default:
                break;
        }

    }

}
