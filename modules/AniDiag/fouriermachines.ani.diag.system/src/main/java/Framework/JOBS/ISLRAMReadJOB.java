package Framework.JOBS;

import android.os.ParcelUuid;

import java.util.ArrayList;
import java.util.UUID;

import Framework.DataTypes.Constants.EEPROM_READ_STATE;
import Framework.DataTypes.Constants.JOB_PRIORITY;
import Framework.DataTypes.Delegates.EEPromReadConvey;
import Framework.DataTypes.Delegates.ISLReadConvey;
import Framework.DataTypes.Delegates.JobConvey;
import Framework.DataTypes.Job;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponse;
import FrameworkInterface.InterfaceImplementation.KineticComms;
import FrameworkInterface.PublicTypes.Delegates.KineticsResponseConvey;
import FrameworkInterface.PublicTypes.EEPROMDetails;

public class ISLRAMReadJOB extends Job implements KineticsResponseConvey
{


    EEPROMDetails ReadDetails;
    ArrayList<Integer> DataBytes;
    ISLReadConvey ReadConvey;

    ParcelUuid WaitingForKineticsRequestAck = new ParcelUuid(UUID.randomUUID());
    ArrayList<KineticsResponse> LastKineticsResponse = new ArrayList<KineticsResponse>();

    EEPROM_READ_STATE.STATES CURRENT_STATE = EEPROM_READ_STATE.STATES.NA;

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

    public ISLRAMReadJOB(EEPROMDetails Details, ISLReadConvey delegate) {
        super();
        PRIORITY = JOB_PRIORITY.USER_ATTENTION;
        ReadDetails = Details;
        ReadConvey = delegate;
    }

    //Job Override
    @Override
    public  void TakeOverResources(JobConvey _delegate) {
        KineticComms.Instance.SetKineticsResposeListener(this);
        super.TakeOverResources( _delegate);
        CURRENT_STATE = EEPROM_READ_STATE.STATES.READ_REQUEST;
    }
    @Override
    public  void Pause() {

        if(CURRENT_STATE != EEPROM_READ_STATE.STATES.NA){
            CURRENT_STATE = EEPROM_READ_STATE.STATES.NA;

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

            case READ_REQUEST: {
                if (KineticComms.Instance.IsConnectedToMachine()) {
                    CURRENT_STATE = EEPROM_READ_STATE.STATES.EXTRACT_BYTES;
                    WaitingForKineticsRequestAck = KineticComms.Instance.ReadFromISLRAM(ReadDetails.Address, ReadDetails.NoOfBytes);

                } else {
                    CURRENT_STATE = EEPROM_READ_STATE.STATES.FINISH;
                    super.delegate.notify_NextStep(ID);
                }
            }
            break;
            case EXTRACT_BYTES: {
                DataBytes = KineticComms.Instance.ExtractBytesFromISLRAMReadResponse(LastKineticsResponse.get(0));

                CURRENT_STATE = EEPROM_READ_STATE.STATES.FINISH;
                super.delegate.notify_NextStep( ID);
                }
                break;
            case FINISH: {
                ReadConvey.ISLReadRecieved(ReadDetails, DataBytes);
                Pause();
            }
                break;
            default:
                break;
        }
    }
}
