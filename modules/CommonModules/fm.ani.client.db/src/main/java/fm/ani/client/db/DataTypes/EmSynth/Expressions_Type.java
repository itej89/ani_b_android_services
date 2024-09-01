package fm.ani.client.db.DataTypes.EmSynth;

/**
 * Created by tej on 02/02/18.
 */

public class Expressions_Type {
    public  int ID;
    public  String Name;
    public  String Action_Data;
    public  Float JOY;
    public  Float SURPRISE;
    public Float FEAR;
    public Float SADNESS;
    public Float ANGER;
    public Float DISGUST;
    public int EM_SYNTH_ID;
    public String SOUND_ID;

    public Expressions_Type(String name, String action_Data, Float joy, Float surprise, Float fear, Float sadness, Float anger, Float disgust, int em_synth_id, String sound_id)
    {
        Name = name;
        Action_Data = action_Data;
        JOY = joy;
        SURPRISE = surprise;
        FEAR = fear;
        SADNESS = sadness;
        ANGER = anger;
        DISGUST = disgust;
        EM_SYNTH_ID = em_synth_id;
        SOUND_ID = sound_id;
    }

    public Expressions_Type(int id, String name, String action_Data, Float joy, Float surprise, Float fear, Float sadness, Float anger, Float disgust, int em_synth_id, String sound_id)
    {
        ID = id;
        Name = name;
        Action_Data = action_Data;
        JOY = joy;
        SURPRISE = surprise;
        FEAR = fear;
        SADNESS = sadness;
        ANGER = anger;
        DISGUST = disgust;
        EM_SYNTH_ID = em_synth_id;
        SOUND_ID = sound_id;
    }
}
