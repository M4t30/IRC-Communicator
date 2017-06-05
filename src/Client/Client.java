package Client; /**
 * Created by M4teo on 03.04.2017.
 */

import java.io.IOException;
import java.net.*;

public class Client
{
    private String name;
    private String address;
    private int port;
    private DatagramSocket socket;
    private InetAddress ip;
    private Thread send;
    private int ID = -1;

    public Client(String name, String address, int port)
    {
        this.name = name;
        this.address = address;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public boolean openConnection(String address)
    {
       try
       {
           socket = new DatagramSocket();//it binds to any available port
           ip = InetAddress.getByName(address);
       }

       catch(UnknownHostException e)
        {
            e.printStackTrace();
            return false;
        }

        catch(SocketException e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public String receive()
    {
        byte[] data = new byte[1024];
        DatagramPacket packet = new DatagramPacket(data, data.length);

        try
        {
            socket.receive(packet);
        }

        catch(IOException e)
        {
            e.printStackTrace();
        }

        String message = new String(packet.getData());

        return message;
    }

    public void send(final byte[] data)
    {
        send = new Thread("Send")
        {
            public void run()
            {
                DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);

                try
                {
                    socket.send(packet);//pakiet w nagłowku ma ip celu, port
                }

                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        };
        send.start();
    }

    public void quit()
    {
        new Thread(){
            public void run(){
                synchronized (socket) {
                    socket.close();
                }
            }
        }.start();
    }
}
