package FrameworkInterface;

import android.hardware.usb.UsbDevice;
import android.util.Log;

import Framework.AudioRecorder;
import Framework.Data.Delegates.AudioDelegate;
import Framework.Data.Delegates.VoiceDelegate;
import Framework.Data.VOICE_PROCESSING_TYPE;
import Framework.USBAudioSpeechManager;

public class AudioInterface extends USBAudioSpeechManager {




    private static AudioInterface _instance = new AudioInterface();

    public static AudioInterface Instance() {
        return _instance;
    }

    public  void SetSpeechDelegate(AudioDelegate _delegate)
    {
        audioDelegate = _delegate;
    }

    public void StartListening()
    {
        StartReadingAudioParameters();
    }

    public void StopListening()
    {
        StopReadingAudioParameters();
    }

    public void StartRecognition()
    {
        SetProcessingType(VOICE_PROCESSING_TYPE.SPEECH_RECOGNITION);
    }

    public void StopRecognition()
    {
        SetProcessingType(VOICE_PROCESSING_TYPE.NA);
    }

    public void StartSearching()
    {
        SetProcessingType(VOICE_PROCESSING_TYPE.WAKE_WORD_DETECTION);
    }

    public void StopSearching()
    {
        SetProcessingType(VOICE_PROCESSING_TYPE.NA);
    }


    public UsbDevice GetUSBMicDevice() { return  Repseaker; }


}
