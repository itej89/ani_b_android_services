//
//  KineticsResponseISLEEPROMRead.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 07/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.ResponseCommandParser;

import android.os.Parcel;

import java.util.ArrayList;

import Framework.DataTypes.Constants.CommandLabels;
import Framework.DataTypes.Constants.KineticsResponseAcknowledgement;
import FrameworkInterface.PublicTypes.Config.MachineConfig;
import FrameworkInterface.PublicTypes.Constants.Actuator;
import FrameworkInterface.PublicTypes.EEPROMDetails;


//ISLER#ADR#O:ISLER#ADR#COUNT#DATA1#DATA2#..#DATAn:
public class KineticsResponseISLEEPROMRead extends  KineticsResponse
{
    EEPROMDetails MemoryLocation;
    public ArrayList<Integer> Data = new     ArrayList<Integer>();

    public KineticsResponseISLEEPROMRead(String response) {
        super(response);
        if(ResponseType == CommandLabels.CommandTypes.ISLER)
        {
            ArrayList<String> RequestParts = DecomposedResponse.get(0);
            ArrayList<String> ResponseParts = DecomposedResponse.get(1);

            if(RequestParts.size() == 3)
            {
                RequestRecievedAck =  KineticsResponseAcknowledgement.ConvertFromString(RequestParts.get(2));
            }

            if(RequestRecievedAck != null && RequestRecievedAck == KineticsResponseAcknowledgement.OK)
            {
                if(ResponseParts.size() >= 3)
                {
                    Integer Address = Integer.parseInt(ResponseParts.get(1));
                    Integer ByteCount = Integer.parseInt(ResponseParts.get(2));

                    if(Address != null && ByteCount != null){
                        MemoryLocation = new EEPROMDetails(Address,  ByteCount);

                        if(MemoryLocation.NoOfBytes > 0 && ResponseParts.size() == 3+MemoryLocation.NoOfBytes)
                        {
                            for(int index=0; index<MemoryLocation.NoOfBytes;index++)
                            {
                                Data.add(Integer.parseInt(ResponseParts.get(3 + index), 16));
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

    protected KineticsResponseISLEEPROMRead(Parcel in) {
        super(in);
        this.MemoryLocation = in.readParcelable(EEPROMDetails.class.getClassLoader());
        this.Data = new ArrayList<Integer>();
        in.readList(this.Data, Integer.class.getClassLoader());
    }

    public static final Creator<KineticsResponseISLEEPROMRead> CREATOR = new Creator<KineticsResponseISLEEPROMRead>() {
        @Override
        public KineticsResponseISLEEPROMRead createFromParcel(Parcel source) {
            return new KineticsResponseISLEEPROMRead(source);
        }

        @Override
        public KineticsResponseISLEEPROMRead[] newArray(int size) {
            return new KineticsResponseISLEEPROMRead[size];
        }
    };
}
