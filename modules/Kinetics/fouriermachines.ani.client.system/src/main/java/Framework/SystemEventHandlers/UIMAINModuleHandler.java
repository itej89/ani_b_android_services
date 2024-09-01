package Framework.SystemEventHandlers;

import Framework.DataTypes.Delegates.Application.ApplicationStateDelegate;
import Framework.DataTypes.Delegates.UI.SystemInitializationUIConvey;
import Framework.DataTypes.Delegates.UI.TestTriggerConvey;
import Framework.SystemFlowManager;

public class UIMAINModuleHandler {

    public static UIMAINModuleHandler Instance  = new UIMAINModuleHandler();

    public SystemInitializationUIConvey InitUIHandler;

    private UIMAINModuleHandler()
    {   }

    public void notifyOnSystemInitializationUIUpdate(SystemInitializationUIConvey delegate)
    {
        InitUIHandler = delegate;
    }

    public ApplicationStateDelegate GetApplicationStateDelegate()
    {
        return ApplicationStateHandler.Instance;
    }

    public TestTriggerConvey GetTestUITriggerListener()
    {
        return SystemFlowManager.Instance;
    }

    public SystemFlowManager GetUIMainConveyListener()
    {
        return SystemFlowManager.Instance;
    }


}
