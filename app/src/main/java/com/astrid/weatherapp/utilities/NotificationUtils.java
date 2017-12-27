package com.astrid.weatherapp.utilities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;

import com.astrid.weatherapp.DetailActivity;
import com.astrid.weatherapp.R;
import com.astrid.weatherapp.data.WeatherContract;
import com.astrid.weatherapp.data.WeatherPreference;
import com.firebase.jobdispatcher.Constraint;

/**
 * Created by TeenWolf on 22.12.2017.
 */

public class NotificationUtils {
    public static final String[] WEATER_NOTIFICATION_PROJECTION = {
            WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP
    };

    public static final int INDEX_WEATHER_ID = 0;
    public static final int INDEX_WEATHER_MAX_TEMP = 1;
    public static final int INDEX_WEATHER_MIN_TEMP = 2;

    private static  final  int WEATHER_NOTIFICATION_ID = 3004;

    public static void notifyUserOfNewWeather(Context context){
        Uri todaysWeatherUri = WeatherContract.WeatherEntry.buildWeatherUriWithDate(WeatherDateUtils.normalizeDate(System.currentTimeMillis()));
        Cursor todaysWeatherCursor = context.getContentResolver().query(
                todaysWeatherUri,
                WEATER_NOTIFICATION_PROJECTION,
                null,
                null,
                null
        );

        if(todaysWeatherCursor.moveToFirst()){
            int weatherId = todaysWeatherCursor.getInt(INDEX_WEATHER_ID);
            float high = todaysWeatherCursor.getFloat(INDEX_WEATHER_MAX_TEMP);
            float low = todaysWeatherCursor.getFloat(INDEX_WEATHER_MIN_TEMP);

            Resources resources = context.getResources();
            int largeArtResourceId = WeatherUtils.getArtResourceIdForWeatherCondition(weatherId);
            Bitmap largeIcon = BitmapFactory.decodeResource(resources, largeArtResourceId);
            String notificationTitle =context.getString(R.string.app_name);
            String notificationText = getNotificationText(context, weatherId, high, low);
            int smallArtResourceId = WeatherUtils.getArtResourceIdForWeatherCondition(weatherId);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                    .setSmallIcon(smallArtResourceId)
                    .setLargeIcon(largeIcon)
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationText)
                    .setAutoCancel(true);

            Intent detailIntentForDay = new Intent(context, DetailActivity.class);
            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
            taskStackBuilder.addNextIntentWithParentStack(detailIntentForDay);
            PendingIntent resultPendingIntent = taskStackBuilder
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            notificationBuilder.setContentIntent(resultPendingIntent);
            NotificationManager notificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
            notificationManager.notify(WEATHER_NOTIFICATION_ID, notificationBuilder.build());

            WeatherPreference.saveLastNotificationTime(context, System.currentTimeMillis());
        }

        todaysWeatherCursor.close();
    }

    private static String getNotificationText(Context context, int weatherId, float high, float low) {
        String shortDescriprion = WeatherUtils.weatherTranslate(context, weatherId);

        String notificationFormat = context.getString(R.string.format_notification);

        String notificationText = String.format(notificationFormat,
                shortDescriprion,
                WeatherUtils.formatTemperature(context, high),
                WeatherUtils.formatTemperature(context, low));
        return notificationText;
    }
}
