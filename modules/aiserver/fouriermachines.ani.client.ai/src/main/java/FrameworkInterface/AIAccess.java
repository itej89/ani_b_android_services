package FrameworkInterface;

import Framework.DataTypes.Delegates.AISTTDelegates;
import Framework.DataTypes.Delegates.AIServerDelegates;

public interface AIAccess extends  IAIAccess {
    public void SendIntentRequest(String connectionID, String Intent,String Message);
    public void GetAIQAObject(String connectionID, String question);
    public void GetAIEmoObject(String connectionID);

    public void InitializeSTTStream(String connectionID);
    public void ProcessStream(byte[] AudBuf);
    public void FinishSTTStream();
}
