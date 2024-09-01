package FrameworkInterface;

import java.util.Map;

import FrameworkInterface.DataTypes.Delegates.ArticulationConvey;
import FrameworkInterface.DataTypes.Delegates.ArticulationServerStatusConvey;
import FrameworkInterface.DataTypes.Delegates.PlayerConvey;
import FrameworkInterface.DataTypes.Delegates.SynthesizerConvey;

public interface ArticulationAccess extends  IArticulationAccess,
        ArticulationConvey, PlayerConvey, SynthesizerConvey{

    public void ConnectToArticulationServices(Map<String, String> AiConveyList);

    public Boolean IsServiceReady();

    public Boolean initServiceConnection();
    public void releaseServiceConnection();

    public void ResetAllSessions();

    public void setOnArticulationListener(ArticulationConvey delegate);

    public void setOnSynthesizerListener(SynthesizerConvey delegate);

    public void setOnPlaySoundListener(PlayerConvey delegate);

    public void setOnArticulationServerStatusConvey(ArticulationServerStatusConvey delegate);

    public  void ArticulationServiceConnected();
    public  void ArticulationServiceDisconnected();







    public void StartListeningToUser();
    public void StopListening();

    public Boolean IsSoundPlayerPlaying();

    public void StartRecognition();
    public void StopRecognition();

    public void StartWakeWordDetection();
    public void StopWakeWordDetection();

    public void ReadyAudioStream();
    public void PlayAudioStream(byte[] Stream);
    public void CloseAudioStream();

    public void PlaySoundSegment(String fileName, int StartSec, int EndSec, Float Volume, Float FadeDuration);
    public void PlaySound(String fileName, Float Volume, Float FadeDuration);
    public void PauseSound();
    public void PlayWavData(String WavData_UTF8, Float Volume, Float FadeDuration);

    public void SpeakText(String _content, Float _UtteranceRate, Float _PitchMultiplier, String _language);


}
