package FrameworkInterface.DataTypes.FrameParsers.Parsers;

import android.util.Pair;

import FrameworkInterface.DataTypes.FrameParsers.RxFrames.ALIVE;
import FrameworkInterface.DataTypes.FrameParsers.TxFrames.ALIVE_ACK;
import FrameworkInterface.DataTypes.FrameParsers.TxFrames.CATEGORY;
import FrameworkInterface.DataTypes.FrameParsers.TxFrames.COMMAND;
import FrameworkInterface.DataTypes.FrameParsers.TxFrames.CONNECT;
import FrameworkInterface.DataTypes.FrameParsers.TxFrames.DATA;
import FrameworkInterface.DataTypes.FrameParsers.TxFrames.DISCONNECT;
import FrameworkInterface.DataTypes.FrameParsers.TxFrames.DISCOVER;
import FrameworkInterface.DataTypes.FrameParsers.TxFrames.REQEST_UPLOAD;
import FrameworkInterface.DataTypes.FrameParsers.TxFrames.TxBaseFrame;
import FrameworkInterface.DataTypes.FrameParsers.TxFrames.UPLOAD_END;
import FrameworkInterface.Enums.ANSTMSG;

public class RxFrameParser {


    public Pair<ANSTMSG,TxBaseFrame> GetBaseFrame(String Json)
    {
        Pair<ANSTMSG,TxBaseFrame> baseFrame = new Pair<>(ANSTMSG.NA, new TxBaseFrame());
        baseFrame.second.ParseJson(Json);

        return  baseFrame;
    }


    public Pair<ANSTMSG,TxBaseFrame> GetRxObject(String Json)
    {
        Pair<ANSTMSG,TxBaseFrame> baseFrame = new Pair<>(ANSTMSG.NA, new TxBaseFrame());
        baseFrame.second.ParseJson(Json);
        switch(baseFrame.second.jANSTMSG) {
            case DISCOVER:
        baseFrame =  new Pair<ANSTMSG,TxBaseFrame>(ANSTMSG.DISCOVER, new DISCOVER());
        baseFrame.second.ParseJson(Json);
        break;
            case CONNECT:
                baseFrame =  new Pair<ANSTMSG,TxBaseFrame>(ANSTMSG.CONNECT, new CONNECT());
                baseFrame.second.ParseJson(Json);
                break;
            case DISCONNECT:
                baseFrame =  new Pair<ANSTMSG,TxBaseFrame>(ANSTMSG.DISCONNECT, new DISCONNECT());
                baseFrame.second.ParseJson(Json);
                break;
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
            case ALIVE_ACK:
                baseFrame =  new Pair<ANSTMSG,TxBaseFrame>(ANSTMSG.ALIVE_ACK, new ALIVE_ACK());
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
            case NA:
        break;
    }

        return baseFrame;

    }
}
