package com.xcion.player.media.callback;

/**
 * author: Kern Hu
 * email: sky580@126.com
 * data_time: 11/14/20 2:39 PM
 * describe: This is...
 */

public enum AudioError {

    NOT_FOUND_DATA_SOURCE(10000, "音频资源不存在"),
    NOT_FOUND_AUDIO_STREAM(10001, "没有找到音频流"),
    DATA_SOURCE_ERROR(10002, "音频源存在异常"),
    DECODE_TIME_OUT(10003, "解码超时"),
    ;


    public int code;
    public String msg;

    AudioError(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "AudioError{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
