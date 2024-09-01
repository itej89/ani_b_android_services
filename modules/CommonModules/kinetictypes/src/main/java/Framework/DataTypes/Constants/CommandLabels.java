package Framework.DataTypes.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tej on 24/06/18.
 */

public class CommandLabels {

    public Map<CommandLabels.CommandTypes, Integer> CommandResponseCount = new HashMap<CommandLabels.CommandTypes,Integer>(){{
        put(CommandLabels.CommandTypes.ANG,1);
        put(CommandLabels.CommandTypes.TMG,1);
        put(CommandLabels.CommandTypes.TRG,1);
        put(CommandLabels.CommandTypes.ITRG,1);
        put(CommandLabels.CommandTypes.DEG,2);
        put(CommandLabels.CommandTypes.ATC,1);
        put(CommandLabels.CommandTypes.DTC,1);
        put(CommandLabels.CommandTypes.LLK,1);
        put(CommandLabels.CommandTypes.RLK,1);
        put(CommandLabels.CommandTypes.EAS,1);
        put(CommandLabels.CommandTypes.INO,1);
        put(CommandLabels.CommandTypes.PRX,2);
        put(CommandLabels.CommandTypes.CELL1,2);
        put(CommandLabels.CommandTypes.CELL2,2);
        put(CommandLabels.CommandTypes.CELL3,2);
        put(CommandLabels.CommandTypes.SLEEP,2);
        put(CommandLabels.CommandTypes.VEN,1);
        put(CommandLabels.CommandTypes.VNO,1);
        put(CommandLabels.CommandTypes.CPW,1);
        put(CommandLabels.CommandTypes.DPW,1);
        put(CommandLabels.CommandTypes.LAT,1);
        put(CommandLabels.CommandTypes.LDT,1);
        put(CommandLabels.CommandTypes.LOF,1);
        put(CommandLabels.CommandTypes.LON,1);
        put(CommandLabels.CommandTypes.SMV,2);
        put(CommandLabels.CommandTypes.SAT,2);
        put(CommandLabels.CommandTypes.SPW,2);
        put(CommandLabels.CommandTypes.LSA,2);
        put(CommandLabels.CommandTypes.LPW,2);
        put(CommandLabels.CommandTypes.FRQ,1);
        put(CommandLabels.CommandTypes.DEL,1);
        put(CommandLabels.CommandTypes.DMP,1);
        put(CommandLabels.CommandTypes.VEL,1);
        put(CommandLabels.CommandTypes.ISLR,2);
        put(CommandLabels.CommandTypes.ISLW,2);
        put(CommandLabels.CommandTypes.ISLER,2);
        put(CommandLabels.CommandTypes.ISLEW,2);
        put(CommandLabels.CommandTypes.EEPR,2);
        put(CommandLabels.CommandTypes.EEPW,2);
        put(CommandLabels.CommandTypes.POFF,1);
        put(CommandLabels.CommandTypes.SCAL,1);
        put(CommandLabels.CommandTypes.SEPR,2);
        put(CommandTypes.UNKNOWN,0);
    }};



        public  enum EasingFunction
        {
             SIN,
                QAD,
                        LIN,
                        EXP,
                        ELA,
                        CIR,
                        BOU,
                        BAK,
                        TRI,
                        TRW,
                        SNW,
                        SPR
        }

    public  enum EasingType{
         IN ,
            OU ,
                    IO
    }

    public enum CommandTypes
    {
         ANG,  // Servo command to set angle in PWM ~ANG#6#[PWM : 544-2400]:
         TMG,  // Servo command to set TIMING ~TMG#6#[ms]:
         TRG,  // Command to trigger servo motion ~TRG:
         ITRG, // Command to trigger servo motion with interrupt ~ITRG:
         DEG,  // Command to read current Servo angle in ADC ~DEG#6:
         ATC,  //Attach Servo ~ATC#6:
         DTC,  //Detach Servo ~DTC#6:
         LLK,  // Set Left Lock Servo Angle in PWM  ~LLK#6#PWM_VALUE:
         RLK,  // Set Right Lock Servo Angle in PWM ~RLK#6#PWM_VALUE:
         EAS,  //Set Servo Easing Func  ~EAS#6#SIN:
         INO,  // Set SErvo Easing Type ~INO#6#IN:
         PRX,  // read promity sensor.. Support sonly 9 address  ~PRX#9:
         CELL1,//Read CELL1 value in ADC   ~CELL1:
         CELL2,//Read CELL2 value in ADC   ~CELL2:
         CELL3,//Read CELL3 value in ADC   ~CELL3:
         SLEEP,
         VEN,  //Set Voice LED Fade     ~VEN:
         VNO,  //Stop Voice LED Fade    ~VNO:
         CPW,  //Connect servo Power    ~ATC#6:
         DPW,  //Disconnect servo power ~DTC#6:
         LAT,  //Attach lock servos     ~LAT#9:
         LDT,  //Detach lock servos     ~LDT#9:
         LON,  //Power on lock servos   ~LON#9:
         LOF,  //power off lock servos  ~LOF#9:
         SMV,  //REad if servo is moving or not ~SMV#6:
         SAT,  //REad if servo is attached or not ~SAT#6:
         SPW,  //REad if servo is powered on or not ~SPW#6:
         LSA,  //REad if lock servo attached or not ~LSA#6:
         LPW,  //REad if lock servo is powered or not ~LPW#6:
         FRQ,  //Sets the frequency of animation waveforms     ~FRQ#6#
         DEL,  //Sets the delay before motor starts animation  ~DEL#6#[ms]:
         DMP,  //range 1-10 sets the damping level of spring waveform motion   ~DMP#6#[1-10]:
         VEL,  //range 1-5  set the velocity of spring waveform motion   ~VEL#6#[1-5]:
         ISLR, //Reads Data from ISL94203 RAM Address
         ISLW, //Writes Data to ISL94203 RAM Address
         ISLER,//Reads Data from ISL94203 EEPROM Address
         ISLEW,//Writes Data to ISL94203 EEPROM Address
         EEPR, //Reads data from MAater Controller EEPROM
         EEPW, //Writes Data to  MAster Controlelr EEPROM
         POFF, //Request MAchine Power Off
         SCAL,//Start  Servo Calibration loop ~SCAL#ServoAddress:
         SEPR, //Read Servo EEPROM Data ~SEPR#ServoAddress#EEPROMAddress#NumberOfBytes:
         UNKNOWN
    }

   public enum RemoteCommandTypes {
         PBTN,
         VBTN
    }

}
