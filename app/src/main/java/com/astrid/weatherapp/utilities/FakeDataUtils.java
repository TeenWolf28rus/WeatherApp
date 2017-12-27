package com.astrid.weatherapp.utilities;

import android.content.ContentValues;
import android.content.Context;

import com.astrid.weatherapp.data.WeatherContract;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by TeenWolf on 15.12.2017.
 */

public class FakeDataUtils {
    private static int [] weatherIds = {4,40,28,9,18,35};

    private static ContentValues createTestWeatherContentvalues(long date){
        ContentValues testWeatherValues = new ContentValues();
        testWeatherValues.put(WeatherContract.WeatherEntry.COLUMN_DATE, date);
        testWeatherValues.put(WeatherContract.WeatherEntry.COLUMN_DEGREES, Math.random()*2);
        testWeatherValues.put(WeatherContract.WeatherEntry.COLUMN_HUMIDITY, Math.random()*100);
        testWeatherValues.put(WeatherContract.WeatherEntry.COLUMN_PRESSURE, 870 + Math.random()*10);
        int maxTemp = (int)(Math.random()*100);
        testWeatherValues.put(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP, maxTemp);
        testWeatherValues.put(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP, maxTemp - (int)(Math.random()*10));
        testWeatherValues.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED, Math.random()*10);
        testWeatherValues.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID, weatherIds[(int)(Math.random()*10%5)]);
        return testWeatherValues;
    }

    public static void insertFakeData(Context context){
        long today = WeatherDateUtils.normalizeDate(System.currentTimeMillis());
        List<ContentValues> fakeValues = new ArrayList<ContentValues>();
        for(int i = 0; i < 7; i++){
            fakeValues.add(FakeDataUtils.createTestWeatherContentvalues(today + TimeUnit.DAYS.toMillis(i)));
        }

        context.getContentResolver().bulkInsert(WeatherContract.WeatherEntry.CONTENT_URI, fakeValues.toArray(new ContentValues[7]));
    }

    public static double getFakePressure(){
        double p = 870 + Math.random()*10;
        return p;
    }
    public  static double getFakeHumidity(){
        double h = Math.random()*100;
        return h;
    }
    public static double getFakeWindSpeed(){
     double wS = Math.random()*10;
     return wS;
    }
    public static double getFakeWindDirection(){
        double wD = Math.random()*2;
        return wD;
    }
}
