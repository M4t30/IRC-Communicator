package GUIScaller;
import java.awt.*;
/**
 * Created by M4teo on 01.05.2017.
 */
public class GUIScaller
{
    static private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    static private double width = screenSize.getWidth();
    static private double height = screenSize.getHeight();
    static public int[] loginWindowSize = new int[2];
    static public int loginWindowFontSize;
    static public int[] labelNameBounds = new int[4];
    static public int[] textNameBounds = new int[4];
    static public int[] labelAddressBounds = new int[4];
    static public int[] textAddressBounds = new int[4];
    static public int[] labelPortBounds = new int[4];
    static public int[] textPortBounds = new int[4];
    static public int[] loginButtonBounds = new int[4];
    static public int[] clientWindowSize = new int[2];
    static public int clientWindowFontSize;
    static public int[] gblColumnWidths = new int [4];
    static public int[] gblRowHeights = new int [3];

    static public void setLoginWindowSize()
    {
        if (width == 3840 && height == 2160)
        {
            loginWindowSize[0] = 600;
            loginWindowSize[1] = 600;
            loginWindowFontSize = 50;
            labelNameBounds[0] = 220;
            labelNameBounds[1] = 15;
            labelNameBounds[2] = 200;
            labelNameBounds[3] = 55;
            textNameBounds[0] = 100;
            textNameBounds[1] = 80;
            textNameBounds[2] = 400;
            textNameBounds[3] = 50;
            labelAddressBounds[0] = 160;
            labelAddressBounds[1] = 140;
            labelAddressBounds[2] = 260;
            labelAddressBounds[3] = 55;
            textAddressBounds[0] = 100;
            textAddressBounds[1] = 205;
            textAddressBounds[2] = 400;
            textAddressBounds[3] = 50;
            labelPortBounds[0] = 240;
            labelPortBounds[1] = 265;
            labelPortBounds[2] = 260;
            labelPortBounds[3] = 55;
            textPortBounds[0] = 100;
            textPortBounds[1] = 330;
            textPortBounds[2] = 400;
            textPortBounds[3] = 50;
            loginButtonBounds[0] = 100;
            loginButtonBounds[1] = 450;
            loginButtonBounds[2] = 400;
            loginButtonBounds[3] = 50;
        }

        else
        {
            loginWindowSize[0] = 400;
            loginWindowSize[1] = 400;
            loginWindowFontSize = 30;
            labelNameBounds[0] = 150;
            labelNameBounds[1] = 24;
            labelNameBounds[2] = 150;
            labelNameBounds[3] = 36;
            textNameBounds[0] = 60;
            textNameBounds[1] = 60;
            textNameBounds[2] = 265;
            textNameBounds[3] = 36;
            labelAddressBounds[0] = 115;
            labelAddressBounds[1] = 100;
            labelAddressBounds[2] = 200;
            labelAddressBounds[3] = 36;
            textAddressBounds[0] = 60;
            textAddressBounds[1] = 140;
            textAddressBounds[2] = 265;
            textAddressBounds[3] = 36;
            labelPortBounds[0] = 160;
            labelPortBounds[1] = 180;
            labelPortBounds[2] = 300;
            labelPortBounds[3] = 36;
            textPortBounds[0] = 60;
            textPortBounds[1] = 220;
            textPortBounds[2] = 265;
            textPortBounds[3] = 36;
            loginButtonBounds[0] = 135;
            loginButtonBounds[1] = 280;
            loginButtonBounds[2] = 117;
            loginButtonBounds[3] = 29;
        }
    }

    public static void setClientWindowSize()
    {
        if (width == 3840 && height == 2160)
        {
            clientWindowSize[0] = 2048;
            clientWindowSize[1] = 1024;
            clientWindowFontSize = 30;
            gblColumnWidths[0] = 60;
            gblColumnWidths[1] = 1973;
            gblColumnWidths[2] = 10;
            gblColumnWidths[3] = 5;
            gblRowHeights[0] = 40;
            gblRowHeights[1] = 959;
            gblRowHeights[2] = 25;
        }

        else
        {
            clientWindowSize[0] = 880;
            clientWindowSize[1] = 550;
            clientWindowFontSize = 30;
            gblColumnWidths[0] = 28;
            gblColumnWidths[1] = 815;
            gblColumnWidths[2] = 30;
            gblColumnWidths[3] = 7;
            gblRowHeights[0] = 30;
            gblRowHeights[1] = 480;
            gblRowHeights[2] = 40;
        }
    }

}
