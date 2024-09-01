package Framework.DataTypes.PAYLOAD_OBJECTS;

import java.util.ArrayList;

public class Payload_Item_Type {
    public byte[] RawData = null;
    public Integer Position = 0;
    public long Length_ISO_13400 = 0;
    private Boolean IsOfVariableLength = false;

    public Payload_Item_Type(Integer _Position, long _Length)
    {
        Position = _Position;
        Length_ISO_13400 = _Length;
        RawData = new byte[(int)_Length];
    }

    public Payload_Item_Type(Integer _Position)
    {
        Position = _Position;
        IsOfVariableLength = true;
        RawData = null;
    }

    public Boolean IsItemLengthNotDefined()
    {
        return IsOfVariableLength;
    }

}
