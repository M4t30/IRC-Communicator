package server;

import java.util.Scanner;

/**
 * Created by M4teo on 16.04.2017.
 */
public class ServerMain
{
    private int port;
    private Server server;

    public ServerMain(int port)
    {
        this.port = port;
        server = new Server(port);
    }

    public static void main(String[] args)
    {
        new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                for(;;)
                    try{
                        int port;

                        if(args.length != 1)
                        {
                            System.out.println("Usage: java -jar ChatServer.jar [port]");
                            return;
                        }

                        port = Integer.parseInt(args[0]);
                        new ServerMain(port);
                        wait();
                    }
                    catch(InterruptedException e){
                    e.printStackTrace();
                    }
            }
        }).run();
    }
}
