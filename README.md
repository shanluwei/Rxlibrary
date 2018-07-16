# Rxlibrary
基于RxJava2+Retrofit2+RxAndroid+FastJson实现简单易用的网络请求框架。动态方法名，支持对象作为请求接口

#1 基本使用(1)\n
Add it in your root build.gradle at the end of repositories:
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Add the dependency
  	dependencies {
		//添加第三方依赖	
		implementation 'io.reactivex.rxjava2:rxjava:2.1.9'
		implementation 'io.reactivex.rxjava2:rxandroid:2.0.2'
		implementation 'com.squareup.retrofit2:adapter-rxjava2:2.4.0'
		//
	        implementation 'com.github.shanluwei:Rxlibrary:1.0.3'
	}

#2 基本使用(2)
  1 配置全局的BuilderConfig  
        如：在Application中 onCreate（）方法初始化
         @Override
        public void onCreate() {
                super.onCreate();
                AndroidHttpApis.initGlobalBuilderConfig(Request.BuilderConfig.getBuilderConfig());
         }
   
  
  
