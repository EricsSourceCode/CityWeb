// Copyright Eric Chauvin 2021 - 2024.



// This is licensed under the GNU General
// Public License (GPL).  It is the
// same license that Linux has.
// https://www.gnu.org/licenses/gpl-3.0.html





public class UTF8Strings
  {


  protected static byte[] StringToBytes( String InString )
    {
    try
    {
    if( InString == null )
      return null;

    if( InString.length() == 0 )
      return null;

    // UTF-16 is "either one or two 16-bit code _units_ per code point.
    // One Char in a Windows string is a code unit.

    // But "All code points in the BMP are accessed as a single code unit
    // in UTF-16 encoding and can be encoded in one, two or three bytes in
    // UTF-8".

    // Bits
    //  7  U+007F  0xxxxxxx
    // 11  U+07FF  110xxxxx  10xxxxxx
    // 16  U+FFFF  1110xxxx  10xxxxxx  10xxxxxx

    // 21  U+1FFFFF  11110xxx  10xxxxxx  10xxxxxx  10xxxxxx
    // 26  U+3FFFFFF  111110xx  10xxxxxx  10xxxxxx  10xxxxxx  10xxxxxx
    // 31  U+7FFFFFFF  1111110x  10xxxxxx  10xxxxxx  10xxxxxx  10xxxxxx  10x

    // try
    byte[] Result = new byte[InString.length() * 3];

    int Where = 0;
    for( int Count = 0; Count < InString.length(); Count++ )
      {
      char Character = InString.charAt( Count );
      if( Character <= 0x7F )
        {
        // Regular ASCII.
        Result[Where] = (byte)Character;
        Where++;
        continue;
        }

      if( Character >= 0xD800 ) // High Surrogates
        {
        break;
        // Where++;
        // continue;
        }

      // "the first byte unambiguously indicates the length of the
      // sequence in bytes."
      // "All continuation bytes (byte nos. 2 - 6 in the table above) have
      // 10 as their two most-significant bits."


      //  7  U+007F  0xxxxxxx
      // 11  U+07FF  110xxxxx  10xxxxxx
      // 16  U+FFFF  1110xxxx  10xxxxxx  10xxxxxx
      if( (Character > 0x7F) && (Character <= 0x7FF) )
        {
        // Notice that this conversion from characters to bytes
        // doesn't involve characters over 0x7F.
        byte SmallByte = (byte)(Character & 0x3F); // Bottom 6 bits.
        byte BigByte = (byte)((Character >> 6) & 0x1F); // Big 5 bits.

        // Notice I'm changing these bytes in to negative numbers in Java.
        BigByte |= (byte)0xC0; // Mark it as the beginning byte.
        SmallByte |= (byte)0x80; // Mark it as a continuing byte.
        Result[Where] = BigByte;
        Where++;
        Result[Where] = SmallByte;
        Where++;
        }


      // 16  U+FFFF  1110xxxx  10xxxxxx  10xxxxxx
      if( Character > 0x7FF ) // && (Character < 0xD800) )
        {
        byte Byte3 = (byte)(Character & 0x3F); // Bottom 6 bits.
        byte Byte2 = (byte)((Character >> 6) & 0x3F); // Next 6 bits.
        byte BigByte = (byte)((Character >> 12) & 0x0F); // Biggest 4 bits.

        // Notice I'm changing these bytes in to negative numbers in Java.
        BigByte |= (byte)0xE0; // Mark it as the beginning byte.
        Byte2 |= (byte)0x80; // Mark it as a continuing byte.
        Byte3 |= (byte)0x80; // Mark it as a continuing byte.
        Result[Where] = BigByte;
        Where++;
        Result[Where] = Byte2;
        Where++;
        Result[Where] = Byte3;
        Where++;
        }
      }

    byte[] RealResult = new byte[Where];
    for( int Count = 0; Count < Where; Count++ )
      RealResult[Count] = Result[Count];

    return RealResult;

    }
    catch( Exception e )
      {
      // RadNetApp.StrKeeper.AppendLine( "Error in StringToBytes()." );
      // RadNetApp.StrKeeper.AppendLine( e.getMessage() );
      return null;
      }
    }





  protected static String BytesToString( byte[] InBytes, int MaxLen )
    {
    try
    {
    if( InBytes == null )
      return "";

    if( InBytes.length == 0 )
      return "";

    if( MaxLen > InBytes.length )
      MaxLen = InBytes.length;

    // The constructor has a "suggested capacity" value to start with.
    StringBuilder SBuilder = new StringBuilder( MaxLen );
    // for( int Count = 0; Count < InBytes.Length; Count++ )
    for( int Count = 0; Count < MaxLen; Count++ )
      {
      if( (InBytes[Count] & 0x80) == 0 )
        {
        // It's regular ASCII.
        SBuilder.append( (char)InBytes[Count] );
        continue;
        }

      if( (InBytes[Count] & 0xC0) == 0x80 )
        {
        // It's a continuing byte which was dealt with already below, so just
        // skip past it.
        continue;
        }

      if( (InBytes[Count] & 0xC0) == 0xC0 )
        {
        // It's a beginning byte.
        // A beginning byte is either 110xxxxx or 1110xxxx.
        if( (InBytes[Count] & 0xF0) == 0xE0 )
          {
          // Starts with 1110xxxx.
          // It's a 3-byte character.
          if( (Count + 2) >= MaxLen )
            break; // Ignore the garbage.

          char BigByte = (char)(InBytes[Count] & 0x0F); // Biggest 4 bits.
          char Byte2 = (char)(InBytes[Count + 1] & 0x3F); // Next 6 bits.
          char Byte3 = (char)(InBytes[Count + 2] & 0x3F); // Next 6 bits.

          // A char is unsigned so this should not make it negative.
          char Character = (char)(BigByte << 12);
          Character |= (char)(Byte2 << 6);
          Character |= Byte3;

          if( Character < 0xD800 ) // High Surrogates
            SBuilder.append( Character );

          }

        if( (InBytes[Count] & 0xE0) == 0xC0 )
          {
          // Starts with 110xxxxx.
          // It's a 2-byte character.
          if( (Count + 1) >= MaxLen )
            break; // Ignore the garbage.

          char BigByte = (char)(InBytes[Count] & 0x1F); // Biggest 5 bits.
          char Byte2 = (char)(InBytes[Count + 1] & 0x3F); // Next 6 bits.

          char Character = (char)(BigByte << 6);
          Character |= Byte2;
          if( Character < 0xD800 ) // High Surrogates
            SBuilder.append( Character );

          }

        // If it doesn't match the two above it gets ignored.
        }
      }

    return SBuilder.toString();

    }
    catch( Exception e )
      {
      return "Error in BytesToString(). " + e.getMessage();
      }
    }




  }
