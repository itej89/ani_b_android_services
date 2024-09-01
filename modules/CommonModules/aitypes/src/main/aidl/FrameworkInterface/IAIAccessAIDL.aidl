// IAIAccessAIDL.aidl
package FrameworkInterface;

// Declare any non-default types here with import statements

interface IAIAccessAIDL {
    void ConnectToAiServices(String connectionID, in Map AiConveyList);
    void SendIntentRequest(String connectionID, String Intent,String Message);
    void GetAIQAObject(String connectionID, String question);
    void GetAIEmoObject(String connectionID);
    void InitializeSTTStream(String connectionID);
    void ProcessStream(String connectionID, in byte[] AudBuf);
    void FinishSTTStream(String connectionID);
}
