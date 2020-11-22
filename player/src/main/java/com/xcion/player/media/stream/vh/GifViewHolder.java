package com.xcion.player.media.stream.vh;

import android.view.View;
import android.widget.ImageView;

import com.xcion.player.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * author: Kern Hu
 * email: sky580@126.com
 * data_time: 11/18/20 8:22 PM
 * describe: This is...
 */

public class GifViewHolder extends RecyclerView.ViewHolder {

    public ImageView mGifLargeIV;

    public GifViewHolder(@NonNull View itemView) {
        super(itemView);
        mGifLargeIV = itemView.findViewById(R.id.item_style_image_large_iv);
    }
}
