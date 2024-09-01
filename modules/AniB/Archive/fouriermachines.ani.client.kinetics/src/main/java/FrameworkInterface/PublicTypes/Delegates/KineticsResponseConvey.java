package FrameworkInterface.PublicTypes.Delegates;

import java.util.ArrayList;
import java.util.UUID;

import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponse;

public interface KineticsResponseConvey
{
    public void  CommsLost();

    public void  MachiResponseTimeout(ArrayList<KineticsResponse> partialResponse, UUID _Acknowledgement);

    public void  MachineResponseRecieved(ArrayList<KineticsResponse> responeData, UUID _Acknowledgement);
}