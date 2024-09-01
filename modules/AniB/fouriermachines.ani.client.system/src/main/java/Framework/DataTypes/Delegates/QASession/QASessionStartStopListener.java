package Framework.DataTypes.Delegates.QASession;

public interface QASessionStartStopListener {
    public void StartQASession();
    public void StopQASession(boolean IsAppInactive);
}
