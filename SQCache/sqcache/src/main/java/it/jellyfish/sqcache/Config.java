package it.jellyfish.sqcache;

/**
 * Created by luca on 30/01/16.
 */
public class Config {

    public static final long INFINITE = -1;

    public static final long VERY_SHORT = 1000*5; //5 minutes
    public static final long SHORT = 1000*30; //30 minutes
    public static final long NORMAL = 1000*60; //60 minutes
    public static final long LONG = 1000*60*24; //24h
    public static final long VERY_LONG = 1000*60*24*3; //three days

    public long duration;



    public Config() {
        duration = NORMAL;
    }





}
