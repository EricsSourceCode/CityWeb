// Copyright Eric Chauvin 2021 - 2024.



// This is licensed under the GNU General
// Public License (GPL).  It is the
// same license that Linux has.
// https://www.gnu.org/licenses/gpl-3.0.html



import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;


// A long is about 292,471,208 Years.



public final class ECTime
  {
  private GregorianCalendar GCalendarLocal;
  private int HourOffset = 0;
  private int UTCYear = 1970;
  private int UTCMonth = 1; // 1 to 12.
  private int UTCDay = 1;  // 1 to 31
  private int UTCHour = 0; // 0 to 23
  private int UTCMinute = 1; // 0 to 59.
  private int UTCSecond = 0; // 0 to 59
  private int LocalYear = 1970;
  private int LocalMonth = 1; // January
  private int LocalDay = 1;  // 1 to 31
  private int LocalHour = 0; // 0 to 23
  private int LocalMinute = 1; // 0 to 59.
  private int LocalSecond = 0; // 0 to 59

  // Unix time (a.k.a. POSIX time or Epoch time) defined as the number of seconds that have
  // elapsed since 00:00:00 Coordinated Universal Time (UTC), Thursday, 1 January 1970
  // not counting leap seconds.


  protected ECTime()
    {
    GCalendarLocal = new GregorianCalendar();
    SetLocalTimesFromLocalCalendar();
    SetUTCTimesFromLocalCalendar();
    }


  protected ECTime( long Index )
    {
    GCalendarLocal = new GregorianCalendar();

    SetFromIndex( Index );
    }




  protected void SetToYear( int Year )
    {
    // These values are in the local TimeZone and Locale:
    GCalendarLocal = new GregorianCalendar( Year, 1, 1, 1, 0, 0 );
    SetLocalTimesFromLocalCalendar();
    SetUTCTimesFromLocalCalendar();
    }




  protected int GetHourOffset()
    {
    return HourOffset;
    }




  protected int GetLocalYear()
    {
    return LocalYear;
    }


  protected int GetLocalMonth()
    {
    return LocalMonth;
    }


  protected int GetLocalDay()
    {
    return LocalDay;
    }



  protected int GetLocalHour()
    {
    return LocalHour;
    }



  protected int GetLocalMinute()
    {
    return LocalMinute;
    }



  protected int GetLocalSecond()
    {
    return LocalSecond;
    }



  protected int GetUTCYear()
    {
    return UTCYear;
    }



  protected int GetUTCMonth()
    {
    return UTCMonth;
    }



  protected int GetUTCDay()
    {
    return UTCDay;
    }



  protected int GetUTCHour()
    {
    return UTCHour;
    }


  protected int GetUTCMinute()
    {
    return UTCMinute;
    }



  protected int GetUTCSecond()
    {
    return UTCSecond;
    }




  protected String ToLocalDateStringShort()
    {
    int Day = LocalDay;
    int Month = LocalMonth;
    int Year = LocalYear;

    if( Year < 2000 )
      return "No Date";

    Year = Year % 100;

    String DayS = Integer.toString( Day );
    if( DayS.length() == 1 )
      DayS = "0" + DayS;

    String MonthS = Integer.toString( Month );
    if( MonthS.length() == 1 )
      MonthS = "0" + MonthS;

    String YearS = Integer.toString( Year );
    if( YearS.length() == 1 )
      YearS = "0" + YearS;

    return MonthS + "/" + DayS + "/" + YearS;
    }




  protected String ToLocalDateStringVeryShort()
    {
    int Day = LocalDay;
    int Month = LocalMonth;
    int Year = LocalYear;
    if( Year < 2000 )
      return "No Date";

    // Year = Year % 100;

    String DayS = Integer.toString( Day );
    if( DayS.length() == 1 )
      DayS = "0" + DayS;

    String MonthS = Integer.toString( Month );
    if( MonthS.length() == 1 )
      MonthS = "0" + MonthS;

    // String YearS = Integer.toString( Year );
    // if( YearS.length() == 1 )
      // YearS = "0" + YearS;

    return MonthS + "/" + DayS;
    }




  protected String ToLocalTimeString()
    {
    int Hour = LocalHour;
    int Minute = LocalMinute;
    int Second = LocalSecond;

    String AMPM = "AM";
    if( Hour >= 12 )
      AMPM = "PM";

    Hour = Hour % 12;

    String HourS = Integer.toString( Hour );
    // if( HourS.length() == 1 )
      // HourS = "0" + HourS;

    String MinuteS = Integer.toString( Minute );
    if( MinuteS.length() == 1 )
      MinuteS = "0" + MinuteS;

    String SecondS = Integer.toString( Second );
    if( SecondS.length() == 1 )
      SecondS = "0" + SecondS;

    String Result = HourS + ":" + MinuteS + ":" + SecondS + " " + AMPM;

    if( Result.equals( "0:00:00 P" ))
      Result = "12:00:00 P";

    return Result;

    }



  protected String ToLocalTimeStringNoSeconds()
    {
    int Hour = LocalHour;
    int Minute = LocalMinute;
    // int Second = LocalSecond;

    String AMPM = "AM";
    if( Hour >= 12 )
      AMPM = "PM";

    Hour = Hour % 12;

    String HourS = Integer.toString( Hour );
    // if( HourS.length() == 1 )
      // HourS = "0" + HourS;

    String MinuteS = Integer.toString( Minute );
    if( MinuteS.length() == 1 )
      MinuteS = "0" + MinuteS;

    // String SecondS = Integer.toString( Second );
    // if( SecondS.length() == 1 )
      // SecondS = "0" + SecondS;

    String Result = HourS + ":" + MinuteS + " " + AMPM;

    if( Result.equals( "0:00 P" ))
      Result = "12:00 P";

    return Result;
    }



  protected String ToUTCDateStringShort()
    {
    int Day = UTCDay;
    int Month = UTCMonth;
    int Year = UTCYear;
    Year = Year % 100;

    String DayS = Integer.toString( Day );
    if( DayS.length() == 1 )
      DayS = "0" + DayS;

    String MonthS = Integer.toString( Month );
    if( MonthS.length() == 1 )
      MonthS = "0" + MonthS;

    String YearS = Integer.toString( Year );
    if( YearS.length() == 1 )
      YearS = "0" + YearS;

    return MonthS + "/" + DayS + "/" + YearS;
    }




  protected String ToUTCTimeString()
    {
    int Hour = UTCHour;
    int Minute = UTCMinute;
    int Second = UTCSecond;

    String AMPM = "AM";
    if( Hour >= 12 )
      AMPM = "PM";

    Hour = Hour % 12;

    String HourS = Integer.toString( Hour );
    // if( HourS.length() == 1 )
      // HourS = "0" + HourS;

    String MinuteS = Integer.toString( Minute );
    if( MinuteS.length() == 1 )
      MinuteS = "0" + MinuteS;

    String SecondS = Integer.toString( Second );
    if( SecondS.length() == 1 )
      SecondS = "0" + SecondS;

    String Result = HourS + ":" + MinuteS + ":" + SecondS + " " + AMPM;
    if( Result.equals( "0:00:00 PM" ))
      Result = "12:00:00 PM";

    return Result;
    }




  protected String ToUTCTimeStringNoSeconds()
    {
    int Hour = UTCHour;
    int Minute = UTCMinute;
    // int Second = UTCSecond;

    String AMPM = "AM";
    if( Hour >= 12 )
      AMPM = "PM";

    Hour = Hour % 12;

    String HourS = Integer.toString( Hour );
    // if( HourS.length() == 1 )
      // HourS = "0" + HourS;

    String MinuteS = Integer.toString( Minute );
    if( MinuteS.length() == 1 )
      MinuteS = "0" + MinuteS;

    String Result = HourS + ":" + MinuteS + " " + AMPM;
    if( Result.equals( "0:00 PM" ))
      Result = "12:00 PM";

    return Result;
    }



  protected long GetIndex()
    {
    // 16 bits is enough for the year 6,500 or so.  (64K)
    long Result = UTCYear;

    Result <<= 4; // Room for a month up to 16.
    Result |= UTCMonth;

    Result <<= 5; // 32 days.
    Result |= UTCDay;

    Result <<= 5; // 32 hours.
    Result |= UTCHour;

    Result <<= 6; // 64 minutes.
    Result |= UTCMinute;

    Result <<= 6; // 64 seconds.
    Result |= UTCSecond;

    Result <<= 10;  // 1024 milliseconds.
    // Result |= UTCMilliSecond;

    // 16 + 5 + 4 + 5 + 6 + 6 + 10.
    // (16 + 5) + (4 + 5) + (6 + 6) + 10.
    // 21 +          9 +      12    + 10
    // 30 +      22
    // 52 bits wide.

    return Result;
    }



  protected void SetFromIndex( long Index )
    {
    try
    {

    if( Index < 1 )
      {
      SetToYear( 1971 );
      return;
      }

    // 10 bits
    // UTCMillisecond = (int)(Index & 0x3FF);
    Index >>= 10;

    UTCSecond = (int)(Index & 0x3F);
    Index >>= 6;

    UTCMinute = (int)(Index & 0x3F);
    Index >>= 6;

    UTCHour = (int)(Index & 0x1F);
    Index >>= 5;

    UTCDay = (int)(Index & 0x1F);
    Index >>= 5;

    UTCMonth = (int)(Index & 0xF);
    Index >>= 4;

    // 16 bits.
    UTCYear = (int)(Index & 0xFFFF);
    // Index >>= 16;

    if( UTCYear <= 1971 )
      {
      SetToYear( 1971 );
      return;
      }

    SetLocalTimeFromUTCTime();

    }
    catch( Exception e )
      {
      SetToYear( 1971 );
      }
    }



  private void SetLocalTimeFromUTCTime()
    {
    // Notice UTCMonth - 1 here because it takes as input months 0 to 11.
    GCalendarLocal.set( UTCYear, UTCMonth - 1, UTCDay, UTCHour, UTCMinute, UTCSecond );

    long Millisec = GCalendarLocal.getTimeInMillis();

    // Raw offset from GMT in milliseconds.
    long ZoneOffset = GCalendarLocal.get( Calendar.ZONE_OFFSET );
    // DaylightTimeOffset = 0; // GCalendarLocal.get( Calendar.DST_OFFSET );

    TimeZone TZ = TimeZone.getDefault();
    int DaylightSavings = TZ.getDSTSavings();

    // Notice that .getTime() was set a little off here, like if was near the
    // change over when daylight savings time ends or begins.
    if( !TZ.inDaylightTime( GCalendarLocal.getTime()))
      DaylightSavings = 0;

    HourOffset = (int)((-ZoneOffset - DaylightSavings) / (60L * 60L * 1000L));

    // Add them back here:
    Millisec = Millisec + ZoneOffset + DaylightSavings;
    GCalendarLocal.setTimeInMillis( Millisec );

    SetLocalTimesFromLocalCalendar();
    }



  private void SetLocalTimesFromLocalCalendar()
    {
    LocalYear = GCalendarLocal.get( Calendar.YEAR );
    LocalMonth = GCalendarLocal.get( Calendar.MONTH );
    LocalMonth++; // Make the months 1 to 12.

    LocalDay = GCalendarLocal.get( Calendar.DAY_OF_MONTH );
    LocalHour = GCalendarLocal.get( Calendar.HOUR_OF_DAY );
    LocalMinute = GCalendarLocal.get( Calendar.MINUTE );
    LocalSecond = GCalendarLocal.get( Calendar.SECOND );
    // GCalendarLocal.get( Calendar.MILLISECOND );
    }



  private void SetUTCTimesFromLocalCalendar()
    {
    try
    {
    long Millisec = GCalendarLocal.getTimeInMillis();
    long ZoneOffset = GCalendarLocal.get( Calendar.ZONE_OFFSET );
    // DaylightTimeOffset = GCalendarLocal.get( Calendar.DST_OFFSET );
    TimeZone TZ = TimeZone.getDefault();
    int DaylightSavings = TZ.getDSTSavings();

    if( !TZ.inDaylightTime( GCalendarLocal.getTime()))
      DaylightSavings = 0;

    Millisec = Millisec - ZoneOffset - DaylightSavings;
    HourOffset = (int)((-ZoneOffset - DaylightSavings) / (60L * 60L * 1000L));

    // "Sets the time of this Calendar to the given Unix time."
    // If you ask this for its time zone it would say it's in local
    // time, but it is offset to UTC time.

    GregorianCalendar GCalendarUTC = new GregorianCalendar();
    GCalendarUTC.setTimeInMillis( Millisec );

    UTCYear = GCalendarUTC.get( Calendar.YEAR );
    UTCMonth = GCalendarUTC.get( Calendar.MONTH );
    UTCMonth++; // Make the months 1 to 12.

    UTCDay = GCalendarUTC.get( Calendar.DAY_OF_MONTH );
    UTCHour = GCalendarUTC.get( Calendar.HOUR_OF_DAY );
    UTCMinute = GCalendarUTC.get( Calendar.MINUTE );
    UTCSecond = GCalendarUTC.get( Calendar.SECOND );

    }
    catch( Exception e )
      {
      SetToYear( 1971 );
      }
    }




  protected long GetMilliSecToNow()
    {
    GregorianCalendar RightNow = new GregorianCalendar();
    long Millisec = GCalendarLocal.getTimeInMillis();
    long MillisecNow = RightNow.getTimeInMillis();
    long Diff = MillisecNow - Millisec;

    return Diff;
    }



  protected long GetSecondsToNow()
    {
    GregorianCalendar RightNow = new GregorianCalendar();
    long Millisec = GCalendarLocal.getTimeInMillis();
    long MillisecNow = RightNow.getTimeInMillis();
    long Diff = MillisecNow - Millisec;

    return Diff / 1000; // In seconds.
    }




  protected long GetHoursToNow()
    {
    GregorianCalendar RightNow = new GregorianCalendar();
    long Millisec = GCalendarLocal.getTimeInMillis();
    long MillisecNow = RightNow.getTimeInMillis();
    long Diff = MillisecNow - Millisec;

    return Diff / (60 * 60 * 1000); // In Hours.
    }




  protected void SetToNow()
    {
    // Initialized to the current date and time with the default
    // Locale and TimeZone.
    GCalendarLocal = new GregorianCalendar();

    SetLocalTimesFromLocalCalendar();
    SetUTCTimesFromLocalCalendar();
    }





  protected void AddSeconds( long Seconds )
    {
    // Seconds can be negative.
    long Millisec = GCalendarLocal.getTimeInMillis();
    Seconds = Seconds * 1000;
    Millisec = Millisec + Seconds;

    try
    {
    GCalendarLocal.setTimeInMillis( Millisec );
    SetLocalTimesFromLocalCalendar();
    SetUTCTimesFromLocalCalendar();

    }
    catch( Exception e )
      {
      SetToYear( 1971 );
      }
    }



  protected void AddMilliseconds( long Milli )
    {
    // MilliSec can be negative.
    long Millisec = GCalendarLocal.getTimeInMillis();
    Millisec = Millisec + Milli;

    try
    {
    GCalendarLocal.setTimeInMillis( Millisec );
    SetLocalTimesFromLocalCalendar();
    SetUTCTimesFromLocalCalendar();
    }
    catch( Exception e )
      {
      SetToYear( 1971 );
      }
    }



  protected long GetMilliseconds()
    {
    // MilliSec can be negative.
    return GCalendarLocal.getTimeInMillis();
    }




  protected void SetFromAndroidMilliseconds( long MilliSec )
    {
    try
    {
    /*
    GregorianCalendar GCalendarUTC = new GregorianCalendar();
    GCalendarUTC.setTimeInMillis( MilliSec );

    UTCYear = GCalendarUTC.get( Calendar.YEAR );
    UTCMonth = GCalendarUTC.get( Calendar.MONTH );
    UTCMonth++; // Make the months 1 to 12.

    UTCDay = GCalendarUTC.get( Calendar.DAY_OF_MONTH );
    UTCHour = GCalendarUTC.get( Calendar.HOUR_OF_DAY );
    UTCMinute = GCalendarUTC.get( Calendar.MINUTE );
    UTCSecond = GCalendarUTC.get( Calendar.SECOND );

    long ZoneOffset = GCalendarLocal.get( Calendar.ZONE_OFFSET );
    // DaylightTimeOffset = GCalendarLocal.get( Calendar.DST_OFFSET );
    TimeZone TZ = TimeZone.getDefault();
    int DaylightSavings = TZ.getDSTSavings();

    if( !TZ.inDaylightTime( GCalendarLocal.getTime()))
      DaylightSavings = 0;
    */

    // MilliSec = MilliSec + ZoneOffset + DaylightSavings;
    GCalendarLocal.setTimeInMillis( MilliSec );
    SetLocalTimesFromLocalCalendar();
    SetUTCTimesFromLocalCalendar();

    }
    catch( Exception e )
      {
      SetToYear( 1971 );
      }
    }


  }
