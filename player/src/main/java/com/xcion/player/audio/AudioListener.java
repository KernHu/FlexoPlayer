package com.xcion.player.audio;

/**
 * Author: Kern
 * E-mail: sky580@126.com
 * DateTime: 2020/11/14  00:38
 * Intro:
 */
public interface AudioListener {

    void onFailure(AudioError error);

    void onComplete();
}
