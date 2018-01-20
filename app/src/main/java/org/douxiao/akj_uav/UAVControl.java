package org.douxiao.akj_uav;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.sunflower.FlowerCollector;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

import ARDrone.ARDrone;
import ARDrone.ARDroneAPI;
import speech.ApkInstaller;
import speech.JsonParser;
import speech.Speech_Init;
import videoview.VideoManager;

public class UAVControl extends AppCompatActivity implements LocationListener, SensorEventListener, View.OnClickListener,SurfaceHolder.Callback {

    public SurfaceHolder holder;
    VideoManager mVideo;
    public DisplayMetrics metrics;

    public static String EngineType_const;// 语音引擎值
    public String mEngineType;// 语音引擎
    private ARDroneAPI drone;
    private ARDrone ardrone;
    private LocationManager mLocationManager;
    private SensorManager mSensorManager;
    private Location mCurrentLocation;
    private Location mTargetLocation;
    private boolean mStarted = false;
    private double mOrientation;

    private Drawable ForWardon;
    private Drawable ForWardoff;
    private Drawable BackWardon;
    private Drawable BackWardoff;
    private Drawable TurnLefton;
    private Drawable TurnLeftoff;
    private Drawable TurnRighton;
    private Drawable TurnRightoff;
    private Drawable buttonLenon;
    private Drawable buttonLenoff;
    private EditText chat_info; // 文本编辑框
    private static final String TAG = UAVControl.class.getSimpleName();
    // 语音听写对象
    private SpeechRecognizer mIat;
    // 语音听写UI
    private RecognizerDialog mIatDialog;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    // 引擎类型
    // public String mEngineType = SpeechConstant.TYPE_CLOUD;
    // // 语记安装助手类
    ApkInstaller mInstaller;
    VideoView videoView;
    boolean isRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) { //隐去标题栏
            getSupportActionBar().hide();
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置窗体全屏
        getSettingValue();// 初始化设置参数
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new // 严苛模式
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        setContentView(R.layout.speechcontrol);
        InitLayout(); // 按键的初始化
        init_IP(); //IP地址初始化
        videoinit();
        Speech_Init speech = new Speech_Init(); //实例化
        speech.init_speech(UAVControl.this);// 初始化语音，加载AppId
        mIat = SpeechRecognizer.createRecognizer(UAVControl.this, mInitListener);
        mIatDialog = new RecognizerDialog(UAVControl.this, mInitListener);
        chat_info = (EditText) findViewById(R.id.chat_info_1);// 用来显示识别的语音文本框
        mInstaller = new ApkInstaller(UAVControl.this); // 用来安装讯飞语记APK

        //dummy date, Auto Pilot is not completed.
        try {
            drone = new ARDroneAPI();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mTargetLocation = new Location("target");

        mTargetLocation.setLatitude(1.0);
        mTargetLocation.setLongitude(1.0);

        mCurrentLocation = new Location("cr");

        mCurrentLocation.setLatitude(1.0);
        mCurrentLocation.setLongitude(1.0);
    }


    private void getSettingValue() {// 初始化设置参数
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        Constant.CtrlIP_const = settings.getString(Constant.PREF_CONTROLIP_URL, Constant.DEFAULT_CONTROLIP_Value);// 加载控制地址
        Constant.CameraIp_const = settings.getString(Constant.PREF_CAMERAIP_URL, Constant.DEFAULT_CONTROLIP_Value);// 加载视频流地址
        Constant.CameraPort_const = settings.getString(Constant.PREF_CAMERA_PORT, Constant.DEFAULT_CAMERAPort_Value);// 加载视频流地址
        EngineType_const = settings.getString(Constant.PREF_SPEECH_SET, Constant.DEFAULT_SPEECH_Value);// 加载语音引擎
        switch (EngineType_const) {
            case "Cloud":
                mEngineType = SpeechConstant.TYPE_CLOUD;
                break;
            case "Local":
                mEngineType = SpeechConstant.TYPE_LOCAL;
            case "Mix":
                mEngineType = SpeechConstant.TYPE_MIX;
            default:
                break;
        }
    }

    public void init_IP() {
        int index = Constant.CtrlIP_const.indexOf(":"); // 获取控制IP地址中的":"位置的地址
        Constant.DRONE_IP = Constant.CtrlIP_const.substring(0, index);
        String ctrlport = Constant.CtrlIP_const.substring(index + 1, Constant.CtrlIP_const.length());
        Constant.AT_PORT = Integer.parseInt(ctrlport);// String型转int整形
        Constant.Video_PORT = Integer.parseInt(Constant.CameraPort_const);
        Constant.CameraIp = Constant.CameraIp_const;
        MySurfaceView.GetCameraIP(Constant.CameraIp);// 把视频流地址传递给SurfaceView

    }

    private void videoinit(){

        mVideo = new VideoManager(this);
        SurfaceView mPreview = (SurfaceView) findViewById(R.id.surface);
        holder = mPreview.getHolder();
        holder.addCallback(this);
        holder.setFormat(PixelFormat.RGBA_8888);//设置播放的清晰度（像素格式）
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

    }


    private void InitLayout() {
//        findViewById(R.id.videoView).setOnClickListener(this);  //用来显示视频流
//        findViewById(R.id.mySurfaceViewVideo).setOnClickListener(this);
        findViewById(R.id.picture).setOnClickListener(this);// 拍照监听器
        findViewById(R.id.ButtonTakePic).setOnClickListener(this);// 设置Sys_setting的监听器
        findViewById(R.id.speech_recognize).setOnClickListener(this);// 设置语言识别的按钮
        findViewById(R.id.btnForward).setOnClickListener(this);
        findViewById(R.id.btnLeft).setOnClickListener(this);
        findViewById(R.id.btnRight).setOnClickListener(this);
        findViewById(R.id.btnBack).setOnClickListener(this);
        findViewById(R.id.btnStop).setOnClickListener(this);
        findViewById(R.id.land).setOnClickListener(this);
        findViewById(R.id.launch).setOnClickListener(this);
        ForWardon = getResources().getDrawable(R.drawable.sym_forward_1);
        ForWardoff = getResources().getDrawable(R.drawable.sym_forward);

        TurnLefton = getResources().getDrawable(R.drawable.sym_left_1);
        TurnLeftoff = getResources().getDrawable(R.drawable.sym_left);

        TurnRighton = getResources().getDrawable(R.drawable.sym_right_1);
        TurnRightoff = getResources().getDrawable(R.drawable.sym_right);

        BackWardon = getResources().getDrawable(R.drawable.sym_backward_1);
        BackWardoff = getResources().getDrawable(R.drawable.sym_backward);

        buttonLenon = getResources().getDrawable(R.drawable.sym_light);
        buttonLenoff = getResources().getDrawable(R.drawable.sym_light_off);
        // 下面的代码是默认混和式听写即有网络或者没有网络都可以的
        // mEngineType = SpeechConstant.TYPE_MIX;

    }

    @Override
    public void onClick(View v) {// 用来监听此活动下的按键
        switch (v.getId()) {
            case R.id.ButtonTakePic:
                new Thread(new Runnable() {// 这里拍照开启一个新的线程,这里要学会这种开启新的线程的方法
                    @Override
                    public void run() {
                        try {
                            drone = new ARDroneAPI();
                            //   setPilotState(drone.getStatus());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

//                        if (null != Constant.handler) {
//                            Message message = new Message();
//                            message.what = 1;
//                            Constant.handler.sendMessage(message);
//                        }
                    }
                }).start();
                break;
            case R.id.speech_recognize:
                mEngineType_Choice();
                startSpeech();
                break;
            case R.id.picture:
                showTip("查看已拍摄的照片");
                Intent intent = new Intent(UAVControl.this, photograph.class);
                startActivity(intent);
                break;
            case R.id.btnForward:
                showTip("向前飞");
                drone.goForward();
//                setPilotState(drone.getStatus());
//			SendCmd(Cmd_ForWard);
                break;
            case R.id.btnBack:
                showTip("向后飞");
                drone.goBackward();
                // setPilotState(drone.getStatus());
//			SendCmd(Cmd_BackWard);
                break;
            case R.id.btnLeft:
                showTip("向左飞");
                drone.rotatel();
//			SendCmd(Cmd_TurnLeft);
                break;
            case R.id.btnRight:
                showTip("向右飞");
                drone.rotater();
//			SendCmd(Cmd_TurnRight);
                break;
            case R.id.btnStop:
                showTip("悬停");
                drone.hovering();
                // setPilotState(drone.getStatus());
                //   SendCmd(DR_Cmd_Emergency);
                break;
            case R.id.launch:
                showTip("起飞");
//                mStarted = true;
                drone.takeoff();
                //  setPilotState(drone.getStatus());
                //  SendCmd(DR_Cmd_Launch);
                break;
            case R.id.land:
                showTip("着陆");
                drone.landing();
                //  setPilotState(drone.getStatus());
                //  SendCmd(DR_Cmd_Land);
            default:
                break;
        }
    }

    private void mEngineType_Choice() {

        if ((mEngineType.equals(SpeechConstant.TYPE_MIX)) || (mEngineType.equals(SpeechConstant.TYPE_LOCAL))) {

            // 选择本地听写 判断是否安装语记,未安装则跳转到提示安装页面

            if (!SpeechUtility.getUtility().checkServiceInstalled()) {
                mInstaller.install();
            }
//            else {
//                String result = FucUtil.checkLocalResource();
//                if (!TextUtils.isEmpty(result)) {
//                    showTip(result);
//                }
//            }

        }

    }


    private void startSpeech() {
        // 移动数据分析，收集开始听写事件
        FlowerCollector.onEvent(UAVControl.this, "iat_recognize");
        chat_info.setText(null);// 清空显示内容
        // 设置参数
        setParam();
        mIatDialog.setListener(mRecognizerDialogListener);
        // mIatDialog.setListener(new MyRecognizerDialogListener());
        // 6.显示Dialog,接收语音输入
        mIatDialog.show();
    }

    /**
     * 参数设置
     */
    private void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);
        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
        // 2.设置accent \ language 等参数
        mIatDialog.setParameter(SpeechConstant.LANGUAGE, "zn_ch");
        mIatDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
    }

    /**
     * 用来显示可以消失的对话框
     */
    public void showTip(String data) {

        Toast.makeText(UAVControl.this, data, Toast.LENGTH_SHORT).show();
    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败，错误码：" + code);
            }
        }
    };

    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuilder resultBuffer = new StringBuilder();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        chat_info.setText(resultBuffer.toString());
        // Toast.makeText(Video.this, chat_info.getText(),
        // Toast.LENGTH_SHORT).show();
        String message_voice = chat_info.getText().toString();
        switch (message_voice) {
            case "向前。":
//			SendCmd(Cmd_ForWard);
                showTip("向前飞");
                drone.goForward();
                break;
            case "向后。":
//			SendCmd(Cmd_BackWard);
                showTip("向后飞");
                drone.goBackward();
                break;
            case "向左。":
//			SendCmd(Cmd_TurnLeft);
                showTip("向左飞");
                drone.rotatel();
                break;
            case "向右。":
//			SendCmd(Cmd_TurnRight);
                showTip("向右飞");
                drone.rotater();
                break;
            case "紧急着陆。":
                showTip("紧急着陆");
                drone.disbleEmergency();
                break;
            case "着陆。":
                //  SendCmd(DR_Cmd_Land);
                showTip("着陆");
                drone.landing();
                break;
            case "降落。":
                //  SendCmd(DR_Cmd_Land);
                showTip("降落");
                drone.landing();
                break;
            case "起飞。":
                //    SendCmd(DR_Cmd_Launch);
                showTip("起飞");
                drone.takeoff();
                break;
            case "漆黑。":
                //    SendCmd(DR_Cmd_Launch);
                showTip("起飞");
                drone.takeoff();
                break;
            case "悬停。":
                //    SendCmd(DR_Cmd_Launch);
                showTip("悬停");
                drone.hovering();
                break;
            default:
                showTip("指令错误：请您重新输入！");
                break;
        }

        // if(message_voice.equals("向前飞。")){
        // SendCmd(Cmd_ForWard);
        // }
        chat_info.setSelection(chat_info.length());
    }


    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            printResult(results);
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            showTip(error.getPlainDescription(true));
        }

    };
    // 以下代码是提示再按一次退出程序
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - exitTime) > 2500) // System.currentTimeMillis()无论何时调用，肯定大于2500
            {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0); // 这个会释放内存
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        mVideo.playVideo();
        super.onResume();
        startSensors();
    }

    @Override
    protected void onPause() {
        mVideo.releaseMediaPlayer();
        stopSensors();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mVideo.releaseMediaPlayer();
        this.finish();
        super.onDestroy();
    }
    public void stopSensors() {
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(this);
            mLocationManager = null;
        }

        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
            mSensorManager = null;
        }
    }


    public void startSensors() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (mLocationManager != null) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        1000 /* minTime ms */,
                        1 /* minDistance in meters */,
                        this);
            }
        }


        if (mSensorManager == null) {
            mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            if (mSensorManager != null) {
                mSensorManager.registerListener(this,
                        mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)
                        , SensorManager.SENSOR_DELAY_NORMAL);

            }
        }

    }

    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        mCurrentLocation = location;

        Log.d("Drone", "Height=" + mCurrentLocation.getAltitude());
    }

    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }

    public void onSensorChanged(SensorEvent event) {
        mOrientation = event.values[0];
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
