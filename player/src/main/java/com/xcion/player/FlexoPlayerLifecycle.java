package com.xcion.player;

/**
 * Author: Kern
 * E-mail: sky580@126.com
 * DateTime: 2020/11/19  01:11
 * Intro:
 */
public interface FlexoPlayerLifecycle extends Lifecycle {

    void onCreateStream();

    void onCreateAudio();

    void onCreateVideo();
}
