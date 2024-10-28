// Copyright Eric Chauvin 2020 - 2021.



public class CharDictionaryLine
  {
  private char[] keyArray;
  private int[] countArray;
  private int[] sortIndexArray;
  private int arrayLast = 0;



  public CharDictionaryLine()
    {
    keyArray = new char[8];
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
    int max = sortIndexArray.length;
    sortIndexArray = new int[max + toAdd];
    resetSortIndexArray();

    char[] tempKeyArray = new char[max + toAdd];
    int[] tempCountArray = new int[max + toAdd];

    for( int count = 0; count < arrayLast; count++ )
      {
      tempKeyArray[count] = keyArray[count];
      tempCountArray[count] = countArray[count];
      }

    keyArray = tempKeyArray;
    countArray = tempCountArray;
    }



  public void sort()
    {
    if( arrayLast < 2 )
      return;

    for( int count = 0; count < arrayLast; count++ )
      {
      if( !bubbleSortOnePass() )
        break;

      }
    }



  private boolean bubbleSortOnePass()
    {
    // This returns true if it swaps anything.

    boolean switched = false;
    for( int count = 0; count < (arrayLast - 1); count++ )
      {
      if( keyArray[sortIndexArray[count]] >
            keyArray[sortIndexArray[count + 1]] )
        {
        int temp = sortIndexArray[count];
        sortIndexArray[count] = sortIndexArray[count + 1];
        sortIndexArray[count + 1] = temp;
        switched = true;
        }
      }

    return switched;
    }



  public void sortByCount()
    {
    if( arrayLast < 2 )
      return;

    for( int count = 0; count < arrayLast; count++ )
      {
      if( !bubbleSortByCountOnePass() )
        break;

      }
    }



  private boolean bubbleSortByCountOnePass()
    {
    // This returns true if it swaps anything.

    boolean switched = false;
    for( int count = 0; count < (arrayLast - 1); count++ )
      {
      if( countArray[sortIndexArray[count]] >
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




  private int getIndexOfKey( char key )
    {
    if( arrayLast < 1 )
      return -1;

    for( int count = 0; count < arrayLast; count++ )
      {
      if( keyArray[count] == key )
        return count;

      }

    return -1;
    }



  public void setValue( char key )
    {
    int index = getIndexOfKey( key );
    if( index >= 0 )
      {
      // keyArray[index] = value;
      countArray[index] = countArray[index] + 1;
      }
    else
      {
      if( arrayLast >= sortIndexArray.length )
        resizeArrays( 1024 * 4 );

      keyArray[arrayLast] = key;
      countArray[arrayLast] = 1;
      arrayLast++;
      }
    }



  public int getCount( char key )
    {
    int index = getIndexOfKey( key );
    if( index < 0 )
      return 0;

    return countArray[index];
    }




  public boolean keyExists( char key )
    {
    int index = getIndexOfKey( key );
    if( index < 0 )
      return false;

    return true;
    }




  public StrA makeKeysValuesStrA()
    {
    if( arrayLast < 1 )
      return StrA.Empty;

    StrABld sBuilder = new StrABld( 1024 );

    for( int count = 0; count < arrayLast; count++ )
      {
      if( count > 500 )
        break;

      // Using the sortIndexArray for the sorted order.
      StrA oneLine = new StrA( "" +
              keyArray[sortIndexArray[count]] );

      oneLine = oneLine.concat( new StrA( ";" ));
      oneLine = oneLine.concat( new StrA( "" +
                countArray[sortIndexArray[count]] ));


      int charValue = keyArray[sortIndexArray[count]];
      oneLine = oneLine.concat( new StrA(
                                " ;" + charValue ));

      oneLine = oneLine.concat( new StrA( "\n" ));

      sBuilder.appendStrA( oneLine );
      }

    return sBuilder.toStrA();
    }



  }
