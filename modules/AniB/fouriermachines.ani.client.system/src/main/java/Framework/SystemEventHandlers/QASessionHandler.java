package Framework.SystemEventHandlers;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

import Framework.DataTypes.Constants.QA_SESSION_STATE;
import Framework.DataTypes.Delegates.AIServerDelegates;
import Framework.DataTypes.Delegates.QASession.QAAIServerConvey;
import Framework.DataTypes.Delegates.QASession.QASessionStartStopListener;
import Framework.DataTypes.Delegates.QASession.QASessionUserEvent;
import Framework.DataTypes.ServiceQAResponseWithEmo;
import FrameworkInterface.InterfaceImplementations.AIManager;
import fm.ani.client.db.DB_Local_Store;
import fm.ani.client.db.DataTypes.CommandStore.Constants.CommandStore_Table_Columns;
import fm.ani.client.db.DataTypes.CommandStore.DataContext;
import fm.ani.client.db.DataTypes.CommandStore.Expressions_Type;
import fm.ani.client.db.DataTypes.EmSynth.EM_SYNTH;

public class QASessionHandler implements QASessionUserEvent, AIServerDelegates
        {

public static QASessionHandler Instance = new QASessionHandler();
            QASessionStartStopListener qaSessionListener;
            QAAIServerConvey qaAIServerConvey;

            QA_SESSION_STATE.STATE CURRENT_STATE = QA_SESSION_STATE.STATE.STOPPED;

public  boolean IsListening()
{
     if(CURRENT_STATE == QA_SESSION_STATE.STATE.LISTENING)
         return  true;

     return  false;
}

//QASessionUserEventQASessionRequest
public void QASessionRequest()
        {
        switch(CURRENT_STATE) {
        case LISTENING:

        if(qaSessionListener != null)
        {
        qaSessionListener.StopQASession(false);
        CURRENT_STATE = QA_SESSION_STATE.STATE.STOPPED;
        }
        break;
        case STOPPED:

        if(qaSessionListener != null)
        {
        qaSessionListener.StartQASession();
        CURRENT_STATE = QA_SESSION_STATE.STATE.LISTENING;
        }
        break;
        }
        }
//End of QASessionUserEvent


//AIServerDelegates
public void RecievedAnswerWithEmotion(ServiceQAResponseWithEmo QAResponse) {
                DB_Local_Store dbHAndler = new DB_Local_Store();
                ArrayList<DataContext> contextData = dbHAndler.ReadFromContext(CommandStore_Table_Columns.DBCONTEXT_KEYS.EM_SYNTH_ID.name());
                if(contextData != null && contextData.size() > 0) {
                        int EmSynthID = Integer.valueOf(contextData.get(0).VALUE);

                        EM_SYNTH emSynth = dbHAndler.ReadEmSynthByEmSynthID(EmSynthID);

                        if (!emSynth.Path.isEmpty()) {
                                emSynth.Path = emSynth.Path.concat("/EmSynth.db");
                                File DBFile = new File(Environment.getExternalStorageDirectory(), emSynth.Path);
                                if (DBFile.exists()) {
                                        dbHAndler = new DB_Local_Store(DBFile.getPath());
                                }
                        }
                }

                Expressions_Type animationExpression = dbHAndler.readExpressionByEmotion(
                        QAResponse.Joy, QAResponse.Surprise, QAResponse.Fear,
                        QAResponse.Anger, QAResponse.Sadness, QAResponse.Disgust);

                if (animationExpression.Action_Data != null || animationExpression.Action_Data != "") {
                        qaAIServerConvey.AnimatableResponse(QAResponse.intent, QAResponse.confidence, QAResponse.Synth, animationExpression.Action_Data);
                }

        }
//End of AIServerDelegates


private QASessionHandler()
        {
        KineticRemoteRequestHandler.Instance.set_QASessionConveyConvey(this);
        AIManager.Instance.SubscribeAIServer(this);
        }

public void notify_OnQASessionRequest(QASessionStartStopListener delegate)
        {
        qaSessionListener = delegate;
        }

public void notifyOnAIServerData(QAAIServerConvey delegate)
        {
        qaAIServerConvey = delegate;
        }

public void ListeningIDLETimeout()
        {
        CURRENT_STATE = QA_SESSION_STATE.STATE.STOPPED;

        }

public void ResetSession()
        {

        CURRENT_STATE = QA_SESSION_STATE.STATE.STOPPED;
        qaSessionListener = null;
        qaAIServerConvey  = null;
        }


}
