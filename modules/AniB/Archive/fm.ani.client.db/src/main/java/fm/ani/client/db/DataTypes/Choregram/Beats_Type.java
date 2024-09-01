package fm.ani.client.db.DataTypes.Choreogram;

public class Beats_Type {
    public int Beat_Id;
    public int Act_Id;
    public String Action_Data;
    public float JOY;
    public float SURPRISE;
    public float FEAR;
    public float SADNESS;
    public float ANGER;
    public float DISGUST;
    public int StartSec;
    public int EndSec;

    public Beats_Type(int act_Id, int beat_ID, String action_Data, float joy, float surprise, float fear, float sadness, float anger, float disgust, int startSec, int endSec)
    {
        Act_Id = act_Id;
        Beat_Id = beat_ID;
        Action_Data = action_Data;
        JOY = joy;
        SURPRISE = surprise;
        FEAR = fear;
        SADNESS = sadness;
        ANGER = anger;
        DISGUST = disgust;
        StartSec = startSec;
        EndSec = endSec;
    }
}
