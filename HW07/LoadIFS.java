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
import java.awt.geom.Point2D;
import java.awt.geom.AffineTransform;
import static java.lang.Math.abs;
import java.util.Scanner;

public class LoadIFS {
  private static final int WIDTH = 800;
  private static final int HEIGHT = 800;

  public static void main( String[] args) {
    
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {createAndShowGUI();}
    });
  }
  public static void createAndShowGUI() {
     JFrame frame = new ImageFrame( WIDTH, HEIGHT);
     frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
     frame.setVisible( true );
  }
 
}

//##############################################################


class ImageFrame extends JFrame {
  int width = 401;
  int height = 401;

  private final JFileChooser chooser; //JFileChooser variable
  private AffineTransform trans[]; //array of affinetransform objects that will be referenced to calculate the particular fractal transforms
  private double probability[]; //separate array that stores the probability value "p" if given
  private boolean thereIsIFSDesc = false;
  private BufferedImage fractalImg;
  private int imageSize = 0;
  private boolean imageIsCreated = false; 
  private int backgroundColor = 0;
  private int foregroundColor = 0;
  private boolean imgConfigd = false;

  //================================================
  //constructor

  public ImageFrame ( int width, int height ) {

    //setup the frames attributes

    this.setTitle( "CAP 3027 2017 - HW07 - Logan Peck" );
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

    //when this option is selected
    //user should be prompted for file containing
    //IFS Description (JFileChooser)
    //Load IFS Description from Configuration File
    JMenuItem loadIFSDescription = new JMenuItem( "Load IFS Description" );
    loadIFSDescription.addActionListener( new ActionListener() {
      public void actionPerformed( ActionEvent event ) {
        recordDesc();
      }
    } );

    fileMenu.add( loadIFSDescription );

    
    JMenuItem configImage = new JMenuItem( "Configure image" );
    configImage.addActionListener( new ActionListener() {
      public void actionPerformed( ActionEvent event ) {
        configImg();
      }
    } );

    fileMenu.add( configImage );
    
    JMenuItem displayIFS = new JMenuItem( "Display IFS" );
    displayIFS.addActionListener( new ActionListener() {
      public void actionPerformed( ActionEvent event ) {
        displayImg();
      }
    } );

    fileMenu.add( displayIFS );
    
    JMenuItem saveImg = new JMenuItem( "Save image" );
    saveImg.addActionListener( new ActionListener() {
      public void actionPerformed( ActionEvent event ) {
      
      }
    } );

    fileMenu.add( saveImg );


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
  
  public void recordDesc() {
    new Thread(new Runnable() {
      public void run() {
        takeInDesc();
      }
    }).start();
  }
  
  public void configImg() {
    new Thread(new Runnable() {
      public void run() {
        configImgTask();
      }
    }).start();
  }
  
  public void displayImg() {
    new Thread(new Runnable() {
      public void run() {
        displayImgTask();
      }
    }).start();
  }
  
   public void saveImg() {
      new Thread(new Runnable() {
        public void run() {
          saveImgTask();
        }
      }).start();
    }
  
  public void takeInDesc() {
    
    double a, b, c, d, e, f, p;
    String storeFileLines;
    int countTheLines = 0; //the number of possible transforms that will be computed
    boolean pProvided = false; //will be used to verify if the probability is included in inputted file string for transforms
    
    File IFSDesc = getFile(); //actually gets the file for the IFS description
    
    FileReader firstPass = null;
    FileReader getData = null;
    
    while(firstPass == null) {
      try {
        firstPass = new FileReader(IFSDesc); //calling the filereader on the file that was stored and saving as filereader variable
        getData = new FileReader(IFSDesc); //calling the filereader on same file because retroactive file reading is not allowed
      } 
      catch (FileNotFoundException exp){
        JOptionPane.showMessageDialog(this, exp); //this refers to the JFrame and to display the error message "e" in said JFrame
      }
    }
    
    //Filereader gets information in from file, buferedReader allows to go line by line
    BufferedReader firstTime = new BufferedReader(firstPass);
    BufferedReader secondTime = new BufferedReader(getData);
    
    //create scanner for string parsing
    Scanner sc;
    
    //when using BufferedReader, also one of those times that you must do the try/catch
    
    try {
      
      //checks for a line within the text file to read
      while((storeFileLines = firstTime.readLine()) != null) { //keep checking for lines in the file until there are no lines
        System.out.println(storeFileLines);
        
        ++countTheLines;
      }
      
      System.out.println(countTheLines); //check for the line counter to make sure the BufferedReader takes in all lines from text file
    
      trans = new AffineTransform[countTheLines]; //initializes the array to null/empty with the number of rows equalling the number of lines
      probability = new double[countTheLines]; //initializes array to null for all "p" values given
      
      for (int i = 0; i < countTheLines; i++) {
          sc = new Scanner(secondTime.readLine()); //took the file in, imposed bufferedreader, now getting line from bufferedreader and imposing scanner  
                                                   // on it, scanner value recycled each time
          while (sc.hasNextDouble()) {
            
            a = sc.nextDouble();
            b = sc.nextDouble();
            c = sc.nextDouble();
            d = sc.nextDouble();
            e = sc.nextDouble();
            f = sc.nextDouble();
            
            if (sc.hasNextDouble()) { //if there is a p value, get it and set the value equal to the index i of that step in the for loop for
                                      //the array of probability values
              p = sc.nextDouble();    
              probability[i] = p;
              pProvided = true;       //probabilities are either provided for all lines or not at all
            }
            else {
              probability[i] = 0; //if there is no probability at that index, set value to zero
              System.out.println("does not have probability");
            }
            
            trans[i] = new AffineTransform(a, c, b, d, e, f);  //populates array with affinetransform data
            System.out.println(trans[i]); //checks to determine that values are populated correctly
          }  
        } 
      }
      catch (IOException ex) {
        JOptionPane.showMessageDialog(this, ex);
      }
      
      if (pProvided == false) { //if there are no probability values provided, matrix determinants must be calculated
        double det1 = 0.0; //temporary variable to be used to calculate the determinant values
        double detSum = 0.0; //will store the value of all determinants calculated
        double det[] = new double[countTheLines];
        
        double fudgeF = 0.0; 
        double detCheck = 0.0;
        double pTotal = 0.0; //keeps running sum of how much that have been added to other transformed to at least guarantee that they will 
                  //possibly be chosen by the affinetransform algorithm
        double checkSum = 0.0;
        
        for( int i = 0; i < countTheLines; i++ ) {
          det1 = abs(trans[i].getDeterminant()); //abs() is used because affinetransforms must be positive otherwise the sum used to find probabilities 
                                                 //cannot be calculated properly
          if (det1 == 0.0) {  //check to see if that determinant value is zero
            detCheck = detCheck + 1;
            pTotal = pTotal + 0.01;
          }
          
          det[i] = det1; //storing the determinant values in the determinant array
          detSum = detSum + det1; //keeps the running sum of the determinant values in order to calculate probability later
        }
        
        if ( detCheck != 0.0 ) {
          fudgeF = pTotal/(countTheLines - detCheck);
        
        }
        
        for ( int i = 0; i < countTheLines; i++ ) {
          if ( det[i] == 0.0 ) {
            probability[i] = 0.01;
          }
          else {
            probability[i] = ((det[i])/detSum) - fudgeF; //subtracts any distribution of probabilty if there is any
          }
          
          checkSum = checkSum + probability[i];
          System.out.println(probability[i]);
        }
        System.out.println(checkSum);
      }
    thereIsIFSDesc = true;
  }
  
  public void configImgTask() {
      imageSize = imageSize();
      backgroundColor = backgroundCol();
      foregroundColor = foregroundCol();
      
      fractalImg = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_ARGB);
      initializeBackground(imageSize, backgroundColor);
      
      imgConfigd = true;
      
      
  }
  
  public void initializeBackground(int imageSize, int backgroundColor) {
      for (int i = 0; i < imageSize; i++) {
        for (int j = 0; j < imageSize; j++) {
          fractalImg.setRGB(i, j, backgroundColor);
        }
      }
  }
  
  public void displayImgTask() {
    
    if(imgConfigd && thereIsIFSDesc) {
      
      double runSum = 0.0; //set up an array for the intervals where a randomly chosen transform will be applied
      Random randoImg = new Random(); //creation of new random number gen
      
      //Point2D's are the best way to compute transforms
      Point2D pt = new Point2D.Double(randoImg.nextDouble(), randoImg.nextDouble()); //from 0.0 inclusive to 1.0 exclusive
      double var;
      
      //how many time a transform is applied to a point
      int th = 10;  //also try 15 for possibly better img
      
      int numberOfGenerations = genUserInput();
      
      initializeBackground(imageSize, backgroundColor);
      
      double rdmSelect[] = new double[probability.length]; //random choice array
      
      for (int i = 0; i < probability.length; i++) {
        
        runSum = runSum + probability[i];
        rdmSelect[i] = runSum;
        
      }
      
      for (int i = 0; i < th; i++) {
        
        var = randoImg.nextDouble();
        
        //comparing random bias choice
        int qu;
        
        for (qu = 0; qu != rdmSelect.length; ++qu) {
          if (var < rdmSelect[qu]) {
            break; //qu is now a value that can be used and will give index of randomly chosen transform
          }
        }
        
        trans[qu].transform(pt, pt); //taking the point2D object and updating the new location of the Point2D
        
      }
      
      int startX;
      int startY;
      
      for (int i = 0; i < numberOfGenerations; i++) {
        
        var = randoImg.nextDouble();
        
        //comparing random bias choice
        int qu;
        
        for (qu = 0; qu < rdmSelect.length; ++qu) {
          if (var < rdmSelect[qu]) {
            break; //qu is now a value that can be used and will give index of randomly chosen transform
          }
        }
        
        trans[qu].transform(pt, pt); //taking the point2D object and updating the new location of the Point2D
        
        startX = (int)(pt.getX()*imageSize);
        startY = (int)(-1*(pt.getY()*imageSize) + imageSize);
        
        fractalImg.setRGB(startX, startY, foregroundColor);
      }
      
     
      
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          displayBufferedImage(fractalImg);
        }
      });
      
       imageIsCreated = true;
      
    }
    
  }
  
  public void saveImgTask() {
    if (imageIsCreated) {
      File outputFile = new File("newImage.png");
      
      try {
        javax.imageio.ImageIO.write( fractalImg, "png", outputFile);
      }
      catch ( IOException e )
      {
        JOptionPane.showMessageDialog( ImageFrame.this,
      	   		          "Error saving file",
      				  "oops!",
      				  JOptionPane.ERROR_MESSAGE );
      }
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
  
    
    //prompt user for background color
    //check for valid input
    public int backgroundCol()
    {
      int backgroundCol = 0;
      boolean isInt = false;

      while (isInt == false){

        String result = JOptionPane.showInputDialog( "What is your background color? (hexadecimal): " );

        try {
          backgroundCol = (int) Long.parseLong( result.substring( 2, result.length() ), 16 );
          isInt = true;
        } catch (NumberFormatException e) {
          JOptionPane.showMessageDialog( this, e );
        }
      }

      return backgroundCol;
      
    }

    //prompt user for foreground color
    //check for valid input
    public int foregroundCol()
    {
      int foregroundCol = 0;
      boolean isInt = false;

      while (isInt == false){

        String result = JOptionPane.showInputDialog( "What is your foreground color? (hexadecimal): " );

        try {
          foregroundCol = (int) Long.parseLong( result.substring( 2, result.length() ), 16 );
          isInt = true;
        } catch (NumberFormatException e) {
          JOptionPane.showMessageDialog( this, e );
        }
      }

      return foregroundCol;

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

    public int genUserInput()
    {
      int genNum = 0;
      boolean isInt = false;

      while (isInt == false) {

        String resultStem = JOptionPane.showInputDialog( "Number of generations: " );

        try{
          genNum = Integer.parseInt(resultStem);
          isInt = true;
        } catch (NumberFormatException f) {
          JOptionPane.showMessageDialog(this, f);
        }
      }

      return genNum;
    }


    //displays the bufferedImage after the algorithm runs
    public void displayBufferedImage(BufferedImage fractalImg)
    {

      this.setContentPane(new JScrollPane(new JLabel( new ImageIcon(fractalImg))));
      this.validate();

    }

    

}
