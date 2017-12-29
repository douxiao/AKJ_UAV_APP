package speech;

import com.iflytek.cloud.SpeechUtility;

import android.content.Context;

//语音识别
public class Speech_Init {

    //初始化语音识别
    public void init_speech(Context context) {

        // 将“12345678”替换成您申请的 APPID，申请地址： http://www.xfyun.cn
        // 请勿在 “ =”与 appid 之间添加任务空字符或者转义符
        SpeechUtility.createUtility(context, "appid=57f70bd1");

    }







}