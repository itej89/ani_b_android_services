package Framework.DataTypes.Delegates;

import java.util.UUID;

public interface JobConvey {

    public void notify_LostResource(UUID ID);
    public void notify_Paused(UUID ID);
    public void notify_NextStep(UUID ID);
    public void notify_Finish(UUID ID);
}
