package com.xcion.player.taskbar.vh;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xcion.player.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * author: Kern Hu
 * email: sky580@126.com
 * data_time: 11/18/20 8:22 PM
 * describe: This is...
 */

public class TaskbarViewHolder extends RecyclerView.ViewHolder {

    public TextView mTitleText;
    public TextView mDescribeText;

    public TaskbarViewHolder(@NonNull View itemView) {
        super(itemView);
        mTitleText = itemView.findViewById(R.id.item_taskbar_title);
        mTitleText.setTextColor(Color.WHITE);
        mDescribeText = itemView.findViewById(R.id.item_taskbar_describe);
        mDescribeText.setTextColor(Color.WHITE);
        mDescribeText.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
    }
}
