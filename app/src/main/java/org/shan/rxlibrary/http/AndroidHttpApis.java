package org.shan.rxlibrary.http;

import android.support.annotation.NonNull;

import org.shan.rxlibrary.http.interfaces.IRequestBean;

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
    public static void initGlobalBuilderConfig(@NonNull Request.BuilderConfig builderConfig) {
        Request.initBuilderConfig(builderConfig);
    }

    /**
     * 构建Builder
     */
    public static <T extends Request.Builder> T request() {
        ObjectHelper.requireNonNull(Request.mGlobalBuilderConfig, "mGlobalBuilderConfig is null");
        return (T) request(Request.mGlobalBuilderConfig.getBuilder());
    }

    public static <T extends Request.Builder> T request(@NonNull String url) {
        ObjectHelper.requireNonNull(Request.mGlobalBuilderConfig, "mGlobalBuilderConfig is null");
        return (T) request(Request.mGlobalBuilderConfig.getBuilder().addUrl(url));
    }

    public static <T extends Request.Builder> T request(@NonNull String url, @NonNull String method) {
        ObjectHelper.requireNonNull(Request.mGlobalBuilderConfig, "mGlobalBuilderConfig is null");
        return (T) request(Request.mGlobalBuilderConfig.getBuilder().addUrl(url).addMethod(method));
    }

    public static <K extends Request.Builder, T extends IRequestBean> K request(@NonNull T requestBean) {
        ObjectHelper.requireNonNull(Request.mGlobalBuilderConfig, "mGlobalBuilderConfig is null");
        return (K) request(Request.mGlobalBuilderConfig.getBuilder().addRequestBean(requestBean));
    }

    /**
     * 构建Builder
     */
    public static <T extends Request.Builder> T request(@NonNull T builder) {
        return builder;
    }

}
