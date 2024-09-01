package fm.ani.client.db.DataTypes.Applet.Nurse.GeneralConsulation;

import java.util.Date;

public class HEARTBEAT {
        public int ID;
        public Date TIMESTAMP;
        public byte[] VALUE;

        public HEARTBEAT(int _ID, Date _TimeStamp, byte[] _Value)
        {
            ID = _ID;
            TIMESTAMP = _TimeStamp;
            VALUE = _Value;
        }

        public HEARTBEAT(Date _TimeStamp, byte[] _Value)
        {
            TIMESTAMP = _TimeStamp;
            VALUE = _Value;
        }
}