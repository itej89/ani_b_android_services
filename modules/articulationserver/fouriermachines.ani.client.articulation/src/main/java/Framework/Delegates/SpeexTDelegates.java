package Framework.Delegates;

public interface SpeexTDelegates {
    public void SpeexTRunning();
    public void SendSpeexT(String data);
    public void SpeexTStopped();
    public void SpeexTListeningIDLETimeout();
    public  void SpeexTWakeWordDetected();
}
