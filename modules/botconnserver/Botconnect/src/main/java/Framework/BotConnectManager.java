package Framework;

import android.os.Environment;
import android.util.Log;
import android.util.Pair;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import Framework.DOIPLayer.DOIPStateMachines.DOIPTesterContext;
import Framework.DataTypes.EndPointInformation;
import Framework.DataTypes.Enums.ACK;
import Framework.DataTypes.Enums.COMMAND_TYPES;
import Framework.DataTypes.FrameParsers.RxFrames.REQUEST_ACK;
import Framework.DataTypes.Transports.Helpers.RecievedData;
import FrameworkImplementation.DataTypes.BotConnectionInfo;
import FrameworkImplementation.DataTypes.Delegates.BotConnectConvey;
import FrameworkImplementation.DataTypes.Delegates.LinkTransportConvey;
import FrameworkImplementation.DataTypes.Enums.LINK_ANCHORS;
import Framework.DataTypes.Extras.DOIPContextConvey;
import Framework.DataTypes.Extras.DOIPContextResultConvey;
import Framework.DataTypes.FrameParsers.RxFrames.BIND;
import Framework.DataTypes.FrameParsers.RxFrames.CATEGORY_ACK;
import Framework.DataTypes.FrameParsers.RxFrames.COMMAND_ACK;
import Framework.DataTypes.FrameParsers.RxFrames.DATA_ACK;
import Framework.DataTypes.FrameParsers.RxFrames.LINK_ACK;
import Framework.DataTypes.FrameParsers.RxFrames.REQEST_UPLOAD_ACK;
import Framework.DataTypes.FrameParsers.RxFrames.STREAM_ACK;
import Framework.DataTypes.FrameParsers.RxFrames.UNBIND;
import Framework.DataTypes.FrameParsers.RxFrames.UPLOAD_END_ACK;
import Framework.DataTypes.FrameParsers.TxFrames.LINK;
import Framework.DataTypes.FrameParsers.TxFrames.STREAM;
import Framework.DataTypes.IPEndPoint;
import Framework.DataTypes.LinkInformation;
import Framework.DataTypes.ZipManager;
import Framework.DataTypes.FrameParsers.Parsers.RxFrameParser;
import Framework.DataTypes.FrameParsers.TxFrames.CATEGORY;
import Framework.DataTypes.FrameParsers.TxFrames.COMMAND;
import Framework.DataTypes.FrameParsers.TxFrames.DATA;
import Framework.DataTypes.FrameParsers.TxFrames.REQEST_UPLOAD;
import Framework.DataTypes.FrameParsers.TxFrames.TxBaseFrame;
import Framework.DataTypes.FrameParsers.TxFrames.UPLOAD_END;
import Framework.DataTypes.Enums.ANSTMSG;
import Framework.DataTypes.Enums.CATEGORY_TYPES;
import fm.ani.client.db.DB_Local_Store;
import fm.ani.client.db.DataTypes.Choreogram.ACTS;
import fm.ani.client.db.DataTypes.Choreogram.BeatsType;
import fm.ani.client.db.DataTypes.Choreogram.Constants.Choreogram_Table_Columns;


public class BotConnectManager implements DOIPContextResultConvey, DOIPContextConvey {

    public BotConnectConvey botDelegate;

    public  Map<String, EndPointInformation> EndPointData = new HashMap<>();


    public void UDSResponseRecieved(IPEndPoint Endpoint, byte[] response)
    {
        byte[] byteArray =(response);

        try {
            String strFrame = new String(byteArray, "UTF-8");
            RxFrameParser rxFrameParser = new RxFrameParser();
            if(strFrame != null && strFrame != "")
            {
                Pair<ANSTMSG, TxBaseFrame> state = rxFrameParser.GetBaseFrame(strFrame);
                if(state.first != ANSTMSG.NA)
                {
                    FrameRecieved(Endpoint, state.second.jANSTMSG,  strFrame);
                }
            }
        }
        catch (Exception e){}
    }

   protected boolean IsInitialized = false;




    public void InitializeResultNotify(IPEndPoint EndPoint, int result)
    {}
    public void UDSSendResultNotify(IPEndPoint EndPoint, int result)
    {}
    public  void LinkConnected(IPEndPoint EndPoint)
    {
        EndPointData.put(EndPoint.IPAddress, new EndPointInformation(EndPoint));
    }

    public void LinkDisconnected(IPEndPoint EndPoint)
    {
//        if(IsInitialized) {
//            UnInitialize();
//            DOIPTesterContext.Instance.Initialize(this, this);
//        }
        if(EndPointData.keySet().contains(EndPoint.IPAddress))
        {
            EndPointData.remove(EndPoint.IPAddress);
        }
    }

    public  String ID =  UUID.randomUUID().toString();

    Map<IPEndPoint, ANSTMSG> PreviousFrameTypes = new HashMap<IPEndPoint, ANSTMSG>();

    int BlockCounter = 0;

    String StorageLocaion = "";
    CATEGORY_TYPES category = CATEGORY_TYPES.NA;
    File DebugFile;
    int Offset = 0;

    public  ANSTMSG GetPreviousFrameType(IPEndPoint EndPoint){
        for (Map.Entry<IPEndPoint, ANSTMSG> entry : PreviousFrameTypes.entrySet()) {
            if(entry.getKey().IPAddress.equals(EndPoint.IPAddress))
            {
                return  entry.getValue();
            }
        }
        return  ANSTMSG.NA;
    }

    public  void SetPreviousFrameType(IPEndPoint EndPoint, ANSTMSG Type){
        for (Map.Entry<IPEndPoint, ANSTMSG> entry : PreviousFrameTypes.entrySet()) {
            if(entry.getKey().IPAddress.equals(EndPoint.IPAddress))
            {
                entry.setValue(Type);
                return;
            }
        }
        PreviousFrameTypes.put(EndPoint, Type);
    }

    public void FrameRecieved(IPEndPoint EndPoint, ANSTMSG FrameType, String Json) {
        RxFrameParser rxFrameParser = new RxFrameParser();
        Pair<ANSTMSG, TxBaseFrame> state = rxFrameParser.GetRxObject(Json);
        String FrameID = (state.second).FRAME_ID;
        switch(FrameType) {
            case CATEGORY:
                category = ((CATEGORY)state.second).TYPE;
                SetPreviousFrameType(EndPoint, ANSTMSG.CATEGORY);
                FrameID = ((CATEGORY)state.second).FRAME_ID;
                ANIActionModeSet(EndPoint, FrameID, ID);
                break;
            case DATA:
                if(GetPreviousFrameType(EndPoint) == ANSTMSG.REQEST_UPLOAD || GetPreviousFrameType(EndPoint) == ANSTMSG.DATA) {
                    if((BlockCounter+1) == ((DATA)state.second).BLOCK_COUNT) {
                        SetPreviousFrameType(EndPoint, ANSTMSG.DATA);
                        BlockCounter = ((DATA)state.second).BLOCK_COUNT;
                        FrameID = ((DATA) state.second).FRAME_ID;
                        try {
                            FileOutputStream fos=new FileOutputStream(DebugFile.getPath(), true);

                            fos.write(((DATA) state.second).DATA);
                            fos.close();
                        }
                        catch (java.io.IOException e) {
                            Log.e("File save", "Exception in File save", e);
                        }

                        SendDataAck(EndPoint, FrameID, ID);
                    }
                }
                break;
            case REQEST_UPLOAD:
                if(GetPreviousFrameType(EndPoint) == ANSTMSG.CATEGORY) {
                    SetPreviousFrameType(EndPoint, ANSTMSG.REQEST_UPLOAD);
                    BlockCounter = 0;
                    Offset = 0;
                    FrameID = ((REQEST_UPLOAD) state.second).FRAME_ID;
                    DebugFile=new File(Environment.getExternalStorageDirectory(), category.toString()+".B");
                    if (DebugFile.exists()) {
                        DebugFile.delete();
                    }
                    RequestUploadAck(EndPoint, FrameID, ID);
                }
                break;
            case UPLOAD_END:
                if(GetPreviousFrameType(EndPoint) == ANSTMSG.DATA) {
                    SetPreviousFrameType(EndPoint, ANSTMSG.UPLOAD_END);
                    FrameID = ((UPLOAD_END) state.second).FRAME_ID;
                    ExitUploadAck(EndPoint, FrameID, ID);
                }
                break;
            case COMMAND:
                SetPreviousFrameType(EndPoint, ANSTMSG.COMMAND);
                    FrameID = ((COMMAND) state.second).FRAME_ID;

                    if(((COMMAND)state.second).COMMAND == COMMAND_TYPES.PLAY_CHOREOGRAM &&
                            ((COMMAND)state.second).TYPE == CATEGORY_TYPES.CHOREOGRAM)
                    {
                        File ZipFile = new File(Environment.getExternalStorageDirectory(), "CHOREOGRAM.B");

                        if(!ZipFile.exists())
                        {
                            return;
                        }

                        File CHOREOGRAMDIRECTORY = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/CHOREOGRAM");

                        if (CHOREOGRAMDIRECTORY.isDirectory())
                        {
                            String[] children = CHOREOGRAMDIRECTORY.list();
                            for (int i = 0; i < children.length; i++)
                            {
                                new File(CHOREOGRAMDIRECTORY, children[i]).delete();
                            }
                        }

                        CHOREOGRAMDIRECTORY.mkdirs();
                        DB_Local_Store dbHelper = null;
                        try {
                            ZipManager.unzip(ZipFile.getAbsolutePath(), CHOREOGRAMDIRECTORY.getAbsolutePath());



                            File DBFile = new File(CHOREOGRAMDIRECTORY, "Choreogram.db");

                            if(!DBFile.exists())
                            {
                                return;
                            }

                             dbHelper = new DB_Local_Store(DBFile.getAbsolutePath());
                            ArrayList<ACTS> acts = dbHelper.ReadActs(Choreogram_Table_Columns.DBTables.ACTS.toString());

                            if(acts == null || acts.size() == 0)
                            {
                                return;
                            }

                            File audioFile = new File(CHOREOGRAMDIRECTORY, acts.get(0).Audio);
                            if(!audioFile.exists())
                            {
                                return;
                            }

                            ArrayList<BeatsType> beats = dbHelper.ReadBeatsByActID(Choreogram_Table_Columns.DBTables.BEATS.toString(), acts.get(0).ID);
                            if(beats != null && beats.size() > 0) {
                                Collections.sort(beats, new BeatsSorter());
                                int StartSec = beats.get(0).StartSec;
                                int EndSec = beats.get(beats.size() - 1).EndSec;

                                if(botDelegate != null)
                                {
                                    botDelegate.RunChoreogram(audioFile.getAbsolutePath(), StartSec, EndSec, beats);
                                }
                            }
                            else
                            {
                                return;
                            }
                        }
                        catch (Exception e){
                            Log.e("File extract", "Exception in choreogram parse ", e);

                            return;
                        }
                        finally {
                            if(dbHelper != null)
                            {
                                dbHelper.CloseDBConnection();
                            }
                        }
                    }
                SendCommandAck(EndPoint, FrameID, ID);
                break;

            case LINK:
                SetPreviousFrameType(EndPoint, ANSTMSG.LINK);
                FrameID = ((LINK)state.second).FRAME_ID;
                for (Map.Entry<LINK_ANCHORS, LinkInformation> entry : ((LINK)state.second).LINK_INFO.entrySet())
                {
                    if(EndPointData.get(EndPoint.IPAddress).LinkData.containsKey(entry.getKey()))
                    {
                        EndPointData.get(EndPoint.IPAddress).LinkData.get(entry.getKey()).LinkExpired();
                        EndPointData.get(EndPoint.IPAddress).LinkData.remove(entry.getKey());
                    }
                     final LinkInformation _LinkInformation =  entry.getValue();
                    _LinkInformation.FrameID = FrameID;
                    _LinkInformation.LinkEndPoint = EndPoint;

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            botDelegate.LinkDiscovered(_LinkInformation.LinkAnchor);

                        }
                    });
                    thread.start();

                    EndPointData.get(EndPoint.IPAddress).LinkData.put(entry.getKey(), entry.getValue());
                }
                ANILinkRecieved(EndPoint, FrameID, ID);

                break;

            case STREAM:
                SetPreviousFrameType(EndPoint, ANSTMSG.STREAM);
                FrameID = ((STREAM)state.second).FRAME_ID;
                for (Map.Entry<LINK_ANCHORS, String> entry : ((STREAM)state.second).STREAM_DATA.entrySet())
                {
                    if(botDelegate != null)
                    {
                        botDelegate.BotStream(entry);
                    }
                }
                ANIStreamRecieved(EndPoint, FrameID, ID);

                break;

            case REQUEST_ACK:
                SetPreviousFrameType(EndPoint, ANSTMSG.REQUEST_ACK);
                FrameID = ((REQUEST_ACK)state.second).FRAME_ID;


                    if(botDelegate != null)
                    {
                        if(((REQUEST_ACK)state.second).jACK == ACK.OK)
                        botDelegate.BotError(BotConnectionInfo.OK);
                        else
                            botDelegate.BotError(BotConnectionInfo.ERROR);
                    }



                break;

            default:
                break;

        }
    }

    public class BeatsSorter implements Comparator<BeatsType>
    {
        public int compare(BeatsType left, BeatsType right) {
             if(left.StartSec < right.StartSec)
             {
                 return -1;
             }
             else if(left.StartSec > right.StartSec)
            {
                return 1;
            }
            else
                return  0;
        }
    }

    public void ANIStreamRecieved(IPEndPoint EndPoint, String Frame_ID, String _ID){
        STREAM_ACK txFrame = new STREAM_ACK(_ID, ACK.OK);
        txFrame.FRAME_ID = Frame_ID;
        if(EndPointData.keySet().contains(EndPoint.IPAddress))
            DOIPTesterContext.Instance.SendData(EndPoint, txFrame.Json().getBytes());
    }

    public void ANILinkRecieved(IPEndPoint EndPoint, String Frame_ID, String _ID){
        LINK_ACK txFrame = new LINK_ACK(_ID, ACK.OK);
        txFrame.FRAME_ID = Frame_ID;
        if(EndPointData.keySet().contains(EndPoint.IPAddress))
            DOIPTesterContext.Instance.SendData(EndPoint, txFrame.Json().getBytes());
    }

    public void ANIActionModeSet(IPEndPoint EndPoint, String Frame_ID, String _ID){
        CATEGORY_ACK txFrame = new CATEGORY_ACK(_ID, ACK.OK);
        txFrame.FRAME_ID = Frame_ID;
        if(EndPointData.keySet().contains(EndPoint.IPAddress))
        DOIPTesterContext.Instance.SendData(EndPoint, txFrame.Json().getBytes());
    }

    public void RequestUploadAck(IPEndPoint EndPoint, String Frame_ID, String _ID){
        REQEST_UPLOAD_ACK txFrame = new REQEST_UPLOAD_ACK(_ID, ACK.OK);
        txFrame.FRAME_ID = Frame_ID;
        if(EndPointData.keySet().contains(EndPoint.IPAddress))
        DOIPTesterContext.Instance.SendData(EndPoint, txFrame.Json().getBytes());
    }

    public void SendDataAck(IPEndPoint EndPoint, String Frame_ID, String _ID){
        DATA_ACK txFrame = new DATA_ACK(_ID, ACK.OK);
        txFrame.FRAME_ID = Frame_ID;
        if(EndPointData.keySet().contains(EndPoint.IPAddress))
        DOIPTesterContext.Instance.SendData(EndPoint, txFrame.Json().getBytes());
    }

    public void ExitUploadAck(IPEndPoint EndPoint, String Frame_ID, String _ID){
        UPLOAD_END_ACK txFrame = new UPLOAD_END_ACK(_ID, ACK.OK);
        txFrame.FRAME_ID = Frame_ID;
        if(EndPointData.keySet().contains(EndPoint.IPAddress))
        DOIPTesterContext.Instance.SendData(EndPoint, txFrame.Json().getBytes());
    }

    public void SendCommandAck(IPEndPoint EndPoint, String Frame_ID, String _ID){
        COMMAND_ACK txFrame = new COMMAND_ACK(_ID, ACK.OK);
        txFrame.FRAME_ID = Frame_ID;
        if(EndPointData.keySet().contains(EndPoint.IPAddress))
        DOIPTesterContext.Instance.SendData(EndPoint, txFrame.Json().getBytes());
    }


}
