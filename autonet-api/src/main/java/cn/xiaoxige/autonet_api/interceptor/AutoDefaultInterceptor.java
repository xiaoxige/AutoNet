package cn.xiaoxige.autonet_api.interceptor;

import android.text.TextUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * @author by xiaoxige on 2018/5/20.
 *         AutoNet default network interceptor
 */

public class AutoDefaultInterceptor implements Interceptor {
    private static final String GET = "GET";
    private static final String DELETE = "DELETE";
    private String extraDynamicParam;
    private Map<String, String> heads;
    private Long encryptionKey;
    private boolean isEncryption;

    public AutoDefaultInterceptor(String extraDynamicParam, Map<String, String> heads, Long encryptionKey, Boolean isEncryption) {
        this.extraDynamicParam = extraDynamicParam;
        this.heads = heads;
        this.encryptionKey = encryptionKey;
        this.isEncryption = isEncryption;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        // init head
        request = splicingHeads(request);
        // init params
        request = splicingParams(request);

        return chain.proceed(request);
    }

    private Request splicingParams(Request request) {
        HttpUrl httpUrl = request.url();
        String url = httpUrl.toString();
        if (!TextUtils.isEmpty(url) && TextUtils.isEmpty(this.extraDynamicParam)) {
            char c = url.charAt(url.length() - 1);
            if ('/' == c) {
                url = url + this.extraDynamicParam;
            } else {
                url = url + "/" + this.extraDynamicParam;
            }
        }
        httpUrl = httpUrl.newBuilder(url).build();
        request = request.newBuilder().url(httpUrl).build();
        return request;
    }

    private Request splicingHeads(Request request) {
        if (heads == null) {
            return request;
        }
        Headers headers = request.headers();
        Headers.Builder builder = headers.newBuilder();
        Set<String> keys = heads.keySet();
        for (String key : keys) {
            builder.add(key, heads.get(key));
        }
        Headers newHeads = builder.build();
        return request.newBuilder().headers(newHeads).build();
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
