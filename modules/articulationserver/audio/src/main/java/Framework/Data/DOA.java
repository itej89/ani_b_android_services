package Framework.Data;

public class DOA extends ReadParameterBase {

    public  double Theta;

    public  DOA(RESP_PARAM_TYPE _Parameter, int _Id, int _Offset, RESP_PARAM_DATA_TYPE _Data_Type, double _Min, double  _Max, RESP_RW_TYPE _RW_TYPE)
    {
        super(_Parameter,  _Id,  _Offset,  _Data_Type,  _Min,  _Max,  _RW_TYPE);
        Parameter = RESP_PARAM_TYPE.DOAANGLE;
    }

    @Override
    public void SetRawData(byte[] _RawData) {
        super.SetRawData(_RawData);

        Theta = value;
        if(Theta < 0)
        {
            Theta = 360+value;
        }
    }

}
