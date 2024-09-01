//
//  KineticRequestEEPROMRead.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 07/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.RequestCommandParser;

import android.os.Parcel;

import java.util.ArrayList;

import Framework.DataTypes.Constants.CommandLabels;
import FrameworkInterface.PublicTypes.Config.MachineConfig;
import FrameworkInterface.PublicTypes.EEPROMDetails;

public class KineticsRequestEEPROMRead extends KineticsRequest
{
    EEPROMDetails MemoryLocation;
    Integer NoOfBytes;
    Integer Address;

    public void PostConstructor()
    {
        String Command = super.formCommand(new String[]{(Address.toString()), (NoOfBytes.toString())});

        Request = super.addDelimiters( Command);
    }

    public KineticsRequestEEPROMRead(EEPROMDetails memoryLocation)
    {
        super(CommandLabels.CommandTypes.EEPR);
        
        NoOfBytes = memoryLocation.NoOfBytes;
        Address = memoryLocation.Address;

        PostConstructor();
    }
    
    public KineticsRequestEEPROMRead(String command)
    {
        super(CommandLabels.CommandTypes.EEPR);
        String _command = super.removeDelimiters(command);
        ArrayList<String> contents = super.decomposeCommand( _command);
        if(contents.size() == 2)
        {
            Address = Integer.parseInt(contents.get(1));
            NoOfBytes = Integer.parseInt(contents.get(2));
            
            MemoryLocation = new EEPROMDetails(Address, NoOfBytes);
            
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
        dest.writeValue(this.NoOfBytes);
        dest.writeValue(this.Address);
    }

    protected KineticsRequestEEPROMRead(Parcel in) {
        super(in);
        this.MemoryLocation = in.readParcelable(EEPROMDetails.class.getClassLoader());
        this.NoOfBytes = (Integer) in.readValue(Integer.class.getClassLoader());
        this.Address = (Integer) in.readValue(Integer.class.getClassLoader());

        PostConstructor();
    }

    public static final Creator<KineticsRequestEEPROMRead> CREATOR = new Creator<KineticsRequestEEPROMRead>() {
        @Override
        public KineticsRequestEEPROMRead createFromParcel(Parcel source) {
            return new KineticsRequestEEPROMRead(source);
        }

        @Override
        public KineticsRequestEEPROMRead[] newArray(int size) {
            return new KineticsRequestEEPROMRead[size];
        }
    };
}
