package com.example.jsandroid.scheme;

import android.net.Uri;
import android.text.TextUtils;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;

/**
 * 用来表示是scheme的实体
 *
 * @author kangsen
 * @version v1.0
 * @since 2020/6/29
 */
public class SchemeEntity {
    private Uri mUri;
    private List<String> mPaths;
    private String mHost;
    private HashMap<String, String> mParams;

    public SchemeEntity(Uri uri) {
        this.mUri = uri;
        this.mHost = uri.getHost();
        this.mPaths = uri.getPathSegments();
        this.mParams = getParams(uri.toString());
    }

    public Uri getUri() {
        return this.mUri;
    }

    public String getHost() {
        return this.mHost;
    }

    public HashMap<String, String> getParams() {
        return this.mParams;
    }

    public List<String> getPaths() {
        return this.mPaths;
    }

    // 简单解析一下，没用uri.getQuery()
    private HashMap<String, String> getParams(String scheme) {
        HashMap<String, String> params = new HashMap<>();
        if (TextUtils.isEmpty(scheme)) {
            return params;
        }
        int qsi = scheme.indexOf("?");
        int fsi = scheme.indexOf("#");
        if (qsi < 0) {
            return params;
        }
        String query;
        if (fsi < 0) {
            query = scheme.substring(qsi + 1);
        } else {
            query = scheme.substring(qsi + 1, fsi);
        }

        String[] items = query.split("&");
        for (String item : items) {
            int index = item.indexOf("=");
            if (index > 0) {
                try {
                    params.put(URLDecoder.decode(item.substring(0, index)), URLDecoder.decode(item.substring(index + 1)));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
        return params;
    }
}
