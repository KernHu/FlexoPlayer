package com.xcion.downloader;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.SparseArray;

import com.xcion.downloader.db.dao.TaskDaoImpl;
import com.xcion.downloader.db.dao.ThreadDaoImpl;
import com.xcion.downloader.entry.FileInfo;
import com.xcion.downloader.entry.ThreadInfo;
import com.xcion.downloader.utils.FileTool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
        boolean success = FileTool.mkdir(Downloader.DownloadOptions.getStoragePath());
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
            Intent intent = new Intent(Downloader.ACTION_FAILURE);
            intent.putExtra(FileInfo.class.getName(), mFileInfo);
            mContext.sendBroadcast(intent);
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
            newTask = !tempFile.exists();
            //
            try {
                //在本地sd卡创建待下载文件
                FileTool.createFile(mFileInfo.getFileName());
                //创建一个可读可写的随机访问文件
                RandomAccessFile raf = new RandomAccessFile(mFileInfo.getFileName(), "rwd");
                //设置文件长度
                raf.setLength(mFileInfo.getLength());
                //分配每条线程的下载区间
                Properties pro = FileTool.loadConfig(tempFile);
                int blockSize = (int) (mFileInfo.getLength() / Downloader.DownloadOptions.getMaxThreadCount());
                SparseArray<DownloadThread> tasks = new SparseArray<>();
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
                    Object state = pro.getProperty(mFileInfo.getFileName() + "_state_" + i);
                    Log.e("sos", "state>>>" + state);
                    if (state != null && Integer.parseInt(state + "") == 1) {  //该线程已经完成

                        System.gc();
                    }
                    //分配下载位置
                    Object record = pro.getProperty(mFileInfo.getFileName() + "_record_" + i);
                    Log.e("sos", "record>>>" + state);

                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
