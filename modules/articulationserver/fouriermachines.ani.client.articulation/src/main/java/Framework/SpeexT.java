package Framework;

import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.util.Log;

import java.net.PortUnreachableException;
import java.util.Timer;
import java.util.TimerTask;

import Framework.Data.Delegates.AudioDelegate;
import Framework.Delegates.SpeexTDelegates;
import FrameworkInterface.AudioInterface;


public class SpeexT  implements AudioDelegate {

    public SpeexTDelegates speexTDelegate;


    private Intent recognizerIntent;
    private String LOG_TAG = "VoiceRecognitionActivity";
    private Boolean IsEndOfSpeech = true;

    Timer timer;



    public  SpeexT()
    {
        AudioInterface.Instance().SetSpeechDelegate(this);
    }

    public Boolean isInListeningContext = false;
    //Becomes True on Start listening and false when SentanceFinalized
    Boolean SentenceRecognitionStarted = true;

    public UsbDevice GetUSBDMicevice()
    {
       return AudioInterface.Instance().GetUSBMicDevice();
    }

    //Start of AudioDelegate
    public void RecievedData(String Data)
    {
        if(SentenceRecognitionStarted) {
            if (speexTDelegate != null) {
                speexTDelegate.SendSpeexT(Data);
            }
            IsEndOfSpeech = true;
        }
    }
    public void DetectedSpeech(boolean State)
    {
        if(SentenceRecognitionStarted) {
            if (speexTDelegate != null) {
                speexTDelegate.SpeexTRunning();
            }
        }
    }
    public  void DetectedWakeWord()
    {
        if(SentenceRecognitionStarted) {
            if (speexTDelegate != null) {
                speexTDelegate.SpeexTWakeWordDetected();
            }
        }
    }
    //End of AudioDelegate

    public  void StartSpeechREcognition()
    {
        AudioInterface.Instance().StartRecognition();
        if(timer != null)
        {
            Log.d("SPEEXT", "Timer stopped 1");
            timer.cancel();
        }
        timer = new Timer();
        Log.d("SPEEXT", "Timer started");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d("SPEEXT", "Timer Timeout");
               // pauseRec();
                if(speexTDelegate != null) {
                    speexTDelegate.SpeexTListeningIDLETimeout();
                }
            }
        }, 30000);
    }

    public  void StopSpeechREcognition() {
        if(timer != null)
        {
            Log.d("SPEEXT", "Timer stopped 2");
            timer.cancel();
        }
        AudioInterface.Instance().StopRecognition();
    }


        public  void StartWakeWordDetection()
    {
        AudioInterface.Instance().StartSearching();
    }


    public  void StopWakeWordDetection() {
        AudioInterface.Instance().StopSearching();
    }

    // call this function to start recognization
    public void startRec(){
        SentenceRecognitionStarted = true;
        isInListeningContext = true;
        IsEndOfSpeech =false;
        AudioInterface.Instance().StartListening();

    }

    // call this function to pause recognization
    public void pauseRec(){


        if(isInListeningContext == false) {
            return;
        }

        AudioInterface.Instance().StopListening();

        isInListeningContext = false;


        SentenceRecognitionStarted = false;


    }

    public void stopRec(){

        pauseRec();

//        if(speexTDelegate != null) {
//            speexTDelegate.SpeexTStopped();
//        }
    }

    public void SentenceFinalizedByUppedLayer()
    {
        if(SentenceRecognitionStarted == true) {
            if (timer != null) {
                Log.d("SPEEXT", "Timer stopped 3");
                timer.cancel();
            }
        }
    }






}
