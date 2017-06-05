package Login; /**
 * Created by M4teo on 29.03.2017.
 */

import GUIScaller.GUIScaller;
import Client.*;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

public class Login extends JFrame
{
    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JTextField textName;
    private JTextField textAddress;
    private JTextField textPort;
    private JLabel labelName;
    private JLabel labelAddress;
    private JLabel labelPort;
    private JButton loginButton;

    public Login()
    {
        createWindow();
    }

    private void createWindow()
    {
        GUIScaller.setLoginWindowSize();
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //natywny widok okien, charakterystyczny dla systemue
        }

        catch(Exception e)
        {
            e.printStackTrace();
        }

        //rozmiar okna, tytul okna, jego skalowanie, pozycja
        setTitle("Login Mazur");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(GUIScaller.loginWindowSize[0],GUIScaller.loginWindowSize[1]);
        setLocationRelativeTo(null);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5,5,5,5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        //label do wypisywania name
        labelName = new JLabel("Name");
        labelName.setFont(new Font("Arial", Font.BOLD, GUIScaller.loginWindowFontSize));
        labelName.setBounds(GUIScaller.labelNameBounds[0], GUIScaller.labelNameBounds[1], GUIScaller.labelNameBounds[2], GUIScaller.labelNameBounds[3]);
        contentPane.add(labelName);

        //ramka na name
        textName = new JTextField();
        textName.setBounds(GUIScaller.textNameBounds[0],GUIScaller.textNameBounds[1],GUIScaller.textNameBounds[2],GUIScaller.textNameBounds[3]);
        contentPane.add(textName);
        textName.setColumns(10);

        //label do wypisywania address
        labelAddress = new JLabel("IP Address");
        labelAddress.setFont(new Font("Arial", Font.BOLD, GUIScaller.loginWindowFontSize));
        labelAddress.setBounds(GUIScaller.labelAddressBounds[0], GUIScaller.labelAddressBounds[1], GUIScaller.labelAddressBounds[2], GUIScaller.labelAddressBounds[3]);
        contentPane.add(labelAddress);

        //ramka na address
        textAddress = new JTextField();
        textAddress.setBounds(GUIScaller.textAddressBounds[0],GUIScaller.textAddressBounds[1],GUIScaller.textAddressBounds[2],GUIScaller.textAddressBounds[3]);
        contentPane.add(textAddress);
        textAddress.setColumns(10);

        //label do wypisywania port
        labelPort = new JLabel("Port");
        labelPort.setFont(new Font("Arial", Font.BOLD, GUIScaller.loginWindowFontSize));
        labelPort.setBounds(GUIScaller.labelPortBounds[0], GUIScaller.labelPortBounds[1], GUIScaller.labelPortBounds[2], GUIScaller.labelPortBounds[3]);
        contentPane.add(labelPort);

        //ramka na port
        textPort = new JTextField();
        textPort.setBounds(GUIScaller.textPortBounds[0],GUIScaller.textPortBounds[1],GUIScaller.textPortBounds[2],GUIScaller.textPortBounds[3]);
        contentPane.add(textPort);
        textPort.setColumns(10);

        //login button
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, GUIScaller.loginWindowFontSize - 10));
        loginButton.setBounds(GUIScaller.loginButtonBounds[0],GUIScaller.loginButtonBounds[1],GUIScaller.loginButtonBounds[2],GUIScaller.loginButtonBounds[3]);
        contentPane.add(loginButton);
        loginButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                login(textName.getText(), textAddress.getText(), Integer.parseInt(textPort.getText()));
            }
        });
    }

    //logowanie

    private void login(String name, String address, int port)
    {
        dispose();
        new ClientWindow(name, address, port);
    }

    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    Login frame = new Login();
                    frame.setVisible(true);
                }

                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }
}
