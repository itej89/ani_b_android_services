package fm.ani.client.db.DataTypes.Applet.Nurse.GeneralConsulation.Constants;

public class GeneralConsultation_Table_Columns {
    public  enum  DBTables  {
        PULSE,
        BPM,
        TEMPERATURE,
        HEARTBEAT,
        SPO2
    }

    public enum TEMPERATURE_COLUMNS
    {
        ID(0),
        TIMESTAMP(1),
        VALUE(2);

        private final int value;
        private TEMPERATURE_COLUMNS(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum PULSE_COLUMNS
    {
        ID(0),
        TIMESTAMP(1),
        VALUE(2);

        private final int value;
        private PULSE_COLUMNS(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum BPM_COLUMNS
    {
        ID(0),
        TIMESTAMP(1),
        VALUE(2);

        private final int value;
        private BPM_COLUMNS(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum HEARTBEAT_COLUMNS
    {
        ID(0),
        TIMESTAMP(1),
        VALUE(2);

        private final int value;
        private HEARTBEAT_COLUMNS(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum SPO2_COLUMNS
    {
        ID(0),
        TIMESTAMP(1),
        VALUE(2);

        private final int value;
        private SPO2_COLUMNS(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
