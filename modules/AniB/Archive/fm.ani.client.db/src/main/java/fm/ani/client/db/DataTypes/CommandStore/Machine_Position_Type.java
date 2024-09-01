package fm.ani.client.db.DataTypes.CommandStore;

/**
 * Created by tej on 21/05/18.
 */

public class Machine_Position_Type
{
    public String Name;
    public Integer TURN;
    public Integer LIFT;
    public Integer LEAN;
    public Integer TILT;

    public Machine_Position_Type(String name, Integer turn, Integer lift, Integer lean, Integer tilt)
    {
        Name = name;
        TURN = turn;
        LIFT = lift;
        LEAN = lean;
        TILT = tilt;
    }
    public Machine_Position_Type(String name)
    {
        Name = name;
    }
}
