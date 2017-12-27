package com.astrid.weatherapp.utilities;


import android.content.ContentValues;
import android.content.Context;

import com.astrid.weatherapp.R;
import com.astrid.weatherapp.data.WeatherPreference;
import com.astrid.weatherapp.data.WeatherContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by TeenWolf on 05.12.2017.
 */

public final class ParseJsonUtils {
    private static final String OWM_QUERY = "query";
    private static final String OWM_RESULTS = "results";
    private static final String OWM_CHANNEL = "channel";
    private static final String OWM_ITEM = "item";
    private static final String OWM_FORECAST = "forecast";
    private static final String OWM_ATMOSPHERE = "atmosphere";
    private static final String OWM_WIND = "wind";

    private static final String OWM_MAX = "high";
    private static final String OWM_MIN = "low";

    private static final String OWM_CODE = "code";

    private static final String OWM_LATITUDE = "lat";
    private static  final String OWM_LONGITUDE = "long";

    private static final String OWM_PRESSURE = "pressure";
    private static final String OWM_HUMIDITY = "humidity";
    private static final String OWM_WINDSPEED = "speed";
    private static final String OWM_WIND_DIRECTION = "direction";

    public static ContentValues[] getSimpleWeatherStringFromJson(Context context, String forecastJsonStr) throws JSONException{
         JSONObject forecastJson = new JSONObject(forecastJsonStr);


        JSONObject weatherArrayQuery = forecastJson.getJSONObject(OWM_QUERY);
        JSONObject weatherArrayResults = weatherArrayQuery.getJSONObject(OWM_RESULTS);
        JSONObject weatherArrayChannel = weatherArrayResults.getJSONObject(OWM_CHANNEL);

        JSONObject weatherArrayWind = weatherArrayChannel.getJSONObject(OWM_WIND);
        JSONObject weatherArrayAtmosphere = weatherArrayChannel.getJSONObject(OWM_ATMOSPHERE);
        JSONObject weatherArrayItem = weatherArrayChannel.getJSONObject(OWM_ITEM);

        double cityLatitude = weatherArrayItem.getDouble(OWM_LATITUDE);
        double cityLongitude = weatherArrayItem.getDouble(OWM_LONGITUDE);
        WeatherPreference.setLocationDetails(context, cityLatitude, cityLongitude);
        JSONArray weatherArray = weatherArrayItem.getJSONArray(OWM_FORECAST);

        long startDay = WeatherDateUtils.getNormalizedUtcDateForToday();

        ContentValues[] weatherConentValues = new ContentValues[weatherArray.length()];
        for(int i = 0; i < weatherArray.length(); i++){
            int code;
            float high;
            float low;
            long dateTimeMillis;
            double pressure;
            double humidity;
            double windSpeed;
            double windDirection;

            JSONObject dayForecastJsonObj = weatherArray.getJSONObject(i);
            dateTimeMillis = startDay + WeatherDateUtils.DAY_IN_MILLIS * i;
            if(i==0){
                pressure = weatherArrayAtmosphere.getDouble(OWM_PRESSURE);
                humidity = weatherArrayAtmosphere.getDouble(OWM_HUMIDITY);
                windSpeed = weatherArrayWind.getDouble(OWM_WINDSPEED);
                windDirection = weatherArrayWind.getDouble(OWM_WIND_DIRECTION);
            }else {
                pressure = FakeDataUtils.getFakePressure();
                humidity = FakeDataUtils.getFakeHumidity();
                windSpeed = FakeDataUtils.getFakeWindSpeed();
                windDirection = FakeDataUtils.getFakeWindDirection();
            }

            high = (float) dayForecastJsonObj.getDouble(OWM_MAX);
            low = (float) dayForecastJsonObj.getDouble(OWM_MIN);
            code = dayForecastJsonObj.getInt(OWM_CODE);

            ContentValues weatherValues = new ContentValues();
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID, code);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DATE, dateTimeMillis);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP, low);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP, high);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_PRESSURE, pressure);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_HUMIDITY, humidity);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED, windSpeed);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DEGREES, windDirection);
            weatherConentValues[i] = weatherValues;
        }
        return weatherConentValues;
    }
}
