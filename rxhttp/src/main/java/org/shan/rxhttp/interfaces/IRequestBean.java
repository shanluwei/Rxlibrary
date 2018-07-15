package org.shan.rxhttp.interfaces;


import org.shan.rxhttp.Request;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by shanluwei on 2018/5/23.
 */

public interface IRequestBean extends Serializable {
    /**
     * 接口方法名
     *
     * @see Request#mMethod
     */
    String getMethod();

    /**
     * 请求接口地址 必须以"/" 结尾
     *
     * @see Request#mUrl
     */
    String getUrl();

    /**
     * @see Request#mQueryMap
     */
    Map<String, String> getQueryMap();
}
