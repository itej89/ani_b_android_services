package Framework.DataTypes.Delegates;

import Framework.DataTypes.Constants.CommandLabels;
import FrameworkInterface.PublicTypes.Constants.MachineRequests;

import static java.io.File.separator;

/**
 * Created by tej on 24/06/18.
 */

public class RemoteCommandToMachineRequest {

  public   MachineRequests Convert(String data)

    {
        String[] parts = data.split("#");
        if(parts.length > 0)
        {
            CommandLabels.RemoteCommandTypes CommadType = CommandLabels.RemoteCommandTypes.valueOf(parts[0]);
            if(CommadType != null)
            {
                switch(CommadType)
                {
                    case PBTN:
                        if(parts.length == 3 && parts[2].equals("O"))
                        {
                            switch (parts[1]) {
                            case "0":
                                return MachineRequests.POWER_BUTTON_PRESSED;
                            case "1":
                                return MachineRequests.POWER_BUTTON_DOUBLE_PRESSED;
                            case "2":
                                return MachineRequests.POWER_BUTTON_LONG_PRESS;
                            default:
                                break;
                        }
                        }
                        break;

                    case VBTN:
                        if(parts.length == 3 && parts[2].equals("O"))
                        {
                            switch (parts[1]) {
                            case "0":
                                return MachineRequests.ATTENTION_BUTTON_PRESSED;
                            case "1":
                                return MachineRequests.ATTENTION_BUTTON_DOUBLE_PRESSED;
                            case "2":
                                return MachineRequests.ATTENTION_BUTTON_LONG_PRESS;
                            default:
                                break;
                        }

                        }
                        break;
                }
            }
        }
        return MachineRequests.UNKNOWN;
    }

}
