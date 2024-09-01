package Framework.DataTypes.Delegates.UI;

import java.util.ArrayList;

import FrameworkImplementation.DataTypes.Enums.LINK_ANCHORS;

public interface AppletUIRead {
    public void CloseApplet();
    public void ShowBlankScreen();
    public void ShutdownRequest();
    public void ShowURL(String Link);
    public void UpdateNurseData(int BPM, int Pulse, int SPO2, double Temp);

    public void AccessoryConnected(ArrayList<LINK_ANCHORS> ANCHOR);
    public void AccessoryDisconnected(LINK_ANCHORS ANCHOR);

    public void StartNurseRecording();
    public  void  StopNurseRecording();
}
