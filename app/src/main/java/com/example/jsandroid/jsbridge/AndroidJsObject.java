package com.example.jsandroid.jsbridge;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.example.jsandroid.Utils;
import com.example.jsandroid.scheme.IJsHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author kangsen
 * @version v1.0
 * @since 2020/6/28
 */
public class AndroidJsObject {
    private Context mContext;
    private IJsHandler mHandler;
    public static final String NAME = "android_obj";

    public AndroidJsObject(WebView wv, IJsHandler handler) {
        this.mContext = wv.getContext().getApplicationContext();
        this.mHandler = handler;
    }

    @JavascriptInterface
    public String callAndroid(String params) {
        JSONObject json = parse(params);
        if (json == null) {
            return retMsg(-1, "error params");
        }
        final String msg = json.optString("params");
        final String callback = json.optString("callback");
        Toast.makeText(mContext, params, Toast.LENGTH_SHORT).show();

        if (!TextUtils.isEmpty(callback)) {

            // 异步回调
            Utils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mHandler.callJsFun(callback, "delay 1000ms");
                }
            }, 1000);
        }

        return retMsg(200, "get your params");
    }


    private JSONObject parse(String msg) {
        JSONObject obj = null;
        try {
            obj = new JSONObject(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    private String retMsg(int code, String msg) {
        JSONObject ret = new JSONObject();
        try {
            ret.put("code", code);
            ret.put("msg", msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ret.toString();
    }

}
