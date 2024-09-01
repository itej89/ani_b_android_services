package fm.ani.client.db.DataTypes.Applet.Nurse.GeneralConsulation;

import java.util.Date;

public class SPO2 {
        public int ID;
        public Date TIMESTAMP;
        public double VALUE;

        public SPO2(int _ID, Date _TimeStamp, double _Value)
        {
            ID = _ID;
            TIMESTAMP = _TimeStamp;
            VALUE = _Value;
        }

        public SPO2(Date _TimeStamp, double _Value)
        {
            TIMESTAMP = _TimeStamp;
            VALUE = _Value;
        }
}
