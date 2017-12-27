package com.astrid.weatherapp.utilities;

import android.content.Context;

import com.astrid.weatherapp.R;

import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by TeenWolf on 08.12.2017.
 */

public class WeatherDateUtils {

    public static final long DAY_IN_MILLIS = TimeUnit.DAYS.toMillis(1);

    public static long normalizeDate(long date) {
        long daysSinceEpoch = elapsedDaysSinceEpoch(date);
        long retValNew = date / DAY_IN_MILLIS * DAY_IN_MILLIS;
        return retValNew;
    }
    private static long elapsedDaysSinceEpoch(long utcDate) {
        return TimeUnit.MILLISECONDS.toDays(utcDate);
    }

    public static long getUTCDateFromLocal(long localDate) {
        TimeZone tz = TimeZone.getDefault();
        long gmtOffset = tz.getOffset(localDate);
        return localDate + gmtOffset;
    }

    public static long getNormalizedUtcDateForToday(){
        long utcNowMillis = System.currentTimeMillis();
        TimeZone currentTimeZone = TimeZone.getDefault();
        long gmtOffsetMillis = currentTimeZone.getOffset(utcNowMillis);
        long timeSinceEpochLocalTimeMillis = utcNowMillis + gmtOffsetMillis;
        long daysSinceEpoch = TimeUnit.MILLISECONDS.toDays(timeSinceEpochLocalTimeMillis);
        long normalizedUtcMidnightMillis = TimeUnit.DAYS.toMillis(daysSinceEpoch);
        return normalizedUtcMidnightMillis;
    }

    public static String getFriendlyDateString(Context context, long dateInMillis, boolean showFullDate) {

        long localDate = getLocalDateFromUTC(dateInMillis);
        long dayNumber = getDayNumber(localDate);
        long currentDayNumber = getDayNumber(System.currentTimeMillis());

        if (dayNumber == currentDayNumber || showFullDate) {

            String dayName = getDayName(context, localDate);
            String readableDate = getReadableDateString(context, localDate);
            if (dayNumber - currentDayNumber < 2) {

                String localizedDayName = new SimpleDateFormat("EEEE").format(localDate);
                return readableDate.replace(localizedDayName, dayName);
            } else {
                return readableDate;
            }
        } else if (dayNumber < currentDayNumber + 7) {
            return getDayName(context, localDate);
        } else {
            int flags = android.text.format.DateUtils.FORMAT_SHOW_DATE
                    | android.text.format.DateUtils.FORMAT_NO_YEAR
                    | android.text.format.DateUtils.FORMAT_ABBREV_ALL
                    | android.text.format.DateUtils.FORMAT_SHOW_WEEKDAY;

            return android.text.format.DateUtils.formatDateTime(context, localDate, flags);
        }
    }

    private static String getDayName(Context context, long dateInMillis) {
        long dayNumber = getDayNumber(dateInMillis);
        long currentDayNumber = getDayNumber(System.currentTimeMillis());
        if (dayNumber == currentDayNumber) {
            return context.getString(R.string.today);
        } else if (dayNumber == currentDayNumber + 1) {
            return context.getString(R.string.tomorrow);
        } else {
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
            return dayFormat.format(dateInMillis);
        }
    }

    public static long getDayNumber(long date) {
        TimeZone tz = TimeZone.getDefault();
        long gmtOffset = tz.getOffset(date);
        return (date + gmtOffset) / DAY_IN_MILLIS;
    }

    private static String getReadableDateString(Context context, long timeInMillis) {
        int flags = android.text.format.DateUtils.FORMAT_SHOW_DATE
                | android.text.format.DateUtils.FORMAT_NO_YEAR
                | android.text.format.DateUtils.FORMAT_SHOW_WEEKDAY;

        return android.text.format.DateUtils.formatDateTime(context, timeInMillis, flags);
    }
    public static long getLocalDateFromUTC(long utcDate) {
        TimeZone tz = TimeZone.getDefault();
        long gmtOffset = tz.getOffset(utcDate);
        return utcDate - gmtOffset;
    }

    public static boolean isDateNormalized(long millisSinceEpoch){
        boolean isDateNormalized = false;
        if(millisSinceEpoch % DAY_IN_MILLIS == 0){
            isDateNormalized = true;
        }
        return isDateNormalized;
    }
}
