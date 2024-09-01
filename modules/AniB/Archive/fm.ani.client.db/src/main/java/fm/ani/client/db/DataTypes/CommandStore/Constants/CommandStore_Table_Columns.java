package fm.ani.client.db.DataTypes.CommandStore.Constants;

/**
 * Created by tej on 02/02/18.
 */

public class CommandStore_Table_Columns {

      public  enum  DBTables  {
         NO_DATA, //Used to indicate a table that is not present oin DB
         SERVO_DATA,
         SERVO_TURN,
         SERVO_LIFT,
         SERVO_LEAN,
         SERVO_TILT,
         CAPTURED_COMMANDS,
         EXPRESSIONS,
         CONTEXT,
          MACHINE_POSITIONS
    }

    public enum DBCONTEXT_KEYS
    {
         ACT_ID,
         BEAT_ID,
        EM_SYNTH_ID
    }

   public enum CONTEXT_COLUMNS
    {
         KEY(0),
         VALUE(1);

        private final int value;
        private CONTEXT_COLUMNS(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }


    public enum MACHINE_POSITIONS_COLUMNS
    {
         NAME(0),
         TURN(1),
         LIFT(2),
         LEAN(3),
         TILT(4);

        private final int value;
        private MACHINE_POSITIONS_COLUMNS(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }


    public   enum SERVO_DATA_COLUMNS
        {
             NAME(0),
             ADDRESS(1),
             MIN_ANGLE(2),
             MAX_ANGLE(3);

            private final int value;
            private SERVO_DATA_COLUMNS(int value) {
                this.value = value;
            }

            public int getValue() {
                return value;
            }
        }

    public    enum SERVO_CALIBRATION_COLUMNS
        {
             DEGREE(0),
             ADC(1);

            private final int value;
            private SERVO_CALIBRATION_COLUMNS(int value) {
                this.value = value;
            }

            public int getValue() {
                return value;
            }
        }

    public     enum CAPTURED_COMMAND_COLUMNS
        {
             NAME(0),
            COMMAND(1);


            private final int value;
            private CAPTURED_COMMAND_COLUMNS(int value) {
                this.value = value;
            }

            public int getValue() {
                return value;
            }
        }

    public enum CONTEXT
    {
        KEY(0),
        VALUE(1);

        private final int value;
        private CONTEXT(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
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

    public     enum ANIMATION_ACT_COLUMNS
        {
             NAME(0),
             SEQUENCE(1);


            private final int value;
            private ANIMATION_ACT_COLUMNS(int value) {
                this.value = value;
            }

            public int getValue() {
                return value;
            }
        }


    public     enum COLUMN_TYPES
        {
             TEXT,
             NUMBER,
             FLOAT


        }
    }


