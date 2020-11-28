package com.xcion.downloader;

import com.xcion.downloader.db.dao.TaskDaoImpl;
import com.xcion.downloader.db.dao.ThreadDaoImpl;
import com.xcion.downloader.entry.FileInfo;
import com.xcion.downloader.entry.ThreadInfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @Author: Kern Hu
 * @E-mail: sky580@126.com
 * @CreateDate: 2020/11/28 9:55
 * @UpdateUser: Kern Hu
 * @UpdateDate: 2020/11/28 9:55
 * @Version: 1.0
 * @Description: java类作用描述
 * @UpdateRemark: 更新说明
 */
public class DownloadThread extends Thread implements Controller {

    private FileInfo fileInfo;
    private ThreadInfo threadInfo;

    private TaskDaoImpl mTaskDaoImpl;
    private ThreadDaoImpl mThreadDaoImpl;

    private String tempPath;
    private State state = State.START;

    public enum State {
        START,
        PAUSE,
        CANCEL,
    }

    @Override
    public void startDownload() {
        state = State.START;
    }

    @Override
    public void pauseDownload() {
        state = State.PAUSE;
    }

    @Override
    public void cancelDownload() {
        state = State.CANCEL;
    }

    public void setTaskDaoImpl(TaskDaoImpl mTaskDaoImpl) {
        this.mTaskDaoImpl = mTaskDaoImpl;
    }

    public void setThreadDaoImpl(ThreadDaoImpl mThreadDaoImpl) {
        this.mThreadDaoImpl = mThreadDaoImpl;
    }

    public DownloadThread(FileInfo fileInfo, ThreadInfo threadInfo) {
        this.fileInfo = fileInfo;
        this.threadInfo = threadInfo;
        tempPath = Downloader.DownloadOptions.getStoragePath() + "/temp/" + fileInfo.getFileName() + "_" + threadInfo.getThreadId() + ".properties";
    }

    @Override
    public void run() {
        super.run();
        try {
            URL url = new URL(fileInfo.getUrl());
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            //在头里面请求下载开始位置和结束位置
            httpConn.setRequestProperty("Range", "bytes=" + threadInfo.getStart() + "-" + threadInfo.getEnds());
            httpConn.setRequestMethod("GET");
            httpConn.setRequestProperty("Charset", "UTF-8");
            httpConn.setConnectTimeout(Downloader.DownloadOptions.getConnectTimeout());
            httpConn.setRequestProperty("User-Agent", Downloader.DownloadOptions.getUserAgentProperty());
            httpConn.setRequestProperty("Accept", Downloader.DownloadOptions.getAcceptProperty());
            httpConn.setReadTimeout(Downloader.DownloadOptions.getReadTimeout());
            InputStream is = httpConn.getInputStream();
            //创建可设置位置的文件
            RandomAccessFile file = new RandomAccessFile(fileInfo.getFileName(), "rwd");
            //设置每条线程写入文件的位置
            file.seek(threadInfo.getStart());
            byte[] buffer = new byte[1024];
            int len;
            //当前子线程的下载位置
            long currentLocation = threadInfo.getStart();
            while ((len = is.read(buffer)) != -1) {
                //把下载数据数据写入文件
                file.write(buffer, 0, len);
                synchronized (DownloadThread.this) {
                    threadInfo.setCurrent(threadInfo.getCurrent() + len);
                }
                currentLocation += len;
            }
            file.close();
            is.close();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
