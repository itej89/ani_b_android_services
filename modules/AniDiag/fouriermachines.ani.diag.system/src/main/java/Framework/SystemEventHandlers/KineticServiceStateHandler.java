package Framework.SystemEventHandlers;

import Framework.DataTypes.Delegates.ServiceConnectionConvey;
import FrameworkInterface.DataTypes.Delegates.KineticsServiceStatusConvey;
import FrameworkInterface.InterfaceImplementation.KineticComms;
import FrameworkInterface.PublicTypes.Constants.MachineRequests;
import FrameworkInterface.PublicTypes.Delegates.KineticsBindStatusConvey;
import FrameworkInterface.PublicTypes.Delegates.KineticsRemoteRequestConvey;

public class KineticServiceStateHandler implements KineticsServiceStatusConvey, KineticsBindStatusConvey {

        ServiceConnectionConvey notify_ServiceConnectionConvey;

        public void setNotify_ServiceConnectionConvey(ServiceConnectionConvey serviceConnectionConvey)
        {
                notify_ServiceConnectionConvey = serviceConnectionConvey;
        }

        //KineticsServiceStatusConvey
        public void ConnectedToService()
        {
        }

        public void ServiceDisconnected()
        {
        }
        //End of KineticsServiceStatusConvey

        //KineticsBindStatusConvey
        public void  KineticsBindStatusUpdated(String request)
        {
                notify_ServiceConnectionConvey.ServiceBindStatusConvey(request);
        }
        //End of KineticsBindStatusConvey

        public static KineticServiceStateHandler Instance = new KineticServiceStateHandler();

        private KineticServiceStateHandler() {
                KineticComms.Instance.setServiceStatusConvey(this);
                KineticComms.Instance.SetKineticsBindStateListener(this);
        }
}

