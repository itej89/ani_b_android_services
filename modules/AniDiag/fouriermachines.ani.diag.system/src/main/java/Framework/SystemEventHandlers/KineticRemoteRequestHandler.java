package Framework.SystemEventHandlers;

import FrameworkInterface.PublicTypes.Constants.MachineRequests;
import FrameworkInterface.PublicTypes.Delegates.KineticsRemoteRequestConvey;

public class KineticRemoteRequestHandler implements KineticsRemoteRequestConvey {
        public void machineRequested(MachineRequests Request) {
                switch (Request) {
                        case POWER_BUTTON_PRESSED:
                                break;
                        case POWER_BUTTON_LONG_PRESS:
                                break;
                        case ATTENTION_BUTTON_PRESSED:
                                break;
                        case ATTENTION_BUTTON_LONG_PRESS:
                                break;
                        default:
                                break;
                }
        }

        public static KineticRemoteRequestHandler Instance = new KineticRemoteRequestHandler();

        private KineticRemoteRequestHandler() {

        }
}

