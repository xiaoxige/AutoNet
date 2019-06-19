package cn.xiaoxige.autonet_api.client;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cn.xiaoxige.autonet_api.interceptor.AutoDefaultInterceptor;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetEncryptionCallback;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetHeadCallBack;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * @author by xiaoxige on 2018/5/20.
 * Okhttp3 structure client
 */

public class Client {

    private Client() {
    }

    public static OkHttpClient client() {
        return new OkHttpClient.Builder()
                .sslSocketFactory(createSSLSocketFactory())
                .hostnameVerifier(new TrustAllHostnameVerifier())
                .build();
    }

    public static OkHttpClient client(Object flag, Map<String, Object> heads, Long writeOutTime, Long readOutTime, Long connectOutTime,
                                      Long encryptionKey, Boolean isEncryption, List<Interceptor> interceptors, IAutoNetEncryptionCallback encryptionCallback, IAutoNetHeadCallBack headCallBack) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .sslSocketFactory(createSSLSocketFactory())
                .hostnameVerifier(new TrustAllHostnameVerifier())
                .addNetworkInterceptor(new AutoDefaultInterceptor(flag, heads, encryptionKey, isEncryption, encryptionCallback, headCallBack))
                .addNetworkInterceptor(new StethoInterceptor())
                .writeTimeout(writeOutTime, TimeUnit.SECONDS)
                .readTimeout(readOutTime, TimeUnit.SECONDS)
                .connectTimeout(connectOutTime, TimeUnit.SECONDS);


        if (interceptors != null) {
            for (Interceptor interceptor : interceptors) {
                builder.addNetworkInterceptor(interceptor);
            }
        }

        return builder.build();
    }

    private static class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }


}
