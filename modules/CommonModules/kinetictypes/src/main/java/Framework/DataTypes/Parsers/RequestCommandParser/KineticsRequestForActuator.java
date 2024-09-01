//
//  KineticsRequestForActuator.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 06/05/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.RequestCommandParser;

import android.os.Parcel;

import Framework.DataTypes.Constants.CommandLabels;
import FrameworkInterface.PublicTypes.Constants.Actuator;

public class KineticsRequestForActuator extends KineticsRequest
{
   public Actuator ActuatorType;
    
    public KineticsRequestForActuator(CommandLabels.CommandTypes requestType)
    {
        super(requestType);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.ActuatorType == null ? -1 : this.ActuatorType.ordinal());
    }

    protected KineticsRequestForActuator(Parcel in) {
        super(in);
        int tmpActuatorType = in.readInt();
        this.ActuatorType = tmpActuatorType == -1 ? null : Actuator.values()[tmpActuatorType];
    }

}
