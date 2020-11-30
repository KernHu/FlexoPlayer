package com.xcion.flexoplayer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.xcion.downloader.DownloadOptions;
import com.xcion.downloader.Downloader;
import com.xcion.downloader.entry.FileInfo;
import com.xcion.downloader.entry.ThreadInfo;
import com.xcion.downloader.listener.DownloadClient;
import com.xcion.flexoplayer.image.ImageActivity;
import com.xcion.flexoplayer.voice.VoiceActivity;
import com.xcion.flexoplayer.voide.VideoActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mItemImage;
    private TextView mItemVoice;
    private TextView mItemVideo;
    private TextView mItemDownload;

    private Intent mIntent;
    private Downloader mDownloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mItemImage = (TextView) findViewById(R.id.item_image);
        mItemVoice = (TextView) findViewById(R.id.item_voice);
        mItemVideo = (TextView) findViewById(R.id.item_video);
        mItemDownload = (TextView) findViewById(R.id.item_download);

        mItemImage.setOnClickListener(this);
        mItemVoice.setOnClickListener(this);
        mItemVideo.setOnClickListener(this);
        mItemDownload.setOnClickListener(this);


        DownloadOptions mDownloadOptions = new DownloadOptions();
        mDownloadOptions.setConnectTimeout(1000 * 12);
        mDownloadOptions.setReadTimeout(1000 * 12);
        mDownloadOptions.setMaxThreadCount(9);
        mDownloadOptions.setMaxConcurrencyCount(4);
        mDownloadOptions.setStoragePath("/com.xcion.flexoplayer/");
        //
        mDownloader = new Downloader(this);
        mDownloader.setOptions(mDownloadOptions);
        mDownloader.setDownloadClient(mDownloadClient);
        //
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mDownloader != null) {
            mDownloader.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mDownloader != null) {
            mDownloader.unBind();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.item_image:
                mIntent = new Intent(this, ImageActivity.class);
                startActivity(mIntent);
                break;
            case R.id.item_voice:
                mIntent = new Intent(this, VoiceActivity.class);
                startActivity(mIntent);
                break;
            case R.id.item_video:
                mIntent = new Intent(this, VideoActivity.class);
                startActivity(mIntent);
                break;

            case R.id.item_download:

                mDownloader.setTask(new FileInfo("https://vt1.doubanio.com/202011192008/d343d8a2e9997dda6a68d64b5eb2bcd3/view/movie/M/402590258.mp4")).bind();

                break;
        }
    }


    DownloadClient mDownloadClient = new DownloadClient() {
        @Override
        public void onJoinQueue(FileInfo fileInfo) {
            Log.e("sos", "onJoinQueue>" + fileInfo.toString());
        }

        @Override
        public void onStarted(FileInfo fileInfo) {
            Log.e("sos", "onStarted>" + fileInfo.toString());
        }

        @Override
        public void onDownloading(FileInfo fileInfo, ArrayList<ThreadInfo> threadInfo) {
            Log.e("sos", threadInfo.size() + ";;;;onDownloading>" + fileInfo.toString());
        }

        @Override
        public void onStopped(FileInfo fileInfo) {
            Log.e("sos", "onStopped>" + fileInfo.toString());
        }

        @Override
        public void onCancelled(FileInfo fileInfo) {
            Log.e("sos", "onCancelled>" + fileInfo.toString());
        }

        @Override
        public void onCompleted(FileInfo fileInfo) {
            Log.e("sos", "onCompleted>" + fileInfo.toString());
        }

        @Override
        public void onFailure(FileInfo fileInfo) {
            Log.e("sos", "onFailure>" + fileInfo.toString());
        }
    };
}