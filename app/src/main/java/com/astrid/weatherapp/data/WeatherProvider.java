package com.astrid.weatherapp.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.astrid.weatherapp.utilities.WeatherDateUtils;

/**
 * Created by TeenWolf on 13.12.2017.
 */

public class WeatherProvider extends ContentProvider {
    public static final int CODE_WEATHER = 100;
    public static final int CODE_WEATHER_WITH_DATE = 101;

    private static final UriMatcher sUriMatcher = buildUriMathcer();
    private WeatherDbHelper mOpenHelper;

    private static UriMatcher buildUriMathcer() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = WeatherContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, WeatherContract.PATH_WEATHER, CODE_WEATHER);
        matcher.addURI(authority, WeatherContract.PATH_WEATHER + "/#", CODE_WEATHER_WITH_DATE);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new WeatherDbHelper(getContext());
        return true;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
       final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
       switch (sUriMatcher.match(uri)){
           case CODE_WEATHER:
               db.beginTransaction();
               int rowsInserted =0;
               try{
                   for(ContentValues value:values){
                       long weatherDate =
                               value.getAsLong(WeatherContract.WeatherEntry.COLUMN_DATE);
                       if(!WeatherDateUtils.isDateNormalized(weatherDate)){
                           throw new IllegalArgumentException("Date must be normalized to insert");
                       }

                       long _id = db.insert(WeatherContract.WeatherEntry.TABLE_NAME, null, value);
                       if(_id != -1){
                           rowsInserted++;
                       }
                   }
                   db.setTransactionSuccessful();
           }finally {
                   db.endTransaction();
               }
            if(rowsInserted > 0){
                   getContext().getContentResolver().notifyChange(uri, null);
            }
            return rowsInserted;
            default:
                return  super.bulkInsert(uri, values);
       }
    }

    @Override
    public Cursor query(@NonNull Uri uri,  String[] strings,  String s,  String[] strings1,  String s1) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)){
            case CODE_WEATHER_WITH_DATE:{
                String normalizedUtcDateString = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{normalizedUtcDateString};
                cursor = mOpenHelper.getReadableDatabase().query(
                    WeatherContract.WeatherEntry.TABLE_NAME, strings, WeatherContract.WeatherEntry.COLUMN_DATE + " = ? ",
                    selectionArguments, null, null, s1);
                break;
                }
            case CODE_WEATHER: {
                cursor = mOpenHelper.getReadableDatabase().query(
                        WeatherContract.WeatherEntry.TABLE_NAME,
                        strings,
                        s,
                        strings1,
                        null,
                        null,
                        s1);

                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("We are not implementing getType in WeatherApp.");
    }


    @Override
    public Uri insert(@NonNull Uri uri,  ContentValues contentValues) {
        throw new RuntimeException(
                "We are not implementing insert in WeatherApp. Use bulkInsert instead");
    }

    @Override
    public int delete(@NonNull Uri uri,  String s,  String[] strings) {
       int numRowsDeleted;
       if(s == null) s = "1";

       switch (sUriMatcher.match(uri)){
           case CODE_WEATHER:
               numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                       WeatherContract.WeatherEntry.TABLE_NAME,
                       s,
                       strings);
               break;
           default:
               throw new UnsupportedOperationException("Unknown uri: " + uri);
       }
       if(numRowsDeleted != 0){
           getContext().getContentResolver().notifyChange(uri, null);
       }

       return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri,ContentValues contentValues,  String s, String[] strings) {
        throw new RuntimeException("We are not implementing update in WeatherApp");
    }
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
