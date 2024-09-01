package Framework.DataTypes.Parsers.RequestCommandParser;

import java.util.ArrayList;

import Framework.DataTypes.Constants.CommandLabels;

public class KineticsRequestPowerOff extends KineticsRequest
        {

public KineticsRequestPowerOff()
        {
        super(CommandLabels.CommandTypes.POFF);
        String Command  = super.formCommand(new String[]{});
        Request = super.addDelimiters( Command);
        }

public KineticsRequestPowerOff(String command)
        {
        super( CommandLabels.CommandTypes.POFF);

        super.removeDelimiters(command);

        }
        }
