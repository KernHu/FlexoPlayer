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

    public String videoUri;
    public ArrayList<String> voiceUriList;
    public ArrayList<StreamTask> streamTasks;
    public ArrayList<String> messageList;

    public MediaTask() {
    }

    public MediaTask(String videoUri, ArrayList<String> voiceUriList, ArrayList<StreamTask> streamTasks, ArrayList<String> messageList) {
        this.videoUri = videoUri;
        this.voiceUriList = voiceUriList;
        this.streamTasks = streamTasks;
        this.messageList = messageList;
    }

    public String getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(String videoUri) {
        this.videoUri = videoUri;
    }

    public List<String> getVoiceUriList() {
        return voiceUriList;
    }

    public void setVoiceUriList(ArrayList<String> voiceUriList) {
        this.voiceUriList = voiceUriList;
    }

    public List<StreamTask> getStreamTasks() {
        return streamTasks;
    }

    public void setStreamTasks(ArrayList<StreamTask> streamTasks) {
        this.streamTasks = streamTasks;
    }

    public List<String> getMessageList() {
        return messageList;
    }

    public void setMessageList(ArrayList<String> messageList) {
        this.messageList = messageList;
    }
}
