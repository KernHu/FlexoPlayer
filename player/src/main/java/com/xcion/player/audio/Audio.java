package com.xcion.player.audio;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;


import java.util.ArrayList;

/**
 * author: Kern Hu
 * email: sky580@126.com
 * data_time: 2020/11/12 7:56 PM
 * describe: This is...
 */

public class Audio {

    private static Audio mAudio;
    private Context mContext;
    private Intent mIntent;

    public Audio getInstance(Context context) {
        synchronized (Audio.this) {
            if (mAudio == null) {
                mAudio = new Audio(context);
            }
            return mAudio;
        }
    }

    public Audio(Context context) {
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
            AudioService.VoicerBinder binder = (AudioService.VoicerBinder) iBinder;
            AudioService service = binder.getService();
            service.getCurrentPosition();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

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
