package com.xcion.flexoplayer.voice;

import android.app.Service;
import android.content.Intent;
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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mDataSource = intent.getStringArrayListExtra(KEY_DATA_SOURCE);


        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
