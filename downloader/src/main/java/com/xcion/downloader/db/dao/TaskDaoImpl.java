package com.xcion.downloader.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xcion.downloader.db.ConfigHelp;
import com.xcion.downloader.db.DBHelper;
import com.xcion.downloader.entry.FileInfo;

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
public class TaskDaoImpl implements TaskDao {

    private Context context;
    private DBHelper mHelper = null;

    public TaskDaoImpl(Context context) {
        this.context = context;
        mHelper = new DBHelper(context);
    }

    @Override
    public void insertTask(FileInfo info) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("url", info.getUrl());
        values.put("filename", info.getFileName());
        values.put("mimetype", info.getMimeType());
        values.put("filepath", info.getFilePath());
        values.put("length", info.getLength());
        values.put("state", info.getState());
        values.put("error", info.getError());
        db.insert(ConfigHelp.DB_TABLE_TASK, null, values);
        db.close();
    }

    @Override
    public void deleteTask(String url) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(ConfigHelp.DB_TABLE_TASK, "url?", new String[]{url});
        db.close();
    }

    @Override
    public void updateTask(String url, FileInfo info) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("filename", info.getFileName());
        values.put("mimetype", info.getMimeType());
        values.put("filepath", info.getFilePath());
        values.put("length", info.getLength());
        values.put("state", info.getState());
        values.put("error", info.getError());
        db.update(ConfigHelp.DB_TABLE_TASK, values, "url=?", new String[]{"url"});
        db.close();
    }

    @Override
    public FileInfo getTaskByUrl(String url) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + ConfigHelp.DB_TABLE_TASK + " where url = ?", new String[]{url});
        FileInfo info = null;
        while (cursor.moveToNext()) {
            info = new FileInfo();
            info.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            info.setFileName(cursor.getString(cursor.getColumnIndex("filename")));
            info.setMimeType(cursor.getString(cursor.getColumnIndex("mimetype")));
            info.setFilePath(cursor.getString(cursor.getColumnIndex("filepath")));
            info.setLength(cursor.getInt(cursor.getColumnIndex("length")));
            info.setState(cursor.getInt(cursor.getColumnIndex("state")));
            info.setError(cursor.getString(cursor.getColumnIndex("error")));
        }
        cursor.close();
        db.close();
        return info;
    }

    @Override
    public List<FileInfo> getAllTasks() {
        List<FileInfo> list = new ArrayList<>();
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + ConfigHelp.DB_TABLE_TASK, new String[]{});
        FileInfo info = null;
        while (cursor.moveToNext()) {
            info = new FileInfo();
            info.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            info.setFileName(cursor.getString(cursor.getColumnIndex("filename")));
            info.setMimeType(cursor.getString(cursor.getColumnIndex("mimetype")));
            info.setFilePath(cursor.getString(cursor.getColumnIndex("filepath")));
            info.setLength(cursor.getInt(cursor.getColumnIndex("length")));
            info.setState(cursor.getInt(cursor.getColumnIndex("state")));
            info.setError(cursor.getString(cursor.getColumnIndex("error")));
            list.add(info);
        }
        cursor.close();
        db.close();
        return list;
    }

    @Override
    public List<FileInfo> getAllTasksBy(int state) {
        List<FileInfo> list = new ArrayList<>();
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + ConfigHelp.DB_TABLE_TASK + " where state = ?", new String[]{String.valueOf(state)});
        FileInfo info = null;
        while (cursor.moveToNext()) {
            info = new FileInfo();
            info.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            info.setFileName(cursor.getString(cursor.getColumnIndex("filename")));
            info.setMimeType(cursor.getString(cursor.getColumnIndex("mimetype")));
            info.setFilePath(cursor.getString(cursor.getColumnIndex("filepath")));
            info.setLength(cursor.getInt(cursor.getColumnIndex("length")));
            info.setState(cursor.getInt(cursor.getColumnIndex("state")));
            info.setError(cursor.getString(cursor.getColumnIndex("error")));
            list.add(info);
        }
        cursor.close();
        db.close();
        return list;
    }


    @Override
    public boolean isExists(String url) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + ConfigHelp.DB_TABLE_TASK + " where url = ?",
                new String[]{url});
        while (cursor.moveToNext()) {
            cursor.close();
            db.close();
            return true;
        }
        return false;
    }
}
