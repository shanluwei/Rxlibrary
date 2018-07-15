package org.shan.rxhttp;


import org.shan.rxhttp.interfaces.IRequestBean;

import io.reactivex.internal.functions.ObjectHelper;

/**
 * Created by shanluwei on 2018/5/23.
 */
@SuppressWarnings({"unchecked"})
public final class AndroidHttpApis {
    private AndroidHttpApis() {
    }

    /**
     * 全局配置 OkHttpClient
     * 推荐在 Application 中使用
     * {@link Request#  }
     */
    public static void initGlobalBuilderConfig(Request.BuilderConfig builderConfig) {
        Request.initBuilderConfig(builderConfig);
    }

    /**
     * 构建Builder
     */
    public static <T extends Request.Builder> T request() {
        ObjectHelper.requireNonNull(Request.mGlobalBuilderConfig, "mGlobalBuilderConfig is null");
        return (T) request(Request.mGlobalBuilderConfig.getBuilder());
    }

    public static <T extends Request.Builder> T request( String url) {
        ObjectHelper.requireNonNull(Request.mGlobalBuilderConfig, "mGlobalBuilderConfig is null");
        return (T) request(Request.mGlobalBuilderConfig.getBuilder().addUrl(url));
    }

    public static <T extends Request.Builder> T request(String url,  String method) {
        ObjectHelper.requireNonNull(Request.mGlobalBuilderConfig, "mGlobalBuilderConfig is null");
        return (T) request(Request.mGlobalBuilderConfig.getBuilder().addUrl(url).addMethod(method));
    }

    public static <K extends Request.Builder, T extends IRequestBean> K request(T requestBean) {
        ObjectHelper.requireNonNull(Request.mGlobalBuilderConfig, "mGlobalBuilderConfig is null");
        return (K) request(Request.mGlobalBuilderConfig.getBuilder().addRequestBean(requestBean));
    }

    /**
     * 构建Builder
     */
    public static <T extends Request.Builder> T request( T builder) {
        return builder;
    }

}
