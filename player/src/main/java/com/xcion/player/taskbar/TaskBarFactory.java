package com.xcion.player.taskbar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.xcion.player.media.AbstractFactory;
import com.xcion.player.R;
import com.xcion.player.pojo.MediaTask;

import java.util.ArrayList;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * author: Kern Hu
 * email: sky580@126.com
 * data_time: 11/22/20 4:59 PM
 * describe: This is...
 */

public class TaskBarFactory extends AbstractFactory {

    private Context context;
    private int taskBarViewRes;

    private RecyclerView mRecyclerView;
    private TaskBarAdapter mTaskBarAdapter;

    public TaskBarFactory(Context context, int taskBarViewRes) {
        this.context = context;
        this.taskBarViewRes = taskBarViewRes;
    }

    @Override
    public View getView() {

        View view = ViewGroup.inflate(context, taskBarViewRes, null);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                context.getResources().getDimensionPixelSize(R.dimen.task_bar_width),
                FrameLayout.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.RIGHT;
        view.setLayoutParams(lp);
        view.setVisibility(View.VISIBLE);
        view.bringToFront();
        initView(view);

        return view;
    }

    @Override
    public void initView(View view) {

        mRecyclerView = view.findViewById(R.id.flexo_player_taskbar);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        DividerItemDecoration divider = new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(new ColorDrawable(Color.parseColor("#FFC5C5C5")));
        mRecyclerView.addItemDecoration(divider);

        mTaskBarAdapter = new TaskBarAdapter(view.getContext(), null);
        mRecyclerView.setAdapter(mTaskBarAdapter);

    }

    public void setUpdate(ArrayList<MediaTask> mediaTask) {
        if (mTaskBarAdapter != null) {
            mTaskBarAdapter.setUpdate(mediaTask);
        }
    }
}
