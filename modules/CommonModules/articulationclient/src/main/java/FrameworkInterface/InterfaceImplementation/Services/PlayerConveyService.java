package FrameworkInterface.InterfaceImplementation.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.HashMap;
import java.util.Map;

import Framework.DataTypes.GlobalContext;
import FrameworkInterface.DataTypes.Delegates.IArticulationConveyAIDL;
import FrameworkInterface.DataTypes.Delegates.IPlayerConveyAIDL;
import FrameworkInterface.InterfaceImplementation.ArticulationManager;

public class PlayerConveyService extends Service {
    public PlayerConveyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Map<String, String> strAiConveyList = new HashMap<>();
        String Package = GlobalContext.context.getPackageName();
        strAiConveyList.put(Package,
                "FrameworkInterface.InterfaceImplementation.Services.SynthesizerConveyService");
        try {
            ArticulationManager.Instance.ConnectToArticulationServices(strAiConveyList);
        }
        catch (Exception e)
        {

        }

        return new IPlayerConveyAIDL.Stub() {
           public void FinishedPlayingSound(){
               ArticulationManager.Instance.FinishedPlayingSound();
           }
           public void UpdateAudioPlayProgress(int progress){
               ArticulationManager.Instance.UpdateAudioPlayProgress(progress);
           }
        };
    }
}
