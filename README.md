# Rxlibrary
基于RxJava2+Retrofit2+RxAndroid+FastJson实现简单易用的网络请求框架。动态方法名，支持对象作为请求接口
# 项目依赖
	Add it in your root build.gradle at the end of repositories:
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
	Add the dependency
  	dependencies {
		//添加第三方依赖（如果项目已经添加可忽略）	
		implementation 'io.reactivex.rxjava2:rxjava:2.1.9'
		implementation 'io.reactivex.rxjava2:rxandroid:2.0.2'
		implementation 'com.squareup.retrofit2:adapter-rxjava2:2.4.0'
		implementation 'com.alibaba:fastjson:1.2.47'
		//
	        implementation 'com.github.shanluwei:Rxlibrary:1.0.3'
	}
# 基本使用
 	1 配置全局的BuilderConfig  
        如：在Application中 onCreate（）方法初始化
         @Override
        public void onCreate() {
                super.onCreate();
                AndroidHttpApis.initGlobalBuilderConfig(Request.BuilderConfig.getBuilderConfig());
         }
   	2 使用
	如：new 一个请求实例对象 ，RquestEntity entity = new RquestEntity(); 根据业务需求自定义RquestEntity要求实现IRequestBean接口，
	默认类名为请求方法名 
        AndroidHttpApis
                .request(entity)
                .build()
                .rxRequest(String.class)
                .compose(HttpSchedulers.<String>io_main())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String result) throws Exception {
                        Log.d("AndroidHttpApis", result);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("AndroidHttpApis", throwable.getMessage());
                    }
                });
  
  
