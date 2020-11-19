package com.xcion.flexoplayer;

import com.xcion.player.pojo.MediaTask;
import com.xcion.player.pojo.StreamTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Kern
 * E-mail: sky580@126.com
 * DateTime: 2020/11/19  01:32
 * Intro:
 */
public class DataUtils {


    public static ArrayList<MediaTask> getTask() {

        ArrayList<MediaTask> mediaTasks = new ArrayList<>();
        MediaTask task = new MediaTask();
        task.setVideoUri("https://vt1.doubanio.com/202011192008/d343d8a2e9997dda6a68d64b5eb2bcd3/view/movie/M/402590258.mp4");
        ArrayList<String> voiceUris = new ArrayList<>();
        voiceUris.add("https://mallcos.heli33.com/xcion/TheGiverReprise.mp3");
        voiceUris.add("https://mallcos.heli33.com/xcion/TheTingTings.mp3");
        voiceUris.add("https://mallcos.heli33.com/xcion/ThisCentury.mp3");
        task.setAudioUrls(voiceUris);
        ArrayList<StreamTask> streamTasks = new ArrayList<>();
        streamTasks.add(new StreamTask(StreamTask.TYPE_IMAGE, "https://img3.doubanio.com/view/subject/m/public/s4515951.jpg"));
        streamTasks.add(new StreamTask(StreamTask.TYPE_IMAGE, "https://img2.doubanio.com/view/photo/l/public/p1272961663.webp"));
        streamTasks.add(new StreamTask(StreamTask.TYPE_IMAGE, "https://img9.doubanio.com/view/photo/l/public/p2318079156.webp"));
        streamTasks.add(new StreamTask(StreamTask.TYPE_IMAGE, "https://img3.doubanio.com/view/photo/l/public/p2357118350.webp"));
        task.setStreamTasks(streamTasks);
        ArrayList<String> messageList = new ArrayList<>();
        task.setMessageList(messageList);
        mediaTasks.add(task);

        return mediaTasks;
    }


}
