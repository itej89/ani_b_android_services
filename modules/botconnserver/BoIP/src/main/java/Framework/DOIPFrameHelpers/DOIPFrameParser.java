package Framework.DOIPFrameHelpers;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import Framework.DataTypes.DOIP_OBJECTS.DOIPRequestObject;
import Framework.DataTypes.PAYLOAD_TYPES.Request_Payload_Types;
import Framework.DataTypes.PAYLOAD_TYPE_OBJECT_MAP.Request_Payload_Type_To_Object;

public class DOIPFrameParser {



    public DOIPRequestObject Parse(byte[] DOIPFrame)
    {
        if(DOIPFrame.length < DOIPRequestObject.GetHeaderLength())
        {
            return null;
        }
        else
        {
            DOIPRequestObject doipRequestObject = new DOIPRequestObject();
            doipRequestObject.SetProtocolVersion( DOIPFrame[0]);
            doipRequestObject.SetInverseProtocolVersion(DOIPFrame[1]);

            byte[] PayloadTypeBytes = new byte[]{DOIPFrame[3], DOIPFrame[2]};

            doipRequestObject.SetPayloadType(Request_Payload_Types.Instance.Decode(PayloadTypeBytes));


            if(doipRequestObject.GetPayLoadType() != Request_Payload_Types.CODE.DOIPTester_UNKNOWN_CODE)
            {
                byte[] LengthBytes = new byte[]{DOIPFrame[7], DOIPFrame[6], DOIPFrame[5], DOIPFrame[4]};

                long payloadLength = GetLengthFromBytes(LengthBytes);
                doipRequestObject.SetPayloadLength(payloadLength);

                if(payloadLength > 0 && payloadLength + DOIPRequestObject.GetHeaderLength() <= DOIPFrame.length)
                {
                    Log.i("EQ Bug DEBUG", "header length: "+(payloadLength+8));
                    Log.i("EQ Bug DEBUG", "frame length: "+(DOIPFrame.length));

                    byte[] PayloadBytes = Arrays.copyOfRange(DOIPFrame, 8, (int) payloadLength + DOIPRequestObject.GetHeaderLength());

                    Request_Payload_Type_To_Object responeToPayloadObject = new Request_Payload_Type_To_Object();
                    doipRequestObject.SetPayload(responeToPayloadObject.GetPayloadObjectOfType(doipRequestObject.GetPayLoadType(), PayloadBytes, payloadLength));
                }
                else
                {
                    Log.i("Bug DEBUG", "header length: "+(payloadLength+8));
                    Log.i("Bug DEBUG", "frame length: "+(DOIPFrame.length));
                }
            }

            return doipRequestObject;

        }
    }

    private long GetLengthFromBytes(byte[] bytes)
    {
        Log.i("LEN", "byte[0] : "+bytes[0]+" byte[1] : "+bytes[1]+" byte[2] : "+bytes[2]+" byte[3] : "+bytes[3]);
        long value = 0;
        for(int i =0; i < bytes.length; i++)
        {
            Integer shiftCount = 8 * i;
            value = value + (long)(((bytes[i] & 0xFF) << shiftCount));
        }
        Log.i("LEN", "Value : "+value);
        return value;
    }
}
