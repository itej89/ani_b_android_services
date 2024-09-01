package Framework.DataTypes;

import java.util.ArrayList;
import java.util.UUID;

import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequest;

public class SerialCommandTxRunnable implements Runnable {
    public ArrayList<KineticsRequest> Command;
    public UUID Acknowledgement;
    public SerialCommandTxRunnable(ArrayList<KineticsRequest> _data, UUID Ack) {
        Command = _data;
        Acknowledgement = Ack;
    }

    @Override
    public void run() {

    }
}


