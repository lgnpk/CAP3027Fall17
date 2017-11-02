import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

public class RandomWalkPlant {
  private static final int WIDTH = 800;
  private static final int HEIGHT = 800;



  public static void main( String[] args) {
    JFrame frame = new ImageFrame( WIDTH, HEIGHT);
    frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    frame.setVisible( true );

  }
}

//##############################################################


class ImageFrame extends JFrame {
  int width = 401;
  int height = 401;

  private final JFileChooser chooser;


  //================================================
  //constructor

  public ImageFrame ( int width, int height ) {

    //setup the frames attributes

    this.setTitle( "CAP 3027 2017 - HW05 - Logan Peck" );
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

    JMenuItem randomWalkPlant = new JMenuItem( "Directed random walk plant" );
    randomWalkPlant.addActionListener( new ActionListener() {
      public void actionPerformed( ActionEvent event ) {
        plantRandom();
      }
    } );

    fileMenu.add( randomWalkPlant );


    JMenuItem exitItem = new JMenuItem( "Exit" );
    exitItem.addActionListener( new ActionListener()  {
      public void actionPerformed( ActionEvent event ) {
        System.exit(0);
      }
    } );

    fileMenu.add( exitItem );

    JMenuBar menuBar = new JMenuBar();
    menuBar.add( fileMenu );
    this.setJMenuBar( menuBar );

  }


    public int imageSize()
    {
      int size = 0;
      boolean isInt = false;

      while (isInt == false){

        String result = JOptionPane.showInputDialog( "Image Size (width == height): " );

        try {
          size = Integer.parseInt(result);
          isInt = true;
        } catch (NumberFormatException e) {
          JOptionPane.showMessageDialog( this, e );
        }
      }

      return size;

    }

    public int stemNum()
    {
      int numOfStems = 0;
      boolean isInt = false;

      while (isInt == false) {

        String resultStem = JOptionPane.showInputDialog( "Number of stems: " );

        try{
          numOfStems = Integer.parseInt(resultStem);
          isInt = true;
        } catch (NumberFormatException f) {
          JOptionPane.showMessageDialog(this, f);
        }
      }

      return numOfStems;
    }


    public int maxSteps()
    {
      int numberOfSteps = 0;
      boolean isInt = false;

      while (isInt == false) {

        String resultSteps = JOptionPane.showInputDialog( "Number of Steps per stem: " );

        try{
          numberOfSteps = Integer.parseInt(resultSteps);
          isInt = true;
        } catch (NumberFormatException f) {
          JOptionPane.showMessageDialog(this, f);
        }
      }

      return numberOfSteps;
    }


    public double transmissionProbability()
    {
      double transmissionProb = 0;
      boolean isDouble = false;

      while (isDouble == false) {

        String resultTransProb = JOptionPane.showInputDialog( "Transmission Probability [0.0, 1.0]" );

        try{
          transmissionProb = Double.parseDouble(resultTransProb);
          isDouble = true;
        } catch (NumberFormatException f) {
          JOptionPane.showMessageDialog(this, f);
        }
      }

      return transmissionProb;
    }


    public double maxRotationIncrement()
    {
      double rotInc = 0;
      boolean isDouble = false;

      while (isDouble == false) {

        String resultRotInc = JOptionPane.showInputDialog( "Rotation Incremement [0.0, 1.0]: " );

        try{
          rotInc = Double.parseDouble(resultRotInc);
          isDouble = true;
        } catch (NumberFormatException f) {
          JOptionPane.showMessageDialog(this, f);
        }
      }

      return rotInc;
    }

    public double growthSegmentIncrement()
    {
      double growSegInc = 0;
      boolean isDouble = false;

      while (isDouble == false) {

        String resultGrowthInc = JOptionPane.showInputDialog( "Growth Increment: " );

        try{
          growSegInc = Double.parseDouble(resultGrowthInc);
          isDouble = true;
        } catch (NumberFormatException f) {
          JOptionPane.showMessageDialog(this, f);
        }
      }

      return growSegInc;
    }

    private void plantRandom() {
      //all six variables taken from user imput
      int size = imageSize();
      int numStems = stemNum();
      int maxNumSteps = maxSteps();
      
      //alpha
      double transProb= transmissionProbability();
      
      //delta theta
      double maxRotInc = maxRotationIncrement();
      
      //delta rho
      double growthSegInc = growthSegmentIncrement();
      
      //initialize these variables
      double theta = Math.PI/2.0;
      double rho = 1.0;
      double x = size/2.0;
      double y = size/2.0;
      double reflPropBeta;
      double direction = 1.0;
      double tau = 0.0;
      double beta = (1.0 - transProb);
      
      //random numbers generated
      Random rando1 = new Random();
      Random rando2 = new Random();
      
      double turnLeft = 1.0;
      double turnRight = -1.0;
      double doTheTurn = rando1.nextBoolean() ? turnLeft : turnRight; // randomly generate the 1/-1 to determine which way to turn

      //create buffImg
      BufferedImage randomPlantImg = new BufferedImage( size, size, BufferedImage.TYPE_INT_ARGB );
      Graphics2D graph2D = (Graphics2D)randomPlantImg.createGraphics();
      beginImg(size, randomPlantImg);
      
      //a counter for the steps
      int numStepsRemaining = maxNumSteps;
      
      //initialzie and set variables for the initial step for each stem
      double previousX = x;
      double previousY = y;
      double previousTurn = doTheTurn;
      double coinBias;
      double curr;
      double newX;
      double newY;
      double theNextSegment = rho;
      
      
      //creating a 2D array for the information regarding the creation fo the steps
      double arrStem [][] = new double[numStems][5];
      
      //starting position for each stem
      for (int i = 0; i < numStems; i++) {
        arrStem[i][0] = previousX;
        arrStem[i][1] = previousY;
        arrStem[i][2] = rando1.nextBoolean() ? turnLeft : turnRight;
        arrStem[i][3] = theNextSegment;
        arrStem[i][4] = theta;
      }
      
      //while the number of steps remaining in each steps is not 0, entire the while loop
      while( numStepsRemaining != 0 ) {
        
        //draws the stems
        for ( int i = 0; i < numStems; i++ ){
          
          //draws the first step of each stem
          if( numStepsRemaining == maxNumSteps ){
              x = arrStem[i][0];
              y = arrStem[i][1];
              theNextSegment = arrStem[i][3];
              theta = arrStem[i][4];
              
              newX = theNextSegment*Math.cos(theta) + x;
              newY = y - theNextSegment*Math.sin(theta);
              //draw function here
              drawPlant(graph2D, (int)x, (int)y, (int)newX, (int)newY);
              
              
              arrStem[i][0] = newX;
              arrStem[i][1] = newY;
          }
          
          //draws the remaining segments for each stem one at time
          else {
              
              previousTurn = arrStem[i][2];
              
              //determine coinBias (same as tau from the algorithm), the virtual coin's bias
              if ( previousTurn == -1.0) {
                coinBias = transProb;
              } else {
                coinBias = beta;
              }
              
              //flip the biased coin to determine which direction to turn next
              if ( rando2.nextDouble() > coinBias ) {
                previousTurn = 1.0;
              } else {
                previousTurn = -1.0;
              }
              
              x = arrStem[i][0];
              y = arrStem[i][1];
              theNextSegment = arrStem[i][3];
              theta = arrStem[i][4];
              
              theNextSegment = theNextSegment + growthSegInc;
              theta = theta + (maxRotInc*rando2.nextDouble()*previousTurn);
              
              newX = theNextSegment*Math.cos(theta) + x;
              newY = y - theNextSegment*Math.sin(theta);
              drawPlant(graph2D, (int)x, (int)y, (int)newX, (int)newY);
              
              arrStem[i][0] = newX; 
              arrStem[i][1] = newY;
              arrStem[i][2] = previousTurn;
              arrStem[i][3] = theNextSegment;
              arrStem[i][4] = theta;
              
          }
        }
        //decrement the number of steps remaining as each step is completed
        numStepsRemaining--;
      }
      

      //after functionality is complete, display the buffered image in the window
      displayBufferedImage(randomPlantImg);

    }
    
    //function called to drawLine at the specified coordinates
    public void drawPlant(Graphics2D graph2D, int x, int y, int newX, int newY) {
      graph2D.setColor(Color.BLACK);
      graph2D.drawLine(x, y, newX, newY);
    }

    //initializes the buffered image with a white background
    public void beginImg(int size, BufferedImage buffImg) {

      //initializes each pixel in the buffered image to black
      for (int i=0; i<size; i++) {
        for (int j=0; j<size; j++) {
          buffImg.setRGB(i, j, 0xFFFFFFFF);
        } 
      }
      
      
    }

    //displays the bufferedImage after the algorithm runs
    public void displayBufferedImage(BufferedImage img)
    {

      this.setContentPane( new JScrollPane( new JLabel( new ImageIcon( img ) ) ) );
      this.validate();

    }


}
