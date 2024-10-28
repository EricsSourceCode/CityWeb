// Copyright Eric Chauvin 2020 - 2021.



public class StringArray
  {
  private String[] valueArray;
  private int[] sortIndexArray;
  private int arrayLast = 0;


  public StringArray()
    {
    valueArray = new String[8];
    sortIndexArray = new int[8];
    resetSortIndexArray();
    }


  public void clear()
    {
    arrayLast = 0;
    }



  public int length()
    {
    return arrayLast;
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

    String[] tempValueArray = new String[oldLength + toAdd];
    for( int count = 0; count < arrayLast; count++ )
      {
      tempValueArray[count] = valueArray[count];
      }

    valueArray = tempValueArray;
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
      // compareTo() uses case.
      if( valueArray[sortIndexArray[count]].
                             compareToIgnoreCase(
         valueArray[sortIndexArray[count + 1]] ) > 0 )
        {
        int temp = sortIndexArray[count];
        sortIndexArray[count] = sortIndexArray[count + 1];
        sortIndexArray[count + 1] = temp;
        switched = true;
        }
      }

    return switched;
    }



  public void append( String value )
    {
    if( arrayLast >= sortIndexArray.length )
      resizeArrays( 1024 * 64 );

    valueArray[arrayLast] = value;
    arrayLast++;
    }



  // Gets it in sorted order.
  public String getStringAt( int where )
    {
    if( where < 0 )
      return "";

    if( where >= arrayLast )
      return "";

    return valueArray[sortIndexArray[where]];
    }


  }
