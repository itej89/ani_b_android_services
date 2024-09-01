package FrameworkInterface.InterfaceImplementation;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.usb.UsbDevice;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import Framework.Context.AudioContext;
import Framework.DataTypes.GlobalContext;
import Framework.Delegates.AudioContextConvey;
import FrameworkInterface.ArticulationAccess;
import FrameworkInterface.DataTypes.Constants.ArticulationStates;
import FrameworkInterface.DataTypes.Delegates.ArticulationConvey;
import FrameworkInterface.DataTypes.Delegates.IArticulationConveyAIDL;
import FrameworkInterface.DataTypes.Delegates.IPlayerConveyAIDL;
import FrameworkInterface.DataTypes.Delegates.ISynthesizerConveyAIDL;
import FrameworkInterface.DataTypes.Delegates.PlayerConvey;
import FrameworkInterface.DataTypes.Delegates.ServiceConnectionEstablishedConvey;
import FrameworkInterface.DataTypes.Delegates.SynthesizerConvey;
import FrameworkInterface.InterfaceImplementation.ServiceConnections.ArticulationConveyServiceConnection;
import FrameworkInterface.InterfaceImplementation.ServiceConnections.PlayerConveyServiceConnection;
import FrameworkInterface.InterfaceImplementation.ServiceConnections.SynthesizerConveyServiceConnection;

public class ArticulationManager implements AudioContextConvey, ArticulationAccess,
        ServiceConnectionEstablishedConvey {

    ArticulationStates.STATES ARTICULATION_STATE = ArticulationStates.STATES.FINISH;

    public static ArticulationAccess Instance  = new ArticulationManager();


    AudioContext articulationContext;


    String Articulation_Convey_ID = "";
    String Player_Convey_ID = "";
    String Synthesizer_Convey_ID = "";


    public ArticulationManager()
    {
        articulationContext = new AudioContext(this);
    }
    public UsbDevice GetUSBMicDevice()
    {
       return articulationContext.GetUSBMicDevice();
    }


    //ServiceConnectionEstablishedConvey
    public void ServiceConnectionEstablished(){}
    //Enbd of ServiceConnectionEstablishedConvey



    public static final String ARTICULATION_CONVEY = "FrameworkInterface.InterfaceImplementation.Services.ArticulationConveyService";
    public static final String PLAYER_CONVEY = "FrameworkInterface.InterfaceImplementation.Services.PlayerConveyService";
    public static final String SYNTH_CONVEY = "FrameworkInterface.InterfaceImplementation.Services.SynthesizerConveyService";
    public Map<String, Map<String,ServiceConnection>> ArticulationServiceConveyConnections = new HashMap();

    public void ConnectToArticulationServices(String connectionID, Map<String, String> AiConveyList){

        for (Map.Entry<String,String> keypair: AiConveyList.entrySet()) {
            Intent i = new Intent();
            i.setClassName(keypair.getKey(), keypair.getValue());

            if(ArticulationServiceConveyConnections.containsKey(keypair.getValue())) {
                if (ArticulationServiceConveyConnections.get(keypair.getValue()).containsKey(keypair.getKey())) {
                    try {
                        GlobalContext.context.unbindService(ArticulationServiceConveyConnections.get(keypair.getValue()).remove(keypair.getKey()));
                    }
                    catch (Exception e)
                        {
                            Log.e("ArticulationManager", e.getMessage());
                        }
                    ArticulationServiceConveyConnections.get(keypair.getValue()).remove(keypair.getKey());
                }
            }
            else
                ArticulationServiceConveyConnections.put(keypair.getValue(), new HashMap<String, ServiceConnection>());

            switch (keypair.getValue()) {
                case ARTICULATION_CONVEY:
                    ArticulationServiceConveyConnections.get(ARTICULATION_CONVEY).put(keypair.getKey(), new ArticulationConveyServiceConnection());
                    break;
                case PLAYER_CONVEY:
                    ArticulationServiceConveyConnections.get(PLAYER_CONVEY).put(keypair.getKey(), new PlayerConveyServiceConnection());
                    break;
                case SYNTH_CONVEY:
                    ArticulationServiceConveyConnections.get(SYNTH_CONVEY).put(keypair.getKey(), new SynthesizerConveyServiceConnection(this));
                   break;
                default:
                    break;
            }

            ServiceConnection connection = ArticulationServiceConveyConnections.get(keypair.getValue()).get(keypair.getKey());

            try {
                boolean ret = GlobalContext.context.bindService(i, connection, Context.BIND_AUTO_CREATE);
            }
            catch (Exception e)
            {
                Log.e("ArticulationManager", "ConnectToAiServices : "+e.getMessage());
            }
       }
    }

    IArticulationConveyAIDL notifyArticulationState()
    {
        if(ArticulationServiceConveyConnections.containsKey(ARTICULATION_CONVEY))
        {
            if(ArticulationServiceConveyConnections.get(ARTICULATION_CONVEY).containsKey(Articulation_Convey_ID))
            {
               ServiceConnection serviceConnection =   (ArticulationServiceConveyConnections.get(ARTICULATION_CONVEY).get(Articulation_Convey_ID));
                if(((ArticulationConveyServiceConnection)serviceConnection).getService() != null) {
                    return ((ArticulationConveyServiceConnection) serviceConnection).getService();
                }
            }
        }
        return  null;
    }

    IPlayerConveyAIDL notifyPlayerStatetate()
    {
        if(ArticulationServiceConveyConnections.containsKey(PLAYER_CONVEY))
        {
            if(ArticulationServiceConveyConnections.get(PLAYER_CONVEY).containsKey(Player_Convey_ID))
            {
                ServiceConnection serviceConnection =   (ArticulationServiceConveyConnections.get(PLAYER_CONVEY).get(Player_Convey_ID));
                if(((PlayerConveyServiceConnection)serviceConnection).getService() != null) {
                    return ((PlayerConveyServiceConnection) serviceConnection).getService();
                }
            }
        }
        return  null;
    }



    ISynthesizerConveyAIDL notifySynthesizerState()
    {
        if(ArticulationServiceConveyConnections.containsKey(SYNTH_CONVEY))
        {
            if(ArticulationServiceConveyConnections.get(SYNTH_CONVEY).containsKey(Synthesizer_Convey_ID))
            {
                ServiceConnection serviceConnection =   (ArticulationServiceConveyConnections.get(SYNTH_CONVEY).get(Synthesizer_Convey_ID));
                if(((SynthesizerConveyServiceConnection)serviceConnection).getService() != null) {
                    return ((SynthesizerConveyServiceConnection) serviceConnection).getService();
                }
            }
        }
        return  null;
    }

    //Recognizer Calls
    public void StartListeningToUser(String connectionID) {
        Articulation_Convey_ID = connectionID;
        articulationContext.StartListeningToUser();
        if(notifyArticulationState() != null)
        {
            try {
                notifyArticulationState().ListeningToUserNow();
            }
            catch (Exception e)
            {
                Log.e("ArticulationManager", "StartListeningToUser  :"+e.getMessage());
            }
        }
    }
    public void StopListening(String connectionID) {
        Articulation_Convey_ID = connectionID;
        articulationContext.StopListening();
        ARTICULATION_STATE = ArticulationStates.STATES.FINISH;
    }

    public void StartRecognition(String connectionID) {
        Articulation_Convey_ID = connectionID;
        articulationContext.StartRecognition();
    }
    public  void StopRecognition(String connectionID) {
        Articulation_Convey_ID = connectionID;
        articulationContext.StopRecognition();
        ARTICULATION_STATE = ArticulationStates.STATES.FINISH;
    }

    public void StartWakeWordDetection(String connectionID) {
        Articulation_Convey_ID = connectionID;
        articulationContext.StartWakeWordDetection();
    }
    public void StopWakeWordDetection(String connectionID) {
        Articulation_Convey_ID = connectionID;
        articulationContext.StopWakeWordDetection();
        ARTICULATION_STATE = ArticulationStates.STATES.FINISH;
    }
    //End of Recognizer Calls

    //Player Calls
    public void ReadyAudioStream(String connectionID)
    {
        Player_Convey_ID = connectionID;
        articulationContext.ReadyAudioStream();
    }
    public void PlayAudioStream(String connectionID, byte[] Stream)
    {
        Player_Convey_ID = connectionID;
        articulationContext.PlayAudioStream(Stream);
    }
    public void CloseAudioStream(String connectionID) {
        Player_Convey_ID = connectionID;
        articulationContext.CloseAudioStream();
    }

    public void PlaySoundSegment(String connectionID, String fileName, int StartSec, int EndSec, Float Volume, Float FadeDuration) {
        Player_Convey_ID = connectionID;
        articulationContext.PlaySound( fileName , StartSec, EndSec,  Volume,  FadeDuration);
    }
    public void PlaySound(String connectionID, String fileName, Float Volume, Float FadeDuration) {
        Player_Convey_ID = connectionID;
        articulationContext.PlaySound( fileName,  Volume,  FadeDuration);
    }
    public void PauseSound(String connectionID) {
        Player_Convey_ID = connectionID;
        articulationContext.PauseSound();
    }
    public void PlayWavData(String connectionID, String fileName, Float Volume, Float FadeDuration) {
        Player_Convey_ID = connectionID;
        articulationContext.PlayWavData( fileName,  Volume,  FadeDuration);
    }
    public Boolean IsSoundPlayerPlaying(String connectionID)
    {
        return articulationContext.IsSoundPlayerPlaying();
    }
    //End of Player Calls

    //Synthesizer Calls
    public void SpeakText(String connectionID, String _content, Float _UtteranceRate,Float _PitchMultiplier,String _language) {
        Synthesizer_Convey_ID = connectionID;
        articulationContext.SpeakText( _content,  _UtteranceRate,  _PitchMultiplier,  _language);
    }
    //End of Synthesizer Calls
    //end public functions


    //AudioContextconvey
    public void WakeWordDetected() {
        if(notifyArticulationState() != null){
            try {
                notifyArticulationState().WakeWordDetected();
            }
            catch (Exception e)
            {
                Log.e("ArticulationManager", "WakeWordDetected  :"+e.getMessage());
            }
        }
    }
    public void StoppedListeningIDLETimeout() {
        ARTICULATION_STATE = ArticulationStates.STATES.FINISH;
        if(notifyArticulationState() != null){
            try {
                notifyArticulationState().ListeningIDLETimeout();
            }
            catch (Exception e)
            {
                Log.e("ArticulationManager", "StoppedListeningIDLETimeout  :"+e.getMessage());
            }
        }
    }
    public  void NotifyOnSpeech() {
                if(notifyArticulationState() != null)
        {
            if(ARTICULATION_STATE == ArticulationStates.STATES.FINISH)
            {
                ARTICULATION_STATE = ArticulationStates.STATES.BEGIN;
                try {
                    notifyArticulationState().TextArticulationBegan( "");
                }
                catch (Exception e)
                {
                    Log.e("ArticulationManager", "NotifyOnSpeech  :"+e.getMessage());
                }
            }
            else
            if(ARTICULATION_STATE == ArticulationStates.STATES.BEGIN)
            {
                ARTICULATION_STATE = ArticulationStates.STATES.ONGOING;
                //notifyArticulationState().TextBeingArticulatedByUser( data);
                try {
                    notifyArticulationState().NotifyOnSpeechDetect();
                }
                catch (Exception e)
                {
                    Log.e("ArticulationManager", "NotifyOnSpeech  :"+e.getMessage());
                }
            }
            if(ARTICULATION_STATE == ArticulationStates.STATES.ONGOING)
            {
                try {
                    notifyArticulationState().NotifyOnSpeechDetect();
                }
                catch (Exception e)
                {
                    Log.e("ArticulationManager", "NotifyOnSpeech  :"+e.getMessage());
                }
            }

//            if(notifyArticulationState().ShouldContinueListeningForFullSentence())
//            {
//                if(utterance_timer != null)
//                {
//                    utterance_timer.cancel();
//                }
//
//                if(ArticulatedText != "")
//                {
//                    StartUtteranceTimer( 1500);
//                }
//            }
        }
    }
    public void SendArticulatedText(String data) {
        ARTICULATION_STATE = ArticulationStates.STATES.FINISH;

        articulationContext.SentanceFinalized();
        if(notifyArticulationState() != null)
        {
            try {
            notifyArticulationState().TextArticulationFinishedByUser(data);
            }
            catch (Exception e)
            {
                Log.e("ArticulationManager", "SendArticulatedText  :"+e.getMessage());
            }
        }
    }
    public void StoppedListeningToUser() {
        ARTICULATION_STATE = ArticulationStates.STATES.FINISH;
        if(notifyArticulationState() != null)
        {
            try {
                notifyArticulationState().StoppedListeningToUser();
            }
            catch (Exception e)
            {
                Log.e("ArticulationManager", "StoppedListeningToUser  :"+e.getMessage());
            }
        }
    }


    public void UpdateAudioPlayProgress(int progress) {
        if(notifyPlayerStatetate() != null)
        {
            try {
                notifyPlayerStatetate().UpdateAudioPlayProgress(progress);
            }
            catch (Exception e)
            {
                Log.e("ArticulationManager", "UpdateAudioPlayProgress  :"+e.getMessage());
            }
        }
    }
    public void FinishedPalyingSound() {
        if(notifyPlayerStatetate() != null)
        {
            try {
                notifyPlayerStatetate().FinishedPlayingSound();
            }
            catch (Exception e)
            {
                Log.e("ArticulationManager", "FinishedPalyingSound  :"+e.getMessage());
            }
        }
    }


    public void FinishedSynthesis()
    {
        if(notifySynthesizerState() != null)
        {
            try {
                notifySynthesizerState().FinishedSynthesis();
            }
            catch (Exception e)
            {
                Log.e("ArticulationManager", "FinishedSynthesis  :"+e.getMessage());
            }
        }
    }
    //End AudioContextconvey
}
