package Framework.DataTypes.Parsers.ResponseCommandParser;

import android.os.Parcel;

import java.util.ArrayList;

import Framework.DataTypes.Constants.CommandLabels;
import Framework.DataTypes.Constants.KineticsResponseAcknowledgement;

public class KineticsResponsePowerOff extends KineticsResponse
        {

public KineticsResponsePowerOff(String response) {
        super(response);
        if(ResponseType == CommandLabels.CommandTypes.POFF)
        {
            ArrayList<String> RequestAckParts = DecomposedResponse.get(0);


        if(RequestAckParts.size() == 2)
        {
        RequestRecievedAck =  KineticsResponseAcknowledgement.ConvertFromString(RequestAckParts.get(1));
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
        }

        protected KineticsResponsePowerOff(Parcel in) {
                super(in);
        }

        public static final Creator<KineticsResponsePowerOff> CREATOR = new Creator<KineticsResponsePowerOff>() {
                @Override
                public KineticsResponsePowerOff createFromParcel(Parcel source) {
                        return new KineticsResponsePowerOff(source);
                }

                @Override
                public KineticsResponsePowerOff[] newArray(int size) {
                        return new KineticsResponsePowerOff[size];
                }
        };
        }

