package FrameworkInterface;

import Framework.DataTypes.Delegates.AISTTDelegates;
import Framework.DataTypes.Delegates.AIServerDelegates;
import Framework.DataTypes.Delegates.AIServerStatusConvey;
import Framework.DataTypes.ServiceQAResponseWithEmo;
import FrameworkInterface.IAIAccess;

public interface AIAccess extends IAIAccess {


    public Boolean initServiceConnection();
    public void releaseServiceConnection();

    public void SubscribeAIServer(AIServerDelegates delegate);
    public void SubScribeAISTTServer(AISTTDelegates delegates);
    public void SubScribeAIServerStatus(AIServerStatusConvey delegates);

    public void RecievedAnswerWithEmotion(ServiceQAResponseWithEmo QAResponse);
    public  void RecievedSTTBeginAck(boolean status);
    public  void RecievedSTTProcessingAck(boolean status);
    public  void RecievedSTTFinished(String Response);

    public  void AiServiceConnected();
    public  void AiServiceDisconnected();




    public void SendIntentRequest(String Intent,String Message);
    public void GetAIQAObject(String question);
    public void GetAIEmoObject();

    public void InitializeSTTStream();
    public void ProcessStream(byte[] AudBuf);
    public void FinishSTTStream();
}
