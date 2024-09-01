package fm.ani.client.db.DataTypes.Applet.Nurse.GeneralConsulation;

import java.util.Date;

public class PULSE {
        public int ID;
        public Date TIMESTAMP;
        public double VALUE;

        public PULSE(int _ID, Date _TimeStamp, double _Value)
        {
            ID = _ID;
            TIMESTAMP = _TimeStamp;
            VALUE = _Value;
        }

        public PULSE(Date _TimeStamp, double _Value)
        {
            TIMESTAMP = _TimeStamp;
            VALUE = _Value;
        }
}
