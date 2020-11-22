package com.xcion.player;

import com.xcion.player.pojo.MediaTask;

import java.util.List;

/**
 * Author: Kern
 * E-mail: sky580@126.com
 * DateTime: 2020/11/18  23:24
 * Intro:
 */
public interface Lifecycle<T> {

    void setMediaTask(List<T> tasks);

    void setMediaTask(List<T> tasks, boolean isAppend);

    void startPlay();

    void stopPlay();

    void lowMemory();

    void trimMemory(int level);

    void recycle();

}
