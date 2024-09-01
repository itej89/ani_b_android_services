package Framework.DataTypes.Constants;

/**
 * Created by tej on 24/06/18.
 */

public enum ActuatorMotionSymbols
{
         MOVING {
             public String toString() {

                 return "1";

             }
         },
         NotMoving {
             public String toString() {

                return "0";

            }
        };

    public static ActuatorMotionSymbols ConvertFromString(String s)
    {
        switch (s)
        {
            case "1":
                return  MOVING;
            case  "0":
                return  NotMoving;
        }
        return  null;
    }
}
