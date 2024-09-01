package FrameworkInterface;

import FrameworkInterface.Enums.CATEGORY_TYPES;
import FrameworkInterface.Enums.COMMAND_TYPES;

public interface MqttClientAccess {
    public void ConnectToBroker();
    public void DisconnectFromBroker();
    public void SubscribeTo(String Channel);
    public void UnsubscribeFrom(String Channel);
    public void AniDiscoverable(String Frame_ID, String ID, String NAME);
    public void ConnectStudio(String Frame_ID, String ID);
    public void DisconnectStudio(String Frame_ID, String ID);
    public void ANIActionModeSet(String Frame_ID, String _ID);
    public void RequestUploadAck(String Frame_ID, String _ID);
    public void SendDataAck(String Frame_ID, String _ID);
    public void ExitUploadAck(String Frame_ID, String _ID);
    public void SendCommandAck(String Frame_ID, String _ID);

}
