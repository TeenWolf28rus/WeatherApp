package com.astrid.weatherapp.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;

import com.astrid.weatherapp.R;

/**
 * Created by TeenWolf on 21.12.2017.
 */

public class WeatherPreference {

        public static final String PREF_COORD_LAT = "coord lat";
        public static final String PREF_COORD_LONG = "coord long";

        public static String getPreferredWeatherLocation(Context context) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            String keyForLocation = context.getString(R.string.pref_location_key);
            String defaultLocation = context.getString(R.string.pref_location_default);

            return sharedPreferences.getString(keyForLocation, defaultLocation);
        }

        public static void setLocationDetails(Context context, double lat, double lon){
            SharedPreferences sp =  PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sp.edit();
            editor.putLong(PREF_COORD_LAT, Double.doubleToLongBits(lat));
            editor.putLong(PREF_COORD_LONG, Double.doubleToLongBits(lon));
            editor.apply();
        }

        public static void resetLocationCoordinates(Context context){
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sp.edit();
            editor.remove(PREF_COORD_LAT);
            editor.remove(PREF_COORD_LONG);
            editor.apply();
        }

        public static boolean isMetric(Context context){
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            String keyForUnits = context.getString(R.string.pref_units_key);
            String defaultunits = context.getString(R.string.pref_units_metric);
            String preferedUnits = sharedPreferences.getString(keyForUnits, defaultunits);
            String metric = context.getString(R.string.pref_units_metric);
            boolean userPreferenceMetric;
            if(metric.equals(preferedUnits)){
                userPreferenceMetric = true;
            }else  {
                userPreferenceMetric = false;
            }
            return  userPreferenceMetric;
        }

        public static  boolean isLocationLatLonAvailable(Context context){
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            boolean spContainLat = sp.contains(PREF_COORD_LAT);
            boolean spContainLon = sp.contains(PREF_COORD_LONG);
            boolean spContainLatLon = false;
            if(spContainLat && spContainLon){
                spContainLatLon = true;
            }
            return spContainLatLon;
        }

        public static double[] getLocationCoordinates(Context context){
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            double[] preferredCoordinates = new double[2];
            preferredCoordinates[0] = Double.longBitsToDouble(sp.getLong(PREF_COORD_LAT, Double.doubleToLongBits(0.0)));
            preferredCoordinates[1] = Double.longBitsToDouble(sp.getLong(PREF_COORD_LONG, Double.doubleToLongBits(0.0)));
            return preferredCoordinates;
        }
        public static void saveLastNotificationTime(Context context, long timeOfNotification) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sp.edit();
            String lastNotificationKey = context.getString(R.string.pref_last_notification);
            editor.putLong(lastNotificationKey, timeOfNotification);
            editor.apply();
        }

        public static long getLastNotificationTimeInMillis(Context context) {
            String lastNotificationKey = context.getString(R.string.pref_last_notification);
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            long lastNotificationTime = sp.getLong(lastNotificationKey, 0);
            return lastNotificationTime;
        }

        public static boolean areNotificationsEnabled(Context context){
            String displayNotificationsKey  = context.getString(R.string.pref_enable_notifications_key);
            boolean shouldDisplayNotificationsByDefault = context.getResources().getBoolean(R.bool.show_notifications_by_default);
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            boolean shouldDisplayNotifications = sp.getBoolean(displayNotificationsKey, shouldDisplayNotificationsByDefault);
            return shouldDisplayNotifications;
        }

        public static long getEllapsedTimeSinceLastNotification(Context context) {
            long lastNotificationTimeMillis =
                    WeatherPreference.getLastNotificationTimeInMillis(context);
            long timeSinceLastNotification = System.currentTimeMillis() - lastNotificationTimeMillis;
            return timeSinceLastNotification;
        }
}
