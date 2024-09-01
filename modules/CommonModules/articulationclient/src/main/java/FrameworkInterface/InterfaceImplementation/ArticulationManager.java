package FrameworkInterface.InterfaceImplementation;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Map;

import Framework.DataTypes.GlobalContext;
import FrameworkInterface.ArticulationAccess;
import FrameworkInterface.DataTypes.Delegates.ArticulationConvey;
import FrameworkInterface.DataTypes.Delegates.ArticulationServerStatusConvey;
import FrameworkInterface.DataTypes.Delegates.PlayerConvey;
import FrameworkInterface.DataTypes.Delegates.SynthesizerConvey;
import FrameworkInterface.InterfaceImplementation.ServiceConnections.ArticulationServiceConnection;

public class ArticulationManager implements ArticulationAccess {

    public static ArticulationAccess Instance  = new ArticulationManager();

    public void ConnectToArticulationServices(Map<String, String> AiConveyList){
        try {
            connection.getService().
                    ConnectToArticulationServices(GlobalContext.context.getPackageName(), AiConveyList);
        }
        catch (Exception e) {
            Log.e("ArticulationManager", "ConnectToAiServices  :"+e.getMessage());
        }
    }


    ArticulationConvey notifyArticulationState;
    SynthesizerConvey notifySynthesizerState;
    PlayerConvey notifyPlayerStatetate;
    ArticulationServerStatusConvey notifyArticulationServerStatusConvey;
    public void ResetAllSessions(){
        notifyArticulationState = null;
        notifySynthesizerState = null;
        notifyPlayerStatetate = null;
    }
    public void setOnArticulationListener(ArticulationConvey delegate){
        notifyArticulationState = delegate;
    }
    public void setOnSynthesizerListener(SynthesizerConvey delegate){
        notifySynthesizerState = delegate;
    }
    public void setOnPlaySoundListener(PlayerConvey delegate){
        notifyPlayerStatetate = delegate;
    }

    public void setOnArticulationServerStatusConvey(ArticulationServerStatusConvey delegate)
    {
        notifyArticulationServerStatusConvey = delegate;
    }


    public void StartListeningToUser(){
        try {
            connection.getService().StartListeningToUser(GlobalContext.context.getPackageName());
        }
        catch (Exception e)
        {
            Log.e("ArticulationManager", "StartListeningToUser  :"+e.getMessage());
        }
    }
    public void StopListening(){
        try {
            connection.getService().StopListening(GlobalContext.context.getPackageName());
        }
        catch (Exception e)
        {
            Log.e("ArticulationManager", "StopListening  :"+e.getMessage());
        }
    }

    public Boolean IsSoundPlayerPlaying(){

        try {
           return connection.getService().IsSoundPlayerPlaying(GlobalContext.context.getPackageName()) == 1 ? true : false;
        }
        catch (Exception e)
        {
            Log.e("ArticulationManager", "IsSoundPlayerPlaying  :"+e.getMessage());
        }
        return  false;
    }

    public void StartRecognition(){

        try {
             connection.getService().StartRecognition(GlobalContext.context.getPackageName());
        }
        catch (Exception e)
        {
            Log.e("ArticulationManager", "StartRecognition  :"+e.getMessage());
        }
    }
    public void StopRecognition(){

        try {
            connection.getService().StopRecognition(GlobalContext.context.getPackageName());
        }
        catch (Exception e)
        {
            Log.e("ArticulationManager", "StopRecognition  :"+e.getMessage());
        }
    }

    public void StartWakeWordDetection(){

        try {
            connection.getService().StartWakeWordDetection(GlobalContext.context.getPackageName());
        }
        catch (Exception e)
        {
            Log.e("ArticulationManager", "StartWakeWordDetection  :"+e.getMessage());
        }
    }
    public void StopWakeWordDetection(){

        try {
            connection.getService().StopWakeWordDetection(GlobalContext.context.getPackageName());
        }
        catch (Exception e)
        {
            Log.e("ArticulationManager", "StopWakeWordDetection  :"+e.getMessage());
        }
    }

    public void ReadyAudioStream(){

        try {
            connection.getService().ReadyAudioStream(GlobalContext.context.getPackageName());
        }
        catch (Exception e)
        {
            Log.e("ArticulationManager", "ReadyAudioStream  :"+e.getMessage());
        }
    }
    public void PlayAudioStream(byte[] Stream){

        try {
            connection.getService().PlayAudioStream(GlobalContext.context.getPackageName(), Stream);
        }
        catch (Exception e)
        {
            Log.e("ArticulationManager", "PlayAudioStream  :"+e.getMessage());
        }
    }
    public void CloseAudioStream(){

        try {
            connection.getService().CloseAudioStream(GlobalContext.context.getPackageName());
        }
        catch (Exception e)
        {
            Log.e("ArticulationManager", "CloseAudioStream  :"+e.getMessage());
        }
    }

    public void PlaySoundSegment(String fileName, int StartSec, int EndSec, Float Volume, Float FadeDuration){
        try {
            connection.getService().PlaySoundSegment(GlobalContext.context.getPackageName(), fileName, StartSec, EndSec, Volume, FadeDuration);
        }
        catch (Exception e)
        {
            Log.e("ArticulationManager", "PlaySoundSegment  :"+e.getMessage());
        }
    }

    public void PlaySound(String fileName, Float Volume, Float FadeDuration)
    {
        try {
            connection.getService().PlaySound(GlobalContext.context.getPackageName(), fileName, Volume, FadeDuration);
        }
        catch (Exception e)
        {
            Log.e("ArticulationManager", "PlaySound  :"+e.getMessage());
        }
    }

    public void PauseSound() {

        try {
            connection.getService().PauseSound(GlobalContext.context.getPackageName());
        } catch (Exception e) {
            Log.e("ArticulationManager", "PauseSound  :" + e.getMessage());
        }
    }
    public void PlayWavData(String WavData_UTF8, Float Volume, Float FadeDuration){

        try {
            connection.getService().PlayWavData(GlobalContext.context.getPackageName(), WavData_UTF8, Volume, FadeDuration);
        }
        catch (Exception e)
        {
            Log.e("ArticulationManager", "PlayWavData  :"+e.getMessage());
        }
    }

    public void SpeakText(String _content, Float _UtteranceRate, Float _PitchMultiplier, String _language){

        try {
            connection.getService().SpeakText(GlobalContext.context.getPackageName(), _content, _UtteranceRate, _PitchMultiplier, _language);
        }
        catch (Exception e)
        {
            Log.e("ArticulationManager", "SpeakText  :"+e.getMessage());
        }
    }



    ArticulationServiceConnection connection;


    public Boolean IsServiceReady(){
        try {
           return connection.getService().IsServiceReady(GlobalContext.context.getPackageName()) == 1 ? true : false;
        }
        catch (Exception e)
        {
            Log.e("ArticulationManager", "SpeakText  :"+e.getMessage());
        }

        return  false;
    }

    public Boolean initServiceConnection() {
        connection = new ArticulationServiceConnection();

        Intent i = new Intent();
        i.setClassName("fm.ani.articulationserver", "FrameworkInterface.InterfaceImplementation.Services.ArticulationService");
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


    public  void ArticulationServiceConnected(){
        if(notifyArticulationServerStatusConvey != null)
            notifyArticulationServerStatusConvey.ConnectedToArticulationService();
    }
    public  void ArticulationServiceDisconnected(){
        if(notifyArticulationServerStatusConvey != null)
            notifyArticulationServerStatusConvey.ArticulationServiceDisconnected();
    }

    //ArticulationConvey
     //Called if speech is detected
    public void NotifyOnSpeechDetect(){
        if(notifyArticulationState != null)
            notifyArticulationState.NotifyOnSpeechDetect();
    }
    //CAlled for the second word recognition onwards
    public void TextBeingArticulatedByUser(String data){
        if(notifyArticulationState != null)
            notifyArticulationState.TextBeingArticulatedByUser(data);
    }
    //Called for the first word recognized
    public void TextArticulationBegan(String data){
        if(notifyArticulationState != null)
            notifyArticulationState.TextArticulationBegan(data);
    }
    //Called at the at of sentance recognition
    public void TextArticulationFinishedByUser(String data){
        if(notifyArticulationState != null)
            notifyArticulationState.TextArticulationFinishedByUser(data);
    }
    public void StoppedListeningToUser(){
        if(notifyArticulationState != null)
            notifyArticulationState.StoppedListeningToUser();
    }
    //Called when listening timer timeout
    public void ListeningIDLETimeout(){
        if(notifyArticulationState != null)
            notifyArticulationState.ListeningIDLETimeout();
    }
    public void ListeningToUserNow(){
        if(notifyArticulationState != null)
            notifyArticulationState.ListeningToUserNow();
    }
    public Boolean ShouldContinueListeningForFullSentence(){
        if(notifyArticulationState != null)
          return   notifyArticulationState.ShouldContinueListeningForFullSentence();

        return  false;
    }
    public void WakeWordDetected(){
        if(notifyArticulationState != null)
             notifyArticulationState.WakeWordDetected();
    }
    //End of ArticulationConvey

    //PlayerConvey
    public void FinishedPlayingSound(){
        if(notifyPlayerStatetate != null)
            notifyPlayerStatetate.FinishedPlayingSound();
    }
    public void UpdateAudioPlayProgress(int progress){
        if(notifyPlayerStatetate != null)
            notifyPlayerStatetate.UpdateAudioPlayProgress(progress);
    }

    //SynthesizerConvey
    public void FinishedSynthesis(){
        if(notifySynthesizerState != null)
            notifySynthesizerState.FinishedSynthesis();
    }
    //End of SynthesizerConvey
}
