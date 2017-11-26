package cn.xiaoxige.autonet_api.net;

import android.text.TextUtils;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cn.xiaoxige.autonet_api.config.AutoNetConfig;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetEncryptionCallback;
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
    private AutoNetConfig mConfig;
    private IAutoNetEncryptionCallback mAutoNetEncryption;

    public BaseApplicationInterceptor(boolean encryption) {
        this(encryption, null, null);
    }

    public BaseApplicationInterceptor(boolean encryption, AutoNetConfig config) {
        this(encryption, config, null);
    }

    public BaseApplicationInterceptor(boolean encryption, AutoNetConfig config, IAutoNetEncryptionCallback autoNetEncryption) {
        this.mIsEncryption = encryption;
        this.mConfig = config;
        this.mAutoNetEncryption = autoNetEncryption;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request =
                chain.request();

        Headers headers = request.headers();
        /**
         * 添加头部公共参数
         */
        Headers.Builder builder = headers.newBuilder();
        if (mConfig != null) {
            Map headMap = mConfig.getHeader();
            if (headMap != null) {
                Set set = headMap.keySet();
                Iterator iterator = set.iterator();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    String value = (String) headMap.get(key);
                    builder.add(key, value);
                }
            }
        }
        Headers build = builder.build();
        request = request.newBuilder()
                .headers(build)
                .build();


        String method = request.method();
        if (method.equals("GET") || method.equals("DELETE")) {
            HttpUrl url = request.url();

            HttpUrl.Builder urlBulder = url.newBuilder();

            /**
             * 添加get或者delete的公共参数
             */
            if (mConfig != null) {
                Map<String, String> getDelParams = mConfig.getGetDelParams();
                if (getDelParams != null) {
                    Set<String> set = getDelParams.keySet();
                    Iterator<String> iterator = set.iterator();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        String value = getDelParams.get(key);
                        urlBulder.addQueryParameter(key, value);
                    }
                }
            }

            HttpUrl httpUrl = urlBulder.build();
            String uri = httpUrl.url().toString();
            // 参数加密
            if (mIsEncryption) {
                String[] urlSplit = uri.split("\\?");
                if (urlSplit != null && urlSplit.length == 2) {
                    if (mAutoNetEncryption != null) {
                        String encryption = mAutoNetEncryption.encryption(urlSplit[1]);
                        uri = urlSplit[0] + "?" + encryption;
                    }
                }
            }

            if (TextUtils.isEmpty(uri)) {
                request = request.newBuilder()
                        .url(httpUrl)
                        .build();
            } else {
                request = request.newBuilder()
                        .url(uri)
                        .build();
            }

        } else {
            if (mIsEncryption) {
                RequestBody body = request.body();
                String badyContent = getBadyContent(body);
                // body进行加密
                if (mAutoNetEncryption != null && !TextUtils.isEmpty(badyContent)) {
                    badyContent = mAutoNetEncryption.encryption(badyContent);
                }
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
