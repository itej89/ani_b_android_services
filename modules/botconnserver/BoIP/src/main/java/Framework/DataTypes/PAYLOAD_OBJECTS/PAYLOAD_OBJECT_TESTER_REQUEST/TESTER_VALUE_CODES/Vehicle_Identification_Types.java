package Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_TESTER_REQUEST.TESTER_VALUE_CODES;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class Vehicle_Identification_Types {
    public static Vehicle_Identification_Types Instance = new Vehicle_Identification_Types();

    public enum CODE
    {
         VEHICLE_IDENTIFICATION,
         VEHICLE_IDENTIFICATION_VIN,
         VEHICLE_IDENTIFICATION_EID
    }

    Map<CODE, Byte> CODE_TO_VALUE = new HashMap<CODE, Byte>();

    public  Vehicle_Identification_Types()
    {
        CODE_TO_VALUE.put(CODE.VEHICLE_IDENTIFICATION, (byte)0x01);
        CODE_TO_VALUE.put(CODE.VEHICLE_IDENTIFICATION_EID, (byte)0x02);
        CODE_TO_VALUE.put(CODE.VEHICLE_IDENTIFICATION_VIN, (byte)0x03);
    }


    public Boolean IsIdentificationMode(Byte Mode)
    {
        return CODE_TO_VALUE.keySet().contains(Mode);
    }

    public Byte ENCODE(CODE code)
    {
        if(CODE_TO_VALUE.keySet().contains(code))
        {
            return CODE_TO_VALUE.get(code);
        }
        else
        {
            return 0x01;
        }
    }
}
