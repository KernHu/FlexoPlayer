package com.xcion.flexoplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xcion.flexoplayer.image.ImageActivity;
import com.xcion.flexoplayer.voice.VoiceActivity;
import com.xcion.flexoplayer.voide.VideoActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mItemImage;
    private TextView mItemVoice;
    private TextView mItemVideo;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mItemImage = (TextView) findViewById(R.id.item_image);
        mItemVoice = (TextView) findViewById(R.id.item_voice);
        mItemVideo = (TextView) findViewById(R.id.item_video);
        mItemImage.setOnClickListener(this);
        mItemVoice.setOnClickListener(this);
        mItemVideo.setOnClickListener(this);
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
        }
    }
}