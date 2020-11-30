package com.xcion.downloader;

import android.content.Context;
import android.util.Log;

import com.xcion.downloader.broadcast.Broadcaster;
import com.xcion.downloader.db.dao.TaskDaoImpl;
import com.xcion.downloader.db.dao.ThreadDaoImpl;
import com.xcion.downloader.entry.FileInfo;
import com.xcion.downloader.entry.ThreadInfo;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

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
public class DownloadThread implements Runnable, Controller {

    public static final String TAG = "downloader";

    private TaskDaoImpl mTaskDaoImpl;
    private ThreadDaoImpl mThreadDaoImpl;

    private WeakReference<Context> reference;
    private FileInfo fileInfo;
    private ThreadInfo threadInfo;

    private String tempPath;
    private State state = State.START;
    private int mCancelNum = 0;
    private int mStopNum = 0;
    private int mCompleteThreadNum = 0;
    private boolean isDownloading;
    private long lastTimeMillis;
    private long rangeLength;


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

    @Override
    public boolean isDownloading() {
        return isDownloading;
    }

    public void setTaskDaoImpl(TaskDaoImpl taskDaoImpl) {
        this.mTaskDaoImpl = taskDaoImpl;
    }

    public void setThreadDaoImpl(ThreadDaoImpl threadDaoImpl) {
        this.mThreadDaoImpl = threadDaoImpl;
    }

    public DownloadThread(Context context, FileInfo fileInfo, ThreadInfo threadInfo) {
        this.reference = new WeakReference<>(context);
        this.fileInfo = fileInfo;
        this.threadInfo = threadInfo;
        tempPath = Downloader.DownloadOptions.getStoragePath() + "/temp/" + fileInfo.getFileName() + "_" + threadInfo.getThreadId() + ".properties";
    }

    HttpURLConnection httpConn;
    InputStream is;


    @Override
    public void run() {
        try {
            URL url = new URL(fileInfo.getUrl());
            httpConn = (HttpURLConnection) url.openConnection();
            //在头里面请求下载开始位置和结束位置
            httpConn.setRequestProperty("Range", "bytes=" + threadInfo.getStart() + "-" + threadInfo.getEnds());
            httpConn.setRequestMethod("GET");
            httpConn.setRequestProperty("Charset", "UTF-8");
            httpConn.setConnectTimeout(Downloader.DownloadOptions.getConnectTimeout());
            httpConn.setRequestProperty("User-Agent", Downloader.DownloadOptions.getUserAgentProperty());
            httpConn.setRequestProperty("Accept", Downloader.DownloadOptions.getAcceptProperty());
            httpConn.setReadTimeout(Downloader.DownloadOptions.getReadTimeout());
            is = httpConn.getInputStream();
            //创建可设置位置的文件
            RandomAccessFile file = new RandomAccessFile(fileInfo.getFilePath(), "rwd");
            //设置每条线程写入文件的位置
            file.seek(threadInfo.getStart());
            byte[] buffer = new byte[1024 * 4];
            int len;
            //当前子线程的下载位置
            long currentLocation = threadInfo.getStart();
            while ((len = is.read(buffer)) != -1) {
                //取消
                if (state == State.CANCEL) {
                    Log.e(TAG, "---" + fileInfo.getFileName() + "----" + threadInfo.getThreadId() + "-----取消下载！！！！");
                    break;
                }
                //暂停
                if (state == State.PAUSE) {
                    Log.e(TAG, "---" + fileInfo.getFileName() + "----" + threadInfo.getThreadId() + "-----暂停下载！！！！");
                    break;
                }
                isDownloading = true;
                //把下载数据数据写入文件
                file.write(buffer, 0, len);
                rangeLength += len;
                if (threadInfo.getThreadId() == 0) {
                    Log.e("sos-hm", "len>>>" + threadInfo.getStart() + rangeLength);
                }

                if (System.currentTimeMillis() - lastTimeMillis >= 1) {
                    lastTimeMillis = System.currentTimeMillis();
                    synchronized (DownloadThread.this) {
                        //
                        threadInfo.setCurrent(threadInfo.getStart() + rangeLength);
                        //实施进度
                        if (mThreadDaoImpl.isExists(threadInfo.getUrl(), threadInfo.getThreadId())) {
                            mThreadDaoImpl.updateThread(threadInfo.getUrl(), threadInfo.getThreadId(), threadInfo);
                        } else {
                            mThreadDaoImpl.insertThread(threadInfo);
                        }
                        //取出发广播
                        ArrayList<ThreadInfo> infos = (ArrayList<ThreadInfo>) mThreadDaoImpl.getThreadByUrl(fileInfo.getUrl());
                        if (reference.get() != null) {
                            Broadcaster.getInstance(reference.get()).setAction(Downloader.ACTION_DOWNLOADING).setFileInfo(fileInfo).setThreadInfo(infos).send();
                        }
                    }
                }

                currentLocation += len;

                /**********************************************************************************/
            }
            file.close();
            is.close();


            if (state == State.CANCEL) {
                synchronized (DownloadThread.this) {
                    mCancelNum++;
                    if (mCancelNum == mThreadDaoImpl.getThreadByUrl(fileInfo.getUrl()).size()) {
                        File tempFile = new File(tempPath);
                        File realFile = new File(fileInfo.getFilePath());
                        if (tempFile.exists()) {
                            tempFile.delete();
                        }
                        if (realFile.exists()) {
                            realFile.delete();
                        }
                        isDownloading = false;
                        System.gc();
                    }
                }
                return;
            }

            //停止状态不需要删除记录文件
            if (state == State.PAUSE) {
                synchronized (DownloadThread.this) {
                    mStopNum++;
                    String location = String.valueOf(currentLocation);
                    Log.i(TAG, "thread_" + threadInfo.getThreadId() + "_stop, stop location ==> " + currentLocation);
                    //writeConfig(fileInfo.getFileName() + "_record_" + threadInfo.getThreadId(), location);
                    if (mStopNum == mThreadDaoImpl.getThreadByUrl(fileInfo.getUrl()).size()) {
                        //暂停回调
                        System.gc();
                    }
                }
                return;
            }

            Log.i(TAG, "线程【" + threadInfo.getThreadId() + "】下载完毕Ends=" + threadInfo.getEnds() + ";;;;Current=" + threadInfo.getCurrent());
            //writeConfig(fileInfo.getFileName() + "_state_" + threadInfo.getThreadId() 1 + "");
            //mListener.onChildComplete(dEntity.endLocation);
            mCompleteThreadNum++;
            if (mCompleteThreadNum == mThreadDaoImpl.getThreadByUrl(fileInfo.getUrl()).size()) {
                //同步下载状态
                fileInfo.setState(FileInfo.STATE_COMPLETED);
                mTaskDaoImpl.updateTask(fileInfo.getUrl(), fileInfo);

                File configFile = new File(tempPath);
                if (configFile.exists()) {
                    configFile.delete();
                }
                //广播通知下载完成
                if (reference.get() != null) {
                    Broadcaster.getInstance(reference.get()).setAction(Downloader.ACTION_COMPLETED).setFileInfo(fileInfo).send();
                }

                //下载完成
                isDownloading = false;
                System.gc();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Exception>>>" + e.getMessage());
            isDownloading = false;
            //广播通知下载完成
            fileInfo.setState(FileInfo.EXCEPTION);
            fileInfo.setError(e.toString());
            if (reference.get() != null) {
                Broadcaster.getInstance(reference.get()).setAction(Downloader.ACTION_FAILURE).setFileInfo(fileInfo).send();
            }
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpConn != null) {
                httpConn.disconnect();
                httpConn = null;
            }
        }

    }
}