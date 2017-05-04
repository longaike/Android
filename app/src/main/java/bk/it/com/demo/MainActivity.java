package bk.it.com.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import bk.it.com.demo.Utils.BaseActivity;
import bk.it.com.demo.Utils.BaseUtil;
import bk.it.com.demo.Utils.HttpUtil;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class MainActivity extends BaseActivity {

    private String TAG = "Mactivity";


    private Button btn_text;
    public static final String URL = "http://www.baidu.com/";
    private TextView tv;
    private Map<String, String> map;
    private HashMap<String, String> maps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_text = (Button) findViewById(R.id.btn_text);
        tv = (TextView) findViewById(R.id.tv_text);
        map = new HashMap<>();

    }

    public void click(View view) {
        try {
            HttpUtil.requestNet(URL, map, new HttpUtil.CallBacks() {
                @Override
                public void onSuccess(String message) {
                    Log.i(TAG, "onSuccess: "+message);
                }

                @Override
                public void onError(Request request, Exception e) {
                    Log.i(TAG, "onError: "+e.toString());
                }
            });
        }catch (Exception e){
            Log.i(TAG, "出异常了: "+ e.toString());
        }

    }
}
