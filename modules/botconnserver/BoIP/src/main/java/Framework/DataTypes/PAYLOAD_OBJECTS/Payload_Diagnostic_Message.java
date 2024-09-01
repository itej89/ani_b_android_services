package Framework.DataTypes.PAYLOAD_OBJECTS;

import java.util.ArrayList;

import Framework.Validation.VALIDATION_ERROR_CODES;
import Framework.Validation.ValidationRuleMessage;

public class Payload_Diagnostic_Message extends PayloadObject {

    public void initialize_PayloadItems()
    {
        Payload.put(TAG.SOURCE_ADDRESS,new Payload_Item_Type(0,2));
                Payload.put(TAG.TARGET_ADDRESS,new Payload_Item_Type(2,2));
                Payload.put(TAG.USER_DATA,new Payload_Item_Type(4));
    }

    public Payload_Diagnostic_Message(byte[] DOIPPayload, long payload_length)
    {
        super();
        initialize_PayloadItems();
        Decode_Payload(DOIPPayload, payload_length);
    }

    public  Payload_Diagnostic_Message()
    {
        super();
        initialize_PayloadItems();
    }

    public byte[] GetTargetAddress()
    {
        return Payload.get(TAG.TARGET_ADDRESS).RawData;
    }

    public byte[]  GetSourceAddress()
    {
        return Payload.get(TAG.SOURCE_ADDRESS).RawData;
    }

    public byte[] GetUserData()
    {
        byte[]  userData = Payload.get(TAG.USER_DATA).RawData;

        if(userData != null || userData.length == 0)
        {
            ValidationErrors.Add(new ValidationRuleMessage(VALIDATION_ERROR_CODES.EMPTY, "UserData in Diagnostic Message PAyload"));
        }

        return userData;
    }

    public void SetSourceAddress(byte[] SA)
    {
        Payload.get(TAG.SOURCE_ADDRESS).RawData = SA;
    }

    public void SetTargetAddress(byte[] TA)
    {
        Payload.get(TAG.TARGET_ADDRESS).RawData = TA;
    }

    public void SetUserData(byte[] User_Data)
    {
        if(User_Data != null)
        {
            Payload.get(TAG.USER_DATA).RawData = User_Data;
        }
        else
        {
            ValidationErrors.Add(new ValidationRuleMessage(VALIDATION_ERROR_CODES.EMPTY, "Payload_Diagnostic_Message:SetUserData",  ""));
        }
    }
}
