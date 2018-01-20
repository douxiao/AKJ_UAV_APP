package org.douxiao.akj_uav;


import android.content.Context;
import android.os.Handler;

public class Constant{

    public static int AT_PORT;
    public static int NAVDATA_PORT = 5554;
    public static String DRONE_IP;
    public static String CameraIp;
    public static int Video_PORT;


    public static final String PREF_CONTROLIP_URL = "pref_controlIP_url";
    public static final String PREF_CAMERAIP_URL = "pref_cameraIP_url";
    public static final String PREF_CAMERA_PORT   = "pref_camera_port";
    public static final String PREF_SPEECH_SET = "pref_speech_settings";

    public static String DEFAULT_CONTROLIP_Value = "192.168.1.1:5556";// 默认的控制IP：地址
    public static String DEFAULT_CAMERAIP_Value = "tcp://192.168.1.1:5555";// 默认的视频流地址
    public static String DEFAULT_CAMERAPort_Value = "5555";// 默认的视频流端口
    public static String DEFAULT_SPEECH_Value = "Local";//// 默认的语音引擎

    public static String url = "tcp://192.168.1.1:5555";// 默认的视频流地址

    public static Context context;
    public static Handler handler = null;
    public static String CameraPort_const; //视频流端口
    public static String CameraIp_const; //视频流地址
    public static String CtrlIP_const;  //控制IP地址,即为飞机的IP地址

}



