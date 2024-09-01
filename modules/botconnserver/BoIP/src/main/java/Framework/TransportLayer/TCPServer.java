package Framework.TransportLayer;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import Framework.DOIPLayer.DOIPSession;
import Framework.DataTypes.Extras.TCPTransportClientConvey;
import Framework.DataTypes.GlobalContext;
import Framework.DataTypes.IPEndPoint;
import Framework.DataTypes.Transports.Helpers.RecievedData;
import Framework.Validation.ValidationRuleMessages;

public class TCPServer {

    public ValidationRuleMessages ValidationErrors = new ValidationRuleMessages();
    public static TCPServer Instance = new TCPServer();
    public TCPTransportClientConvey TCPNotify;

    static final int socketServerPORT = 13400;
    ServerSocket serverSocket;
    ArrayList<Socket> Clients = new ArrayList<>();

    public TCPServer() { }

    AtomicBoolean RunServerLoop = new AtomicBoolean();
    public void StartServer()
    {
        RunServerLoop.set(true);
        DOIPSession.Instance.RemoteIPAddress.clear();
        Thread tcpServerListener = new Thread(new TCPServerListener());
        tcpServerListener.start();
    }

    public  void StopServer()
    {
        RunServerLoop.set(false);

        try
        {
            serverSocket.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        for(int i=0; i<Clients.size(); i++)
        {
            try
            {
                if (Clients.get(i) != null && Clients.get(i).isConnected())
                {
                    Clients.get(i).close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Clients.clear();
        DOIPSession.Instance.RemoteIPAddress.clear();
    }

    public void onDestroy() {
        StopServer();
    }

    private class TCPServerListener extends  Thread
    {

            @Override
            public void run () {

                try
                {

                ServerSocket serverSocket = new ServerSocket();
                // create ServerSocket using specified port
                serverSocket.setReuseAddress(true);
                serverSocket.bind(new InetSocketAddress(socketServerPORT));

                    while (RunServerLoop.get()) {
                    try {

                        Socket socket = serverSocket.accept();
                        socket.setSoTimeout(5 * 1000);
                        if (socket.getRemoteSocketAddress() == null) {
                            socket.close();
                        } else {

                            IPEndPoint _IPEndPoint = new IPEndPoint(socket.getInetAddress().getHostAddress(), socketServerPORT);
                            DOIPSession.Instance.RemoteIPAddress.add(_IPEndPoint);
                            Clients.add(socket);

                            for (int i = Clients.size() - 2; i >= 0; i--) {
                                String ip = Clients.get(i).getInetAddress().getHostAddress();
                                if (ip.equals(_IPEndPoint.IPAddress)) {
                                    {
                                        Clients.get(i).close();
                                        Clients.remove(i);
                                        i--;
                                    }
                                }
                            }
                            for (int i = DOIPSession.Instance.RemoteIPAddress.size() - 2; i >= 0; i--) {
                                if (DOIPSession.Instance.RemoteIPAddress.get(i).IPAddress.equals(_IPEndPoint.IPAddress)) {
                                    DOIPSession.Instance.RemoteIPAddress.remove(i);
                                    i--;
                                }
                            }

                            Thread socketServerThread = new Thread(new SocketServerThread(DOIPSession.Instance.RemoteIPAddress.size() - 1
                                    , Clients.size() - 1, TCPNotify));
                            socketServerThread.start();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private class SocketServerThread extends Thread {
        TCPTransportClientConvey delTransportClientConvey;

        Socket Client;
        IPEndPoint RmoteEndPoint;
        public SocketServerThread(int _IPEndPoint, int _Client, TCPTransportClientConvey TCPNotify)
        {
            delTransportClientConvey = TCPNotify;
            Client = Clients.get(_Client);
            RmoteEndPoint = DOIPSession.Instance.RemoteIPAddress.get(_IPEndPoint);
        }

        @Override
        public void run() {
            Islooping = false;
           final ArrayList<RecievedData> data = new ArrayList<RecievedData>();
                while (Client != null && Client.isConnected()) {
                    try {
                    // block the call until connection is created and return
                    // Socket object
                       // int count = Client.getInputStream().available();

                        int available = Client.getInputStream().available();
                        if(available > 0) {
                            byte[] buf = new byte[available];
                            int count = Client.getInputStream().read(buf);

                            Log.i("RX:", "recieved smtng : " + count);
                            if (count > 0) {
                                if (Client != null) {
                                    RecievedData recievedData = new RecievedData();
                                    recievedData.IPAddress = Client.getInetAddress().getHostAddress();
                                    recievedData.Port = socketServerPORT;
                                    recievedData.Data = Arrays.copyOfRange(buf, 0, count);
                                    if (delTransportClientConvey != null) {
                                        delTransportClientConvey.TCP_DataRecieved(recievedData);
                                    }
                                }
                            }
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                    try {
                        sleep(1);
                    }
                    catch (Exception e){}
                }

            DOIPSession.Instance.RemoteIPAddress.remove(RmoteEndPoint);
            Clients.remove(Client);
            if(TCPNotify != null)
            {
                TCPNotify.TCP_Disconnected(RmoteEndPoint);
            }

        }
    }


    boolean Islooping = false;

    IPEndPoint findCarnet(String IPAddress) {
        for(IPEndPoint endPoint : DOIPSession.Instance.RemoteIPAddress) {
            if(endPoint.IPAddress.equals(IPAddress)) {
                return endPoint;
            }
        }
        return null;
    }

    public void SendData(IPEndPoint _IPEndPoint, byte[] Data)
    {
       for(int i=Clients.size()-1; i>=0; i--) {
           try {
               String ip = Clients.get(i).getInetAddress().getHostAddress();
               if(ip.equals(_IPEndPoint.IPAddress)) {
                   if (Clients.get(i) != null && Clients.get(i).isConnected()) {

                       OutputStream outputStream;

                       try {
                           outputStream = Clients.get(i).getOutputStream();
                           outputStream.write(Data);
                           Log.i("TX:", "sent smtng");
                       } catch (IOException e) {
                           e.printStackTrace();
                       }
                   }
                   break;
               }
           }
           catch (Exception e){
             Log.e("TCP Client", e.getMessage());
           }
       }
    }

}
