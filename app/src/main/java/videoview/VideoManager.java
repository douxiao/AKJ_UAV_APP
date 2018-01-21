package videoview;


import android.util.Log;

import org.douxiao.akj_uav.Constant;
import org.douxiao.akj_uav.UAVControl;

import java.io.IOException;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.MediaPlayer.OnVideoSizeChangedListener;

/**
 * Class managing the video stream coming from the Drone.
 */
public class VideoManager implements OnBufferingUpdateListener,
        OnCompletionListener, OnPreparedListener, OnVideoSizeChangedListener {

    public MediaPlayer mMediaPlayer;
    UAVControl joyActivity;
    public String TAG = "Fcking NOTIF";

    public VideoManager(UAVControl joy) {
        joyActivity = joy;
    }
    /**
     * Initializes the MediaPlayer and SurfaceHolder.
     */
    public void playVideo() {

        mMediaPlayer = new MediaPlayer(joyActivity);
        try {
            mMediaPlayer.setDataSource(Constant.CameraIp_const);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        mMediaPlayer.setDisplay(joyActivity.holder);
        mMediaPlayer.prepareAsync();
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnVideoSizeChangedListener(this);

    }

    /**
     * Destroys the MediaPlayer and release the ressources.
     */
    public void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    /**
     * Set up the size of the video to the size of the screen and starts the playback.
     */
    void startVideoPlayback() {
        joyActivity.holder.setFixedSize(joyActivity.metrics.widthPixels, joyActivity.metrics.heightPixels);
        mMediaPlayer.start();
    }

    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        // TODO Auto-generated method stub
        startVideoPlayback();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        // TODO Auto-generated method stub
        if (mp.getCurrentFrame() != null) {

            Log.v(TAG, "Frame Received");

        } else {
            Log.v(TAG, "Frame Not Received");
        }

    }


}







