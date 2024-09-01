package FrameworkInterface.InterfaceImplementation;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.usb.UsbDevice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.os.Build;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.util.Log;

import Framework.DataTypes.Constants.ActuatorPowerStatusSymbols;
import Framework.DataTypes.Constants.ActuatorSignalStatusSymbols;
import Framework.DataTypes.Constants.CommandLabels;
import Framework.DataTypes.Constants.KineticsResponseAcknowledgement;
import Framework.DataTypes.Constants.ProximityStateSymbols;
import Framework.DataTypes.GlobalContext;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequest;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestAngle;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestAttachLockServo;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestAttachServo;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestAttentionOff;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestAttentionOn;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestConnectPower;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestDettachLockServo;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestDettachServo;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestDisconnectPower;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestEEPROMRead;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestEEPROMWrite;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestISLEEPROMRead;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestISLEEPROMWrite;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestISLRead;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestISLWrite;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestInstantTrigger;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestLockPowerStatus;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestLockSignalStatus;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestPowerOff;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestPowerOffLockServo;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestPowerOnLockServo;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestProximityRead;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestServoDegree;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestServoEEPROMRead;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestServoMotionCheck;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestServoPowerStatus;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestServoSignalStatus;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestTrigger;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponse;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseEEPROMRead;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseEEPROMWrite;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseISLEEPROMRead;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseISLEEPROMWrite;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseISLRead;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseISLWrite;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseLockPowerStatus;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseLockSignalStatus;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseProximityRead;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseServoDegree;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseServoEEPROMRead;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseServoPowerStatus;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseServoSignalStatus;

import FrameworkInterface.DataTypes.Delegates.KineticsServiceStatusConvey;
import FrameworkInterface.IKineticsAccessAIDL;
import FrameworkInterface.InterfaceImplementation.ServiceConnections.KineticsServiceConnection;
import FrameworkInterface.InterfaceImplementation.Services.KineticsParameterUpdatesConveyService;
import FrameworkInterface.InterfaceImplementation.Services.KineticsRemoteRequestConveyService;
import FrameworkInterface.InterfaceImplementation.Services.KineticsResponseConveyService;
import FrameworkInterface.KineticsAccess;
import FrameworkInterface.PublicTypes.Config.MachineConfig;
import FrameworkInterface.PublicTypes.Constants.Actuator;
import FrameworkInterface.PublicTypes.Constants.KineticsEEPROM;
import FrameworkInterface.PublicTypes.Constants.MachineCommsStates;
import FrameworkInterface.PublicTypes.Constants.MachineRequests;
import FrameworkInterface.PublicTypes.Delegates.KineticsBindStatusConvey;
import FrameworkInterface.PublicTypes.Delegates.KineticsParameterUpdatesConvey;
import FrameworkInterface.PublicTypes.Delegates.KineticsRemoteRequestConvey;
import FrameworkInterface.PublicTypes.Delegates.KineticsResponseConvey;
import FrameworkInterface.PublicTypes.EEPROMDetails;

public class KineticComms implements KineticsAccess {

    public   static  KineticsAccess Instance = new KineticComms();


    KineticsServiceConnection connection = new KineticsServiceConnection();;

    public KineticsServiceStatusConvey notify_KineticsServiceStatusConvey;
    /** Binds this activity to the service. */
    public void setServiceStatusConvey(KineticsServiceStatusConvey kineticsStatusConvey)
    {
        notify_KineticsServiceStatusConvey = kineticsStatusConvey;
    }


    public  void KineticsServiceConnected(){
        if(notify_KineticsServiceStatusConvey != null)
            notify_KineticsServiceStatusConvey.ConnectedToService();
    }
    public  void KineticsServiceDisconnected(){
        if(notify_KineticsServiceStatusConvey != null)
            notify_KineticsServiceStatusConvey.ServiceDisconnected();
    }

    private boolean isServiceRunning(String className) {
        ActivityManager manager = (ActivityManager) GlobalContext.context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo serviceInfo : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (className.equals(serviceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    public Boolean initServiceConnection() {
        connection = new KineticsServiceConnection() {

            public void onServiceConnected(ComponentName className, IBinder service) {
                super.onServiceConnected(className, service);
                Log.d("KineticComms", "onServiceConnected called.");
            }

            @Override
            public void onBindingDied(ComponentName name) {
                super.onBindingDied(name);
                Log.d("KineticComms", "onBindingDied :"+name);
            }

            @Override
            public void onNullBinding(ComponentName name) {
                super.onNullBinding(name);
                Log.d("KineticComms", "onNullBinding :"+name.toString());
            }

            public void onServiceDisconnected(ComponentName className) {
                super.onServiceDisconnected(className);
                Log.d("KineticComms", "onNullBinding :"+className.toString());
            }
        };

        Intent i = new Intent();
        i.setClassName("fm.ani.kinetics", "fm.ani.kinetics.KineticsService");
        return GlobalContext.context.bindService(i, connection, Context.BIND_AUTO_CREATE);
    }

    /** Unbinds this activity from the service. */
    public void releaseServiceConnection() {
        try {
            GlobalContext.context.unbindService(connection);
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "releaseServiceConnection : "+e.getMessage());
        }
        connection = null;
    }

    public Boolean IsServiceReady(){
        try {
            return connection.getService().IsServiceReady() == 1? true : false;
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "IsServiceReady : "+e.getMessage());
            return false;
        }
    }

    public Boolean IsConnectedToMachine()
    {
        return connection.IsConnectedToMachine();
    }


    public  void ConnectToKineticServices(Map<String, String> KineticsConveyList)
    {
        try {

            connection.getService().ConnectToKineticServices(KineticsConveyList);
        }
        catch (Exception e)
        {

            Log.e("KineticComms", "ConnectToKineticServices : "+e.getMessage());
        }
    }

    public void RequestSent(KineticsRequest request) {
        if(notify_KineticsParameterUpdates != null)
        notify_KineticsParameterUpdates.ParameterUpdated(request);
    }

    public void ParameterTriggerSuccuss() {
        if(notify_KineticsParameterUpdates != null)
        notify_KineticsParameterUpdates.ParametersSetSuccessfully();
    }

    public void KineticsResponseDataTimeout(UUID uuid, ArrayList<KineticsResponse> PartialResponse) {
        if(notify_KineticsResponse != null){
            notify_KineticsResponse.MachiResponseTimeout(PartialResponse,  uuid);
        }
    }

    public void RecievedRemoteCommand(MachineRequests event)
    {
        if(notify_KineticsRemoteRequest != null){
            notify_KineticsRemoteRequest.machineRequested( event);
        }
    }

    public void RecievedResponseData(ArrayList<KineticsResponse> responeData,  UUID _Acknowledgement) {
        if(notify_KineticsResponse != null){
            notify_KineticsResponse.MachineResponseRecieved( responeData,  _Acknowledgement);
        }
    }

    public void SetBindingStatus(String state)
    {
        if(notify_KineticsBindStatusConvey != null)
            notify_KineticsBindStatusConvey.KineticsBindStatusUpdated(state);
    }

    public void BindMachine(){
        try {
            connection.getService().BindMachine(GlobalContext.context.getPackageName());
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "BindMachine : "+e.getMessage());
        }
    }

    public void UnBindMachine(){
        try {
            connection.getService().UnBindMachine(GlobalContext.context.getPackageName());
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "UnBindMachine : "+e.getMessage());
        }
    }

    public MachineConfig GetMachineConfig()
    {
        try {
          return   connection.getService().GetMachineConfig();
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "GetMachineConfig : "+e.getMessage());
        }

        return null;
    }

    public ParcelUuid IndicateAttention() {
        try {
            return connection.getService().IndicateAttention(GlobalContext.context.getPackageName());
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "IndicateAttention : "+e.getMessage());
            return null;
        }
    }

    public ParcelUuid IndicateNoAttention() {

        try {
            return connection.getService().IndicateNoAttention(GlobalContext.context.getPackageName());
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "IndicateNoAttention : "+e.getMessage());
            return null;
        }
    }


    public ParcelUuid ReadDegree(Actuator actuator) {
        try {
            return connection.getService().ReadDegree(GlobalContext.context.getPackageName(), actuator.toString());
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "ReadDegree : "+e.getMessage());
            return null;
        }
    }

    public ParcelUuid ReadProximity() {

        try {
            return connection.getService().ReadProximity(GlobalContext.context.getPackageName());
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "ReadProximity : "+e.getMessage());
            return null;
        }
    }

    public ParcelUuid ReadMotionState(Actuator actuatorType) {
        try {
            return connection.getService().ReadMotionState(GlobalContext.context.getPackageName(), actuatorType.toString());
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "ReadMotionState : "+e.getMessage());
            return null;
        }
    }

    public ParcelUuid ReadActuatorPowerStatus(Actuator actuator) {
        try {
            return connection.getService().ReadActuatorPowerStatus(GlobalContext.context.getPackageName(), actuator.toString());
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "ReadActuatorPowerStatus : "+e.getMessage());
            return null;
        }
    }

    public ParcelUuid ReadActuatorSignalStatus(Actuator actuator) {
        try {
            return connection.getService().ReadActuatorSignalStatus(GlobalContext.context.getPackageName(), actuator.toString());
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "ReadActuatorSignalStatus : "+e.getMessage());
            return null;
        }
    }

    public ParcelUuid ReadLockPowerStatus() {

        try {
            return connection.getService().ReadLockPowerStatus(GlobalContext.context.getPackageName());
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "ReadLockPowerStatus : "+e.getMessage());
            return null;
        }
    }

    public ParcelUuid ReadLockSignalStatus() {

        try {
            return connection.getService().ReadLockSignalStatus(GlobalContext.context.getPackageName());
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "ReadLockSignalStatus : "+e.getMessage());
            return null;
        }
    }


    public ParcelUuid PowerOnMotor(Actuator actuator) {

        try {
            return connection.getService().PowerOnMotor(GlobalContext.context.getPackageName(), actuator.toString());
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "PowerOnMotor : "+e.getMessage());
            return null;
        }
    }

    public ParcelUuid PowerOffMotor(Actuator actuator) {

        try {
            return connection.getService().PowerOffMotor(GlobalContext.context.getPackageName(), actuator.toString());
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "PowerOffMotor : "+e.getMessage());
            return null;
        }
    }

    public ParcelUuid AttachSignalToActuator(Actuator actuator) {
        try {
            return connection.getService().AttachSignalToActuator(GlobalContext.context.getPackageName(), actuator.toString());
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "AttachSignalToActuator : "+e.getMessage());
            return null;
        }
    }

    public ParcelUuid DetachSignalToActuator(Actuator actuator) {
        try {
            return connection.getService().DetachSignalToActuator(GlobalContext.context.getPackageName(), actuator.toString());
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "DetachSignalToActuator : "+e.getMessage());
            return null;
        }
    }

    public ParcelUuid PowerOnLockMotor() {

        try {
            return connection.getService().PowerOnLockMotor(GlobalContext.context.getPackageName());
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "PowerOnLockMotor : "+e.getMessage());
            return null;
        }
    }

    public ParcelUuid PowerOffLockMotor() {

        try {
            return connection.getService().PowerOffLockMotor(GlobalContext.context.getPackageName());
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "PowerOffLockMotor : "+e.getMessage());
            return null;
        }
    }

    public ParcelUuid AttachSignalToALockctuator() {

        try {
            return connection.getService().AttachSignalToALockctuator(GlobalContext.context.getPackageName());
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "AttachSignalToALockctuator : "+e.getMessage());
            return null;
        }
    }

    public ParcelUuid DetachSignalToLockActuator() {

        try {
            return connection.getService().DetachSignalToLockActuator(GlobalContext.context.getPackageName());
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "DetachSignalToLockActuator : "+e.getMessage());
            return null;
        }
    }

    public ParcelUuid StartInstantMotion() {

        try {
            return connection.getService().StartInstantMotion(GlobalContext.context.getPackageName());
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "StartInstantMotion : "+e.getMessage());
            return null;
        }
    }

    public ParcelUuid StartMotion() {

        try {
            return connection.getService().StartMotion(GlobalContext.context.getPackageName());
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "StartMotion : "+e.getMessage());
            return null;
        }
    }

    public Boolean CheckProximity(ArrayList<KineticsResponse> response)
    {
        try {
            return connection.getService().CheckProximity(response) == 1? true : false;
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "CheckProximity : "+e.getMessage());
            return null;
        }
    }

    public Boolean CheckActuatorPowerStatus(ArrayList<KineticsResponse> response) {

        try {
            return connection.getService().CheckActuatorPowerStatus(response) == 1? true : false;
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "CheckActuatorPowerStatus : "+e.getMessage());
            return null;
        }
    }

    public Boolean CheckActuatorSignalStatus(ArrayList<KineticsResponse> response) {

        try {
            return connection.getService().CheckActuatorSignalStatus(response) == 1? true : false;
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "CheckActuatorSignalStatus : "+e.getMessage());
            return null;
        }
    }

    public Boolean CheckLockPowerStatus(ArrayList<KineticsResponse> response) {

        try {
            return connection.getService().CheckLockPowerStatus(response) == 1? true : false;
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "CheckLockPowerStatus : "+e.getMessage());
            return null;
        }
    }

    public Boolean CheckLockSignalStatus(ArrayList<KineticsResponse> response) {

        try {
            return connection.getService().CheckLockSignalStatus(response) == 1? true : false;
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "CheckLockSignalStatus : "+e.getMessage());
            return null;
        }
    }


    public ParcelUuid ReadActuatorAddress(Actuator actuator) {

        try {
            return connection.getService().ReadActuatorAddress(GlobalContext.context.getPackageName(), actuator.toString());
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "ReadActuatorAddress : "+e.getMessage());
            return null;
        }
    }

    public Boolean SetActuatorAddress(Actuator actuator, ArrayList<KineticsResponse> response)
    {
        try {
            return connection.getService().SetActuatorAddress(actuator.toString(), response) == 1? true : false;
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "SetActuatorAddress : "+e.getMessage());
            return null;
        }

    }

    public ParcelUuid ReadDeltaResetAngle(Actuator actuator)
    {
        try {
            return connection.getService().ReadDeltaResetAngle(GlobalContext.context.getPackageName(), actuator.toString());
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "ReadDeltaResetAngle : "+e.getMessage());
            return null;
        }
    }

    public Boolean SetDeltaResetAngle(Actuator actuator,ArrayList<KineticsResponse> response)
    {
        try {
            return connection.getService().SetDeltaResetAngle(actuator.toString(), response) == 1? true : false;
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "SetDeltaResetAngle : "+e.getMessage());
            return null;
        }
    }

    public ParcelUuid ReadReferanceAngle(Actuator actuator) {
        try {
            return connection.getService().ReadReferanceAngle(GlobalContext.context.getPackageName(), actuator.toString());
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "ReadReferanceAngle : "+e.getMessage());
            return null;
        }
    }

    public Boolean SetReferanceAngle(Actuator actuator, ArrayList<KineticsResponse> response)
    {
        try {
            return connection.getService().SetReferanceAngle(actuator.toString(), response)== 1? true : false;
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "SetReferanceAngle : "+e.getMessage());
            return null;
        }
    }

    public KineticsRequestAngle GetKineticRequestAngleFromDegreeResponse(Actuator actuator, ArrayList<KineticsResponse> response)
    {
        try {
            return (KineticsRequestAngle)connection.getService().GetKineticRequestAngleFromDegreeResponse(actuator.toString(), response);
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "GetKineticRequestAngleFromDegreeResponse : "+e.getMessage());
            return null;
        }
    }

    public Integer GetDeltaAngleFromFullAngle(Integer FullAngle, Actuator actuator)
    {
        return (FullAngle - (MachineConfig.Instance.MachineActuatorList.get(actuator).RefPosition));
    }


    public Integer GetFullAngleFromDeltaAngle(Integer DeltaAngle, Actuator actuator)
    {
        return (DeltaAngle + (MachineConfig.Instance.MachineActuatorList.get(actuator).RefPosition));
    }

    public ArrayList<KineticsRequest> GetPredefinedKineticsRequestAngleByName(String Name)
    {
        try {
            List reqAngle = connection.getService().GetPredefinedKineticsRequestAngleByName(Name);
            ArrayList<KineticsRequest> kineticsRequests = new ArrayList<KineticsRequest>(reqAngle);
            return kineticsRequests;
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "GetPredefinedKineticsRequestAngleByName : "+e.getMessage());
            return null;
        }
    }

    public ParcelUuid SetParameters(ArrayList<KineticsRequest> parameters) {
        try
        {
            return connection.getService().SetParameters(GlobalContext.context.getPackageName(), parameters);
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "SetParameters : "+e.getMessage());
            return null;
        }
    }


    //Machine remote events Delegate
    KineticsRemoteRequestConvey notify_KineticsRemoteRequest;

    //Machine response Delegate
    KineticsResponseConvey notify_KineticsResponse;

    //Machine Parameter Updates Delegate
    KineticsParameterUpdatesConvey notify_KineticsParameterUpdates;

    //Kinetics Machine Bindstate Delegate
    KineticsBindStatusConvey notify_KineticsBindStatusConvey;


    public void SetMachineRemoteRequestListener(KineticsRemoteRequestConvey delegate)
    {
        notify_KineticsRemoteRequest = delegate;
    }

    public void SetKineticsResposeListener(KineticsResponseConvey delegate)
    {

        notify_KineticsResponse = delegate;
    }

    public void SetKineticsParameterUpdatesListener(KineticsParameterUpdatesConvey delegate)
    {
        notify_KineticsParameterUpdates = delegate;
    }

    public void SetKineticsBindStateListener(KineticsBindStatusConvey delegate)
    {
        notify_KineticsBindStatusConvey = delegate;
    }


    public void ResetCommsContext()
    {
        try {
            connection.getService().ResetCommsContext();
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "ResetCommsContext : "+e.getMessage());
        }
    }

    public ParcelUuid TurnOffPower() {
        try {
            return connection.getService().TurnOffPower(GlobalContext.context.getPackageName());
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "TurnOffPower : "+e.getMessage());
            return null;
        }
    }

    public  ParcelUuid WriteToEEPROM(EEPROMDetails AddressDetails, Integer Data)
    {  try {
        return connection.getService().WriteToEEPROM(GlobalContext.context.getPackageName(), AddressDetails, Data);
    }
    catch (Exception e)
    {
        Log.e("KineticComms", "WriteToEEPROM : "+e.getMessage());
        return null;
    }
    }

    public  ParcelUuid ReadFromEEPROM(EEPROMDetails AddressDetails)
    {
        try {
            return connection.getService().ReadFromEEPROM(GlobalContext.context.getPackageName(), AddressDetails);
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "ReadFromEEPROM : "+e.getMessage());
            return new ParcelUuid(UUID.randomUUID());
        }
    }

    public  ArrayList<Integer> ExtractBytesFromEEPROMReadResponse(KineticsResponse EEPROMReadResponse)
    {
        try
        {
            int[] bytes = connection.getService().ExtractBytesFromEEPROMReadResponse(EEPROMReadResponse);

            ArrayList<Integer> intList = new ArrayList<Integer>(bytes.length);
            for (int i : bytes)
            {
                intList.add(i);
            }
            return intList;
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "ExtractBytesFromEEPROMReadResponse : "+e.getMessage());
            return null;
        }
    }

    public  Boolean CheckWriteToEEPROM(KineticsResponse Response)
    {
        try {
            return connection.getService().CheckWriteToEEPROM(Response)== 1? true : false;
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "CheckWriteToEEPROM : "+e.getMessage());
            return null;
        }
    }


    //read given number of bytes from from Servo board eeprom Address location
    public  ParcelUuid ReadFromServoEEPROM(Actuator actuator, EEPROMDetails AddressDetails)
    {
        try {
            return connection.getService().ReadFromServoEEPROM(GlobalContext.context.getPackageName(), actuator.toString(), AddressDetails);
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "ReadFromServoEEPROM : "+e.getMessage());
            return null;
        }
    }

    //Returns the payload bytes from ServoEEPROMReadResonse Command
    public ArrayList<Integer> ExtractBytesFromServoEEPROMReadResponse(KineticsResponse EEPROMReadResponse)
    {
        try {
            int[] bytes = connection.getService().ExtractBytesFromServoEEPROMReadResponse(EEPROMReadResponse);

            ArrayList<Integer> intList = new ArrayList<Integer>(bytes.length);
            for (int i : bytes)
            {
                intList.add(i);
            }
            return intList;
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "ExtractBytesFromServoEEPROMReadResponse : "+e.getMessage());
            return null;
        }
    }

    public  void DeleteCalibrationDataForActuator(Actuator actuator)
    {
        try {
            connection.getService().DeleteCalibrationDataForActuator(actuator.toString());
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "DeleteCalibrationDataForActuator : "+e.getMessage());
        }
    }

    public  void SaveActuatorCalibrationData(Actuator actuator, Integer Degree, Integer ADC )
    {
        try {
            connection.getService().SaveActuatorCalibrationData(actuator.toString(), Degree, ADC);
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "SaveActuatorCalibrationData : "+e.getMessage());
        }
    }


    public  ParcelUuid WriteToISLEEPROM(Integer Address,Integer numberOfBytes, Integer Value)
    {
        try {
           return connection.getService().WriteToISLEEPROM(GlobalContext.context.getPackageName(), Address, numberOfBytes, Value);
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "WriteToISLEEPROM : "+e.getMessage());
            return  null;
        }
    }

    public  ParcelUuid ReadFromISLEEPROM(Integer Address, Integer numberOfBytes)
    {
        try {
            return connection.getService().ReadFromISLEEPROM(GlobalContext.context.getPackageName(), Address, numberOfBytes);
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "ReadFromISLEEPROM : "+e.getMessage());
            return  null;
        }
    }

    public  ParcelUuid WriteToISLRAM(Integer Address,Integer numberOfBytes, Integer Value)
    {
        try {
            return connection.getService().WriteToISLRAM(GlobalContext.context.getPackageName(), Address, numberOfBytes, Value);
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "WriteToISLRAM : "+e.getMessage());
            return  null;
        }
    }

    public  ParcelUuid ReadFromISLRAM(Integer Address, Integer numberOfBytes)
    {
        try {
            return connection.getService().ReadFromISLRAM(GlobalContext.context.getPackageName(), Address, numberOfBytes);
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "ReadFromISLRAM : "+e.getMessage());
            return  null;
        }
    }


    //Returns the payload bytes from ISLEEPROMReadResonse Command
    public  ArrayList<Integer> ExtractBytesFromISLEEPROMReadResponse(KineticsResponse ISLEEPROMReadResponse)
    {
        try {
            int[] bytes = connection.getService().ExtractBytesFromISLEEPROMReadResponse(ISLEEPROMReadResponse);

            ArrayList<Integer> intList = new ArrayList<Integer>(bytes.length);
            for (int i : bytes)
            {
                intList.add(i);
            }
            return intList;
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "ExtractBytesFromISLEEPROMReadResponse : "+e.getMessage());
            return null;
        }
    }


    //Returns the payload bytes from ISLRAMReadResonse Command
    public  ArrayList<Integer> ExtractBytesFromISLRAMReadResponse(KineticsResponse ISLRAMReadResponse)
    {
        try {
            int[] bytes = connection.getService().ExtractBytesFromISLRAMReadResponse(ISLRAMReadResponse);

            ArrayList<Integer> intList = new ArrayList<Integer>(bytes.length);
            for (int i : bytes)
            {
                intList.add(i);
            }
            return intList;
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "ExtractBytesFromISLRAMReadResponse : "+e.getMessage());
            return  null;
        }
    }

    public  Boolean CheckWriteToISLEEPROM(KineticsResponse Response)
    {
        try {
            return connection.getService().CheckWriteToISLEEPROM(Response)== 1? true : false;
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "CheckWriteToISLEEPROM : "+e.getMessage());
            return  null;
        }
    }

    public  Boolean CheckWriteToISLRAM(KineticsResponse Response)
    {
        try {
            return connection.getService().CheckWriteToISLRAM(Response)== 1? true : false;
        }
        catch (Exception e)
        {
            Log.e("KineticComms", "CheckWriteToISLRAM : "+e.getMessage());
            return  null;
        }
    }
}
