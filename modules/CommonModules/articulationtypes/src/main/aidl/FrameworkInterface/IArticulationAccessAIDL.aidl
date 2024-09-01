// IArticulationAccessAIDL.aidl
package FrameworkInterface;

// Declare any non-default types here with import statements

interface IArticulationAccessAIDL {
        void ConnectToArticulationServices(String connectionID, in Map AiConveyList);
        byte IsServiceReady(String connectionID);


        //Recognizer Calls
        void StartListeningToUser(String connectionID);
        void StopListening(String connectionID);

        byte IsSoundPlayerPlaying(String connectionID);

        void StartRecognition(String connectionID);
        void StopRecognition(String connectionID);

        void StartWakeWordDetection(String connectionID);
        void StopWakeWordDetection(String connectionID);
        //End of Recognizer Calls


        //Player Calls
        void ReadyAudioStream(String connectionID);
        void PlayAudioStream(String connectionID, in byte[] Stream);
        void CloseAudioStream(String connectionID);

        void PlaySoundSegment(String connectionID, String fileName, int StartSec, int EndSec, float Volume, float FadeDuration);
        void PlaySound(String connectionID, String fileName, float Volume, float FadeDuration);
        void PauseSound(String connectionID);
        void PlayWavData(String connectionID, String WavData_UTF8, float Volume, float FadeDuration);
        //End of Player Calls


        //Synthesizer Calls
        void SpeakText(String connectionID, String _content, float _UtteranceRate, float _PitchMultiplier, String _language);
        //End of Synthesizer Calls
}
