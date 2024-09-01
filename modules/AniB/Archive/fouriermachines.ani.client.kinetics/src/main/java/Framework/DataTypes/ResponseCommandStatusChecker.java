package Framework.DataTypes;

import java.util.ArrayList;

import Framework.DataTypes.Constants.CommandLabels;
import FrameworkInterface.PublicTypes.Constants.MachineResponseConditions;

/**
 * Created by tej on 24/06/18.
 */

public class ResponseCommandStatusChecker {
    MachineResponseConditions Check(String request, String response)
    {
        ArrayList<MachineResponseConditions>  responseConditions = new ArrayList<MachineResponseConditions>();
        String[] responseCommandList = response.split(":");
        String[] requestCommandListNonTrimmed = request.split(":");
         ArrayList<String> requestCommandList = new ArrayList<String>();

        //remove starting '~' character from each command
        for(int index=0; index<requestCommandList.size(); index++)
        {
            requestCommandList.add(requestCommandListNonTrimmed[index].replaceAll("~$", ""));
        }


        if(requestCommandList.size() > 0)
        {
            CommandLabels commandConstatns = new CommandLabels();
            Integer TotalResponsesCounted = 0;
            for(String data : requestCommandList)
            {
                //split each request command
                String[] parts = data.split("#");
                if(parts.length > 0)
                {
                    //get the command type
                    CommandLabels.CommandTypes command =  CommandLabels.CommandTypes.valueOf(parts[0]);
                    if(command != null)
                    {
                        Integer responseCount = commandConstatns.CommandResponseCount.get(command);
                        TotalResponsesCounted = TotalResponsesCounted + responseCount;
                        Integer index = (TotalResponsesCounted - 1);
                        //split the acknowledgement command
                        String[] subparts = responseCommandList[index].split("#");
                        if(subparts.length > 0)
                        {

                            if(subparts[subparts.length - 1] == "E")
                            {
                                responseConditions.add(MachineResponseConditions.ERROR);
                            }
                            else
                            {
                                responseConditions.add(MachineResponseConditions.OK);
                            }
                        }
                        else
                        {
                            responseConditions.add(MachineResponseConditions.INVALID_RESPONSE);
                        }

                    }
                    else
                    {
                        responseConditions.add(MachineResponseConditions.INVALID_REQUEST_COMMAND);
                    }
                }
            }

            for( MachineResponseConditions condition : responseConditions)
            {
                if(condition != MachineResponseConditions.OK)
                {
                    return condition;
                }
            }
            return MachineResponseConditions.OK;

        }
        else
        {
            return MachineResponseConditions.REPSONSE_NOT_FOUND;
        }
    }
}
