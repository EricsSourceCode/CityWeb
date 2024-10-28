// Copyright Eric Chauvin 2020 - 2021.



public class StrABld
  {
  char[] values;
  int valuesLast = 0;


  private StrABld()
    {
    }


  public StrABld( int howLong )
    {
    // Test:
    // values = new char[2];
    values = new char[howLong];
    }


  public int length()
    {
    return valuesLast;
    }



  public void clear()
    {
    valuesLast = 0;
    }



  private void resizeArray( int toAdd )
    {
    int max = values.length;
    char[] tempValues = new char[max + toAdd];
    for( int count = 0; count < valuesLast; count++ )
      {
      tempValues[count] = values[count];
      }

    values = tempValues;
    }



  public void appendChar( char in )
    {
    if( valuesLast >= values.length )
      resizeArray( 1024 * 4 );

    values[valuesLast] = in;
    valuesLast++;
    }


  public void appendArray( char[] in )
    {
    if( in == null )
      return;

    int last = in.length;
    if( last == 0 )
      return;

    for( int count = 0; count < last; count++ )
      appendChar( in[count] );

    }



  public void appendStrA( StrA in )
    {
    if( in == null )
      return;

    int last = in.length();
    if( last == 0 )
      return;

    for( int count = 0; count < last; count++ )
      appendChar( in.charAt( count ));

    }




  public StrA toStrA()
    {
    if( valuesLast == 0 )
      return StrA.Empty;

    char[] result = new char[valuesLast];
    for( int count = 0; count < valuesLast; count++ )
      result[count] = values[count];

    return new StrA( result );
    }



  public StrA getReverse()
    {
    char[] result = new char[valuesLast];
    int where = 0;
    for( int count = valuesLast - 1; count >= 0; count-- )
      {
      result[where] = values[count];
      where++;
      }

    return new StrA( result );
    }


  public char charAt( int where )
    {
    if( where < 0 )
      return Markers.ErrorPoint;

    if( where >= valuesLast )
      return Markers.ErrorPoint;

    return values[where];
    }


  }
