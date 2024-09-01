package Framework.SystemEventHandlers;

import Framework.DataTypes.Constants.STUDIO_SESSION_STATE;
import Framework.DataTypes.Delegates.StudioSession.StudioSessionStartStopListener;
import Framework.DataTypes.Delegates.StudioSession.StudioSessionUserEvent;

public class StudioSessionHandler implements StudioSessionUserEvent {


    public static StudioSessionHandler Instance = new StudioSessionHandler();
    STUDIO_SESSION_STATE.STATE STATE = STUDIO_SESSION_STATE.STATE.NOTRUNNING;
    StudioSessionStartStopListener studioSessionListener;
    public void StudioSessionRequest()
    {
        switch (STATE)
        {
            case RUNNING:
                if(studioSessionListener != null)
                {
                    studioSessionListener.StopStudioSession();
                }
                STATE = STUDIO_SESSION_STATE.STATE.NOTRUNNING;
                break;

            case NOTRUNNING:
                if(studioSessionListener != null)
                {
                    studioSessionListener.StartStudioSession();
                }
                STATE = STUDIO_SESSION_STATE.STATE.RUNNING;
                break;
        }
    }

    public boolean IsRunning()
    {
        if(STATE == STUDIO_SESSION_STATE.STATE.RUNNING)
            return  true;
        else
            return  false;
    }

    public void notify_OnStudioSessionRequest(StudioSessionStartStopListener delegate)
    {
        studioSessionListener = delegate;
    }

    private StudioSessionHandler()
    {
        KineticRemoteRequestHandler.Instance.set_StudioSessionConvey(this);
    }





}
