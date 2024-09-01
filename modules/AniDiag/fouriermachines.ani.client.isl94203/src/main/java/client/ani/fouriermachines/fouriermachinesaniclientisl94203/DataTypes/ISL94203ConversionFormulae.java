package client.ani.fouriermachines.fouriermachinesaniclientisl94203.DataTypes;

import java.util.ArrayList;
import java.util.List;

import client.ani.fouriermachines.fouriermachinesaniclientisl94203.DataTypes.Constants.ISL94203_FLOAT_CONVERSION_TYPES;

public class ISL94203ConversionFormulae
{


    double GAIN = 50.0;
    double SenseR = 0.001;

     public   double  RegToFloat(ArrayList<Integer> bytes, Integer byteCount , ISL94203_FLOAT_CONVERSION_TYPES TYPE)
        {

        Integer reg = 0;
        Integer interate = byteCount;

        while(interate > 0)
        {
        Integer index = interate - 1;

        reg = reg | (bytes.get(index) << ((interate-1)*8));

        interate = interate-1;
        }

        switch(TYPE) {

        case REG_TO_VOLT:
        return RegToVolt(reg);
        case REG_TO_PACK_CURRENT:
        return RegToPackCurrent(reg);
        case REG_TEMP_SENSOR_VOLT:
        return RegToTempSensorVolt(reg);
        case REG_TO_VRGO:
        return RegToVRGO(reg);
        case REG_TO_14_BIT_ADC:
        return RegTo14BITADC(reg);
        case BIT:
        return (double)(reg);
        }

            return  -1;
        }

       public ArrayList<Byte> FloatToReg(double Physical, Integer byteCount ,ISL94203_FLOAT_CONVERSION_TYPES TYPE)
        {

        Integer reg = 0;

        switch(TYPE) {

        case REG_TO_VOLT:
        reg =  VoltToReg(Physical);
        case REG_TO_PACK_CURRENT:
        reg = PackCurrentToReg(Physical);
        case REG_TEMP_SENSOR_VOLT:
        reg = 0;
        case REG_TO_VRGO:
        reg = 0;
        case REG_TO_14_BIT_ADC:
        reg = 0;
        case BIT:
        reg = 0;
        }

        ArrayList<Byte> bytes = new ArrayList<>();

        for(int i=0; i<byteCount; i++)
        {
        bytes.add((byte)(reg>>(i*8) & 0xFF));
        }

        return bytes;

        }


       public double RegToVolt(Integer reg)
        {
        //Consider only lower 12bits for voltage calculation
        //Higher 4 bits are either reserved or misc. indormation
        Integer adc12bit = reg & 0x0FFF;
        return ((((adc12bit) * 1.8 * 8)/(4095 * 3)));
        }

       public int VoltToReg(double volt)
        {
        int reg12bit =   (int)((volt * 4095 * 3)/(1.8 * 8));
        //Higher 4 bits are made one to preserve the misc.information present in register
        Integer regToAnd =  reg12bit | 0xF000;
        return regToAnd;
        }

 public double RegToPackCurrent(Integer reg)
        {
        return (((reg) *  1.8)/(4095 * GAIN * SenseR));
        }

 public Integer PackCurrentToReg(double amp)
        {
        int reg12bit = (int)((amp * 4095.0 * GAIN * SenseR)/(1.8));
        //Higher 4 bits are made one to preserve the misc.information present in register
        int regToAnd =  reg12bit | 0xF000;
        return regToAnd;
        }

 public double RegToTempSensorVolt(int reg)
        {
        return ((((reg) * 1.8)/(4095)));
        }

 public double RegToVRGO(int reg)
        {
        return ((((reg) * 1.8 * 2)/(4095)));
        }

 public double RegTo14BITADC(int reg)
        {
        if(reg >= 8191)
        {

        return ((((reg - 16384) * 1.8)/(8191)));
        }
        else
        {

        return ((((reg) * 1.8)/(8191)));
        }
        }
        }
