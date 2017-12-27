package com.astrid.weatherapp.utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;


import com.astrid.weatherapp.data.WeatherPreference;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import static android.content.ContentValues.TAG;

/**
 * Created by TeenWolf on 01.12.2017.
 */

public class NetworkUtils{

    final static String FORECAST_BASE_URL = "https://query.yahooapis.com";

    final static String PATH_PARAM_ONE= "v1";
    final static String PATH_PARAM_TWO= "public";
    final static String PATH_PARAM_THREE= "yql";
    final static String QUERY_PARAM = "q";
    final static String QUERY_PARAM_FORMAT = "format";
    final static String QUERY_PARAM_ENV = "env";

    final static String queryParamStart = "select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=";
    final static String queryParamFormat = "json";
    final static String queryParamEnv = "store://datatables.org/alltableswithkeys";

    public static URL buildUrlWithLocation(String location){
        Uri builtUrl = Uri.parse(FORECAST_BASE_URL).buildUpon()
                .appendPath(PATH_PARAM_ONE)
                .appendPath(PATH_PARAM_TWO)
                .appendPath(PATH_PARAM_THREE)
                .appendQueryParameter(QUERY_PARAM, queryParamStart + "\"" + location + "\")")
                .appendQueryParameter(QUERY_PARAM_FORMAT, queryParamFormat)
                .appendQueryParameter(QUERY_PARAM_ENV, queryParamEnv)
                .build();
        URL url = null;
        try{
            url = new URL(builtUrl.toString());
        }catch (MalformedURLException e) {
            e.printStackTrace();;
        }
        Log.v(TAG, "Built URI " + url);
        return url;
    }

    public static URL buildUrlWithLatitudeLongitude(double latitude, double longitude){
        Uri builtUrl = Uri.parse(FORECAST_BASE_URL).buildUpon()
                .appendPath(PATH_PARAM_ONE)
                .appendPath(PATH_PARAM_TWO)
                .appendPath(PATH_PARAM_THREE)
                .appendQueryParameter(QUERY_PARAM, queryParamStart + "\"(" + latitude + "," + longitude + ")\"" + ")")
                .appendQueryParameter(QUERY_PARAM_FORMAT, queryParamFormat)
                .appendQueryParameter(QUERY_PARAM_ENV, queryParamEnv).build();
        URL url = null;
        try {
            url = new URL(builtUrl.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        Log.v(TAG, "Build URL : " + url);
        return url;
    }

    public static URL getUrl(Context context){
        if(WeatherPreference.isLocationLatLonAvailable(context)){
            double[] preferedCoordinates = WeatherPreference.getLocationCoordinates(context);
            double latitude = preferedCoordinates[0];
            double longitude = preferedCoordinates[1];
            return buildUrlWithLatitudeLongitude(latitude, longitude);
        }else {
            String location = WeatherPreference.getPreferredWeatherLocation(context);
            URL url = buildUrlWithLocation(location);
            Log.v(TAG, "Build URL : " + url);
            return url;
        }
    }

    public  static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try{
            InputStream inputStream = urlConnection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if(hasInput){
                return scanner.next();
            }
            else {
                return null;
            }
        }finally {
            urlConnection.disconnect();
        }
    }
}
