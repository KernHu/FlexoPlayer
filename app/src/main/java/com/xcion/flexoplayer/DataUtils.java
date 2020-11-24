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
        MediaTask task1 = new MediaTask();
        task1.setTitle("元旦--XCION-播放任务1");
        task1.setVideoUri("https://vt1.doubanio.com/202011192008/d343d8a2e9997dda6a68d64b5eb2bcd3/view/movie/M/402590258.mp4");
        ArrayList<String> voiceUris1 = new ArrayList<>();
        voiceUris1.add("https://mr3.doubanio.com/b34a3d1d57508ed942a0f1345eecf369/0/fm/song/p1423875_128k.mp3");
        voiceUris1.add("https://mr3.doubanio.com/34660d788b219a6ec974df199dc5b8e7/0/fm/song/p988598_128k.mp3");
        voiceUris1.add("https://mr1.doubanio.com/81c4ba06752bf36fbc4646cfca40efb3/1/fm/song/p125583_128k.mp4");
        task1.setAudioUrls(voiceUris1);
        ArrayList<StreamTask> streamTasks1 = new ArrayList<>();
        streamTasks1.add(new StreamTask(StreamTask.TYPE_IMAGE, "https://images.pexels.com/photos/1382731/pexels-photo-1382731.jpeg?auto=compress&cs=tinysrgb&h=750&w=1260"));
        streamTasks1.add(new StreamTask(StreamTask.TYPE_IMAGE, "https://images.pexels.com/photos/1391499/pexels-photo-1391499.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
        streamTasks1.add(new StreamTask(StreamTask.TYPE_IMAGE, "https://images.pexels.com/photos/1187822/pexels-photo-1187822.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
        streamTasks1.add(new StreamTask(StreamTask.TYPE_IMAGE, "https://images.pexels.com/photos/1854129/pexels-photo-1854129.jpeg?auto=compress&cs=tinysrgb&dpr=3&h=750&w=1260"));
        task1.setStreamTasks(streamTasks1);
        ArrayList<String> messageList1 = new ArrayList<>();
        task1.setMessageList(messageList1);
        mediaTasks.add(task1);

        MediaTask task2 = new MediaTask();
        task2.setTitle("元旦--XCION-播放任务1");
        task2.setVideoUri("https://vt1.doubanio.com/202011192008/d343d8a2e9997dda6a68d64b5eb2bcd3/view/movie/M/402590258.mp4");
        ArrayList<String> voiceUris2 = new ArrayList<>();
        voiceUris2.add("https://mr3.doubanio.com/b34a3d1d57508ed942a0f1345eecf369/0/fm/song/p1423875_128k.mp3");
        voiceUris2.add("https://mr3.doubanio.com/34660d788b219a6ec974df199dc5b8e7/0/fm/song/p988598_128k.mp3");
        voiceUris2.add("https://mr1.doubanio.com/81c4ba06752bf36fbc4646cfca40efb3/1/fm/song/p125583_128k.mp4");
        task2.setAudioUrls(voiceUris2);
        ArrayList<StreamTask> streamTasks2 = new ArrayList<>();
        streamTasks2.add(new StreamTask(StreamTask.TYPE_IMAGE, "https://images.pexels.com/photos/1382731/pexels-photo-1382731.jpeg?auto=compress&cs=tinysrgb&h=750&w=1260"));
        streamTasks2.add(new StreamTask(StreamTask.TYPE_IMAGE, "https://images.pexels.com/photos/1391499/pexels-photo-1391499.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
        streamTasks2.add(new StreamTask(StreamTask.TYPE_IMAGE, "https://images.pexels.com/photos/1187822/pexels-photo-1187822.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
        streamTasks2.add(new StreamTask(StreamTask.TYPE_IMAGE, "https://images.pexels.com/photos/1854129/pexels-photo-1854129.jpeg?auto=compress&cs=tinysrgb&dpr=3&h=750&w=1260"));
        task2.setStreamTasks(streamTasks2);
        ArrayList<String> messageList2 = new ArrayList<>();
        task2.setMessageList(messageList2);
        mediaTasks.add(task2);

        MediaTask task3 = new MediaTask();
        task3.setTitle("元旦--XCION-播放任务1");
        task3.setVideoUri("https://vt1.doubanio.com/202011192008/d343d8a2e9997dda6a68d64b5eb2bcd3/view/movie/M/402590258.mp4");
        ArrayList<String> voiceUris3 = new ArrayList<>();
        voiceUris3.add("https://mr3.doubanio.com/b34a3d1d57508ed942a0f1345eecf369/0/fm/song/p1423875_128k.mp3");
        voiceUris3.add("https://mr3.doubanio.com/34660d788b219a6ec974df199dc5b8e7/0/fm/song/p988598_128k.mp3");
        voiceUris3.add("https://mr1.doubanio.com/81c4ba06752bf36fbc4646cfca40efb3/1/fm/song/p125583_128k.mp4");
        task3.setAudioUrls(voiceUris3);
        ArrayList<StreamTask> streamTasks3 = new ArrayList<>();
        streamTasks3.add(new StreamTask(StreamTask.TYPE_IMAGE, "https://images.pexels.com/photos/1382731/pexels-photo-1382731.jpeg?auto=compress&cs=tinysrgb&h=750&w=1260"));
        streamTasks3.add(new StreamTask(StreamTask.TYPE_IMAGE, "https://images.pexels.com/photos/1391499/pexels-photo-1391499.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
        streamTasks3.add(new StreamTask(StreamTask.TYPE_IMAGE, "https://images.pexels.com/photos/1187822/pexels-photo-1187822.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
        streamTasks3.add(new StreamTask(StreamTask.TYPE_IMAGE, "https://images.pexels.com/photos/1854129/pexels-photo-1854129.jpeg?auto=compress&cs=tinysrgb&dpr=3&h=750&w=1260"));
        task3.setStreamTasks(streamTasks3);
        ArrayList<String> messageList3 = new ArrayList<>();
        task3.setMessageList(messageList3);
        mediaTasks.add(task3);

        return mediaTasks;
    }


}
