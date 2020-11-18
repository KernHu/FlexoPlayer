package com.xcion.player.stream.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.xcion.player.R;
import com.xcion.player.pojo.StreamTask;
import com.xcion.player.stream.holder.GifViewHolder;
import com.xcion.player.stream.holder.ImageViewHolder;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * author: Kern Hu
 * email: sky580@126.com
 * data_time: 11/18/20 8:20 PM
 * describe: This is...
 */

public class StreamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context mContext;
    private ArrayList<StreamTask> mStreamTask = new ArrayList<>();

    public StreamAdapter(Context context, ArrayList<StreamTask> streamTask) {
        mContext = context;
        if (streamTask != null && !streamTask.isEmpty())
            mStreamTask.addAll(streamTask);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == StreamTask.TYPE_IMAGE) {
            view = ViewGroup.inflate(mContext, R.layout.item_style_image_large, null);
            return new ImageViewHolder(view);
        } else if (viewType == StreamTask.TYPE_GIF) {
            view = ViewGroup.inflate(mContext, R.layout.item_style_image_large, null);
            return new ImageViewHolder(view);
        }
        return null;

    }


    @Override
    public int getItemViewType(int position) {
        StreamTask task = mStreamTask.get(position);
        return task.type;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return mStreamTask.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        StreamTask task = mStreamTask.get(position);

        if (holder instanceof ImageViewHolder) {
            ImageViewHolder imageVH = (ImageViewHolder) holder;
            Glide.with(mContext)
                    .asBitmap()
                    .load(task.detail)
                    .centerCrop()
                    .placeholder(R.drawable.default_image_cover)
                    .into(imageVH.mImageLargeIV);
        } else if (holder instanceof GifViewHolder) {
            GifViewHolder gifVH = (GifViewHolder) holder;
            Glide.with(mContext)
                    .asBitmap()
                    .load(task.detail)
                    .centerCrop()
                    .placeholder(R.drawable.default_image_cover)
                    .into(gifVH.mGifLargeIV);
        }

    }

}