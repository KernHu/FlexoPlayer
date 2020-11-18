package com.xcion.player.pojo;

/**
 * author: Kern Hu
 * email: sky580@126.com
 * data_time: 11/18/20 8:35 PM
 * describe: This is...
 */

public class StreamTask {

    public static final int TYPE_TEXT = 0;
    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_GIF = 2;

    public int type;

    public String detail;

    public StreamTask(int type, String detail) {
        this.type = type;
        this.detail = detail;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
