package com.xcion.player.audio;

/**
 * Author: Kern
 * E-mail: sky580@126.com
 * DateTime: 2020/11/14  00:38
 * Intro:
 */
public class AudioError {

    private int code;
    private String message;

    public AudioError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
