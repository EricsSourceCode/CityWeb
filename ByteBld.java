// Copyright Eric Chauvin 2020.


public class ByteBld
  {
  byte[] values;
  int valuesLast = 0;



  private ByteBld()
    {
    }


  public ByteBld( int howLong )
    {
    values = new byte[howLong];
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
    byte[] tempValues = new byte[max + toAdd];
    for( int count = 0; count < valuesLast; count++ )
      {
      tempValues[count] = values[count];
      }

    values = tempValues;
    }



  public void appendByte( byte in )
    {
    if( valuesLast >= values.length )
      resizeArray( 1024 * 4 );

    values[valuesLast] = in;
    valuesLast++;
    }


  public void appendArray( byte[] in )
    {
    if( in == null )
      return;

    final int last = in.length;
    if( last == 0 )
      return;

    for( int count = 0; count < last; count++ )
      appendByte( in[count] );

    }



  public byte[] toByteArray()
    {
    if( valuesLast == 0 )
      return null;

    byte[] result = new byte[valuesLast];
    for( int count = 0; count < valuesLast; count++ )
      result[count] = values[count];

    return result;
    }




  }
