package client.ani.fouriermachines.fouriermachinesaniclientisl94203.DataTypes;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import client.ani.fouriermachines.fouriermachinesaniclientisl94203.DataTypes.Constants.ISL94203ConfigurationParameters;
import client.ani.fouriermachines.fouriermachinesaniclientisl94203.DataTypes.Constants.ISL94203_FLOAT_CONVERSION_TYPES;

public class ISL94203ConfigurationObjects {


       public static ISL94203ConfigurationObjects Instance = new ISL94203ConfigurationObjects();

       public   Map<ISL94203ConfigurationParameters, ISL94203Parameter> PARAMETERS = new HashMap<>();

       public  ArrayList<ISL94203ConfigurationParameters> AVAILABLE_PARAM = new ArrayList<>();

       public ISL94203ConfigurationObjects()
        {


            PARAMETERS.put(ISL94203ConfigurationParameters.EEPROM_ENABLE,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Cell_Minimum_Voltage,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Cell_Maximum_Voltage,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Pack_Current,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Internal_Temperature,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.External_Temperature1,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.External_Temperature2,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.VBATT_Votlage,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.VRGO_Voltage,new ISL94203Parameter());

            PARAMETERS.put(ISL94203ConfigurationParameters.Charge_Over_Temperature_Recovery_Voltage,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Charge_Under_Temperature_Voltage,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Charge_Under_Temperature_Recovery_Voltage,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Discharge_Over_Temperature_Voltage,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Discharge_Over_Temperature_Recovery_Voltage,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Discharge_Under_Temperature_Voltage,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Discharge_Under_Temperature_Recovery_Voltage,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Internal_Over_Temperature_Voltage,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Internal_Over_Temperature_Recovery_Voltage,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Sleep_Level_Voltage,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Sleep_Delay_Timer_Watchdog_Timer,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Sleep_Mode_Timer_Cell_Configuration,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.CONFIG1,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.CONFIG2,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.STATUS1,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.STATUS2,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.STATUS3,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.STATUS4,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.CONTROL1,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.CONTROL2,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.CONTROL3,new ISL94203Parameter());

            PARAMETERS.put(ISL94203ConfigurationParameters.Under_Voltage_Threshold,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Under_Voltage_Recovery,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Over_Voltage_Lockout_Threshold,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Under_Voltage_Lockout_Threshold,new IS94203VOLTAGE());
            PARAMETERS.put(ISL94203ConfigurationParameters.End_Of_Charge_Threshold,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Over_Voltage_Delay_Timeout,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Under_Voltage_Delay_Timeout,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Open_Wire_Timing,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Discharge_Over_Current_Timeout_Threshold,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Charge_Over_Current_Timeout_Threshold,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Discharge_Short_Circuit_Timeout_Threshold,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Cell_Balance_Minimum_Differential_Voltage,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Cell_Balance_Off_Time,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Cell_Balance_Minimum_Temperature_Limit,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Cell_Balance_Maximum_Temperature_Limit,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Cell_Balance_Minimum_Temperature_Recovery_Level,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Cell_Balance_Maximum_Temperature_Recovery_Level,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Charge_Over_Temperature_Voltage,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Low_Voltage_Charge_Level,new ISL94203Parameter());



            PARAMETERS.put(ISL94203ConfigurationParameters.ADC_Voltage_14Bit,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Analog_Mux_Control_Bits,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Cell1_Voltage,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Cell2_Voltage,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Cell3_Voltage,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Cell4_Voltage,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Cell5_Voltage,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Cell6_Voltage,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Cell7_Voltage,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Cell8_Voltage,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Cell_Balance_FET_control,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Cell_Balance_Maximum_Differential_Voltage,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Cell_Balance_Maximum_Voltage,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Cell_Balance_Min_Voltage,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Over_Voltage_Threshold,new ISL94203Parameter());
            PARAMETERS.put(ISL94203ConfigurationParameters.Over_Voltage_Recovery,new ISL94203Parameter());








            PARAMETERS.get(ISL94203ConfigurationParameters.Analog_Mux_Control_Bits).PARAM_TYPE = ISL94203ConfigurationParameters.Analog_Mux_Control_Bits;
            PARAMETERS.get(ISL94203ConfigurationParameters.Analog_Mux_Control_Bits).Address.add(0x85);
            PARAMETERS.get(ISL94203ConfigurationParameters.Analog_Mux_Control_Bits).ParameterName = "Analog MUX control bits";
            PARAMETERS.get(ISL94203ConfigurationParameters.Analog_Mux_Control_Bits).ParameterDescription = "Voltage monitored by ADC when microcontroller overrides the internal scan operation.";
            PARAMETERS.get(ISL94203ConfigurationParameters.Analog_Mux_Control_Bits).ParameterPhysical_Unit = "NA";
            PARAMETERS.get(ISL94203ConfigurationParameters.Analog_Mux_Control_Bits).BitNames = new ArrayList<>(Arrays.asList("AO0", "AO1", "AO2", "AO3", "CG0", "CG1", "ADCSTRT", "-", "-", "-", "-", "-", "-", "-", "-", "-"));
            PARAMETERS.get(ISL94203ConfigurationParameters.Analog_Mux_Control_Bits).BitValues = new ArrayList<>(Arrays.asList( 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 ));
            PARAMETERS.get(ISL94203ConfigurationParameters.Analog_Mux_Control_Bits).reg = new ArrayList<>(Arrays.asList(0, 0));
            PARAMETERS.get(ISL94203ConfigurationParameters.Analog_Mux_Control_Bits).Number_Of_Bytes = 1;
            PARAMETERS.get(ISL94203ConfigurationParameters.Analog_Mux_Control_Bits).CONVERSION_TYPE = ISL94203_FLOAT_CONVERSION_TYPES.BIT;



            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Threshold).PARAM_TYPE = ISL94203ConfigurationParameters.Over_Voltage_Threshold;
            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Threshold).Address.add(0x00);
            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Threshold).Address.add(0x01);
            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Threshold).ParameterName = "Over Voltage Threshold";
            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Threshold).ParameterDescription = "If any cell voltage is above this threshold voltage for an overvoltage delay time,the charge FET is turned off. ";
            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Threshold).ParameterPhysical_Unit = "V";
            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Threshold).BitNames = new ArrayList<>(Arrays.asList("OVL0", "OVL1", "OVL2", "OVL3", "OVL4", "OVL5", "OVL6", "OVL7", "OVL8", "OVL9", "OVLA", "OVLB", "CPW0", "CPW1", "CPW2", "CPW3"));
            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Threshold).BitValues = new ArrayList<>(Arrays.asList( 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 ));
            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Threshold).reg = new ArrayList<>(Arrays.asList(0, 0));
            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Threshold).Number_Of_Bytes = 2;
            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Threshold).CONVERSION_TYPE = ISL94203_FLOAT_CONVERSION_TYPES.REG_TO_VOLT;






            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Recovery).PARAM_TYPE = ISL94203ConfigurationParameters.Over_Voltage_Recovery;
            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Recovery).Address.add(0x02);
            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Recovery).Address.add(0x03);
            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Recovery).ParameterName = "Over Voltage Recovery";
            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Recovery).ParameterDescription = "If all cells fall below this overvoltage recovery level, the charge FET is turned on.";
            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Recovery).ParameterPhysical_Unit = "V";
            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Recovery).BitNames = new ArrayList<>(Arrays.asList("OVR0", "OVR1", "OVR2", "OVR3", "OVR4", "OVR5", "OVR6", "OVR7", "OVR8", "OVR9", "OVRA", "OVRB", "-", "-", "-", "-"));
            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Recovery).BitValues = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Recovery).reg = new ArrayList<>(Arrays.asList(0, 0));
            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Recovery).Number_Of_Bytes = 2;
            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Recovery).CONVERSION_TYPE = ISL94203_FLOAT_CONVERSION_TYPES.REG_TO_VOLT;





            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Threshold).PARAM_TYPE = ISL94203ConfigurationParameters.Under_Voltage_Threshold;
            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Threshold).Address.add(0x04);
            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Threshold).Address.add(0x05);
            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Threshold).ParameterName = "Under Voltage Threshold";
            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Threshold).ParameterDescription = "If any cell voltage is below this threshold voltage for an undervoltage delay time, the discharge FET is turned off. ";
            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Threshold).ParameterPhysical_Unit = "V";
            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Threshold).BitNames = new ArrayList<>(Arrays.asList("UVL0", "UVL1", "UVL2", "UVL3", "UVL4", "UVL5", "UVL6", "UVL7", "UVL8", "UVL9", "UVLA", "UVLB", "LPW0", "LPW1", "LPW2", "LPW3"));
            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Threshold).BitValues = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Threshold).reg = new ArrayList<>(Arrays.asList(0, 0));
            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Threshold).Number_Of_Bytes = 2;
            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Threshold).CONVERSION_TYPE = ISL94203_FLOAT_CONVERSION_TYPES.REG_TO_VOLT;






            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Recovery).PARAM_TYPE = ISL94203ConfigurationParameters.Under_Voltage_Recovery;
            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Recovery).Address.add(0x06);
            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Recovery).Address.add(0x07);
            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Recovery).ParameterName = "Under Voltage Recovery";
            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Recovery).ParameterDescription = "If all cells rise above this overvoltage recovery level (and there is no load), the discharge FET is turned on. ";
            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Recovery).ParameterPhysical_Unit = "V";
            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Recovery).BitNames =  new ArrayList<>(Arrays.asList("UVR0", "UVR1", "UVR2", "UVR3", "UVR4", "UVR5", "UVR6", "UVR7", "UVR8", "UVR9", "UVRA", "UVRB", "-", "-", "-", "-"));
            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Recovery).BitValues =  new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Recovery).reg =  new ArrayList<>(Arrays.asList(0, 0));
            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Recovery).Number_Of_Bytes = 2;
            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Recovery).CONVERSION_TYPE = ISL94203_FLOAT_CONVERSION_TYPES.REG_TO_VOLT;








            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Lockout_Threshold).PARAM_TYPE = ISL94203ConfigurationParameters.Over_Voltage_Lockout_Threshold;
            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Lockout_Threshold).Address.add(0x08);
            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Lockout_Threshold).Address.add(0x09);
            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Lockout_Threshold).ParameterName = "Overvoltage Lockout Threshold";
            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Lockout_Threshold).ParameterDescription = "If any cell voltage is above this thresh old for five successive scans, then the device is in an overvoltage lockout condition. In this condition, the Charge FET is turned off, the cell balance FETs are turned off, the OVLO bit is set and the PSD output is set to active.";
            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Lockout_Threshold).ParameterPhysical_Unit = "V";
            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Lockout_Threshold).BitNames =   new ArrayList<>(Arrays.asList("OVLO0", "OVLO1", "OVLO2", "OVLO3", "OVLO4", "OVLO5", "OVLO6", "OVLO7", "OVLO8", "OVLO9", "OVLOA", "OVLOB", "-", "-", "-", "-"));
            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Lockout_Threshold).BitValues =   new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Lockout_Threshold).reg =   new ArrayList<>(Arrays.asList(0, 0));
            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Lockout_Threshold).Number_Of_Bytes = 2;
            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Lockout_Threshold).CONVERSION_TYPE = ISL94203_FLOAT_CONVERSION_TYPES.REG_TO_VOLT;








            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Lockout_Threshold).PARAM_TYPE = ISL94203ConfigurationParameters.Under_Voltage_Lockout_Threshold;
            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Lockout_Threshold).Address.add(0x0A);
            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Lockout_Threshold).Address.add(0x0B);
            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Lockout_Threshold).ParameterName = "Undervoltage Lockout Threshold";
            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Lockout_Threshold).ParameterDescription = "If any cell voltage is below this threshold for five successive scans, then the device is in an undervoltage lockout condition. In this condition, the Discharge FET is turned off and the UVLO bit is set. The device also powers down (unless overridden).";
            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Lockout_Threshold).ParameterPhysical_Unit = "V";
            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Lockout_Threshold).BitNames =   new ArrayList<>(Arrays.asList("UVLO0", "UVLO1", "UVLO2", "UVLO3", "UVLO4", "UVLO5", "UVLO6", "UVLO7", "UVLO8", "UVLO9", "UVLOA", "UVLOB", "-", "-", "-", "-"));
            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Lockout_Threshold).BitValues =   new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 ));
            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Lockout_Threshold).reg =   new ArrayList<>(Arrays.asList(0, 0));
            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Lockout_Threshold).Number_Of_Bytes = 2;
            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Lockout_Threshold).CONVERSION_TYPE = ISL94203_FLOAT_CONVERSION_TYPES.REG_TO_VOLT;






            PARAMETERS.get(ISL94203ConfigurationParameters.End_Of_Charge_Threshold).PARAM_TYPE = ISL94203ConfigurationParameters.End_Of_Charge_Threshold;
            PARAMETERS.get(ISL94203ConfigurationParameters.End_Of_Charge_Threshold).Address.add(0x0C);
            PARAMETERS.get(ISL94203ConfigurationParameters.End_Of_Charge_Threshold).Address.add(0x0D);
            PARAMETERS.get(ISL94203ConfigurationParameters.End_Of_Charge_Threshold).ParameterName = "End of Charge Threshold";
            PARAMETERS.get(ISL94203ConfigurationParameters.End_Of_Charge_Threshold).ParameterDescription = "If any cell exceeds this level, then the EOC output and the EOC bit are set. ";
            PARAMETERS.get(ISL94203ConfigurationParameters.End_Of_Charge_Threshold).ParameterPhysical_Unit = "V";
            PARAMETERS.get(ISL94203ConfigurationParameters.End_Of_Charge_Threshold).BitNames = new ArrayList<>(Arrays.asList("EOC0", "EOC1", "EOC2", "EOC3", "EOC4", "EOC5", "EOC6", "EOC7", "EOC8", "EOC9", "EOCA", "EOCB", "-", "-", "-", "-"));
            PARAMETERS.get(ISL94203ConfigurationParameters.End_Of_Charge_Threshold).BitValues = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
            PARAMETERS.get(ISL94203ConfigurationParameters.End_Of_Charge_Threshold).reg = new ArrayList<>(Arrays.asList(0, 0));
            PARAMETERS.get(ISL94203ConfigurationParameters.End_Of_Charge_Threshold).Number_Of_Bytes = 2;
            PARAMETERS.get(ISL94203ConfigurationParameters.End_Of_Charge_Threshold).CONVERSION_TYPE = ISL94203_FLOAT_CONVERSION_TYPES.REG_TO_VOLT;


            PARAMETERS.get(ISL94203ConfigurationParameters.CONTROL1).PARAM_TYPE = ISL94203ConfigurationParameters.CONTROL1;
            PARAMETERS.get(ISL94203ConfigurationParameters.CONTROL1).Address.add(0x86);
            PARAMETERS.get(ISL94203ConfigurationParameters.CONTROL1).ParameterName = "Control 1";
            PARAMETERS.get(ISL94203ConfigurationParameters.CONTROL1).ParameterDescription = "";
            PARAMETERS.get(ISL94203ConfigurationParameters.CONTROL1).ParameterPhysical_Unit = "NA";
            PARAMETERS.get(ISL94203ConfigurationParameters.CONTROL1).BitNames =  new ArrayList<>(Arrays.asList("CLR_LERR", "LMON_EN", "CLR_ERR", "CMON_EN", "PSD", "PCFET", "CFET", "DFET", "-", "-", "-", "-", "-", "-", "-", "-"));
            PARAMETERS.get(ISL94203ConfigurationParameters.CONTROL1).BitValues =  new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, -1, -1, -1, -1, -1));
            PARAMETERS.get(ISL94203ConfigurationParameters.CONTROL1).reg =  new ArrayList<>(Arrays.asList(0));
            PARAMETERS.get(ISL94203ConfigurationParameters.CONTROL1).Number_Of_Bytes = 1;
            PARAMETERS.get(ISL94203ConfigurationParameters.CONTROL1).CONVERSION_TYPE = ISL94203_FLOAT_CONVERSION_TYPES.BIT;

            PARAMETERS.get(ISL94203ConfigurationParameters.CONTROL2).PARAM_TYPE = ISL94203ConfigurationParameters.CONTROL2;
            PARAMETERS.get(ISL94203ConfigurationParameters.CONTROL2).Address.add(0x87);
            PARAMETERS.get(ISL94203ConfigurationParameters.CONTROL2).ParameterName = "Control 2";
            PARAMETERS.get(ISL94203ConfigurationParameters.CONTROL2).ParameterDescription = "";
            PARAMETERS.get(ISL94203ConfigurationParameters.CONTROL2).ParameterPhysical_Unit = "NA";
            PARAMETERS.get(ISL94203ConfigurationParameters.CONTROL2).BitNames =  new ArrayList<>(Arrays.asList("-", "uFET", "uCCBAL", "uCLMON", "uCCMON", "uCSCAN", "OW_STRT", "CBAL_ON", "-", "-", "-", "-", "-", "-", "-", "-"));
            PARAMETERS.get(ISL94203ConfigurationParameters.CONTROL2).BitValues =  new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, -1, -1, -1, -1, -1));
            PARAMETERS.get(ISL94203ConfigurationParameters.CONTROL2).reg =  new ArrayList<>(Arrays.asList(0));
            PARAMETERS.get(ISL94203ConfigurationParameters.CONTROL2).Number_Of_Bytes = 1;
            PARAMETERS.get(ISL94203ConfigurationParameters.CONTROL2).CONVERSION_TYPE = ISL94203_FLOAT_CONVERSION_TYPES.BIT;


            PARAMETERS.get(ISL94203ConfigurationParameters.CONTROL3).PARAM_TYPE = ISL94203ConfigurationParameters.CONTROL3;
            PARAMETERS.get(ISL94203ConfigurationParameters.CONTROL3).Address.add(0x88);
            PARAMETERS.get(ISL94203ConfigurationParameters.CONTROL3).ParameterName = "Control 3";
            PARAMETERS.get(ISL94203ConfigurationParameters.CONTROL3).ParameterDescription = "";
            PARAMETERS.get(ISL94203ConfigurationParameters.CONTROL3).ParameterPhysical_Unit = "NA";
            PARAMETERS.get(ISL94203ConfigurationParameters.CONTROL3).BitNames =  new ArrayList<>(Arrays.asList("-", "-", "-", "-", "PDWN", "SLEEP", "DOZE", "IDLE", "-", "-", "-", "-", "-", "-", "-", "-"));
            PARAMETERS.get(ISL94203ConfigurationParameters.CONTROL3).BitValues =  new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, -1, -1, -1, -1, -1));
            PARAMETERS.get(ISL94203ConfigurationParameters.CONTROL3).reg =  new ArrayList<>(Arrays.asList(0));
            PARAMETERS.get(ISL94203ConfigurationParameters.CONTROL3).Number_Of_Bytes = 1;
            PARAMETERS.get(ISL94203ConfigurationParameters.CONTROL3).CONVERSION_TYPE = ISL94203_FLOAT_CONVERSION_TYPES.BIT;






            PARAMETERS.get(ISL94203ConfigurationParameters.STATUS1).PARAM_TYPE = ISL94203ConfigurationParameters.STATUS1;
            PARAMETERS.get(ISL94203ConfigurationParameters.STATUS1).Address.add(0x80);
            PARAMETERS.get(ISL94203ConfigurationParameters.STATUS1).ParameterName = "Status 1";
            PARAMETERS.get(ISL94203ConfigurationParameters.STATUS1).ParameterDescription = "";
            PARAMETERS.get(ISL94203ConfigurationParameters.STATUS1).ParameterPhysical_Unit = "NA";
            PARAMETERS.get(ISL94203ConfigurationParameters.STATUS1).BitNames =  new ArrayList<>(Arrays.asList("OV", "OV Lockout", "UV", "UV Lockout", "Disc OTemp", "Disc UTemp", "Char OTemp", "Char UTemp", "-", "-", "-", "-", "-", "-", "-", "-"));
            PARAMETERS.get(ISL94203ConfigurationParameters.STATUS1).BitValues =  new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, -1, -1, -1, -1, -1));
            PARAMETERS.get(ISL94203ConfigurationParameters.STATUS1).reg =  new ArrayList<>(Arrays.asList(0));
            PARAMETERS.get(ISL94203ConfigurationParameters.STATUS1).Number_Of_Bytes = 1;
            PARAMETERS.get(ISL94203ConfigurationParameters.STATUS1).CONVERSION_TYPE = ISL94203_FLOAT_CONVERSION_TYPES.BIT;






            PARAMETERS.get(ISL94203ConfigurationParameters.STATUS2).PARAM_TYPE = ISL94203ConfigurationParameters.STATUS2;
            PARAMETERS.get(ISL94203ConfigurationParameters.STATUS2).Address.add(0x81);
            PARAMETERS.get(ISL94203ConfigurationParameters.STATUS2).ParameterName = "Status 2";
            PARAMETERS.get(ISL94203ConfigurationParameters.STATUS2).ParameterDescription = "";
            PARAMETERS.get(ISL94203ConfigurationParameters.STATUS2).ParameterPhysical_Unit = "NA";
            PARAMETERS.get(ISL94203ConfigurationParameters.STATUS2).BitNames = new ArrayList<>(Arrays.asList("Int OTemp", "Char OCurrent", "Disc OCurrent", "Disc SC", "Cell Fail", "Open Wire", "-", "EOC", "-", "-", "-", "-", "-", "-", "-", "-"));
            PARAMETERS.get(ISL94203ConfigurationParameters.STATUS2).BitValues = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, -1, -1, -1, -1, -1));
            PARAMETERS.get(ISL94203ConfigurationParameters.STATUS2).reg = new ArrayList<>(Arrays.asList(0));
            PARAMETERS.get(ISL94203ConfigurationParameters.STATUS2).Number_Of_Bytes = 1;
            PARAMETERS.get(ISL94203ConfigurationParameters.STATUS2).CONVERSION_TYPE = ISL94203_FLOAT_CONVERSION_TYPES.BIT;






            PARAMETERS.get(ISL94203ConfigurationParameters.STATUS3).PARAM_TYPE = ISL94203ConfigurationParameters.STATUS3;
            PARAMETERS.get(ISL94203ConfigurationParameters.STATUS3).Address.add(0x82);
            PARAMETERS.get(ISL94203ConfigurationParameters.STATUS3).ParameterName = "Status 3";
            PARAMETERS.get(ISL94203ConfigurationParameters.STATUS3).ParameterDescription = "";
            PARAMETERS.get(ISL94203ConfigurationParameters.STATUS3).ParameterPhysical_Unit = "NA";
            PARAMETERS.get(ISL94203ConfigurationParameters.STATUS3).BitNames = new ArrayList<>(Arrays.asList("LD PRSNT", "CH PRSNT", "CHING", "DSCHING", "ECC USED", "ECC FAIL", "INT_SCAN", "LVCHG", "-", "-", "-", "-", "-", "-", "-", "-"));
            PARAMETERS.get(ISL94203ConfigurationParameters.STATUS3).BitValues = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, -1, -1, -1, -1, -1));
            PARAMETERS.get(ISL94203ConfigurationParameters.STATUS3).reg = new ArrayList<>(Arrays.asList(0));
            PARAMETERS.get(ISL94203ConfigurationParameters.STATUS3).Number_Of_Bytes = 1;
            PARAMETERS.get(ISL94203ConfigurationParameters.STATUS3).CONVERSION_TYPE = ISL94203_FLOAT_CONVERSION_TYPES.BIT;







            PARAMETERS.get(ISL94203ConfigurationParameters.STATUS4).PARAM_TYPE = ISL94203ConfigurationParameters.STATUS4;
            PARAMETERS.get(ISL94203ConfigurationParameters.STATUS4).Address.add(0x83);
            PARAMETERS.get(ISL94203ConfigurationParameters.STATUS4).ParameterName = "Status 4";
            PARAMETERS.get(ISL94203ConfigurationParameters.STATUS4).ParameterDescription = "";
            PARAMETERS.get(ISL94203ConfigurationParameters.STATUS4).ParameterPhysical_Unit = "NA";
            PARAMETERS.get(ISL94203ConfigurationParameters.STATUS4).BitNames = new ArrayList<>(Arrays.asList("CBOT", "CBUT", "CBOV", "CBUV", "IN_IDLE", "IN_DOZE", "IN_SLEEP", "-", "-", "-", "-", "-", "-", "-", "-", "-"));
            PARAMETERS.get(ISL94203ConfigurationParameters.STATUS4).BitValues = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, -1, -1, -1, -1, -1));
            PARAMETERS.get(ISL94203ConfigurationParameters.STATUS4).reg = new ArrayList<>(Arrays.asList(0));
            PARAMETERS.get(ISL94203ConfigurationParameters.STATUS4).Number_Of_Bytes = 1;
            PARAMETERS.get(ISL94203ConfigurationParameters.STATUS4).CONVERSION_TYPE = ISL94203_FLOAT_CONVERSION_TYPES.BIT;





            PARAMETERS.get(ISL94203ConfigurationParameters.Pack_Current).PARAM_TYPE = ISL94203ConfigurationParameters.Pack_Current;
            PARAMETERS.get(ISL94203ConfigurationParameters.Pack_Current).Address.add(0x8E);
            PARAMETERS.get(ISL94203ConfigurationParameters.Pack_Current).Address.add(0x8F);
            PARAMETERS.get(ISL94203ConfigurationParameters.Pack_Current).ParameterName = "Pack Current";
            PARAMETERS.get(ISL94203ConfigurationParameters.Pack_Current).ParameterDescription = "This is the current flowing into or out of the pack.";
            PARAMETERS.get(ISL94203ConfigurationParameters.Pack_Current).ParameterPhysical_Unit = "A";
            PARAMETERS.get(ISL94203ConfigurationParameters.Pack_Current).BitNames = new ArrayList<>(Arrays.asList("ISNS0", "ISNS1", "ISNS2", "ISNS3", "ISNS4", "ISNS5", "ISNS6", "ISNS7", "ISNS8", "ISNS9", "ISNSA", "ISNSB", "-", "-", "-", "-"));
            PARAMETERS.get(ISL94203ConfigurationParameters.Pack_Current).BitValues = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
            PARAMETERS.get(ISL94203ConfigurationParameters.Pack_Current).reg = new ArrayList<>(Arrays.asList(0, 0));
            PARAMETERS.get(ISL94203ConfigurationParameters.Pack_Current).Number_Of_Bytes = 2;
            PARAMETERS.get(ISL94203ConfigurationParameters.Pack_Current).CONVERSION_TYPE = ISL94203_FLOAT_CONVERSION_TYPES.REG_TO_PACK_CURRENT;







            PARAMETERS.get(ISL94203ConfigurationParameters.Low_Voltage_Charge_Level).PARAM_TYPE = ISL94203ConfigurationParameters.Low_Voltage_Charge_Level;
            PARAMETERS.get(ISL94203ConfigurationParameters.Low_Voltage_Charge_Level).Address.add(0x0E);
            PARAMETERS.get(ISL94203ConfigurationParameters.Low_Voltage_Charge_Level).Address.add(0x0F);
            PARAMETERS.get(ISL94203ConfigurationParameters.Low_Voltage_Charge_Level).ParameterName = "Low Voltage Change Level";
            PARAMETERS.get(ISL94203ConfigurationParameters.Low_Voltage_Charge_Level).ParameterDescription = "If the voltage on any cell is less than this level, then the PCFET output turns on instead of the PC output. To disable this function, set the value to zero or set the PCFETE bit to 0.";
            PARAMETERS.get(ISL94203ConfigurationParameters.Low_Voltage_Charge_Level).ParameterPhysical_Unit = "V";
            PARAMETERS.get(ISL94203ConfigurationParameters.Low_Voltage_Charge_Level).BitNames = new ArrayList<>(Arrays.asList("LVCH0", "LVCH1", "LVCH2", "LVCH3", "LVCH4", "LVCH5", "LVCH6", "LVCH7", "LVCH8", "LVCH9", "LVCHA", "LVCHB", "-", "-", "-", "-"));
            PARAMETERS.get(ISL94203ConfigurationParameters.Low_Voltage_Charge_Level).BitValues = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
            PARAMETERS.get(ISL94203ConfigurationParameters.Low_Voltage_Charge_Level).reg = new ArrayList<>(Arrays.asList(0, 0));
            PARAMETERS.get(ISL94203ConfigurationParameters.Low_Voltage_Charge_Level).Number_Of_Bytes = 2;
            PARAMETERS.get(ISL94203ConfigurationParameters.Low_Voltage_Charge_Level).CONVERSION_TYPE = ISL94203_FLOAT_CONVERSION_TYPES.REG_TO_VOLT;








            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Delay_Timeout).PARAM_TYPE = ISL94203ConfigurationParameters.Over_Voltage_Delay_Timeout;
            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Delay_Timeout).Address.add(0x10);
            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Delay_Timeout).Address.add(0x11);
            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Delay_Timeout).ParameterName = "Over Voltage Delay Timeout";
            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Delay_Timeout).ParameterDescription = "This value sets the time that is required for any cell to be above the overvoltage threshold before an overvoltage condition is detected";
            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Delay_Timeout).ParameterPhysical_Unit = "-";
            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Delay_Timeout).BitNames = new ArrayList<>(Arrays.asList("OVDT0", "OVDT1", "OVDT2", "OVDT3", "OVDT4", "OVDT5", "OVDT6", "OVDT7", "OVDT8", "OVDT9", "OVDTA", "OVDTB", "-", "-", "-", "-"));
            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Delay_Timeout).BitValues = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Delay_Timeout).reg = new ArrayList<>(Arrays.asList(0, 0));
            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Delay_Timeout).Number_Of_Bytes = 2;
            PARAMETERS.get(ISL94203ConfigurationParameters.Over_Voltage_Delay_Timeout).CONVERSION_TYPE = ISL94203_FLOAT_CONVERSION_TYPES.BIT;








            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Delay_Timeout).PARAM_TYPE = ISL94203ConfigurationParameters.Under_Voltage_Delay_Timeout;
            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Delay_Timeout).Address.add(0x12);
            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Delay_Timeout).Address.add(0x13);
            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Delay_Timeout).ParameterName = "Undervoltage Delay Time Out";
            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Delay_Timeout).ParameterDescription = "This value sets the time that is required for any cell to be below the undervoltage threshold before an undervoltage condition is detected.";
            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Delay_Timeout).ParameterPhysical_Unit = "-";
            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Delay_Timeout).BitNames = new ArrayList<>(Arrays.asList("UVDT0", "UVDT1", "UVDT2", "UVDT3", "UVDT4", "UVDT5", "UVDT6", "UVDT7", "UVDT8", "UVDT9", "UVDTA", "UVDTB", "-", "-", "-", "-"));
            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Delay_Timeout).BitValues = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Delay_Timeout).reg = new ArrayList<>(Arrays.asList(0, 0));
            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Delay_Timeout).Number_Of_Bytes = 2;
            PARAMETERS.get(ISL94203ConfigurationParameters.Under_Voltage_Delay_Timeout).CONVERSION_TYPE = ISL94203_FLOAT_CONVERSION_TYPES.BIT;









            PARAMETERS.get(ISL94203ConfigurationParameters.Open_Wire_Timing).PARAM_TYPE = ISL94203ConfigurationParameters.Open_Wire_Timing;
            PARAMETERS.get(ISL94203ConfigurationParameters.Open_Wire_Timing).Address.add(0x14);
            PARAMETERS.get(ISL94203ConfigurationParameters.Open_Wire_Timing).Address.add(0x15);
            PARAMETERS.get(ISL94203ConfigurationParameters.Open_Wire_Timing).ParameterName = "Open-Wire Timing (OWT)";
            PARAMETERS.get(ISL94203ConfigurationParameters.Open_Wire_Timing).ParameterDescription = "This value sets the width of the open-wire test pulse for each cell input.";
            PARAMETERS.get(ISL94203ConfigurationParameters.Open_Wire_Timing).ParameterPhysical_Unit = "-";
            PARAMETERS.get(ISL94203ConfigurationParameters.Open_Wire_Timing).BitNames = new ArrayList<>(Arrays.asList("OWT0", "OWT1", "OWT2", "OWT3", "OWT4", "OWT5", "OWT6", "OWT7", "OWT8", "OWT9", "-", "-", "-", "-", "-", "-"));
            PARAMETERS.get(ISL94203ConfigurationParameters.Open_Wire_Timing).BitValues = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
            PARAMETERS.get(ISL94203ConfigurationParameters.Open_Wire_Timing).reg = new ArrayList<>(Arrays.asList(0, 0));
            PARAMETERS.get(ISL94203ConfigurationParameters.Open_Wire_Timing).Number_Of_Bytes = 2;
            PARAMETERS.get(ISL94203ConfigurationParameters.Open_Wire_Timing).CONVERSION_TYPE = ISL94203_FLOAT_CONVERSION_TYPES.BIT;








            PARAMETERS.get(ISL94203ConfigurationParameters.Discharge_Over_Current_Timeout_Threshold).PARAM_TYPE = ISL94203ConfigurationParameters.Discharge_Over_Current_Timeout_Threshold;
            PARAMETERS.get(ISL94203ConfigurationParameters.Discharge_Over_Current_Timeout_Threshold).Address.add(0x16);
            PARAMETERS.get(ISL94203ConfigurationParameters.Discharge_Over_Current_Timeout_Threshold).Address.add(0x17);
            PARAMETERS.get(ISL94203ConfigurationParameters.Discharge_Over_Current_Timeout_Threshold).ParameterName = "Discharge Overcurrent Time Out/Threshold";
            PARAMETERS.get(ISL94203ConfigurationParameters.Discharge_Over_Current_Timeout_Threshold).ParameterDescription = "A discharge overcurrent needs to remain for this time period prior to entering a discharge overcurrent condition. This is an 12-bit value: Lower 10 bits sets the time. Upper bits sets the time base.";
            PARAMETERS.get(ISL94203ConfigurationParameters.Discharge_Over_Current_Timeout_Threshold).ParameterPhysical_Unit = "-";
            PARAMETERS.get(ISL94203ConfigurationParameters.Discharge_Over_Current_Timeout_Threshold).BitNames = new ArrayList<>(Arrays.asList("OCDT0", "OCDT1", "OCDT2", "OCDT3", "OCDT4", "OCDT5", "OCDT6", "OCDT7", "OCDT8", "OCDT9", "OCDTA", "OCDTB", "OCD0", "OCD1", "OCD2", "-"));
            PARAMETERS.get(ISL94203ConfigurationParameters.Discharge_Over_Current_Timeout_Threshold).BitValues = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
            PARAMETERS.get(ISL94203ConfigurationParameters.Discharge_Over_Current_Timeout_Threshold).reg = new ArrayList<>(Arrays.asList(0, 0));
            PARAMETERS.get(ISL94203ConfigurationParameters.Discharge_Over_Current_Timeout_Threshold).Number_Of_Bytes = 2;
            PARAMETERS.get(ISL94203ConfigurationParameters.Discharge_Over_Current_Timeout_Threshold).CONVERSION_TYPE = ISL94203_FLOAT_CONVERSION_TYPES.BIT;









            PARAMETERS.get(ISL94203ConfigurationParameters.Charge_Over_Current_Timeout_Threshold).PARAM_TYPE = ISL94203ConfigurationParameters.Charge_Over_Current_Timeout_Threshold;
            PARAMETERS.get(ISL94203ConfigurationParameters.Charge_Over_Current_Timeout_Threshold).Address.add(0x18);
            PARAMETERS.get(ISL94203ConfigurationParameters.Charge_Over_Current_Timeout_Threshold).Address.add(0x19);
            PARAMETERS.get(ISL94203ConfigurationParameters.Charge_Over_Current_Timeout_Threshold).ParameterName = "Charge Overcurrent Time Out/Threshold";
            PARAMETERS.get(ISL94203ConfigurationParameters.Charge_Over_Current_Timeout_Threshold).ParameterDescription = "A charge overcurrent needs to remain for this time period prior to entering a charge overcurrent condition. This is an 12-bit value: Lower 10 bits sets the time. Upper bits set the time base. ";
            PARAMETERS.get(ISL94203ConfigurationParameters.Charge_Over_Current_Timeout_Threshold).ParameterPhysical_Unit = "-";
            PARAMETERS.get(ISL94203ConfigurationParameters.Charge_Over_Current_Timeout_Threshold).BitNames = new ArrayList<>(Arrays.asList("OCCT0", "OCCT1", "OCCT2", "OCCT3", "OCCT4", "OCCT5", "OCCT6", "OCCT7", "OCCT8", "OCCT9", "OCCTA", "OCCTB", "OCC0", "OCC1", "OCC2", "-"));
            PARAMETERS.get(ISL94203ConfigurationParameters.Charge_Over_Current_Timeout_Threshold).BitValues = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
            PARAMETERS.get(ISL94203ConfigurationParameters.Charge_Over_Current_Timeout_Threshold).reg = new ArrayList<>(Arrays.asList(0, 0));
            PARAMETERS.get(ISL94203ConfigurationParameters.Charge_Over_Current_Timeout_Threshold).Number_Of_Bytes = 2;
            PARAMETERS.get(ISL94203ConfigurationParameters.Charge_Over_Current_Timeout_Threshold).CONVERSION_TYPE = ISL94203_FLOAT_CONVERSION_TYPES.BIT;










            PARAMETERS.get(ISL94203ConfigurationParameters.Discharge_Short_Circuit_Timeout_Threshold).PARAM_TYPE = ISL94203ConfigurationParameters.Discharge_Short_Circuit_Timeout_Threshold;
            PARAMETERS.get(ISL94203ConfigurationParameters.Discharge_Short_Circuit_Timeout_Threshold).Address.add(0x1A);
            PARAMETERS.get(ISL94203ConfigurationParameters.Discharge_Short_Circuit_Timeout_Threshold).Address.add(0x1B);
            PARAMETERS.get(ISL94203ConfigurationParameters.Discharge_Short_Circuit_Timeout_Threshold).ParameterName = "Discharge Short-Circuit Time Out/Threshold";
            PARAMETERS.get(ISL94203ConfigurationParameters.Discharge_Short_Circuit_Timeout_Threshold).ParameterDescription = "A short-circuit current needs to remain for this time period prior to entering a short-circuit condition. This is an 12 bit value: Lower 10 bits sets the time. Upper bits set the time base";
            PARAMETERS.get(ISL94203ConfigurationParameters.Discharge_Short_Circuit_Timeout_Threshold).ParameterPhysical_Unit = "V";
            PARAMETERS.get(ISL94203ConfigurationParameters.Discharge_Short_Circuit_Timeout_Threshold).BitNames = new ArrayList<>(Arrays.asList("SCT0", "SCT1", "SCT2", "SCT3", "SCT4", "SCT5", "SCT6", "SCT7", "SCT8", "SCT9", "SCTA", "SCTB", "SCD0", "SCD1", "SCD2", "-"));
            PARAMETERS.get(ISL94203ConfigurationParameters.Discharge_Short_Circuit_Timeout_Threshold).BitValues = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
            PARAMETERS.get(ISL94203ConfigurationParameters.Discharge_Short_Circuit_Timeout_Threshold).reg = new ArrayList<>(Arrays.asList(0, 0));
            PARAMETERS.get(ISL94203ConfigurationParameters.Discharge_Short_Circuit_Timeout_Threshold).Number_Of_Bytes = 2;
            PARAMETERS.get(ISL94203ConfigurationParameters.Discharge_Short_Circuit_Timeout_Threshold).CONVERSION_TYPE = ISL94203_FLOAT_CONVERSION_TYPES.BIT;













            PARAMETERS.get(ISL94203ConfigurationParameters.Cell_Balance_Min_Voltage).PARAM_TYPE = ISL94203ConfigurationParameters.Cell_Balance_Min_Voltage;
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell_Balance_Min_Voltage).Address.add(0x1C);
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell_Balance_Min_Voltage).Address.add(0x1D);
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell_Balance_Min_Voltage).ParameterName = "Cell Balance Minimum Voltage (CBMIN)";
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell_Balance_Min_Voltage).ParameterDescription = "If all cell voltages are less than this voltage, then cell balance stops. ";
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell_Balance_Min_Voltage).ParameterPhysical_Unit = "V";
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell_Balance_Min_Voltage).BitNames = new ArrayList<>(Arrays.asList("CBVL0", "CBVL1", "CBVL2", "CBVL3", "CBVL4", "CBVL5", "CBVL6", "CBVL7", "CBVL8", "CBVL9", "CBVLA", "CBVLB", "-", "-", "-", "-"));
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell_Balance_Min_Voltage).BitValues = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell_Balance_Min_Voltage).reg = new ArrayList<>(Arrays.asList(0, 0));
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell_Balance_Min_Voltage).Number_Of_Bytes = 2;
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell_Balance_Min_Voltage).CONVERSION_TYPE = ISL94203_FLOAT_CONVERSION_TYPES.REG_TO_VOLT;
















            PARAMETERS.get(ISL94203ConfigurationParameters.Cell_Balance_Maximum_Voltage).PARAM_TYPE = ISL94203ConfigurationParameters.Cell_Balance_Maximum_Voltage;
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell_Balance_Maximum_Voltage).Address.add(0x1E);
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell_Balance_Maximum_Voltage).Address.add(0x1F);
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell_Balance_Maximum_Voltage).ParameterName = "Cell Balance Maximum Voltage (CBMAX)";
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell_Balance_Maximum_Voltage).ParameterDescription = "If all cell voltages are greater than this voltage, then cell balance stops. ";
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell_Balance_Maximum_Voltage).ParameterPhysical_Unit = "V";
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell_Balance_Maximum_Voltage).BitNames =   new ArrayList<>(Arrays.asList("CBVU0", "CBVU1", "CBVU2", "CBVU3", "CBVU4", "CBVU5", "CBVU6", "CBVU7", "CBVU8", "CBVU9", "CBVUA", "CBVUB", "-", "-", "-", "-"));
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell_Balance_Maximum_Voltage).BitValues =   new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell_Balance_Maximum_Voltage).reg =   new ArrayList<>(Arrays.asList(0, 0));
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell_Balance_Maximum_Voltage).Number_Of_Bytes = 2;
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell_Balance_Maximum_Voltage).CONVERSION_TYPE = ISL94203_FLOAT_CONVERSION_TYPES.REG_TO_VOLT;


            PARAMETERS.get(ISL94203ConfigurationParameters.Cell8_Voltage).PARAM_TYPE = ISL94203ConfigurationParameters.Cell8_Voltage;
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell8_Voltage).Address.add(0x9E);
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell8_Voltage).Address.add(0x9F);
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell8_Voltage).ParameterName = "Cell8 Voltage";
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell8_Voltage).ParameterDescription = "This is the voltage of CELL8.";
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell8_Voltage).ParameterPhysical_Unit = "V";
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell8_Voltage).BitNames =   new ArrayList<>(Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"));
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell8_Voltage).BitValues =   new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell8_Voltage).reg =   new ArrayList<>(Arrays.asList(0, 0));
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell8_Voltage).Number_Of_Bytes = 2;
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell8_Voltage).CONVERSION_TYPE = ISL94203_FLOAT_CONVERSION_TYPES.REG_TO_VOLT;


            PARAMETERS.get(ISL94203ConfigurationParameters.Cell2_Voltage).PARAM_TYPE = ISL94203ConfigurationParameters.Cell2_Voltage;
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell2_Voltage).Address.add(0x92);
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell2_Voltage).Address.add(0x93);
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell2_Voltage).ParameterName = "Cell2 Voltage";
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell2_Voltage).ParameterDescription = "This is the voltage of CELL2.";
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell2_Voltage).ParameterPhysical_Unit = "V";
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell2_Voltage).BitNames =   new ArrayList<>(Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"));
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell2_Voltage).BitValues =   new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell2_Voltage).reg =   new ArrayList<>(Arrays.asList(0, 0));
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell2_Voltage).Number_Of_Bytes = 2;
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell2_Voltage).CONVERSION_TYPE = ISL94203_FLOAT_CONVERSION_TYPES.REG_TO_VOLT;


            PARAMETERS.get(ISL94203ConfigurationParameters.Cell1_Voltage).PARAM_TYPE = ISL94203ConfigurationParameters.Cell1_Voltage;
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell1_Voltage).Address.add(0x90);
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell1_Voltage).Address.add(0x91);
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell1_Voltage).ParameterName = "Cell1 Voltage";
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell1_Voltage).ParameterDescription = "This is the voltage of CELL1.";
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell1_Voltage).ParameterPhysical_Unit = "V";
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell1_Voltage).BitNames =   new ArrayList<>(Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"));
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell1_Voltage).BitValues =   new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell1_Voltage).reg =   new ArrayList<>(Arrays.asList(0, 0));
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell1_Voltage).Number_Of_Bytes = 2;
            PARAMETERS.get(ISL94203ConfigurationParameters.Cell1_Voltage).CONVERSION_TYPE = ISL94203_FLOAT_CONVERSION_TYPES.REG_TO_VOLT;







            PARAMETERS.get(ISL94203ConfigurationParameters.Sleep_Mode_Timer_Cell_Configuration).PARAM_TYPE = ISL94203ConfigurationParameters.Sleep_Mode_Timer_Cell_Configuration;
            PARAMETERS.get(ISL94203ConfigurationParameters.Sleep_Mode_Timer_Cell_Configuration).Address.add(0x48);
            PARAMETERS.get(ISL94203ConfigurationParameters.Sleep_Mode_Timer_Cell_Configuration).Address.add(0x49);
            PARAMETERS.get(ISL94203ConfigurationParameters.Sleep_Mode_Timer_Cell_Configuration).ParameterName = "Sleep Mode Timer/Cell Configuration";
            PARAMETERS.get(ISL94203ConfigurationParameters.Sleep_Mode_Timer_Cell_Configuration).ParameterDescription = "Only these combinations are acceptable. Any other combination will prevent any FET from turning on.. - Time required to enter Sleep mode from the Doze mode when no current is detected.";
            PARAMETERS.get(ISL94203ConfigurationParameters.Sleep_Mode_Timer_Cell_Configuration).ParameterPhysical_Unit = "NA";
            PARAMETERS.get(ISL94203ConfigurationParameters.Sleep_Mode_Timer_Cell_Configuration).BitNames = new ArrayList<>(Arrays.asList("MOD0", "MOD1", "MOD2", "MOD3", "MOD4", "MOD5", "MOD6", "MOD7", "CELL1", "CELL2", "CELL3", "CELL4", "CELL5", "CELL6", "CELL7", "CELL8"));
            PARAMETERS.get(ISL94203ConfigurationParameters.Sleep_Mode_Timer_Cell_Configuration).BitValues =  new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
            PARAMETERS.get(ISL94203ConfigurationParameters.Sleep_Mode_Timer_Cell_Configuration).reg =  new ArrayList<>(Arrays.asList(0, 0));
            PARAMETERS.get(ISL94203ConfigurationParameters.Sleep_Mode_Timer_Cell_Configuration).Number_Of_Bytes = 2;
            PARAMETERS.get(ISL94203ConfigurationParameters.Sleep_Mode_Timer_Cell_Configuration).CONVERSION_TYPE = ISL94203_FLOAT_CONVERSION_TYPES.BIT;




            AVAILABLE_PARAM.addAll(new ArrayList<ISL94203ConfigurationParameters>(Arrays.asList(
//                    ISL94203ConfigurationParameters.Sleep_Mode_Timer_Cell_Configuration,


//                    ISL94203ConfigurationParameters.Analog_Mux_Control_Bits,

                    ISL94203ConfigurationParameters.Cell8_Voltage,

                    ISL94203ConfigurationParameters.Cell2_Voltage,

                    ISL94203ConfigurationParameters.Cell1_Voltage,
//
//                    ISL94203ConfigurationParameters.Cell_Balance_Maximum_Voltage,
//
//                    ISL94203ConfigurationParameters.Cell_Balance_Min_Voltage,
//
//                    ISL94203ConfigurationParameters.Discharge_Short_Circuit_Timeout_Threshold,
//
//                    ISL94203ConfigurationParameters.Charge_Over_Current_Timeout_Threshold,
//
//                    ISL94203ConfigurationParameters.Discharge_Over_Current_Timeout_Threshold,
//
//                    ISL94203ConfigurationParameters.Open_Wire_Timing,
//
//                    ISL94203ConfigurationParameters.Under_Voltage_Delay_Timeout,
//
//                    ISL94203ConfigurationParameters.Over_Voltage_Delay_Timeout,
//
//                    ISL94203ConfigurationParameters.Low_Voltage_Charge_Level,

                    ISL94203ConfigurationParameters.Pack_Current,

//                    ISL94203ConfigurationParameters.STATUS4,
//
                    ISL94203ConfigurationParameters.STATUS3,
//
//                    ISL94203ConfigurationParameters.STATUS2
//
//                    ISL94203ConfigurationParameters.STATUS1
//
//                    ISL94203ConfigurationParameters.CONTROL1,
//
//                    ISL94203ConfigurationParameters.CONTROL2,
//
//                    ISL94203ConfigurationParameters.CONTROL3



                    ISL94203ConfigurationParameters.End_Of_Charge_Threshold
//
//                    ISL94203ConfigurationParameters.Under_Voltage_Lockout_Threshold,
//
//                    ISL94203ConfigurationParameters.Over_Voltage_Lockout_Threshold,
//
//                    ISL94203ConfigurationParameters.Under_Voltage_Recovery,
//
//                    ISL94203ConfigurationParameters.Under_Voltage_Threshold,
//
//                    ISL94203ConfigurationParameters. Over_Voltage_Recovery,
//
//                    ISL94203ConfigurationParameters.Over_Voltage_Threshold
            )));

        }
}
