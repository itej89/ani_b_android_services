package Framework.DataTypes.Constants;

/**
 * Created by tej on 24/06/18.
 */

public enum ActuatorPowerStatusSymbols
{
         ON{
             public String toString() {

                 return "1";

             }
         },
         OFF{
             public String toString() {

                 return "0";

             }
         };

    public static ActuatorPowerStatusSymbols ConvertFromString(String s)
    {
        switch (s)
        {
            case "1":
                return  ON;
            case  "0":
                return  OFF;
        }
        return  null;
    }

}
