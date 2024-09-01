package Framework.Data.Delegates;

public interface AudioDelegate {
    public void RecievedData(String Data);
    public void DetectedSpeech(boolean State);
    public void  DetectedWakeWord();
}
