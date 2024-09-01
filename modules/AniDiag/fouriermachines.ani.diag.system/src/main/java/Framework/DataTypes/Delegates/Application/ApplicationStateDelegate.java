package Framework.DataTypes.Delegates.Application;

public interface ApplicationStateDelegate {
    public void AppInterrupted();
    public void AppInterruptOver();
    public void AppInactive();
    public void AppIsActive();
}
