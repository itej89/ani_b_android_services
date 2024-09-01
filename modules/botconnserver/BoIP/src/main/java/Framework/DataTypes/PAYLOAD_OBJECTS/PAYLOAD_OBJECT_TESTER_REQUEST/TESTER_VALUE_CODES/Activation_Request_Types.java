package Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_TESTER_REQUEST.TESTER_VALUE_CODES;

import java.util.HashMap;
import java.util.Map;

import Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_ECU_RESPONSE.ECU_CODE_VALUES.Vehicle_Announce_Further_Actions;

public class Activation_Request_Types {

    public static Activation_Request_Types Instance = new Activation_Request_Types();

    public enum CODE
    {
         RA_REQ_DEFAULT,
        RA_REQ_WWH_OBD,
        RA_ISO13400_RESERVED,
        RA_REQ_CENTRAL_SECURITY,
        RA_OEM_SPECIFIC,
        DOIPTester_UNKNOWN_CODE
    }

    Map<CODE, Byte> CODE_TO_VALUE = new HashMap<CODE, Byte>();


    Map<Byte, CODE> VALUE_TO_CODE = new HashMap<Byte, CODE>();

    Map<Vehicle_Announce_Further_Actions.CODE, CODE> FURTHER_ACTION_ROUTING_ACTIVATION = new HashMap<Vehicle_Announce_Further_Actions.CODE, CODE>();


    public Activation_Request_Types()
    {
        CODE_TO_VALUE.put(CODE.RA_REQ_DEFAULT, (byte)0x00);
        CODE_TO_VALUE.put(CODE.RA_REQ_WWH_OBD, (byte)0x01);
        CODE_TO_VALUE.put(CODE.RA_ISO13400_RESERVED, (byte)0x02);
        CODE_TO_VALUE.put(CODE.RA_REQ_CENTRAL_SECURITY, (byte)0xE0);
        CODE_TO_VALUE.put(CODE.RA_OEM_SPECIFIC, (byte)0xE1);

        VALUE_TO_CODE.put((byte)0x00, CODE.RA_REQ_DEFAULT);
        VALUE_TO_CODE.put((byte)0x01, CODE.RA_REQ_WWH_OBD);
        VALUE_TO_CODE.put((byte)0x02, CODE.RA_ISO13400_RESERVED);
        VALUE_TO_CODE.put((byte)0xDF, CODE.RA_ISO13400_RESERVED);
        VALUE_TO_CODE.put((byte)0xE0, CODE.RA_REQ_CENTRAL_SECURITY);
        VALUE_TO_CODE.put((byte)0xE1, CODE.RA_OEM_SPECIFIC);
        VALUE_TO_CODE.put((byte)0xFF, CODE.RA_OEM_SPECIFIC);

        FURTHER_ACTION_ROUTING_ACTIVATION.put(Vehicle_Announce_Further_Actions.CODE.NO_FURTHER_ACTION_REQD, CODE.RA_REQ_DEFAULT);
        FURTHER_ACTION_ROUTING_ACTIVATION.put(Vehicle_Announce_Further_Actions.CODE.ROUTING_REQD_CENTRAL_SECURITY, CODE.RA_REQ_CENTRAL_SECURITY);
    }



    public CODE DECODE(Byte Activation_Request_value)
    {
        if(VALUE_TO_CODE.keySet().contains(Activation_Request_value))
        {
            return VALUE_TO_CODE.get(Activation_Request_value);
        }
        else
        {
            return CODE.DOIPTester_UNKNOWN_CODE;
        }
    }

    public Byte Encode(CODE Activation_Request_code)
    {
        return CODE_TO_VALUE.get(Activation_Request_code);
    }

    public CODE getRoutingActivationTypeFromVehicleAnnouncementFurtherActions(Byte furtheraction)
    {
        Vehicle_Announce_Further_Actions furtherActions = new Vehicle_Announce_Further_Actions();

        Vehicle_Announce_Further_Actions.CODE code = furtherActions.DECODE(furtheraction);

        return FURTHER_ACTION_ROUTING_ACTIVATION.get(code);
    }
}
