package com.pb.criconet;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.danikula.videocache.HttpProxyCacheServer;
import com.pb.criconet.models.AGEventHandler;
import com.pb.criconet.models.CurrentUserSettings;
import com.pb.criconet.models.EngineConfig;
import com.pb.criconet.models.MyEngineEventHandler;

import io.agora.rtc.Constants;
import io.agora.rtc.RtcEngine;
import timber.log.Timber;

public class AGApplication extends Application {
    private CurrentUserSettings mVideoSettings = new CurrentUserSettings();

   // private final Logger log = LoggerFactory.getLogger(this.getClass());
    private RtcEngine mRtcEngine;
    private EngineConfig mConfig;
    private MyEngineEventHandler mEventHandler;

    public RtcEngine rtcEngine() {
        return mRtcEngine;
    }

    public EngineConfig config() {
        return mConfig;
    }

    public CurrentUserSettings userSettings() {
        return mVideoSettings;
    }

    public void addEventHandler(AGEventHandler handler) {
        mEventHandler.addEventHandler(handler);
    }

    public void remoteEventHandler(AGEventHandler handler) {
        mEventHandler.removeEventHandler(handler);
    }

    private static HttpProxyCacheServer proxy;

    public static HttpProxyCacheServer getProxy() {
        return proxy;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createRtcEngine();
        Timber.plant(new Timber.DebugTree());
        proxy = new HttpProxyCacheServer(this);

    }

    private void createRtcEngine() {
        Context context = getApplicationContext();
        String appId = context.getString(R.string.agora_app_id);
        if (TextUtils.isEmpty(appId)) {
            throw new RuntimeException("NEED TO use your App ID, get your own ID at https://dashboard.agora.io/");
        }

        mEventHandler = new MyEngineEventHandler();
        try {
            // Creates an RtcEngine instance
            mRtcEngine = RtcEngine.create(context, appId, mEventHandler);
        } catch (Exception e) {
            //log.error(Log.getStackTraceString(e));
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }

        /*
          Sets the channel profile of the Agora RtcEngine.
          The Agora RtcEngine differentiates channel profiles and applies different optimization
          algorithms accordingly. For example, it prioritizes smoothness and low latency for a
          video call, and prioritizes video quality for a video broadcast.
         */
        mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION);
        // Enables the video module.
        mRtcEngine.enableVideo();
        /*
          Enables the onAudioVolumeIndication callback at a set time interval to report on which
          users are speaking and the speakers' volume.
          Once this method is enabled, the SDK returns the volume indication in the
          onAudioVolumeIndication callback at the set time interval, regardless of whether any user
          is speaking in the channel.
         */
        mRtcEngine.enableAudioVolumeIndication(200, 3, false);

        mConfig = new EngineConfig();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}