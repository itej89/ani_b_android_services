package Framework.JOBS.Initialization.MachineBindJOB;

public class BIND_STATES {

    public enum STATES {
     NA,
     PING,
     VALIDATE_PING,

     SET_NO_ATTENTION,

     READ_TURN_ADR,
     SET_TURN_ADR,
     READ_LIFT_ADR,
     SET_LIFT_ADR,
     READ_LEAN_ADR,
     SET_LEAN_ADR,
     READ_TILT_ADR,
     SET_TILT_ADR,


     READ_LOCK_RIGHT_ADR,
     SET_LOCK_RIGHT_ADR,
     READ_LOCK_LEFT_ADR,
     SET_LOCK_LEFT_ADR,

     CLR_TURN_CALIB,
     CLR_LIFT_CALIB,
     CLR_LEAN_CALIB,
     CLR_TILT_CALIB,

     READ_TURN_REF,
     SET_TURN_REF,
     READ_LIFT_REF,
     SET_LIFT_REF,
     READ_LEAN_REF,
     SET_LEAN_REF,
     READ_TILT_REF,
     SET_TILT_REF,

     READ_TURN_DELTA_ANGLE,
     SET_TURN_DELTA_ANGLE,
     READ_LIFT_DELTA_ANGLE,
     SET_LIFT_DELTA_ANGLE,
     READ_LEAN_DELTA_ANGLE,
     SET_LEAN_DELTA_ANGLE,
     READ_TILT_DELTA_ANGLE,
     SET_TILT_DELTA_ANGLE,

     READ_CALIB_FINISH,


     READ_LOCK_RIGHT_REF,
     SET_LOCK_RIGHT_REF,
     READ_LOCK_LEFT_REF,
     SET_LOCK_LEFT_REF,

     READ_TURN_CALIB,
     SET_TURN_CALIB,

     READ_LIFT_CALIB,
     SET_LIFT_CALIB,

     READ_LEAN_CALIB,
     SET_LEAN_CALIB,

     READ_TILT_CALIB,
     SET_TILT_CALIB,


     DUMMY_READ_DEGREE_TURN,
     DUMMY_READ_DEGREE_LIFT,
     DUMMY_READ_DEGREE_LEAN,
     DUMMY_READ_DEGREE_TILT,

     IS_TURN_CONNECTED,
     IS_TURN_ATTACHED,
     READ_DEGREE_TURN,
     SET_DEGREE_TURN,
     TRIGGER_TURN,
     TRIGGER_TURN_CONFIRM,
     ATTACH_SERVO_TURN,
     CONNECT_TURN,


     IS_LIFT_CONNECTED,
     CONNECT_LIFT,
     IS_LIFT_ATTACHED,
     READ_DEGREE_LIFT,
     SET_DEGREE_LIFT,
     TRIGGER_LIFT,
     TRIGGER_LIFT_CONFIRM,
     ATTACH_SERVO_LIFT,

     IS_LEAN_CONNECTED,
     IS_LEAN_ATTACHED,
     READ_DEGREE_LEAN,
     SET_DEGREE_LEAN,
     TRIGGER_LEAN,
     TRIGGER_LEAN_CONFIRM,
     ATTACH_SERVO_LEAN,
     CONNECT_LEAN,

     IS_TILT_CONNECTED,
     IS_TILT_ATTACHED,
     READ_DEGREE_TILT,
     SET_DEGREE_TILT,
     TRIGEGER_TILT,
     TRIGEGER_TILT_CONFIRM,
     ATTACH_SERVO_TILT,
     CONNECT_TILT,



     CHECK_INITIAL_PROXIMITY,
     EVALUATE_INITIAL_PROXIMITY,

     SET_MOUNT_POSITION,
     UNLOCK_PORT,
     CHECK_PROXIMITY,
     WAIT_MOUNT,
     LOCK_PORT,
     DETACH_TURN,
     DETACH_LEAN,
     DETACH_LIFT,
     DETACH_TILT,
     SET_ACTIVATE_POSITION,
     LOAD_ANI,
     WAIT_MACHINE_BIND,
     MACHINE_BINDED,

     SLEEP,
     IDLE,
     STUDIO_SESSION,
     QA,
     APPLET
    }

}
