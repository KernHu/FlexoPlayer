package com.xcion.downloader.entry;

import java.io.Serializable;

/**
 * @Author: Kern Hu
 * @E-mail: sky580@126.com
 * @CreateDate: 2020/11/26 19:25
 * @UpdateUser: Kern Hu
 * @UpdateDate: 2020/11/26 19:25
 * @Version: 1.0
 * @Description: java类作用描述
 * @UpdateRemark: 更新说明
 */
public class ThreadInfo implements Serializable {

    public static final int STATE_PAUSE = 1;
    public static final int STATE_DOWNLOADING = 2;
    public static final int STATE_COMPLETE = 3;

    private int threadId;
    private String url;
    private long start = 0;
    private long ends = 0;
    private long current = 0;
    private int state = 0;

    public ThreadInfo() {
    }

    public ThreadInfo(int threadId, String url, long start, long ends, long current, int state) {
        this.threadId = threadId;
        this.url = url;
        this.start = start;
        this.ends = ends;
        this.current = current;
        this.state = state;
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnds() {
        return ends;
    }

    public void setEnds(long ends) {
        this.ends = ends;
    }

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "ThreadInfo{" +
                "threadId=" + threadId +
                ", url='" + url + '\'' +
                ", start=" + start +
                ", ends=" + ends +
                ", current=" + current +
                ", state=" + state +
                '}';
    }
}
