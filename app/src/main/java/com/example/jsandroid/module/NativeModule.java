package com.example.jsandroid.module;

import android.util.Log;

import com.example.jsandroid.scheme.IDispatcher;
import com.example.jsandroid.scheme.IJsHandler;
import com.example.jsandroid.scheme.SchemeEntity;

/**
 * @author kangsen
 * @version v1.0
 * @since 2020/6/29
 */
public class NativeModule implements IDispatcher {
    @Override
    public String dispatch(SchemeEntity entity, IJsHandler handler) {
        print(entity);
        return "from NativeModule";
    }

    @Override
    public String moduleName() {
        return "native";
    }

    private void print(SchemeEntity entity) {
        Log.d("NativeModule", "url-" + entity.getUri().toString());
        Log.d("NativeModule", "host-" + entity.getHost());
        for(String path : entity.getPaths()) {
            Log.d("NativeModule", "path-" + path);
        }
        for(String key :entity.getParams().keySet()) {
            Log.d("NativeModule", "params-" + key+":"+entity.getParams().get(key));
        }
    }
}
