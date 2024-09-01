package Framework.Delegates;

public interface SoundPlayerDelegates {
    public Boolean CanPlaySound();
    public void   PlayingSoudFinished();
    public void   PlayingSoudProgress(int progress);
    public void   ReleaseAnyLocksOnPlayError();
}
