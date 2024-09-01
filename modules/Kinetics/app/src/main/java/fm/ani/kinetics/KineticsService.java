package fm.ani.kinetics;

import android.Manifest;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.os.PowerManager;
import android.util.Log;

import com.permissioneverywhere.PermissionEverywhere;
import com.permissioneverywhere.PermissionResponse;
import com.permissioneverywhere.PermissionResultCallback;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Framework.DataTypes.Delegates.UI.SystemInitializationUIConvey;
import Framework.DataTypes.Delegates.UI.UIMAINConvey;
import Framework.DataTypes.GlobalContext;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequest;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponse;
import Framework.SystemEventHandlers.UIMAINModuleHandler;
import FrameworkInterface.IKineticsAccessAIDL;
import FrameworkInterface.InterfaceImplementation.KineticComms;
import FrameworkInterface.PublicTypes.Config.MachineConfig;
import FrameworkInterface.PublicTypes.Constants.Actuator;
import FrameworkInterface.PublicTypes.EEPROMDetails;
import FrameworkInterface.PublicTypes.Machine;

public class KineticsService extends Service implements SystemInitializationUIConvey {

    public KineticsService() {
    }

    @Override
    public void onCreate(){
        _Context = this;
        GlobalContext.context = this;
        UIMAINModuleHandler.Instance.notifyOnSystemInitializationUIUpdate(this);
        UserEventConvey = UIMAINModuleHandler.Instance.GetUIMainConveyListener();
        DO_CHECK();
    }

    @Override
    public IBinder onBind(Intent intent) {

        return new IKineticsAccessAIDL.Stub() {

            public byte IsServiceReady()
            {
                return  UserEventConvey.IsInitialized() ? (byte)1 : (byte)0;
            }

            //Handles Functionality to show explicit indication when machine has attention for the user input
            //In version 1 this will be turning on white led
            public ParcelUuid IndicateAttention(String connectionID) {
                return KineticComms.Instance.IndicateAttention(connectionID);
            }

            //Handles Functionality to show explicit indication when machine is not paying attention for the user input
            //In version 1 this will be turning off white led
            public ParcelUuid IndicateNoAttention(String connectionID) {
                return KineticComms.Instance.IndicateNoAttention(connectionID);
            }

            //request machines for the current angle of the given actuator
            public ParcelUuid ReadDegree(String connectionID, String actuator) {
                return KineticComms.Instance.ReadDegree(connectionID, Actuator.valueOf(actuator));
            }

            //request machine on the status of the proximity sensor
            public ParcelUuid ReadProximity(String connectionID) {
                return KineticComms.Instance.ReadProximity(connectionID);
            }

            //request machine for status if, all actuators are completed moving or not
            public ParcelUuid ReadMotionState(String connectionID, String actuatorType) {
                return KineticComms.Instance.ReadMotionState(connectionID, Actuator.valueOf(actuatorType));
            }

            //request machine for status if, the power is on to the actuator
            public ParcelUuid ReadActuatorPowerStatus(String connectionID, String actuator) {
                return KineticComms.Instance.ReadActuatorPowerStatus(connectionID, Actuator.valueOf(actuator));
            }

            //request machine for status if, the signal is attached to the actuator
            public ParcelUuid ReadActuatorSignalStatus(String connectionID, String actuator) {
                return KineticComms.Instance.ReadActuatorSignalStatus(connectionID, Actuator.valueOf(actuator));
            }

            //request machine for status if, the power is on to the lock
            public ParcelUuid ReadLockPowerStatus(String connectionID) {
                return KineticComms.Instance.ReadLockPowerStatus(connectionID);
            }

            //request machine for status if, the signal is attached to lock
            public ParcelUuid ReadLockSignalStatus(String connectionID) {
                return KineticComms.Instance.ReadLockSignalStatus(connectionID);
            }


            //switch on power to the actuator
            public ParcelUuid PowerOnMotor(String connectionID, String actuator) {
                return KineticComms.Instance.PowerOnMotor(connectionID, Actuator.valueOf(actuator));
            }

            //switch off power to the actuator
            public ParcelUuid PowerOffMotor(String connectionID, String actuator) {
                return KineticComms.Instance.PowerOffMotor(connectionID, Actuator.valueOf(actuator));
            }

            //Attach Signal to actuator
            public ParcelUuid AttachSignalToActuator(String connectionID, String actuator) {
                return KineticComms.Instance.AttachSignalToActuator(connectionID, Actuator.valueOf(actuator));
            }

            //Detach Signal to actuator
            public ParcelUuid DetachSignalToActuator(String connectionID, String actuator) {
                return KineticComms.Instance.DetachSignalToActuator(connectionID, Actuator.valueOf(actuator));
            }


            //switch on power to the lock motor
            public ParcelUuid PowerOnLockMotor(String connectionID) {
                return KineticComms.Instance.PowerOnLockMotor(connectionID);
            }

            //switch off power to the lock motor
            public ParcelUuid PowerOffLockMotor(String connectionID) {
                return KineticComms.Instance.PowerOffLockMotor(connectionID);
            }

            //Attach Signal to lock actuator
            public ParcelUuid AttachSignalToALockctuator(String connectionID) {
                return KineticComms.Instance.AttachSignalToALockctuator(connectionID);
            }

            //Detach Signal to lock actuator
            public ParcelUuid DetachSignalToLockActuator(String connectionID) {
                return KineticComms.Instance.DetachSignalToLockActuator(connectionID);
            }

            //This Will Reset MachineComms Context during app deactivate or activate
            public void ResetCommsContext() {
                KineticComms.Instance.ResetCommsContext();
            }

            //requests machine to start motion with the previously set parameters
            public ParcelUuid StartMotion(String connectionID) {
                return KineticComms.Instance.StartMotion(connectionID);
            }

            //Stops current ongoing motion and requests machine to start motion with the previously set parameters immediately
            public ParcelUuid StartInstantMotion(String connectionID) {
                return KineticComms.Instance.StartInstantMotion(connectionID);
            }

            //sets the parameters for the given actuator to be used in the next upload request
            public ParcelUuid SetParameters(String connectionID, List<KineticsRequest> parameters) {
                return KineticComms.Instance.SetParameters(connectionID, new ArrayList<KineticsRequest>(parameters));
            }

            //Sends comamnd to read actuator communication address
            public ParcelUuid ReadActuatorAddress(String connectionID, String actuator) {
                return KineticComms.Instance.ReadActuatorAddress(connectionID, Actuator.valueOf(actuator));
            }

            //Extracts Actuator address from machine response and saves in config for future use
            public byte SetActuatorAddress(String actuator, List<KineticsResponse> response) {
                return KineticComms.Instance.SetActuatorAddress(Actuator.valueOf(actuator), new ArrayList<KineticsResponse>(response)) ? (byte) 1 : (byte) 0;
            }

            //Sends comamnd to read actuator Delta Rese angle
            public ParcelUuid ReadDeltaResetAngle(String connectionID, String actuator) {
                return KineticComms.Instance.ReadDeltaResetAngle(connectionID, Actuator.valueOf(actuator));
            }

            //Extracts Actuator Delta Reset angle from machine response and saves in config for future use
            public byte SetDeltaResetAngle(String actuator, List<KineticsResponse> response) {
                return KineticComms.Instance.SetDeltaResetAngle(Actuator.valueOf(actuator), new ArrayList<KineticsResponse>(response)) ? (byte) 1 : (byte) 0;
            }

            //Sends comamnd to read actuator referance angle
            public ParcelUuid ReadReferanceAngle(String connectionID, String actuator) {
                return KineticComms.Instance.ReadReferanceAngle(connectionID, Actuator.valueOf(actuator));
            }

            //Extracts Actuator referance angle from machine response and saves in config for future use
            public byte SetReferanceAngle(String actuator, List<KineticsResponse> response) {
                return KineticComms.Instance.SetReferanceAngle(Actuator.valueOf(actuator), new ArrayList<KineticsResponse>(response)) ? (byte) 1 : (byte) 0;
            }

            //Create KinticAngleRequest from the KineticDegreeresponse for initial actuator initial settings
            public KineticsRequest GetKineticRequestAngleFromDegreeResponse(String actuator, List<KineticsResponse> response) {
                return KineticComms.Instance.GetKineticRequestAngleFromDegreeResponse(Actuator.valueOf(actuator), new ArrayList<KineticsResponse>(response));
            }

            //Returns Delta Angle by substracting referance angle for the actuator
            public int GetDeltaAngleFromFullAngle(int FullAngle, String actuator) {
                return KineticComms.Instance.GetDeltaAngleFromFullAngle(FullAngle, Actuator.valueOf(actuator));
            }

            //Returns Delta Angle by substracting referance angle for the actuator
            public int GetFullAngleFromDeltaAngle(int DeltaAngle, String actuator) {
                return KineticComms.Instance.GetFullAngleFromDeltaAngle(DeltaAngle, Actuator.valueOf(actuator));
            }

            //Reads any predefined machine command that is present in db using name
            public List<KineticsRequest> GetPredefinedKineticsRequestAngleByName(String Name) {
                return KineticComms.Instance.GetPredefinedKineticsRequestAngleByName(Name);
            }

            //Check if any device detected on proximty sensor from the read proximity repsonse
            public byte CheckProximity(List<KineticsResponse> response) {
                return KineticComms.Instance.CheckProximity(new ArrayList<KineticsResponse>(response)) ? (byte) 1 : (byte) 0;
            }

            //Check if power is connected to actuator from the read actuator power status repsonse
            public byte CheckActuatorPowerStatus(List<KineticsResponse> response) {
                return KineticComms.Instance.CheckActuatorPowerStatus(new ArrayList<KineticsResponse>(response)) ? (byte) 1 : (byte) 0;
            }

            //Check if Signal is connected to actuator from the read actuator power status repsonse
            public byte CheckActuatorSignalStatus(List<KineticsResponse> response) {
                return KineticComms.Instance.CheckActuatorSignalStatus(new ArrayList<KineticsResponse>(response)) ? (byte) 1 : (byte) 0;
            }

            //Check if power is connected to lock from the read actuator power status repsonse
            public byte CheckLockPowerStatus(List<KineticsResponse> response) {
                return KineticComms.Instance.CheckLockPowerStatus(new ArrayList<KineticsResponse>(response)) ? (byte) 1 : (byte) 0;
            }

            //Check if Signal is connected to lock from the read actuator power status repsonse
            public byte CheckLockSignalStatus(List<KineticsResponse> response) {
                return KineticComms.Instance.CheckLockSignalStatus(new ArrayList<KineticsResponse>(response)) ? (byte) 1 : (byte) 0;
            }


            //requests machine to start power off sequence
            public ParcelUuid TurnOffPower(String connectionID) {
                return KineticComms.Instance.TurnOffPower(connectionID);
            }

            //
//        //writes given bytes to mainboard eeprom Address location
         public ParcelUuid WriteToEEPROM(String connectionID, EEPROMDetails AddressDetails, int Data)
         {
             return KineticComms.Instance.WriteToEEPROM(connectionID, AddressDetails, Data);
         }
//
            //Check if EEPROM write is successful
            public byte CheckWriteToEEPROM(KineticsResponse Response) {
                return KineticComms.Instance.CheckWriteToEEPROM(Response) ? (byte) 1 : (byte) 0;
            }
//
//        //read given number of bytes from from mainboard eeprom Address location
          public ParcelUuid ReadFromEEPROM(String connectionID, EEPROMDetails AddressDetails)
          {
              return KineticComms.Instance.ReadFromEEPROM(connectionID, AddressDetails);
          }

            //Returns the payload bytes from EEPROMReadResonse Command
            public int[] ExtractBytesFromEEPROMReadResponse(KineticsResponse EEPROMReadResponse) {
                ArrayList<Integer> list = KineticComms.Instance.ExtractBytesFromEEPROMReadResponse(EEPROMReadResponse);
                int[] array = new int[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    array[i] = list.get(i);
                }
                ;
                return array;
            }

            //
//        //read given number of bytes from from Servo board eeprom Address location
            public ParcelUuid ReadFromServoEEPROM(String connectionID, String actuator, EEPROMDetails AddressDetails)
            {
                return KineticComms.Instance.ReadFromServoEEPROM(connectionID, Actuator.valueOf(actuator), AddressDetails);
            }
//
//
            //Returns the payload bytes from ServoEEPROMReadResonse Command
            public int[] ExtractBytesFromServoEEPROMReadResponse(KineticsResponse EEPROMReadResponse) {
                ArrayList<Integer> list = KineticComms.Instance.ExtractBytesFromServoEEPROMReadResponse(EEPROMReadResponse);
                int[] array = new int[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    array[i] = list.get(i);
                }
                ;
                return array;
            }

            //Deletes calibration Data From DataBase for the given actuator
            public void DeleteCalibrationDataForActuator(String actuator) {
                KineticComms.Instance.DeleteCalibrationDataForActuator(Actuator.valueOf(actuator));
            }

            //Saves Servo Degree and ADC Value
            public void SaveActuatorCalibrationData(String actuator, int Degree, int ADC) {
                KineticComms.Instance.SaveActuatorCalibrationData(Actuator.valueOf(actuator), Degree, ADC);
            }

            public void ConnectToKineticServices(Map KineticsConveyList)
            {
                KineticComms.Instance.ConnectToKineticServices(KineticsConveyList);
            }


            //writes a byte from from ISL94203 EEPROM Address location
            public ParcelUuid WriteToISLEEPROM(String connectionID, int Address, int numberOfBytes, int Value)
            {
               return KineticComms.Instance.WriteToISLEEPROM(connectionID, Address, numberOfBytes, Value);
            }

            //read a byte from from ISL94203 RAM Address location
            public ParcelUuid ReadFromISLEEPROM(String connectionID, int Address, int numberOfBytes)
            {
                return KineticComms.Instance.ReadFromISLEEPROM(connectionID, Address, numberOfBytes);
            }

            //writes a byte from from ISL94203 RAM Address location
            public ParcelUuid WriteToISLRAM(String connectionID, int Address, int numberOfBytes, int Value)
            {
                return KineticComms.Instance.WriteToISLRAM(connectionID, Address, numberOfBytes, Value);
            }

            //read a byte from from ISL94203 eeprom Address location
            public ParcelUuid ReadFromISLRAM(String connectionID, int Address, int numberOfBytes)
            {
                return KineticComms.Instance.ReadFromISLRAM(connectionID, Address, numberOfBytes);
            }

            //Returns the payload bytes from ISLEEPROMReadResonse Command
            public int[] ExtractBytesFromISLEEPROMReadResponse(KineticsResponse ISLEEPROMReadResponse)
            {
                ArrayList<Integer> bytes = KineticComms.Instance.ExtractBytesFromISLEEPROMReadResponse(ISLEEPROMReadResponse);
                int[] arrbytes = new int[bytes.size()];
                for(int i=0; i< bytes.size(); i++)
                    arrbytes[i] = bytes.get(i);
                return  arrbytes;
            }

            //Returns the payload bytes from ISLRAMReadResonse Command
            public int[] ExtractBytesFromISLRAMReadResponse(KineticsResponse ISLRAMReadResponse)
            {
                ArrayList<Integer> bytes =  KineticComms.Instance.ExtractBytesFromISLRAMReadResponse(ISLRAMReadResponse);
                int[] arrbytes = new int[bytes.size()];
                for(int i=0; i< bytes.size(); i++)
                    arrbytes[i] = bytes.get(i);
                return  arrbytes;
            }

            //Check if ISLEEPROM write is successful
            public byte CheckWriteToISLEEPROM(KineticsResponse Response)
            {
                return KineticComms.Instance.CheckWriteToISLEEPROM(Response)? (byte)1 : (byte)0;
            }

            //Check if ISLRAM write is successful
            public byte CheckWriteToISLRAM(KineticsResponse Response)
            {
                return KineticComms.Instance.CheckWriteToISLRAM(Response)? (byte)1 : (byte)0;
            }

            public void BindMachine(String connectionID)
            {
                KineticComms.Instance.BindMachine(connectionID);
            }

            public void UnBindMachine(String connectionID)
            {
                KineticComms.Instance.UnBindMachine(connectionID);
            }

            public MachineConfig GetMachineConfig()
            {
               return KineticComms.Instance.GetMachineConfig();
            }
        };
    }

    UIMAINConvey UserEventConvey;

    Context _Context;

    enum InitializationStages {
        SCAN,
        CONNECT,
        NA
    }
    KineticsStartServiceReciever.InitializationStages initializationStage = KineticsStartServiceReciever.InitializationStages.NA;

    //SystemInitializationUIConvey
    public void ClearScanDeatils()
    {
    }

    public void  ResetInitializing()
    {

    }

    public void  Scanning()
    {
        initializationStage = KineticsStartServiceReciever.InitializationStages.SCAN;
    }

    public void  Binding()
    {
        initializationStage = KineticsStartServiceReciever.InitializationStages.CONNECT;
    }

    public void  PauseApplication()
    {

    }

    public void ShutdownApplication()
    {

    }

    public void  ResumeApplication()
    {

    }
    public void  NewMachineFound(Machine Device)
    {
        DiscoveredNewDevice(Device);
    }

    public void  MachinePoweredOff()
    {
    }

    public void  MachineDisconnected()
    {

    }

    public void  MachineConected()
    {
    }

    public void  MachineConnectionTimeout()
    {

    }

    public void  MachineIsMovingTOMountPosition()
    {

    }

    public void  MachineCheckingInitialProximity()
    {
    }

    public void  MachinWaitingForMounting()
    {

    }

    boolean IsServiceInitialized =false;
    public void MachineLoadKineticsService()
    {
        IsServiceInitialized =true;
//        Intent intent = new Intent(_Context,KineticsService.class);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            _Context.startForegroundService(intent);
//        } else {
//            _Context.startService(intent);
//        }
        Log.i("Ani Kinetics", "Machine Kinetics Service started");
    }

    public void ShutdownRequest()
    {
        try {
            PowerManager powerManager = (PowerManager)_Context.getSystemService(Context.POWER_SERVICE);
            powerManager.shutdown(false, null, false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void ShowBlankScreen()
    {
    }
    //End of SystemInitializationUIConvey



    public void DiscoveredNewDevice(final Machine Name) {
        if(initializationStage == KineticsStartServiceReciever.InitializationStages.SCAN ) {
            if(Name.Name.equals("FTDI_ANI_232"))
            // if(Name.Name.equals("/dev/ttyUSB0"))
            {
                UserEventConvey.UserSelectedAMachine(Name);
            }
        }
    }



    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";


    public boolean grantAutomaticPermission(UsbDevice usbDevice)
    {
        try
        {
            Context context= _Context;
            PackageManager pkgManager=context.getPackageManager();
            ApplicationInfo appInfo=pkgManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);

            Class serviceManagerClass=Class.forName("android.os.ServiceManager");
            Method getServiceMethod=serviceManagerClass.getDeclaredMethod("getService",String.class);
            getServiceMethod.setAccessible(true);
            android.os.IBinder binder=(android.os.IBinder)getServiceMethod.invoke(null, Context.USB_SERVICE);

            Class iUsbManagerClass=Class.forName("android.hardware.usb.IUsbManager");
            Class stubClass=Class.forName("android.hardware.usb.IUsbManager$Stub");
            Method asInterfaceMethod=stubClass.getDeclaredMethod("asInterface", android.os.IBinder.class);
            asInterfaceMethod.setAccessible(true);
            Object iUsbManager=asInterfaceMethod.invoke(null, binder);


            System.out.println("UID : " + appInfo.uid + " " + appInfo.processName + " " + appInfo.permission);
            final Method grantDevicePermissionMethod = iUsbManagerClass.getDeclaredMethod("grantDevicePermission", UsbDevice.class,int.class);
            grantDevicePermissionMethod.setAccessible(true);
            grantDevicePermissionMethod.invoke(iUsbManager, usbDevice,appInfo.uid);


            System.out.println("PERMISSION OK : "+usbDevice.getManufacturerName());
            return true;
        }
        catch(Exception e)
        {
            System.err.println("Error trying to assing automatic usb permission : ");
            e.printStackTrace();
            return false;
        }
    }

    private final BroadcastReceiver mUsbReceiver;
    {
        mUsbReceiver = new BroadcastReceiver() {

            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (ACTION_USB_PERMISSION.equals(action)) {
                    synchronized (this) {
                        UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                        if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                            if (device != null) {

                                _Context.unregisterReceiver(mUsbReceiver);

                                Log.d("USB Device Tester", "permission granted for device " + device);
                                DO_CHECK();
                            }
                        } else {
                            Log.d("AUDIOMODULETEster", "permission denied for device " + device);
                        }
                    }
                }
            }
        };
    }

    public boolean RequestUSBSerialPermission() {
        UsbDevice serial = UIMAINModuleHandler.Instance.GetUIMainConveyListener().GetUSBSerialDevice();
        if(!grantAutomaticPermission(serial)) {
            if (serial != null) {

                UsbManager mUsbManager = (UsbManager) _Context.getSystemService(Context.USB_SERVICE);

                PendingIntent mPermissionIntent = PendingIntent.getBroadcast(_Context, 0, new Intent(ACTION_USB_PERMISSION), 0);
                IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
                _Context.registerReceiver(mUsbReceiver, filter);
                mUsbManager.requestPermission(serial, mPermissionIntent);
                return true;
            }

        }
        else
        {
            DO_CHECK();
            return  true;
        }

        return false;
    }





    private final int WRITE_REQUEST_CODE = 300;

    public Boolean RequestStoragePermission() {

        PermissionEverywhere.getPermission(_Context,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                WRITE_REQUEST_CODE,
                "Notification title",
                "This app needs a write permission",
                R.mipmap.ic_launcher)
                .enqueue(new PermissionResultCallback() {
                    @Override
                    public void onComplete(PermissionResponse permissionResponse) {
                        DO_CHECK();
                    }
                });

        return  true;
    }



    private   enum PERMISSION_STAGES{USBSERIAL, STORAGE, OK}
    private PERMISSION_STAGES PERMISSION_STAGE = PERMISSION_STAGES.USBSERIAL;
    public void DO_CHECK()
    {
        switch (PERMISSION_STAGE)
        {
            case USBSERIAL:
            {
                PERMISSION_STAGE = PERMISSION_STAGES.STORAGE;
                RequestUSBSerialPermission();
                break;
            }
            case STORAGE:
            {
                PERMISSION_STAGE = PERMISSION_STAGES.OK;
                RequestStoragePermission();
                break;
            }
            case OK:
            {
                UserEventConvey.AppStarted();
                break;
            }
        }
    }
}
