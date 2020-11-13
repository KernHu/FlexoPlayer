package com.xcion.player;

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

public class Voicer {

    private static Voicer mVoicer;
    private Context mContext;
    private Intent mIntent;

    public Voicer getInstance(Context context) {
        synchronized (Voicer.this) {
            if (mVoicer == null) {
                mVoicer = new Voicer(context);
            }
            return mVoicer;
        }
    }

    public Voicer(Context context) {
        mContext = context;


    }

    public void setDataSource(ArrayList<String> sources) {

        mIntent = new Intent(mContext, VoicerService.class);
        mIntent.putStringArrayListExtra(VoicerService.KEY_DATA_SOURCE, sources);
        mContext.bindService(mIntent, mServiceConnection, Activity.BIND_AUTO_CREATE);

    }

    /**********************************************************************************/
    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            VoicerService.VoicerBinder binder = (VoicerService.VoicerBinder) iBinder;
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
        mVoicer = null;
    }
}
