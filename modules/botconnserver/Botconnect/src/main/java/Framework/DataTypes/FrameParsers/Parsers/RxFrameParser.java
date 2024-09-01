package Framework.DataTypes.FrameParsers.Parsers;

import android.util.Pair;

import Framework.DataTypes.FrameParsers.RxFrames.REQUEST_ACK;
import Framework.DataTypes.FrameParsers.TxFrames.CATEGORY;
import Framework.DataTypes.FrameParsers.TxFrames.COMMAND;
import Framework.DataTypes.FrameParsers.TxFrames.DATA;
import Framework.DataTypes.FrameParsers.TxFrames.LINK;
import Framework.DataTypes.FrameParsers.TxFrames.REQEST_UPLOAD;
import Framework.DataTypes.FrameParsers.TxFrames.STREAM;
import Framework.DataTypes.FrameParsers.TxFrames.TxBaseFrame;
import Framework.DataTypes.FrameParsers.TxFrames.UPLOAD_END;
import Framework.DataTypes.Enums.ANSTMSG;

public class RxFrameParser {


    public Pair<ANSTMSG,TxBaseFrame> GetBaseFrame(String Json)
    {
        Pair<ANSTMSG,TxBaseFrame> baseFrame = new Pair<>(ANSTMSG.NA, new TxBaseFrame());
        baseFrame.second.ParseJson(Json);
        baseFrame = new Pair<>(baseFrame.second.jANSTMSG, baseFrame.second);
        return  baseFrame;
    }


    public Pair<ANSTMSG,TxBaseFrame> GetRxObject(String Json)
    {
        Pair<ANSTMSG,TxBaseFrame> baseFrame = new Pair<>(ANSTMSG.NA, new TxBaseFrame());
        baseFrame.second.ParseJson(Json);
        switch(baseFrame.second.jANSTMSG) {
            case CATEGORY:
                baseFrame =  new Pair<ANSTMSG,TxBaseFrame>(ANSTMSG.CATEGORY, new CATEGORY());
                baseFrame.second.ParseJson(Json);
                break;
            case DATA:
                baseFrame =  new Pair<ANSTMSG,TxBaseFrame>(ANSTMSG.DATA, new DATA());
                baseFrame.second.ParseJson(Json);
                break;
            case COMMAND:
                baseFrame =  new Pair<ANSTMSG,TxBaseFrame>(ANSTMSG.COMMAND, new COMMAND());
                baseFrame.second.ParseJson(Json);
                break;
            case REQEST_UPLOAD:
                baseFrame =  new Pair<ANSTMSG,TxBaseFrame>(ANSTMSG.REQEST_UPLOAD, new REQEST_UPLOAD());
                baseFrame.second.ParseJson(Json);
                break;
            case UPLOAD_END:
                baseFrame =  new Pair<ANSTMSG,TxBaseFrame>(ANSTMSG.UPLOAD_END, new UPLOAD_END());
                baseFrame.second.ParseJson(Json);
                break;
            case LINK:
                baseFrame =  new Pair<ANSTMSG,TxBaseFrame>(ANSTMSG.LINK, new LINK());
                baseFrame.second.ParseJson(Json);
                break;
            case STREAM:
                baseFrame =  new Pair<ANSTMSG,TxBaseFrame>(ANSTMSG.STREAM, new STREAM());
                baseFrame.second.ParseJson(Json);
                break;
            case REQUEST_ACK:
                baseFrame =  new Pair<ANSTMSG,TxBaseFrame>(ANSTMSG.REQUEST_ACK, new REQUEST_ACK());
                baseFrame.second.ParseJson(Json);
                break;
            case NA:
        break;
    }

        return baseFrame;

    }
}
