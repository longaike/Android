package bk.it.com.demo.Utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/5/4.
 */

public class ToastUtils {
    private static Toast toast = null; //Toast的对象！

    public static void showToast(Context mContext, String id) {
        if (toast == null) {
            toast = Toast.makeText(mContext, id, Toast.LENGTH_SHORT);
        } else {
            toast.setText(id);
        }
        toast.show();
    }
}
