package FrameworkInterface.DataTypes.Delegates;


public interface ArticulationConvey {

        //Called if speech is detected
        public void NotifyOnSpeechDetect();

        //CAlled for the second word recognition onwards
        public void TextBeingArticulatedByUser(String data);

        //Called for the first word recognized
        public void TextArticulationBegan(String data);

        //Called at the at of sentance recognition
        public void TextArticulationFinishedByUser(String data);

        public void StoppedListeningToUser();

        //Called when listening timer timeout
        public void ListeningIDLETimeout();

        public void ListeningToUserNow();

        public Boolean ShouldContinueListeningForFullSentence();

        public void WakeWordDetected();

}

