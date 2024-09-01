package Framework.DataTypes.Delegates;

public interface AISTTDelegates {
    public  void RecievedSTTBeginAck(boolean status);
    public  void RecievedSTTProcessingAck(boolean status);
    public  void RecievedSTTFinished(String Response);
}
