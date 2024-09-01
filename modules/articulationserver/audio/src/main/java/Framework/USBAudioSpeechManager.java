package Framework;

import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import Framework.Data.Delegates.AudioDelegate;
import Framework.Data.Delegates.VoiceDelegate;
import Framework.Data.RESP_PARAM_TYPE;
import Framework.Data.Respeaker4MicConfig;
import Framework.Data.SPDET;
import Framework.Data.VOICE_PROCESSING_TYPE;
import Framework.DataTypes.GlobalContext;

import static Framework.Data.VOICE_PROCESSING_TYPE.NA;

public class USBAudioSpeechManager implements VoiceDelegate {

    Timer VoicePauseChaser = new Timer();
    public AudioDelegate audioDelegate;
    //if no voice detected for 1.5 sec end recording
    int PauseTimeout = 1500;

    //Start of VoiceDelegate
    public void RecievedData(String Data)
    {
        if(audioDelegate != null && !Data.equals(null) && !Data.equals(""))
        {
            SetProcessingType(NA);
            audioDelegate.RecievedData(Data);
        }
    }

    public void DetectedWakeWord()
    {
        if(audioDelegate != null)
        {
            audioDelegate.DetectedWakeWord();
        }

    }
    //End of VoiceDelegate

    private Respeaker4MicConfig MicConfig = new Respeaker4MicConfig();

    boolean IsInferanceStarted = false;
    boolean IsVoiceDetected = false;
    private boolean KeepReadingAudioParameters = false;
    private boolean IsReadingAudioParameters = false;

    UsbManager usbManager;

    AudioRecorder aRecorder;




    public USBAudioSpeechManager() {
        FindUSBAudioDevice();
    }

    public UsbDevice Repseaker;
    UsbDeviceConnection connection;

    public boolean FindUSBAudioDevice() {
        usbManager = (UsbManager) GlobalContext.context.getSystemService(GlobalContext.context.USB_SERVICE);


        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        while (deviceIterator.hasNext()) {
            UsbDevice device = deviceIterator.next();

            int VID = device.getVendorId();
            int PID = device.getProductId();
            if (PID == 0x0018 && VID == 0x2886) {
                Repseaker = device;
                return true;
            }
        }

        return false;
    }


    public void StopReadingAudioParameters()
    {
        if(!IsReadingAudioParameters)
            return;

        KeepReadingAudioParameters = false;

    }

     VOICE_PROCESSING_TYPE ProcessingMode = NA;

    public  void SetProcessingType(VOICE_PROCESSING_TYPE ProcessingType)
    {
        ProcessingMode = ProcessingType;
        if(ProcessingMode.equals(VOICE_PROCESSING_TYPE.NA))
        {
            Log.d("PROCESSIGN", "SetProcessingType: ");
        }
    }

    public void StartReadingAudioParameters() {

        KeepReadingAudioParameters = false;
        while (IsReadingAudioParameters) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(!KeepReadingAudioParameters) {
            aRecorder = new AudioRecorder(GlobalContext.context);
            aRecorder.SetSpeechDelegate(this);
            KeepReadingAudioParameters = true;

            IsReadingAudioParameters = true;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (connection == null) {
                        UsbInterface intf = Repseaker.getInterface(2);
                        UsbEndpoint endPoint = intf.getEndpoint(1);

                        connection = usbManager.openDevice(Repseaker);
                        connection.claimInterface(intf, true);
                    }
                    if (aRecorder != null) {
                        aRecorder.StartRecording();
                    }

                    IsInferanceStarted = false;
                    IsVoiceDetected = false;

                    while (KeepReadingAudioParameters) {
                        try {
                            if (Repseaker != null) {
                                ReadParameter(connection, RESP_PARAM_TYPE.DOAANGLE);
                                ReadParameter(connection, RESP_PARAM_TYPE.SPEECHDETECTED);

                                if (((SPDET) MicConfig.Parameters.get(RESP_PARAM_TYPE.SPEECHDETECTED)).IsSpeachdetected) {
                                    if (ProcessingMode != NA && audioDelegate != null) {
                                        audioDelegate.DetectedSpeech(true);
                                    }
                                }

                                if (IsVoiceDetected != ((SPDET) MicConfig.Parameters.get(RESP_PARAM_TYPE.SPEECHDETECTED)).IsSpeachdetected) {


                                    if (((SPDET) MicConfig.Parameters.get(RESP_PARAM_TYPE.SPEECHDETECTED)).IsSpeachdetected) {

                                        if (!IsInferanceStarted) {
                                            if (ProcessingMode == VOICE_PROCESSING_TYPE.SPEECH_RECOGNITION)
                                                IsInferanceStarted = aRecorder.StartRecognizingSpeech();
                                            else if (ProcessingMode == VOICE_PROCESSING_TYPE.WAKE_WORD_DETECTION)
                                                IsInferanceStarted = aRecorder.StartWakeWordDetection();
                                            Log.d("SPEAKER STAT", "VOICE STARTED");
                                        }

                                        if (VoicePauseChaser != null && !IsVoiceDetected) {
                                            VoicePauseChaser.cancel();
                                        }

                                    } else {
                                        if (IsInferanceStarted) {

                                            if (VoicePauseChaser != null) {
                                                VoicePauseChaser.cancel();
                                            }
                                            VoicePauseChaser = new Timer();
                                            VoicePauseChaser.schedule(new TimerTask() {
                                                @Override
                                                public void run() {
                                                    VoicePauseChaser.cancel();
                                                    if (aRecorder != null) {

                                                        aRecorder.StopVoiceInferance();

                                                        IsInferanceStarted = false;
                                                        Log.d("SPEAKER STAT", "VOICE STOPPED");
                                                    }
                                                }
                                            }, 300);


                                        }
                                    }
                                    IsVoiceDetected = ((SPDET) MicConfig.Parameters.get(RESP_PARAM_TYPE.SPEECHDETECTED)).IsSpeachdetected;
                                }
                            }
                            Thread.sleep(1);
                        } catch (Exception e) {
                            Log.d("SPEAKER STAT", e.getMessage());
                        }
                    }

                    Log.d("SPEAKER STAT", "loop done");

                    IsVoiceDetected = false;

                    if (IsInferanceStarted) {

                        if (VoicePauseChaser != null) {
                            VoicePauseChaser.cancel();
                        }
                        aRecorder.StopVoiceInferance();
                    }

                    aRecorder.StopRecording();


                    IsReadingAudioParameters = false;
                }
            });
            t.start();
        }
    }




    public void ReadParameter(UsbDeviceConnection connection, RESP_PARAM_TYPE Param_Type) {
        switch (MicConfig.Parameters.get(Param_Type).Data_Type) {
            case INT:
                ReadInt(connection, Param_Type);
                break;

            case FLOAT:
                break;
        }
    }

    public  void ReadInt(UsbDeviceConnection connection, RESP_PARAM_TYPE Param_Type)
    {
        int cmd = (0x80 | MicConfig.Parameters.get(Param_Type).Offset);
        cmd |= 0x40;

        short length = 8;

        int requestType = (UsbConstants.USB_DIR_IN | UsbConstants.USB_TYPE_VENDOR);

        byte[] buf = new byte[8];
        int ret =  connection.controlTransfer(requestType, 0, cmd, MicConfig.Parameters.get(Param_Type).Id, buf ,8, 2000   );
        if(ret >= 0) {
            MicConfig.Parameters.get(Param_Type).SetRawData(buf);
        }
    }

    private void ReadFloat(UsbDeviceConnection connection, RESP_PARAM_TYPE Param_Type)
    {
        byte cmd = (byte)(0x80 | MicConfig.Parameters.get(Param_Type).Offset);

        short length = 8;

        int requestType = (UsbConstants.USB_DIR_IN | UsbConstants.USB_TYPE_VENDOR);

        byte[] buf = new byte[8];
        int ret =  connection.controlTransfer(requestType, 0, cmd, MicConfig.Parameters.get(Param_Type).Id, buf ,8, 2000   );
        if(ret >= 0) {
            MicConfig.Parameters.get(Param_Type).SetRawData(buf);
        }
    }

}


