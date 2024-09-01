package Framework.SystemEventHandlers;

import Framework.DataTypes.Delegates.Applet.ActionRequestConvey;
import Framework.DataTypes.Delegates.Application.ApplicationStateDelegate;
import Framework.DataTypes.Delegates.UI.SystemInitializationUIConvey;
import Framework.JOBS.Applet.AugmentRacingJob;
import Framework.Scheduler;
import Framework.SystemFlowManager;

public class UIMAINModuleHandler {

    public static UIMAINModuleHandler Instance  = new UIMAINModuleHandler();

    public SystemInitializationUIConvey InitUIHandler;

    private UIMAINModuleHandler()
    {   }

    public void notifyOnSystemInitializationUIUpdate(SystemInitializationUIConvey delegate)
    { InitUIHandler = delegate; }

    public ApplicationStateDelegate GetApplicationStateDelegate()
    {
        return Framework.SystemEventHandlers.ApplicationStateHandler.Instance;
    }

    public SystemFlowManager GetUIMainConveyListener()
    {
        return SystemFlowManager.Instance;
    }
}
