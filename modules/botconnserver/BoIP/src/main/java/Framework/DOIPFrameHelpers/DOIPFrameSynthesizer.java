package Framework.DOIPFrameHelpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import Framework.DOIPLayer.DOIPSession;
import Framework.DataTypes.DOIP_OBJECTS.DOIPResponseObject;
import Framework.DataTypes.PAYLOAD_TYPES.Response_Payload_Types;

public class DOIPFrameSynthesizer {
    public byte[] CreateDOIPFrame(DOIPResponseObject DOIPObject)
    {
        byte[] DOIPFrame = null;

        if(DOIPObject != null)
        {
            long PayloadLength = 0;
            byte[] DOIPPayload = null;

            if(DOIPObject.GetPayload() != null)
            {
                PayloadLength = ((DOIPObject.GetPayload().TotalNumberOfBytesInPayload()));

                if(PayloadLength > 0)
                {
                    //DOIPFrame = [UInt8](repeating: 0, count: DOIPResponseObject.GetHeaderLength() + Int(PayloadLength));
                    DOIPPayload = DOIPObject.GetPayload().Make_Payload();
                }
                else
                {
                    return null;
                }
            }
            else
            {
                //DOIPFrame = [UInt8](repeating: 0, count: DOIPRequestObject.GetHeaderLength())
            }

            int TotalLength = 8;
            if(DOIPPayload != null)
            {
                TotalLength = TotalLength + DOIPPayload.length;
            }

            DOIPFrame = new byte[TotalLength];

            DOIPFrame[0] = (DOIPObject.GetProtocolVersion());
            DOIPFrame[1] = (DOIPObject.GetInverseProtocolVersion());

            byte[] PayloadTypeArray = Response_Payload_Types.Instance.Encode(DOIPObject.GetPayLoadType());

            byte[] PayloadLengthArray = toByteArray(PayloadLength);

            DOIPFrame[2] = (PayloadTypeArray[0]);
            DOIPFrame[3] = (PayloadTypeArray[1]);


            DOIPFrame[4] = (PayloadLengthArray[0]);
            DOIPFrame[5] = (PayloadLengthArray[1]);
            DOIPFrame[6] = (PayloadLengthArray[2]);
            DOIPFrame[7] = (PayloadLengthArray[3]);


            if(DOIPPayload != null)
            {
                System.arraycopy(DOIPPayload, 0 , DOIPFrame, 8, DOIPPayload.length);
            }
        }
        return DOIPFrame;
    }

    byte[] toByteArray(long value) {
        byte[] Values = new byte[4];
        Values[0] = ((byte)((value & 0xFF000000)>>24));
        Values[1] = ((byte)((value & 0x00FF0000)>>16));
        Values[2] = ((byte)((value & 0x0000FF00)>>8));
        Values[3] = ((byte)(value & 0x000000FF));
        return Values;
    }

    public DOIPResponseObject FormHeaderForDoIPFrame(Response_Payload_Types.CODE payloadType)
    {
        DOIPResponseObject request = new DOIPResponseObject();
        if(payloadType == Response_Payload_Types.CODE.PLD_VEH_IDEN_RES)
        {
            request.SetProtocolVersion(DOIPSession.Instance.ProtocolVersion);
        }
        else
        {
            request.SetProtocolVersion(DOIPSession.Instance.ProtocolVersion);
        }

        request.SetPayloadType(payloadType);
        return request;
    }

}
