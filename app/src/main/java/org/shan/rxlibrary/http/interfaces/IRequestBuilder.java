package org.shan.rxlibrary.http.interfaces;

import org.shan.rxlibrary.http.enums.RequestMethodType;

import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * Created by shanluwei on 2018/5/23.
 */

public interface IRequestBuilder {
    String url();

    String method();

    Map<String, String> queryMap();

    Map<String, String> paramMap();

    Map<String, RequestBody> uploadMap();

    RequestMethodType requestType();

    OkHttpClient httpClient();

}
