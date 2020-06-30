package com.example.jsandroid;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author kangsen
 * @version v1.0
 * @since 2020/6/28
 */
public class Utils {
    private static Handler sHandler = new Handler(Looper.getMainLooper());

    public static void runOnUiThread(Runnable runnable, long delayMs) {

        if(delayMs > 0){
            sHandler.postDelayed(runnable, delayMs);
            return;
        }

        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            sHandler.post(runnable);
        } else {
            runnable.run();
        }
    }

    public static JSONObject msg(int code, String msg){
        JSONObject json = new JSONObject();
        try {
            json.put("code", code);
            json.put("msg", msg == null ? "" : msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }
}
