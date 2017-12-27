package com.astrid.weatherapp.Sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.text.format.DateUtils;

import com.astrid.weatherapp.data.WeatherContract;
import com.astrid.weatherapp.data.WeatherPreference;
import com.astrid.weatherapp.utilities.NetworkUtils;
import com.astrid.weatherapp.utilities.NotificationUtils;
import com.astrid.weatherapp.utilities.ParseJsonUtils;
import com.astrid.weatherapp.utilities.WeatherUtils;

import java.net.URL;

/**
 * Created by TeenWolf on 19.12.2017.
 */

public class WeatherSyncTask {
    synchronized public static void  syncWeather(Context context){

        try{
            URL weatherRequestUrl = NetworkUtils.getUrl(context);
            String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);
            ContentValues[] weatherValues = ParseJsonUtils.getSimpleWeatherStringFromJson(context, jsonWeatherResponse);

            if(weatherValues != null && weatherValues.length !=0){
                ContentResolver weatherContentResolver = context.getContentResolver();
                weatherContentResolver.delete(WeatherContract.WeatherEntry.CONTENT_URI, null, null);
                weatherContentResolver.bulkInsert(WeatherContract.WeatherEntry.CONTENT_URI, weatherValues);

                boolean notificationsEnabled = WeatherPreference.areNotificationsEnabled(context);
                long timeLastNotification  = WeatherPreference.getEllapsedTimeSinceLastNotification(context);
                boolean oneDayPassedSinceLastNotification = false;
                if(timeLastNotification >= DateUtils.DAY_IN_MILLIS){
                    oneDayPassedSinceLastNotification = true;
                }
                if(notificationsEnabled && oneDayPassedSinceLastNotification){
                    NotificationUtils.notifyUserOfNewWeather(context);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
