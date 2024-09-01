package Framework.JOBS;

import android.os.ParcelUuid;

import java.util.ArrayList;
import java.util.UUID;

import Framework.DataTypes.Constants.EEPROM_READ_STATE;
import Framework.DataTypes.Constants.EEPROM_WRITE_STATE;
import Framework.DataTypes.Constants.JOB_PRIORITY;
import Framework.DataTypes.Delegates.EEPromWriteConvey;
import Framework.DataTypes.Delegates.ISLReadConvey;
import Framework.DataTypes.Delegates.ISLWriteConvey;
import Framework.DataTypes.Delegates.JobConvey;
import Framework.DataTypes.Job;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponse;
import FrameworkInterface.InterfaceImplementation.KineticComms;
import FrameworkInterface.PublicTypes.Delegates.KineticsResponseConvey;
import FrameworkInterface.PublicTypes.EEPROMDetails;

public class ISLRAMWriteJOB extends Job implements KineticsResponseConvey
{


    EEPROMDetails WriteDetails;
    ArrayList<Integer> DataBytes;
    ISLWriteConvey WriteConvey;
    Integer Index=  0;

    ParcelUuid WaitingForKineticsRequestAck = new ParcelUuid(UUID.randomUUID());
    ArrayList<KineticsResponse> LastKineticsResponse = new ArrayList<KineticsResponse>();

    EEPROM_WRITE_STATE.STATES CURRENT_STATE = EEPROM_WRITE_STATE.STATES.NA;

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

    public  ISLRAMWriteJOB(EEPROMDetails Details, ISLWriteConvey delegate, ArrayList<Integer> Data) {
        super();
        PRIORITY = JOB_PRIORITY.USER_ATTENTION;
        WriteDetails = Details;
        WriteConvey = delegate;
        DataBytes = Data;
    }

    //Job Override
    @Override
    public  void TakeOverResources(JobConvey _delegate) {
        KineticComms.Instance.SetKineticsResposeListener(this);
        super.TakeOverResources( _delegate);
        CURRENT_STATE = EEPROM_WRITE_STATE.STATES.WRITE_REQUEST;
    }
    @Override
    public  void Pause() {

        if(CURRENT_STATE != EEPROM_WRITE_STATE.STATES.NA){
            CURRENT_STATE = EEPROM_WRITE_STATE.STATES.NA;

            KineticComms.Instance.SetKineticsResposeListener(null);

            super.Pause();
            super.delegate.notify_Finish( ID);
        }
    }
    @Override
    public  void Resume() {
        DOSTEP();
    }
//End Job Override

    public void MachiResponseTimeout(ArrayList<KineticsResponse> partialResponse, UUID _Acknowledgement) {

    }

    void DOSTEP()
    {
        switch(CURRENT_STATE) {

            case WRITE_REQUEST: {
                if (KineticComms.Instance.IsConnectedToMachine()) {
                    CURRENT_STATE = EEPROM_WRITE_STATE.STATES.CHECK_RESPONSE;
                    WaitingForKineticsRequestAck = KineticComms.Instance.WriteToISLRAM(WriteDetails.Address, 1 ,DataBytes.get(Index));
                } else {
                    CURRENT_STATE = EEPROM_WRITE_STATE.STATES.FINISH;
                    super.delegate.notify_NextStep(ID);
                }
            }
            break;
            case CHECK_RESPONSE: {
                if(KineticComms.Instance.CheckWriteToISLRAM(LastKineticsResponse.get(0))) {
                    Index++;
                    WriteDetails.Address++;
                    if(Index >= DataBytes.size())
                    {
                        CURRENT_STATE = EEPROM_WRITE_STATE.STATES.FINISH;
                        super.delegate.notify_NextStep(ID);
                    }
                    else
                    {
                        CURRENT_STATE = EEPROM_WRITE_STATE.STATES.WRITE_REQUEST;
                        super.delegate.notify_NextStep(ID);
                    }
                }
                else {
                    WriteConvey.ISLWriteStatus(false);
                    Pause();
                }
            }
            break;
            case FINISH: {
                WriteConvey.ISLWriteStatus(true);
                Pause();
            }
            break;
            default:
                break;
        }
    }
}
