package com.xcion.player;

import com.xcion.player.pojo.MediaTask;

import java.util.List;

/**
 * Author: Kern
 * E-mail: sky580@126.com
 * DateTime: 2020/11/17  01:12
 * Intro:
 */
public interface FlexoPlayerControl {

    public interface View {

        Presenter setPresenter();

        List<MediaTask> getDataSource();

    }

    public interface Presenter {

        void startPlay();

        void stopPlay();

        void recycle();

    }
}
