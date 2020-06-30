package com.example.jsandroid.scheme;

/**
 * @author kangsen
 * @version v1.0
 * @since 2020/6/29
 */
public interface IDispatcher {

    // 分发逻辑
    public String dispatch(SchemeEntity entity, IJsHandler handler);

    // 业务模块名字
    public String moduleName();
}
