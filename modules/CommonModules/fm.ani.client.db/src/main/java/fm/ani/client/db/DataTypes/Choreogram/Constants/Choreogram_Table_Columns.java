package fm.ani.client.db.DataTypes.Choreogram.Constants;

public class Choreogram_Table_Columns {

    public  enum  DBTables  {
        ACTS,
        BEATS,
        TRACK
    }

    public enum ACTS_COLUMNS
    {
        ACT_ID(0),
        ACT_NAME(1),
        ACT_AUDIO(2);

        private final int value;
        private ACTS_COLUMNS(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public  enum TRACK_COLUMNS {
        TRACK_ID(0),
        DATA(1);

        private final int value;
        private TRACK_COLUMNS(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public  enum BEATS_COLUMNS
    {
        BEAT_ID(0),
        ACT_ID(1),
        ACTION_DATA(2),
        JOY(3),
        SURPRISE(4),
        FEAR(5),
        SADNESS(6),
        ANGER(7),
        DISGUST(8),
        StartSec(9),
        EndSec(10);

        private final int value;
        private BEATS_COLUMNS(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
