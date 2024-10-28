// Copyright Eric Chauvin 2019 - 2024.



// This is licensed under the GNU General
// Public License (GPL).  It is the
// same license that Linux has.
// https://www.gnu.org/licenses/gpl-3.0.html




// Local Business Project.

// "\\AILocalBusData\\URLFiles\\"



import javax.swing.SwingUtilities;

// For Serializable:
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;



class MainApp implements Runnable, Serializable
  {
  public static final String versionDate =
                              "10/28/2024";
  public static final long serialVersionUID = 1;


  private MainWindow mainWin;
  // public ConfigureFile mainConfigFile;
  private String[] argsArray;
  private StrA programDirectory;



  public static void main( String[] args )
    {
    MainApp mApp = new MainApp( args );
    SwingUtilities.invokeLater( mApp );
    }



  @Override
  public void run()
    {
    setupProgram();
    }



  private MainApp()
    {
    }


  public MainApp( String[] args )
    {
    argsArray = args;
    }


  // For Serializable:
  private void writeObject(
                   ObjectOutputStream stream )
                           throws IOException
    {
    stream.defaultWriteObject();
    }

  private void readObject(
                     ObjectInputStream stream )
                     throws IOException,
                     ClassNotFoundException
    {
    stream.defaultReadObject();
    }


  private void setupProgram()
    {
    // checkSingleInstance()

     // All programs need to have a batch file give
    // it the program directory so they're not stuck
    // in that directory.
    // programDirectory = new StrA(
    //                 "\\Eric\\Main\\... what?" );

    // int length = argsArray.length;
    // if( length > 0 )
      // programDirectory = new StrA( argsArray[0] );

/*
    String mainConfigFileName = programDirectory +
                                      "MainConfigure.txt";

    mainConfigFile = new ConfigureFile( this,
                                mainConfigFileName );
    */

    mainWin = new MainWindow( this,
                              "Local Business" );
    mainWin.initialize();

    /*
    showStatus( " " );
    showStatus( "argsArray length: " + length );
    for( int count = 0; count < length; count++ )
      showStatus( argsArray[count] );
    */

    // showStatus( " " );
    }



  public void showStatusAsync( String toShow )
    {
    if( mainWin == null )
      return;

    mainWin.showStatusAsync( toShow );
    }



  public void clearStatus()
    {
    if( mainWin == null )
      return;

    mainWin.clearStatus();
    }




  }
