package Framework.DataTypes.Delegates.UI;

import android.view.View;

import java.util.ArrayList;


public interface AniUIConvey {
    public void AniStarted(ArrayList<View> elements);
    public  void JoySnapDetected();
    public  void JoyTriggerDetected();
}
