package Framework.Delegates;

public interface SynthesizerDelegates {
    public void ReleaseAnyLocksOnSynthError();
    public void SynthesisFinished();
    public Boolean CanStartSynthesis();
}
