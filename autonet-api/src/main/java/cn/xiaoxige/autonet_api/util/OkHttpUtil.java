package cn.xiaoxige.autonet_api.util;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cn.xiaoxige.autonet_api.config.AutoNetConfig;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetEncryptionCallback;
import cn.xiaoxige.autonet_api.net.BaseApplicationInterceptor;
import okhttp3.OkHttpClient;

/**
 * Created by zhuxiaoan on 2017/11/26.
 */

public class OkHttpUtil {


    public static OkHttpClient start(boolean isencryption, long mencryptionkey, AutoNetConfig config,
                                     long writeTime, long readtime, long connectOutTime,
                                     IAutoNetEncryptionCallback autoNetEncryptionCallback) {
        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(createSSLSocketFactory())
                .hostnameVerifier(new TrustAllHostnameVerifier())
                .addNetworkInterceptor(new BaseApplicationInterceptor(isencryption, mencryptionkey, config, autoNetEncryptionCallback))
                .addNetworkInterceptor(new StethoInterceptor())
                .writeTimeout(writeTime, TimeUnit.SECONDS)
                .readTimeout(readtime, TimeUnit.SECONDS)
                .connectTimeout(connectOutTime, TimeUnit.SECONDS)
                .build();
        return client;
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
