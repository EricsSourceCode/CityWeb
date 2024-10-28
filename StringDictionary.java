// Copyright Eric Chauvin 2020 - 2021.



public class StringDictionary
  {
  private MainApp mApp;
  private StringDictionaryLine lineArray[];
  private final int keySize = 127 * 127;



  private StringDictionary()
    {
    }



  public StringDictionary( MainApp useApp )
    {
    mApp = useApp;
    lineArray = new StringDictionaryLine[keySize];
    }



  public void clear()
    {
    for( int count = 0; count < keySize; count++ )
      lineArray[count] = null;

    }



  private int getCharIndex( char letter )
    {
    // letter = Character.toLowerCase( letter );
    int index = letter - ' ';
    if( index < 0 )
      index = 0;

    if( index > 126 )
      index = 126;

    return index;
    }


  private int getIndex( String word )
    {
    if( word == null )
      return 0;

    if( word.length() < 1 )
      return 0;

    if( word.length() < 2 )
      word = word + " ";

    char letter1 = word.charAt( 0 );
    char letter2 = word.charAt( 1 );

    int index = getCharIndex( letter1 );
    int index2 = getCharIndex( letter2 );
    index = index << 7;
    index = index | index2;

    if( index < 0 )
      index = 0;

    if( index >= keySize )
      index = keySize - 1;

    return index;
    }


  public void setValue( String key )
    {
    try
    {
    int index = getIndex( key );

    if( lineArray[index] == null )
      lineArray[index] = new StringDictionaryLine();

    lineArray[index].setValue( key );
    }
    catch( Exception e )
      {
      mApp.showStatusAsync( "Exception in setValue()." );
      mApp.showStatusAsync( e.getMessage() );
      }
    }



  public int getCount( String key )
    {
    int index = getIndex( key );
    if( lineArray[index] == null )
      return 0;

    return lineArray[index].getCount( key );
    }


/*
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
*/


/*
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
*/


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
      mApp.showStatusAsync( "Exception in StringDictionary.makeKeysValuesString():\n" );
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


  public boolean keyExists( String key )
    {
    int index = getIndex( key );
    if( lineArray[index] == null )
      return false;

    return lineArray[index].keyExists( key );
    }


  public StringArrayByCount makeStringArrayByCount()
    {
    try
    {
    StringArrayByCount saByCount = new
            StringArrayByCount( mApp );

    for( int count = 0; count < keySize; count++ )
      {
      if( lineArray[count] == null )
        continue;

      lineArray[count].makeStringArrayByCount(
                                     saByCount );
      }

    return saByCount;
    }
    catch( Exception e )
      {
      mApp.showStatusAsync( "Exception in StringDictionary.makeStringArrayByCount():\n" );
      mApp.showStatusAsync( e.getMessage() );
      return null;
      }
    }


  }
