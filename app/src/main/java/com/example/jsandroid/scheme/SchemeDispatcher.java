package com.example.jsandroid.scheme;

import android.net.Uri;
import android.text.TextUtils;
import android.webkit.JsPromptResult;

import com.example.jsandroid.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * scheme分发规则
 * jsandroid://XXX/xxxx/xxxx/xxx?xx=xx&xx=xx&callback=xxx
 * 其中jsandroid就是我们的scheme协议头
 * XXX是一级业务名
 * xxxx/xxxx、是二级业务
 * ?之后的是参数
 * callback是异步回调
 * <p>
 * 同步回调会立即返回
 * <p>
 * 注意：
 * 参数是经过urlEncode的，防止出现解析错误
 *
 * @author kangsen
 * @version v1.0
 * @since 2020/6/29
 */
public class SchemeDispatcher {
    public static final String SCHEME_PREFIX = "jsandroid";

    private HashMap<String, IDispatcher> mDispatchers;
    private IJsHandler mHandler;

    public SchemeDispatcher(IJsHandler handler) {
        this.mDispatchers = new HashMap<>();
        this.mHandler = handler;
    }

    public SchemeDispatcher register(IDispatcher dispatcher) {
        if (dispatcher != null) {
            mDispatchers.put(dispatcher.moduleName(), dispatcher);
        }
        return this;
    }

    public boolean dispatch(String msg, JsPromptResult result) {
        // 不是指定协议，不拦截
        if (!intercept(msg)) {
            return false;
        }

        SchemeEntity entity = new SchemeEntity(Uri.parse(msg));
        String moduleName = entity.getHost();
        IDispatcher dispatcher = mDispatchers.get(moduleName);
        if (dispatcher != null) {
            String res = dispatcher.dispatch(entity, mHandler);
            result.confirm(Utils.msg(200, res).toString());
            return true;
        }
        return false;
    }

    private boolean intercept(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return false;
        }
        return msg.startsWith(SCHEME_PREFIX + "://");
    }

}
