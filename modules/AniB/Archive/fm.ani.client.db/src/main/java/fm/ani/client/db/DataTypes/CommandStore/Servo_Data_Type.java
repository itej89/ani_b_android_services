package fm.ani.client.db.DataTypes.CommandStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tej on 02/02/18.
 */

public class Servo_Data_Type {

    public String Name;
    public  Integer Address;
    public  Integer Min_Angle;
    public Integer Max_Angle;
    public List<Servo_Calibration_Type> SERVO_CALIBRATED_DATA = new ArrayList<Servo_Calibration_Type>();

    public Servo_Data_Type(String name, Integer address, Integer min_Angle, Integer max_Angle)
    {
        Name = name;
        Address = address;
        Min_Angle = min_Angle;
        Max_Angle = max_Angle;
    }

}
