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



public class FifoStrA implements Serializable
  {
  // It needs to have a version UID since it's
  // serializable.
  public static final long serialVersionUID = 1;

  private MainApp mApp;
  private StrA[] values;
  private int first = 0;
  private int last = 0;
  private final int arraySize;


  private FifoStrA()
    {
    arraySize = 0;
    }



  public FifoStrA( MainApp useApp, int setSize )
    {
    mApp = useApp;
    arraySize = setSize;
    values = new StrA[arraySize];
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



  public void setValue( StrA in )
    {
    if( in == null )
      return;

    values[last] = in;
    last++;
    if( last >= arraySize )
      last = 0;

    if( last == first )
      {
      mApp.showStatusAsync( "The Fifo was overrun." );
      return;
      }
    }




  public StrA getValue()
    {
    if( last == first )
      return null; // Nothing in Fifo.

    StrA result = values[first];
    values[first] = null;
    first++;
    if( first >= arraySize )
      first = 0;

    return result;
    }



  }
