package FrameworkInterface.InterfaceImplementation.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.Map;

import Framework.DataTypes.GlobalContext;
import FrameworkInterface.AIManager;
import FrameworkInterface.IAIAccessAIDL;

public class AIService extends Service {
    public AIService() {
    }

    @Override
    public void onCreate() {
        GlobalContext.context = this;
    }

    @Override
    public IBinder onBind(Intent intent) {

        return new IAIAccessAIDL.Stub() {
            public void ConnectToAiServices(String connectionID, Map AiConveyList){
                AIManager.Instance.ConnectToAiServices(AiConveyList);
            }
            public void SendIntentRequest(String connectionID, String Intent,String Message){
                AIManager.Instance.SendIntentRequest(connectionID, Intent, Message);
            }
            public void GetAIQAObject(String connectionID, String question){
                AIManager.Instance.GetAIQAObject(connectionID, question);
            }
            public void GetAIEmoObject(String connectionID){
                AIManager.Instance.GetAIEmoObject(connectionID);
            }
            public void InitializeSTTStream(String connectionID){
                AIManager.Instance.InitializeSTTStream(connectionID);
            }
            public void ProcessStream(String connectionID, byte[] AudBuf){
                AIManager.Instance.ProcessStream(AudBuf);
            }
            public void FinishSTTStream(String connectionID){
                AIManager.Instance.FinishSTTStream();
            }
        };
    }
}
