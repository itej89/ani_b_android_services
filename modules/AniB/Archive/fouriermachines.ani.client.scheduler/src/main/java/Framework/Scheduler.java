package Framework;

import java.util.ArrayList;
import java.util.UUID;

import Framework.DataTypes.Constants.JOB_PRIORITY;
import Framework.DataTypes.Constants.JOB_STATE;
import Framework.DataTypes.Delegates.JobConvey;
import Framework.DataTypes.Job;

public class Scheduler implements JobConvey {


    public static Scheduler SharedInstance = new Scheduler();


    //Delegate job convey
    public void notify_Paused(UUID ID) {

    }

    public void notify_LostResource(UUID ID) {

    }

    public void notify_NextStep(UUID ID) {
        Job job = FindJobByID( ID);
        if(job != null)
        {
            job.Resume();
        }
    }

    public void notify_Finish(UUID ID) {
        DeleteJob( ID);
    }
    //End delegate


    ArrayList<Job> JOBS = new ArrayList<Job>();

    ArrayList<Job>  JOB_STORE = new ArrayList<Job>();

    Job Idle_JOB = new Job();

    Job Current_JOB;



    private Scheduler()
    {
        JOBS.add(Idle_JOB);
        Current_JOB = Idle_JOB;
        Current_JOB.TakeOverResources(this);
        Current_JOB.Resume();
    }

    public Job FindJobByID(UUID UID)
    {
        if(Current_JOB != null && Current_JOB.ID == UID)
        {
            return Current_JOB;
        }

        for(int i=1; i<JOBS.size();i++)
        {
            if(JOBS.get(i).ID == UID)
            {
                return JOBS.get(i);
            }

        }

        for(int i=0;i<JOB_STORE.size();i++)
        {
            if(JOB_STORE.get(i).ID == UID)
            {
                return JOB_STORE.get(i);
            }

        }

        return null;
    }

    public void KillJobWithID(UUID UID)
    {
        Job job = FindJobByID(UID);
        if(job != null)
        {
            job.Pause();
            if(job != null && job.delegate != null)
            {
                job.delegate.notify_Finish(UID);
            }
        }

    }

    public void ScheduleNextJOB()
    {
        DoJob(FindHighestPriorityJob());
    }

    public void PauseCurrentJob()
    {
        if(Current_JOB != null && Current_JOB.ID != JOBS.get(0).ID)
        {
            Current_JOB.Pause();
        }
    }



    public Boolean AddJob(Job job)
    {
        if(job.PRIORITY == JOB_PRIORITY.ZERO){
            return false;
        }

        JOBS.add(job);
        if(Current_JOB == null)
        {
            DoJob(job);
        }
        else
        if(job.PRIORITY.getValue() > Current_JOB.PRIORITY.getValue())
        {
            Current_JOB.Pause();
            DoJob(job);
        }

        return true;

    }

    public Job FindHighestPriorityJob()
    {
        Job job = JOBS.get(0);
        for(int i=0; i<JOBS.size();i++)
        {
            if(JOBS.get(i).PRIORITY.getValue() > job.PRIORITY.getValue())
            {
                job = JOBS.get(i);
            }
        }
        return job;
    }

    public void DeleteJob(UUID id)
    {
        if(JOBS.size() > 1){
            for(int i=1; i<JOBS.size();i++)
            {
                if(JOBS.get(i).ID == id)
                {
                    //JOBS.get(i).destroy();
                    JOBS.remove(i);
                    if(Current_JOB != null && Current_JOB.ID == id)
                        Current_JOB = null;
                    System.gc();
                    break;
                }
            }
        }
    }

    public void DoJob(Job job)
    {
        if(Current_JOB != null && Current_JOB.ID == job.ID)
            return;

            Current_JOB = job;
            Current_JOB.TakeOverResources(this);
            Current_JOB.Resume();

    }


    //This method is added to empty jobs que during system pause and-
    //-moves all the jobs other than IDLE job to store buffer
    //This will stop scheduler from executing peding jobs after pause
    public void MoveAllJobsToStoreBuffer()
    {
        Integer TotalNumberOfPendingJobs = JOBS.size();
        if(TotalNumberOfPendingJobs > 1){
            //Move all pending jobs to store buffer exept for the IDLE job
            for(int i=1; i<TotalNumberOfPendingJobs; i++)
            {
                JOB_STORE.add(JOBS.get(i));
            }
            //Remove all pending jobs to from Jobs buffer
            while(JOBS.size() > 1)
            {
                JOBS.remove(1);
            }
        }

        if(Current_JOB != null)
        {
            Current_JOB.Pause();
        }

        Current_JOB = JOBS.get(0);

        //print("numberof jobs stored : ")
        //print(TotalNumberOfPendingJobs)
    }

    //This method is added to restore jobs que after system restore
    //This will resume any jobs paused during system pause
    public void RestorePendingJobsFromStoreBuffer()
    {
        Integer TotalNumberOfPendingJobs = JOB_STORE.size();
        if(TotalNumberOfPendingJobs > 0){
            //Move all stored pending jobs to JOBS que
            for(int i=0; i<TotalNumberOfPendingJobs;i++)
            {
                JOBS.add(JOB_STORE.get(i));
            }
            //Clear all store jobs
            while(JOB_STORE.size() > 0)
            {
                JOB_STORE.remove(0);
            }
        }

        DoJob(FindHighestPriorityJob());
    }
}
