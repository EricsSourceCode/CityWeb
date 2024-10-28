// Copyright Eric Chauvin 2020 - 2024.



// This is licensed under the GNU General
// Public License (GPL).  It is the
// same license that Linux has.
// https://www.gnu.org/licenses/gpl-3.0.html



// Some programs add a byte order mark at the start
// of a file even though they shouldn't.  They are
// the three bytes: 0xEF, 0xBB, 0xBF
// Those three bytes encode the one character that
// is the Byte Order Mark.

// http://en.wikipedia.org/wiki/Unicode
// http://en.wikipedia.org/wiki/UTF-8
// http://en.wikipedia.org/wiki/Basic_Multilingual_Plane




public class UTF8StrA
  {
  public final static char HighSurrogate = 0xD800;


  public static void doTest( MainApp mApp )
    {
    StrABld sBld = new StrABld( 1024 * 64);

    // Byte order mark: EF BB BF

    // Markers.ErrorPoint is 0x2708.
    for( int count = 1; count < 0xD800; count++ )
      {
      char c = (char)count;
      sBld.appendChar( c );

      if( (count > 127) && (count < 256))
        mApp.showStatusAsync( "" + count + ") " + c );

      }

    StrA testS = sBld.toStrA();
    final int max = testS.length();

    byte[] buf = strAToBytes( testS );

    StrA result = bytesToStrA( mApp, buf,
                     1000000000, new StrA( "Test" ));

    // for( int count = 0; count < max; count++ )
      // {
      // if( result.charAt( count ) !=

    if( result.length() != testS.length())
      {
      mApp.showStatusAsync( "Not the same length." );
      return;
      }

    if( result.equalTo( testS ))
      mApp.showStatusAsync( "It passed the test." );
    else
      mApp.showStatusAsync( "It failed the test." );

    }




  public static byte[] strAToBytes( StrA in )
    {
    if( in == null )
      return null;

    final int max = in.length();

    if( max == 0 )
      return null;

    byte[] result;
    try
    {
    result = new byte[max + (1024 * 64)];
    }
    catch( Exception e )
      {
      return null;
      }

    int where = 0;
    for( int count = 0; count < max; count++ )
      {
      char fullChar = in.charAt( count );
      // Two 16 bits values make up the whole
      // character for these surrogate values.
      // The high 16 bits and the low 16 bits.
      // High surrogate is 0xD800 to 0xDBFF.
      // Low surrogate is 0xDC00 to 0xDFFF.
      if( fullChar >= 0xD800 )
        fullChar = ' ';

// From Wikipedia:
// 1 U+0000 U+007F 0xxxxxxx
// 2 U+0080 U+07FF 110xxxxx 10xxxxxx
// 3 U+0800 U+FFFF 1110xxxx 10xxxxxx 10xxxxxx
// 4 U+10000 U+10FFFF[18] 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx

      if( fullChar <= 0x7F )
        {
        // Regular ASCII.
        if( where >= result.length )
          {
          result = Utility.resizeByteArrayBigger(
                                          result,
                                          1024 * 64 );
          }

        result[where] = (byte)fullChar;
        where++;
        continue;
        }


// 2 U+0080 U+07FF 110xxxxx 10xxxxxx

      if( (fullChar >= 0x80) && (fullChar <= 0x7FF) )
        {
        // Bottom 6 bits.
        byte smallByte = (byte)(fullChar & 0x3F);
        // Big 5 bits.
        byte bigByte = (byte)((fullChar >> 6) & 0x1F);

        bigByte |= (byte)0xC0; // Mark it as the beginning byte.
        smallByte |= (byte)0x80; // Mark it as a continuing byte.

        if( (where + 2) >= result.length )
          {
          result = Utility.resizeByteArrayBigger( result,
                                          1024 * 64 );
          }

        result[where] = bigByte;
        where++;
        result[where] = smallByte;
        where++;
        }


// This would encode things like High Surrogate
// values like any other character.

// 3 U+0800 U+FFFF 1110xxxx 10xxxxxx 10xxxxxx
      if( fullChar >= 0x800 ) // && (fullChar <= 0xFFFF  )
        {
        byte byte3 = (byte)(fullChar & 0x3F);
        byte byte2 = (byte)((fullChar >> 6) & 0x3F);
        byte bigByte = (byte)((fullChar >> 12) & 0x0F); // Biggest 4 bits.

        bigByte |= (byte)0xE0; // Mark it as the beginning byte.
        byte2 |= (byte)0x80; // Mark it as a continuing byte.
        byte3 |= (byte)0x80; // Mark it as a continuing byte.

        if( (where + 3) >= result.length )
          {
          result = Utility.resizeByteArrayBigger( result,
                                          1024 * 64 );
          }

        result[where] = bigByte;
        where++;
        result[where] = byte2;
        where++;
        result[where] = byte3;
        where++;
        }
      }

    return Utility.resizeByteArraySmaller( result,
                                             where );
    }




  public static StrA bytesToStrA( MainApp mApp,
                                  byte[] in,
                                  int maxLen,
                                  StrA showUrl )
    {
    try
    {
    if( in == null )
      return StrA.Empty;

    if( in.length == 0 )
      return StrA.Empty;

    if( in[0] == 0 )
      return StrA.Empty;

    if( maxLen > in.length )
      maxLen = in.length;

    final int last = maxLen;
    StrABld sBld = new StrABld( last + (1024 * 8));
    boolean isInside = false;
    for( int count = 0; count < last; count++ )
      {
      // This character is from a byte, so it can't
      // be more than 0xFF.
      char c = (char)in[count];
      if( (c & 0x80) == 0 )
        {
        // It is regular ASCII.
        if( isInside )
          sBld.appendChar( Markers.End );

        isInside = false;
        sBld.appendChar( c );
        continue;
        }

      // If it is a beginning byte.
      if( (c & 0xC0) == 0xC0 )
        {
        if( isInside )
          {
          // There can be a sequence of high bytes
          // with no ASCII characters in between.
          sBld.appendChar( Markers.End );
          sBld.appendChar( Markers.Begin );
          }
        }

      if( !isInside )
        {
        sBld.appendChar( Markers.Begin );
        isInside = true;
        }

      sBld.appendChar( c );
      }

    if( isInside )
      sBld.appendChar( Markers.End );

    StrA markedS = sBld.toStrA();
    return convertMarked( mApp, markedS, showUrl );
    }
    catch( Exception e )
      {
      mApp.showStatusAsync( "Exception in UTF8StrA.bytesToStrA(): " + e.getMessage() );
      return StrA.Empty;
      }
    }



// From Wikipedia:
// 1 U+0000 U+007F 0xxxxxxx
// 2 U+0080 U+07FF 110xxxxx 10xxxxxx
// 3 U+0800 U+FFFF 1110xxxx 10xxxxxx 10xxxxxx
// 4 U+10000 U+10FFFF[18] 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx

  private static StrA convertMarked( MainApp mApp,
                                     StrA in,
                                     StrA showUrl )
    {
    if( in.length() == 0 )
      return StrA.Empty;

    StrArray splitS = in.splitChar( Markers.Begin );
    final int last = splitS.length();
    StrABld sBld = new StrABld( in.length());

    // Add what's before the first Begin marker.
    StrA lowChars = splitS.getStrAt( 0 );
    // Remove the end marker.
    lowChars = Markers.removeAllMarkers( lowChars );
    sBld.appendStrA( lowChars );

    for( int count = 1; count < last; count++ )
      {
      StrA line = splitS.getStrAt( count );
      if( 1 != Markers.countMarkers( line ))
        {
        mApp.showStatusAsync( "The number of markers is not one." );
        return StrA.Empty;
        }

      StrArray splitLine = line.splitChar(
                                       Markers.End );

      StrA highChars = splitLine.getStrAt( 0 );
      // mApp.showStatusAsync( "highChars: " + highChars );

      final int hLength = highChars.length();

      if( hLength == 0 )
        {
        mApp.showStatusAsync( "Sequence was length zero." );
        highChars = StrA.Empty;
        }

      lowChars = StrA.Empty;
      if( splitLine.length() > 1 )
        lowChars = splitLine.getStrAt( 1 );

      char fullChar = sequenceToChar( mApp, highChars, showUrl );
      sBld.appendChar( fullChar );

      sBld.appendStrA( lowChars );
      }

    return sBld.toStrA();
    }



  private static char sequenceToChar( MainApp mApp,
                                      StrA in,
                                      StrA showUrl )
    {
    //  7   U+007F   0xxxxxxx
    // 11   U+07FF   110xxxxx   10xxxxxx
    // 16   U+FFFF   1110xxxx   10xxxxxx   10xxxxxx
    // 4 bytes       11110xxx  10xxxxxx  10xxxxxx  10xxxxxx

    // A 1010
    // B 1011
    // C 1100
    // D 1101
    // E 1110
    // F 1111

    final int max = in.length();
    if( max < 2 )
      {
      // reforma is not utf8.
      // mApp.showStatusAsync( "Single character: " +
      //                    in + "  from: " + showUrl );
      return '_';
      }

    char first = in.charAt( 0 );
    // A beginning byte is either 110xxxxx or
    // 1110xxxx or 11110xxx.
    if( (first & 0xC0) != 0xC0 )
      {
      mApp.showStatusAsync( "First is not a beginning byte." );
      return '_';
      }

    char second = in.charAt( 1 );
    // It should be a continuing byte.
    if( (second & 0xC0) != 0x80 )
      {
      mApp.showStatusAsync( "Second is not a continuing byte." );
      return '_';
      }

    // Two bytes.
    if( (first & 0xE0) == 0xC0 )
      {
      // It is a 2 byte sequence.
      // Starts with 110xxxxx.
      if( max != 2 )
        {
        mApp.showStatusAsync( "max != 2: " + in +
                                "  from: " + showUrl );
        return '_';
        }

      char fullChar = (char)(first & 0x1F);
      fullChar = (char)(fullChar << 6);
      char byte2 = (char)(second & 0x3F);
      fullChar |= byte2;
      // fullChar is <= 0x07FF.
      // if( fullChar >= 0xD800 ) // High Surrogates

      // mApp.showStatusAsync( "FullChar: " + fullChar +
      //                                      "   " +
      //           Integer.toHexString( (int)fullChar ));

      return fullChar;
      }

    // Three bytes.
    if( (first & 0xF0) == 0xE0 )
      {
      // It is a 3 byte sequence.
      if( max != 3 )
        {
        mApp.showStatusAsync( "max != 3: " + in +
                               "  from: " + showUrl );

        return '_';
        }

      char third = in.charAt( 2 );
      if( (third & 0xC0) != 0x80 )
        {
        mApp.showStatusAsync( "third is not a continuing byte." );
        return '_';
        }

      // Byte order mark: EF BB BF
      if( (first == 0xEF) &&
          (second == 0xBB) &&
          (third == 0xBF))
        {
        mApp.showStatusAsync( "Byte order mark from: " +
                                             showUrl );

        return '_';
        }

      char fullChar = (char)(first & 0x0F);
      char byte2 = (char)(second & 0x3F);
      char byte3 = (char)(third & 0x3F);
      fullChar = (char)(fullChar << 12);
      fullChar |= (char)(byte2 << 6);
      fullChar |= byte3;

      if( fullChar >= 0xD800 ) // High Surrogates
        {
        // mApp.showStatusAsync( "fullChar >= 0xD800." );
        return '_';
        }


      // if( !Markers.isMarker( fullChar ))
        // {
        // mApp.showStatusAsync( "FullChar: " + fullChar +
        //           "   " +
        //           Integer.toHexString( (int)fullChar ));
        // }

      return fullChar;
      }

    // Four bytes.
    if( (first & 0xF8) == 0xF0 )
      {
      // This doesn't convert the longer unusual 4
      // byte sequences.  It just does the Basic
      // Multilingual Plane.

      if( max != 4 )
        {
        mApp.showStatusAsync( "max != 4. max is: " +
                             max + " " + in +
                             "  from: " + showUrl );
        return '_';
        }

      mApp.showStatusAsync( "It was four bytes: " +
                          in + "  from: " + showUrl );
      return '_';
      }

    mApp.showStatusAsync( "It was none of the above " +
                          in + "  from: " + showUrl );

    return '_';
    }




  }
