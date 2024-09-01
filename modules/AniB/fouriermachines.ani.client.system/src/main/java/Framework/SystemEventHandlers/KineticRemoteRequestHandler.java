package Framework.SystemEventHandlers;

import android.content.Intent;

import Framework.DataTypes.Delegates.PlayPauseRequest;
import Framework.DataTypes.Delegates.QASession.QASessionUserEvent;
import Framework.DataTypes.Delegates.ShutDownRequest;
import Framework.DataTypes.Delegates.StudioSession.StudioSessionUserEvent;
import Framework.DataTypes.GlobalContext;
import FrameworkInterface.InterfaceImplementation.KineticComms;
import FrameworkInterface.PublicTypes.Constants.MachineRequests;
import FrameworkInterface.PublicTypes.Delegates.KineticsRemoteRequestConvey;

public class KineticRemoteRequestHandler implements KineticsRemoteRequestConvey
        {
public void machineRequested(MachineRequests Request) {
        switch(Request) {
        case POWER_BUTTON_PRESSED: {
                //        if(PlayPauseConvey != null)
                //        {
                //                PlayPauseConvey.PlayPauseRequested();
                //        }
//                if (UIMAINModuleHandler.Instance.AppletUIHandler != null) {
//                        UIMAINModuleHandler.Instance.AppletUIHandler.ShutdownRequest();
//                } else if (UIMAINModuleHandler.Instance.AniUIHandler != null) {
//                        UIMAINModuleHandler.Instance.AniUIHandler.ShutdownRequest();
//                } else if (UIMAINModuleHandler.Instance.InitUIHandler != null) {
//                        UIMAINModuleHandler.Instance.InitUIHandler.ShutdownRequest();
//                }
                break;
        }
        case POWER_BUTTON_LONG_PRESS: {
                if (UIMAINModuleHandler.Instance.AppletUIHandler != null) {
                        UIMAINModuleHandler.Instance.AppletUIHandler.ShutdownRequest();
                } else if (UIMAINModuleHandler.Instance.AniUIHandler != null) {
                        UIMAINModuleHandler.Instance.AniUIHandler.ShutdownRequest();
                } else if (UIMAINModuleHandler.Instance.InitUIHandler != null) {
                        UIMAINModuleHandler.Instance.InitUIHandler.ShutdownRequest();
                }
//                try {
//                Process p = Runtime.getRuntime().exec(new String[]{"su", "-c", "/system/bin/sh -c \"reboot -p\""});
//               // Process p = Runtime.getRuntime().exec(new String[]{"/system/bin/sh -c \"chmod 666 /dev/ttyUSB0\""});
//            }
//            catch (Exception e) { }
//        if(ShutDownConvey != null)
//        {
//        ShutDownConvey.ShutDownRequested();
//        }
                break;
        }
        case ATTENTION_BUTTON_PRESSED:
        if(QASessionConvey != null)
        {
        QASessionConvey.QASessionRequest();
        }

        break;
        case ATTENTION_BUTTON_LONG_PRESS:
//        if(StudioSessionConvey != null)
//        {
//                StudioSessionConvey.StudioSessionRequest();
//        }
        break;

        default:
        break;
        }
        }

public static KineticRemoteRequestHandler Instance = new KineticRemoteRequestHandler();
private PlayPauseRequest PlayPauseConvey;
private ShutDownRequest ShutDownConvey;
private StudioSessionUserEvent StudioSessionConvey;
private QASessionUserEvent QASessionConvey;

private KineticRemoteRequestHandler()
        {
                KineticComms.Instance.SetMachineRemoteRequestListener( this);
        }

public void set_ShutDownConvey(ShutDownRequest delegate)
        {
        ShutDownConvey = delegate;
        }

public void set_PlayPauseConvey(PlayPauseRequest delegate)
        {
        PlayPauseConvey = delegate;
        }



public void set_QASessionConveyConvey(QASessionUserEvent delegate)
                {
                        QASessionConvey = delegate;
                }


                public void set_StudioSessionConvey(StudioSessionUserEvent delegate)
                {
                        StudioSessionConvey = delegate;
                }

        }

