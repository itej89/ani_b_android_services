package Framework.Delegates;

public interface AudioContextConvey {
    public  void NotifyOnSpeech();
    public void SendArticulatedText(String data);
    public void  StoppedListeningToUser();
    public void  StoppedListeningIDLETimeout();

    public void  FinishedSynthesis();

    public void UpdateAudioPlayProgress(int progress);

    public void  FinishedPalyingSound();

    public void WakeWordDetected();
}
