package Framework;

import Framework.DataTypes.Constants.CommandLabels;

/**
 * Created by tej on 24/06/18.
 */

public class MachineCommandHelper {
    CommandLabels ModuleConstants = new CommandLabels();

    public MachineCommandHelper()
    {

    }

    public  Integer ConvertAngleToPWMValue(Integer Angle)
    {
        return (((2400-544)/180)*(Angle))+544;
    }


    public  Integer GetResponseCountForCommand(String CommandType) {
        return ModuleConstants.CommandResponseCount.get(CommandLabels.CommandTypes.valueOf(CommandType));
    }

    public  Integer GetResponseCountForCommand(CommandLabels.CommandTypes CommandType) {
        return (ModuleConstants.CommandResponseCount.get(CommandType));
    }
}
