package com.popfu.dailynote;

import android.app.Application;
import android.content.Context;

/**
 * Created by pengfu on 24/06/2017.
 */

public class DNApp extends Application {


    private static Context instance ;



    public static Context getAppContext(){
        return instance ;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this ;
    }
}
