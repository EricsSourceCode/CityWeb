// Copyright Eric Chauvin 2019 - 2025.



// This is licensed under the GNU General
// Public License (GPL).  It is the
// same license that Linux has.
// https://www.gnu.org/licenses/gpl-3.0.html




// City Web



import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowStateListener;
import javax.swing.JTextArea;
import javax.swing.JOptionPane;
import javax.swing.JMenuBar;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import javax.swing.WindowConstants;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Container;
import java.io.File;

// For Serializable:
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;





// black, blue, cyan, darkGray, gray, green, lightGray,
// magenta, orange, pink, red, white, yellow.


// This class is final because of warnings
// about using subclasses with the constructor
// this class has.
public final class MainWindow extends JFrame
                                implements
                                WindowListener,
                                WindowFocusListener,
                                WindowStateListener,
                                ActionListener
  {
  // It needs to have a version UID since it's
  // serializable.
  public static final long serialVersionUID = 1;
  private MainApp mApp;
  private Font mainFont;
  private boolean windowIsClosing = false;
  private JTextArea statusTextArea;
  private String statusFileName = "";
  private JPanel mainPanel = null;
  private String uiThreadName = "";
  private WebSites webSites;




  private MainWindow()
    {
    }



  public MainWindow( MainApp useApp,
                     String showText )
    {
    super( showText );

    mApp = useApp;
    uiThreadName = Thread.currentThread().
                                 getName();

    mainFont = new Font( Font.MONOSPACED,
                         Font.PLAIN,
                         45 );

    setDefaultCloseOperation( WindowConstants.
                              EXIT_ON_CLOSE );

    setSize( 900, 500 );
    addComponents( getContentPane() );
    // pack();
    // setExtendedState( JFrame.MAXIMIZED_BOTH );

    setupMenus();

    webSites = new WebSites( mApp );


    addWindowListener( this );
    addWindowFocusListener( this );
    addWindowStateListener( this );

    // Center it.
    setLocationRelativeTo( null );
    setVisible( true );
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



  // Do this after the constructor has returned.
  public void initialize()
    {
    String allTogether =
             "Programming by Eric Chauvin.\n" +
             "Version date: " +
             MainApp.versionDate + "\n\n" +
             "Leadville City Web";

    showStatusAsync( allTogether );
    }



  public void windowStateChanged( WindowEvent e )
    {
    // showStatusAsync( "windowStateChanged" );
    }


  public void windowGainedFocus( WindowEvent e )
    {
    // showStatusAsync( "windowGainedFocus" );
    }


  public void windowLostFocus( WindowEvent e )
    {
    // showStatusAsync( "windowLostFocus" );
    }



  public void windowOpened( WindowEvent e )
    {
    // showStatusAsync( "windowOpened" );
    }



  public void windowClosing( WindowEvent e )
    {
    windowIsClosing = true;
    webSites.cancel();
    }



  public void windowClosed( WindowEvent e )
    {
    // showStatusAsync( "windowClosed" );
    }


  public void windowIconified( WindowEvent e )
    {
    // keyboardTimer.stop();
    // showStatusAsync( "windowIconified" );
    }



  public void windowDeiconified( WindowEvent e )
    {
    // keyboardTimer.start();
    // showStatusAsync( "windowDeiconified" );
    }



  public void windowActivated( WindowEvent e )
    {
    // showStatusAsync( "windowActivated" );
    }


  public void windowDeactivated( WindowEvent e )
    {
    // showStatusAsync( "windowDeactivated" );
    }



  private void addComponents( Container pane )
    {
    // The red colors are for testing.
    this.setBackground( Color.red );
    this.setForeground( Color.red );

    pane.setBackground( Color.black );
    pane.setForeground( Color.red );
    pane.setLayout( new LayoutSimpleVertical());

    mainPanel = new JPanel();
    mainPanel.setLayout( new LayoutSimpleVertical());
    mainPanel.setBackground( Color.black );
    // Setting it to FixedHeightMax means this component is
    // stretchable.
    // new Dimension( Width, Height );
    mainPanel.setPreferredSize( new Dimension(
                   1, LayoutSimpleVertical.FixedHeightMax ));

    pane.add( mainPanel );
    addStatusTextPane();
    }



  public void showStatusAsync( String toShow )
    {
    SwingUtilities.invokeLater( new Runnable()
      {
      public void run()
        {
        showStatusA( toShow );
        }
      });
    }



  private void showStatusA( String toShow )
    {
    if( statusTextArea == null )
      return;

    String thisThreadName = Thread.currentThread().
                                           getName();

    if( !uiThreadName.equals( thisThreadName ))
      {
      String showWarn = "showStatusA() is being" +
                " called from the wrong thread.\n\n";

      statusTextArea.setText( showWarn );
      return;
      }

    statusTextArea.append( toShow + "\n" );
    }



  public void clearStatus()
    {
    if( statusTextArea == null )
      return;

    statusTextArea.setText( "" );
    }



  private void addStatusTextPane()
    {
    statusTextArea = new JTextArea();
    // statusTextArea.setEditable( false );
    statusTextArea.setDragEnabled( false );
    statusTextArea.setFont( mainFont );
    statusTextArea.setLineWrap( true );
    statusTextArea.setWrapStyleWord( true );
    statusTextArea.setBackground( Color.black );
    statusTextArea.setForeground( Color.white );
    statusTextArea.setSelectedTextColor( Color.white );
    statusTextArea.setSelectionColor( Color.darkGray );
    // black, blue, cyan, darkGray, gray, green, lightGray,
    // magenta, orange, pink, red, white, yellow.

    CaretWide cWide = new CaretWide();
    statusTextArea.setCaret( cWide );
    statusTextArea.setCaretColor( Color.white );

    JScrollPane scrollPane1 = new JScrollPane(
                                     statusTextArea );

    scrollPane1.setVerticalScrollBarPolicy(
             JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );

    // Setting it to FixedHeightMax means this component is
    // stretchable.
    // new Dimension( Width, Height );

    scrollPane1.setPreferredSize( new Dimension(
                   1, LayoutSimpleVertical.FixedHeightMax ));

    // mainPanel.add( statusTextArea );
    mainPanel.add( scrollPane1 );
    }



  private void setupMenus()
    {
    JMenuBar menuBar = new JMenuBar();
    menuBar.setBackground( Color.black );

    ///////////////////////
    // File Menu:
    JMenu fileMenu = new JMenu( "File" );
    fileMenu.setMnemonic( KeyEvent.VK_F );
    fileMenu.setFont( mainFont );
    fileMenu.setForeground( Color.white );
    menuBar.add( fileMenu );

    JMenuItem menuItem;

    menuItem = new JMenuItem( "Timer Start" );
    menuItem.setMnemonic( KeyEvent.VK_T );
    menuItem.setActionCommand( "FileTimerStart" );
    menuItem.addActionListener( this );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( mainFont );
    fileMenu.add( menuItem );

    menuItem = new JMenuItem( "Analyze News" );
    menuItem.setMnemonic( KeyEvent.VK_A );
    menuItem.setActionCommand( "FileAnalyze" );
    menuItem.addActionListener( this );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( mainFont );
    fileMenu.add( menuItem );

/*
    menuItem = new JMenuItem( "Analyze Spanish" );
    menuItem.setMnemonic( KeyEvent.VK_S );
    menuItem.setActionCommand( "FileAnalyzeSpanish" );
    menuItem.addActionListener( this );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( mainFont );
    fileMenu.add( menuItem );
*/

    menuItem = new JMenuItem( "Cancel" );
    menuItem.setMnemonic( KeyEvent.VK_C );
    menuItem.setActionCommand( "FileCancel" );
    menuItem.addActionListener( this );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( mainFont );
    fileMenu.add( menuItem );

    menuItem = new JMenuItem( "Exit" );
    menuItem.setMnemonic( KeyEvent.VK_X );
    menuItem.setActionCommand( "FileExit" );
    menuItem.addActionListener( this );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( mainFont );
    fileMenu.add( menuItem );


    ///////////////////////
    // Edit Menu:
    JMenu editMenu = new JMenu( "Edit" );
    editMenu.setMnemonic( KeyEvent.VK_E );
    editMenu.setForeground( Color.white );
    editMenu.setFont( mainFont );
    menuBar.add( editMenu );

    menuItem = new JMenuItem( "Copy" );
    menuItem.setMnemonic( KeyEvent.VK_C );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( mainFont );
    menuItem.setActionCommand( "EditCopy" );
    menuItem.addActionListener( this );
    editMenu.add( menuItem );


    ///////////////////////
    // Help Menu:
    JMenu helpMenu = new JMenu( "Help" );
    helpMenu.setMnemonic( KeyEvent.VK_H );
    helpMenu.setForeground( Color.white );
    helpMenu.setFont( mainFont );
    menuBar.add( helpMenu );

    menuItem = new JMenuItem( "About" );
    menuItem.setMnemonic( KeyEvent.VK_A );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( mainFont );
    menuItem.setActionCommand( "HelpAbout" );
    menuItem.addActionListener( this );
    helpMenu.add( menuItem );

    menuItem = new JMenuItem( "Show Non-ASCII" );
    menuItem.setMnemonic( KeyEvent.VK_A );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( mainFont );
    menuItem.setActionCommand( "HelpShowNonASCII" );
    menuItem.addActionListener( this );
    helpMenu.add( menuItem );

    setJMenuBar( menuBar );
    }



  public void actionPerformed( ActionEvent event )
    {
    try
    {
    // String paramS = event.paramString();

    String command = event.getActionCommand();

    if( command == null )
      {
      // keyboardTimerEvent();
      return;
      }

    // showStatusAsync( "ActionEvent Command is: " + command );

    //////////////
    // File Menu:
    if( command == "FileTimerStart" )
      {
      webSites.timerStart();
      }

    if( command == "FileAnalyze" )
      {
      webSites.analyze();
      }

/*
    if( command == "FileAnalyzeSpanish" )
      {
      webSites.analyzeSpanish();
      }
*/
    if( command == "FileCancel" )
      {
      showStatusAsync( "Canceled...." );
      webSites.cancel();
      }


    if( command == "FileExit" )
      {
      System.exit( 0 );
      // return;
      }



    /////////////
    // Edit Menu:
    if( command == "EditCopy" )
      {
      editCopy();
      return;
      }


    //////////////
    // Help Menu:
    if( command == "HelpAbout" )
      {
      showAboutBox();
      return;
      }

    if( command == "HelpShowNonASCII" )
      {
      showNonAsciiCharacters();
      return;
      }
    }
    catch( Exception e )
      {
      showStatusAsync( "Exception in actionPerformed()." );
      showStatusAsync( e.getMessage() );
      }
    }



  private void editCopy()
    {
    try
    {
    if( statusTextArea == null )
      return;

    statusTextArea.copy();
    }
    catch( Exception e )
      {
      showStatusAsync( "Exception in editCopy()." );
      showStatusAsync( e.getMessage() );
      }
    }



  private void showAboutBox()
    {
    JOptionPane.showMessageDialog( this,
                 "Programming by Eric Chauvin.  Version date: " + MainApp.versionDate );

    }



  private void showNonAsciiCharacters()
    {
    /*

    Symbols:
        General Punctuation (2000206F)
        Superscripts and Subscripts (2070209F)
        Currency Symbols (20A020CF)
        Combining Diacritical Marks for Symbols (20D020FF)
        Letterlike Symbols (2100214F)
        Number Forms (2150218F)
        Arrows (219021FF)
        Mathematical Operators (220022FF)
        Miscellaneous Technical (230023FF)
        Control Pictures (2400243F)
        Optical Character Recognition (2440245F)
        Enclosed Alphanumerics (246024FF)
        Box Drawing (2500257F)
        Block Elements (2580259F)
        Geometric Shapes (25A025FF)
        Miscellaneous Symbols (260026FF)
        Dingbats (270027BF)
        Miscellaneous Mathematical Symbols-A (27C027EF)
        Supplemental Arrows-A (27F027FF)
        Braille Patterns (280028FF)
        Supplemental Arrows-B (2900297F)
        Miscellaneous Mathematical Symbols-B (298029FF)
        Supplemental Mathematical Operators (2A002AFF)
        Miscellaneous Symbols and Arrows (2B002BFF)

    // See the MarkersDelimiters.cs file.
    // Don't exclude any characters in the Basic
    // Multilingual Plane except these Dingbat characters
    // which are used as markers or delimiters.

    //    Dingbats (270027BF)

    // for( int Count = 0x2700; Count < 0x27BF; Count++ )
      // ShowStatusAsync( Count.ToString( "X2" ) + ") " + Char.ToString( (char)Count ));

    // for( int Count = 128; Count < 256; Count++ )
      // ShowStatusAsync( "      case (int)'" + Char.ToString( (char)Count ) + "': return " + Count.ToString( "X4" ) + ";" );


    // for( int Count = 32; Count < 256; Count++ )
      // ShowStatusAsync( "    CharacterArray[" + Count.ToString() + "] = '" + Char.ToString( (char)Count ) + "';  //  0x" + Count.ToString( "X2" ) );

     // &#147;

    // ShowStatusAsync( " " );
    */

    int getVal = 191;
    showStatusAsync( "Character: " + (char)getVal );
    }



  }
