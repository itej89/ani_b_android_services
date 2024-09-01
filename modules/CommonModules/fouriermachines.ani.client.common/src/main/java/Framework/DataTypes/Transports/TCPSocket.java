package Framework.DataTypes.Transports;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import Framework.DataTypes.Delegates.TCPSocketClientConvey;
import Framework.DataTypes.IPEndPoint;
import Framework.DataTypes.Transports.Helpers.RecievedData;

public class TCPSocket {
    public TCPSocketClientConvey TCPNotify;

    int socketServerPORT = 13400;
    ServerSocket serverSocket;


    ArrayList<Socket> Clients = new ArrayList<>();
    ArrayList<IPEndPoint> RemoteIPAddress = new ArrayList<>();

    public TCPSocket( TCPSocketClientConvey _TCPNotify) {

        TCPNotify = _TCPNotify;
    }


    AtomicBoolean RunServerLoop = new AtomicBoolean();

    public int GetPort()
    {
        return  socketServerPORT;
    }
    public void StartServer()
    {
        RunServerLoop.set(true);
        RemoteIPAddress.clear();
        Thread tcpServerListener = new Thread(new TCPServerListener());
        tcpServerListener.start();
    }

    public  void StopServer()
    {
        RunServerLoop.set(false);

        try
        {
            if(serverSocket != null)
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
        RemoteIPAddress.clear();
    }

    public void onDestroy() {
        StopServer();
    }

    private class TCPServerListener extends  Thread
    {
        @Override
        public void run() {
            try {
            // create ServerSocket using specified port
            ServerSocket serverSocket = new ServerSocket(0);
            socketServerPORT = serverSocket.getLocalPort();
            serverSocket.setReuseAddress(true);
            while (RunServerLoop.get()) {
                    Socket socket = serverSocket.accept();
                    socket.setSoTimeout(5*1000);

                    if (socket.getRemoteSocketAddress() == null) {
                        socket.close();
                    }
                    else {
                        IPEndPoint _IPEndPoint = new IPEndPoint(socket.getInetAddress().getHostAddress(), socketServerPORT);
                        RemoteIPAddress.add(_IPEndPoint);
                        Clients.add(socket);

                        for(int i=Clients.size()-2; i>=0; i--)
                        {
                            String ip = Clients.get(i).getInetAddress().getHostAddress();
                            if(ip.equals(_IPEndPoint.IPAddress)) {
                                {
                                    Clients.get(i).close();
                                    Clients.remove(i);
                                    i--;
                                }
                            }
                        }
                        for(int i=RemoteIPAddress.size()-2; i>=0; i--) {
                            if(RemoteIPAddress.get(i).IPAddress.equals(_IPEndPoint.IPAddress))
                            {
                                RemoteIPAddress.remove(i);
                                i--;
                            }
                        }
                        Thread socketServerThread = new Thread(new SocketServerThread(RemoteIPAddress.size()-1
                                , Clients.size()-1, TCPNotify));
                        socketServerThread.start();
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class SocketServerThread extends Thread {
        TCPSocketClientConvey delTransportClientConvey;

        Socket Client;
        IPEndPoint RmoteEndPoint;
        public SocketServerThread(int _IPEndPoint, int _Client, TCPSocketClientConvey TCPNotify)
        {
            delTransportClientConvey = TCPNotify;
            Client = Clients.get(_Client);
            RmoteEndPoint = RemoteIPAddress.get(_IPEndPoint);
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

            RemoteIPAddress.remove(RmoteEndPoint);
            Clients.remove(Client);
            if(TCPNotify != null)
            {
                TCPNotify.TCP_Disconnected(RmoteEndPoint);
            }

        }
    }


    boolean Islooping = false;

    IPEndPoint findCarnet(String IPAddress) {
        for(IPEndPoint endPoint : RemoteIPAddress) {
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