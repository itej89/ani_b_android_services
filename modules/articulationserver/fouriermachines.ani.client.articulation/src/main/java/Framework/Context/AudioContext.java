package Framework.Context;

import android.hardware.usb.UsbDevice;

import java.util.concurrent.locks.ReentrantLock;

import Framework.DataTypes.Constants.LISTENING_STATES;
import Framework.Delegates.AudioContextConvey;
import Framework.Delegates.SoundPlayerDelegates;
import Framework.Delegates.SpeexTDelegates;
import Framework.Delegates.SynthesizerDelegates;
import Framework.SoundPlayer;
import Framework.SpeexT;
import Framework.Synthesizer;
import FrameworkInterface.AudioInterface;

public class AudioContext implements SynthesizerDelegates, SoundPlayerDelegates, SpeexTDelegates {



    AudioContextConvey notifyContext;

    SpeexT speext;
    Synthesizer synthesizer;
    SoundPlayer soundPlayer;

    Boolean isRecMod = false;


    LISTENING_STATES.STATES CurrentVoiceState = LISTENING_STATES.STATES.NOTLISTENING;

    public ReentrantLock audSyncLock = new ReentrantLock();


    public AudioContext(AudioContextConvey delegate)
    {
        speext = new SpeexT();
        synthesizer = new Synthesizer();
        soundPlayer = new SoundPlayer();

        notifyContext = delegate;

        speext.speexTDelegate = this;
        synthesizer.sysnthesisDelegate = this;
        soundPlayer.soundPlayerDelegate = this;

    }
    public UsbDevice GetUSBMicDevice()
    {
        return speext.GetUSBDMicevice();
    }

     //public fucntions
    public void StartListeningToUser() {
        speext.startRec();
        CurrentVoiceState = LISTENING_STATES.STATES.LISTENING;
    }

    public void StopListening() {
        speext.stopRec();
        CurrentVoiceState = LISTENING_STATES.STATES.NOTLISTENING;
    }

    public boolean IsSoundPlayerPlaying()
    {
        return soundPlayer.IsPlaying();
    }

    public void StartRecognition()
    {
        speext.StartSpeechREcognition();
    }

    public void StartWakeWordDetection()
    {
        speext.StartWakeWordDetection();
    }

    public void  StopRecognition()
    {
        speext.StopSpeechREcognition();
    }

    public void StopWakeWordDetection()
    {
        speext.StopWakeWordDetection();
    }

    public void ReadyAudioStream()
    {
        if(!speext.isInListeningContext && isRecMod == false) {
            soundPlayer.ReadyAudioStream();
        }
    }
    public void PlayAudioStream(byte[] Stream)
    {
        if(!speext.isInListeningContext && isRecMod == false) {
            soundPlayer.PlayAudioStream(Stream);
        }
    }
    public void CloseAudioStream()
    {
        soundPlayer.CloseAudioStream();
        notifyContext.FinishedPalyingSound();
        if(isRecMod) {
            // if (speext.timer?.isValid)!
            // {

            StartListeningToUser();

            //  }
        }
        isRecMod = false;
        if(audSyncLock != null && audSyncLock.isLocked()) {
            audSyncLock.unlock();
        }
    }


    public void PlaySound(String fileName, int StartSec, int EndSec, Float Volume, Float FadeDuration) {
        if(!speext.isInListeningContext && isRecMod == false) {
            soundPlayer.PlaySound( fileName , StartSec, EndSec,  Volume,  FadeDuration);
        }
    }


    public void PlaySound(String fileName, Float Volume, Float FadeDuration) {
        if(!speext.isInListeningContext && isRecMod == false) {
            soundPlayer.PlaySound( fileName,  Volume,  FadeDuration);
        }
    }

    public void PauseSound() {
        soundPlayer.PauseSound();
        notifyContext.FinishedPalyingSound();
        if(isRecMod) {
            // if (speext.timer?.isValid)!
            // {

            StartListeningToUser();

            //  }
        }
        isRecMod = false;
        if(audSyncLock != null && audSyncLock.isLocked()) {
            audSyncLock.unlock();
        }

    }

    public void PlayWavData(String WavData_UTF8, Float Volume, Float FadeDuration) {

            soundPlayer.PlayWavData( WavData_UTF8,  Volume,  FadeDuration);

    }

    public void SpeakText(String _content, Float _UtteranceRate, Float _PitchMultiplier,String _language)
    {
        synthesizer.textToSpeech( _content,  _UtteranceRate,  _PitchMultiplier,  _language);

    }
    //end public funciions


    // SynthesizerDelegates
    public void SynthesisFinished() {
        notifyContext.FinishedSynthesis();
        if(isRecMod) {
            // if (speext.timer?.isValid)!
            // {

            StartListeningToUser();

            //  }
        }
//        if(audSyncLock != null && audSyncLock.isLocked()) {
//            audSyncLock.unlock();
//        }
        isRecMod = false;
    }

    public Boolean CanStartSynthesis() {

//        if(!audSyncLock.tryLock()){
//            return false;
//        }

        if(speext.isInListeningContext)
        {
            isRecMod = true;
            speext.pauseRec();
        }

        return true;
    }

    public void  ReleaseAnyLocksOnSynthError(){
//        audSyncLock.unlock();
    }
    //End SynthesizerDelegates



    //Sound Player Delegats
    public Boolean  CanPlaySound() {
//        if(!audSyncLock.tryLock()){
//            return false;
//        }
        if(speext.isInListeningContext)
        {
            isRecMod = true;
            speext.pauseRec();
        }

        return true;
    }

    public void  PlayingSoudFinished() {
        notifyContext.FinishedPalyingSound();
        if(isRecMod) {
            // if (speext.timer?.isValid)!
            // {

            StartListeningToUser();
            StartRecognition();

            //  }
        }

        isRecMod = false;
//                if(audSyncLock != null && audSyncLock.isLocked()) {
//            audSyncLock.unlock();
//        }
    }

    public void  ReleaseAnyLocksOnPlayError() {
//        audSyncLock.unlock();
    }

    public void PlayingSoudProgress(int progress) {
        if(notifyContext != null) {
            notifyContext.UpdateAudioPlayProgress(progress);
        }
    }

    //End Sound Player Delegats


    //Speext Control methods
    public void  SentanceFinalized()
    {
        speext.SentenceFinalizedByUppedLayer();
    }
    //End Speext Control methods

    //SpeexTDelegates
    public void SpeexTRunning()
    {
        notifyContext.NotifyOnSpeech();
    }
    public void  SendSpeexT(String data) {
        notifyContext.SendArticulatedText(data);
    }

    public void  SpeexTStopped() {
        CurrentVoiceState = LISTENING_STATES.STATES.NOTLISTENING;
        notifyContext.StoppedListeningToUser();
    }

    public void  SpeexTListeningIDLETimeout() {
        notifyContext.StoppedListeningIDLETimeout();
    }

    public void SpeexTWakeWordDetected()
    {
        notifyContext.WakeWordDetected();
    }

    //End SpeexTDelegates
}

