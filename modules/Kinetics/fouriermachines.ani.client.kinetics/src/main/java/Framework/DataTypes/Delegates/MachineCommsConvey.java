package Framework.DataTypes.Delegates;

import java.util.ArrayList;
import java.util.UUID;

import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequest;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponse;
import FrameworkInterface.PublicTypes.Constants.MachineCommsStates;
import FrameworkInterface.PublicTypes.Constants.MachineRequests;
import FrameworkInterface.PublicTypes.Machine;

/**
 * Created by tej on 24/06/18.
 */

public interface MachineCommsConvey {
    public void commsStateChanged(MachineCommsStates State);
    public void newMachineFound(Machine Device);

    public void RecievedRemoteCommand(MachineRequests event);
    public void RecievedResponseData(ArrayList<KineticsResponse> responeData, UUID _Acknowledgement);
    public void KineticsResponseDataTimeout(UUID uuid, ArrayList<KineticsResponse> artialResponse);

    public void RequestSent(KineticsRequest request);
    public void ParameterTriggerSuccuss();
}
