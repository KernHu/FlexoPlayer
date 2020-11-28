package com.xcion.downloader.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xcion.downloader.db.ConfigHelp;
import com.xcion.downloader.db.DBHelper;
import com.xcion.downloader.entry.FileInfo;
import com.xcion.downloader.entry.ThreadInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Kern Hu
 * @E-mail: sky580@126.com
 * @CreateDate: 2020/11/26 16:48
 * @UpdateUser: Kern Hu
 * @UpdateDate: 2020/11/26 16:48
 * @Version: 1.0
 * @Description: java类作用描述
 * @UpdateRemark: 更新说明
 */
public class ThreadDaoImpl implements ThreadDao {

    private Context context;
    private DBHelper mHelper = null;

    public ThreadDaoImpl(Context context) {
        this.context = context;
        mHelper = new DBHelper(context);
    }


    @Override
    public void insertThread(ThreadInfo info) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("thread_id", info.getThreadId());
        values.put("url", info.getUrl());
        values.put("start", info.getStart());
        values.put("ends", info.getEnds());
        values.put("current", info.getCurrent());
        values.put("state", info.getState());
        db.insert(ConfigHelp.DB_TABLE_THREAD, null, values);
        db.close();
    }

    @Override
    public void deleteThread(String url) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(ConfigHelp.DB_TABLE_THREAD, "url?", new String[]{url});
        db.close();
    }

    @Override
    public void updateThread(String url, ThreadInfo info) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("thread_id", info.getThreadId());
        values.put("url", info.getUrl());
        values.put("start", info.getStart());
        values.put("ends", info.getEnds());
        values.put("current", info.getCurrent());
        values.put("state", info.getState());
        db.update(ConfigHelp.DB_TABLE_THREAD, values, "url=?", new String[]{"url"});
        db.close();
    }

    @Override
    public List<ThreadInfo> getThreadByUrl(String url) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + ConfigHelp.DB_TABLE_THREAD + " where url = ?", new String[]{url});
        List<ThreadInfo> threadInfos = new ArrayList<>();
        while (cursor.moveToNext()) {
            ThreadInfo info = new ThreadInfo();
            info.setThreadId(cursor.getInt(cursor.getColumnIndex("thread_id")));
            info.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            info.setStart(cursor.getLong(cursor.getColumnIndex("start")));
            info.setEnds(cursor.getLong(cursor.getColumnIndex("ends")));
            info.setCurrent(cursor.getLong(cursor.getColumnIndex("current")));
            info.setState(cursor.getInt(cursor.getColumnIndex("state")));
            threadInfos.add(info);
        }
        cursor.close();
        db.close();
        return threadInfos;
    }

    @Override
    public List<ThreadInfo> getAllThreads() {
        List<ThreadInfo> list = new ArrayList<>();
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + ConfigHelp.DB_TABLE_THREAD, new String[]{});
        ThreadInfo info = null;
        while (cursor.moveToNext()) {
            info = new ThreadInfo();
            info.setThreadId(cursor.getInt(cursor.getColumnIndex("thread_id")));
            info.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            info.setStart(cursor.getLong(cursor.getColumnIndex("start")));
            info.setEnds(cursor.getLong(cursor.getColumnIndex("ends")));
            info.setCurrent(cursor.getLong(cursor.getColumnIndex("current")));
            info.setState(cursor.getInt(cursor.getColumnIndex("state")));
            list.add(info);
        }
        cursor.close();
        db.close();
        return list;
    }

    @Override
    public List<ThreadInfo> getAllThreadsBy(int state) {
        List<ThreadInfo> list = new ArrayList<>();
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + ConfigHelp.DB_TABLE_THREAD + " where state = ?", new String[]{String.valueOf(state)});
        ThreadInfo info = null;
        while (cursor.moveToNext()) {
            info = new ThreadInfo();
            info.setThreadId(cursor.getInt(cursor.getColumnIndex("thread_id")));
            info.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            info.setStart(cursor.getLong(cursor.getColumnIndex("start")));
            info.setEnds(cursor.getLong(cursor.getColumnIndex("ends")));
            info.setCurrent(cursor.getLong(cursor.getColumnIndex("current")));
            info.setState(cursor.getInt(cursor.getColumnIndex("state")));
            list.add(info);
        }
        cursor.close();
        db.close();
        return list;
    }

    @Override
    public boolean isExists(String url) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + ConfigHelp.DB_TABLE_THREAD + " where url = ?",
                new String[]{url});
        while (cursor.moveToNext()) {
            cursor.close();
            db.close();
            return true;
        }
        return false;
    }
}
