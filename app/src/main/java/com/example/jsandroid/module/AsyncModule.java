package com.example.jsandroid.module;

import android.util.Log;

import com.example.jsandroid.Utils;
import com.example.jsandroid.scheme.IDispatcher;
import com.example.jsandroid.scheme.IJsHandler;
import com.example.jsandroid.scheme.SchemeEntity;

/**
 * @author kangsen
 * @version v1.0
 * @since 2020/6/29
 */
public class AsyncModule implements IDispatcher {
    @Override
    public String dispatch(SchemeEntity entity, IJsHandler handler) {
        print(entity);
        async(entity, handler);
        return "from AsyncModule";
    }

    @Override
    public String moduleName() {
        return "async";
    }

    private void print(SchemeEntity entity) {
        Log.d("AsyncModule", "url-" + entity.getUri().toString());
        Log.d("AsyncModule", "host-" + entity.getHost());
        for (String path : entity.getPaths()) {
            Log.d("AsyncModule", "path-" + path);
        }
        for (String key : entity.getParams().keySet()) {
            Log.d("AsyncModule", "params-" + key + ":" + entity.getParams().get(key));
        }
    }

    private void async(final SchemeEntity entity, final IJsHandler handler) {
        Utils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                handler.callJsFun(entity.getParams().get("callback"), Utils.msg(200,"delay 3000 ms in AsyncModule").toString());
            }
        }, 3000);
    }
}
