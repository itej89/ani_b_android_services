package Framework.DataTypes;

import android.os.Parcel;
import android.os.Parcelable;

import FrameworkInterface.PublicTypes.Constants.Actuator;
import FrameworkInterface.PublicTypes.Constants.KineticsEEPROM;
import fm.ani.client.db.DataTypes.CommandStore.Constants.CommandStore_Table_Columns;

/**
 * Created by tej on 24/06/18.
 */

public class ActuatorCalibration implements Parcelable {
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ActuatorType == null ? -1 : this.ActuatorType.ordinal());
        dest.writeValue(this.Address);
        dest.writeValue(this.RefPosition);
        dest.writeValue(this.ShutdownDeltaAngle);
        dest.writeInt(this.RefAngleAddressLocationInEERPOM == null ? -1 : this.RefAngleAddressLocationInEERPOM.ordinal());
        dest.writeInt(this.ActuatorAddressLocationInEERPOM == null ? -1 : this.ActuatorAddressLocationInEERPOM.ordinal());
        dest.writeInt(this.ShutdownAngleAddressLocationInEERPOM == null ? -1 : this.ShutdownAngleAddressLocationInEERPOM.ordinal());
        dest.writeInt(this.CalibrationStoreName == null ? -1 : this.CalibrationStoreName.ordinal());
    }

    protected ActuatorCalibration(Parcel in) {
        int tmpActuatorType = in.readInt();
        this.ActuatorType = tmpActuatorType == -1 ? null : Actuator.values()[tmpActuatorType];
        this.Address = (Integer) in.readValue(Integer.class.getClassLoader());
        this.RefPosition = (Integer) in.readValue(Integer.class.getClassLoader());
        this.ShutdownDeltaAngle = (Integer) in.readValue(Integer.class.getClassLoader());
        int tmpRefAngleAddressLocationInEERPOM = in.readInt();
        this.RefAngleAddressLocationInEERPOM = tmpRefAngleAddressLocationInEERPOM == -1 ? null : KineticsEEPROM.EEPROMParameter.values()[tmpRefAngleAddressLocationInEERPOM];
        int tmpActuatorAddressLocationInEERPOM = in.readInt();
        this.ActuatorAddressLocationInEERPOM = tmpActuatorAddressLocationInEERPOM == -1 ? null : KineticsEEPROM.EEPROMParameter.values()[tmpActuatorAddressLocationInEERPOM];
        int tmpShutdownAngleAddressLocationInEERPOM = in.readInt();
        this.ShutdownAngleAddressLocationInEERPOM = tmpShutdownAngleAddressLocationInEERPOM == -1 ? null : KineticsEEPROM.EEPROMParameter.values()[tmpShutdownAngleAddressLocationInEERPOM];
        int tmpCalibrationStoreName = in.readInt();
        this.CalibrationStoreName = tmpCalibrationStoreName == -1 ? null : CommandStore_Table_Columns.DBTables.values()[tmpCalibrationStoreName];
    }

    public static final Creator<ActuatorCalibration> CREATOR = new Creator<ActuatorCalibration>() {
        @Override
        public ActuatorCalibration createFromParcel(Parcel source) {
            return new ActuatorCalibration(source);
        }

        @Override
        public ActuatorCalibration[] newArray(int size) {
            return new ActuatorCalibration[size];
        }
    };
}
