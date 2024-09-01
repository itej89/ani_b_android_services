package FrameworkInterface.InterfaceImplementations.Services;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import java.util.HashMap;
import java.util.Map;

import Framework.DataTypes.Delegates.IAIServerDelegatesAIDL;
import Framework.DataTypes.GlobalContext;
import Framework.DataTypes.ServiceQAResponseWithEmo;
import FrameworkInterface.InterfaceImplementations.AIManager;

public class AIServerConveyService extends Service {
    public AIServerConveyService() {

    }

    @Override
    public IBinder onBind(Intent intent) {

        Map<String, String> strAiConveyList = new HashMap<>();
        String Package = GlobalContext.context.getPackageName();
        strAiConveyList.put(Package,
                "FrameworkInterface.InterfaceImplementations.Services.AISTTConveyService");
        try {
            AIManager.Instance.ConnectToAiServices(strAiConveyList);
        }
        catch (Exception e)
        {
        }

        return new IAIServerDelegatesAIDL.Stub() {
            public void RecievedAnswerWithEmotion(ServiceQAResponseWithEmo QAResponse)
            {
                AIManager.Instance.RecievedAnswerWithEmotion(QAResponse);
            }
        };
    }
}
