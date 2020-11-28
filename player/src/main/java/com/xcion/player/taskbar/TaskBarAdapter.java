package com.xcion.player.taskbar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xcion.player.R;
import com.xcion.player.pojo.MediaTask;
import com.xcion.player.taskbar.vh.TaskbarViewHolder;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * author: Kern Hu
 * email: sky580@126.com
 * data_time: 11/18/20 8:20 PM
 * describe: This is...
 */

public class TaskBarAdapter extends RecyclerView.Adapter<TaskbarViewHolder> {

    private Context mContext;
    private ArrayList<MediaTask> mMediaTask = new ArrayList<>();

    public TaskBarAdapter(Context context, ArrayList<MediaTask> mediaTasks) {
        mContext = context;
        if (mediaTasks != null && !mediaTasks.isEmpty())
            mMediaTask.addAll(mediaTasks);
    }

    @NonNull
    @Override
    public TaskbarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fpv_item_taskbar, parent, false);
        return new TaskbarViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return mMediaTask.size();
    }

    @Override
    public void onBindViewHolder(@NonNull TaskbarViewHolder holder, int position) {

        MediaTask task = mMediaTask.get(position);
        holder.itemView.setBackgroundColor(task.isSelected() ? Color.parseColor("#F6EA7B") : Color.parseColor("#FFFFFFFF"));

        holder.mTitleText.setText(task.title);
        String videoCount = String.valueOf(1);
        String audioCount = String.valueOf(task.audioUrls == null ? 0 : task.audioUrls.size());
        String streamCount = String.valueOf(task.streamTasks == null ? 0 : task.streamTasks.size());
        holder.mDescribeText.setText(String.format(
                mContext.getResources().getString(R.string.taskbar_describe),
                videoCount, audioCount, streamCount));

    }


    public void setUpdate(ArrayList<MediaTask> mediaTasks) {
        mMediaTask.clear();
        mMediaTask.addAll(mediaTasks);
        notifyDataSetChanged();
    }
}
