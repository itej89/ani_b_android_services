package Framework.SystemEventHandlers;

import FrameworkInterface.DataTypes.Delegates.PlayerConvey;
import FrameworkInterface.InterfaceImplementation.ArticulationManager;

public class AudioContextConveyHandler implements PlayerConvey
{

public static AudioContextConveyHandler Instance  = new AudioContextConveyHandler();

    //PlayerConvey
    public void FinishedPlayingSound() {

        }

    public void UpdateAudioPlayProgress(int progress) {

        }
    //end of PlayerConvey


    public void RevokePlayerDelegates()
        {
        ArticulationManager.Instance.setOnPlaySoundListener(this);
        }

    public void PullPlayerDelegates(PlayerConvey delegate)
        {
        ArticulationManager.Instance.setOnPlaySoundListener(delegate);
        }

}

