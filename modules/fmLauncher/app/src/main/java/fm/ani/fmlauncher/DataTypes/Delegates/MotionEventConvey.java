package fm.ani.fmlauncher.DataTypes.Delegates;

import android.view.KeyEvent;
import android.view.MotionEvent;

public interface MotionEventConvey {
    public void ReceivedMotionEvent(MotionEvent e);
    public void ReceivedKeyEvent(KeyEvent e);
}
