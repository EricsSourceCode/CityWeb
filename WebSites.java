// Copyright Eric Chauvin 2020 - 2024.



// This is licensed under the GNU General
// Public License (GPL).  It is the
// same license that Linux has.
// https://www.gnu.org/licenses/gpl-3.0.html



import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// For Serializable:
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;




public class WebSites implements ActionListener,
                       Serializable
  {
  // It needs to have a version UID since it's
  // serializable.
  public static final long serialVersionUID = 1;
  private MainApp mApp;
  private Timer getURLTimer;
  private FifoStrA urlFifo;
  private URLFileDictionary urlDictionary;



  private WebSites()
    {
    }


  public WebSites( MainApp appToUse )
    {
    mApp = appToUse;
    StrA fileName = new StrA(
          "\\EconData\\UrlDictionary.txt" );

    urlDictionary = new URLFileDictionary( mApp,
                                     fileName );
    urlDictionary.readFromFile();
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




  public void timerStart()
    {
    urlFifo = new FifoStrA( mApp, 1024 * 16 );

    addURLsToFifo();
    setupTimer();
    }



  public void analyze()
    {
    // mApp.showStatusAsync( "analyze()." );
    AnalyzeNewLinks newLinks = new AnalyzeNewLinks(
                                mApp, urlDictionary );
    Thread aThread = new Thread( newLinks );
    aThread.start();
    }


/*
  public void analyzeSpanish()
    {
    AnalyzeSpanish spanish = new AnalyzeSpanish(
                             mApp, urlDictionary );
    Thread aThread = new Thread( spanish );
    aThread.start();
    }
*/


  public void cancel()
    {
    if( getURLTimer != null )
      {
      getURLTimer.stop();
      getURLTimer = null;
      }

    urlDictionary.saveToFile();
    }



  private void setupTimer()
    {
    // HTTP error 429 is too many requests.
    // Even waiting 73 seconds doesn't
    // work.  Is it keeping the TLS connection
    // alive or something?

    // Very slow...
    int delay = 1000 * 4;
    getURLTimer = new Timer( delay, this );
    getURLTimer.start();
    mApp.showStatusAsync( "Timer started." );
    }



  public void actionPerformed( ActionEvent event )
    {
    try
    {
    // String paramS = event.paramString();
    String command = event.getActionCommand();
    if( command == null )
      {
      // mApp.showStatusAsync( "ActionEvent command is null." );
      doTimerEvent();
      return;
      }

    // showStatus( "ActionEvent Command is: " + command );
    }
    catch( Exception e )
      {
      mApp.showStatusAsync( "Exception in ActionPerformed()." );
      mApp.showStatusAsync( e.getMessage() );
      }
    }



  private void doTimerEvent()
    {
    StrA urlToGet = urlFifo.getValue();
    if( urlToGet == null )
      {
      mApp.showStatusAsync( "\n\nNothing in Fifo." );
      getURLTimer.stop();
      urlDictionary.saveToFile();
      return;
      }

    urlToGet = urlToGet.replace( new StrA( "\'" ),
                                  StrA.Empty );

    // mApp.showStatusAsync( "\nurlToGet is:\n" + urlToGet );
    URLFile uFile = urlDictionary.getValue(
                                  urlToGet );
    if( uFile == null )
      uFile = new URLFile( mApp, urlToGet );

    uFile.setAnchorsPulledFalse();
    urlDictionary.setValue( urlToGet, uFile );

    String fileName = uFile.getFileName().toString();
    fileName = "\\EconData\\URLFiles\\" +
                             fileName;
    // mApp.showStatusAsync( "File name: " + fileName );
    String urlS = urlToGet.toString();
    URLClient urlClient = new URLClient( mApp,
                                 fileName,
                                 urlS );

    Thread urlThread = new Thread( urlClient );
    urlThread.start();
    }



  public void showCharacters()
    {
    // 126 is the tilde character.
    // 127 is delete.
    // 161 is upside down exclamation.
    // 169 is copyright.
    // 174 is rights symbol.
    // 209 is capital N like el niNa.
    // 232 through 235 is e.

    // C1 Controls and Latin-1 Supplement (0080 00FF)
    // Latin Extended-A (0100 017F)
    // Latin Extended-B (0180 024F)


    mApp.showStatusAsync( "\n\n" );
    // for( int count = 0x100; count <= 0x17F; count++ )
    for( int count = 161; count <= 255; count++ )
      {
      // Integer.toHexString(n).toUpperCase()

      char testC = (char)count;
      mApp.showStatusAsync( "" + count + ") " + testC );
      }

    mApp.showStatusAsync( "\n\n" );
    }



  public void addURLsToFifo()
    {
    urlFifo.setValue( new StrA(
     "https://www.federalreserve.gov/" ));

    urlFifo.setValue( new StrA(
         "https://research.stlouisfed.org/" ));

    urlFifo.setValue( new StrA(
    "https://research.stlouisfed.org/news-releases" ));



    // Add it to isGoodFullFile() too.
    // _And_ URLParse.hasValidDomain()

    addEmptyFilesToFifo();
    }



  private void addEmptyFilesToFifo()
    {
    mApp.showStatusAsync(
             "Adding empty files to Fifo." );
    StrA fileS = urlDictionary.
                       makeKeysValuesStrA();

    StrArray linesArray = fileS.splitChar( '\n' );
    final int last = linesArray.length();
    int howMany = 0;
    for( int count = 0; count < last; count++ )
      {
      StrA line = linesArray.getStrAt( count );
      URLFile uFile = new URLFile( mApp );
      uFile.setFromStrA( line );
      StrA fileName = uFile.getFileName();

      // mApp.showStatusAsync( "" + line );
      StrA filePath = new StrA(
              "\\EconData\\URLFiles\\" );
      filePath = filePath.concat( fileName );
      // mApp.showStatusAsync( "filePath: " + filePath );


      if( !FileUtility.exists( filePath ))
        {
        StrA urlToGet = uFile.getUrl();
        if( !isGoodFullFile( urlToGet ))
          continue;

        // HTTP error 429 is too many requests.
        howMany++;
        if( howMany > 5 )
          break;

        mApp.showStatusAsync( "\nAdding to Fifo: (" +
                                   howMany + ") " +
                                   urlToGet );

        urlFifo.setValue( urlToGet );
        }
      }
    }



  private boolean isGoodFullFile( StrA in )
    {
    if( in.containsStrA( new StrA(
        "/site/forms/" )))
      return false;

    if( in.containsStrA( new StrA(
        "/users/admin/" )))
      return false;

    if( in.containsStrA( new StrA(
        "/users/login/" )))
      return false;

    if( in.containsStrA( new StrA(
        "/users/signup/" )))
      return false;

    if( in.containsStrA( new StrA(
        "/classifieds/" )))
      return false;

    if( in.containsStrA( new StrA(
        "/place_an_ad/" )))
      return false;

    if( in.containsStrA( new StrA(
        "/ayuda-fund/" )))
      return false;

    if( in.containsStrA( new StrA(
        ".php" )))
      return false;


    if( in.containsStrA( new StrA(
        "apply.coloradomtn.edu" )))
      return false;

    if( in.containsStrA( new StrA(
        "outlook.office.com" )))
      return false;

    if( in.containsStrA( new StrA(
        ".alpinebank.com" )))
      return false;

    if( in.containsStrA( new StrA(
        "&quot;" )))
      return false;

    if( in.containsStrA( new StrA(
        "/." )))
      return false;

    if( in.containsStrA( new StrA(
        "application/pdf" )))
      return false;

    if( in.containsStrA( new StrA(
      "coloradomtn.edu/download/" )))
      return false;

    if( in.endsWith( new StrA(
           ".pdf" )))
      return false;

    if( in.endsWith( new StrA(
           ".php" )))
      return false;

    if( in.endsWith( new StrA(
           ".aspx" )))
      return false;

    if( in.containsStrA( new StrA(
           "#" )))
      return false;

    if( in.containsStrA( new StrA(
           "/../" )))
      return false;



    if( in.containsStrA( new StrA(
         // "research.stlouisfed.org/" )))
         ".stlouisfed.org/" )))
      return true;

    if( in.containsStrA( new StrA(
           ".federalreserve.gov/" )))
      return true;
    return false;
    }




  }
