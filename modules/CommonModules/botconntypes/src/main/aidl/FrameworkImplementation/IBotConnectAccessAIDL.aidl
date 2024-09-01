// IBotConnectAccessAIDL.aidl
package FrameworkImplementation;

// Declare any non-default types here with import statements

interface IBotConnectAccessAIDL {

    void SendLinkRequest(String connectionID, String anchor, String Data);

    void ConnectToBotConnectServices(String connectionID, in Map BotConnectConveyList);

    byte IsServiceReady(String connectionID);

    void SubscribeToBotConnectConvey(String connectionID);

    void StartLinkStream(String connectionID, String anchor);

    void StopLinkStream(String connectionID, String anchor);

    byte IsLinkAvailable(String connectionID, String AnchorType);

    String GetLinkData(String connectionID, String Anchor);
}
