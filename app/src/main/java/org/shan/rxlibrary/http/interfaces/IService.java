package org.shan.rxlibrary.http.interfaces;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;

public interface IService {
    /**
     * 数据解析
     * RxJava
     *
     * @param var1 方法名
     * @param var2 地址参数
     * @param var3 请求实体
     * @return
     */
//    @POST("{MethodName}")
//    Observable<ResponseBody> postObservable(@Path("MethodName") String var1, @QueryMap Map<String, String> var2, @Body BaseRequestBean var3);
    @POST("{MethodName}")
    Observable<ResponseBody> postObservable(@Path("MethodName") String var1, @QueryMap Map<String, String> var2, @Body Map<String, String> var3);

//    @POST("{MethodName}")
//    Observable<ResponseBody> postObservable(@Path("MethodName") String var1, @QueryMap Map<String, String> var2, @Body JSON var3);

    @GET("{MethodName}")
    Observable<ResponseBody> getObservable(@Path("MethodName") String var1, @QueryMap Map<String, String> var2, @Body Map<String, String> var3);

    /**
     * 数据解析
     * Call
     *
     * @param var1 方法名
     * @param var2 地址参数
     * @param var3 请求实体
     * @return
     */
//    @POST("{MethodName}")
//    Call<ResponseBody> postCallback(@Path("MethodName") String var1, @QueryMap Map<String, String> var2, @Body BaseRequestBean var3);
    @POST("{MethodName}")
    Call<ResponseBody> postCallback(@Path("MethodName") String var1, @QueryMap Map<String, String> var2, @Body Map<String, String> var3);

//    @GET("{MethodName}")
//    Call<ResponseBody> getCallback(@Path("MethodName") String var1, @QueryMap Map<String, String> var2, @Body BaseRequestBean var3);

    @POST("{MethodName}")
    Call<ResponseBody> getCallback(@Path("MethodName") String var1, @QueryMap Map<String, String> var2, @Body Map<String, String> var3);

    @Streaming
    @GET("{MethodName}")
    Observable<ResponseBody> downLoadFile(@Path("MethodName") String var1, @QueryMap Map<String, String> var2, @Body Map<String, String> var3);

    @Multipart
    @POST("{MethodName}")
    Observable<ResponseBody> uploadFiles(@Path("MethodName") String var1, @QueryMap Map<String, String> var2, @PartMap() Map<String, RequestBody> maps);
}