package bk.it.com.demo.Utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.input.InputManager;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/5/3.
 * 一些获取系统属性的工具类
 */

public class BaseUtil {
    private Context context;

    protected InputMethodManager imm;

    private TelephonyManager tManager;

    public BaseUtil(Context context) {
        this.context = context;
        tManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);

    }

    //吐司时间长
    public void DisPlay(String content) {
        Toast.makeText(context, content, Toast.LENGTH_LONG).show();
    }

    //吐司时间短
    public void DisplayToast(String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }


    public void hideOrShowSoftInput(boolean isShowSoft, EditText editText) {
        if (isShowSoft) {
            imm.showSoftInput(editText, 0);
        } else {
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }


    // 获得当前程序版本信息
    public String getVersionName() throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        return packInfo.versionName;
    }


    /* 获得系统版本号 */
    public String getClientOsVer() {
        return android.os.Build.VERSION.RELEASE;
    }


    /* 获得系统版本 */

    public String getClientOs() {
        return android.os.Build.ID;
    }

}
