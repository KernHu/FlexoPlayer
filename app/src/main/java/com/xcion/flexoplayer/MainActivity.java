package com.xcion.flexoplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xcion.downloader.DownloadOptions;
import com.xcion.downloader.Downloader;
import com.xcion.downloader.entry.FileInfo;
import com.xcion.flexoplayer.image.ImageActivity;
import com.xcion.flexoplayer.voice.VoiceActivity;
import com.xcion.flexoplayer.voide.VideoActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mItemImage;
    private TextView mItemVoice;
    private TextView mItemVideo;
    private TextView mItemDownload;

    private Intent mIntent;
    Downloader mDownloader;

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
        mDownloadOptions.setMaxThreadCount(5);
        mDownloadOptions.setMaxConcurrencyCount(4);
        mDownloadOptions.setStoragePath("/com.xcion.webmage/");
        //
        mDownloader = new Downloader(this);
        mDownloader.setOptions(mDownloadOptions);
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


}