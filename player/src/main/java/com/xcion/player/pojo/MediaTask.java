package com.xcion.player.pojo;

import java.util.List;

/**
 * Author: Kern
 * E-mail: sky580@126.com
 * DateTime: 2020/11/9  01:31
 * Intro:
 */
public class MediaTask {

    public String videoUri;
    public List<String> voiceUriList;
    public List<String> imageUriList;
    public List<String> messageList;

    public MediaTask() {
    }

    public MediaTask(String videoUri, List<String> voiceUriList, List<String> imageUriList) {
        this.videoUri = videoUri;
        this.voiceUriList = voiceUriList;
        this.imageUriList = imageUriList;
    }

    public MediaTask(String videoUri, List<String> voiceUriList, List<String> imageUriList, List<String> messageList) {
        this.videoUri = videoUri;
        this.voiceUriList = voiceUriList;
        this.imageUriList = imageUriList;
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

    public void setVoiceUriList(List<String> voiceUriList) {
        this.voiceUriList = voiceUriList;
    }

    public List<String> getImageUriList() {
        return imageUriList;
    }

    public void setImageUriList(List<String> imageUriList) {
        this.imageUriList = imageUriList;
    }

    public List<String> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<String> messageList) {
        this.messageList = messageList;
    }

    @Override
    public String toString() {
        return "MediaTask{" +
                "videoUri='" + videoUri + '\'' +
                ", voiceUriList=" + voiceUriList +
                ", imageUriList=" + imageUriList +
                ", messageList=" + messageList +
                '}';
    }
}
