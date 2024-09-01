package Framework.DataTypes.Constants;


public enum JOB_PRIORITY
{
        ZERO(0),
        LOW(1),
        CASUAL(2),
        LIVE(3),
        CHOREOGRAM(4),
        QAIDLE(5),
        QAATTENTION(6),
        QARESPONSE(7),
        APPLET(8),
        CONNECT(9),
        PROCESS(10),
        DISCONNECT(11),
        USER_ATTENTION(12),
        MACHINE_RESPONCE(13),
        USER_CRITICAL(14),
        SYSTEM_CRITICAL(15),
        EMERGENCY(16);

        private final int value;
        private JOB_PRIORITY(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

}


