package Framework.DOIPLayer.DOIPStateMachines.Interface;

import android.util.Pair;

import java.util.ArrayList;

import Framework.DataTypes.DOIP_OBJECTS.DOIPRequestObject;
import Framework.DataTypes.DOIP_OBJECTS.DOIPResponseObject;
import Framework.Validation.ValidationRuleMessages;

public interface IDiagState {
    ValidationRuleMessages ValidationErrors = null;
    Pair<Integer, ArrayList<Byte>> HandleIncomingData(DOIPRequestObject objRequest);
}
