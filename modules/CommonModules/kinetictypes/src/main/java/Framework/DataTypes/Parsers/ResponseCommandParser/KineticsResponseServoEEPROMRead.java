package Framework.DataTypes.Parsers.ResponseCommandParser;

import android.os.Parcel;
import android.util.Log;

import java.util.ArrayList;

import Framework.DataTypes.Constants.CommandLabels;
import Framework.DataTypes.Constants.KineticsResponseAcknowledgement;
import FrameworkInterface.PublicTypes.Config.MachineConfig;
import FrameworkInterface.PublicTypes.Constants.Actuator;
import FrameworkInterface.PublicTypes.EEPROMDetails;

//SEPR#EPRADR#COUNT#O:SEPR#SADR#EPRADR#COUNT#DATA1#DATA2#..#DATAn:
public class KineticsResponseServoEEPROMRead extends  KineticsResponse
{
    EEPROMDetails MemoryLocation;
    public ArrayList<Integer> Data = new ArrayList<Integer>();

    public KineticsResponseServoEEPROMRead(String response) {
        super(response);
        if(ResponseType == CommandLabels.CommandTypes.SEPR)
        {
            ArrayList<String> RequestParts = DecomposedResponse.get(0);
            ArrayList<String> ResponseParts = DecomposedResponse.get(1);

            if(RequestParts.size() == 4)
            {
                RequestRecievedAck =  KineticsResponseAcknowledgement.ConvertFromString(RequestParts.get(3));
            }

            if(RequestRecievedAck != null && RequestRecievedAck == KineticsResponseAcknowledgement.OK)
            {
                if(ResponseParts.size() >= 4)
                {


                    Integer Address = Integer.parseInt(ResponseParts.get(1));
                    Integer ByteCount = Integer.parseInt(ResponseParts.get(2));

                    if(Address != null && ByteCount != null){
                        MemoryLocation = new EEPROMDetails(Address, ByteCount);

                        if(MemoryLocation.NoOfBytes > 0 && ResponseParts.size() == 3+MemoryLocation.NoOfBytes)
                        {
                            for(int index=0; index< MemoryLocation.NoOfBytes; index++)
                            {
                                try {
                                    Data.add(Integer.parseInt(ResponseParts.get(3 + index), 16));
                                }
                                catch (Exception e)
                                {
                                    Log.e("KiResServoEEPROMRead", "Data Integet parsing failed.. setting value zero: ", e);
                                    Data.add(0);
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.MemoryLocation, flags);
        dest.writeList(this.Data);
    }

    protected KineticsResponseServoEEPROMRead(Parcel in) {
        super(in);
        this.MemoryLocation = in.readParcelable(EEPROMDetails.class.getClassLoader());
        this.Data = new ArrayList<Integer>();
        in.readList(this.Data, Integer.class.getClassLoader());
    }

    public static final Creator<KineticsResponseServoEEPROMRead> CREATOR = new Creator<KineticsResponseServoEEPROMRead>() {
        @Override
        public KineticsResponseServoEEPROMRead createFromParcel(Parcel source) {
            return new KineticsResponseServoEEPROMRead(source);
        }

        @Override
        public KineticsResponseServoEEPROMRead[] newArray(int size) {
            return new KineticsResponseServoEEPROMRead[size];
        }
    };
}
