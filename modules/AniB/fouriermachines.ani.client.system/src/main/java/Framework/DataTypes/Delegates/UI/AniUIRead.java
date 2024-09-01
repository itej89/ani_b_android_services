package Framework.DataTypes.Delegates.UI;

import android.view.View;

import java.util.ArrayList;
import java.util.Map;

public interface AniUIRead {
    public Map<Integer, View> GetAllUIViews();
    public ArrayList<View> GetAllUIElements();
    public void ShowStudioSession();
    public void CloseStudioSession();
    public void ShowStudioSessionConnecting();
    public void ShowStudioSessionDisconnecting();
    public void ShowAnimationDataSaved(Boolean Status);
    public void CloseAnyProgress();
    public void ShowBlankScreen();
    public void ShutdownRequest();
    public void  MachineLoadApplet();
}
