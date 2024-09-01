package Framework.DataTypes.Delegates.UI;

import java.util.ArrayList;

import FrameworkInterface.PublicTypes.Constants.Actuator;
import FrameworkInterface.PublicTypes.EEPROMDetails;

public interface DiagRequestListener {
    public  void ReadEEPROMData(EEPROMDetails ReadDetails);
    public  void WriteEEPROMData(EEPROMDetails ReadDetails, ArrayList<Integer> Data);
    public  void ReadServoEEPROMData(Actuator actuator, EEPROMDetails ReadDetails);
    public  void RemoveServoCalibrationData(Actuator actuator);
    public  void SaveServoCalibrationData(Actuator actuator, Integer DEGREE, Integer ADC);
    public  void AttachActuator(Actuator actuator);
    public  void DettachActuator(Actuator actuator);
    public  void MoveActuator(Actuator actuator, Integer Degree);

    public  void ReadISL94203EEPROMData(EEPROMDetails ReadDetails);
    public  void WriteISL94203EEPROMData(Integer Address, ArrayList<Integer> Data);

    public  void ReadISL94203RAMData(EEPROMDetails ReadDetails);
    public  void WriteISL94203RAMMData(Integer Address, ArrayList<Integer> Data);
}
