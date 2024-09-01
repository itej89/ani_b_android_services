package fm.ani.client.db.DataTypes.EmSynth;

public class EM_SYNTH {
    public int ID;
    public String Name;
    public String Path;

    public EM_SYNTH(String name, int id)
    {
        Name = name;
        ID = id;
        Path = "";
    }

    public EM_SYNTH(String name, int id, String path)
    {
        ID = id;
        Name = name;
        Path = path;
    }
}
