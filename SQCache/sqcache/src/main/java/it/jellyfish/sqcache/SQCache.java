package it.jellyfish.sqcache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by luca on 30/01/16.
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

    public static SQCache with(Context context) {
        return new SQCache(getDefaultConfig(), context);
    }

    public static SQCache with(Context context, Config config) {
        return new SQCache(config, context);
    }

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

    public void set(String key, String value) {
        set(key, value, config.duration);
    }

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
