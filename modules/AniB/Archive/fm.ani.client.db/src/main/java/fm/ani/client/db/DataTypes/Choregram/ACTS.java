package fm.ani.client.db.DataTypes.Choreogram;

public class ACTS
{
    public int ID;
    public String Name;
    public String Audio;

    public ACTS(String name, int id)
    {
        Name = name;
        ID = id;
        Audio = "";
    }

    public ACTS(String name, int id, String audio)
    {
        ID = id;
        Name = name;
        Audio = audio;
    }
}