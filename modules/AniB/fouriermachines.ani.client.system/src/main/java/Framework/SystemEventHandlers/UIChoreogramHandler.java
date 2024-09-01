package Framework.SystemEventHandlers;

import Framework.DataTypes.Delegates.UI.UIChoreogramConvey;
import Framework.JOBS.StudioSession.ChoreogramRead;

public class UIChoreogramHandler implements ChoreogramRead, UIChoreogramConvey
        {



//ChoreogramRead
public void ChoreogramFinished() {

        }

public Boolean IsPlaying()  {
        return false;
        }
//End of ChoreogramRead

//UIChoreogramConvey
public void ProgressUpdated(int progress) {

        }
//End of UIChoreogramConvey

public static UIChoreogramHandler Instance  = new UIChoreogramHandler();

public ChoreogramRead choreogramRead;
public UIChoreogramConvey choreogramConvey;

public ChoreogramRead getChoreogramRead()
        {
        return choreogramRead;
        }

public UIChoreogramConvey getChoreogramConvey()
        {
        return choreogramConvey;
        }

public void RevokeChoreogramRead()
        {
        choreogramRead = this;
        }

public void setNotifyOnRead(ChoreogramRead _choreogramRead)
        {
        choreogramRead = _choreogramRead;
        }

public void setNotifyOnConvey(UIChoreogramConvey _choreogramConvey)
        {
        choreogramConvey = _choreogramConvey;
        }

        }

