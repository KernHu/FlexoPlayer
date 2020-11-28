package com.xcion.player;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.xcion.player.controller.ControllerFactory;
import com.xcion.player.enum1.DisplayMode;
import com.xcion.player.enum1.Template;
import com.xcion.player.media.audio.AudioPlayerFactory;
import com.xcion.player.media.stream.StreamFactory;
import com.xcion.player.media.video.VideoPlayerFactory;
import com.xcion.player.pojo.MediaTask;
import com.xcion.player.taskbar.TaskBarFactory;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * author: Kern Hu
 * email: sky580@126.com
 * data_time: 11/16/20 8:03 PM
 * describe: This is...
 */
public class FlexoPlayerView extends AbstractFlexoPlayerView {

    private static final String TAG = "FlexoPlayerView";

    private ArrayList<MediaTask> mMediaTasks = new ArrayList<>();
    private ControllerFactory mControllerFactory;
    private TaskBarFactory mTaskBarFactory;
    private StreamFactory mStreamFactory;
    private AudioPlayerFactory mAudioPlayerFactory;
    private VideoPlayerFactory mVideoPlayerFactory;
    private View mControllerView, mTaskBarView;

    private int index = 0;

    public FlexoPlayerView(@NonNull Context context) {
        this(context, null);
    }

    public FlexoPlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlexoPlayerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /***********************************************************************************************/
    /***********************************************************************************************/
    /***********************************************************************************************/
    @Override
    protected void onCreateController() {
        mControllerFactory = new ControllerFactory(getContext(), getControllerViewRes());
        mControllerView = mControllerFactory.getView();
        this.addView(mControllerView);
    }

    @Override
    protected void onCreateTaskBar() {
        mTaskBarFactory = new TaskBarFactory(getContext(), getTaskBarViewRes());
        mTaskBarView = mTaskBarFactory.getView();
        this.addView(mTaskBarView);
    }


    @Override
    protected void onCreateCache() {

    }

    @Override
    protected void onCreateStream() {
        mStreamFactory = new StreamFactory(getContext());
        this.addView(mStreamFactory.getView());
    }

    @Override
    protected void onCreateAudio() {
        mAudioPlayerFactory = new AudioPlayerFactory(getContext());
        this.addView(mAudioPlayerFactory.getView());
    }

    @Override
    protected void onCreateVideo() {
        mVideoPlayerFactory = new VideoPlayerFactory(getContext(), getRenderMode());
        mVideoPlayerFactory.getMediaPlayer();
        this.addView(mVideoPlayerFactory.getView());
        this.setLayerType(LAYER_TYPE_HARDWARE, null);
    }

    @Override
    protected void onCreateLive() {

    }

    @Override
    protected void onTaskBarInAnim() {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_taskbar_in);
        mTaskBarView.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mTaskBarView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onTaskBarOutAnim() {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_taskbar_out);
        mTaskBarView.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mTaskBarView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onControllerInAnim() {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_controller_in);
        mControllerView.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mControllerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onControllerOutAnim() {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_controller_out);
        mControllerView.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mControllerView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onShowUi(boolean isShowUI) {
        if (isShowUI) {
            onTaskBarInAnim();
            onControllerInAnim();
        } else {
            onTaskBarOutAnim();
            onControllerOutAnim();
        }
    }
    /***********************************************************************************************/
    /***********************************************************************************************/
    /***********************************************************************************************/

    @Override
    protected void initView() {

        if (Template.JUST_STREAM.ordinal() == getTemplate()) {
            onCreateStream();
        } else if (Template.JUST_VIDEO.ordinal() == getTemplate()) {
            onCreateVideo();
        } else if (Template.JUST_LIVE.ordinal() == getTemplate()) {


        } else if (Template.BOTH_AUDIO_STREAM.ordinal() == getTemplate()) {
            onCreateStream();
            onCreateAudio();
        } else if (Template.BOTH_AUDIO_VIDEO.ordinal() == getTemplate()) {
            onCreateAudio();
            onCreateVideo();
        } else {
            onCreateStream();
            onCreateAudio();
            onCreateVideo();
        }
        onCreateTaskBar();
        onCreateController();
        onBindData();
    }

    @Override
    public void onBindData() {
        onShowUi(false);
    }

    @Override
    public void lowMemory() {
        if (mStreamFactory != null) {
            mStreamFactory.lowMemory();
        }
        if (mAudioPlayerFactory != null) {
            mAudioPlayerFactory.lowMemory();
        }
        if (mVideoPlayerFactory != null) {
            mVideoPlayerFactory.lowMemory();
        }
    }

    @Override
    public void trimMemory(int level) {
        if (mStreamFactory != null) {
            mStreamFactory.trimMemory(level);
        }
        if (mAudioPlayerFactory != null) {
            mAudioPlayerFactory.trimMemory(level);
        }
        if (mVideoPlayerFactory != null) {
            mVideoPlayerFactory.trimMemory(level);
        }
    }

    @Override
    public void recycle() {
        if (mStreamFactory != null) {
            mStreamFactory.recycle();
        }
        if (mAudioPlayerFactory != null) {
            mAudioPlayerFactory.recycle();
        }
        if (mVideoPlayerFactory != null) {
            mVideoPlayerFactory.recycle();
        }
    }

    @Override
    public void setAudioVolume(int level) {

    }

    @Override
    public void setVideoVolume(int level) {

    }

    @Override
    public void setVideoArea(int topLeftX, int topLeftY, int bottomRightX, int bottomRightY) {

    }

    @Override
    public void setDisplayAspectRatio(DisplayMode mode) {

    }

    @Override
    public void setDisplayOrientation(int value) {

    }

    /***********************************************************************************************/
    /***********************************************************************************************/
    /***********************************************************************************************/

    @Override
    public void setMediaTask(ArrayList<MediaTask> tasks) {
        setMediaTask(tasks, false);
    }

    @Override
    public void setMediaTask(ArrayList<MediaTask> tasks, boolean isAppend) {
        if (tasks != null && !tasks.isEmpty()) {
            if (!isAppend) {
                mMediaTasks.clear();
            }
            mMediaTasks.addAll(tasks);
        }
    }

    @Override
    public void startPlay() {
        prepare(index);
    }

    @Override
    public void stopPlay() {
        if (mStreamFactory != null)
            mStreamFactory.stopPlay();
        if (mAudioPlayerFactory != null)
            mAudioPlayerFactory.stopPlay();
        if (mVideoPlayerFactory != null)
            mVideoPlayerFactory.stopPlay();

    }

    /***********************************************************************************************/
    /***********************************************************************************************/
    /***********************************************************************************************/

    private void prepare(int index) {
        //任务列表
        if (mTaskBarFactory != null) {
            mTaskBarFactory.setUpdate(mMediaTasks);
        }

        //信息流播放
        if (mStreamFactory != null) {
            mStreamFactory.setMediaTask(mMediaTasks.get(index).getStreamTasks(), false);
        }

        //音频播放
        if (mAudioPlayerFactory != null) {
            mAudioPlayerFactory.setMediaTask(mMediaTasks.get(index).getAudioUrls(), false);
        }
        //
        if (mVideoPlayerFactory != null) {
            mVideoPlayerFactory.setMediaTask(mMediaTasks.get(index).getVideoUri());
        }
    }


}
