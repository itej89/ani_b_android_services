package FrameworkInterface;

import android.util.Pair;
import java.util.ArrayList;
import java.util.UUID;
import Framework.DataTypes.MQTTMetaData;
import Framework.MqttClientManager;
import FrameworkInterface.DataTypes.FrameParsers.Parsers.RxFrameParser;
import FrameworkInterface.DataTypes.FrameParsers.RxFrames.ALIVE;
import FrameworkInterface.DataTypes.FrameParsers.RxFrames.CATEGORY_ACK;
import FrameworkInterface.DataTypes.FrameParsers.RxFrames.COMMAND_ACK;
import FrameworkInterface.DataTypes.FrameParsers.RxFrames.CONNECT_ACK;
import FrameworkInterface.DataTypes.FrameParsers.RxFrames.DATA_ACK;
import FrameworkInterface.DataTypes.FrameParsers.RxFrames.DISCONNECT_ACK;
import FrameworkInterface.DataTypes.FrameParsers.RxFrames.FIND;
import FrameworkInterface.DataTypes.FrameParsers.RxFrames.REQEST_UPLOAD_ACK;
import FrameworkInterface.DataTypes.FrameParsers.RxFrames.UPLOAD_END_ACK;
import FrameworkInterface.DataTypes.FrameParsers.TxFrames.TxBaseFrame;
import FrameworkInterface.Delegates.MqttInterfaceConvey;
import FrameworkInterface.Enums.ACK;
import FrameworkInterface.Enums.ANSTMSG;
import FrameworkInterface.Enums.CATEGORY_TYPES;
import FrameworkInterface.Enums.COMMAND_TYPES;
import FrameworkInterface.Enums.IOTIntents;

public class MQTTInterfaceImplementation extends MqttClientManager implements MqttClientAccess {

    public static MQTTInterfaceImplementation Instance = new MQTTInterfaceImplementation();

    enum MQSTATES {CONNECTED, DISCONENCTED}

    public final String ID =  UUID.randomUUID().toString();
    public final String NAME =  "A.n.i B"+UUID.randomUUID().toString();


    final String PublishToLight = "ANI_HOME/feeds/LIGHT";
    final String SubscribeToLight = "ANI_HOME/feeds/LIGHT/ani-studio";

    final String PublishTo3DPrinter = "Ender3/Octo/Command";
    final String PublishToWav = "Wav/State/Command";




     ArrayList<String> SubscribeToIOT = new ArrayList<String>();


    public MQSTATES CURRENT_STATE = MQSTATES.DISCONENCTED;

    public ArrayList<String> SubscribedTopics = new ArrayList<>();


    MqttInterfaceConvey mQttIterfaceStudioNotify;


    private void MqttClientInterfaceImplementation()
    {
        SubscribeToIOT.add(SubscribeToLight);
    }


    public void SetStudioMessagteNotify(MqttInterfaceConvey delegate)
    {
        mQttIterfaceStudioNotify = delegate;
    }

    //MqttClientDelegates
    public  void ReciecvedMessage(String TopicName, String Message)
    {
                if(mQttIterfaceStudioNotify!=null)
                {
                    RxFrameParser rxFrameParser = new RxFrameParser();
                    if(Message != null && Message != "")
                    {
                        Pair<ANSTMSG, TxBaseFrame> state = rxFrameParser.GetRxObject(Message);
                        if(state.first != ANSTMSG.NA)
                        {
                            mQttIterfaceStudioNotify.FrameRecieved(state.first, Message);
                        }
                    }
                }
    }
    //End of MqttClientDelegates


    //MqttClientAccess
    public void ConnectToBroker(){
        if(CURRENT_STATE == MQSTATES.DISCONENCTED) {
            if (Connect()) {
                CURRENT_STATE = MQSTATES.CONNECTED;
            }
        }
    }

    public void DisconnectFromBroker(){
        if(CURRENT_STATE == MQSTATES.CONNECTED)
        {
            Disconnect();
            CURRENT_STATE = MQSTATES.DISCONENCTED;
        }
    }

    public void SubscribeTo(String Channel){
        Subscribe(Channel);
    }

    public void UnsubscribeFrom(String Channel){
        Unsubscribe(Channel);
    }

    public void AniDiscoverable(String Frame_ID, String ID, String NAME){
        FIND txFrame = new FIND(ID, NAME, ACK.OK);
        txFrame.FRAME_ID = Frame_ID;
        Send(Frame_ID, txFrame.Json(), MQTTMetaData.ScanChannel_Tx);
    }

    public void ConnectStudio(String Frame_ID, String ID){
        CONNECT_ACK txFrame = new CONNECT_ACK(ID, ACK.OK);
        txFrame.FRAME_ID = Frame_ID;
        Send(Frame_ID, txFrame.Json(), MQTTMetaData.ScanChannel_Tx);
    }

    public void DisconnectStudio(String Frame_ID, String ID){
        DISCONNECT_ACK txFrame = new DISCONNECT_ACK(ID, ACK.OK);
        txFrame.FRAME_ID = Frame_ID;
        Send(Frame_ID, txFrame.Json(), MQTTMetaData.ScanChannel_Tx);
    }

    public String SendAlive(String _ID){
        ALIVE txFrame = new ALIVE(_ID);
        txFrame.FRAME_ID = UUID.randomUUID().toString();
        Send(txFrame.FRAME_ID, txFrame.Json(), MQTTMetaData.AliveChannel_Tx);
        return  txFrame.FRAME_ID;
    }

    public void ANIActionModeSet(String Frame_ID, String _ID){
        CATEGORY_ACK txFrame = new CATEGORY_ACK(_ID, ACK.OK);
        txFrame.FRAME_ID = Frame_ID;
        Send(Frame_ID, txFrame.Json(), MQTTMetaData.CategoryChannel_Tx);
    }

    public void RequestUploadAck(String Frame_ID, String _ID){
        REQEST_UPLOAD_ACK txFrame = new REQEST_UPLOAD_ACK(_ID, ACK.OK);
        txFrame.FRAME_ID = Frame_ID;
        Send(Frame_ID, txFrame.Json(), MQTTMetaData.DataChannel_Tx);
    }

    public void SendDataAck(String Frame_ID, String _ID){
        DATA_ACK txFrame = new DATA_ACK(_ID, ACK.OK);
        txFrame.FRAME_ID = Frame_ID;
        Send(Frame_ID, txFrame.Json(), MQTTMetaData.DataChannel_Tx);
    }

    public void ExitUploadAck(String Frame_ID, String _ID){
        UPLOAD_END_ACK txFrame = new UPLOAD_END_ACK(_ID, ACK.OK);
        txFrame.FRAME_ID = Frame_ID;
        Send(Frame_ID, txFrame.Json(), MQTTMetaData.DataChannel_Tx);
    }

    public void SendCommandAck(String Frame_ID, String _ID){
        COMMAND_ACK txFrame = new COMMAND_ACK(_ID, ACK.OK);
        txFrame.FRAME_ID = Frame_ID;
        Send(Frame_ID, txFrame.Json(), MQTTMetaData.CommandChannel_Tx);
    }


    public boolean PublishIntentToIOT(IOTIntents Intent)
    {
        switch(Intent)
        {
            case LIGHTOFF:
                Send("NA", "0", PublishToLight);
                break;
            case LIGHTON:
                Send("NA", "1", PublishToLight);
                break;
            case PRINTERSTART3D:
                Send("NA", "print", PublishTo3DPrinter);
                break;
            case PRINTERSTOP3D:
                Send("NA", "stop", PublishTo3DPrinter);
                break;
            case PRINTERRESET3D:
                Send("NA", "resethome", PublishTo3DPrinter);
                break;
            case TAGTOWAV:
                Send("NA", "tagtowav", PublishToWav);
                break;
        }
        return  true;
    }
    //End of MqttClientAccess
}
