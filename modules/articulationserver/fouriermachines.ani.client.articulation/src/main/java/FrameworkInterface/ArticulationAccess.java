package FrameworkInterface;

import android.hardware.usb.UsbDevice;

import java.util.Map;

import Framework.Delegates.SpeexTSpeechRequest;
import FrameworkInterface.DataTypes.Delegates.ArticulationConvey;
import FrameworkInterface.IArticulationAccess;
import FrameworkInterface.DataTypes.Delegates.PlayerConvey;
import FrameworkInterface.DataTypes.Delegates.SynthesizerConvey;

public interface ArticulationAccess extends IArticulationAccess {

    public void ConnectToArticulationServices(String connectionID, Map<String, String> AiConveyList);

    public UsbDevice GetUSBMicDevice();


    public void StartListeningToUser(String connectionID);
    public void StopListening(String connectionID);

    public Boolean IsSoundPlayerPlaying(String connectionID);

    public void StartRecognition(String connectionID);
    public void StopRecognition(String connectionID);

    public void StartWakeWordDetection(String connectionID);
    public void StopWakeWordDetection(String connectionID);

    public void ReadyAudioStream(String connectionID);
    public void PlayAudioStream(String connectionID, byte[] Stream);
    public void CloseAudioStream(String connectionID);

    public void PlaySoundSegment(String connectionID, String fileName, int StartSec, int EndSec, Float Volume, Float FadeDuration);
    public void PlaySound(String connectionID, String fileName, Float Volume, Float FadeDuration);
    public void PauseSound(String connectionID);
    public void PlayWavData(String connectionID, String WavData_UTF8, Float Volume, Float FadeDuration);

    public void SpeakText(String connectionID, String _content, Float _UtteranceRate, Float _PitchMultiplier, String _language);

}

