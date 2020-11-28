package com.xcion.downloader.entry;

import java.io.Serializable;

/**
 * @Author: Kern Hu
 * @E-mail: sky580@126.com
 * @CreateDate: 2020/11/26 14:17
 * @UpdateUser: Kern Hu
 * @UpdateDate: 2020/11/26 14:17
 * @Version: 1.0
 * @Description: java类作用描述
 * @UpdateRemark: 更新说明
 */
public class FileInfo implements Serializable {

    public static final int STATE_OK = 1;           //准备OK
    public static final int STATE_DOWNLOADING = 2;  //正在下载
    public static final int STATE_STOPPED = 4;      //下载暂停
    public static final int STATE_COMPLETED = 5;    //下载完成
    public static final int HTTP_ERROR_URL = 101;   //HTTP请求异常，一般跟网络或者URL有关系
    public static final int EXCEPTION = 201;        //SDK抛异常

    private String url = "";
    private String fileName = "";
    private String mimeType = "";
    private long length = 0;
    private int state = 0;
    private String error = "";

    public FileInfo() {
    }

    public FileInfo(String url) {
        this.url = url;
    }

    public FileInfo(String url, String error) {
        this.url = url;
        this.error = error;
    }

    public FileInfo(String url, String fileName, String mimeType, long length) {
        this.url = url;
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.length = length;
    }

    public FileInfo(String url, int state, String error) {
        this.url = url;
        this.state = state;
        this.error = error;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "url='" + url + '\'' +
                ", fileName='" + fileName + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", length=" + length +
                ", state=" + state +
                ", error='" + error + '\'' +
                '}';
    }
}
