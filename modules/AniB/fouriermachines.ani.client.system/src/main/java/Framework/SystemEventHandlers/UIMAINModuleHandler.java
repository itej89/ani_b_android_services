package Framework.SystemEventHandlers;

import Framework.DataTypes.Delegates.Application.ApplicationStateDelegate;
import Framework.DataTypes.Delegates.UI.AniUIRead;
import Framework.DataTypes.Delegates.UI.AppletUIRead;
import Framework.DataTypes.Delegates.UI.SystemInitializationUIConvey;
import Framework.DataTypes.Delegates.UI.TestTriggerConvey;
import Framework.SystemFlowManager;

public class UIMAINModuleHandler {

    public static UIMAINModuleHandler Instance  = new UIMAINModuleHandler();

    public SystemInitializationUIConvey InitUIHandler;
    public AniUIRead AniUIHandler;
    public AppletUIRead AppletUIHandler;

    private UIMAINModuleHandler()
    {   }

    public void notifyOnSystemInitializationUIUpdate(SystemInitializationUIConvey delegate)
    {
        InitUIHandler = delegate;
    }

    public void setAniUIHandler(AniUIRead delegate)
    {
        AniUIHandler = delegate;
    }

    public void setAppletUIHandler(AppletUIRead delegate)
    {
        AppletUIHandler = delegate;
    }

    public ApplicationStateDelegate GetApplicationStateDelegate()
    {
        return ApplicationStateHandler.Instance;
    }

    public TestTriggerConvey GetTestUITriggerListener()
    {
        return SystemFlowManager.Instance;
    }

    public SystemFlowManager GetAppletUIListener()
    {
        return SystemFlowManager.Instance;
    }

    public SystemFlowManager GetAniUIListener()
    {
        return SystemFlowManager.Instance;
    }

    public SystemFlowManager GetUIMainConveyListener()
    {
        return SystemFlowManager.Instance;
    }


}
