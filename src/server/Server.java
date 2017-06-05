package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by M4teo on 16.04.2017.
 */
public class Server implements Runnable
{
    private List<ServerClient> clients = new ArrayList<ServerClient>();
    private List<Integer> clientsResponse = new ArrayList<Integer>();
    private int port;
    private DatagramSocket socket;
    private boolean running = false;
    public Thread run, manage, send, receive;
    private final int MAX_ATTEMPTS = 5;
    private boolean raw = false;

    public Server(int port)
    {
        this.port = port;
        try{
            socket = new DatagramSocket(port);
        }catch(SocketException e){
            e.printStackTrace();
        }

        run = new Thread(this, "Server");
        run.start();
    }

    public void run()
    {
        running = true;
        System.out.println("Server is running on port " + port);
        manageClients();
        receive();
        Scanner aScanner = new Scanner(System.in);
        while(running)
        {
            String text = aScanner.nextLine();
            if(!text.startsWith("/"))
            {
                sendToAll("/m/Server: " + text + "/e/");
                continue;
            }

            text = text.substring(1);
            if (text.equals("raw"))
            {
                if(raw)
                    System.out.println("Raw mode off.");

                else
                    System.out.println("Raw mode on.");

                raw = !raw;
            }

            else if(text.equals("clients"))
            {
                System.out.println("CONNECTED CLIENTS:");
                for (int i = 0; i < clients.size(); i++)
                {
                    ServerClient sc = clients.get(i);
                    System.out.println("Nick: " + sc.name + " ID: " + sc.getID() + " Address: " + sc.address + " Port: " + sc.port);
                }
            }

            else if(text.startsWith("kick"))
            {
                String name = text.split(" ")[1];
                int id = -1;
                boolean number = false;
                try{
                    id = Integer.parseInt(name);
                    number = true;
                }catch(NumberFormatException e){
                    number = false;
                }

                if(number)
                {
                    boolean exists = false;
                    for (int i = 0; i < clients.size(); i++)
                    {
                        if(clients.get(i).getID() == id)
                        {
                            exists = true;
                            break;
                        }
                    }

                    if (exists)
                        disconnect(id, true);

                    else
                        System.out.println("Client witd id: " + id + " doesn't exist!");
                }

                else
                {
                    for (int i = 0; i < clients.size(); i++)
                    {
                        ServerClient sc = clients.get(i);
                        if(name.equals(sc.name))
                        {
                            disconnect(sc.getID(), true);
                            break;
                        }
                    }
                }
            }

            else if (text.equals("help")){
                printHelp();
            }

            else if (text.equals("quit")){
                quit();
            }

            else {
                System.out.println("Unknown command.");
                printHelp();
            }
        }
    }

    private void printHelp()
    {
        System.out.println("Here is a list of all available commands:");
        System.out.println("=========================================");
        System.out.println("/raw - enables raw mode.");
        System.out.println("/clients - shows all connected clients.");
        System.out.println("/kick [users ID or username] - kicks a user.");
        System.out.println("/help - shows this help message.");
        System.out.println("/quit - shuts down the server.");
    }

    private void manageClients()
    {
        manage = new Thread("Manage")
        {
            public void run()
            {
                while(running)
                {
                    sendToAll("/i/server");
                    sendStatus();
                   try {
                        manage.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < clients.size(); i++)
                    {
                        ServerClient aServerClient = clients.get(i);

                        if(!clientsResponse.contains(clients.get(i).getID()))
                        {
                            if(aServerClient.attempt > MAX_ATTEMPTS)
                            {
                                disconnect(aServerClient.getID(), false);
                            }

                            else
                                aServerClient.attempt++;
                        }

                        else
                        {
                            clientsResponse.remove(new Integer(aServerClient.getID()));
                            aServerClient.attempt = 0;
                        }
                    }
                }
            }
        }; manage.start();
    }

    private void sendStatus()
    {
        if(clients.size() <= 0)
            return;

        String users = "/u/";
        for (int i = 0; i < clients.size() -1; i++){
            users += clients.get(i).name + "/n/";
        }

        users += clients.get(clients.size() - 1).name + "/e/";
        sendToAll(users);
    }

    private void receive()
    {
        receive = new Thread("Receive")
        {
            public void run()
            {
                while(running)
                {
                    byte[] data = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(data, data.length);
                    try {
                        socket.receive(packet);
                    } catch (SocketException e) {
                    }catch (IOException e) {
                        e.printStackTrace();
                    }

                    process(packet);
                }
            }
        };
        receive.start();
    }

    private void process(DatagramPacket packet)
    {
        String packetString = new String(packet.getData());//, 0, packet.getLength());

        if(raw)
            System.out.println(packetString);

        if(packetString.startsWith("/c/"))//polaczenie
        {
            int id = UniqueIdentifier.getIdentifier();
            System.out.println("NICK: " + packetString.split("/c/|/e/")[1] + " ID:" + id + " connected!");
            clients.add(new ServerClient(packetString.split("/c/|/e/")[1], packet.getAddress(), packet.getPort(), id));
            String ID = "/c/" + id;
            send(ID, packet.getAddress(), packet.getPort());
        }

        else if(packetString.startsWith("/m/"))//zwykla wiadomosc
        {
            sendToAll(packetString);
        }

        else if(packetString.startsWith("/d/"))//rozlaczenie
        {
            String id = packetString.split("/d/|/e/")[1];
            disconnect(Integer.parseInt(id), true);
        }

        else if(packetString.startsWith("/i/"))
        {
            clientsResponse.add(Integer.parseInt(packetString.split("/i/|/e/")[1]));
        }

        else
        {
            System.out.println(packetString);
        }
    }

    private void sendToAll(String message)
    {
        if(message.startsWith("/m/"))
        {
            String text = message.substring(3);
            text = text.split("/e/")[0];
            System.out.println(text);
        }

        if(raw)
            System.out.println(message);

        for (int i = 0; i < clients.size(); i++)
        {
            send(message, clients.get(i).address, clients.get(i).port);
        }
    }

    private void send(final byte[] data, final InetAddress address, final int port)
    {
        send = new Thread()
        {
            public void run()
            {
                DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
                try {
                    socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        send.start();
    }

    private void send(String message, InetAddress address, int port)
    {
        message += "/e/";
        send(message.getBytes(), address, port);
    }

    private void disconnect(int id, boolean status)
    {
        ServerClient aServerClient = null;
        boolean existed = false;
        for(int i = 0; i < clients.size(); i++)
        {
            if(clients.get(i).getID() == id)
            {
                aServerClient = clients.get(i);
                clients.remove(i);
                existed = true;
                break;
            }
        }

        String message = "";

        if(!existed)
            return;

        if(status)
        {
            message = "Client " + aServerClient.name + " (" + aServerClient.getID() + ") @ " + aServerClient.address.toString() + ": " +
                    aServerClient.port + " disconnected.";
        }

        else
            message = "Client " + aServerClient.name + " (" + aServerClient.getID() + ") @ " + aServerClient.address.toString() + ": " +
                    aServerClient.port + " timed out.";

        System.out.println(message);
    }

    private void quit()
    {
        for (int i = 0; i < clients.size(); i++){
            disconnect(clients.get(i).getID(), true);
        }

        running = false;
        socket.close();
    }

}
