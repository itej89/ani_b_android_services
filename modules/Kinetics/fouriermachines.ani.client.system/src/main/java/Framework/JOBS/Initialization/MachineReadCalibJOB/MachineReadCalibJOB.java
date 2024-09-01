package Framework.JOBS.Initialization.MachineReadCalibJOB;

import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

import Framework.DataTypes.Constants.CommandLabels;
import Framework.DataTypes.Constants.JOB_PRIORITY;
import Framework.DataTypes.Delegates.JobConvey;
import Framework.DataTypes.GlobalContext;
import Framework.DataTypes.Job;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequest;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestDelay;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestEasingInOut;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestServoEasing;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestTiming;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestTrigger;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponse;
import Framework.JOBS.Initialization.MachineBindJOB.BIND_STATES;
import Framework.JOBS.Initialization.MachineBindJOB.MachineBindStatusDelegate;
import Framework.SystemEventHandlers.UIMAINModuleHandler;
import FrameworkInterface.InterfaceImplementation.KineticComms;
import FrameworkInterface.PublicTypes.Config.MachineConfig;
import FrameworkInterface.PublicTypes.Constants.Actuator;
import FrameworkInterface.PublicTypes.Delegates.KineticsResponseConvey;
import FrameworkInterface.PublicTypes.EEPROMDetails;

public class MachineReadCalibJOB extends Job implements KineticsResponseConvey {

    ParcelUuid WaitingForKineticsRequestAck = new ParcelUuid(UUID.randomUUID());
    ArrayList<KineticsResponse> LastKineticsResponse = new ArrayList<KineticsResponse>();
    MachineReadCalibStatusDelegate ReadCalibStatusConvey;
    ArrayList<Integer> TurnEEPROMCalib = new ArrayList<Integer>();
    ArrayList<Integer> LeanEEPROMCalib = new ArrayList<Integer>();
    ArrayList<Integer> LiftEEPROMCalib = new ArrayList<Integer>();
    ArrayList<Integer> TiltEEPROMCalib = new ArrayList<Integer>();

    //Number of Degree data to read from each actuator
     Integer TotalDegreeCount = 180;

     //NUMBER OF CHARACTERS OF ADC VALUE PER EACH DEGREE OF ACTUATOR IN EEPROM
    Integer ServoCalibDegreeCharacterCount = 3;

    //KineticsResponseConvey delegate
    public void CommsLost() {

        super.delegate.notify_LostResource( ID);
    }

    public void MachineResponseRecieved(ArrayList<KineticsResponse> responeData, UUID _Acknowledgement) {

        Log.e("MachineReadCalibJOB", "Machine Response recieved");
        Log.e("MachineReadCalibJOB", "WaitingForKineticsRequestAck : "+WaitingForKineticsRequestAck.toString());
        Log.e("MachineReadCalibJOB", "_Acknowledgement : "+_Acknowledgement.toString());
        if(_Acknowledgement.toString().equals(WaitingForKineticsRequestAck.toString())){
            Log.e("MachineReadCalibJOB", "Ack Matched");
            WaitingForKineticsRequestAck = new ParcelUuid(UUID.randomUUID());
            Log.e("MachineReadCalibJOB", "Ack Matched - WaitingForKineticsRequestAck : "+WaitingForKineticsRequestAck.toString());
            LastKineticsResponse = responeData;
            super.delegate.notify_NextStep( ID);

        }
    }
    //End KineticsResponseConvey delegate


    BIND_STATES.STATES CURRENT_STATE = BIND_STATES.STATES.NA;
    public MachineReadCalibJOB(MachineReadCalibStatusDelegate bindStateConvey) {
        super();
        ReadCalibStatusConvey = bindStateConvey;
        PRIORITY = JOB_PRIORITY.USER_CRITICAL;
        ReadCalibStatusConvey.ReadCalibStateChanged(BIND_STATES.STATES.NA);
    }

    //Job Override
    @Override
    public void TakeOverResources(JobConvey _delegate) {
        Log.e("MachineReadCalibJOB", "TakeOverResources - WaitingForKineticsRequestAck : "+WaitingForKineticsRequestAck.toString());

        KineticComms.Instance.SetKineticsResposeListener(this);
        super.TakeOverResources(_delegate);
        CURRENT_STATE = BIND_STATES.STATES.PING;
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
        Log.e("MachineReadCalibJOB", "MachiResponseTimeout");
        //Retry if any communication fail
        CURRENT_STATE = BIND_STATES.STATES.PING;
        DOSTEP();
    }

    void DOSTEP()
    {
        switch(CURRENT_STATE) {

            case PING: {
                CURRENT_STATE = BIND_STATES.STATES.VALIDATE_PING;
                WaitingForKineticsRequestAck = KineticComms.Instance.StartInstantMotion(GlobalContext.context.getPackageName());
                Log.e("MachineReadCalibJOB", "PING - WaitingForKineticsRequestAck : "+WaitingForKineticsRequestAck);

                break;
            }
            case VALIDATE_PING: {
                if (LastKineticsResponse.size() == 1 && LastKineticsResponse.get(0).ResponseType == CommandLabels.CommandTypes.ITRG) {
                    CURRENT_STATE = BIND_STATES.STATES.SET_NO_ATTENTION;
                    super.delegate.notify_NextStep(ID);
                }
                break;
            }
            case SET_NO_ATTENTION:
                CURRENT_STATE = BIND_STATES.STATES.READ_TURN_ADR;
                WaitingForKineticsRequestAck = KineticComms.Instance.IndicateNoAttention(GlobalContext.context.getPackageName());
                Log.e("MachineReadCalibJOB", "SET_NO_ATTENTION - WaitingForKineticsRequestAck : "+WaitingForKineticsRequestAck);

                break;
            case READ_TURN_ADR: {
                CURRENT_STATE = BIND_STATES.STATES.SET_TURN_ADR;
                WaitingForKineticsRequestAck = KineticComms.Instance.ReadActuatorAddress(GlobalContext.context.getPackageName(), Actuator.TURN);
                Log.e("MachineReadCalibJOB", "READ_TURN_ADR - WaitingForKineticsRequestAck : "+WaitingForKineticsRequestAck);

                break;
            }
            case SET_TURN_ADR: {
                CURRENT_STATE = BIND_STATES.STATES.READ_LIFT_ADR;
                if (KineticComms.Instance.SetActuatorAddress(Actuator.TURN, LastKineticsResponse)) {
                    super.delegate.notify_NextStep(ID);
                }
                break;
            }
            case READ_LIFT_ADR: {
                CURRENT_STATE = BIND_STATES.STATES.SET_LIFT_ADR;
                WaitingForKineticsRequestAck = KineticComms.Instance.ReadActuatorAddress(GlobalContext.context.getPackageName(), Actuator.LIFT);
                Log.e("MachineReadCalibJOB", "READ_LIFT_ADR - WaitingForKineticsRequestAck : "+WaitingForKineticsRequestAck);

                break;
            }
            case SET_LIFT_ADR: {
                CURRENT_STATE = BIND_STATES.STATES.READ_LEAN_ADR;
                if (KineticComms.Instance.SetActuatorAddress(Actuator.LIFT, LastKineticsResponse)) {
                    super.delegate.notify_NextStep(ID);
                }
                break;
            }
            case READ_LEAN_ADR: {
                CURRENT_STATE = BIND_STATES.STATES.SET_LEAN_ADR;
                WaitingForKineticsRequestAck = KineticComms.Instance.ReadActuatorAddress(GlobalContext.context.getPackageName(), Actuator.LEAN); Log.e("MachineReadCalibJOB", "READ_LIFT_ADR - WaitingForKineticsRequestAck : "+WaitingForKineticsRequestAck);

                break;
            }
            case SET_LEAN_ADR: {
                CURRENT_STATE = BIND_STATES.STATES.READ_TILT_ADR;
                if (KineticComms.Instance.SetActuatorAddress(Actuator.LEAN, LastKineticsResponse)) {
                    super.delegate.notify_NextStep(ID);
                }
                break;
            }
            case READ_TILT_ADR: {
                CURRENT_STATE = BIND_STATES.STATES.SET_TILT_ADR;
                WaitingForKineticsRequestAck = KineticComms.Instance.ReadActuatorAddress(GlobalContext.context.getPackageName(), Actuator.TILT);
                break;
            }
            case SET_TILT_ADR: {
                CURRENT_STATE = BIND_STATES.STATES.READ_TURN_REF;
                if (KineticComms.Instance.SetActuatorAddress(Actuator.TILT, LastKineticsResponse)) {
                    super.delegate.notify_NextStep(ID);
                }
                break;
            }

            case READ_TURN_REF: {
                CURRENT_STATE = BIND_STATES.STATES.SET_TURN_REF;
                WaitingForKineticsRequestAck = KineticComms.Instance.ReadReferanceAngle(GlobalContext.context.getPackageName(), Actuator.TURN);
                break;
            }
            case SET_TURN_REF: {
                CURRENT_STATE = BIND_STATES.STATES.READ_LIFT_REF;
                if (KineticComms.Instance.SetReferanceAngle(Actuator.TURN, LastKineticsResponse)) {
                    super.delegate.notify_NextStep(ID);
                }
                break;
            }
            case READ_LIFT_REF: {
                CURRENT_STATE = BIND_STATES.STATES.SET_LIFT_REF;
                WaitingForKineticsRequestAck = KineticComms.Instance.ReadReferanceAngle(GlobalContext.context.getPackageName(), Actuator.LIFT);
                break;
            }
            case SET_LIFT_REF: {
                CURRENT_STATE = BIND_STATES.STATES.READ_LEAN_REF;
                if (KineticComms.Instance.SetReferanceAngle(Actuator.LIFT, LastKineticsResponse)) {
                    super.delegate.notify_NextStep(ID);
                }
                break;
            }
            case READ_LEAN_REF: {
                CURRENT_STATE = BIND_STATES.STATES.SET_LEAN_REF;
                WaitingForKineticsRequestAck = KineticComms.Instance.ReadReferanceAngle(GlobalContext.context.getPackageName(), Actuator.LEAN);
                break;
            }
            case SET_LEAN_REF: {
                CURRENT_STATE = BIND_STATES.STATES.READ_TILT_REF;
                if (KineticComms.Instance.SetReferanceAngle(Actuator.LEAN, LastKineticsResponse)) {
                    super.delegate.notify_NextStep(ID);
                }
                break;
            }
            case READ_TILT_REF: {
                CURRENT_STATE = BIND_STATES.STATES.SET_TILT_REF;
                WaitingForKineticsRequestAck = KineticComms.Instance.ReadReferanceAngle(GlobalContext.context.getPackageName(), Actuator.TILT);
                break;
            }
            case SET_TILT_REF: {
                CURRENT_STATE = BIND_STATES.STATES.READ_TURN_DELTA_ANGLE;
                if (KineticComms.Instance.SetReferanceAngle(Actuator.TILT, LastKineticsResponse)) {
                    super.delegate.notify_NextStep(ID);
                }
                break;
            }
            case READ_TURN_DELTA_ANGLE: {
                CURRENT_STATE = BIND_STATES.STATES.SET_TURN_DELTA_ANGLE;
                WaitingForKineticsRequestAck = KineticComms.Instance.ReadDeltaResetAngle(GlobalContext.context.getPackageName(), Actuator.TURN);
                break;
            }
            case SET_TURN_DELTA_ANGLE: {
                CURRENT_STATE = BIND_STATES.STATES.READ_LIFT_DELTA_ANGLE;
                if (KineticComms.Instance.SetDeltaResetAngle(Actuator.TURN, LastKineticsResponse)) {
                    super.delegate.notify_NextStep(ID);
                }
                break;
            }
            case READ_LIFT_DELTA_ANGLE: {
                CURRENT_STATE = BIND_STATES.STATES.SET_LIFT_DELTA_ANGLE;
                WaitingForKineticsRequestAck = KineticComms.Instance.ReadDeltaResetAngle(GlobalContext.context.getPackageName(), Actuator.LIFT);
                break;
            }
            case SET_LIFT_DELTA_ANGLE: {
                CURRENT_STATE = BIND_STATES.STATES.READ_LEAN_DELTA_ANGLE;
                if (KineticComms.Instance.SetDeltaResetAngle(Actuator.LIFT, LastKineticsResponse)) {
                    super.delegate.notify_NextStep(ID);
                }
                break;
            }
            case READ_LEAN_DELTA_ANGLE: {
                CURRENT_STATE = BIND_STATES.STATES.SET_LEAN_DELTA_ANGLE;
                WaitingForKineticsRequestAck = KineticComms.Instance.ReadDeltaResetAngle(GlobalContext.context.getPackageName(), Actuator.LEAN);
                break;
            }
            case SET_LEAN_DELTA_ANGLE: {
                CURRENT_STATE = BIND_STATES.STATES.READ_TILT_DELTA_ANGLE;
                if (KineticComms.Instance.SetDeltaResetAngle(Actuator.LEAN, LastKineticsResponse)) {
                    super.delegate.notify_NextStep(ID);
                }
                break;
            }
            case READ_TILT_DELTA_ANGLE: {
                CURRENT_STATE = BIND_STATES.STATES.SET_TILT_DELTA_ANGLE;
                WaitingForKineticsRequestAck = KineticComms.Instance.ReadDeltaResetAngle(GlobalContext.context.getPackageName(), Actuator.TILT);
                break;
            }
            case SET_TILT_DELTA_ANGLE: {
                CURRENT_STATE = BIND_STATES.STATES.READ_CALIB_FINISH;
                if (KineticComms.Instance.SetDeltaResetAngle(Actuator.TILT, LastKineticsResponse)) {
                    super.delegate.notify_NextStep(ID);
                }
                break;
            }
            case CLR_TURN_CALIB: {
                KineticComms.Instance.DeleteCalibrationDataForActuator(Actuator.TURN);
                CURRENT_STATE = BIND_STATES.STATES.CLR_LIFT_CALIB;
                super.delegate.notify_NextStep(ID);
                break;
            }
            case CLR_LIFT_CALIB: {
                KineticComms.Instance.DeleteCalibrationDataForActuator(Actuator.LIFT);
                CURRENT_STATE = BIND_STATES.STATES.CLR_LEAN_CALIB;
                super.delegate.notify_NextStep(ID);
                break;
            }
            case CLR_LEAN_CALIB: {
                KineticComms.Instance.DeleteCalibrationDataForActuator(Actuator.LEAN);
                CURRENT_STATE = BIND_STATES.STATES.CLR_TILT_CALIB;
                super.delegate.notify_NextStep(ID);
                break;
            }
            case CLR_TILT_CALIB: {
                KineticComms.Instance.DeleteCalibrationDataForActuator(Actuator.TILT);
                CURRENT_STATE = BIND_STATES.STATES.READ_TURN_CALIB;
                super.delegate.notify_NextStep(ID);
                break;
            }
            case READ_TURN_CALIB: {
                CURRENT_STATE = BIND_STATES.STATES.SET_TURN_CALIB;
                final EEPROMDetails details = new EEPROMDetails(TurnEEPROMCalib.size(), ServoCalibDegreeCharacterCount);

                WaitingForKineticsRequestAck = KineticComms.Instance.ReadFromServoEEPROM(GlobalContext.context.getPackageName(), Actuator.TURN, details);
                break;
            }
            case SET_TURN_CALIB: {
              ArrayList<Integer>  DataBytes = KineticComms.Instance.ExtractBytesFromServoEEPROMReadResponse(LastKineticsResponse.get(0));
              TurnEEPROMCalib.addAll(DataBytes);

                if(TurnEEPROMCalib.size() == TotalDegreeCount * 3)
                {
                    CURRENT_STATE = BIND_STATES.STATES.READ_LIFT_CALIB;
                }
                else
                {
                    CURRENT_STATE = BIND_STATES.STATES.READ_TURN_CALIB;
                }
                super.delegate.notify_NextStep(ID);
                break;
            }
            case READ_LIFT_CALIB: {
                CURRENT_STATE = BIND_STATES.STATES.SET_LIFT_CALIB;
                final EEPROMDetails details = new EEPROMDetails(LiftEEPROMCalib.size(), ServoCalibDegreeCharacterCount);

                WaitingForKineticsRequestAck = KineticComms.Instance.ReadFromServoEEPROM(GlobalContext.context.getPackageName(), Actuator.LIFT, details);
                break;
            }
            case SET_LIFT_CALIB: {
                ArrayList<Integer>  DataBytes = KineticComms.Instance.ExtractBytesFromServoEEPROMReadResponse(LastKineticsResponse.get(0));
                LiftEEPROMCalib.addAll(DataBytes);

                if(LiftEEPROMCalib.size() == TotalDegreeCount * 3)
                {
                    CURRENT_STATE = BIND_STATES.STATES.READ_LEAN_CALIB;
                }
                else
                {
                    CURRENT_STATE = BIND_STATES.STATES.READ_LIFT_CALIB;
                }
                super.delegate.notify_NextStep(ID);
                break;
            }
            case READ_LEAN_CALIB: {
                CURRENT_STATE = BIND_STATES.STATES.SET_LEAN_CALIB;
                final EEPROMDetails details = new EEPROMDetails(LeanEEPROMCalib.size(), ServoCalibDegreeCharacterCount);

                WaitingForKineticsRequestAck = KineticComms.Instance.ReadFromServoEEPROM(GlobalContext.context.getPackageName(), Actuator.LEAN, details);
                break;
            }
            case SET_LEAN_CALIB: {
                ArrayList<Integer>  DataBytes = KineticComms.Instance.ExtractBytesFromServoEEPROMReadResponse(LastKineticsResponse.get(0));
                LeanEEPROMCalib.addAll(DataBytes);

                if(LeanEEPROMCalib.size() == TotalDegreeCount * 3)
                {
                    CURRENT_STATE = BIND_STATES.STATES.READ_TILT_CALIB;
                }
                else
                {
                    CURRENT_STATE = BIND_STATES.STATES.READ_LEAN_CALIB;
                }
                super.delegate.notify_NextStep(ID);
                break;
            }
            case READ_TILT_CALIB: {
                CURRENT_STATE = BIND_STATES.STATES.SET_TILT_CALIB;
                final EEPROMDetails details = new EEPROMDetails(TiltEEPROMCalib.size(), ServoCalibDegreeCharacterCount);

                WaitingForKineticsRequestAck = KineticComms.Instance.ReadFromServoEEPROM(GlobalContext.context.getPackageName(), Actuator.TILT, details);
                break;
            }
            case SET_TILT_CALIB: {
                ArrayList<Integer>  DataBytes = KineticComms.Instance.ExtractBytesFromServoEEPROMReadResponse(LastKineticsResponse.get(0));
                TiltEEPROMCalib.addAll(DataBytes);

                if(TiltEEPROMCalib.size() == TotalDegreeCount * 3)
                {
                    CURRENT_STATE = BIND_STATES.STATES.READ_CALIB_FINISH;
                }
                else
                {
                    CURRENT_STATE = BIND_STATES.STATES.READ_TILT_CALIB;
                }
                super.delegate.notify_NextStep(ID);
                break;
            }
            case READ_CALIB_FINISH:
                for(int i=0; i<TurnEEPROMCalib.size(); i=i+3)
                {
                    String Label = String.valueOf(i/3);
                    String Data = String.valueOf(TurnEEPROMCalib.get(i))+String.valueOf(TurnEEPROMCalib.get(i+1))+String.valueOf(TurnEEPROMCalib.get(i+2));

                    KineticComms.Instance.SaveActuatorCalibrationData(Actuator.TURN, Integer.parseInt(Label),Integer.parseInt(Data));
                }

                for(int i=0; i<LiftEEPROMCalib.size(); i=i+3)
                {
                    String Label = String.valueOf(i/3);
                    String Data = String.valueOf(LiftEEPROMCalib.get(i))+String.valueOf(LiftEEPROMCalib.get(i+1))+String.valueOf(LiftEEPROMCalib.get(i+2));

                    KineticComms.Instance.SaveActuatorCalibrationData(Actuator.LIFT, Integer.parseInt(Label),Integer.parseInt(Data));
                }

                for(int i=0; i<LeanEEPROMCalib.size(); i=i+3)
                {
                    String Label = String.valueOf(i/3);
                    String Data = String.valueOf(LeanEEPROMCalib.get(i))+String.valueOf(LeanEEPROMCalib.get(i+1))+String.valueOf(LeanEEPROMCalib.get(i+2));

                    KineticComms.Instance.SaveActuatorCalibrationData(Actuator.LEAN, Integer.parseInt(Label),Integer.parseInt(Data));
                }

                for(int i=0; i<TiltEEPROMCalib.size(); i=i+3)
                {
                    String Label = String.valueOf(i/3);
                    String Data = String.valueOf(TiltEEPROMCalib.get(i))+String.valueOf(TiltEEPROMCalib.get(i+1))+String.valueOf(TiltEEPROMCalib.get(i+2));

                    KineticComms.Instance.SaveActuatorCalibrationData(Actuator.TILT, Integer.parseInt(Label),Integer.parseInt(Data));
                }

                Pause();
                notify_CalibState(BIND_STATES.STATES.READ_CALIB_FINISH);
                break;

            default:
                break;
        }

    }

    void notify_CalibState(BIND_STATES.STATES _BindState)
    {
        if(ReadCalibStatusConvey != null)
        {
            ReadCalibStatusConvey.ReadCalibStateChanged(_BindState);
        }
    }

}
