package main;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class HelloWorldSwing
{
    private static void createAndShowGUI ( final GraphicsDevice device )
    {
        final GraphicsConfiguration conf = device.getDefaultConfiguration ();

        final JFrame frame = new JFrame ( "HelloWorldSwing", conf );
        frame.setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );

        final JLabel label = new JLabel ( "Hello World", SwingConstants.CENTER );
        frame.getContentPane ().add ( label );

        final Rectangle sb = conf.getBounds ();
        frame.setBounds ( sb.x + sb.width / 2 - 100, sb.y + sb.height / 2 - 100, 200, 200 );

        frame.setVisible ( true );
    }

    public static void main ( final String[] args )
    {
        SwingUtilities.invokeLater ( new Runnable ()
        {
            @Override
            public void run ()
            {
                final GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment ();
                final GraphicsDevice[] devices = env.getScreenDevices ();
                for ( final GraphicsDevice device : devices )
                {
                    if ( device.getType () == GraphicsDevice.TYPE_RASTER_SCREEN )
                    {
                        createAndShowGUI ( device );
                    }
                }
            }
        } );
    }
}