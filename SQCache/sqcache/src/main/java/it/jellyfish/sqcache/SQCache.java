package it.jellyfish.sqcache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Luca Roverelli on 30/01/16.
 *
 * Main class of the library, that implements the cache system.
 *
 */
public class SQCache {

    private Config config;
    private Context context;
    private DB db_helper;

    private SQCache(Config config, Context context) {
        this.config = config;
        this.context = context;
        db_helper = new DB(context);
    }

    private static Config getDefaultConfig() {
        Config c = new Config();
        return c;
    }

    /**
     * Generate a new SQCache object, using the specified Context
     * @param context the context
     * @return a new SQCache object
     */
    public static SQCache with(Context context) {
        return new SQCache(getDefaultConfig(), context);
    }


    /**
     * Generate a new SQCache object, using the specified Context
     * @param context the context
     * @param config Configuration object
     * @return a new SQCache object
     */
    public static SQCache with(Context context, Config config) {
        return new SQCache(config, context);
    }


    /**
     * Getter method to obtain the specified value associated to the key.
     * @param key the key String
     * @return the the cached object, or null if the cache is expired or not yet inserted the value
     */
    public String get(String key) {
        SQLiteDatabase db = db_helper.getReadableDatabase();
        Cursor cursor = db.query(TABLE, null, KEY + "=?", new String[]{key}, null, null, null, null);
        if (cursor.getCount() == 0) {
            cursor.close();
            return null;
        }
        cursor.moveToNext();
        String v = cursor.getString(1);
        long timer = cursor.getLong(2);
        cursor.close();
        long now = System.currentTimeMillis();
        if (timer!=Config.INFINITE && now > timer) {
            toDelete(key);
            return null;
        }
        return v;
    }

    /**
     * Setter method to save with the standard duration a value associated to a key.
     * @param key the key
     * @param value the value to save
     */
    public void set(String key, String value) {
        set(key, value, config.duration);
    }


    /**
     * Setter method to save with a custom duration a value associated to a key.
     * @param key the key
     * @param value the value to save
     * @param customDuration the duration in mills
     */
    public void set(String key, String value, long customDuration) {
        long final_time = System.currentTimeMillis() + customDuration;
        if (customDuration==Config.INFINITE) final_time = Config.INFINITE;
        SQLiteDatabase db = db_helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY, key);
        values.put(VALUE, value);
        values.put(TIMER, final_time);
        db.insertWithOnConflict(TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }


    /**
     * Setter method to save with the standard duration a value associated to a key.
     * @param key the key
     * @param value the value to save
     */
    public void set(String key, int value) {
        set(key, value, config.duration);
    }

    /**
     * Setter method to save with a custom duration a value associated to a key.
     * @param key the key
     * @param value the value to save
     * @param duration the duration in mills
     */
    public void set(String key, int value, long duration) {
        set(key, "" + value, duration);
    }

    /**
     * Setter method to save with the standard duration a value associated to a key.
     * @param key the key
     * @param value the value to save
     */
    public void set(String key, long value) {
        set(key, value, config.duration);
    }

    /**
     * Setter method to save with a custom duration a value associated to a key.
     * @param key the key
     * @param value the value to save
     * @param duration the duration in mills
     */
    public void set(String key, long value, long duration) {
        set(key, ""+value, duration);
    }

    /**
     * Setter method to save with the standard duration a value associated to a key.
     * @param key the key
     * @param value the value to save
     */
    public void set(String key, float value) {
        set(key, value, config.duration);
    }

    /**
     * Setter method to save with a custom duration a value associated to a key.
     * @param key the key
     * @param value the value to save
     * @param duration the duration in mills
     */
    public void set(String key, float value, long duration) {
        set(key, ""+value, duration);
    }

    /**
     * Setter method to save with the standard duration a value associated to a key.
     * @param key the key
     * @param value the value to save
     */
    public void set(String key, double value) {
        set(key, value, config.duration);
    }

    /**
     * Setter method to save with a custom duration a value associated to a key.
     * @param key the key
     * @param value the value to save
     * @param duration the duration in mills
     */
    public void set(String key, double value, long duration) {
        set(key, ""+value, duration);
    }

    /**
     * Setter method to save with the standard duration a value associated to a key.
     * @param key the key
     * @param value the value to save
     */
    public void set(String key, JSONObject value) {
        set(key, value, config.duration);
    }

    /**
     * Setter method to save with a custom duration a value associated to a key.
     * @param key the key
     * @param value the value to save
     * @param duration the duration in mills
     */
    public void set(String key, JSONObject value, long duration) {
        set(key, value.toString(), duration);
    }

    /**
     * Setter method to save with the standard duration a value associated to a key.
     * @param key the key
     * @param value the value to save
     */
    public void set(String key, JSONArray value) {
        set(key, value, config.duration);
    }

    /**
     * Setter method to save with a custom duration a value associated to a key.
     * @param key the key
     * @param value the value to save
     * @param duration the duration in mills
     */
    public void set(String key, JSONArray value, long duration) {
        set(key, value.toString(), duration);
    }



    /**
     * Getter method to obtain the specified value associated to the key.
     * @param key the key String
     * @param defaultValue the default value if the key is not present
     * @return the the cached object, or the default value if the cache is expired or not yet inserted
     */
    public int getInt(String key, int defaultValue) {
        String v = get(key);
        try {
            return Integer.parseInt(v);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Getter method to obtain the specified value associated to the key.
     * @param key the key String
     * @param defaultValue the default value if the key is not present
     * @return the the cached object, or the default value if the cache is expired or not yet inserted
     */
    public long getLong(String key, long defaultValue) {
        String v = get(key);
        try {
            return Long.parseLong(v);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Getter method to obtain the specified value associated to the key.
     * @param key the key String
     * @param defaultValue the default value if the key is not present
     * @return the the cached object, or the default value if the cache is expired or not yet inserted
     */
    public float getFloat(String key, float defaultValue) {
        String v = get(key);
        try {
            return Float.parseFloat(v);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Getter method to obtain the specified value associated to the key.
     * @param key the key String
     * @param defaultValue the default value if the key is not present
     * @return the the cached object, or the default value if the cache is expired or not yet inserted
     */
    public double getDouble(String key, double defaultValue) {
        String v = get(key);
        try {
            return Double.parseDouble(v);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Getter method to obtain the specified value associated to the key.
     * @param key the key String
     * @return the the cached object, or null if the cache is expired or not yet inserted the value
     */
    public JSONObject getJSONObject(String key) {
        String v = get(key);
        try {
            return new JSONObject(v);
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * Getter method to obtain the specified value associated to the key.
     * @param key the key String
     * @return the the cached object, or null if the cache is expired or not yet inserted the value
     */
    public JSONArray getJSONArray(String key) {
        String v = get(key);
        try {
            return new JSONArray(v);
        }
        catch (Exception e) {
            return null;
        }
    }


    private void toDelete(String key) {
        SQLiteDatabase db = db_helper.getWritableDatabase();
        db.delete(TABLE, KEY+"=?", new String[]{key});
        db.close();
    }






    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////                                     IMPLEMENTATION                                     ////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String TABLE = "Cache";
    private static final String KEY = "key";
    private static final String VALUE = "value";
    private static final String TIMER = "timer";


    private static class DB extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "Cache.db";
        private static final int DATABASE_VERSION = 1;

        public DB(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("CREATE TABLE " + TABLE + " (" + KEY + " TEXT, " + VALUE + " TEXT, " +
                    TIMER + " LONG, PRIMARY KEY(" + KEY + "));");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {}
    }




}
