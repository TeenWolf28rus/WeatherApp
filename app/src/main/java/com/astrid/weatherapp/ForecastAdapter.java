package com.astrid.weatherapp;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.astrid.weatherapp.utilities.WeatherDateUtils;
import com.astrid.weatherapp.utilities.WeatherUtils;

/**
 * Created by TeenWolf on 08.12.2017.
 */

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {
    private  static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;
    private Context mContext;
    final private ForecastAdapterOnClickHandler mClickHandler;
    private Cursor mCursor;
    private boolean mUseTodayLayout;

    public interface ForecastAdapterOnClickHandler{
        void onClick(long date);
    }

    public ForecastAdapter(@NonNull Context context, ForecastAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
        mContext = context;
        mUseTodayLayout = mContext.getResources().getBoolean(R.bool.use_today_layout);
    }

    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId;
        switch(viewType){
            case VIEW_TYPE_TODAY:
                layoutId = R.layout.list_item_forecast_today;
                break;
            case VIEW_TYPE_FUTURE_DAY:
                layoutId = R.layout.forecast_list_item;
                break;
            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }

        View view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        view.setFocusable(true);
        return new ForecastAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        long dateInMillis = mCursor.getLong(MainActivity.INDEX_WEATHER_DATE);
        String dateString = WeatherDateUtils.getFriendlyDateString(mContext, dateInMillis, false);
        holder.dateView.setText(dateString);

        int weatherId = mCursor.getInt(MainActivity.INDEX_WEATHER_CONDITION_ID);
        int weatherImageId = WeatherUtils.getArtResourceIdForWeatherCondition(weatherId);
        holder.iconView.setImageResource(weatherImageId);

        String description = WeatherUtils.weatherTranslate(mContext, weatherId);
        String descriptionAlly = mContext.getString(R.string.a11y_forecast, description);
        holder.descriptionView.setText(description);
        holder.descriptionView.setContentDescription(descriptionAlly);

        float maxTemp = mCursor.getFloat(MainActivity.INDEX_WEATHER_MAX_TEMP);
        String highString = WeatherUtils.formatTemperature(mContext, maxTemp);
        String highAlly = mContext.getString(R.string.a11y_high_temp, highString);
        holder.highTempView.setText(highString);
        holder.highTempView.setContentDescription(highAlly);

        float minTemp = mCursor.getFloat(MainActivity.INDEX_WEATHER_MIN_TEMP);
        String lowString = WeatherUtils.formatTemperature(mContext, minTemp);
        String lowAlly  = mContext.getString(R.string.a11y_low_temp, lowString);
        holder.lowTempView.setText(lowString);
        holder.lowTempView.setContentDescription(lowAlly);
    }

    @Override
    public int getItemViewType(int position) {
        if(mUseTodayLayout && position == 0){
            return VIEW_TYPE_TODAY;
        }else {
            return VIEW_TYPE_FUTURE_DAY;
        }
    }

    @Override
    public int getItemCount() {
        if(null == mCursor) return 0;
        return mCursor.getCount();
    }

   void swapCursor(Cursor newCursor){
        mCursor = newCursor;
        notifyDataSetChanged();
   }

    public class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        final TextView dateView;
        final TextView descriptionView;
        final TextView highTempView;
        final TextView lowTempView;
        final ImageView iconView;

        public ForecastAdapterViewHolder(View itemView) {
            super(itemView);
            dateView = (TextView)itemView.findViewById(R.id.date);
            descriptionView = (TextView)itemView.findViewById(R.id.weather_description);
            highTempView = (TextView)itemView.findViewById(R.id.high_temperature);
            lowTempView = (TextView)itemView.findViewById(R.id.low_temperature);
            iconView = (ImageView)itemView.findViewById(R.id.weather_icon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            long dateInMillis = mCursor.getLong(MainActivity.INDEX_WEATHER_DATE);
            mClickHandler.onClick(dateInMillis);
        }
    }
}
