package com.xcion.player;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.ArrayList;

import androidx.annotation.Nullable;

/**
 * author: Kern Hu
 * email: sky580@126.com
 * data_time: 2020/11/12 6:09 PM
 * describe: This is...
 */

class VoicerService extends Service {

    public static final String KEY_DATA_SOURCE = "key_data_source";
    private ArrayList<String> mDataSource;
    private VoicerBinder mBinder = new VoicerBinder();

    public class VoicerBinder extends Binder {
        public VoicerService getService() {
            return VoicerService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        mDataSource = intent.getStringArrayListExtra(KEY_DATA_SOURCE);


        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
