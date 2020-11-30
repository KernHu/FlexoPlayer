package com.xcion.downloader.db;

import com.xcion.downloader.db.dao.TaskDaoImpl;
import com.xcion.downloader.db.dao.ThreadDaoImpl;
import com.xcion.downloader.entry.FileInfo;
import com.xcion.downloader.entry.ThreadInfo;

import java.util.List;

/**
 * author: Kern Hu
 * email: sky580@126.com
 * data_time: 11/30/20 8:37 PM
 * describe: This is...
 */

public class DBLeader {

    private TaskDaoImpl mTaskDaoImpl;
    private ThreadDaoImpl mThreadDaoImpl;

    public DBLeader(TaskDaoImpl taskDaoImpl, ThreadDaoImpl threadDaoImpl) {
        mTaskDaoImpl = taskDaoImpl;
        mThreadDaoImpl = threadDaoImpl;
    }

    public boolean updateOrReplaceFile(FileInfo fileInfo) {
        if (mTaskDaoImpl.isExists(fileInfo.getUrl())) {
            mTaskDaoImpl.updateTask(fileInfo.getUrl(), fileInfo);
        } else {
            mTaskDaoImpl.insertTask(fileInfo);
        }
        return true;
    }

    public boolean updateOrReplaceThread(ThreadInfo threadInfo) {
        if (mThreadDaoImpl.isExists(threadInfo.getUrl())) {
            mThreadDaoImpl.updateThread(threadInfo.getUrl(), threadInfo.getThreadId(), threadInfo);
        } else {
            mThreadDaoImpl.insertThread(threadInfo);
        }
        return true;
    }


    public FileInfo getTaskByUrl(String url) {
        return mTaskDaoImpl.getTaskByUrl(url);
    }

    public List<ThreadInfo> getThreadByUrl(String url) {
        return mThreadDaoImpl.getThreadByUrl(url);
    }
}
