package Framework;


import android.app.Activity;
import android.content.Intent;
import android.media.MediaSync;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.UtteranceProgressListener;


import java.util.Locale;

import Framework.DataTypes.GlobalContext;
import Framework.Delegates.SynthesizerDelegates;

//public  class Synthesizer
//{
//    public  SynthesizerDelegates sysnthesisDelegate;
//    String Text;
//    public  Synthesizer() {
//        MaryLink.load(GlobalContext.context);
//    }
//
//    public void textToSpeech(String _content, Float _UtteranceRate, Float _PitchMultiplier, String _language)
//    {
//        MaryLink.getInstance().startTTS(_content);
//
//    }
//}
//
public class Synthesizer extends Activity implements  OnInitListener {

    public  SynthesizerDelegates sysnthesisDelegate;

    //TTS object
    private TextToSpeech myTTS;
    //status check code
    private int MY_DATA_CHECK_CODE = 0;
    String Text;



    public void textToSpeech(String _content, Float _UtteranceRate, Float _PitchMultiplier, String _language) {
        if (sysnthesisDelegate != null && sysnthesisDelegate.CanStartSynthesis()) {

            try
            {
            Text = _content;
            myTTS = new TextToSpeech(GlobalContext.context, this);

            myTTS.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onDone(String utteranceId) {
                    if (sysnthesisDelegate != null) {
                        sysnthesisDelegate.SynthesisFinished();
                    }
                }

                @Override
                public void onError(String utteranceId) {
                }

                @Override
                public void onStart(String utteranceId) {
                }
            });

        }
        catch(Exception ex)
        {
            sysnthesisDelegate.ReleaseAnyLocksOnSynthError();
        }
        ///////
    }
    }

    public void onError()
    {

    }

    public void onInit(int initStatus) {

        //check for successful instantiation
        if (initStatus == TextToSpeech.SUCCESS) {
            if(myTTS.isLanguageAvailable(Locale.US)==TextToSpeech.LANG_AVAILABLE)
                myTTS.setLanguage(Locale.US);

            myTTS.speak(Text.subSequence(0,Text.length()),TextToSpeech.QUEUE_FLUSH ,null,TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID);

        }
        else if (initStatus == TextToSpeech.ERROR) {
//            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }
}
