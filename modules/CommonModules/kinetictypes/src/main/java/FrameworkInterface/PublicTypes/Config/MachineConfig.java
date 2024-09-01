package FrameworkInterface.PublicTypes.Config;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

import Framework.DataTypes.ActuatorCalibration;
import FrameworkInterface.PublicTypes.Constants.Actuator;
import FrameworkInterface.PublicTypes.Constants.KineticsEEPROM;
import fm.ani.client.db.DataTypes.CommandStore.Constants.CommandStore_Table_Columns;

/**
 * Created by tej on 24/06/18.
 */

public class MachineConfig implements Parcelable {


   public Map<Actuator, ActuatorCalibration> MachineActuatorList = new HashMap<Actuator, ActuatorCalibration>();

     MachineConfig()
    {

        ActuatorCalibration SERVO_TURN = new ActuatorCalibration(Actuator.TURN, KineticsEEPROM.EEPROMParameter.Referance_Angle_TURN, KineticsEEPROM.EEPROMParameter.SHURDOWN_DELTA_TURN, KineticsEEPROM.EEPROMParameter.Actuator_Address_TURN, CommandStore_Table_Columns.DBTables.SERVO_TURN);

        ActuatorCalibration SERVO_LIFT = new ActuatorCalibration(Actuator.LIFT,  KineticsEEPROM.EEPROMParameter.Referance_Angle_LIFT,KineticsEEPROM.EEPROMParameter.SHURDOWN_DELTA_LIFT, KineticsEEPROM.EEPROMParameter.Actuator_Address_LIFT,  CommandStore_Table_Columns.DBTables.SERVO_LIFT);

        ActuatorCalibration SERVO_LEAN = new ActuatorCalibration( Actuator.LEAN,  KineticsEEPROM.EEPROMParameter.Referance_Angle_LEAN, KineticsEEPROM.EEPROMParameter.SHURDOWN_DELTA_LEAN, KineticsEEPROM.EEPROMParameter.Actuator_Address_LEAN,  CommandStore_Table_Columns.DBTables.SERVO_LEAN);

        ActuatorCalibration SERVO_TILT = new ActuatorCalibration( Actuator.TILT,  KineticsEEPROM.EEPROMParameter.Referance_Angle_TILT, KineticsEEPROM.EEPROMParameter.SHURDOWN_DELTA_TILT, KineticsEEPROM.EEPROMParameter.Actuator_Address_TILT,  CommandStore_Table_Columns.DBTables.SERVO_TILT);


        MachineActuatorList.put(Actuator.TURN, SERVO_TURN);
        MachineActuatorList.put(Actuator.LIFT,SERVO_LIFT);
        MachineActuatorList.put(Actuator.LEAN,SERVO_LEAN);
        MachineActuatorList.put(Actuator.TILT,SERVO_TILT);
    }

   public static MachineConfig Instance = new MachineConfig();

    public Actuator getActuatorWith(Integer address)
    {
        for(Map.Entry<Actuator, ActuatorCalibration> actuator : MachineActuatorList.entrySet())
        {
            if(actuator.getValue().Address == address)
            {
                return actuator.getKey();
            }
        }

        return Actuator.UNKNOWN;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.MachineActuatorList.size());
        for (Map.Entry<Actuator, ActuatorCalibration> entry : this.MachineActuatorList.entrySet()) {
            dest.writeInt(entry.getKey() == null ? -1 : entry.getKey().ordinal());
            dest.writeParcelable(entry.getValue(), flags);
        }
    }

    protected MachineConfig(Parcel in) {
        int MachineActuatorListSize = in.readInt();
        this.MachineActuatorList = new HashMap<Actuator, ActuatorCalibration>(MachineActuatorListSize);
        for (int i = 0; i < MachineActuatorListSize; i++) {
            int tmpKey = in.readInt();
            Actuator key = tmpKey == -1 ? null : Actuator.values()[tmpKey];
            ActuatorCalibration value = in.readParcelable(ActuatorCalibration.class.getClassLoader());
            this.MachineActuatorList.put(key, value);
        }
    }

    public static final Creator<MachineConfig> CREATOR = new Creator<MachineConfig>() {
        @Override
        public MachineConfig createFromParcel(Parcel source) {
            return new MachineConfig(source);
        }

        @Override
        public MachineConfig[] newArray(int size) {
            return new MachineConfig[size];
        }
    };
}
