package com.duowan.fw.util;

import android.annotation.SuppressLint;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class JTimeUtils {

    public static final int MILLIS_IN_SEC = 1000;
    public static final int SECS_IN_MINUTE = 60;
    public static final int MILLIS_IN_MINUTE = 60000;
    
    public static long getJavaTimeFromUint32(int t) {
        return JUtils.uint2long(t) * 1000;
    }
    
    /*
     * Get current timestamp in 32bit
     * 
     * @return Current timestamp in millis
     * */
	static long beginTS = new GregorianCalendar(2015, 10, 1).getTimeInMillis();
    public static long getTimeStamp(){
    	return (System.currentTimeMillis() - beginTS);
    }

    /**
     * Get current time in secs.
     * 
     * @return Current time in seconds.
     */
    public static long getCurrentTime() {
        long l = System.currentTimeMillis();
        return (l / 1000);
    }
	
    /**
     * Get expired dead time, in secs.
     * @param secsToExpire Seconds from now to the dead time.
     * @return Expired dead time, in secs, it is in UTC(GMT).
     */
    public static long getExpireDeadTime(long secsToExpire) {
        return JTimeUtils.getCurrentTime() + secsToExpire;
    }

    public static int javaTimeToUnit32(long time) {
        return (int) ((time / 1000) & Integer.MAX_VALUE);
    }

    private static int[] splitDateStringToInt(String date) {
        if (date == null || date.length() < 8)
            return null;

        int[] ret = new int[3];

        ret[0] = Integer.parseInt(date.substring(0, 4));
        ret[1] = Integer.parseInt(date.substring(4, 6));
        ret[2] = Integer.parseInt(date.substring(6, 8));
        return ret;
    }

    public static String calcuateAge(String birth) {
        int age = getAgeByBirth(birth);
        if (age == 0) {
            return "";
        }
        return String.valueOf(age);
    }

    public static int getAgeByBirthday(String birth) {
        int age = getAgeByBirth(birth);
        if (age < 10 || age > 70) {
            return -1;
        }
        return age;
    }

    private static int getAgeByBirth(String birth) {
        int[] date = splitDateStringToInt(birth);

        if (date == null)
            return 0;

        int birthYear = date[0];
        int birthMonth = date[1];

        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        int nowYear = cal.get(Calendar.YEAR);
        int nowMonth = cal.get(Calendar.MONTH) + 1;

        int age = nowYear - birthYear;

        if (nowMonth < birthMonth) {
            --age;
        }

        return age;
    }

	public static int getAgeByBirth(java.util.Date birthDay){
		Calendar cal = Calendar.getInstance();

		if (cal.before(birthDay)) {
			return 0;
		}

		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH)+1;
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

		cal.setTime(birthDay);
		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				//monthNow==monthBirth
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
				}
			} else {
				//monthNow>monthBirth
				age--;
			}
		}
		return age;
	}

    @SuppressLint("DefaultLocale")
	public static String getDisplayTimeFromTimestamp(long timestamp) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        return String.format("%d-%d-%d  (%02d:%02d)", year, month, day, hour,
                             min);
    }

    /**
     * Return a string to represent time according to fmt
     * 
     * @param fmt
     *            , must contains 5 years to hold, year, month, day, hour and
     *            min;
     * @param timestamp
     *            , time in millis
     * @return formatted string.
     */
    public static String timestampToString(String fmt, long timestamp) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        return String.format(fmt, year, month, day, hour, min);
    }

    public static boolean isSameWeek(long t1, long t2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(t1);
        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(t2);
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
            && c1.get(Calendar.WEEK_OF_YEAR) == c2
            .get(Calendar.WEEK_OF_YEAR);
    }

    public static boolean isSameDay(long t1, long t2) {
        Date myDate1 = new Date(t1);
        Date myDate2 = new Date(t2);
        if (myDate1.getYear() != myDate2.getYear())
            return false;
        
        if (myDate1.getMonth() != myDate2.getMonth())
            return false;
        
        if (myDate1.getDay() != myDate2.getDay())
            return false;
        
        return true;
    }
	
    public static int getDayOfWeek(long t) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(t);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    private static String sToday = null;
    private static String sYesterday = null;
    private static String sBeforeYesterday = null;
    private static String sTimeFormat = null;
    private static String sShortDateFormat = null;
    private static String sDateFormat = null;
    private static String sSpace = " ";
    private static Calendar sCurrent = Calendar.getInstance();
    private static Calendar sPost = Calendar.getInstance();

    public static String getPostTimeString(long postTime, boolean showToday) {

        sCurrent.setTimeInMillis(System.currentTimeMillis());
        sPost.setTimeInMillis(postTime);
        int diffDays = sCurrent.get(Calendar.DAY_OF_YEAR)
            - sPost.get(Calendar.DAY_OF_YEAR);
        boolean isSameYear = sPost.get(Calendar.YEAR) == sCurrent
            .get(Calendar.YEAR);

        StringBuilder builder = new StringBuilder();
        if (diffDays > 0 || !isSameYear) {
            if (diffDays > 0 && diffDays <= 2) {
                builder.append(diffDays == 1 ? sYesterday : sBeforeYesterday);
            }
            else {
                if (isSameYear) {
                    builder.append(String.format(sShortDateFormat,
                                                 sPost.get(Calendar.MONTH) + 1,
                                                 sPost.get(Calendar.DAY_OF_MONTH)));
                }
                else {
                    builder.append(String.format(sDateFormat,
                                                 sPost.get(Calendar.YEAR),
                                                 sPost.get(Calendar.MONTH) + 1,
                                                 sPost.get(Calendar.DAY_OF_MONTH)));
                }
            }
        }
        else if (showToday) {
            builder.append(sToday);
        }
        builder.append(sSpace);
        builder.append(String.format(sTimeFormat,
                                     sPost.get(Calendar.HOUR_OF_DAY), sPost.get(Calendar.MINUTE)));
        return builder.toString();
    }
	
    public static int toSec(long millis) {
        return (int) (millis / 1000);
    }

    public static long toMillis(int sec) {
        return ((long) sec) * 1000;
    }
	
    public static long toMillis(long secs) {
        return secs * 1000;
    }

    public static long curTimeInMillis() {
        return System.currentTimeMillis();
    }
    
    @SuppressLint("NewApi")
	public static String smartUnitConvert(long millis) {
		final TimeUnit[] units = { TimeUnit.DAYS, TimeUnit.HOURS,
				TimeUnit.MINUTES, TimeUnit.SECONDS };
		final String[] unitStrs = { "%dD+", "%dH+", "%dM+", "%dS" };

		for (int i = 0; i < units.length; ++i) {
			long value = units[i].convert(millis, TimeUnit.MILLISECONDS);
			if (value > 0) {
				return String.format(unitStrs[i], value);
			}
		}

		return "0S";
	}

	public static long gerRemainTime(long toTime) {
		return toTime - System.currentTimeMillis();
	}
	
	public static String getCurFormatTime(String pattern) {
		String dateTime = null;
		
		try {
			SimpleDateFormat dateFm = new SimpleDateFormat(pattern, Locale.getDefault());
			dateTime = dateFm.format(new java.util.Date());
		} catch (NullPointerException e) {
			JLog.error("JTimeUtils", e.getMessage());
		} catch (IllegalArgumentException e) {
			JLog.error("JTimeUtils", e.getMessage());
		}
		
		return dateTime;
	}
}
