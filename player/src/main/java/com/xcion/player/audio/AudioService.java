package com.xcion.player.audio;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.xcion.player.AudioTracker;

import java.util.ArrayList;

import androidx.annotation.Nullable;

/**
 * author: Kern Hu
 * email: sky580@126.com
 * data_time: 2020/11/12 6:09 PM
 * describe: This is...
 */

public class AudioService extends Service implements AudioListener {

    public static final String KEY_DATA_SOURCE = "key_data_source";

    private AudioBinder mBinder = new AudioBinder();
    private AudioTracker mAudioTracker;
    private ArrayList<String> mDataSource;
    private int position = 0;


    public class AudioBinder extends Binder {
        public AudioService getService() {
            return AudioService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAudioTracker = new AudioTracker(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        mDataSource = intent.getStringArrayListExtra(KEY_DATA_SOURCE);
        startPlay();

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


    public int getCurrentPosition() {
        return 12;
    }

    private void startPlay() {
        Log.e("sos", "startPlay>>>" + position);
        mAudioTracker.setSourceUrl(mDataSource.get(position % mDataSource.size()));
        mAudioTracker.play();
    }

    @Override
    public void onFailure(AudioError error) {

    }

    @Override
    public void onComplete() {
//        position++;
//        startPlay();
    }
}
