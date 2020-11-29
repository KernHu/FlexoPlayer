package com.xcion.downloader;

import android.content.Context;
import android.content.Intent;
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

    private Context mContext;
    private FileInfo mFileInfo;
    private TaskDaoImpl mTaskDaoImpl;
    private ThreadDaoImpl mThreadDaoImpl;

    public DownloadTask(Context context, FileInfo fileInfo) {
        this.mContext = context;
        this.mFileInfo = fileInfo;
        mTaskDaoImpl = new TaskDaoImpl(mContext);
        mThreadDaoImpl = new ThreadDaoImpl(mContext);
        //
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

        //将文件信息同步到数据库
        if (mTaskDaoImpl.isExists(mFileInfo.getUrl())) {
            mTaskDaoImpl.updateTask(mFileInfo.getUrl(), mFileInfo);
        } else {
            mTaskDaoImpl.insertTask(mFileInfo);
        }

        //判断文件信息是否完整，如果完成则开始分割文件，分配线程；反之则广播通知下载失败
        if (mFileInfo.getState() != FileInfo.STATE_OK) {
            //获取文件信息异常，下载失败
            Broadcaster.getInstance(mContext).setAction(Downloader.ACTION_FAILURE).setFileInfo(mFileInfo).send();
            return;
        } else {
            /***************************************************************************************************************************************/
            boolean newTask;
            //判断是否存在临时记录文件，如不存在则重新生成；
            File tempFile = new File(Downloader.DownloadOptions.getStoragePath() + "/temp/" + mFileInfo.getFileName() + ".properties");
            if (!tempFile.exists()) {
                newTask = true;
                FileTool.createFile(tempFile.getPath());
            } else {
                newTask = false;
            }


            //
            try {
                //在本地sd卡创建待下载文件
                mFileInfo.setFilePath(Downloader.DownloadOptions.getStoragePath() + mFileInfo.getFileName());
                FileTool.createFile(mFileInfo.getFilePath());
                //创建一个可读可写的随机访问文件
                RandomAccessFile raf = new RandomAccessFile(mFileInfo.getFilePath(), "rwd");
                //设置文件长度
                raf.setLength(mFileInfo.getLength());
                //分配每条线程的下载区间
                Properties pro = FileTool.loadConfig(tempFile);
                int blockSize = (int) (mFileInfo.getLength() / Downloader.DownloadOptions.getMaxThreadCount());
                SparseArray<Thread> tasks = new SparseArray<>();
                Log.e("sos", "FileInfo>>>" + mFileInfo.toString());
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
                    ThreadInfo threadInfo = new ThreadInfo(i, mFileInfo.getUrl(), startL, endL, 0, ThreadInfo.STATE_PAUSE);
                    //ThreadInfo同步到数据库
                    boolean isExists = mThreadDaoImpl.isExists(threadInfo.getUrl(), threadInfo.getThreadId());
                    if (isExists) {
                        mThreadDaoImpl.updateThread(threadInfo.getUrl(), threadInfo);
                    } else {
                        mThreadDaoImpl.insertThread(threadInfo);
                    }

                    Log.e("sos", "threadInfo>>>" + threadInfo.toString());
                    Object state = pro.getProperty(mFileInfo.getFileName() + "_state_" + i);
                    Log.e("sos", "state>>>" + state);
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
                    if (!newTask && record != null && Long.parseLong(record + "") > 0) {       //如果有记录，则恢复下载
//                        Long r = Long.parseLong(record + "");
//                        mCurrentLocation += r - startL;
//                        Log.d("sos", "++++++++++ 线程_" + i + "_恢复下载 ++++++++++");
//                        startL = r;
                    }

                    DownloadThread task = new DownloadThread(mFileInfo, threadInfo);
                    tasks.put(i, new Thread(task));
                }

                //启动线程下载
                for (int i = 0, count = tasks.size(); i < count; i++) {
                    Thread task = tasks.get(i);
                    if (task != null) {
                        task.start();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("sos", "Exception>>>" + e.getMessage());
            }

        }
    }


}
