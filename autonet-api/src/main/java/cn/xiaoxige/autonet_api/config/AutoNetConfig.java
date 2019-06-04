package cn.xiaoxige.autonet_api.config;

import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

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
    private Map<String, String> domainNames;
    private Map<String, Object> headParam;
    private List<Interceptor> interceptors;

    private AutoNetConfig() {
    }

    private AutoNetConfig(Builder builder) {
        this.isOpenStetho = builder.isOpenStetho;
        this.domainNames = builder.domainNames;
        this.headParam = builder.headParam;
        this.interceptors = builder.interceptors;
    }

    public static class Builder {

        private boolean isOpenStetho;
        private Map<String, String> domainNames;
        private Map<String, Object> headParam;
        private List<Interceptor> interceptors;

        public Builder() {
            isOpenStetho = false;
            domainNames = new ArrayMap<>();
            headParam = new ArrayMap<>();
            interceptors = new ArrayList<>();
        }

        public Builder isOpenStetho(boolean isOpenStetho) {
            this.isOpenStetho = isOpenStetho;
            return this;
        }

        public Builder setDefaultDomainName(String domainName) {
            if (!TextUtils.isEmpty(domainName)) {
                this.domainNames.put("default", domainName);
            }
            return this;
        }

        public Builder setDomainName(Map<String, String> domainNames) {
            if (domainNames != null) {
                this.domainNames.putAll(domainNames);
            }
            return this;
        }

        public Builder setHeadParam(Map<String, Object> headParam) {
            if (headParam != null) {
                this.headParam.putAll(headParam);
            }
            return this;
        }

        public Builder setInterceptors(List<Interceptor> interceptors) {
            if (interceptors != null) {
                this.interceptors.addAll(interceptors);
            }
            return this;
        }

        public AutoNetConfig build() {
            return new AutoNetConfig(this);
        }
    }

    public boolean isOpenStetho() {
        return isOpenStetho;
    }

    public Map<String, String> getDomainNames() {
        return domainNames;
    }

    public Map<String, Object> getHeadParam() {
        return headParam;
    }

    public List<Interceptor> getInterceptors() {
        return interceptors;
    }

}
