package client.ani.fouriermachines.fouriermachinesaniclientisl94203.DataTypes;

import java.util.ArrayList;
import java.util.List;

import client.ani.fouriermachines.fouriermachinesaniclientisl94203.DataTypes.Constants.ISL94203ConfigurationParameters;

import client.ani.fouriermachines.fouriermachinesaniclientisl94203.DataTypes.Constants.ISL94203_FLOAT_CONVERSION_TYPES;

public class ISL94203Parameter {
    public ISL94203ConfigurationParameters PARAM_TYPE = ISL94203ConfigurationParameters.NA;
    public String ParameterName = "";
    public String ParameterDescription = "";
    public String ParameterPhysical_Unit = "";
    public Integer Number_Of_Bytes = 0x00;
    public ArrayList<String> BitNames = new ArrayList<String>();
    public ArrayList<Integer> BitValues = new ArrayList<Integer>();
    public ArrayList<Integer> Address = new ArrayList<Integer>();
    public ArrayList<Integer> reg = new ArrayList<Integer>();
    public double Value = 0.0;
    public ISL94203_FLOAT_CONVERSION_TYPES CONVERSION_TYPE = ISL94203_FLOAT_CONVERSION_TYPES.BIT;
   public void ConvertToPhysical()
    {
        ISL94203ConversionFormulae convetor = new ISL94203ConversionFormulae();
        Value = convetor.RegToFloat(reg, Number_Of_Bytes, CONVERSION_TYPE);
    }
}
