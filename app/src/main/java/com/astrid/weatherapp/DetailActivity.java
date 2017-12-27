package com.astrid.weatherapp;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.astrid.weatherapp.data.WeatherContract;
import com.astrid.weatherapp.databinding.ActivityDetailBinding;
import com.astrid.weatherapp.utilities.WeatherDateUtils;
import com.astrid.weatherapp.utilities.WeatherUtils;

/**
 * Created by TeenWolf on 11.12.2017.
 */

public class DetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{
    private static final String FORECAST_SHARE_HASHTAG = " #WeatherApp";

    public static final String [] WEATHER_DETAIL_PROJECTION = {
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_DEGREES,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID
    };

    public static final int INDEX_WEATHER_DATE = 0;
    public static final int INDEX_WEATHER_MAX_TEMP = 1;
    public static final int INDEX_WEATHER_MIN_TEMP = 2;
    public static final int INDEX_WEATHER_HUMIDITY = 3;
    public static final int INDEX_WEATHER_PRESSURE = 4;
    public static final int INDEX_WEATHER_WIND_SPEED = 5;
    public static final int INDEX_WEATHER_DEGREES = 6;
    public static final int INDEX_WEATHER_CONDITION_ID = 7;

    private static final int ID_DETAIL_LOADER = 353;
    private String mForecastSummary;
    private Uri mUri;

    private ActivityDetailBinding mDetailBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        mUri = getIntent().getData();
        if(mUri == null) throw new NullPointerException("URI for DetailActivity cannot be null");

        getSupportLoaderManager().initLoader(ID_DETAIL_LOADER, null, this);
    }

    private Intent createShareForecastIntent(){
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(mForecastSummary + FORECAST_SHARE_HASHTAG)
                .getIntent();
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        return shareIntent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_settings){
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        if(id == R.id.action_share){
            Intent shareIntent = createShareForecastIntent();
            startActivity(shareIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case ID_DETAIL_LOADER:
                return new CursorLoader(this,
                        mUri,
                        WEATHER_DETAIL_PROJECTION,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        boolean cursorHasValidData = false;
        if(data != null && data.moveToFirst()){
            cursorHasValidData = true;
        }

        if(!cursorHasValidData){
            return;
        }

        long localDateMidnightGmt = data.getLong(INDEX_WEATHER_DATE);
        String dateText = WeatherDateUtils.getFriendlyDateString(this, localDateMidnightGmt, true);
        mDetailBinding.primaryInfo.date.setText(dateText);

        int weatherId = data.getInt(INDEX_WEATHER_CONDITION_ID);
        int weatherImageId = WeatherUtils.getArtResourceIdForWeatherCondition(weatherId);
        mDetailBinding.primaryInfo.weatherIcon.setImageResource(weatherImageId);
        String description = WeatherUtils.weatherTranslate(this, weatherId);
        String descriptionAlly = getString(R.string.a11y_forecast, description);
        mDetailBinding.primaryInfo.weatherDescription.setText(description);
        mDetailBinding.primaryInfo.weatherDescription.setContentDescription(descriptionAlly);
        mDetailBinding.primaryInfo.weatherIcon.setContentDescription(descriptionAlly);

        float highTemp = data.getFloat(INDEX_WEATHER_MAX_TEMP);
        String highTempText = WeatherUtils.formatTemperature(this, highTemp);
        String highAlly = getString(R.string.a11y_high_temp, highTempText);
        mDetailBinding.primaryInfo.highTemperature.setText(highTempText);
        mDetailBinding.primaryInfo.highTemperature.setContentDescription(highAlly);

        float lowTemp = data.getFloat(INDEX_WEATHER_MIN_TEMP);
        String lowTempText = WeatherUtils.formatTemperature(this, lowTemp);
        String lowAlly = getString(R.string.a11y_low_temp, lowTempText);
        mDetailBinding.primaryInfo.lowTemperature.setText(lowTempText);
        mDetailBinding.primaryInfo.lowTemperature.setContentDescription(lowAlly);

        float humidity = data.getFloat(INDEX_WEATHER_HUMIDITY);
        int formatedHumidity = Math.round(humidity);
        String humidityText = String.format(getResources().getString(R.string.format_humidity), formatedHumidity);
        String humidityAlly = getString(R.string.a11y_humidity, humidityText);
        mDetailBinding.extraDetails.humidity.setText(humidityText);
        mDetailBinding.extraDetails.humidity.setContentDescription(humidityAlly);
        mDetailBinding.extraDetails.humidityLabel.setContentDescription(humidityAlly);

        float windSpeed = data.getFloat(INDEX_WEATHER_WIND_SPEED);
        float windDirection = data.getFloat(INDEX_WEATHER_DEGREES);
        String windText = WeatherUtils.getFormattedWind(this, windSpeed, windDirection);
        String windAlly = getString(R.string.a11y_wind, windText);
        mDetailBinding.extraDetails.windMeasurement.setText(windText);
        mDetailBinding.extraDetails.windMeasurement.setContentDescription(windAlly);
        mDetailBinding.extraDetails.windLabel.setContentDescription(windAlly);

        float pressure = data.getFloat(INDEX_WEATHER_PRESSURE);
        int formatedPressure = Math.round(pressure);
        String pressureText = String.format(getResources().getString(R.string.format_pressure), formatedPressure);
        String pressureAlly = getString(R.string.a11y_pressure, pressureText);
        mDetailBinding.extraDetails.pressure.setText(pressureText);
        mDetailBinding.extraDetails.pressure.setContentDescription(pressureAlly);
        mDetailBinding.extraDetails.pressureLabel.setContentDescription(pressureAlly);


        mForecastSummary = String.format("%s - %s - %s/%s",
                dateText, description, highTempText, lowTempText);
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
