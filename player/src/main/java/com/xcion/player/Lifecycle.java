package com.xcion.player;

/**
 * Author: Kern
 * E-mail: sky580@126.com
 * DateTime: 2020/11/18  23:24
 * Intro:
 */
public interface Lifecycle {

    void onCreate();

    void onBindData();

    void onResume();

    void onPause();

    void onLowMemory();

    void onTrimMemory(int level);

    void onDestroy();

}
