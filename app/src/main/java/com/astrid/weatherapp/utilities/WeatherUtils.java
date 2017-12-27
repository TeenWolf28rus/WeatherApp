package com.astrid.weatherapp.utilities;

import android.content.Context;

import com.astrid.weatherapp.R;
import com.astrid.weatherapp.data.WeatherPreference;

/**
 * Created by TeenWolf on 05.12.2017.
 */

public final class WeatherUtils {

    public static float fahrenheitToCelsius(float temp){
        temp = (temp -32)/1.8f;
        return temp;
    }

    public String translateDay(Context context,String day) {
     switch (day){
         case "Mon":
             day = context.getResources().getString(R.string.translate_day_Mon);
             break;
         case "Tue":
             day = context.getResources().getString(R.string.translate_day_Tue);
             break;
         case "Wed":
             day = context.getResources().getString(R.string.translate_day_Wed);
             break;
         case "Thu":
             day = context.getResources().getString(R.string.translate_day_Thu);
             break;
         case "Fri":
             day = context.getResources().getString(R.string.translate_day_Fri);
             break;
         case "Sat":
             day = context.getResources().getString(R.string.translate_day_Sat);
             break;
         case "Sun":
             day = context.getResources().getString(R.string.translate_day_Sun);
             break;
         default: break;
     }

        return day;
    }

    public static String weatherTranslate(Context context, int code){
        String weatherString = null;
        switch (code){
            case 0:
                weatherString = context.getResources().getString(R.string.tornado);
                break;
            case 1:
                weatherString = context.getResources().getString(R.string.tropical_storm);
                break;
            case 2:
                weatherString = context.getResources().getString(R.string.hurricane);
                break;
            case 3:
                weatherString = context.getResources().getString(R.string.severe_thunderstorms);
                break;
            case 4:
                weatherString = context.getResources().getString(R.string.thunderstorms);
                break;
            case 5:
                weatherString = context.getResources().getString(R.string.mixed_rain_and_snow);
                break;
            case 6:
                weatherString = context.getResources().getString(R.string.mixed_rain_and_sleet);
                break;
            case 7:
                weatherString = context.getResources().getString(R.string.mixed_snow_and_sleet);
                break;
            case 8:
                weatherString = context.getResources().getString(R.string.freezing_drizzle);
                break;
            case 9:
                weatherString = context.getResources().getString(R.string.drizzle);
                break;
            case 10:
                weatherString = context.getResources().getString(R.string.freezing_rain);
                break;
            case 11:
            case 12:
                weatherString = context.getResources().getString(R.string.showers);
                break;
            case 13:
                weatherString = context.getResources().getString(R.string.snow_flurries);
                break;
            case 14:
                weatherString = context.getResources().getString(R.string.light_snow_showers);
                break;
            case 16:
                weatherString = context.getResources().getString(R.string.snow);
                break;
            case 17:
                weatherString = context.getResources().getString(R.string.hail);
                break;
            case 18:
                weatherString = context.getResources().getString(R.string.sleet);
                break;
            case 19:
                weatherString = context.getResources().getString(R.string.dust);
                break;
            case 20:
                weatherString = context.getResources().getString(R.string.foggy);
                break;
            case 21:
                weatherString = context.getResources().getString(R.string.haze);
                break;
            case 22:
                weatherString = context.getResources().getString(R.string.smoky);
                break;
            case 23:
            case 24:
                weatherString = context.getResources().getString(R.string.blustery);
                break;
            case 25:
                weatherString = context.getResources().getString(R.string.cold);
                break;
            case 26:
            case 27:
            case 28:
                weatherString = context.getResources().getString(R.string.cloudy);
                break;
            case 29:
            case 30:
                weatherString = context.getResources().getString(R.string.partly_cloudy);
                break;
            case 31:
            case 34:
            case 33:
                weatherString = context.getResources().getString(R.string.clear);
                break;
            case 32:
                weatherString = context.getResources().getString(R.string.sunny);
                break;
            case 35:
                weatherString = context.getResources().getString(R.string.mixed_rain_and_hail);
                break;
            case 36:
                weatherString = context.getResources().getString(R.string.hot);
                break;
            case 37:
                weatherString = context.getResources().getString(R.string.isolated_thunderstorms);
                break;
            case 38:
            case 39:
                weatherString = context.getResources().getString(R.string.scattered_thunderstorms);
                break;
            case 40:
                weatherString = context.getResources().getString(R.string.scattered_showers);
                break;
            case 41:
            case 42:
            case 43:
                weatherString = context.getResources().getString(R.string.heavy_snow);
                break;
            case 44:
                weatherString = context.getResources().getString(R.string.partly_cloudy);
                break;
            case 45:
                weatherString = context.getResources().getString(R.string.thundershowers);
                break;
            case 46:
                weatherString = context.getResources().getString(R.string.snow_flurries);
                break;
            case 47:
                weatherString = context.getResources().getString(R.string.scattered_showers);
                break;
                default:weatherString = context.getResources().getString(R.string.not_available);
        }
        return weatherString;
    }

    public static String formatTemperature(Context context, float temperature){
        int roundedTemp;
        String formattedTemp;
        if(WeatherPreference.isMetric(context)){
            temperature = fahrenheitToCelsius(temperature);
            roundedTemp = Math.round(temperature);
            formattedTemp = String.format(context.getString(R.string.format_temperature_celsius), roundedTemp);
            return formattedTemp;
        }else {
            roundedTemp = Math.round(temperature);
            formattedTemp = String.format(context.getString(R.string.format_temperature_fahrenheit), roundedTemp);
            return formattedTemp;
        }
    }


    public static String formatHightLows(Context context, float high, float low){
        return formatTemperature(context, high) + "/" + formatTemperature(context, low);
    }
    public static String getFormattedWind(Context context, float windSpeed, float degrees) {
        int windFormat = R.string.format_wind_mph;
        int formatedWindSpeed;
        if (WeatherPreference.isMetric(context)) {
            windFormat = R.string.format_wind_kmh;
            windSpeed = .621371192237334f * windSpeed;
        }
        formatedWindSpeed = Math.round(windSpeed);
        String direction = "Unknown";
        if (degrees >= 337.5 || degrees < 22.5) {
            direction = "N";
        } else if (degrees >= 22.5 && degrees < 67.5) {
            direction = "NE";
        } else if (degrees >= 67.5 && degrees < 112.5) {
            direction = "E";
        } else if (degrees >= 112.5 && degrees < 157.5) {
            direction = "SE";
        } else if (degrees >= 157.5 && degrees < 202.5) {
            direction = "S";
        } else if (degrees >= 202.5 && degrees < 247.5) {
            direction = "SW";
        } else if (degrees >= 247.5 && degrees < 292.5) {
            direction = "W";
        } else if (degrees >= 292.5 && degrees < 337.5) {
            direction = "NW";
        }
        String formatedWind = String.format(context.getString(windFormat), formatedWindSpeed) + String.format(context.getResources().getString(R.string.format_direction), direction);
        return formatedWind;
    }

    public static int getArtResourceIdForWeatherCondition(int code){
        switch (code){
            case 0:
            case 1:
            case 2:
                return R.mipmap.ic_tornado;
            case 3:
            case 4:
            case 37:
            case 38:
            case 39:
            case 45:
                return R.mipmap.ic_rain_thunder;
            case 5:
            case 6:
            case 7:
            case 18:
                return R.mipmap.ic_rain_snow;
            case 8:
            case 9:
            case 25:
                return R.mipmap.ic_cold;
            case 10:
            case 11:
            case 12:
                return R.mipmap.ic_rain;
            case 13:
            case 14:
            case 16:
                return R.mipmap.ic_snow;
            case 17:
            case 35:
                return R.mipmap.ic_ice;
            case 19:
            case 20:
            case 21:
            case 22:
                return R.mipmap.ic_foggy;
            case 23:
            case 24:
                return R.mipmap.ic_blustery;
            case 26:
            case 27:
            case 28:
                return R.mipmap.ic_cloudy;
            case 29:
            case 30:
            case 44:
                return R.mipmap.ic_partly_cloudy;
            case 31:
            case 32:
            case 34:
            case 33:
                return R.mipmap.ic_clear;
            case 36:
                return R.mipmap.ic_hot;
            case 40:
            case 47:
                return R.mipmap.ic_heavy_rain;
            case 41:
            case 42:
            case 43:
            case 46:
               return R.mipmap.ic_heavy_snow;
            default:return R.mipmap.ic_not_available;
        }
    }
}
