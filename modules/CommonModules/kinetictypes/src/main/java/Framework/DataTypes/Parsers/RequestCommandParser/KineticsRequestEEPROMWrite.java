//
//  KineticRequestEEPROMWrite.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 07/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.RequestCommandParser;

import android.os.Parcel;

import java.util.ArrayList;

import Framework.DataTypes.Constants.CommandLabels;
import FrameworkInterface.PublicTypes.EEPROMDetails;

public class KineticsRequestEEPROMWrite extends KineticsRequest
{
    EEPROMDetails MemoryLocation;
    Integer Value;


    public void PostConstructor()
    {
        String Command = super.formCommand(new String[]{(MemoryLocation.Address.toString()), (MemoryLocation.NoOfBytes.toString()), (Value.toString())});

        Request = super.addDelimiters(Command);
    }

    //noOfBytes should always be one
    public KineticsRequestEEPROMWrite(EEPROMDetails memoryLocation, Integer value)
    {
        super(CommandLabels.CommandTypes.EEPW);
        
        MemoryLocation = memoryLocation;
        Value = value;

        PostConstructor();
    }
    
    public KineticsRequestEEPROMWrite(String command)
    {
        super(CommandLabels.CommandTypes.EEPW);
        String _command = super.removeDelimiters( command);
        ArrayList<String> contents = super.decomposeCommand( _command);
        if(contents.size() == 3)
        {
            Integer Address = Integer.parseInt(contents.get(1));
            Integer NoOfBytes = Integer.parseInt(contents.get(2));
            
            MemoryLocation = new EEPROMDetails( Address,  NoOfBytes);
            
            Value = Integer.parseInt(contents.get(3));
            
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
        dest.writeValue(this.Value);
    }

    protected KineticsRequestEEPROMWrite(Parcel in) {
        super(in);
        this.MemoryLocation = in.readParcelable(EEPROMDetails.class.getClassLoader());
        this.Value = (Integer) in.readValue(Integer.class.getClassLoader());

        PostConstructor();
    }

    public static final Creator<KineticsRequestEEPROMWrite> CREATOR = new Creator<KineticsRequestEEPROMWrite>() {
        @Override
        public KineticsRequestEEPROMWrite createFromParcel(Parcel source) {
            return new KineticsRequestEEPROMWrite(source);
        }

        @Override
        public KineticsRequestEEPROMWrite[] newArray(int size) {
            return new KineticsRequestEEPROMWrite[size];
        }
    };
}
