package fouriermachines.anidiag.datatypes.Delegates;

import java.util.ArrayList;
import java.util.Map;

import FrameworkInterface.PublicTypes.Constants.Actuator;

public interface ReferanceAnglesSaveRetrieve {
    public void SaveReferanceAngles(Map<Actuator, String> Data);
    public Map<Actuator, String> RetiriveReferanceAngles();
}
