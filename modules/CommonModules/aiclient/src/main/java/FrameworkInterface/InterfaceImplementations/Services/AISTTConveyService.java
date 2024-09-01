package FrameworkInterface.InterfaceImplementations.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import Framework.DataTypes.Delegates.IAISTTDelegatesAIDL;
import Framework.DataTypes.Delegates.IAIServerDelegatesAIDL;
import FrameworkInterface.InterfaceImplementations.AIManager;

public class AISTTConveyService extends Service {
    public AISTTConveyService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        AIManager.Instance.AiServiceConnected();

        return new IAISTTDelegatesAIDL.Stub() {
            public  void RecievedSTTBeginAck(byte status){
                AIManager.Instance.RecievedSTTBeginAck(status == 1? true : false);
            }
            public  void RecievedSTTProcessingAck(byte status){
                AIManager.Instance.RecievedSTTProcessingAck(status == 1? true : false);
            };
            public  void RecievedSTTFinished(String Response){
                AIManager.Instance.RecievedSTTFinished(Response);
            }
        };
    }
}