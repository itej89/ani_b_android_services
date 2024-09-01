package Framework.DataTypes;

import java.util.UUID;

import Framework.DataTypes.Constants.JOB_PRIORITY;
import Framework.DataTypes.Constants.JOB_STATE;
import Framework.DataTypes.Delegates.JobConvey;

public class Job {

    public  String Name = "";
    public UUID ID = UUID.randomUUID();
    public JOB_PRIORITY PRIORITY = JOB_PRIORITY.ZERO;
    public JobConvey delegate;
    public JOB_STATE STATE = JOB_STATE.NA;

    public void TakeOverResources(JobConvey _delegate)
    {
        delegate = _delegate;
    }

    public void Pause()
    {
        if(delegate != null){
            delegate.notify_Paused(ID);
        }
    }

    public void destroy()
    {

    }

    public void Resume()
    {
    }
}
