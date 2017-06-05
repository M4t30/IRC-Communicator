package Client;

import GUIScaller.GUIScaller;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by M4teo on 18.04.2017.
 */
public class ClientWindow extends JFrame implements Runnable
{
    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JTextArea historyChatBox;
    private JTextField messageBox;
    private SimpleDateFormat aSimpleDateFormat;
    private DefaultCaret aDefaultCaret;
    private JMenuBar menuBar;
    private JMenu mnFile;
    private JMenuItem mntmOnlineUsers;
    private JMenuItem mntmExit;

    private Client aClient;
    private OnlineUsers users;
    private Thread listen, run;
    boolean running = false;

    public ClientWindow(String name, String address, int port)
    {
        aClient = new Client(name, address, port);
        aSimpleDateFormat = new SimpleDateFormat("kk:mm:ss");

        boolean isConnected = aClient.openConnection(address);

        if(isConnected == false)
        {
            System.err.println("Connection failed!");
            writeOnConsole("Console failed!");
        }
        createWindow();
        writeOnConsole("Attempting a connection to " + address + ": " + port + ", user: " + name);
        String connection = "/c/" + name + "/e/";//dodawanie do listy clientow serwera
        aClient.send(connection.getBytes());
        users = new OnlineUsers();
        running = true;
        run = new Thread(this, "Running Thread");
        run.start();
    }

    private void createWindow()
    {
        GUIScaller.setClientWindowSize();
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //natywny widok okien, charakterystyczny dla systemu
        }

        catch(Exception e)
        {
            e.printStackTrace();
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Chat Client Mateusz Mazur");
        setSize(GUIScaller.clientWindowSize[0],GUIScaller.clientWindowSize[1]);
        setVisible(true);
        setLocationRelativeTo(null);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5,5,5,5));
        setContentPane(contentPane);
        //setResizable(false);

        //menubar
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        mnFile = new JMenu("File");
        menuBar.add(mnFile);

        mntmOnlineUsers = new JMenuItem("Online Users");
        mntmOnlineUsers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                users.setVisible(true);
            }
        });

        mnFile.add(mntmOnlineUsers);



        //siatka
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[]{GUIScaller.gblColumnWidths[0], GUIScaller.gblColumnWidths[1], GUIScaller.gblColumnWidths[2], GUIScaller.gblColumnWidths[3]}; //SUM = 2048
        gbl_contentPane.rowHeights = new int[]{GUIScaller.gblRowHeights[0], GUIScaller.gblRowHeights[1], GUIScaller.gblRowHeights[2]}; //SUM = 1024
        contentPane.setLayout(gbl_contentPane);

        //pole tekstowe czatu
        historyChatBox = new JTextArea();

        //scroll
        JScrollPane scroll = new JScrollPane(historyChatBox);
        //karetka
        aDefaultCaret = (DefaultCaret)historyChatBox.getCaret();
        aDefaultCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        historyChatBox.setEditable(false);
        GridBagConstraints scrollConstraints = new  GridBagConstraints();
        scrollConstraints.insets = new Insets(5,5,5,5);
        scrollConstraints.fill = GridBagConstraints.BOTH;
        scrollConstraints.gridx = 0;
        scrollConstraints.gridy = 0;
        scrollConstraints.gridwidth = 3; //zajmuje 3 kolumny w grid, dlatego jest nad send
        scrollConstraints.gridheight = 2;
        scrollConstraints.weightx = 1;// jak ma siÄ™ rozszerzaÄ‡ wzglÄ™dem caĹ‚ego okna, tutaj 1:1 proporcjonalnie
        scrollConstraints.weighty = 1;
        contentPane.add(scroll, scrollConstraints);

        //pole tekstowu wpisywanego tekstu
        messageBox = new JTextField();
        GridBagConstraints gbc_txtMessage = new GridBagConstraints();
        gbc_txtMessage.insets = new Insets(5,5,5,5);
        gbc_txtMessage.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtMessage.gridx = 0;
        gbc_txtMessage.gridwidth = 2;
        gbc_txtMessage.gridy = 2;
        gbc_txtMessage.weightx = 1;
        gbc_txtMessage.weighty = 0;
        contentPane.add(messageBox, gbc_txtMessage);
        messageBox.setColumns(10);
        messageBox.requestFocusInWindow();
        //obsĹ‚uga wysyĹ‚ania za pomocÄ… entera
        messageBox.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e)//jeĹ›li wciĹ›niesz enter to wiadomosc zostanie wysĹ‚ana
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    sendMessage(messageBox.getText(), true);
            }
        });

        //button wysylania
        JButton btnSend = new JButton("Send");
        GridBagConstraints gbc_btnSend = new GridBagConstraints();
        gbc_btnSend.insets = new Insets(5, 5, 5, 5);
        gbc_btnSend.gridx = 2;
        gbc_btnSend.gridy = 2;
        gbc_btnSend.weightx = 0;
        gbc_btnSend.weighty = 0;
        contentPane.add(btnSend, gbc_btnSend);
        btnSend.addActionListener(new ActionListener() { //jeĹ›li klikniesz myszkÄ… na button send to wyĹ›le
            @Override
            public void actionPerformed(ActionEvent e)
            {
                sendMessage(messageBox.getText(), true);
                messageBox.requestFocusInWindow();
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                String disconnect = "/d/" + aClient.getID() + "/e/";
                sendMessage(disconnect, false);
                running = false;
                aClient.quit();
            }
        });
    }


    private void sendMessage(String message, boolean text)
    {
        if(message.equals(""))
            return;

        if(text)
        {
            message = aSimpleDateFormat.format(new Date()) + ": " + aClient.getName() + ": "  + message;
            message = "/m/" + message + "/e/";
            messageBox.setText("");
        }

        aClient.send(message.getBytes());
    }

    void writeOnConsole(String message)
    {
        historyChatBox.setFont(new Font("Arial", Font.BOLD, 30));
        historyChatBox.append(message + "\n\r");
        historyChatBox.setCaretPosition(historyChatBox.getDocument().getLength());
    }

    public void run()
    {
        listen();
    }

    public void listen()
    {
        listen = new Thread("Listen Thread")
        {
            public void run()
            {
                while (running)
                {
                    String message = aClient.receive();
                    if(message.startsWith("/c/"))
                    {//"/c/8121/e/
                        aClient.setID(Integer.parseInt(message.split("/c/|/e/")[1]));
                        writeOnConsole("Successfully connected to server! ID: " + aClient.getID());
                    }

                    else if(message.startsWith("/m/"))
                    {
                        String text = message.substring(3);
                        text = text.split("/e/")[0];
                        writeOnConsole(text);
                    }

                    else if(message.startsWith("/i/"))
                    {
                        String text = "/i/" + aClient.getID() + "/e/";
                        sendMessage(text, false);
                    }

                    else if(message.startsWith("/u/"))
                    {
                        String[] usersArray = message.split("/u/|/n/|/e/");
                        users.update(Arrays.copyOfRange(usersArray, 1, usersArray.length - 1));
                    }
                }
            }
        };
        listen.start();
    }
}
