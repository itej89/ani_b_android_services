package Framework.DataTypes.Constants;

/**
 * Created by tej on 24/06/18.
 */

public enum ProximityStateSymbols
{
    MOUNTED{
        public String toString() {

            return "1";

        }
    },
    NotMounted{
        public String toString() {

        return "0";

       }
    };

    public static ProximityStateSymbols ConvertFromString(String s)
    {
        switch (s)
        {
            case "1":
                return  MOUNTED;
            case  "0":
                return  NotMounted;
        }
        return  null;
    }
}
