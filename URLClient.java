// Copyright Eric Chauvin 2020 - 2024.



// This is licensed under the GNU General
// Public License (GPL).  It is the
// same license that Linux has.
// https://www.gnu.org/licenses/gpl-3.0.html



import java.net.URLConnection;
import java.net.URL;
import java.net.URI;
import java.io.BufferedInputStream;
// BufferedOutputStream



public class URLClient implements Runnable
  {
  private MainApp mApp;
  private String fileName = "";
  private String URLToGet = "";




  private URLClient()
    {
    }



  public URLClient( MainApp useApp,
                    String fileToUse,
                    String URLToUse )
    {
    mApp = useApp;
    fileName = fileToUse;
    URLToGet = URLToUse;
    }



  @Override
  public void run()
    {
    getURLToFile();
    }



  private void getURLToFile()
    {
    try
    {
    mApp.showStatusAsync(
                "\n\nGetting: " + URLToGet );

    URI uri = new URI( URLToGet );
    URL url = uri.toURL();

    // Calling openConnection() in the URL object
    // Creates the specific type of URLConnection.
    // Like HttpURLConnection.
    URLConnection uConnect = url.openConnection();
    BufferedInputStream inStream = new
                          BufferedInputStream(
                          uConnect.getInputStream());

    ByteBld byteBld = new ByteBld( 1024 * 64 );
    String contentType = "";

    try
    {
// Get this stuff after it reads it in the loop.

// getContent
// getHeaderField
/*
======
URL.openConnection(), connect(), getContent(),
 getContentEncoding(), getContentLength(),
 getContentType(), getDate(), getExpiration(),
 getHeaderField(int),
getHeaderField(java.lang.String),
 getInputStream(), getLastModified(),
 getOutputStream(),
 setAllowUserInteraction(boolean),
 setDefaultUseCaches(boolean),
 setDoInput(boolean), setDoOutput(boolean),
 setIfModifiedSince(long),
 setRequestProperty(java.lang.String,
 java.lang.String), setUseCaches(boolean)
*/



    // mApp.showStatusAsync( "getContentEncoding: " +
    //           uConnect.getContentEncoding() );

    // mApp.showStatusAsync( "getContentLength: " +
    //           uConnect.getContentLength() );

    // getContentType: text/html; charset=utf-8

    contentType = uConnect.getContentType();
    // mApp.showStatusAsync( "\n\nContentType: " +
    //                                 contentType );

    // mApp.showStatusAsync( "getDate: " +
    //           uConnect.getDate() );

    // mApp.showStatusAsync( "getExpiration: " +
    //           uConnect.getExpiration() );

    // mApp.showStatusAsync( "getLastModified: " +
    //           uConnect.getLastModified() );

    for( int count = 0; count < 1000000; count++ )
      {
      int b = inStream.read();
      if( b == -1 )
        break;

      // This works.
      // if( b > 127 )
        // mApp.showStatusAsync( "Byte > 127: " + b );

      // Convert to byte?
      byteBld.appendByte( (byte)b );

      // For BufferedOutputStream
      // out.write( b );
      }
    }
    finally
      {
      if( inStream != null )
        inStream.close();

      }

    byte[] bytesBuf = byteBld.toByteArray();
    if( bytesBuf == null )
      return;

    StrA showS = new StrA( URLToGet + " "  + contentType );
    StrA strToWrite = UTF8StrA.bytesToStrA( mApp,
                                       bytesBuf,
                                       1000000000,
                                       showS );

    StrA fileToWrite = new StrA( fileName );
    FileUtility.writeStrAToFile( mApp,
                                 fileToWrite,
                                 strToWrite,
                                 false,
                                 false );

    mApp.showStatusAsync( "\nFinished: " + URLToGet );

    }
    catch( Exception e )
      {
      mApp.showStatusAsync( "Exception in URLClient.getURLToFile()." );
      mApp.showStatusAsync( e.getMessage() );
      }
    }


  }



/*
Below this is notes from the Java tutorials:


// HTTP POST:

                  URLEncoder.encode(args[1], "UTF-8");

    URLConnection connection = url.openConnection();
    connection.setDoOutput(true);

    OutputStreamWriter out = new OutputStreamWriter(
                        connection.getOutputStream());
    out.write("string=" + stringToReverse);

URL myURL = new URL("http://example.com/");

import javax.net.ssl.SSLSocket;

// java.net.Socket


JSSE (Java Secure Socket Extension) API.
Java Cryptography Architecture (JCA)
Java Cryptographic Extension (JCE)
SSLSocketFactory
javax.net.ssl.SSLSocketFactory
getDefault()
Socket createSocket(String host, int port)
Socket createSocket(String host, int port, InetAddress clientHost, int clientPort)
Socket createSocket(InetAddress host, int port)
Socket createSocket(InetAddress host, int port, InetAddress clientHost, int clientPort)
Socket createSocket(Socket socket, String host, int port, boolean autoClose)
javax.net.ssl.SSLContext
SSLSocketFactory
SSLSocket
SSLServerSocketFactory
SSLServerSocket
createServerSocket

SSLSocketFactory sslsocketfactory = SSLSocketFactory.getDefault();
SSLSocket sslsocket = (SSLSocket) sslsocketfactory
  .createSocket(host, port);
InputStream in = sslsocket.getInputStream();
OutputStream out = sslsocket.getOutputStream();

while (in.available() > 0) {
  }

javax.net.ssl.SSLHandshakeException
sun.security.validator.ValidatorException
sun.security.provider.certpath.SunCertPathBuilderException

javax.net.ssl.trustStore

            SSLSocketFactory factory =
                (SSLSocketFactory)SSLSocketFactory.getDefault();
            SSLSocket socket =
                (SSLSocket)factory.createSocket("www.verisign.com", 443);

            socket.startHandshake();

    socket.getOutputStream())));
    socket.getInputStream()));

    socket.close();


SSL port 443



import javax.security.cert.X509Certificate;
import java.security.KeyStore;


            SSLSocketFactory factory = null;
            try {
                SSLContext ctx;
                KeyManagerFactory kmf;
                KeyStore ks;
                char[] passphrase = "passphrase".toCharArray();

                ctx = SSLContext.getInstance("TLS");
                kmf = KeyManagerFactory.getInstance("SunX509");
                ks = KeyStore.getInstance("JKS");

                ks.load(new FileInputStream("testkeys"), passphrase);

                kmf.init(ks, passphrase);
                ctx.init(kmf.getKeyManagers(), null, null);

                factory = ctx.getSocketFactory();

   SSLSocket socket = (SSLSocket)factory.createSocket(host, port);
   socket.startHandshake();


import javax.security.cert.X509Certificate;

*/
