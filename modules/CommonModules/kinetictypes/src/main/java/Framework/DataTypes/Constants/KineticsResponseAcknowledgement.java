package Framework.DataTypes.Constants;

/**
 * Created by tej on 24/06/18.
 */

public enum KineticsResponseAcknowledgement
{
    Error,

    OK;

    public static KineticsResponseAcknowledgement ConvertFromString(String s)
    {
        switch (s)
        {
            case "E":
                return  Error;
            case  "O":
                return  OK;
        }
        return  null;
    }
}