package Framework.DataTypes;

import FrameworkInterface.PublicTypes.Constants.Actuator;
import FrameworkInterface.PublicTypes.Constants.KineticsEEPROM;
import fm.ani.client.db.DataTypes.CommandStore.Constants.CommandStore_Table_Columns;

/**
 * Created by tej on 24/06/18.
 */

public class ActuatorCalibration {
    public Actuator ActuatorType;
    public Integer Address = 0;
    public Integer RefPosition;
    public Integer ShutdownDeltaAngle;
    public KineticsEEPROM.EEPROMParameter RefAngleAddressLocationInEERPOM;
    public KineticsEEPROM.EEPROMParameter ActuatorAddressLocationInEERPOM;
    public KineticsEEPROM.EEPROMParameter ShutdownAngleAddressLocationInEERPOM;
    public CommandStore_Table_Columns.DBTables CalibrationStoreName;

     public ActuatorCalibration(Actuator actuatorType, KineticsEEPROM.EEPROMParameter eepromRefAngleLocation, KineticsEEPROM.EEPROMParameter shutdownAngleAddressLocationInEERPOM, KineticsEEPROM.EEPROMParameter eepromAddressLocation, CommandStore_Table_Columns.DBTables calibrationStore) {
        ActuatorType = actuatorType;
        RefAngleAddressLocationInEERPOM = eepromRefAngleLocation;
        ActuatorAddressLocationInEERPOM = eepromAddressLocation;
        ShutdownAngleAddressLocationInEERPOM = shutdownAngleAddressLocationInEERPOM;
        CalibrationStoreName = calibrationStore;
    }
}
