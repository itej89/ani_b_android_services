package fm.ani.client.db.DataTypes.Applet.Nurse.GeneralConsulation;

import java.util.Date;

public class BPM {
        public int ID;
        public Date TIMESTAMP;
        public double VALUE;

        public BPM(int _ID, Date _TimeStamp, double _Value)
        {
            ID = _ID;
            TIMESTAMP = _TimeStamp;
            VALUE = _Value;
        }

        public BPM(Date _TimeStamp, double _Value)
        {
            TIMESTAMP = _TimeStamp;
            VALUE = _Value;
        }
}
