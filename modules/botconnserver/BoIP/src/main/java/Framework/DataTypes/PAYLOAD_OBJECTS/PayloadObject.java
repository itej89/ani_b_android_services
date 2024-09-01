package Framework.DataTypes.PAYLOAD_OBJECTS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import Framework.Validation.Interface.IValidation;
import Framework.Validation.VALIDATION_ERROR_CODES;
import Framework.Validation.ValidationRuleMessage;
import Framework.Validation.ValidationRuleMessages;

public class PayloadObject implements IValidation
{
    public ValidationRuleMessages ValidationErrors = new ValidationRuleMessages();



    public static Integer Max_DOIP_PAYLOAD_SIZE = 0;

    public long Minimum_Payload_Length_In_Bytes = 0;
    public long Maximum_Payload_Length_In_Bytes = 0;

    protected Map<TAG, Payload_Item_Type> Payload = new HashMap<TAG, Payload_Item_Type>();


    //Calculate Payload Length according to the length specified in doip standard ISO 13400-2
    public long DOIPPayloadMinimumLengthInBytes()
    {
        long ValidData_Length = 0;

        for (Map.Entry<TAG, Payload_Item_Type> pair:Payload.entrySet()) {
            if(pair.getValue().IsItemLengthNotDefined())
            {
                Minimum_Payload_Length_In_Bytes = ValidData_Length;
                Maximum_Payload_Length_In_Bytes = Long.MAX_VALUE;
                return ValidData_Length;
            }
            else
            {
                ValidData_Length += pair.getValue().Length_ISO_13400;
            }
        }

        Minimum_Payload_Length_In_Bytes = ValidData_Length;
        Maximum_Payload_Length_In_Bytes = ValidData_Length;

        return ValidData_Length;
    }

    public long TotalNumberOfBytesInPayload()
    {
        long ValidData_Length = 0;

        for(Map.Entry<TAG, Payload_Item_Type> pair:Payload.entrySet())
        {
            if(pair.getValue().RawData != null)
            {
                ValidData_Length = ValidData_Length + (pair.getValue().RawData.length);
            }
        }

        return ValidData_Length;
    }

    public void ExtractPayload(byte[] DOIPPayload, long payload_length)
    {
        for(Map.Entry<TAG, Payload_Item_Type> pair:Payload.entrySet())
        {
            if(pair.getValue().IsItemLengthNotDefined())
            {
                //This assignment will extract Payload Items whose length is not fixed
                pair.getValue().RawData = Arrays.copyOfRange(DOIPPayload, pair.getValue().Position, (int)payload_length);
            }
            else
            {
                long EndIndex = (pair.getValue().Position + pair.getValue().RawData.length);
                pair.getValue().RawData = Arrays.copyOfRange(DOIPPayload, pair.getValue().Position, (int)(EndIndex));
            }
        }
    }

    public enum TAG
    {
         ISO_RESERVED,OEM_RESERVED,

        //Vehicle Identification REsponse Payload
        VIN,LOGICAL_ADDRESS,EID,GID,FURTHER_ACTION,VIN_GID,SYNC,

        //Routing Activation Request Payload
        SOURCE_ADDRESS, ACTIVATION_TYPE,//ISO_RESERVED,OEM_RESERVED,

        //Routing Activation response Payload
        LOGICAL_ADDRESS_TEST_EQUIPMENT,LOGICAL_ADDRESS_DOIP_ENTITY,ROUTING_ACTIVATION_RESPONSE_CODE,//ISO_RESERVED,OEM_RESERVED,

        //Diagnostic Message Request/Response Payload
        TARGET_ADDRESS,USER_DATA,//SOURCE_ADDRESS,TARGET_ADDRESS,ACK,PREVIOUS_DIAGNOSTIC_DATA

        //Diagnostic Message Positive Acknowledgement Payload
        ACK,PREVIOUS_DIAGNOSTIC_DATA,//SOURCE_ADDRESS,TARGET_ADDRESS_ACK,ACK,PREVIOUS_DIAGNOSTIC_DATA,

        //Diagnostic Message Negative Acknowledgement Payload
        NACK,//SOURCE_ADDRESS,TARGET_ADDRESS_ACK,ACK,PREVIOUS_DIAGNOSTIC_DATA,

        //Alive Check Request
        //No Payload

        //Diagnostic Power Mode
        //No Payload

        //Diagnostic Power Mode Response
        DIAGNOSTIC_POWER_MODE,

        //Diagnostic Status Request
        //Node Payload

        //Diagnostic Status Response
        NODE_TYPE,MAX_CONCURRENT_TCP_SOCKETS,CURRENT_OPEN_TCP_SOCKETS,MAX_DATA_SIZE,

        //Generic HEader NACK
        HEADER_NACK

    }

    public void Decode_Payload(byte[] DOIPPayload, long payload_length)
    {
        if(DOIPPayload == null)
        {
            ValidationErrors.Add( new ValidationRuleMessage(VALIDATION_ERROR_CODES.EMPTY,  "PayloadObject:Decode_Payload",  ""));

            return;
        }

        Minimum_Payload_Length_In_Bytes = DOIPPayloadMinimumLengthInBytes();

        if(DOIPPayload.length >= Minimum_Payload_Length_In_Bytes)
        {
            ExtractPayload(DOIPPayload, payload_length);
        }
        else
        {
            ValidationErrors.Add(new ValidationRuleMessage(VALIDATION_ERROR_CODES.INCORRECT_LENGTH,  "PayloadObject:Decode_Payload",  ""));
        }
    }

    public byte[] Make_Payload()
    {
        long Required_Payload_Length = TotalNumberOfBytesInPayload();

        if(Required_Payload_Length != 0)
        {

            Integer TotalLength =0 ;
            for(Map.Entry<TAG, Payload_Item_Type> pair:Payload.entrySet())
            {
                if(pair.getValue().RawData != null) {
                    TotalLength = TotalLength + pair.getValue().RawData.length;
                }

            }
            byte[] DOIP_Payload = new byte[TotalLength];

            for(Map.Entry<TAG, Payload_Item_Type> pair:Payload.entrySet())
            {
                System.arraycopy(pair.getValue().RawData, 0, DOIP_Payload, pair.getValue().Position, pair.getValue().RawData.length);

            }

            return DOIP_Payload;
        }

        return null;
    }

    public PayloadObject()
    {
        ValidationErrors = new ValidationRuleMessages();
    }
}
