package com.example.jsandroid;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.JsPromptResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.jsandroid.jsbridge.AndroidJsObject;
import com.example.jsandroid.module.AsyncModule;
import com.example.jsandroid.module.NativeModule;
import com.example.jsandroid.module.SyncModule;
import com.example.jsandroid.scheme.IJsHandler;
import com.example.jsandroid.scheme.SchemeDispatcher;

public class MainActivity extends Activity implements IJsHandler {
    private WebView mWv;
    private SchemeDispatcher mDispatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDispatcher = new SchemeDispatcher(this);
        addModules();
        initViews();
        loadPage();
        addClient();
    }

    private void addModules(){
        mDispatcher.register(new NativeModule())
                .register(new SyncModule())
                .register(new AsyncModule());
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initViews() {
        mWv = findViewById(R.id.wv);
        mWv.getSettings().setJavaScriptEnabled(true);
        WebView.setWebContentsDebuggingEnabled(true);
        mWv.addJavascriptInterface(new AndroidJsObject(mWv), AndroidJsObject.NAME);
    }

    private void loadPage() {
        String url = "file:///android_asset/index.html";
        mWv.loadUrl(url);
    }

    private void addClient() {
        mWv.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 拦截
                if (url.startsWith("over-load-url://")) {
                    Toast.makeText(mWv.getContext(), url, Toast.LENGTH_SHORT).show();
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        mWv.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                mDispatcher.dispatch(message, result);
                return true;
            }

        });
    }

    @Override
    public void callJsFun(final String funName, final String msg) {
        if (mWv != null) {
            // 需要在主线程调用
            Utils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(TextUtils.isEmpty(funName)){
                        return;
                    }
                    String js = "javascript:" + funName + "('" + msg + "')";
                    mWv.evaluateJavascript(js, new ValueCallback<String>(){
                        @Override
                        public void onReceiveValue(String value) {
                            Toast.makeText(mWv.getContext(), value, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }, 0);
        }
    }
}
