package Framework;
import android.util.Pair;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.ArrayList;
import java.util.UUID;

import Framework.DataTypes.BufferDataPacket;
import Framework.DataTypes.MQTTMetaData;
import FrameworkInterface.DataTypes.FrameParsers.Parsers.RxFrameParser;
import FrameworkInterface.DataTypes.FrameParsers.TxFrames.TxBaseFrame;
import FrameworkInterface.Delegates.MqttInterfaceConvey;
import FrameworkInterface.Enums.ANSTMSG;

public class MqttClientManager {




    MqttClient mqClient;
    MqttInterfaceConvey delegate;

    public String ID =  UUID.randomUUID().toString();
    public String NAME =  "anib"+UUID.randomUUID().toString();

    ArrayList<BufferDataPacket> PacketBuffer = new ArrayList<BufferDataPacket>();
    boolean IsSendingInProgess = false;

    public MqttClientManager()
    {

    }


    public void Initialize()
    {
        try {
            MemoryPersistence persistence = new MemoryPersistence();
            mqClient = new MqttClient(MQTTMetaData.BROKER_IP, MQTTMetaData.CLIENT_ID, persistence);
            //this.delegate = delegate;
        }
        catch (Exception e)
        {
            System.out.println("excep "+e);
        }
    }

    public MqttConnectOptions GetMqttConnectoptions()
    {
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setConnectionTimeout(3000);
        connOpts.setCleanSession(true);
        connOpts.setUserName("ANI_HOME");
        connOpts.setPassword("tejkiranani9".toCharArray());

        return  connOpts;
    }



    //MQTTAccess
    public  Boolean Connect() {

        try {
            Initialize();
            MqttConnectOptions connOpts = GetMqttConnectoptions();
            connOpts.setConnectionTimeout(1);

            mqClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    if(delegate != null) {
                        delegate.DisconnectedFromBroker(true);
                    }
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {

                    if(delegate != null)
                    {
                        String strFrame = message.toString();
                        RxFrameParser rxFrameParser = new RxFrameParser();
                        if(strFrame != null && strFrame != "")
                        {
                            Pair<ANSTMSG, TxBaseFrame> state = rxFrameParser.GetBaseFrame(strFrame);
                            if(state.first != ANSTMSG.NA)
                            {
                                delegate.FrameRecieved(state.second.jANSTMSG, strFrame);
                            }
                        }
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    if(delegate != null) {
                        delegate.FrameSent(LastSentFrameID, true);
                    }
                }
            });

            mqClient.connect(connOpts);


            if(mqClient.isConnected())
            {
                if(delegate != null) {
                    delegate.ConnectedToBroker(true);
                }
            }
            else
            {
                if(delegate != null) {
                    delegate.ConnectedToBroker(false);
                }
            }

            return  true;

        } catch(MqttException me) {
            Disconnect();
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
            return  false;
        }

    }

    public void Disconnect()
    {
        try {
            if (mqClient != null)
            mqClient.disconnect();
        }
        catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
    }



    public void Subscribe(String Topic)
    {
        try {
            if(mqClient != null) {
                mqClient.subscribe(Topic);
            }
        }
        catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
    }

    public void Unsubscribe(String Topic)
    {
        try {
            if(mqClient != null) {
                mqClient.unsubscribe(Topic);
            }
        }
        catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
    }
    //End of MQTTAccess

    String LastSentFrameID = "";
    public  void Send(String FrameID, String Data, String Topic) {

            BufferDataPacket DataPacket = new BufferDataPacket( FrameID, Data, Topic);
            PacketBuffer.add(DataPacket);

            PublishNextPacket();


    }


    void PublishNextPacket()
    {
        try {
        while(PacketBuffer.size() > 0)
        {


            BufferDataPacket NextPacket = PacketBuffer.get(0);
            if(mqClient != null) {


                MqttMessage message = new MqttMessage(NextPacket.data.getBytes());
                message.setQos(2);

                mqClient.publish(NextPacket.toChannel, message);
                PacketBuffer.remove(0);
                LastSentFrameID = NextPacket.FrameID;
            }
        }
    }
        catch(MqttException me) {

            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
    }

}
