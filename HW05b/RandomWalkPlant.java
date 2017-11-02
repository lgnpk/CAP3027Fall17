import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.lang.Thread;

public class RandomWalkPlant {
  private static final int WIDTH = 800;
  private static final int HEIGHT = 800;

  //Chartreuse	127-255-0	7fff00
  //Saddle Brown	139-69-19	8b4513



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

    this.setTitle( "CAP 3027 2017 - HW05b - Logan Peck" );
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

    //if the user selects this option, the default bright green and dark brown colors are set for the interpolation when
    //drawing the plant
    JMenuItem randomWalkPlant = new JMenuItem( "Directed random walk plant (default)" );
    randomWalkPlant.addActionListener( new ActionListener() {
      public void actionPerformed( ActionEvent event ) {
        colorsDefault();
      }
    } );

    fileMenu.add( randomWalkPlant );

    //if the user selects this option, the user is prompted for two hexadecimal values for their customer start and end colors
    //these colors are then passed into the plantRandom method to draw the plant using the custom colors
    JMenuItem configColors = new JMenuItem( "Directed random walk plant (Configure colors)" );
    configColors.addActionListener( new ActionListener() {
      public void actionPerformed( ActionEvent event ) {
        //plantRandom(startColor, endColor);
        final int customStartColor = customColor1();
        final int customEndColor = customColor2();

        new Thread( new Runnable() {
          public void run() {
            plantRandom(customStartColor, customEndColor);
          }
        }
        ).start();
      }
    } );

    fileMenu.add( configColors );


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
    public void colorsDefault() {
      //0xFF1AFF00 - bright green
      //0xFFA46800 - brown color

      //original start color - 0xFF7FFF00
      //original end color - 0xFF8B4513
      int startColor = 0xFF1AFF00;
      int endColor = 0xFFA46800;



      new Thread( new Runnable() {
        public void run() {
          plantRandom(startColor, endColor);
        }
      }
      ).start();

    }

    //prompt user for custom color 1
    //check for valid input
    public int customColor1()
    {
      int color1 = 0;
      boolean isInt = false;

      while (isInt == false){

        String result = JOptionPane.showInputDialog( "What is your start color? (hexadecimal): " );

        try {
          color1 = (int) Long.parseLong( result.substring( 2, result.length() ), 16 );
          isInt = true;
        } catch (NumberFormatException e) {
          JOptionPane.showMessageDialog( this, e );
        }
      }

      return color1;

    }

    //prompt user for custom color 2
    //check for valid input
    public int customColor2()
    {
      int color2 = 0;
      boolean isInt = false;

      while (isInt == false){

        String result = JOptionPane.showInputDialog( "What is your end color? (hexadecimal): " );

        try {
          color2 = (int) Long.parseLong( result.substring( 2, result.length() ), 16 );
          isInt = true;
        } catch (NumberFormatException e) {
          JOptionPane.showMessageDialog( this, e );
        }
      }

      return color2;

    }

    //user sets the image size
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

    //user inputs the steps
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

    //user inputs the transmission Probability
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

    //user inputs the max rotation increment allowed
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

    //user inputs growth segment increment
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

    //main program method
    //takes in two colors, either default or defined by the user to create the randomPlantWalk
    private void plantRandom(int startColor, int endColor) {
      //all six variables taken from user imput
      int size = imageSize();
      int numStems = stemNum();
      int maxNumSteps = maxSteps();

      //alpha
      double transProb = transmissionProbability();

      //delta theta
      double maxRotInc = maxRotationIncrement();

      //delta rho
      double growthSegInc = growthSegmentIncrement();

      int mask = 0xFF;

      int startR = (startColor >> 16) & mask;
      int startG = (startColor >> 8) & mask;
      int startB = startColor & mask;

      int endR = (endColor >> 16) & mask;
      int endG = (endColor >> 8) & mask;
      int endB = endColor & mask;

      int deltaR = (endR-startR)/(maxNumSteps - 1);
      int deltaG = (endG-startG)/(maxNumSteps - 1);
      int deltaB = (endB-startB)/(maxNumSteps - 1);

      //creates a color array with the number of values equal to the max number of steps entered by the user
      Color colorInterpolate[] = new Color[maxNumSteps];

      for (int i = 0; i < maxNumSteps; i++) {
        if( i == 0 ){
          colorInterpolate[i] = colorConversion(startR, startG, startB);
          //System.out.println("color: " +colorInterpolate[i]);
        }
        else {
          startR += deltaR;
          startG += deltaG;
          startB += deltaB;
          colorInterpolate[i] = colorConversion(startR, startG, startB);
          //System.out.println("color: " +colorInterpolate[i]);
        }
      }

      //variables required for stroke array
      float startStrokeValue = 0.5f;
      float endStrokeValue = 6.0f;
      float deltaStroke = (endStrokeValue-startStrokeValue)/(maxNumSteps-1);
      BasicStroke basicStrokes[] = new BasicStroke[maxNumSteps];

      for (int i = 0; i < maxNumSteps; i++) {
        if(i == 0) {
          basicStrokes[i] = new BasicStroke(startStrokeValue);
        }
        else {
          startStrokeValue += deltaStroke;
          basicStrokes[i] = new BasicStroke(startStrokeValue);
        }
      }

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

      double keepTrackOfX[][] = new double[numStems][maxNumSteps];
      double keepTrackOfY[][] = new double[numStems][maxNumSteps];

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

      //initialize a counter to keep track of the frame the animation is on
      int fCount = 1;

      //while the number of steps remaining in each steps is not 0, entire the while loop
      while( numStepsRemaining != 0 ) {

        //draws the stems
        for ( int i = 0; i < numStems; i++ ){

          //draws the first step of each stem
          if( numStepsRemaining == maxNumSteps ){
              previousX = arrStem[i][0];
              previousY = arrStem[i][1];
              theNextSegment = arrStem[i][3];
              theta = arrStem[i][4];

              newX = theNextSegment*Math.cos(theta) + previousX;
              newY = previousY - theNextSegment*Math.sin(theta);


              arrStem[i][0] = newX;
              arrStem[i][1] = newY;
              //
              keepTrackOfX[i][fCount-1] = newX;
              keepTrackOfY[i][fCount-1] = newY;
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

              previousX = arrStem[i][0];
              previousY = arrStem[i][1];
              theNextSegment = arrStem[i][3];
              theta = arrStem[i][4];

              theNextSegment = theNextSegment + growthSegInc;
              theta = theta + (maxRotInc*rando2.nextDouble()*previousTurn);

              newX = theNextSegment*Math.cos(theta) + previousX;
              newY = previousY - theNextSegment*Math.sin(theta);
              //  drawPlant(graph2D, (int)x, (int)y, (int)newX, (int)newY, colorInterpolate[numStepsRemaining-1], basicStrokes[maxNumSteps-numStepsRemaining]);

              arrStem[i][0] = newX;
              arrStem[i][1] = newY;
              keepTrackOfX[i][fCount-1] = newX;
              keepTrackOfY[i][fCount-1] = newY;
              arrStem[i][2] = previousTurn;
              arrStem[i][3] = theNextSegment;
              arrStem[i][4] = theta;

          }

        }

        double initialX;
        double initialY;
        Color currColor;
        double x1;
        double x2;
        BasicStroke currStroke;

        for (int i = 0; i < fCount; i++) {

          currColor = colorInterpolate[fCount - (i+1)];
          currStroke = basicStrokes[fCount - (i+1)];

          for (int j = 0; j < numStems; j++) {

            if (i == 0) {
              initialX = x;
              initialY = y;
            }

            else{
              initialX = keepTrackOfX[j][i-1];
              initialY = keepTrackOfY[j][i-1];

            }
            x1 = keepTrackOfX[j][i];
            x2 = keepTrackOfY[j][i];
            drawPlant(graph2D, (int)initialX, (int)initialY, (int)x1, (int)x2, currColor, currStroke);
          }

        }

        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
              //after functionality is complete, display the buffered image in the window
            //  displayBufferedImage(randomPlantImg);
              displayBufferedImage(randomPlantImg);

            }
          }
        );
        try {
          Thread.sleep(80);
        }
        catch (InterruptedException e) {
        }
        //decrement the number of steps remaining as each step is completed
        numStepsRemaining--;
        fCount++;
      }



    }

    //function called to drawLine at the specified coordinates
    //takes in a Graphics2D, x, y, x2, y2, color after passing through the conversion method and the index of the basicstrokes array
    //for that step
    public void drawPlant(Graphics2D graph2D, int x, int y, int newX, int newY, Color convertedColor, BasicStroke basicStrokes) {
      RenderingHints hint = new RenderingHints( RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
      graph2D.setRenderingHints(hint);
      graph2D.setStroke( basicStrokes );
      graph2D.setColor(convertedColor);
      graph2D.drawLine(x, y, newX, newY);
    }

    //initializes the buffered image with a white background
    public void beginImg(int size, BufferedImage buffImg) {

      //initializes each pixel in the buffered image to black
      for (int i=0; i<size; i++) {
        for (int j=0; j<size; j++) {
          buffImg.setRGB(i, j, 0xFF000000);
        }
      }


    }

    //displays the bufferedImage after the algorithm runs
    public void displayBufferedImage(BufferedImage img)
    {

      this.setContentPane(new JScrollPane(new JLabel( new ImageIcon(img))));
      this.validate();

    }

    //takes in the int R, G, and B values for a color and synthesizes it into a color object
    //color object is returned and passed into the drawPlant method to draw that part of the stem at that particular color
    public Color colorConversion(int r, int g, int b) {
    //  int mask = 0xFF;

      Color convertedColor = new Color(r, g, b);

      return convertedColor;
    }


}
