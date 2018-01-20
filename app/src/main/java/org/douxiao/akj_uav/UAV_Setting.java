package org.douxiao.akj_uav;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.widget.Toast;

public class UAV_Setting extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private EditTextPreference mEditTextPreConIpUrl;
    private EditTextPreference mEditTextPreCamIPUrl;
    private EditTextPreference mEditTextPreCamPort;
    private ListPreference mListPreference;
    String TAG = null;

    // String Cloud = "引擎选择Cloud";
    // String Local = "引擎选择Local";
    // String Mix = "引擎选择Mix";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        // 所有的值将会自动保存到到SharePreferences
        addPreferencesFromResource(R.xml.preference);
        initPreference();
    }

    private void initPreference() {
        mEditTextPreConIpUrl = (EditTextPreference) findPreference(Constant.PREF_CONTROLIP_URL);
        // Constant.PREF_CONTROLIP_URL对应的是该编辑框的键值 ，在Xml文件中可以找到
        mEditTextPreCamIPUrl = (EditTextPreference) findPreference(Constant.PREF_CAMERAIP_URL);
        mListPreference = (ListPreference) findPreference(Constant.PREF_SPEECH_SET);
    }

    @Override
    // 在onResume中初始化控件的值
    protected void onResume() {
        super.onResume();
        SharedPreferences mSharedPreferences = getPreferenceScreen().getSharedPreferences();
        mEditTextPreConIpUrl
                .setSummary(mSharedPreferences.
                        getString(Constant.PREF_CONTROLIP_URL, Constant.DEFAULT_CONTROLIP_Value));
        mEditTextPreCamIPUrl
                .setSummary(mSharedPreferences.
                        getString(Constant.PREF_CAMERAIP_URL, Constant.DEFAULT_CAMERAIP_Value));
        mListPreference.setSummary(mSharedPreferences.
                getString(Constant.PREF_SPEECH_SET, Constant.DEFAULT_SPEECH_Value));


        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);// 注册

    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);// 注销事件
    }

    @Override
    // 事件处理器. 根据数据的变化,对显示和行为作改变
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);
        if (pref instanceof EditTextPreference) {
            EditTextPreference etp = (EditTextPreference) pref;
            pref.setSummary(etp.getText());
            String prefsValue = etp.toString();
            showToast(prefsValue);
        } else if (pref instanceof ListPreference) {
            ListPreference etp = (ListPreference) pref;
            pref.setSummary(etp.getEntry());
            String prefsValue = etp.getValue();
            showToast(prefsValue);// 这个用来显示改变的值
        }

    }

    private void showToast(String arg) {

        Toast.makeText(this, arg, Toast.LENGTH_SHORT).show();

    }
}