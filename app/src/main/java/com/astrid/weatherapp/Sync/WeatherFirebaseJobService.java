package com.astrid.weatherapp.Sync;


import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by TeenWolf on 22.12.2017.
 */

public class WeatherFirebaseJobService extends JobService {
    private AsyncTask<Void, Void, Void> mFetchWeatherTask;

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        mFetchWeatherTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Context context = getApplicationContext();
                WeatherSyncTask.syncWeather(context);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                jobFinished(jobParameters, false);
            }
        };
        mFetchWeatherTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if(mFetchWeatherTask != null){
            mFetchWeatherTask.cancel(true);
        }
        return true;
    }
}
