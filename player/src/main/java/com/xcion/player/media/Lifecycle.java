package com.xcion.player.media;

import com.xcion.player.enum1.DisplayMode;

/**
 * Author: Kern
 * E-mail: sky580@126.com
 * DateTime: 2020/11/18  23:24
 * Intro:
 */
public interface Lifecycle<T> {

    void setMediaTask(T tasks);

    void setMediaTask(T tasks, boolean isAppend);

    void startPlay();

    void stopPlay();

    void lowMemory();

    void trimMemory(int level);

    void recycle();

    void setAudioVolume(int level);

    void setVideoVolume(int level);

    void setVideoArea(int topLeftX, int topLeftY, int bottomRightX, int bottomRightY);

    void setDisplayAspectRatio(DisplayMode mode);

    void setDisplayOrientation(int value);
}
