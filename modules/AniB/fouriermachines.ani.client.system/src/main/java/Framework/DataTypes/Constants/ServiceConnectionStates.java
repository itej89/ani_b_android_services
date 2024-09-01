package Framework.DataTypes.Constants;

public class ServiceConnectionStates {
    public enum STATES{
        NA,
        CONNECT_TO_ARTICULATION_SERVICE,
        WAIT_FOR_ARTICULATION_SERVICE,
        CONNECT_TO_BOT_CONN_SERVICE,
        WAIT_FOR_BOT_CONN_SERVICE,
        CONNECT_TO_KINETICS_SERVICE,
        WAIT_FOR_KINETICS_SERVICE,
        CONNECT_TO_AI_SERVICE,
        FINISH
    }
}

