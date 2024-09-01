package Framework;

import java.util.ArrayList;

import Framework.DataTypes.Delegates.EEPromReadConvey;
import Framework.DataTypes.Delegates.EEPromWriteConvey;
import Framework.DataTypes.Delegates.ISLReadConvey;
import Framework.DataTypes.Delegates.ISLWriteConvey;
import Framework.DataTypes.Delegates.ServiceConnectionConvey;
import Framework.DataTypes.Delegates.ServoEEPromReadConvey;
import Framework.DataTypes.Delegates.UI.DiagRequestListener;

import Framework.DataTypes.Delegates.UI.UIMAINConvey;
import Framework.JOBS.AttachActuatorJob;
import Framework.JOBS.DettachActuatorJob;
import Framework.JOBS.EEPROMReadJOB;
import Framework.JOBS.EEPROMWriteJOB;
import Framework.JOBS.ISLEEPROMReadJOB;
import Framework.JOBS.ISLEEPROMWriteJOB;
import Framework.JOBS.ISLRAMReadJOB;
import Framework.JOBS.ISLRAMWriteJOB;
import Framework.JOBS.Initialization.ServiceConnectionJob;
import Framework.JOBS.MoveActuatorJob;
import Framework.JOBS.ServoEEPROMReadJOB;
import Framework.SystemEventHandlers.KineticServiceStateHandler;
import Framework.SystemEventHandlers.UIMAINModuleHandler;
import FrameworkInterface.DataTypes.Delegates.KineticsServiceStatusConvey;
import FrameworkInterface.InterfaceImplementation.KineticComms;
import FrameworkInterface.PublicTypes.Constants.Actuator;
import FrameworkInterface.PublicTypes.Delegates.KineticsBindStatusConvey;
import FrameworkInterface.PublicTypes.EEPROMDetails;

public class SystemFlowManager implements  ServiceConnectionConvey, UIMAINConvey,  DiagRequestListener, EEPromReadConvey, EEPromWriteConvey, ServoEEPromReadConvey, ISLReadConvey, ISLWriteConvey {

        ServiceConnectionJob serviceConnectionJob;
        EEPROMReadJOB ReadEEPROMJob;
        EEPROMWriteJOB WriteEEPROMJob;
        ServoEEPROMReadJOB  ReadServoEEPROMJOB;
        AttachActuatorJob attachActuatorJob;
        DettachActuatorJob dettachActuatorJob;
        MoveActuatorJob moveActuatorJob;

        ISLEEPROMReadJOB ReadISLEEPROMJob;
        ISLEEPROMWriteJOB WriteISLEEPROMJob;
        ISLRAMReadJOB ReadISLRAMJob;
        ISLRAMWriteJOB WriteISLRAMJob;


        void PauseCurrentJOB() {
                Scheduler.SharedInstance.PauseCurrentJob();
        }

        void EndCurrentJob() {
                PauseCurrentJOB();
        }

        //UIMAINConvey
        public void AppStarted()
        {
                if(serviceConnectionJob != null) {
                        Scheduler.SharedInstance.notify_Finish(serviceConnectionJob.ID);
                }
                serviceConnectionJob = new ServiceConnectionJob(this);

                Scheduler.SharedInstance.AddJob(serviceConnectionJob);
        }
        //End of UIMAINConvey



        public void CloseServiceConnections()
        {
                KineticComms.Instance.releaseServiceConnection();
        }


        //DiagRequestListener
        public  void ReadEEPROMData(EEPROMDetails ReadDetails)
        {
                if(ReadEEPROMJob != null) {
                        Scheduler.SharedInstance.notify_Finish(ReadEEPROMJob.ID);
                }
                ReadEEPROMJob = new EEPROMReadJOB(ReadDetails, (EEPromReadConvey) this);
                Scheduler.SharedInstance.AddJob(ReadEEPROMJob);
        }
        //End of DiagRequestListener

        //EEPromReadConvey
        public  void  EEPROMReadRecieved(EEPROMDetails Details, ArrayList<Integer> Data)
        {
                UIMAINModuleHandler.Instance.DiagDataHandler.RecievedEEPROMBytes(Details, Data);
        }
        //End of EEPromReadConvey


        //DiagRequestListener
        public  void WriteEEPROMData(EEPROMDetails ReadDetails, ArrayList<Integer> Data)
        {
                if(WriteEEPROMJob != null) {
                        Scheduler.SharedInstance.notify_Finish(WriteEEPROMJob.ID);
                }
                WriteEEPROMJob = new EEPROMWriteJOB(ReadDetails, (EEPromWriteConvey) this, Data);
                Scheduler.SharedInstance.AddJob(WriteEEPROMJob);
        }
        //End of DiagRequestListener

        //EEPromWriteConvey
        public  void  EEPROMWriteStatus(Boolean Status)
        {
                UIMAINModuleHandler.Instance.DiagDataHandler.EEPROMWriteStatus(Status);
        }
        //End of EEPromWriteConvey


        //DiagRequestListener
        public  void ReadServoEEPROMData(Actuator actuator, EEPROMDetails ReadDetails)
        {
                if(ReadServoEEPROMJOB != null) {
                        Scheduler.SharedInstance.notify_Finish(ReadServoEEPROMJOB.ID);
                }
                ReadServoEEPROMJOB = new ServoEEPROMReadJOB(actuator, ReadDetails, (ServoEEPromReadConvey) this);
                Scheduler.SharedInstance.AddJob(ReadServoEEPROMJOB);
        }
        //End of DiagRequestListener

        //ServoEEPromReadConvey
        public  void  ServoEEPROMReadRecieved(Actuator actuator, EEPROMDetails Details, ArrayList<Integer> Data)
        {
                UIMAINModuleHandler.Instance.DiagDataHandler.ServoEEPROMBytes(actuator, Details, Data);
        }
        //End of ServoEEPromReadConvey


        //DiagRequestListener
        public  void RemoveServoCalibrationData(Actuator actuator)
        {
                KineticComms.Instance.DeleteCalibrationDataForActuator(actuator);
        }
        public  void SaveServoCalibrationData(Actuator actuator, Integer DEGREE, Integer ADC)
        {
                KineticComms.Instance.SaveActuatorCalibrationData(actuator, DEGREE, ADC);
        }
        public  void AttachActuator(Actuator actuator)
        {
                if(attachActuatorJob != null) {
                        Scheduler.SharedInstance.notify_Finish(attachActuatorJob.ID);
                }
                attachActuatorJob = new AttachActuatorJob(actuator);
                Scheduler.SharedInstance.AddJob(attachActuatorJob);
        }
        public  void DettachActuator(Actuator actuator)
        {
                if(dettachActuatorJob != null) {
                        Scheduler.SharedInstance.notify_Finish(dettachActuatorJob.ID);
                }
                dettachActuatorJob = new DettachActuatorJob(actuator);
                Scheduler.SharedInstance.AddJob(dettachActuatorJob);
        }
        public  void MoveActuator(Actuator actuator, Integer Degree)
        {
                if(moveActuatorJob != null) {
                        Scheduler.SharedInstance.notify_Finish(moveActuatorJob.ID);
                }
                moveActuatorJob = new MoveActuatorJob(actuator, Degree);
                Scheduler.SharedInstance.AddJob(moveActuatorJob);
        }
        //End of DiagRequestListener

        public static SystemFlowManager Instance = new SystemFlowManager();

        private SystemFlowManager()
        {
                // DB_Local_Store Db = new DB_Local_Store();
                // Db.RemoveDB();
                //  Db.PlaceDB();
                KineticServiceStateHandler.Instance.setNotify_ServiceConnectionConvey(this);
        }



        //ISLReadConvey
        public  void  ISLReadRecieved(EEPROMDetails Details, ArrayList<Integer> Data)
        {
                UIMAINModuleHandler.Instance.DiagDataHandler.RecievedISLBytes(Details, Data);
        }
        //End of ISLReadConvey

        //ISLWriteConvey
        public  void  ISLWriteStatus(Boolean Status)
        {
                UIMAINModuleHandler.Instance.DiagDataHandler.ISLWriteStatus(Status);
        }
        //End of ISLWriteConvey



        //DiagRequestListener
        public  void ReadISL94203EEPROMData(EEPROMDetails ReadDetails)
        {
                if(ReadISLEEPROMJob != null) {
                        Scheduler.SharedInstance.notify_Finish(ReadISLEEPROMJob.ID);
                }
                ReadISLEEPROMJob = new ISLEEPROMReadJOB(ReadDetails, (ISLReadConvey) this);
                Scheduler.SharedInstance.AddJob(ReadISLEEPROMJob);
        }
        public  void ReadISL94203RAMData(EEPROMDetails ReadDetails)
        {
                if(ReadISLRAMJob != null) {
                        Scheduler.SharedInstance.notify_Finish(ReadISLRAMJob.ID);
                }
                ReadISLRAMJob = new ISLRAMReadJOB(ReadDetails, (ISLReadConvey) this);
                Scheduler.SharedInstance.AddJob(ReadISLRAMJob);
        }
        public  void WriteISL94203EEPROMData(Integer Address, ArrayList<Integer> Data)
        {
                EEPROMDetails ReadDetails = new EEPROMDetails(Address, Data.size());
                if(WriteISLEEPROMJob != null) {
                        Scheduler.SharedInstance.notify_Finish(WriteISLEEPROMJob.ID);
                }
                WriteISLEEPROMJob = new ISLEEPROMWriteJOB(ReadDetails, (ISLWriteConvey) this, Data);
                Scheduler.SharedInstance.AddJob(WriteISLEEPROMJob);
        }
        public  void WriteISL94203RAMMData(Integer Address, ArrayList<Integer> Data)
        {
                EEPROMDetails ReadDetails = new EEPROMDetails(Address, Data.size());
                if(WriteISLRAMJob != null) {
                        Scheduler.SharedInstance.notify_Finish(WriteISLRAMJob.ID);
                }
                WriteISLRAMJob = new ISLRAMWriteJOB(ReadDetails, (ISLWriteConvey) this, Data);
                Scheduler.SharedInstance.AddJob(WriteISLRAMJob);
        }
        //End of DiagRequestListener

        //ServiceConnectionConvey
        public  void  ServiceConnectionJobStatus(Boolean Status)
        {

        }
        public  void  ServiceBindStatusConvey(String Status)
        {
                if(Status.equals("WAIT_MACHINE_BIND"))
                        UIMAINModuleHandler.Instance.InitUIHandler.MachineLoadUI();
        }
        //End of ServiceConnectionConvey
}