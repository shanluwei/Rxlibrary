package org.shan.rxlibrary.http;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.observers.DefaultObserver;
import okhttp3.ResponseBody;

public abstract class FileDownLoadObserver<T extends File> extends DefaultObserver<T> {

    @Override
    public void onNext(T t) {
        onDownLoadSuccess(t);
    }

    //可以重写，具体可由子类实现
    @Override
    public void onComplete() {
    }

    //下载成功的回调
    public abstract void onDownLoadSuccess(T t);

    //下载进度监听
    public abstract void onProgress(int progress, long total);

    /**
     * 目标文件夹
     *
     * @return
     */
    public abstract String destFileDir();

    /**
     * 目标文件名
     *
     * @return
     */
    public abstract String destFileName();

    /**
     * 将文件写入本地
     *
     * @param responseBody 请求结果全体
     * @return 写入完成的文件
     * @throws IOException IO异常
     */
    public File saveFile(ResponseBody responseBody) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        try {
            is = responseBody.byteStream();
            final long total = responseBody.contentLength();
            long sum = 0;

            File dir = new File(destFileDir());
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, destFileName());
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                sum += len;
                fos.write(buf, 0, len);
                final long finalSum = sum;
                //这里就是对进度的监听回调
                onProgress((int) (finalSum * 100 / total), total);
            }
            fos.flush();

            return file;

        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}