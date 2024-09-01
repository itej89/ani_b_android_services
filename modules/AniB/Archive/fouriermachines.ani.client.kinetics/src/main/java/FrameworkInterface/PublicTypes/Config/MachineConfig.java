package FrameworkInterface.PublicTypes.Config;

import java.util.HashMap;
import java.util.Map;

import Framework.DataTypes.ActuatorCalibration;
import FrameworkInterface.PublicTypes.Constants.Actuator;
import FrameworkInterface.PublicTypes.Constants.KineticsEEPROM;
import fm.ani.client.db.DataTypes.CommandStore.Constants.CommandStore_Table_Columns;

/**
 * Created by tej on 24/06/18.
 */

public class MachineConfig {


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
}
