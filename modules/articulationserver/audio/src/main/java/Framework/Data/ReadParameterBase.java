package Framework.Data;

import java.nio.ByteBuffer;

public class ReadParameterBase {
    public RESP_PARAM_TYPE Parameter;
    public  int Id;
    public  int Offset;
    public  RESP_PARAM_DATA_TYPE Data_Type;
    public double Min;
    public double Max;
    RESP_RW_TYPE RW_TYPE;
    public  double value;
    private   byte[] RawData;
    public  int Value;

  public   ReadParameterBase(RESP_PARAM_TYPE _Parameter, int _Id, int _Offset, RESP_PARAM_DATA_TYPE _Data_Type, double _Min, double _Max, RESP_RW_TYPE _RW_TYPE)
  {
      Parameter = _Parameter;
      Id = _Id;
      Offset = _Offset;
      Data_Type = _Data_Type;
      Min = _Min;
      Max = _Max;
      RW_TYPE = _RW_TYPE;
  }

    public void SetRawData(byte[] _RawData) {
        RawData = _RawData;
        switch (Data_Type)
        {
            case FLOAT:
                int a = ByteBuffer.wrap(new byte[]{_RawData[0], _RawData[1], _RawData[2], _RawData[3]}).getInt();
                int b = ByteBuffer.wrap(new byte[]{_RawData[4], _RawData[5], _RawData[6], _RawData[7]}).getInt();
                value =  (float)(a * Math.pow(2, b));
                break;
            case INT:
                value = ByteBuffer.wrap(new byte[]{_RawData[0], _RawData[1], _RawData[2], _RawData[3]}).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt();
                break;
        }
    }
}
