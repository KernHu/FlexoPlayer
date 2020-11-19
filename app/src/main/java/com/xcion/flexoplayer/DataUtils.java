package com.xcion.flexoplayer;

import com.xcion.player.pojo.MediaTask;
import com.xcion.player.pojo.StreamTask;

import java.util.ArrayList;

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
        voiceUris.add("https://mr3.doubanio.com/b34a3d1d57508ed942a0f1345eecf369/0/fm/song/p1423875_128k.mp3");
        voiceUris.add("https://mr3.doubanio.com/34660d788b219a6ec974df199dc5b8e7/0/fm/song/p988598_128k.mp3");
        voiceUris.add("https://mr1.doubanio.com/81c4ba06752bf36fbc4646cfca40efb3/1/fm/song/p125583_128k.mp4");
        task.setAudioUrls(voiceUris);
        ArrayList<StreamTask> streamTasks = new ArrayList<>();
        streamTasks.add(new StreamTask(StreamTask.TYPE_IMAGE, "https://images.pexels.com/photos/1382731/pexels-photo-1382731.jpeg?auto=compress&cs=tinysrgb&h=750&w=1260"));
        streamTasks.add(new StreamTask(StreamTask.TYPE_IMAGE, "https://images.pexels.com/photos/1391499/pexels-photo-1391499.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
        streamTasks.add(new StreamTask(StreamTask.TYPE_IMAGE, "https://images.pexels.com/photos/1187822/pexels-photo-1187822.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
        streamTasks.add(new StreamTask(StreamTask.TYPE_IMAGE, "https://images.pexels.com/photos/1854129/pexels-photo-1854129.jpeg?auto=compress&cs=tinysrgb&dpr=3&h=750&w=1260"));
        task.setStreamTasks(streamTasks);
        ArrayList<String> messageList = new ArrayList<>();
        task.setMessageList(messageList);
        mediaTasks.add(task);

        return mediaTasks;
    }


}
