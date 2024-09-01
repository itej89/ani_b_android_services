package Framework.DOIPLayer.DOIPStateMachines.Interface;

import java.util.ArrayList;

import Framework.DataTypes.IPEndPoint;


public interface IDiagStateForSendRecieve extends IDiagState {
    Integer _Init(byte[] arrDataToBeSent, IPEndPoint EndPoint);
}
