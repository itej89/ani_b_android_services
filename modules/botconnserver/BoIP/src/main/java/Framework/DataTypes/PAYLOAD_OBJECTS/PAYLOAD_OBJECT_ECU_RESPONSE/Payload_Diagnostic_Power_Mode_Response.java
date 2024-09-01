package Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_ECU_RESPONSE;

import java.util.ArrayList;

import Framework.DataTypes.PAYLOAD_OBJECTS.PayloadObject;
import Framework.DataTypes.PAYLOAD_OBJECTS.Payload_Item_Type;

public class Payload_Diagnostic_Power_Mode_Response extends PayloadObject
{

    public void initialize_PayloadItems()
    {
        Payload.put(TAG.DIAGNOSTIC_POWER_MODE, new Payload_Item_Type( 0, 1));

    }

    public  Payload_Diagnostic_Power_Mode_Response(byte[] DOIPPayload, long payload_length) {
        super();
        initialize_PayloadItems();
        Decode_Payload(DOIPPayload, payload_length);
    }

    public void SetDiagnosticPowerMode(Byte DPM)
    {
        byte[] _DPM = new byte[1];
        _DPM[0] = (DPM);
        Payload.get(TAG.DIAGNOSTIC_POWER_MODE).RawData = _DPM;
    }
}
