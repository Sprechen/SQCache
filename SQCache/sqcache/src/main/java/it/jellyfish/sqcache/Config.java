package it.jellyfish.sqcache;

/**
 * Created by Luca Roverelli on 30/01/16.
 *
 * Helper class to configure SQCache.
 *
 */
public class Config {

    public static final long INFINITE = -1;
    public static final long FIVE_MINUTES = 1000*5; //5 minutes
    public static final long HALF_HOUR = 1000*30; //30 minutes
    public static final long ONE_HOUR = 1000*60; //60 minutes
    public static final long ONE_DAY = 1000*60*24; //24h
    public static final long THREE_DAY = 1000*60*24*3; //three days
    public static final long ONE_WEEK = 1000*60*24*7; //one week
    public static final long ONE_MONTH = 1000*60*24*7*4; //one month!

    public long duration;


    /**
     * Create a new Config object with default values.
     * The default duration is ONE_HOUR
     */
    public Config() {
        duration = ONE_HOUR;
    }

    /**
     * Create a new Config object with custom values.
     * @param duration the custom duration
     */
    public Config(long duration) {
        this.duration = duration;
    }






}
