// Copyright Eric Chauvin 2020 - 2021.



// This is for all characters that get used,
// like in Spanish there is the upside down
// exclamation mark and other non-ASCII
// characters.



public class CharDictionary
  {
  private MainApp mApp;
  private CharDictionaryLine lineArray[];
  private final int keySize = 2; // 129 - ' ';



  private CharDictionary()
    {
    }



  public CharDictionary( MainApp useApp )
    {
    mApp = useApp;
    lineArray = new CharDictionaryLine[keySize];
    }



  public void clear()
    {
    for( int count = 0; count < keySize; count++ )
      lineArray[count] = null;

    }



  private int getIndex( char letter )
    {
    // letter = Character.toLowerCase( letter );
    int index = letter - ' ';
    if( index < 0 )
      index = 0;

    if( index >= keySize )
      index = keySize - 1;

    return index;
    }



  public void setValue( char key )
    {
    try
    {
    int index = getIndex( key );

    if( lineArray[index] == null )
      lineArray[index] = new CharDictionaryLine();

    lineArray[index].setValue( key );
    }
    catch( Exception e )
      {
      mApp.showStatusAsync( "Exception in setValue()." );
      mApp.showStatusAsync( e.getMessage() );
      }
    }



  public int getCount( char key )
    {
    int index = getIndex( key );
    if( lineArray[index] == null )
      return ' ';

    return lineArray[index].getCount( key );
    }



  public void sort()
    {
    // This is a Library Sort mixed with a Bubble
    // Sort for each line in the array.
    for( int count = 0; count < keySize; count++ )
      {
      if( lineArray[count] == null )
        continue;

      lineArray[count].sort();
      }
    }



  public void sortByCount()
    {
    // This is a Library Sort mixed with a Bubble
    // Sort for each line in the array.
    for( int count = 0; count < keySize; count++ )
      {
      if( lineArray[count] == null )
        continue;

      lineArray[count].sortByCount();
      }
    }


  public StrA makeKeysValuesStrA()
    {
    try
    {
    // Not here.
    // sort();

    StrABld sBuilder = new StrABld( 1024 * 64 );

    for( int count = 0; count < keySize; count++ )
      {
      if( lineArray[count] == null )
        continue;

      sBuilder.appendStrA( new StrA( "\nIndex: " +
                             count + "\n" ));

      sBuilder.appendStrA( lineArray[count].
                            makeKeysValuesStrA());

      }

    return sBuilder.toStrA();
    }
    catch( Exception e )
      {
      mApp.showStatusAsync( "Exception in CharDictionary.makeKeysValuesString():\n" );
      mApp.showStatusAsync( e.getMessage() );
      return StrA.Empty;
      }
    }



  public void writeFile( StrA fileName )
    {
    StrA toWrite = makeKeysValuesStrA();

    FileUtility.writeStrAToFile( mApp,
                                   fileName,
                                   toWrite,
                                   false,
                                   false );
    }


/*
  public void readFile( StrA fileName )
    {
    StrA fileS = FileUtility.readFileToStrA(
                                        mApp,
                                        fileName,
                                        false,
                                        false );

    if( fileS.length() == 0 )
      return;

    StrArray fileLines = fileS.splitChar( '\n' );
    final int last = fileLines.length();
    for( int count = 0; count < last; count++ )
      {
      StrA line = fileLines.getStrAt( count );
      // mApp.showStatusAsync( line );
      StrArray parts = line.splitChar( ';' );
      if( parts.length() < 2 )
        {
        mApp.showStatusAsync( "This line has less than two parts.\n" + line );
        }

      setValue( parts.getStrAt( 0 ), parts.getStrAt( 1 ));
      }
    }
*/


  public boolean keyExists( char key )
    {
    int index = getIndex( key );
    if( lineArray[index] == null )
      return false;

    return lineArray[index].keyExists( key );
    }




  }
