package cn.xiaoxige.autonet_api.config;

import android.support.v4.util.ArrayMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;

/**
 * @author by zhuxiaoan on 2018/5/17 0017.
 *         AutoNet initialization parameter configuration
 *         These values will not be changed again after initialization
 */

public class AutoNetConfig {

    private boolean isOpenStetho;
    private Map<String, String> baseUrl;
    private Map<String, String> headParam;
    private List<Interceptor> interceptors;

    private AutoNetConfig() {
    }

    private AutoNetConfig(Builder builder) {
        this.isOpenStetho = builder.isOpenStetho;
        this.baseUrl = builder.baseUrl;
        this.headParam = builder.headParam;
        this.interceptors = builder.interceptors;
    }

    public static class Builder {

        private boolean isOpenStetho;
        private Map<String, String> baseUrl;
        private Map<String, String> headParam;
        private List<Interceptor> interceptors;

        public Builder() {
            isOpenStetho = false;
            baseUrl = new ArrayMap<>();
            headParam = new ArrayMap<>();
            interceptors = new ArrayList<>();
        }

        public Builder isOpenStetho(boolean isOpenStetho) {
            this.isOpenStetho = isOpenStetho;
            return this;
        }

        public Builder setBaseUrl(Map<String, String> baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder setHeadParam(Map<String, String> headParam) {
            this.headParam = headParam;
            return this;
        }

        public Builder setInterceptors(List<Interceptor> interceptors) {
            this.interceptors = interceptors;
            return this;
        }

        public AutoNetConfig build() {
            return new AutoNetConfig(this);
        }
    }

    public boolean isOpenStetho() {
        return isOpenStetho;
    }

    public Map<String, String> getBaseUrl() {
        return baseUrl;
    }

    public Map<String, String> getHeadParam() {
        return headParam;
    }

    public List<Interceptor> getInterceptors() {
        return interceptors;
    }

}
