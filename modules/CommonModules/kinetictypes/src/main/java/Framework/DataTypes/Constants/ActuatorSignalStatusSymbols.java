package Framework.DataTypes.Constants;

/**
 * Created by tej on 24/06/18.
 */


public enum ActuatorSignalStatusSymbols
{
         ATTACHED {
             public String toString() {

                 return "1";

             }
         },
         DETTACHED{
    public String toString() {

        return "0";

    }
};


    public static ActuatorSignalStatusSymbols ConvertFromString(String s)
    {
        switch (s)
        {
            case "1":
                return  ATTACHED;
            case  "0":
                return  DETTACHED;
        }
        return  null;
    }
}
