package fm.ani.client.db.DataTypes.EmSynth.Constants;

public class EmSynth_Table_Columns {
    public  enum  DBTables  {
        EXPRESSIONS,
        EM_SYNTH
    }

    public enum EM_SYNTH
    {
        ID(0),
        NAME(1),
        PATH(2);

        private final int value;
        private EM_SYNTH(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public     enum EXPRESSIONS_COLUMNS
    {
        ID(0),
        NAME(1),
        ACTION_DATA(2),
        JOY(3),
        SURPRISE(4),
        FEAR(5),
        SADNESS(6),
        ANGER(7),
        DISGUST(8),
        EM_SYNTH_ID(9),
        SOUND_ID(10);



        private final int value;
        private EXPRESSIONS_COLUMNS(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

}
