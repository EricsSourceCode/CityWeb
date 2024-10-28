// Copyright Eric Chauvin 2020 - 2021.



public class StringArrayByCount
  {
  private MainApp mApp;
  private StringArray strArray[];
  private int[] countArray;
  private int[] sortIndexArray;
  private int arrayLast = 0;


  private StringArrayByCount()
    {
    }


  public StringArrayByCount( MainApp useApp )
    {
    mApp = useApp;
    strArray = new StringArray[8];
    countArray = new int[8];
    sortIndexArray = new int[8];
    resetSortIndexArray();
    }


  private void resetSortIndexArray()
    {
    // It's not to arrayLast.  It's to the whole length.
    final int max = sortIndexArray.length;
    for( int count = 0; count < max; count++ )
      sortIndexArray[count] = count;

    }



  private void resizeArrays( int toAdd )
    {
    final int oldLength = sortIndexArray.length;
    sortIndexArray = new int[oldLength + toAdd];
    resetSortIndexArray();

    int[] tempCountArray = new
                          int[oldLength + toAdd];

    StringArray[] tempStrArray = new
                  StringArray[oldLength + toAdd];

    for( int count = 0; count < arrayLast; count++ )
      {
      tempCountArray[count] = countArray[count];
      tempStrArray[count] = strArray[count];
      }

    countArray = tempCountArray;
    strArray = tempStrArray;
    }



  public void sort()
    {
    if( arrayLast < 2 )
      return;

    final int max = arrayLast * 2;
    for( int count = 0; count < max; count++ )
      {
      if( !bubbleSortOnePass() )
        break;

      }
    }



  private boolean bubbleSortOnePass()
    {
    // This returns true if it swaps anything.

    boolean switched = false;
    final int last = arrayLast - 1;
    for( int count = 0; count < last; count++ )
      {
      if( countArray[sortIndexArray[count]] <
          countArray[sortIndexArray[count + 1]] )
        {
        int temp = sortIndexArray[count];
        sortIndexArray[count] = sortIndexArray[count + 1];
        sortIndexArray[count + 1] = temp;
        switched = true;
        }
      }

    return switched;
    }


  private int getIndexOfCount( int howMany )
    {
    if( arrayLast < 1 )
      return -1;

    for( int count = 0; count < arrayLast; count++ )
      {
      if( countArray[count] == howMany )
        return count;

      }

    return -1;
    }



  public void showKeysValuesStrings()
    {
    try
    {
    // Not here.
    // sort();

    mApp.showStatusAsync( "\nAll Words:" );

    StringBuilder sBuilder = new StringBuilder();

    for( int count = 0; count < arrayLast; count++ )
      {
      if( strArray[sortIndexArray[count]] == null )
        continue;

      int howMany = countArray[
                          sortIndexArray[count]];

      if( howMany < 10 )
        continue;

      mApp.showStatusAsync( "\nHowMany: " +
                                        howMany );

      int max = strArray[sortIndexArray[count]].
                                        length();
      for( int sCount = 0; sCount < max; sCount++ )
        {
        // if( sCount > 5 )
          // break;

        mApp.showStatusAsync( strArray[sortIndexArray[count]].
                           getStringAt( sCount ));

        }
      }

    mApp.showStatusAsync( "\n\n" );
    }
    catch( Exception e )
      {
      mApp.showStatusAsync( "Exception in StringArrayByCount.makeKeysValuesString():\n" );
      mApp.showStatusAsync( e.getMessage() );
      // return "";
      }
    }



  public void addStringWithCount( String toAdd,
                                  int howMany )
    {
    int where = getIndexOfCount( howMany );
    if( where >= 0 )
      {
      strArray[where].append( toAdd );
      return;
      }

    if( arrayLast >= sortIndexArray.length )
      resizeArrays( 1024 * 1 );

    strArray[arrayLast] = new StringArray();
    strArray[arrayLast].append( toAdd );
    countArray[arrayLast] = howMany;
    arrayLast++;
    }


/*
  // Gets it in sorted order.
  public StringArray getStringArrayAt( int where )
    {
    if( where < 0 )
      return null;

    if( where >= arrayLast )
      return null;

    return strArray[sortIndexArray[where]];
    }
*/

/*
  public int getCountAt( int where )
    {
    if( where < 0 )
      return 0;

    if( where >= arrayLast )
      return 0;

    return countArray[sortIndexArray[where]];
    }
*/


  }
