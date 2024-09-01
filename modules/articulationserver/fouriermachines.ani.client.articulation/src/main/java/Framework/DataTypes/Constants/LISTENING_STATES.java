package Framework.DataTypes.Constants;

public class LISTENING_STATES {
  public   enum STATES {
         LISTENING(0),
        NOTLISTENING(1);

        private final int value;

        private STATES(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
