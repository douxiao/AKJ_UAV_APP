package org.douxiao.akj_uav;


import android.content.Context;
import android.os.Handler;

public class Constant {

    public static  final int AT_PORT = 5556;
    public static  final int NAVDATA_PORT = 5554;
    public  static final int VIDEO_PORT = 5555;
//    static final int AT_PORT = 5556;

    public static final String DRONE_IP  = "192.168.1.1";
    public static final String PREF_CONTROLIP_URL = "pref_controlIP_url";
    public static final String PREF_CAMERAIP_URL = "pref_cameraIP_url";
    public static final String PREF_SPEECH_SET = "pref_speech_settings";

    public static String DEFAULT_CONTROLIP_Value = "192.168.1.1:5556";// 默认的控制IP：地址
    public static String DEFAULT_CAMERAIP_Value = "http://192.168.1.1:5555/?action=stream";// 默认的视频流地址
    public static String DEFAULT_SPEECH_Value = "Local";//// 默认的语音引擎


    public static Context context;
    public static Handler handler = null;
    public static String CameraIp_const; //视频流地址
    public static String CtrlIP_const; // 控制地址
    public static String CameraIp;
    public static String CtrlIP;
    public static int CtrlPort = 0;

//    public void init_IP() { // 初始化IP地址和端口号
////        int index = CtrlIP_const.indexOf(":"); // 获取控制IP地址中的":"位置的地址
////        CtrlIP = CtrlIP_const.substring(0, index);
////        String ctrlport = CtrlIP_const.substring(index + 1, CtrlIP_const.length());
////        CtrlPort = Integer.parseInt(ctrlport);// String型转int整形
////        CameraIp = CameraIp_const;
//        MySurfaceView.GetCameraIP(CameraIp);// 把视频流地址传递给SurfaceView
//    }
}


