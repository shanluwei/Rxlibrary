package org.shan.rxlibrary.http;

import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.retrofit.Retrofit2ConverterFactory;

import org.shan.rxlibrary.http.enums.RequestMethodType;
import org.shan.rxlibrary.http.interfaces.IRequestBean;
import org.shan.rxlibrary.http.interfaces.IRequestBuilder;
import org.shan.rxlibrary.http.interfaces.IService;
import org.shan.rxlibrary.http.utils.HttpSchedulers;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;


/**
 * Created by shanluwei on 2018/5/23.
 */
@SuppressWarnings({"unchecked"})
public class Request {
    private static ArrayMap<String, Object> sIServiceManager = new ArrayMap<>();
    /**
     * 请求地址  如:{http://192.168.1.1:8080/action/}
     * 必须需以"/"结尾
     */
    private String mUrl;
    /**
     * 请求方法   如: http://192.168.1.1:8080/action/{login.do}
     * method = login.do
     */
    private String mMethod;
    /**
     * 请求地址后跟的参数（key =value）
     * 如:
     * http://192.168.1.1:8080/action/method.do ? {key1=value1}&{key2 =value2}
     */
    private Map<String, String> mQueryMap;
    /**
     * 请求参数（key = value）
     */
    private Map<String, String> mParamsMap;

    private Map<String, RequestBody> mUploadMap;

    private RequestMethodType requestMethodType;

    private static Retrofit getRetrofit(String baseUrl, OkHttpClient httpClient) {
        return new Retrofit.Builder()
                .client(httpClient)
                .addConverterFactory(new Retrofit2ConverterFactory())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }

    private OkHttpClient mOkHttpClient;
    static BuilderConfig mGlobalBuilderConfig;

    /**
     * 初始化默认配置
     * @see AndroidHttpApis#initGlobalBuilderConfig(BuilderConfig)
     */
//    static {
//        mGlobalBuilderConfig = BuilderConfig.getDefaultBuilderConfig();
//    }

    /**
     * 全局配置
     */
    static void initBuilderConfig(BuilderConfig builderConfig) {
        mGlobalBuilderConfig = builderConfig;
    }

    /**
     * 创建请求接口
     * 当默认 {@link IService} 不能达到需求时 重写{@link Request#rxRequest}
     * 调用次方法创建自定义的接口
     *
     * @param baseUrl 请求基地址 "/"结束  不包含请求方法  {@link #mUrl}
     * @param service 接口泛型
     * @param <T>     具体接口
     * @return 返回接口实体对象
     * @see IService
     */
    protected final <T> T createService(String baseUrl, Class<T> service) {
        OkHttpClient client = mOkHttpClient == null ? mGlobalBuilderConfig.getOkHttpClient() : mOkHttpClient;
        String key = baseUrl + service.getName() + client.hashCode();
        Object iService = sIServiceManager.get(key);
        if (iService == null) {
            iService = getRetrofit(baseUrl, client).create(service);
            sIServiceManager.put(key, iService);
        }
        return (T) iService;
    }

    public Request(IRequestBuilder iRequestBuilder) {
        this.mUrl = iRequestBuilder.url();
        this.mMethod = iRequestBuilder.method();
        this.mParamsMap = iRequestBuilder.paramMap();
        this.mQueryMap = iRequestBuilder.queryMap();
        this.requestMethodType = iRequestBuilder.requestType();
        this.mUploadMap = iRequestBuilder.uploadMap();
        this.mOkHttpClient = iRequestBuilder.httpClient();
    }

    public Call<ResponseBody> request() {
        switch (requestMethodType) {
            case GET:
                return createService(mUrl, IService.class)
                        .getCallback(mMethod, mQueryMap, mParamsMap);
            default:
                return createService(mUrl, IService.class)
                        .postCallback(mMethod, mQueryMap, mParamsMap);
        }
    }

    public void request(Callback callback) {
        Call<ResponseBody> call = request();
        call.enqueue(callback);
    }

    /**
     * RxJava 网络请求 默认POST
     * {@link IService} 目前实现支持 {@link RequestMethodType#GET, RequestMethodType#POST} 请求
     * 不能达到需求时 重写此方法
     * 可自定义的接口{@link IService},自定义{@link Builder}
     *
     * @see Request#createService
     */
    public Observable<ResponseBody> rxRequest() {
        switch (requestMethodType) {
            case GET:
                return createService(mUrl, IService.class)
                        .getObservable(mMethod, mQueryMap, mParamsMap);
            default:
                return createService(mUrl, IService.class)
                        .postObservable(mMethod, mQueryMap, mParamsMap);
        }
    }

    /**
     * RxJava 网络请求 默认POST
     *
     * @param tClass 数据返回类型参数
     * @param <T>    数据返回类型
     * @return Observable
     */
    public <T> Observable<T> rxRequest(final Class<T> tClass) {
        return rxRequest().map(new Function<ResponseBody, T>() {
            @Override
            public T apply(ResponseBody responseBody) throws Exception {
                return HttpSchedulers.parse(tClass, responseBody.string());
            }
        });
    }


    /**
     * RxJava 网络下载
     */
    public Observable<ResponseBody> rxDownLoad() {
        return createService(mUrl, IService.class).downLoadFile(mMethod, mQueryMap, mParamsMap);
    }

    /**
     * 网络下载
     *
     * @param fileDownLoadObserver
     */
    public void download(final FileDownLoadObserver<File> fileDownLoadObserver) {
        rxDownLoad()
                .subscribeOn(Schedulers.io())//subscribeOn和ObserOn必须在io线程，如果在主线程会出错
                .observeOn(Schedulers.io())
                .observeOn(Schedulers.computation())//需要
                .map(new Function<ResponseBody, File>() {
                    @Override
                    public File apply(ResponseBody responseBody) throws Exception {
                        return fileDownLoadObserver.saveFile(responseBody);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fileDownLoadObserver);

    }

    /**
     * RxJava 文件上传(暂未测试)
     */
    public Observable<ResponseBody> rxUploadFiles() {
        return createService(mUrl, IService.class)
                .uploadFiles(mMethod, mQueryMap, mUploadMap);
    }

    /**
     * 默认请求构建
     */
    public static class DefaultRequestBuilder extends Builder<Builder, Request> {
        @Override
        public Request waitBuild() {
            return new Request(this);
        }

        public DefaultRequestBuilder() {
            super();
        }

        public DefaultRequestBuilder(String url) {
            super(url);
        }

        public <T extends IRequestBean> DefaultRequestBuilder(T requestBean) {
            super(requestBean);
        }

        public DefaultRequestBuilder(String url, String method) {
            super(url, method);
        }

        @Override
        public String url() {
            if (TextUtils.isEmpty(super.url())) return super.url();
            if (!super.url().startsWith("http://") && !super.url().startsWith("https://"))
                return super.url();
            //不是以"/"结尾   如格式为"http://192.168.1.1:8080/action/login.action?id=6&token=123"
            if (!("/".equals(super.url().substring(super.url().lastIndexOf("/"), super.url().length())))) {
                String method = super.url().substring(super.url().lastIndexOf("/") + 1, super.url().length());
                String url = super.url().substring(0, super.url().lastIndexOf("/") + 1);
                if (method.contains("?")) {
                    String[] strings = method.split("\\?");
                    method = strings[0];
                    if (strings[1].contains("&")) {
                        String[] strings1 = strings[1].split("&");
                        for (String aStrings1 : strings1) {
                            addQueryParam(aStrings1.split("=")[0], aStrings1.split("=")[1]);
                        }
                    } else {
                        addQueryParam(strings[1].split("=")[0], strings[1].split("=")[1]);
                    }
                }
                addMethod(method);
                addUrl(url);
            }
            return super.url();
        }
    }

    public static abstract class Builder<B extends Builder, K extends Request> implements IRequestBuilder {
        private RequestMethodType mRequestMethodType = RequestMethodType.POST;
        /**
         * {@link Request#mUrl}
         */
        private String mUrl;
        /**
         * {@link Request#mMethod}
         */
        private String mMethod;
        /**
         * {@link Request#mQueryMap}
         */
        private Map<String, String> mQueryMap = new HashMap<>();
        /**
         * {@link Request#mParamsMap}
         */
        private Map<String, String> mParamsMap = new HashMap<>();
        private Map<String, RequestBody> mUploadMap = new HashMap<>();
        private OkHttpClient mOkHttpClient;
        private Builder mRequestBuilder;

        public Builder() {
        }

        public Builder(String url) {
            this.mUrl = url;
        }

        public <T extends IRequestBean> Builder(T requestBean) {
            parseBean(requestBean);
        }

        public Builder(String url, String method) {
            this.mUrl = url;
            this.mMethod = method;
        }

        public final <T extends IRequestBean> B addRequestBean(T requestBean) {
            parseBean(requestBean);
            return (B) this;
        }

        public final B addRequestType(RequestMethodType requestMethodType) {
            this.mRequestMethodType = requestMethodType;
            return (B) this;
        }

        public final B addUrl(String url) {
            this.mUrl = url;
            return (B) this;
        }

        public final B addMethod(String method) {
            this.mMethod = method;
            return (B) this;
        }

        public final B addQueryParam(String key, String value) {
            if (key != null && value != null) {
                mQueryMap.put(key, value);
            }
            return (B) this;
        }

        public final B addQueryParam(Map<String, String> queryMap) {
            copyMap(queryMap, mQueryMap);
            return (B) this;
        }

        public final B addRequestParam(String key, String value) {
            if (key != null && value != null) {
                mParamsMap.put(key, value);
            }
            return (B) this;
        }

        public final B addRequestParam(Map<String, String> paramsMap) {
            copyMap(paramsMap, mParamsMap);
            return (B) this;
        }

        public final B addUploadRequestParam(String key, RequestBody value) {
            if (key != null && value != null) {
                mUploadMap.put(key, value);
            }
            return (B) this;
        }

        public final B addUploadRequestParam(Map<String, RequestBody> uploadMap) {
            copyMap(uploadMap, this.mUploadMap);
            return (B) this;
        }

        public final B addOkHttpClient(OkHttpClient okHttpClient) {
            this.mOkHttpClient = okHttpClient;
            return (B) this;
        }

        public final <T extends Builder> B addRequestBuilder(Class<T> builder) {
            this.mRequestBuilder = BuilderConfig.get(builder);
            return (B) this;
        }

        public final <T extends Builder> B addRequestBuilder(T builder) {
            this.mRequestBuilder = builder;
            return (B) this;
        }

        private static <T, B extends T> void copyMap(Map<String, B> srcMap, Map<String, T> tagMap) {
            if (srcMap == null || srcMap.size() == 0 || tagMap == null) return;
            for (Map.Entry<String, B> entry : srcMap.entrySet()) {
                if (entry.getKey() == null || entry.getValue() == null)
                    continue;
                tagMap.put(entry.getKey(), entry.getValue());
            }
        }

        private <T extends IRequestBean> void parseBean(T requestBean) {
            this.mUrl = requestBean.getUrl();
            this.mMethod = requestBean.getMethod();
            copyMap(requestBean.getQueryMap(), mQueryMap);
            Map<String, String> map = JSON.parseObject(JSON.toJSONString(requestBean), HashMap.class);
            copyMap(map, mParamsMap);
        }



        /**
         * 构建请求实体
         *
         * @return {@link Request}
         */
        protected abstract K waitBuild();

        public final K build() {
            if (mRequestBuilder != null && mGlobalBuilderConfig != null) {
                Builder.copyBuilderFiled(mGlobalBuilderConfig.getTempBuilder(), mRequestBuilder);
            }
            return mRequestBuilder != null ? (K) mRequestBuilder.waitBuild() : waitBuild();
        }


        @Override
        public String url() {
            return this.mUrl;
        }

        @Override
        public String method() {
            return this.mMethod;
        }

        @Override
        public final Map<String, String> queryMap() {
            return this.mQueryMap;
        }

        @Override
        public final Map<String, String> paramMap() {
            return this.mParamsMap;
        }

        @Override
        public final Map<String, RequestBody> uploadMap() {
            return this.mUploadMap;
        }

        @Override
        public final RequestMethodType requestType() {
            return this.mRequestMethodType;
        }

        @Override
        public OkHttpClient httpClient() {
            return this.mOkHttpClient;
        }

        private static void copyBuilderFiled(Builder src, Builder target) {
            if (src == null || target == null) return;
            target.mRequestMethodType = src.mRequestMethodType;
            target.mUrl = src.mUrl;
            target.mMethod = src.mMethod;
            target.mQueryMap = src.mQueryMap;
            target.mParamsMap = src.mParamsMap;
            target.mUploadMap = src.mUploadMap;
            target.mOkHttpClient = src.mOkHttpClient;
        }
    }

    /**
     * 配置 OkHttpClient + Builder
     */
    public static class BuilderConfig {
        private OkHttpClient mOkHttpClient = new OkHttpClient();//默认值
        private Class<? extends Builder> aClass = DefaultRequestBuilder.class;//默认值

        private BuilderConfig() {
        }

        private static <T> T get(Class<T> cc) {
            try {
                return cc.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public static BuilderConfig getBuilderConfig() {
            return new BuilderConfig();
        }

        public <T extends Builder> BuilderConfig configBuilder(Class<T> builder) {
            this.aClass = builder;
            return this;
        }

        private Builder mTempBuilder;

        public final Builder getBuilder() {
            mTempBuilder = get(aClass);
            return mTempBuilder;
        }

        private Builder getTempBuilder() {
            return mTempBuilder;
        }

        public static BuilderConfig getDefaultBuilderConfig() {
            return new BuilderConfig().configBuilder(DefaultRequestBuilder.class).configOkHttpClient(OkHttpClient.class);
        }

        public final OkHttpClient getOkHttpClient() {
            return this.mOkHttpClient;
        }

        public <T extends OkHttpClient> BuilderConfig configOkHttpClient(Class<T> okHttpClient) {
            this.mOkHttpClient = get(okHttpClient);
            return this;
        }

        public <T extends OkHttpClient> BuilderConfig configOkHttpClient(T okHttpClient) {
            this.mOkHttpClient = okHttpClient;
            return this;
        }
    }
}
