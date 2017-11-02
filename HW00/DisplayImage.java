import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

public class DisplayImage {
  private static final int WIDTH = 400;
  private static final int HEIGHT = 400;

  public static void main( String[] args) {
    JFrame frame = new ImageFrame( WIDTH, HEIGHT);
    frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    frame.setVisible( true );
  }
}

//##############################################################


class ImageFrame extends JFrame {
  private final JFileChooser chooser;
  private BufferedImage image = null;

  //================================================
  //constructor

  public ImageFrame ( int width, int height ) {

    //setup the frames attributes

    this.setTitle( "Dave's image viewer" );
    this.setSize( width, height );

    //add a menu to the frame
    addMenu();

    //setup the file chooser dialog

    chooser = new JFileChooser();
    chooser.setCurrentDirectory( new File( "." ) );
  }

  private void addMenu() {

    //setup the frame's menu bar

    // === File menu

    JMenu fileMenu = new JMenu( "File" );

    // --- Open

    JMenuItem openItem = new JMenuItem( "Open" );
    openItem.addActionListener( new ActionListener() {
      public void actionPerformed( ActionEvent event ) {
        open();
      }
    } ); //not sure about this one!

    fileMenu.add( openItem );

    // --- Exit

    JMenuItem exitItem = new JMenuItem( "Exit" );
    exitItem.addActionListener( new ActionListener()  {
      public void actionPerformed( ActionEvent event ) {
        System.exit(0);
      }
    } );

    fileMenu.add( exitItem );

    // === attach menu to a menu bar

    JMenuBar menuBar = new JMenuBar();
    menuBar.add( fileMenu );
    this.setJMenuBar( menuBar );

  }

    //========================================================
    //open() - choose a file, load, and display the image.

    private void open()
    {
      File file = getFile();
      if( file != null ) {
        displayFile( file );
      }
    }

    //-------------------------------------------------
    // Open a file selected by the user

    private File getFile()
    {
      File file = null;

      if ( chooser.showOpenDialog( this ) == JFileChooser.APPROVE_OPTION )
      {
        file = chooser.getSelectedFile();
      }

      return file;

    }

    //----------------------------------------------------------
    // Display specified file in the frame

    private void displayFile( File file )
    {
      try
      {
        displayBufferedImage( ImageIO.read( file ) );
      }
      catch ( IOException exception )
      {
        JOptionPane.showMessageDialog( this, exception );
      }
    }

    //--------------------------------------------------------------
    // Display BufferedImage

    public void displayBufferedImage( BufferedImage image )
    {

      //there are many ways to display a BufferedImage; for now we'll...

      this.setContentPane( new JScrollPane( new JLabel( new ImageIcon( image ) ) ) );

      this.validate();
  }
}
