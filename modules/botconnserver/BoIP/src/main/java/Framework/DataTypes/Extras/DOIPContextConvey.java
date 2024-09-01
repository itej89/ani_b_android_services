package Framework.DataTypes.Extras;

import java.util.ArrayList;
import Framework.DataTypes.IPEndPoint;

public interface DOIPContextConvey {

    void UDSResponseRecieved(IPEndPoint ipEndPoint, byte[] response);
}
