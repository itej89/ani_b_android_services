package client.ani.fouriermachines.fouriermachinesaniclientisl94203.DataTypes.Constants;

public enum ISL94203ConfigurationParameters
        {
         Over_Voltage_Threshold(0),
         Over_Voltage_Recovery(1),
         Under_Voltage_Threshold(2),
         Under_Voltage_Recovery (3),
         Over_Voltage_Lockout_Threshold(4),
         Under_Voltage_Lockout_Threshold(5),
         End_Of_Charge_Threshold(6),
         Low_Voltage_Charge_Level(7),
         Over_Voltage_Delay_Timeout(8),
         Under_Voltage_Delay_Timeout(9),
         Open_Wire_Timing(10),
         Discharge_Over_Current_Timeout_Threshold(11),
         Charge_Over_Current_Timeout_Threshold(12),
         Discharge_Short_Circuit_Timeout_Threshold(13),
         Cell_Balance_Min_Voltage(14),
         Cell_Balance_Maximum_Voltage(15),
         Cell_Balance_Minimum_Differential_Voltage(16),
         Cell_Balance_Maximum_Differential_Voltage(17),
         Cell_Balance_on_Time(18),
         Cell_Balance_Off_Time(19),
         Cell_Balance_Minimum_Temperature_Limit(20),
         Cell_Balance_Minimum_Temperature_Recovery_Level(21),
         Cell_Balance_Maximum_Temperature_Limit(22),
         Cell_Balance_Maximum_Temperature_Recovery_Level(23),
         Charge_Over_Temperature_Voltage(24),
         Charge_Over_Temperature_Recovery_Voltage(25),
         Charge_Under_Temperature_Voltage(26),
         Charge_Under_Temperature_Recovery_Voltage(27),
         Discharge_Over_Temperature_Voltage(28),
         Discharge_Over_Temperature_Recovery_Voltage(29),
         Discharge_Under_Temperature_Voltage(30),
         Discharge_Under_Temperature_Recovery_Voltage(31),
         Internal_Over_Temperature_Voltage(32),
         Internal_Over_Temperature_Recovery_Voltage(33),
         Sleep_Level_Voltage(34),
         Sleep_Delay_Timer_Watchdog_Timer(35),
         Sleep_Mode_Timer_Cell_Configuration(36),
         CONFIG1(37),
         CONFIG2(38),


         STATUS1(39),
         STATUS2(40),
         STATUS3(41),
         STATUS4(42),
         Cell_Balance_FET_control(43),
         Analog_Mux_Control_Bits(44),
         CONTROL1(45),
         CONTROL2(46),
         CONTROL3(47),
         EEPROM_ENABLE(48),
         Cell_Minimum_Voltage(49),
         Cell_Maximum_Voltage(50),
         Pack_Current(51),
         Cell1_Voltage(52),
         Cell2_Voltage(53),
         Cell3_Voltage(54),
         Cell4_Voltage(55),
         Cell5_Voltage(56),
         Cell6_Voltage(57),
         Cell7_Voltage(58),
         Cell8_Voltage(59),
         Internal_Temperature(60),
         External_Temperature1(61),
         External_Temperature2(62),
         VBATT_Votlage(63),
         VRGO_Voltage(64),
         ADC_Voltage_14Bit(65),

         NA(66);

            private final int value;
            private ISL94203ConfigurationParameters(int value) {
        this.value = value;
        }

public int getValue() {
        return value;
        }


         public static ISL94203ConfigurationParameters ConvertFromInt(int s)
         {
          switch (s)
          {
           case 0:
            return  Over_Voltage_Threshold;
           case  1:
            return  Over_Voltage_Recovery;
           case  2:
            return  Under_Voltage_Threshold;
           case  3:
            return  Under_Voltage_Recovery;
           case  4:
            return  Over_Voltage_Lockout_Threshold;
           case  5:
            return  Under_Voltage_Lockout_Threshold;
           case  6:
            return  End_Of_Charge_Threshold;
           case  7:
            return  Low_Voltage_Charge_Level;
           case  8:
            return  Over_Voltage_Delay_Timeout;
           case  9:
            return  Under_Voltage_Delay_Timeout;
           case  10:
            return  Open_Wire_Timing;
           case  11:
            return  Discharge_Over_Current_Timeout_Threshold;
           case  12:
            return  Charge_Over_Current_Timeout_Threshold;
           case  13:
            return  Discharge_Short_Circuit_Timeout_Threshold;
           case  14:
            return  Cell_Balance_Min_Voltage;
           case  15:
            return  Cell_Balance_Maximum_Voltage;
           case  16:
            return  Cell_Balance_Minimum_Differential_Voltage;
           case  17:
            return  Cell_Balance_Maximum_Differential_Voltage;
           case  18:
            return  Cell_Balance_on_Time;
           case  19:
            return  Cell_Balance_Off_Time;
           case  20:
            return  Cell_Balance_Minimum_Temperature_Limit;
           case  21:
            return  Cell_Balance_Minimum_Temperature_Recovery_Level;
           case 22:
            return  Cell_Balance_Maximum_Temperature_Limit;
           case  23:
            return  Cell_Balance_Maximum_Temperature_Recovery_Level;
           case  24:
            return  Charge_Over_Temperature_Voltage;
           case  25:
            return  Charge_Over_Temperature_Recovery_Voltage;
           case  26:
            return  Charge_Under_Temperature_Voltage;
           case  27:
            return  Charge_Under_Temperature_Recovery_Voltage;
           case  28:
            return  Discharge_Over_Temperature_Voltage;
           case  29:
            return  Discharge_Over_Temperature_Recovery_Voltage;
           case  30:
            return  Discharge_Under_Temperature_Voltage;
           case  31:
            return  Discharge_Under_Temperature_Recovery_Voltage;
           case  32:
            return  Internal_Over_Temperature_Voltage;
           case  33:
            return  Internal_Over_Temperature_Recovery_Voltage;
           case  34:
            return  Sleep_Level_Voltage;
           case  35:
            return  Sleep_Delay_Timer_Watchdog_Timer;
           case  36:
            return  Sleep_Mode_Timer_Cell_Configuration;
           case  37:
            return  CONFIG1;
           case  38:
            return  CONFIG2;
           case  39:
            return  STATUS1;
           case  40:
            return  STATUS2;
           case  41:
            return  STATUS3;
           case  42:
            return  STATUS4;
           case  43:
            return  Cell_Balance_FET_control;
           case  44:
            return  Analog_Mux_Control_Bits;
           case  45:
            return  CONTROL1;
           case  46:
            return  CONTROL2;
           case  47:
            return  CONTROL3;
           case  48:
            return  EEPROM_ENABLE;
           case  49:
            return  Cell_Minimum_Voltage;
           case  50:
            return  Cell_Maximum_Voltage;
           case  51:
            return  Pack_Current;
           case  52:
            return  Cell1_Voltage;
           case  53:
            return  Cell2_Voltage;
           case  54:
            return  Cell3_Voltage;
           case  55:
            return  Cell4_Voltage;
           case  56:
            return  Cell5_Voltage;
           case  57:
            return  Cell6_Voltage;
           case  58:
            return  Cell7_Voltage;
           case  59:
            return  Cell8_Voltage;
           case  60:
            return  Internal_Temperature;
           case  61:
            return  External_Temperature1;
           case  62:
            return  External_Temperature2;
           case  63:
            return  VBATT_Votlage;
           case  64:
            return  VRGO_Voltage;
           case  65:
            return  ADC_Voltage_14Bit;
           case  66:
            return  NA;
          }
          return  null;
         }

        }

