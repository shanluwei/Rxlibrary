package org.shan.rxlibrary.http;

import android.text.TextUtils;

import org.shan.rxlibrary.http.annotations.PutQueryMap;
import org.shan.rxlibrary.http.interfaces.IRequestBean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by shanluwei on 2018/4/4.
 * 请求实体
 * 子类继承时  如果子类的属性作为接口参数 则属性需实现get set 方法
 */

public abstract class BaseRequestBean implements IRequestBean {
    /**
     * 请求接口地址
     * 如:  {http://192.168.1.100:8080/app/}
     */

    private transient String url;
    /**
     * 请求方法名
     * 如:  http://192.168.1.100:8080/app/{login.action}
     */
    private transient String method;
    /**
     * 请求地址 "?" 后面接的参数 key- values 形式
     * 如果接口后不接 可以忽略
     * 如:  http://192.168.1.100:8080/app/login.action?{key1=value1}&{key2=value2}
     */
    private transient Map<String, String> queryMap = new LinkedHashMap<>();

    public BaseRequestBean() {

    }

    @Override
    public final Map<String, String> getQueryMap() {
        initQueryMap();
        return this.queryMap;

    }

    /**
     * 子类实现 接口地址“？”后接的固定形参 的key value
     */
    private void initQueryMap() {
        this.queryMap.clear();
        parseQueryMap();
    }

    /**
     * 解析类（含父类）中所有包含注解PutQueryMap的字段 并赋值
     */
    private void parseQueryMap() {
        for (Class<?> clas = getClass(); clas != null && clas != Object.class; clas = clas.getSuperclass()) {
            for (Field field : clas.getDeclaredFields()) {
                if (field.isAnnotationPresent(PutQueryMap.class)) {
                    Annotation[] annotation = field.getDeclaredAnnotations();
                    for (Annotation annotation1 : annotation) {
                        if (annotation1.annotationType() == PutQueryMap.class) {
                            String value = ((PutQueryMap) annotation1).value();
                            String name = ((PutQueryMap) annotation1).name();
                            String defaultValue = ((PutQueryMap) annotation1).defaultValue();
                            if ("".equals(value)) {
                                try {
                                    field.setAccessible(true);
                                    value = field.get(this) == null ? null : field.get(this).toString();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            if ("".equals(name)) {
                                name = field.getName();
                            }
                            if (value == null && !TextUtils.isEmpty(defaultValue)) {
                                value = defaultValue;
                            }
                            if (value != null && name != null) {
                                queryMap.put(name, value);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public final String getMethod() {
        return TextUtils.isEmpty(this.method) ? this.method() : this.method;
    }

    @Override
    public final String getUrl() {
        return TextUtils.isEmpty(this.url) ? this.url() : this.url;
    }

    /**
     * 接口方法名
     */
    public abstract String method();

    /**
     * 请求接口地址 必须以"/" 结尾
     */
    public abstract String url();

    public void setUrl(String url) {
        this.url = url;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void parseUrl(String httpUrl) {
        if (TextUtils.isEmpty(httpUrl)) return;
        if (!httpUrl.startsWith("http://") && !httpUrl.startsWith("https://")) return;
        String url = httpUrl.substring(0, httpUrl.lastIndexOf("/") + 1);
        String method = httpUrl.substring(httpUrl.lastIndexOf("/") + 1, httpUrl.length());
        setMethod(method);
        setUrl(url);
    }
}
