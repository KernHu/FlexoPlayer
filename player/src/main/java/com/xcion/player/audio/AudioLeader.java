package com.xcion.player.audio;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;


import java.util.ArrayList;

/**
 * author: Kern Hu
 * email: sky580@126.com
 * data_time: 2020/11/12 7:56 PM
 * describe: This is...
 */

public class AudioLeader {

    private static AudioLeader mAudio;
    private Context mContext;
    private Intent mIntent;

    public static AudioLeader getInstance(Context context) {
        if (mAudio == null) {
            mAudio = new AudioLeader(context);
        }
        return mAudio;
    }

    public AudioLeader(Context context) {
        mContext = context;

    }

    public void setDataSource(ArrayList<String> sources) {

        mIntent = new Intent(mContext, AudioService.class);
        mIntent.putStringArrayListExtra(AudioService.KEY_DATA_SOURCE, sources);
        mContext.bindService(mIntent, mServiceConnection, Activity.BIND_AUTO_CREATE);

    }

    /**********************************************************************************/
    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AudioService.AudioBinder binder = (AudioService.AudioBinder) iBinder;
            AudioService service = binder.getService();
            service.getCurrentPosition();
            Log.e("sos", "onServiceConnected>>>");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e("sos", "onServiceDisconnected>>>");
        }
    };

    /**********************************************************************************/
    public void onDestroy() {

        if (mIntent != null && mContext != null) {
            mContext.stopService(mIntent);
        }
        mAudio = null;
    }
}
