package com.xcion.flexoplayer.voice;

import android.content.Context;
import android.content.Intent;

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
        mContext.startActivity(mIntent);

    }


    public void onDestroy() {

        if (mIntent != null && mContext != null) {
            mContext.stopService(mIntent);
        }
        mVoicer = null;
    }
}
