package com.xcion.downloader;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import com.xcion.downloader.broadcast.Broadcaster;
import com.xcion.downloader.db.dao.TaskDaoImpl;
import com.xcion.downloader.db.dao.ThreadDaoImpl;
import com.xcion.downloader.entry.FileInfo;
import com.xcion.downloader.entry.ThreadInfo;
import com.xcion.downloader.utils.FileTool;

import java.io.File;
import java.io.RandomAccessFile;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Properties;

/**
 * @Author: Kern Hu
 * @E-mail: sky580@126.com
 * @CreateDate: 2020/11/26 18:15
 * @UpdateUser: Kern Hu
 * @UpdateDate: 2020/11/26 18:15
 * @Version: 1.0
 * @Description: java类作用描述
 * @UpdateRemark: 更新说明
 */
public class DownloadTask implements Controller {

    private static final String TAG = "downloader";

    private WeakReference<Context> reference;
    private FileInfo mFileInfo;
    private TaskDaoImpl mTaskDaoImpl;
    private ThreadDaoImpl mThreadDaoImpl;

    public DownloadTask(Context context, FileInfo fileInfo) {
        this.reference = new WeakReference<>(context);
        this.mFileInfo = fileInfo;
        this.mTaskDaoImpl = new TaskDaoImpl(reference.get());
        this.mThreadDaoImpl = new ThreadDaoImpl(reference.get());
        FileTool.mkdir(Downloader.DownloadOptions.getStoragePath());
    }

    @Override
    public void startDownload() {

    }

    @Override
    public void pauseDownload() {

    }

    @Override
    public void cancelDownload() {

    }

    @Override
    public boolean isDownloading() {
        return false;
    }

    public void download() {

        /*************************************** 广播通知状态 **************************************/
        //判断文件信息是否完整，如果完成则开始分割文件，分配线程；反之则广播通知下载失败
        if (mFileInfo.getState() != FileInfo.STATE_OK) {
            //获取文件信息异常，下载失败
            Broadcaster.getInstance(reference.get()).setAction(Downloader.ACTION_FAILURE).setFileInfo(mFileInfo).send();
            return;
        } else {
            /*************************************** 广播通知状态 **************************************/
            boolean newTask;
            //判断是否存在临时记录文件，如不存在则重新生成；
            File tempFile = new File(Downloader.DownloadOptions.getStoragePath() + "/temp/" + mFileInfo.getFileName() + ".properties");
            if (!tempFile.exists()) {
                newTask = true;
                FileTool.createFile(tempFile.getPath());
            } else {
                newTask = false;
            }

            /*************************************** 数据库存储 ***************************************/
            //在本地sd卡创建待下载文件
            mFileInfo.setFilePath(FileTool.createFile(Downloader.DownloadOptions.getStoragePath() + mFileInfo.getFileName()).getPath());
            //将文件信息同步到数据库
            if (mTaskDaoImpl.isExists(mFileInfo.getUrl())) {
                mTaskDaoImpl.updateTask(mFileInfo.getUrl(), mFileInfo);
            } else {
                mTaskDaoImpl.insertTask(mFileInfo);
            }
            Log.i(TAG, "开始下载>>>" + mTaskDaoImpl.getTaskByUrl(mFileInfo.getUrl()).toString());
            /***************************************************************************************/

            //
            try {
                //创建一个可读可写的随机访问文件
                RandomAccessFile raf = new RandomAccessFile(mFileInfo.getFilePath(), "rwd");
                //设置文件长度
                raf.setLength(mFileInfo.getLength());
                //分配每条线程的下载区间
                Properties pro = FileTool.loadConfig(tempFile);
                int blockSize = (int) (mFileInfo.getLength() / Downloader.DownloadOptions.getMaxThreadCount());
                SparseArray<Thread> tasks = new SparseArray<>();
                for (int i = 0; i < Downloader.DownloadOptions.getMaxThreadCount(); i++) {
                    //防止分段时丢失精度，
                    long startL, endL;
                    if (i == Downloader.DownloadOptions.getMaxThreadCount() - 1) {
                        startL = i * blockSize;
                        endL = mFileInfo.getLength();
                    } else {
                        startL = i * blockSize;
                        endL = (i + 1) * blockSize;
                    }
                    ThreadInfo threadInfo = new ThreadInfo(i, mFileInfo.getUrl(), startL, endL, 0, ThreadInfo.STATE_WAIT);
                    /****** ---------------------- ThreadInfo同步到数据库 ---------------------- ******/
                    if (mThreadDaoImpl.isExists(threadInfo.getUrl(), threadInfo.getThreadId())) {
                        mThreadDaoImpl.updateThread(threadInfo.getUrl(), i, threadInfo);
                    } else {
                        mThreadDaoImpl.insertThread(threadInfo);
                    }
                    /*********************************************************************************/

                    Object state = pro.getProperty(mFileInfo.getFileName() + "_state_" + i);
                    Log.e(TAG, "state>>>" + state);
                    if (state != null && Integer.parseInt(state + "") == 1) {  //该线程已经完成
                        //                        mCurrentLocation += endL - startL;
                        //                        Log.d("sos", "++++++++++ 线程_" + i + "_已经下载完成 ++++++++++");
                        //                        mCompleteThreadNum++;
                        //                        if (mCompleteThreadNum == THREAD_NUM) {
                        //                            if (configFile.exists()) {
                        //                                configFile.delete();
                        //                            }
                        //                            isDownloading = false;
                        //                            System.gc();
                        //                            return;
                        //                        }
                        continue;
                    }


                    //分配下载位置
                    Object record = pro.getProperty(mFileInfo.getFileName() + "_record_" + i);
                    Log.e(TAG, "record>>>" + record);
                    if (!newTask && record != null && Long.parseLong(record + "") > 0) {       //如果有记录，则恢复下载
                        //                        Long r = Long.parseLong(record + "");
                        //                        mCurrentLocation += r - startL;
                        //                        Log.d("sos", "++++++++++ 线程_" + i + "_恢复下载 ++++++++++");
                        //                        startL = r;
                    }

                    DownloadThread task = new DownloadThread(reference.get(), mFileInfo, threadInfo);
                    task.setTaskDaoImpl(mTaskDaoImpl);
                    task.setThreadDaoImpl(mThreadDaoImpl);
                    tasks.put(i, new Thread(task));
                }
                /*******************************************************************************/
                if (BuildConfig.DEBUG) {
                    List<ThreadInfo> list = mThreadDaoImpl.getThreadByUrl(mFileInfo.getUrl());
                    for (ThreadInfo info : list) {
                        Log.i(TAG, "数据库中取出线程>>>" + info.toString());
                    }
                }
                /*******************************************************************************/
                //广播通知开始下载
                Broadcaster.getInstance(reference.get()).setAction(Downloader.ACTION_STARTED).setFileInfo(mFileInfo).send();
                //启动线程下载
                for (int i = 0, count = tasks.size(); i < count; i++) {
                    Thread task = tasks.get(i);
                    if (task != null) {
                        task.start();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                mFileInfo.setState(FileInfo.EXCEPTION);
                mFileInfo.setError(e.getMessage());
                Broadcaster.getInstance(reference.get()).setAction(Downloader.ACTION_FAILURE).setFileInfo(mFileInfo).send();
                Log.e("sos", "Exception>>>" + e.getMessage());
            }
        }
    }


}
