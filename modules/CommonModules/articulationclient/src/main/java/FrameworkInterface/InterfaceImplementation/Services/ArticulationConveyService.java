package FrameworkInterface.InterfaceImplementation.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.HashMap;
import java.util.Map;

import Framework.DataTypes.GlobalContext;
import FrameworkInterface.DataTypes.Delegates.IArticulationConveyAIDL;
import FrameworkInterface.InterfaceImplementation.ArticulationManager;

public class ArticulationConveyService extends Service {
    public ArticulationConveyService() {

    }

    @Override
    public IBinder onBind(Intent intent) {

        Map<String, String> strAiConveyList = new HashMap<>();
        String Package = GlobalContext.context.getPackageName();
        strAiConveyList.put(Package,
                "FrameworkInterface.InterfaceImplementation.Services.PlayerConveyService");
        try {
            ArticulationManager.Instance.ConnectToArticulationServices(strAiConveyList);
        }
        catch (Exception e)
        {

        }

        return new IArticulationConveyAIDL.Stub() {
            //Called if speech is detected
            public void NotifyOnSpeechDetect(){
                ArticulationManager.Instance.NotifyOnSpeechDetect();
            }

            //CAlled for the second word recognition onwards
            public void TextBeingArticulatedByUser(String data){
                ArticulationManager.Instance.TextBeingArticulatedByUser(data);
            }

            //Called for the first word recognized
            public void TextArticulationBegan(String data){
                ArticulationManager.Instance.TextArticulationBegan(data);
            }

            //Called at the at of sentance recognition
            public void TextArticulationFinishedByUser(String data){
                ArticulationManager.Instance.TextArticulationFinishedByUser(data);
            }

            public void StoppedListeningToUser(){
                ArticulationManager.Instance.StoppedListeningToUser();
            }

            //Called when listening timer timeout
            public void ListeningIDLETimeout(){
                ArticulationManager.Instance.ListeningIDLETimeout();
            }

            public void ListeningToUserNow(){
                ArticulationManager.Instance.ListeningToUserNow();
            }

            public byte ShouldContinueListeningForFullSentence(){

              return   ArticulationManager.Instance.ShouldContinueListeningForFullSentence() ? (byte)1 : 0;
            }

            public void WakeWordDetected(){
                ArticulationManager.Instance.WakeWordDetected();
            }
        };
    }
}
