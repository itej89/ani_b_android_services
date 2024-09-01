package Framework.SystemEventHandlers;

import Framework.DataTypes.Delegates.CommsStatusConvey;
import FrameworkInterface.PublicTypes.Constants.MachineCommsStates;
import FrameworkInterface.PublicTypes.Delegates.KineticsCommsConvey;
import FrameworkInterface.PublicTypes.Machine;

public class KineticCommsStateHandler implements KineticsCommsConvey
        {
public void commsStateChanged(MachineCommsStates State) {
        if(commsConvey != null)
        {
        commsConvey.commsStateChanged(State);

        }
        }

        public void newMachineFound(Machine Device)
        {

        }



        public static KineticCommsStateHandler Instance = new KineticCommsStateHandler();
        CommsStatusConvey commsConvey;



        private KineticCommsStateHandler()
        {

        }

        public void SetCommsConvey(CommsStatusConvey _delegate)
        {
            commsConvey = _delegate;
        }

        }
