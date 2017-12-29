package org.douxiao.akj_uav;


import android.content.Context;
import android.os.Handler;

public class Constant {

    public static final String PREF_CONTROLIP_URL = "pref_controlIP_url";
    public static final String PREF_CAMERAIP_URL = "pref_cameraIP_url";
    public static final String PREF_SPEECH_SET = "pref_speech_settings";

    public static  String DEFAULT_CONTROLIP_Value = "192.168.1.1:5556";// Ĭ�ϵĿ���IP����ַ
    public static  String DEFAULT_CAMERAIP_Value  = "http://192.168.1.1:5555/?action=stream";// Ĭ�ϵ���Ƶ����ַ
    public static  String DEFAULT_SPEECH_Value    = "Local";// Ĭ�ϵ���������

    public static Context context;
    public static Handler handler=null;
    public static String CameraIp_const; // ��Ƶ����ַ
    public static String CtrlIP_const; // ���Ƶ�ַ
    public static String CameraIp;
    public static String CtrlIP;
    public static int CtrlPort = 0;

    public void init_IP() { // ��ʼ��IP��ַ�Ͷ˿ں�
        int index = CtrlIP_const.indexOf(":"); // ��ȡ����IP��ַ�е�":"λ�õĵ�ַ
        CtrlIP = CtrlIP_const.substring(0, index);
        String ctrlport = CtrlIP_const.substring(index + 1, CtrlIP_const.length());
        CtrlPort = Integer.parseInt(ctrlport);// String��תint����
        CameraIp = CameraIp_const;
        MySurfaceView.GetCameraIP(CameraIp);// ����Ƶ����ַ���ݸ�SurfaceView
    }


}