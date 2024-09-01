package Framework.DataTypes.Constants;

public class ActuatorAttachStates {
    public enum STATES{
        NA,
        PING,
        VALIDATE_PING,
        IS_Actuator_CONNECTED,
        CONNECT_Actuator,
        IS_Actuator_ATTACHED,
        DUMMY_READ_DEGREE_Actuator,
        READ_DEGREE_Actuator,
        SET_DEGREE_Actuator,
        TRIGEGER_ANGLES,
        TRIGEGER_ANGLES_CONFIRM,
        ATTACH_Actuator,
        Finish
    }
}
