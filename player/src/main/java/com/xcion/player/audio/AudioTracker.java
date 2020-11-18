package com.xcion.player.audio;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.xcion.player.audio.AudioError;
import com.xcion.player.audio.AudioListener;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author: Kern
 * E-mail: sky580@126.com
 * DateTime: 2020/11/13  23:01
 * Intro:
 */
public class AudioTracker {


    private ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

}
