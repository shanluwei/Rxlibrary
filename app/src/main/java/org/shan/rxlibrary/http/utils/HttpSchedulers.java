package org.shan.rxlibrary.http.utils;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by shan on 2016/11/24.
 * E-mail:664895026@qq.com
 * Version: 1.0
 */
public class HttpSchedulers {
    /**
     * 将subscribeOn和observeOn进行封装
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> io_main() {
        return new ObservableTransformer<T, T>() {
            @Override
            public Observable<T> apply(Observable<T> tObservable) {
                return tObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> T parse(Class<T> resultType0, String resultInfo) {
        if (resultInfo == null || TextUtils.equals(resultInfo, "null")) {
            return (T) "";
        }
//        if (resultType0 == String.class) {
//            return (T) JSON.parse(resultInfo);
//        } else {
        return JSON.parseObject(resultInfo, resultType0);
//        }
    }
}
