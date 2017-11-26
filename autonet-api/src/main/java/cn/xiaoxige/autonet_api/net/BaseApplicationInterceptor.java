package cn.xiaoxige.autonet_api.net;

import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * @author by zhuxiaoan on 2017/11/8 0008.
 *         网络请求公共的拦截器
 */

public class BaseApplicationInterceptor implements Interceptor {

    private boolean mIsEncryption;

    public BaseApplicationInterceptor(boolean encryption) {
        mIsEncryption = encryption;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request =
                chain.request();

        Headers headers = request.headers();

        Headers build = headers.newBuilder()
                .build();
        request = request.newBuilder()
                .headers(build)
                .build();

        String method = request.method();
        if (method.equals("GET") || method.equals("DELETE")) {
            HttpUrl url = request.url();
            HttpUrl httpUrl = url.newBuilder()
                    .build();
            request = request.newBuilder()
                    .url(httpUrl)
                    .build();
        } else {
            if (mIsEncryption) {
                RequestBody body = request.body();
                String badyContent = getBadyContent(body);
                // body进行加密
                if (!TextUtils.isEmpty(badyContent)) {
                    RequestBody requestBody = RequestBody.create(body.contentType(), badyContent);
                    request = request.newBuilder()
                            .method(method, requestBody)
                            .build();
                }
            }
        }
        return chain.proceed(request);
    }


    private String getBadyContent(RequestBody body) {
        if (body == null) {
            return null;
        }
        String babyContent;
        try {
            Buffer buffer = new Buffer();
            body.writeTo(buffer);
            babyContent = buffer.readUtf8();
        } catch (Exception e) {
            return null;
        }
        return babyContent;
    }

}
