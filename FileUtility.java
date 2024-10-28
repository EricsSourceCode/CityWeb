// Copyright Eric Chauvin 2019 - 2020.



import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;



  public class FileUtility
  {


  public static boolean exists( StrA fileName )
    {
    Path path = Paths.get( fileName.toString() );

    return Files.exists( path, LinkOption.NOFOLLOW_LINKS );
    }



  public static StrA readFileToStrA( MainApp mApp,
                                 StrA fileName,
                                 boolean keepTabs,
                                 boolean keepMarkers )
    {
    try
    {
    Path path = Paths.get( fileName.toString() );

    if( !Files.exists( path, LinkOption.NOFOLLOW_LINKS ))
      {
      // mApp.showStatusAsync( "File doesn't exist:\n" + fileName );
      return StrA.Empty;
      }

    byte[] fileBytes = Files.readAllBytes( path );
    if( fileBytes == null )
      {
      mApp.showStatusAsync( "readAllBytes was null:\n" + fileName );
      return StrA.Empty;
      }

    StrA fileS = UTF8StrA.bytesToStrA( mApp,
                                   fileBytes,
                                   2000000000,
                                   fileName );

    StrABld sBld = new StrABld( fileBytes.length + 1024 );

    char newline = '\n';
    char space = ' ';
    char tab = '\t';
    final int max = fileS.length();
    for( int count = 0; count < max; count++ )
      {
      char sChar = fileS.charAt( count );

      if( !keepTabs )
        {
        if( sChar == tab )
          sChar = space;

        }

      if( !keepMarkers )
        {
        if( Markers.isMarker( sChar ))
          sChar = ' ';

        }

      if( sChar < space )
        {
        // It ignores the \r character.
        if( !((sChar == newline) ||
              (sChar == tab )))
          continue;

        }

      sBld.appendChar( sChar );
      }

    return sBld.toStrA();
    }
    catch( Exception e )
      {
      mApp.showStatusAsync( "Could not read the file: \n" + fileName );
      mApp.showStatusAsync( e.getMessage() );
      return StrA.Empty;
      }
    }




  public static boolean writeStrAToFile( MainApp mApp,
                                 StrA fileName,
                                 StrA textS,
                                 boolean keepTabs,
                                 boolean keepMarkers )
    {
    try
    {
    if( textS == null )
      return false;

    if( textS.length() < 1 )
      return false;

    Path path = Paths.get( fileName.toString() );

    char newline = '\n';
    char space = ' ';
    char tab = '\t';
    final int max = textS.length();
    StrABld sBld = new StrABld( max );
    for( int count = 0; count < max; count++ )
      {
      char sChar = textS.charAt( count );

      if( sChar >= 0xD800 ) // High Surrogates
        continue;

      if( !keepTabs )
        {
        if( sChar == tab )
          sChar = space;

        }

      if( !keepMarkers )
        {
        if( Markers.isMarker( sChar ))
          sChar = space;

        }

      if( sChar < space )
        {
        if( !((sChar == newline) ||
              (sChar == tab )))
          continue;

        }

      sBld.appendChar( sChar );
      }

    StrA outS = sBld.toStrA();

    if( outS.length() < 1 )
      return false;

    byte[] outBuffer = UTF8StrA.strAToBytes( outS );
    if( outBuffer == null )
      {
      mApp.showStatusAsync( "Could not write to the file: \n" + fileName );
      mApp.showStatusAsync( "outBuffer was null." );
      return false;
      }

    Files.write( path, outBuffer,  StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING,
                        StandardOpenOption.WRITE );

    return true;
    }
    catch( Exception e )
      {
      mApp.showStatusAsync( "Could not write to the file: \n" + fileName );
      mApp.showStatusAsync( e.getMessage() );
      return false;
      }
    }



  }
