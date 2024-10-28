// Copyright Eric Chauvin 2020 - 2024.



// This is licensed under the GNU General
// Public License (GPL).  It is the
// same license that Linux has.
// https://www.gnu.org/licenses/gpl-3.0.html


// For Serializable:
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;



public class StrA implements Serializable
  {
  private final char[] values;
  public static final long serialVersionUID = 1;


  public static final StrA Empty =
                                new StrA( "" );


  private StrA()
    {
    values = new char[0];
    }


  public StrA( String in )
    {
    // The length might be zero.
    int last = in.length();

    // values is final so it can be assigned once.
    values = new char[last];
    for( int count = 0; count < last; count++ )
      values[count] = in.charAt( count );

    }



  public StrA( char[] in )
    {
    // Make a copy of the character array in.

    // The length might be zero.
    int last = in.length;

    values = new char[last];
    for( int count = 0; count < last; count++ )
      values[count] = in[count];

    }



  public StrA( StrA in )
    {
    // Make a copy of the character array in.

    // The length might be zero.
    int last = in.values.length;

    values = new char[last];
    for( int count = 0; count < last; count++ )
      values[count] = in.values[count];

    }



  public StrA( StrA in1, StrA in2 )
    {
    int max = in1.values.length + in2.values.length;
    char[] both = new char[max];

    int where = 0;
    int last = in1.values.length;
    for( int count = 0; count < last; count++ )
      {
      both[where] = in1.values[count];
      where++;
      }

    last = in2.values.length;
    for( int count = 0; count < last; count++ )
      {
      both[where] = in2.values[count];
      where++;
      }

    values = both;
    }



  public StrA( StrA in1, StrA in2, StrA in3 )
    {
    int max = in1.values.length + in2.values.length +
                                  in3.values.length;

    char[] allIn = new char[max];

    int where = 0;
    int last = in1.values.length;
    for( int count = 0; count < last; count++ )
      {
      allIn[where] = in1.values[count];
      where++;
      }

    last = in2.values.length;
    for( int count = 0; count < last; count++ )
      {
      allIn[where] = in2.values[count];
      where++;
      }

    last = in3.values.length;
    for( int count = 0; count < last; count++ )
      {
      allIn[where] = in3.values[count];
      where++;
      }

    values = allIn;
    }



  // For Serializable:
  private void writeObject(
                   ObjectOutputStream stream )
                           throws IOException
    {
    stream.defaultWriteObject();
    }

  private void readObject(
                     ObjectInputStream stream )
                     throws IOException,
                     ClassNotFoundException
    {
    stream.defaultReadObject();
    }




  private char[] stringToCharArray( String in )
    {
    if( in == null )
      return new char[0];

    // last can be zero.
    final int last = in.length();
    char[] result = new char[last];
    for( int count = 0; count < last; count++ )
      result[count] = in.charAt( count );

    return result;
    }



  public int length()
    {
    return values.length;
    }



  public char charAt( int where )
    {
    if( where < 0 )
      return Markers.ErrorPoint;

    if( where >= values.length )
      return Markers.ErrorPoint;

    return values[where];
    }



  public char[] getCopyOfValues()
    {
    int last = values.length;
    char[] result = new char[last];
    for( int count = 0; count < last; count++ )
      result[count] = values[count];

    return result;
    }



  public char[] toCharArray()
    {
    return getCopyOfValues();
    }



 /*
  public void testFinal( String in )
    {
    int last = in.length();

    // This causes a compile time error.
    // values = new char[last];

    // This does not cause an error.
    for( int count = 0; count < last; count++ )
      values[count] = in.charAt( count );

    }
*/


  @Override
  public String toString()
    {
    int last = values.length;
    if( last == 0 )
      return "";

    // Can't do:
    // String result = new String( values );

    StringBuilder sBuilder = new StringBuilder();
    // Don't use System.arraycopy() since it's going
    // to be translated in to C++.

    // This has to have the ("" +) in it so it works
    // right with Marker characters.
    for( int count = 0; count < last; count++ )
      sBuilder.append( "" + values[count] );

    return sBuilder.toString();
    }



  private boolean searchTextMatches( int position,
                                     char[] toSearch,
                                     char[] toFind )
    {
    int findLength = toFind.length;
    if( findLength < 1 )
      return false;

    // Count goes from 0 to sLength - 1.
    if( (position + findLength - 1) >= toSearch.length )
      return false;

    for( int count = 0; count < findLength; count++ )
      {
      if( toSearch[position + count] != toFind[count] )
        return false;

      }

    return true;
    }



  public StrA replace( StrA toFind,
                       StrA replaceA )
    {
    if( values.length == 0 )
      return Empty;

    if( toFind.length() == 0 )
      return new StrA( values ); // this

    String testS = toString().replace(
                              toFind.toString(),
                              replaceA.toString() );

    // Replace might be an empty string.
    // if( replace.length() == 0 )

    if( values.length < toFind.length() )
      return  new StrA( values );

    StrABld resultBld = new StrABld( values.length + 2048 );

    char firstChar = toFind.charAt( 0 );
    int last = values.length;
    int skip = 0;
    for( int count = 0; count < last; count++ )
      {
      if( skip > 0 )
        {
        skip--;
        if( skip > 0 )
          continue;

        }

      char testChar = values[count];
      if( testChar != firstChar )
        {
        resultBld.appendChar( testChar );
        continue;
        }

      if( !searchTextMatches( count,
                              values,
                              toFind.values ))
        {
        resultBld.appendChar( testChar );
        continue;
        }

      skip = toFind.length(); //  + 1;
      if( replaceA.length() > 0 )
        resultBld.appendArray( replaceA.values );

      }

    StrA result = resultBld.toStrA();

    String testResult = result.toString();
    if( !testResult.equals( testS ))
      {
      result = new StrA( "StrA Bad string: " + testResult + " and testS " + testS );
      return result;
      }

    return result;
    }



  public StrA replaceChar( char toFind, char replace )
    {
    if( values.length == 0 )
      return Empty;

    int last = values.length;
    char[] toArray = new char[last];
    for( int count = 0; count < last; count++ )
      {
      char testChar = values[count];
      if( testChar == toFind )
        toArray[count] = replace;
      else
        toArray[count] = testChar;

      }

    StrA result = new StrA( toArray );
    return result;
    }




  public StrArray splitChar( char delimit )
    {
    StrArray result = new StrArray();

    if( values.length == 0 )
      return result;

    StrABld sBld = new StrABld( 1024 * 64 );

    final int last = values.length;
    for( int count = 0; count < last; count++ )
      {
      char testChar = values[count];
      if( testChar == delimit )
        {
        result.append( sBld.toStrA());
        sBld.clear();
        }
      else
        {
        sBld.appendChar( testChar );
        }
      }

    if( sBld.length() > 0 )
      result.append( sBld.toStrA());

    return result;
    }



  public StrArray splitStrA( StrA delimit )
    {
    StrArray result = new StrArray();

    if( values.length == 0 )
      return result;

    if( delimit.length() == 0 )
      {
      result.append( new StrA( values )); // this
      return result;
      }

    if( values.length < delimit.length() )
      {
      result.append( new StrA( values )); // this
      return result;
      }

    StrABld sBld = new StrABld( values.length + 2048 );

    char firstChar = delimit.charAt( 0 );
    final int last = values.length;
    int skip = 0;
    for( int count = 0; count < last; count++ )
      {
      if( skip > 0 )
        {
        skip--;
        if( skip > 0 )
          continue;

        }

      char testChar = values[count];
      if( testChar != firstChar )
        {
        sBld.appendChar( testChar );
        continue;
        }

      if( !searchTextMatches( count,
                              values,
                              delimit.values ))
        {
        sBld.appendChar( testChar );
        continue;
        }

      skip = delimit.length();
      result.append( sBld.toStrA() );
      sBld.clear();
      }

    if( sBld.length() > 0 )
      result.append( sBld.toStrA() );

    return result;
    }




  public StrA concat( StrA in )
    {
    if( in.length() == 0 )
      return this; // new StrA( values );

    int max = values.length + in.length();
    char[] both = new char[max];

    int where = 0;
    int last = values.length;
    for( int count = 0; count < last; count++ )
      {
      both[where] = values[count];
      where++;
      }

    last = in.length();
    for( int count = 0; count < last; count++ )
      {
      both[where] = in.charAt( count );
      where++;
      }

    return new StrA( both );
    }



  // Don't use equals() inherited from Object.
  public boolean equalTo( StrA in )
    {
    final int last = values.length;
    if( last != in.values.length )
      return false;

    for( int count = 0; count < last; count++ )
      {
      if( values[count] != in.values[count] )
        return false;

      }

    return true;
    }




  public boolean trimsToZero()
    {
    final int last = values.length;
    if( last == 0 )
      return true;

    for( int count = 0; count < last; count++ )
      {
      char testChar = values[count];
      if( testChar > ' ' )
        return false;

      }

    return true;
    }




  public StrA trimLeft()
    {
    StrABld sBld = new StrABld( values.length + 1024 );
    int last = values.length;
    boolean foundFirst = false;
    for( int count = 0; count < last; count++ )
      {
      char testChar = values[count];
      if( foundFirst )
        {
        sBld.appendChar( testChar );
        continue;
        }

      if( testChar > ' ' )
        {
        foundFirst = true;
        sBld.appendChar( testChar );
        }
      }

    return sBld.toStrA();
    }



  public StrA trimRight()
    {
    StrABld sBld = new StrABld( values.length + 1024 );
    final int last = values.length;
    boolean foundFirst = false;
    for( int count = last - 1; count >= 0; count-- )
      {
      char testChar = values[count];
      if( foundFirst )
        {
        sBld.appendChar( testChar );
        continue;
        }

      if( testChar > ' ' )
        {
        foundFirst = true;
        sBld.appendChar( testChar );
        }
      }

    return sBld.getReverse();
    }



  public StrA trim()
    {
    StrA result = trimRight();
    result = result.trimLeft();
    return result;
    }



  public StrA toLowerCase()
    {
    final int max = values.length;
    char[] lower = new char[max];
    for( int count = 0; count < max; count++ )
      {
      lower[count] = Character.toLowerCase(
                                     values[count] );
      }

    return new StrA( lower );
    }



  public int compareToIgnoreCase( StrA in )
    {
    final int thisLen = values.length;
    final int inLen = in.values.length;
    int shorter = thisLen;
    if( inLen < shorter )
      shorter = inLen;

    StrA thisLower = toLowerCase();
    StrA inLower = in.toLowerCase();

    for( int count = 0; count < shorter; count++ )
      {
      if( thisLower.values[count] == inLower.values[count] )
        continue;

      return (int)thisLower.values[count] -
                (int)inLower.values[count];
      }

    if( thisLen == inLen )
      return 0;

    if( thisLen < inLen )
      return -1;
    else
      return 1;

    }



  public char firstNonSpaceChar()
    {
    final int max = values.length;
    if( max == 0 )
      return ' ';

    for( int count = 0; count < max; count++ )
      {
      char testChar = values[count];
      if( testChar != ' ' )
        return testChar;

      }

    return ' ';
    }




  public StrA replaceFirstChar( char toFind,
                                char replaceC )
    {
    final int max = values.length;
    if( max == 0 )
      return Empty;

    char[] resultA = new char[max];
    boolean foundFirst = false;
    for( int count = 0; count < max; count++ )
      {
      char testChar = values[count];
      if( !foundFirst )
        {
        if( testChar == toFind )
          {
          foundFirst = true;
          resultA[count] = replaceC;
          continue;
          }
        }

      resultA[count] = testChar;
      }

    return new StrA( resultA );
    }



  public StrA removeSections( char startChar,
                                       char endChar )
    {
    final int max = values.length;
    if( max == 0 )
      return Empty;

    StrABld sBld = new StrABld( max );
    boolean isInside = false;
    for( int count = 0; count < max; count++ )
      {
      char testChar = values[count];
      if( testChar == startChar )
        {
        isInside = true;
        continue;
        }

      if( testChar == endChar )
        {
        isInside = false;
        continue;
        }

      if( !isInside )
        sBld.appendChar( testChar );

      }

    return sBld.toStrA();
    }




  // This is a Cyclic Redundancy Check (CRC) function.
  // CCITT is the international standards body.
  // This CRC function is translated from a magazine
  // article in Dr. Dobbs Journal.
  // By Bob Felice, June 17, 2007
  // But this is my translation of what was in that
  // article.  (It was written in C.)
  public int GetCRC16()
    {
    // Different Polynomials can be used.
    int Polynomial = 0x8408;
    int crc = 0xFFFF;

    final int last = values.length;
    if( last == 0 )
      return ~crc;

    int data = 0;
    for( int count = 0; count < last; count++ )
      {
      data = 0xFF & values[count];
      // For each bit in the data byte.
      for( int i = 0; i < 8; i++ )
        {
        if( 0 != ((crc & 0x0001) ^ (data & 0x0001)) )
          crc = (crc >> 1) ^ Polynomial;
        else
          crc >>= 1;

        data >>= 1;
        }
      }

    crc = ~crc;
    data = crc;
    crc = (crc << 8) | ((data >> 8) & 0xFF);

    // Just make sure it's 16 bits.
    return crc & 0xFFFF;
    }



  public boolean containsChar( char toFind )
    {
    if( values.length == 0 )
      return false;

    final int last = values.length;
    for( int count = 0; count < last; count++ )
      {
      if( toFind == values[count] )
        return true;

      }

    return false;
    }



  public boolean containsStrA( StrA toFind )
    {
    if( values.length == 0 )
      return false;

    if( toFind.length() == 0 )
      return false;

    if( values.length < toFind.length() )
      return  false;

    char firstChar = toFind.charAt( 0 );
    final int last = values.length;
    for( int count = 0; count < last; count++ )
      {
      char testChar = values[count];
      if( testChar != firstChar )
        continue;

      if( searchTextMatches( count,
                              values,
                              toFind.values ))
        {
        return true;
        }
      }

    return false;
    }


  public boolean startsWithChar( char toFind )
    {
    if( values.length == 0 )
      return false;

    if( values[0] == toFind )
      return true;

    return false;
    }



  public boolean startsWith( StrA toFind )
    {
    if( values.length == 0 )
      return false;

    if( toFind.length() == 0 )
      return false;

    if( values.length < toFind.length() )
      return  false;

    char firstChar = toFind.charAt( 0 );
    char testChar = values[0];
    if( testChar != firstChar )
      return false;

    if( searchTextMatches( 0, values, toFind.values ))
      return true;

    return false;
    }



  public StrA getReverse()
    {
    final int max = values.length;
    char[] result = new char[max];
    int where = 0;
    for( int count = max - 1; count >= 0; count-- )
      {
      result[where] = values[count];
      where++;
      }

    return new StrA( result );
    }



  public boolean endsWith( StrA toFind )
    {
    if( values.length == 0 )
      return false;

    if( toFind.length() == 0 )
      return false;

    if( values.length < toFind.length() )
      return  false;

    StrA toFindR = toFind.getReverse();
    StrA valuesR = getReverse();

    final int max = toFindR.length();
    for( int count = 0; count < max; count++ )
      {
      if( valuesR.values[count] !=
                             toFindR.values[count] )
        return false;

      }

    return true;
    }



  public boolean endsWithChar( char toFind )
    {
    if( values.length == 0 )
      return false;

    final int where = values.length - 1;
    if( values[where] == toFind )
      return true;
    else
      return false;

    }



  public StrA substring( int begin, int end )
    {
    if( begin < 0 )
      begin = 0;

    if( end < 0 )
      end = 0;

    if( end >= values.length )
      end = values.length - 1;

    if( begin > end )
      return Empty;

    StrABld sBld = new StrABld( end );
    for( int count = begin; count <= end; count++ )
      sBld.appendChar( values[count] );

    return sBld.toStrA();
    }



  public StrA cleanUnicodeField()
    {
    final int last = values.length;
    StrABld sBld = new StrABld( last );
    for( int count = 0; count < last; count++ )
      {
      char testC = values[count];
      if( testC == '\t' )
        testC = ' ';

      if( testC == '\n' )
        testC = ' ';

      if( testC < ' ' )
        testC = '#';

      if( testC >= UTF8StrA.HighSurrogate )
        testC = '%';

      // if( (testC > 126) && (testC < 256) )
        // testC = '*';

      if( Markers.isMarker( testC ))
        testC = '(';

      sBld.appendChar( testC );
      }

    return sBld.toStrA();
    }



  }
