package Framework.DataTypes.Parsers.ResponseCommandParser;

import java.util.ArrayList;

import Framework.DataTypes.Constants.CommandLabels;
import Framework.DataTypes.Constants.KineticsResponseAcknowledgement;

public class KineticsResponsePowerOff extends KineticsResponse
        {

public KineticsResponsePowerOff(String response) {
        super(response);
        if(ResponseType == CommandLabels.CommandTypes.POFF)
        {
            ArrayList<String> RequestAckParts = DecomposedResponse.get(0);


        if(RequestAckParts.size() == 2)
        {
        RequestRecievedAck =  KineticsResponseAcknowledgement.ConvertFromString(RequestAckParts.get(1));
        }
        }
        }

        }

