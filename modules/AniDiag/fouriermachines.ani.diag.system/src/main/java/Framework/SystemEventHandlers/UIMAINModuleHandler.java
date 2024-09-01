package Framework.SystemEventHandlers;

import Framework.DataTypes.Delegates.Application.ApplicationStateDelegate;
import Framework.DataTypes.Delegates.UI.DiagConvey;
import Framework.DataTypes.Delegates.UI.DiagRequestListener;
import Framework.DataTypes.Delegates.UI.SystemInitializationUIConvey;
import Framework.DataTypes.Delegates.UI.UIMAINConvey;
import Framework.SystemFlowManager;

public class UIMAINModuleHandler {

    public static UIMAINModuleHandler Instance  = new UIMAINModuleHandler();

    public SystemInitializationUIConvey InitUIHandler;


    public DiagConvey DiagDataHandler;


    private UIMAINModuleHandler()
    {   }

    public void notifyOnSystemInitializationUIUpdate(SystemInitializationUIConvey delegate)
    {
        InitUIHandler = delegate;
    }

    public void notifyOnDiagUIUpdate(DiagConvey delegate)
    {
        DiagDataHandler = delegate;
    }

    public ApplicationStateDelegate GetApplicationStateDelegate()
    {
        return Framework.SystemEventHandlers.ApplicationStateHandler.Instance;
    }

    public SystemFlowManager GetUIMainConveyListener()
    {
        return SystemFlowManager.Instance;
    }

    public DiagRequestListener GetDiagRequestListenerConveyListener()
    {
        return SystemFlowManager.Instance;
    }
}
