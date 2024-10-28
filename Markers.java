// Copyright Eric Chauvin 2018 - 2021.



      // Basic Multilingual Plane
      // C0 Controls and Basic Latin (Basic Latin)
      //                                 (0000 007F)
      // C1 Controls and Latin-1 Supplement (0080 00FF)
      // Latin Extended-A (0100 017F)
      // Latin Extended-B (0180 024F)
      // IPA Extensions (0250 02AF)
      // Spacing Modifier Letters (02B0 02FF)
      // Combining Diacritical Marks (0300 036F)
      // General Punctuation (2000 206F)
      // Superscripts and Subscripts (2070 209F)
      // Currency Symbols (20A0 20CF)
      // Combining Diacritical Marks for Symbols
      //                                (20D0 20FF)
      // Letterlike Symbols (2100 214F)
      // Number Forms (2150 218F)
      // Arrows (2190 21FF)
      // Mathematical Operators (2200 22FF)
      // Box Drawing (2500 257F)
      // Geometric Shapes (25A0 25FF)
      // Miscellaneous Symbols (2600 26FF)
      // Dingbats (2700 27BF)
      // Miscellaneous Symbols and Arrows (2B00 2BFF)


  public class Markers
  {
  public static final char Begin = (char)0x2700;
  public static final char End = (char)0x2701;
  public static final char ErrorPoint = (char)0x2702;
  public static final char BeginCData = (char)0x2703;
  public static final char EndCData = (char)0x2704;
  public static final char BeginScript = (char)0x2705;
  public static final char EndScript = (char)0x2706;
  public static final char BeginHtmlComment =
                                         (char)0x2707;
  public static final char EndHtmlComment =
                                         (char)0x2708;

//                        (char)0x2709;
//                        (char)0x270A;
//                        (char)0x270B;
//                        (char)0x270C;
//                        (char)0x270D;
//                        (char)0x270E;
//                        (char)0x270F;
//                        (char)0x2710;
//                        (char)0x2711;


  public static final char URLFileDelimit =
                                       (char)0x2712;


/*
  public static final char EscapedSingleQuote
  public static final char EscapedDoubleQuote
  public static final char TypeString
  public static final char TypeChar
  public static final char TypeNumber
  public static final char TypeIdentifier
  public static final char TypeLineNumber
  public static final char SlashStar
  public static final char StarSlash
  public static final char DoubleSlash
  public static final char TypeOperator
  public static final char EscapedSlash
  public static final char QuoteAsSingleCharacter
  public static final char TypeBoolean
  public static final char TypeCodeBlock
  public static final char ShowOddChar

*/



  public static boolean isMarker( char testChar )
    {
    // Reserve these symbols for markers.
    // Miscellaneous Symbols (0x2600 to 0x26FF)
    // Dingbats (0x2700 to 0x27BF)
    // Miscellaneous Symbols and Arrows (0x2B00 to 0x2BFF)

    int value = (int)testChar;
    if( (value >= 0x2600) && (value <= 0x2BFF))
      return true;

    return false;
    }



  public static StrA removeAllMarkers( StrA in )
    {
    if( in == null )
      return StrA.Empty;

    final int max = in.length();
    if( max == 0 )
      return StrA.Empty;

    StrABld sBuilder = new StrABld( in.length());
    for( int count = 0; count < max; count++ )
      {
      char testChar = in.charAt( count );
      if( isMarker( testChar ))
        continue;

      sBuilder.appendChar( testChar );
      }

    return sBuilder.toStrA();
    }



  public static int countMarkers( StrA in )
    {
    if( in == null )
      return 0;

    final int max = in.length();
    if( max == 0 )
      return 0;

    int howMany = 0;
    for( int count = 0; count < max; count++ )
      {
      char testChar = in.charAt( count );
      if( isMarker( testChar ))
        howMany++;

      }

    return howMany;
    }



  }
