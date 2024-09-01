package Framework.DataTypes;

public class MQTTMetaData {
    public static final String CategoryChannel_Rx = "ani-studio-action_Tx";
    public static final String ScanChannel_Rx = "ani-studio-scan_Tx";
    public static final String DataChannel_Rx = "ani-studio-data_Tx";
    public static final String CommandChannel_Rx = "ani-studio-command_Tx";
    public static final String AliveChannel_Rx = "ani-studio-alive_Rx";


    public static final String CategoryChannel_Tx = "ani-studio-action_Rx";
    public static final String ScanChannel_Tx = "ani-studio-scan_Rx";
    public static final String DataChannel_Tx = "ani-studio-data_Rx";
    public static final String CommandChannel_Tx = "ani-studio-command_Rx";
    public static final String AliveChannel_Tx = "ani-studio-alive_Tx";

    public static String BROKER_IP = "tcp://192.168.0.189:1883";
    public static String CLIENT_ID = "ani-b-89";
}
