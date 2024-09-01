package FrameworkInterface;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import Framework.DataTypes.Delegates.AISTTDelegates;
import Framework.DataTypes.Delegates.AIServerDelegates;
import Framework.DataTypes.GlobalContext;
import Framework.DataTypes.ServiceConnectionEstablishedConvey;
import Framework.fmServiceInterface;
import FrameworkInterface.InterfaceImplementation.ServiceConnections.AISTTDelegatesConnection;
import FrameworkInterface.InterfaceImplementation.ServiceConnections.AIServerDelegatesConnection;

public class AIManager extends fmServiceInterface implements  AIAccess, ServiceConnectionEstablishedConvey {


    public static AIAccess Instance  = new AIManager();

    //ServiceConnectionEstablishedConvey
    public void ServiceConnectionEstablished(){}
    //End of ServiceConnectionEstablishedConvey


    public void SendIntentRequest(String connectionID, String Intent,String Message){ IntentRequest(connectionID, Intent, Message);}
    public void GetAIQAObject(String connectionID, String question) { ConversationQuery(connectionID, question); }

    public void GetAIEmoObject(String connectionID) { }

    public void ConnectToAiServices(Map<String, String> AiConveyList) {
        for (Map.Entry<String,String> keypair: AiConveyList.entrySet()) {
            Intent i = new Intent();
            i.setClassName(keypair.getKey(), keypair.getValue());

            if(AIServiceConveyConnections.containsKey(keypair.getValue())) {
                if (AIServiceConveyConnections.get(keypair.getValue()).containsKey(keypair.getKey())) {
                    try {
                        GlobalContext.context.unbindService(AIServiceConveyConnections.get(keypair.getValue()).get(keypair.getKey()));
                    }
                    catch (Exception e)
                    {
                        Log.e("AIMAnager", e.getMessage());
                    }
                    AIServiceConveyConnections.get(keypair.getValue()).remove(keypair.getKey());
                }
            }
            else
                AIServiceConveyConnections.put(keypair.getValue(), new HashMap<String, ServiceConnection>());

            switch (keypair.getValue())
            {
                case SERVICE_CONVEY:
                    AIServiceConveyConnections.get(SERVICE_CONVEY).put(keypair.getKey(), new AIServerDelegatesConnection());
                    break;
                case STT_CONVEY:
                    AIServiceConveyConnections.get(STT_CONVEY).put(keypair.getKey(), new AISTTDelegatesConnection(this));
                    break;
                default:
                    break;
            }
            ServiceConnection connection = AIServiceConveyConnections.get(keypair.getValue()).get(keypair.getKey());

            try {
                boolean ret = GlobalContext.context.bindService(i, connection, Context.BIND_AUTO_CREATE);
            }
            catch (Exception e)
            {
                Log.e("AIManager", "ConnectToAiServices : "+e.getMessage());
            }

        }
    }

    public void InitializeSTTStream(String connectionID)
    {
        SpeechStreamBegin(connectionID);
    }
    public void ProcessStream(byte[] AudBuf)
    {
        SpeechStreamProcess(AudBuf);
    }
    public void FinishSTTStream()
    {
        SpeechStreamFinish();
    }
    //End of AIAccess
}
