package fm.ani.anib;

import Framework.DataTypes.Delegates.Application.ApplicationStateDelegate;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

public class AppStateListener implements LifecycleObserver {

    ApplicationStateDelegate appDelegate;

@OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onMoveToForeground() {
    if(appDelegate != null) {
        appDelegate.AppInterruptOver();
    }
            }

@OnLifecycleEvent(Lifecycle.Event.ON_STOP)
public void  onMoveToBackground() {
    if(appDelegate != null) {
        appDelegate.AppInterrupted();
    }
            }
}