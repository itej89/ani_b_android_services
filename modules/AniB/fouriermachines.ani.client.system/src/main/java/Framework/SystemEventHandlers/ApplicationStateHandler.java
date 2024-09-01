package Framework.SystemEventHandlers;

import Framework.DataTypes.Constants.APP_STATE;
import Framework.DataTypes.Delegates.Application.ApplicationStateDelegate;
import Framework.DataTypes.Delegates.PlayPauseRequest;
import Framework.SystemFlowManager;

public class ApplicationStateHandler implements ApplicationStateDelegate, PlayPauseRequest
{


    public APP_STATE.STATE CURRENT_SATE = APP_STATE.STATE.ACTIVE;


    //ApplicationStateDelegate
    public void AppInterrupted() {
        if(CURRENT_SATE == APP_STATE.STATE.ACTIVE){
            CURRENT_SATE = APP_STATE.STATE.INACTIVE;
            SystemFlowManager.Instance.WentBackground();
        }
    }

    public void AppInterruptOver() {
        if(CURRENT_SATE == APP_STATE.STATE.INACTIVE)
        {
            CURRENT_SATE = APP_STATE.STATE.ACTIVE;
            SystemFlowManager.Instance.CameForeground();
        }
    }

    public void AppInactive() {
        CURRENT_SATE = APP_STATE.STATE.BACKGROUND;
    }

    public void AppIsActive() {
        CURRENT_SATE = APP_STATE.STATE.INACTIVE;
    }
    //End of ApplicationStateDelegate



    //PlayPauseRequest
    public void PlayPauseRequested() {
        switch(CURRENT_SATE)
        {
            case INACTIVE:
                AppInterruptOver();

            case ACTIVE:
                AppInterrupted();

            default:
                break;
        }
    }
    //end of PlayPauseRequest



    private ApplicationStateHandler()
    {
        Framework.SystemEventHandlers.KineticRemoteRequestHandler.Instance.set_PlayPauseConvey(this);
    }

    public static ApplicationStateHandler Instance = new  ApplicationStateHandler();


}
