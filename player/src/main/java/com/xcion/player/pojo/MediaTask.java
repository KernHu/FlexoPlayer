package com.xcion.player.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Kern
 * E-mail: sky580@126.com
 * DateTime: 2020/11/9  01:31
 * Intro:
 */
public class MediaTask {

    public String title;
    public String videoUri;
    public ArrayList<String> audioUrls;
    public ArrayList<StreamTask> streamTasks;
    public ArrayList<String> messageList;

    public boolean selected;

    public MediaTask() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(String videoUri) {
        this.videoUri = videoUri;
    }

    public ArrayList<String> getAudioUrls() {
        return audioUrls;
    }

    public void setAudioUrls(ArrayList<String> audioUrls) {
        this.audioUrls = audioUrls;
    }

    public ArrayList<StreamTask> getStreamTasks() {
        return streamTasks;
    }

    public void setStreamTasks(ArrayList<StreamTask> streamTasks) {
        this.streamTasks = streamTasks;
    }

    public ArrayList<String> getMessageList() {
        return messageList;
    }

    public void setMessageList(ArrayList<String> messageList) {
        this.messageList = messageList;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "MediaTask{" +
                "videoUri='" + videoUri + '\'' +
                ", audioUrls=" + audioUrls +
                ", streamTasks=" + streamTasks +
                ", messageList=" + messageList +
                '}';
    }
}
