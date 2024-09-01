package Framework.TransportLayer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import Framework.DataTypes.Extras.UDPTransportClientConvey;
import Framework.DataTypes.Transports.Helpers.RecievedData;
import Framework.Validation.ValidationRuleMessages;

public class UDPListen {

    public ValidationRuleMessages ValidationErrors = new ValidationRuleMessages();

    public static UDPListen Instance = new UDPListen();

    public UDPTransportClientConvey UDPNotify;

    static final int UdpServerPORT = 13400;
    UdpServerThread udpServerThread;

    public void Start() {
        udpServerThread = new UdpServerThread(UdpServerPORT, UDPNotify);
        udpServerThread.start();
    }

    public void Stop() {
        if(udpServerThread != null){
            udpServerThread.setRunning(false);
            udpServerThread = null;
        }
    }

    public void Send(byte[] message, String address, int port) {
        if(udpServerThread != null){
            try {
                udpServerThread.sendData(message, InetAddress.getByName(address), port);
            }
            catch (Exception e){}
        }
    }

    private class UdpServerThread extends Thread{

        int serverPort;
        DatagramSocket socket;

        boolean running;
        UDPTransportClientConvey delTransportClientConvey;


        public UdpServerThread(int serverPort, UDPTransportClientConvey TCPNotify) {
            super();
            delTransportClientConvey = TCPNotify;
            this.serverPort = serverPort;
        }

        public void setRunning(boolean running){
            this.running = running;
        }

        public void sendData(byte[] SendData, InetAddress address, int port) {
            try {
                DatagramSocket socket = new DatagramSocket();
            if(socket != null) {
                DatagramPacket packet = new DatagramPacket(SendData, SendData.length, address, port);
                socket.send(packet);
                socket.close();
            }
            } catch (SocketException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {

            running = true;
            try {
                socket = new DatagramSocket(serverPort);

                while(running){
                    byte[] buf = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    if(delTransportClientConvey != null)
                    {
                        RecievedData recievedData = new RecievedData();
                        recievedData.IPAddress =  packet.getAddress().getHostAddress();
                        recievedData.Port =  packet.getPort();
                        recievedData.Data  = new byte[packet.getLength()];
                        System.arraycopy(packet.getData(), packet.getOffset(), recievedData.Data, 0, packet.getLength());
                        delTransportClientConvey.UDP_DataRecieved(recievedData);
                    }
                }

            } catch (SocketException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                if(socket != null){
                    socket.close();
                    setRunning(false);
                }
            }
        }
    }

}
