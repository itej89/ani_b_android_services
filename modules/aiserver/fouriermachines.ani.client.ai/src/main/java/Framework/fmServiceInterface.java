package Framework;

import android.content.ServiceConnection;
import android.util.Base64;
import android.util.Log;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import Framework.DataTypes.Delegates.AISTTDelegates;
import Framework.DataTypes.Delegates.STTStreamFinishMessage;
import Framework.DataTypes.GenericExtentions;
import Framework.DataTypes.Delegates.AIServerDelegates;
import Framework.DataTypes.STTStreamBeginMessage;
import Framework.DataTypes.ServiceQAResponseWithEmo;
import FrameworkInterface.InterfaceImplementation.ServiceConnections.AISTTDelegatesConnection;
import FrameworkInterface.InterfaceImplementation.ServiceConnections.AIServerDelegatesConnection;

public class fmServiceInterface {

    WebSocketClient mSTTWebSocketClient = null;
    WebSocketClient mQAWebSocketClient = null;
    WebSocketClient mIntentWebSocketClient = null;

    String Server_link = "10.104.60.111:8088";

    public static final String SERVICE_CONVEY = "FrameworkInterface.InterfaceImplementations.Services.AIServerConveyService";
    public static final String STT_CONVEY = "FrameworkInterface.InterfaceImplementations.Services.AISTTConveyService";
    public Map<String, Map<String,ServiceConnection>> AIServiceConveyConnections = new HashMap();

    public   fmServiceInterface() {
    }

    public void SpeechStreamBegin(final String connectionID) {

        if(mSTTWebSocketClient != null && mSTTWebSocketClient.isOpen())
        {
            mSTTWebSocketClient.close();
        }

        mSTTWebSocketClient = new WebSocketClient(createSocket("chat"))  {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                String query  = "{ "+" \"Type\": \"STT\", \n \"SES_STATE\": \"INITIALIZE_STREAM\" , \n \"Message\": \" \"}";

                mSTTWebSocketClient.send(query);
            }

            @Override
            public void onMessage(String s) {
                try {
                    final String data = s;

                    JSONObject dictionary = GenericExtentions.parseJSONString(data);

                    if (dictionary != null) {
                        String msgType = dictionary.getString("Type");
                        if( msgType != null && msgType.equals("STT")) {

                            if(dictionary.has("INITIALIZE_STREAM")) {
                                STTStreamBeginMessage STTInitResponse = new STTStreamBeginMessage();
                                STTInitResponse.Type = dictionary.getString("Type");
                                STTInitResponse.INITIALIZE_STREAM = dictionary.getString("INITIALIZE_STREAM");

                                if (AIServiceConveyConnections.get(STT_CONVEY) != null &&
                                        AIServiceConveyConnections.get(STT_CONVEY).get(connectionID) != null) {
                                    if (STTInitResponse.INITIALIZE_STREAM.equals("OK"))
                                        ((AISTTDelegatesConnection)AIServiceConveyConnections.get(STT_CONVEY).get(connectionID)).getService().RecievedSTTBeginAck((byte)1);
                                    else
                                        ((AISTTDelegatesConnection)AIServiceConveyConnections.get(STT_CONVEY).get(connectionID)).getService().RecievedSTTBeginAck((byte)0);
                                }
                            }
                            else
                            if(dictionary.has("PROCESS_STREAM"))
                            {
                                STTStreamFinishMessage STTInitResponse = new STTStreamFinishMessage();
                                STTInitResponse.Type = dictionary.getString("Type");
                                STTInitResponse.FINISH_STREAM = dictionary.getString("PROCESS_STREAM");



                                if (AIServiceConveyConnections.get(STT_CONVEY) != null &&
                                        AIServiceConveyConnections.get(STT_CONVEY).get(connectionID) != null) {
                                    if (STTInitResponse.FINISH_STREAM.equals("OK"))
                                        ((AISTTDelegatesConnection)AIServiceConveyConnections.get(STT_CONVEY).get(connectionID)).getService().RecievedSTTProcessingAck((byte)1);
                                    else
                                        ((AISTTDelegatesConnection)AIServiceConveyConnections.get(STT_CONVEY).get(connectionID)).getService().RecievedSTTProcessingAck((byte)0);
                                }
                            }
                            else
                            if(dictionary.has("FINISH_STT"))
                            {
                                STTStreamFinishMessage STTInitResponse = new STTStreamFinishMessage();
                                STTInitResponse.Type = dictionary.getString("Type");
                                STTInitResponse.FINISH_STREAM = dictionary.getString("FINISH_STT");

                                mSTTWebSocketClient.close();

                                if (AIServiceConveyConnections.get(STT_CONVEY) != null &&
                                        AIServiceConveyConnections.get(STT_CONVEY).get(connectionID) != null) {
                                    if (STTInitResponse.Type.equals("STT"))
                                        ((AISTTDelegatesConnection)AIServiceConveyConnections.get(STT_CONVEY).get(connectionID)).getService().RecievedSTTFinished(STTInitResponse.FINISH_STREAM);
                                }
                            }
                        }
                    }
                }
                catch (Exception e)
                {
                    mSTTWebSocketClient.close();
                }


            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket Begin STT", "Error " + e.getMessage());
                mSTTWebSocketClient.close();
            }
        };

        mSTTWebSocketClient.connect();



    }

    public void SpeechStreamProcess(final byte[] AudBuffer) {
        String strAudBuffer = Base64.encodeToString(AudBuffer, Base64.NO_WRAP);
        String query  = "{ "+" \"Type\": \"STT\", \n \"AUDBUF\": \""+strAudBuffer+"\", \n \"SES_STATE\": \"PROCESS_STREAM\" , \n \"Message\": \" \"}";
        mSTTWebSocketClient.send(query);
    }


    public void SpeechStreamFinish() {
        String query  = "{ "+" \"Type\": \"STT\", \n \"SES_STATE\": \"FINISH_STT\" , \n \"Message\": \" \"}";
        mSTTWebSocketClient.send(query);
    }

    public void IntentRequest(final String connectionID, final String Intent,final String Message)
    {

        mIntentWebSocketClient = new WebSocketClient(createSocket("chat")) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                String query = "{ " + " \"Type\": \""+Intent+"\", \n \"Message\": \"" + Message + "\"}";

                mIntentWebSocketClient.send(query);

                mIntentWebSocketClient.close();
            }

            @Override
            public void onMessage(String s)
            {
                Log.i("IntentRequest", "Unexpected Message : " + s);
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("IntentRequest", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {

                Log.i("Websocket", "Error " + e.getMessage());
            }
        };

        mIntentWebSocketClient.connect();
    }

    public void ConversationQuery(final String connectionID, final String Question) {

        mQAWebSocketClient = new WebSocketClient(createSocket("chat"))  {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                String query  = "{ "+" \"Type\": \"QA\", \n \"Message\": \""+Question+"\"}";

                mQAWebSocketClient.send(query);
            }

            @Override
            public void onMessage(String s) {
                try {
                final String data = s;

                JSONObject dictionary = GenericExtentions.parseJSONString(data);

                if (dictionary != null) {
                    ServiceQAResponseWithEmo QAResponse = new ServiceQAResponseWithEmo();
                    QAResponse.Message = dictionary.getString("message");
                    QAResponse.Joy = Double.valueOf(dictionary.getDouble("joy")).floatValue();
                    QAResponse.Anger = Double.valueOf(dictionary.getDouble("anger")).floatValue();
                    QAResponse.Surprise = Double.valueOf(dictionary.getDouble("surprise")).floatValue();
                    QAResponse.Sadness = Double.valueOf(dictionary.getDouble("sadness")).floatValue();
                    QAResponse.Fear = Double.valueOf(dictionary.getDouble("fear")).floatValue();
                    QAResponse.Disgust = Double.valueOf(dictionary.getDouble("disgust")).floatValue();
                    QAResponse.Synth = String.valueOf(dictionary.getString("synth"));
                    QAResponse.intent = dictionary.getString("intent");
                    QAResponse.confidence = Double.valueOf(dictionary.getDouble("intent_conf")).floatValue();

                    if (AIServiceConveyConnections.get(SERVICE_CONVEY) != null &&
                            AIServiceConveyConnections.get(SERVICE_CONVEY).get(connectionID) != null) {
                        ((AIServerDelegatesConnection)AIServiceConveyConnections.get(SERVICE_CONVEY).get(connectionID)).getService().RecievedAnswerWithEmotion(QAResponse);
                    }
                }
                }
                catch (Exception e)
                {
                    Log.i("db read Emsynth ID", "Error " + e.getMessage());
                }

                mQAWebSocketClient.close();
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("QA Websocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {

                Log.i("Websocket", "Error " + e.getMessage());
            }
        };

        mQAWebSocketClient.connect();



    }


    URI createSocket(String cmd)  {

        URI uri = null;
        try {
            uri = new URI("ws://"+Server_link+"/"+cmd);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
        return uri;
    }

}
