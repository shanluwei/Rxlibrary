package org.shan.rxlibrary.http;

import android.support.annotation.NonNull;

import org.shan.rxlibrary.http.interfaces.IRequestBean;

import java.util.Arrays;

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

    public static void main(String[] args) {
        fun1();
        fun2();
        select_sort();
        insert_sort();
    }

    static void fun1() {
        int[] data = {6, 8, 1, 3, 4, 9, 4, 6, 7, 1};
        int temp = 0;
        for (int i = 0; i < data.length - 1; i++) {
            for (int j = data.length - 1; j > i; j--) {
                if (data[j] < data[j - 1]) {
                    temp = data[j];
                    data[j] = data[j - 1];
                    data[j - 1] = temp;
                }
            }
        }
        System.out.println(Arrays.toString(data));
    }

    static void fun2() {
        int[] data = {6, 8, 1, 3, 4, 9, 4, 6, 7, 1};
        int index = 0;
        int temp;
        for (int i = 0; i < data.length - 1; i++) {
            index = i;
            for (int j = i + 1; j < data.length; j++) {
                if (data[j] < data[index]) {
                    index = j;
                }
                if (j == data.length - 1) {
                    temp = data[i];
                    data[i] = data[index];
                    data[index] = temp;
                }
            }

        }

        System.out.println(Arrays.toString(data));
    }

    public static void select_sort() {
        int[] array = {6, 8, 1, 3, 4, 9, 4, 6, 7, 1};
        int lenth = array.length;
        for (int i = 0; i < lenth - 1; i++) {

            int minIndex = i;
            for (int j = i + 1; j < lenth; j++) {
                if (array[j] < array[minIndex]) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                int temp = array[i];
                array[i] = array[minIndex];
                array[minIndex] = temp;
            }
        }
        System.out.println(Arrays.toString(array));
    }

    public static void insert_sort() {
        int[] array = {6, 8, 1, 3, 4, 9, 4, 6, 7, 1};
        int lenth = array.length;
        int temp;
        for (int i = 0; i < lenth - 1; i++) {
            for (int j = i + 1; j > 0; j--) {
                if (array[j] < array[j - 1]) {
                    temp = array[j - 1];
                    array[j - 1] = array[j];
                    array[j] = temp;
                }
                else {         //不需要交换
                    break;
                }
            }
        }
        System.out.println(Arrays.toString(array));
    }
}
