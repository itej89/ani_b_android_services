package fm.ani.client.db.DataTypes.CommandStore;

/**
 * Created by tej on 02/02/18.
 */

public class Servo_Calibration_Type {
    public String Name = "";
    public Integer Degree = 0;
    public Integer ADC = 0;

    public  Servo_Calibration_Type(String name, Integer degree,  Integer adc)
    {
        Name = name;
        Degree = degree;
        ADC = adc;
    }

    public  Servo_Calibration_Type(Integer degree, Integer adc)
    {
        Degree = degree;
        ADC = adc;
    }

    public Servo_Calibration_Type()
    {

    }
}
