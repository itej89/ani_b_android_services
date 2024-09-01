// IArticulationConveyAIDL.aidl
package FrameworkInterface.DataTypes.Delegates;

// Declare any non-default types here with import statements

interface IArticulationConveyAIDL {
          //Called if speech is detected
          void NotifyOnSpeechDetect();

          //CAlled for the second word recognition onwards
          void TextBeingArticulatedByUser(String data);

          //Called for the first word recognized
          void TextArticulationBegan(String data);

          //Called at the at of sentance recognition
          void TextArticulationFinishedByUser(String data);

          void StoppedListeningToUser();

          //Called when listening timer timeout
          void ListeningIDLETimeout();

          void ListeningToUserNow();

          byte ShouldContinueListeningForFullSentence();

          void WakeWordDetected();
}
