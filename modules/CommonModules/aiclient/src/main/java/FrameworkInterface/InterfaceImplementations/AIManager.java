package FrameworkInterface.InterfaceImplementations;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Map;

import Framework.DataTypes.Delegates.AISTTDelegates;
import Framework.DataTypes.Delegates.AIServerDelegates;
import Framework.DataTypes.Delegates.AIServerStatusConvey;
import Framework.DataTypes.GlobalContext;
import Framework.DataTypes.ServiceQAResponseWithEmo;
import FrameworkInterface.AIAccess;
import FrameworkInterface.InterfaceImplementations.ServiceConnections.AiServiceConnection;

public class AIManager implements AIAccess {
        public static AIAccess Instance  = new AIManager();

        AIServerStatusConvey ServerStatusDelegate = null;
        AIServerDelegates ServerManagerDelegate = null;
        AISTTDelegates ServerManagerSTTDelegate = null;

        //AIAccess
        public void SubscribeAIServer(AIServerDelegates delegate) {
            ServerManagerDelegate = delegate;
        }
        public void SubScribeAISTTServer(AISTTDelegates delegates) { ServerManagerSTTDelegate = delegates; }
        public void SubScribeAIServerStatus(AIServerStatusConvey delegates) { ServerStatusDelegate = delegates; }


        public  void RecievedSTTBeginAck(boolean status){
            if(ServerManagerSTTDelegate != null)
                ServerManagerSTTDelegate.RecievedSTTBeginAck(status);
        }
        public  void RecievedSTTProcessingAck(boolean status){
            if(ServerManagerSTTDelegate != null)
                ServerManagerSTTDelegate.RecievedSTTProcessingAck(status);
        }
        public  void RecievedSTTFinished(String Response){
            if(ServerManagerSTTDelegate != null)
                ServerManagerSTTDelegate.RecievedSTTFinished(Response);
        }

        public void RecievedAnswerWithEmotion(ServiceQAResponseWithEmo QAResponse){
            if(ServerManagerDelegate != null)
                ServerManagerDelegate.RecievedAnswerWithEmotion(QAResponse);
       }
        public  void AiServiceConnected(){
            if(ServerStatusDelegate != null) ServerStatusDelegate.ConnectedToAIService();
        }
        public  void AiServiceDisconnected(){
            if(ServerStatusDelegate != null) ServerStatusDelegate.AIServiceDisconnected();
        }


        AiServiceConnection connection;
        public Boolean initServiceConnection() {
            connection = new AiServiceConnection();

            Intent i = new Intent();
            i.setClassName("fm.ani.aiserver", "FrameworkInterface.InterfaceImplementation.Services.AIService");
            return GlobalContext.context.bindService(i, connection, Context.BIND_AUTO_CREATE);
        }

        /** Unbinds this activity from the service. */
        public void releaseServiceConnection() {
            try {
                GlobalContext.context.unbindService(connection);
            }
            catch (Exception e)
            {
            }
            connection = null;
        }
        public void ConnectToAiServices(Map<String, String> AiConveyList){

            try {
                connection.getService().ConnectToAiServices(GlobalContext.context.getPackageName(),AiConveyList);
            }
            catch (Exception e)
            {
                Log.e("AIManager", "ConnectToAiServices  :"+e.getMessage());
            }
        }
        public void SendIntentRequest(String Intent,String Message){
            try {
                connection.getService().SendIntentRequest(GlobalContext.context.getPackageName(),Intent, Message);
            }
            catch (Exception e)
            {
            }
        }
        public void GetAIQAObject(String question){
            try {
                connection.getService().GetAIQAObject(GlobalContext.context.getPackageName(),question);
            }
            catch (Exception e)
            {
            }
        }
        public void GetAIEmoObject(){
            try {
                connection.getService().GetAIEmoObject(GlobalContext.context.getPackageName());
            }
            catch (Exception e)
            {
            }
        }
        public void InitializeSTTStream(){
            try {
                connection.getService().InitializeSTTStream(GlobalContext.context.getPackageName());
            }
            catch (Exception e)
            {
            }
        }
        public void ProcessStream(byte[] AudBuf){
            try {
                connection.getService().ProcessStream(GlobalContext.context.getPackageName(), AudBuf);
            }
            catch (Exception e)
            {
            }
        }
        public void FinishSTTStream(){
            try {
                connection.getService().FinishSTTStream(GlobalContext.context.getPackageName());
            }
            catch (Exception e)
            {
            }
        }
}
