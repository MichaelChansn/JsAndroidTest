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
public class SyncModule implements IDispatcher {
    @Override
    public String dispatch(SchemeEntity entity, IJsHandler handler) {
        print(entity);
        return "from SyncModule";
    }

    @Override
    public String moduleName() {
        return "sync";
    }

    private void print(SchemeEntity entity) {
        Log.d("SyncModule", "url-" + entity.getUri().toString());
        Log.d("SyncModule", "host-" + entity.getHost());
        for(String path : entity.getPaths()) {
            Log.d("SyncModule", "path-" + path);
        }
        for(String key :entity.getParams().keySet()) {
            Log.d("SyncModule", "params-" + key+":"+entity.getParams().get(key));
        }
    }
}
