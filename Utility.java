// Copyright Eric Chauvin 2019 - 2020.


  public class Utility
  {

  public static StrA getFileName( StrA path,
                                  char delimit )
    {
    path = path.trim();

    if( path.length() == 0 )
      return new StrA( "" );

    StrArray parts = path.splitChar( delimit ); 
    int last = parts.length();
    if( last == 0 )
      return new StrA( "" );

    for( int count = last - 1; count >= 0; count-- )
      {
      StrA fileName = parts.getStrAt( count );
      if( fileName.length() != 0 )
        return fileName;

      }

    return new StrA( "" );
    }



  public static byte[] resizeByteArraySmaller(
                                       byte[] in,
                                       int newSize )
    {
    if( newSize >= in.length )
      return in;

    byte[] newArray = new byte[newSize];
    for( int count = 0; count < newSize; count++ )
      newArray[count] = in[count];

    return newArray;
    }



  public static byte[] resizeByteArrayBigger(
                                       byte[] in,
                                       int sizeToAdd )
    {
    byte[] newArray = new byte[in.length + sizeToAdd];
    int max = in.length;
    for( int count = 0; count < max; count++ )
      newArray[count] = in[count];

    return newArray;
    }


/*
  public static int[] ResizeIntArray( int[] in, int sizeToAdd )
    {
    int[] newArray = new int[in.length + sizeToAdd];
    int max = in.length;
    for( int count = 0; count < max; count++ )
      newArray[count] = in[count];

    return newArray;
    }
*/




  // Java doesn't have unsigned values so the ByteToShort()
  // ShortToByte() functions are needed to make things work
  // right in Java.

  public static short ByteToShort( byte In )
    {
    short Result = (short)(In & 0x7F);

    if( (In & 0x80) == 0x80 )
      Result |= 0x80;

    return Result;
    }





  }
