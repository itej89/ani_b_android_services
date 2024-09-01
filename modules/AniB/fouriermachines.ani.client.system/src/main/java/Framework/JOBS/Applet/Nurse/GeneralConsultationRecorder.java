package Framework.JOBS.Applet.Nurse;

import android.os.Environment;
import android.util.Log;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import Framework.DataTypes.GlobalContext;
import client.ani.fouriermachines.fouriermachinesaniclientsystem.R;
import fm.ani.client.db.DB_Basic_Operations;
import fm.ani.client.db.DB_Local_Store;
import fm.ani.client.db.DataTypes.Applet.Nurse.GeneralConsulation.BPM;
import fm.ani.client.db.DataTypes.Applet.Nurse.GeneralConsulation.HEARTBEAT;
import fm.ani.client.db.DataTypes.Applet.Nurse.GeneralConsulation.PULSE;
import fm.ani.client.db.DataTypes.Applet.Nurse.GeneralConsulation.SPO2;
import fm.ani.client.db.DataTypes.Applet.Nurse.GeneralConsulation.TEMPERATURE;
//import fouriermachines.ani.client.nexcloud.Framework.Delegates.nexCloudUploadNotify;
//import fouriermachines.ani.client.nexcloud.FrameworkInterface.NexCloudManager;

import static Framework.DataTypes.GlobalContext.context;

//public class GeneralConsultationRecorder implements nexCloudUploadNotify {
    public class GeneralConsultationRecorder  {

    boolean IsRecording = false;
    public String CopyDBTemplate() {
        //Open your local db as the input stream
        String DBTemplatePath = DB_Local_Store.GetDefaultDBPathFromName(DB_Local_Store.GC_TEMPLATE_DB);

        File dbFile = new File(DBTemplatePath);

        try {

            if (!dbFile.exists())
            {
                File folder = new File(Environment.getExternalStorageDirectory() +
                        File.separator + "GeneralConsultations");

                if (!folder.exists()) {
                    folder.mkdirs();
                }
                else
                {
                     if (folder.isDirectory())
                    {
                        String[] children = folder.list();
                        for (int i = 0; i < children.length; i++)
                        {
                            new File(folder, children[i]).delete();
                        }
                    }
                }

                Long tsLong = System.currentTimeMillis()/1000;
                String timeStamp = tsLong.toString();

                File GCRecordingPath = new File(Environment.getExternalStorageDirectory()+File.separator+"GeneralConsultations", "GeneralConsultation.B");
                GCRecordingPath.deleteOnExit();
                InputStream myInput = context.getAssets().open(DB_Local_Store.GC_TEMPLATE_DB);

                //Open the empty db as the output stream
                OutputStream myOutput = new FileOutputStream(GCRecordingPath.getPath(), false);

                //transfer bytes from the inputfile to the outputfile
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }

                //Close the streams
                myOutput.flush();
                myOutput.close();
                myInput.close();

                return  GCRecordingPath.getPath();

            }
        } catch (Exception ex)
        {
            Log.d("Error", ex.getMessage());
        }


        return "Database Created Successfully!";
    }

    List<byte[]> PCM = new ArrayList<byte[]>();

    String RecordPath = "";

    DB_Local_Store _recordStore;

    public void PrepareRecoding()
    {
        RecordPath = CopyDBTemplate();
        //Copy DB to Record Path
        PCM = new ArrayList<>();
        _recordStore = new DB_Local_Store(RecordPath);
        IsRecording = true;
    }

    public void AddPulse(int _Pulse)
    {
        if(!IsRecording) return;
        PULSE _pulse = new PULSE(StringDateToDate(GetUTCdatetimeAsString()), _Pulse);
        _recordStore.InsertPulse(_pulse);
    }

    public void AddBPM(int bpm)
    {
        if(!IsRecording) return;
        BPM _bpm = new BPM(StringDateToDate(GetUTCdatetimeAsString()), bpm);
        _recordStore.InsertBPM(_bpm);
    }

    public void AddSPO2(int spo2)
    {
        if(!IsRecording) return;
        SPO2 _spo2 = new SPO2(StringDateToDate(GetUTCdatetimeAsString()), spo2);
        _recordStore.InsertSPO2(_spo2);
    }

    public void AddTEMPERATURE(double temperature)
    {
        if(!IsRecording) return;
        TEMPERATURE _TEMPERATURE = new TEMPERATURE(StringDateToDate(GetUTCdatetimeAsString()), temperature);
        _recordStore.InsertTemperature(_TEMPERATURE);
    }

    public void AddBeat(byte[] _PCM)
    {
        if(!IsRecording) return;
        PCM.add(_PCM);
    }

    public  void WriteToFile(byte[] PCMData)
    {
        File file = new File(Environment.getExternalStorageDirectory()+File.separator+"GeneralConsultations", "Beat.pcm");

        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);

            // Writes bytes from the specified byte array to this file output stream
            fos.write(PCMData);
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found" + e);
        }
        catch (IOException ioe) {
            System.out.println("Exception while writing file " + ioe);
        }
        finally {
            // close the streams using close method
            try {
                if (fos != null) {
                    fos.close();
                }
            }
            catch (IOException ioe) {
                System.out.println("Error while closing stream: " + ioe);
            }
        }
    }


    public  void EndRecording()
    {
        IsRecording = false;

        if(PCM.size() >0) {
            int PCMLength = 0;
            for (int i = 0; i < PCM.size(); i++)
                PCMLength += PCM.get(i).length;

            byte[] PCMData = new byte[PCMLength];
            int index = 0;
            for (int i = 0; i < PCM.size(); index += PCM.get(i).length, i++)
                System.arraycopy(PCM.get(i), 0, PCMData, index, PCM.get(i).length);

            HEARTBEAT _HEARTBEAT = new HEARTBEAT(StringDateToDate(GetUTCdatetimeAsString()), PCMData);
            WriteToFile(PCMData);
            _recordStore.InsertHeartBeat(_HEARTBEAT);
        }
        _recordStore.CloseDBConnection();
    }


    public void nexCloudUploadFinished()
    {

    }
    public void RequestConsultation()
    {


        File DBCache_shm = new File(Environment.getExternalStorageDirectory()+File.separator+"GeneralConsultations", "GeneralConsultation.B-shm");
        File DBCache_wal = new File(Environment.getExternalStorageDirectory()+File.separator+"GeneralConsultations", "GeneralConsultation.B-wal");
        long tStart = System.currentTimeMillis();
        while (DBCache_shm.exists() || DBCache_wal.exists())
        {
            if(System.currentTimeMillis() - tStart > 10000)
            {
                return;
            }
            try {

                Thread.sleep(100);
            }
            catch (Exception e){}
        }

        String RemotePath = context.getString(R.string.general_consulation_upload)+File.separator+GetUTCdatetimeAsString();

        HashMap<String, ArrayList<String>> Directories = new HashMap<>();
        Directories.put(RemotePath, new ArrayList<String>());

        File GCRecordingPCMfile = new File(Environment.getExternalStorageDirectory()+File.separator+"GeneralConsultations", "GeneralConsultation.B");
        if(GCRecordingPCMfile.exists())
            Directories.get(RemotePath).add(GCRecordingPCMfile.getAbsolutePath());

        File NurseRecordingDBfile = new File(Environment.getExternalStorageDirectory()+File.separator+"GeneralConsultations", "Beat.pcm");
        if(NurseRecordingDBfile.exists())
            Directories.get(RemotePath).add(NurseRecordingDBfile.getAbsolutePath());

//        NexCloudManager nexCloudManagter = new NexCloudManager(this);
//        nexCloudManagter.BeginDirectoryUpload(Directories);
    }


    static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";

    public static Date GetUTCdatetimeAsDate()
    {
        //note: doesn't check for null
        return StringDateToDate(GetUTCdatetimeAsString());
    }

    public static String GetUTCdatetimeAsString()
    {
        final SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        final String utcTime = sdf.format(new Date());

        return utcTime;
    }
    public static Date StringDateToDate(String StrDate)
    {
        Date dateToReturn = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);

        try
        {
            dateToReturn = (Date)dateFormat.parse(StrDate);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return dateToReturn;
    }
}
