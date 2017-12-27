package com.astrid.weatherapp.Sync;

import android.app.IntentService;
import android.content.Intent;


/**
 * Created by TeenWolf on 19.12.2017.
 */

public class WeatherSyncIntentService extends IntentService {
    public WeatherSyncIntentService(){super("WeatherSyncIntentService");}

    @Override
    protected void onHandleIntent(Intent intent) {WeatherSyncTask.syncWeather(this);}
}
