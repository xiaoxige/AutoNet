package cn.xiaoxige.autonet_api.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhuxiaoan on 2017/11/26.
 * AutoNetConfigs
 */

public class AutoNetConfig {

    private Map<String, String> mBaseUrl = null;
    private Map<String, String> mHeader = null;
    private Map<String, String> mGetDelParams = null;

    private AutoNetConfig() {
    }

    private AutoNetConfig(Buidler buidler) {
        this.mBaseUrl = buidler.mBaseUrl;
        this.mHeader = buidler.mHeader;
        this.mGetDelParams = buidler.mGetDelParams;
    }

    public Map<String, String> getBaseUrl() {
        return mBaseUrl;
    }

    public Map<String, String> getHeader() {
        return mHeader;
    }

    public Map<String, String> getGetDelParams() {
        return mGetDelParams;
    }

    public static class Buidler {

        private Map<String, String> mBaseUrl = null;
        private Map<String, String> mHeader = null;
        private Map<String, String> mGetDelParams = null;


        public Buidler() {
            mBaseUrl = new HashMap<>();
            mHeader = new HashMap<>();
            mGetDelParams = new HashMap<>();
        }


        public Buidler setBaseUrl(String baseUrl) {
            Map<String, String> mapBaseUrl = new HashMap<>();
            mapBaseUrl.put("default", baseUrl);
            setBaseUrl(mapBaseUrl, true);
            return this;
        }

        public Buidler setBaseUrl(Map baseUrl) {
            setBaseUrl(baseUrl, true);
            return this;
        }

        public Buidler addBaseUrl(Map baseUrl) {
            setBaseUrl(baseUrl, false);
            return this;
        }

        public Buidler setHeader(Map header) {
            setHeader(header, true);
            return this;
        }

        public Buidler addHeader(Map header) {
            setHeader(header, false);
            return this;
        }

        public Buidler setGetDelParams(Map getParams) {
            setGetDelParam(getParams, true);
            return this;
        }

        public Buidler addGetDelParams(Map getParams) {
            setGetDelParam(getParams, false);
            return this;
        }

        public AutoNetConfig build() {
            return new AutoNetConfig(this);
        }

        private void setBaseUrl(Map baseUrl, boolean isClear) {
            if (baseUrl == null) {
                return;
            }
            if (isClear) {
                mBaseUrl.clear();
            }
            mBaseUrl.putAll(baseUrl);
        }

        private void setHeader(Map header, boolean isClear) {
            if (header == null) {
                return;
            }
            if (isClear) {
                mHeader.clear();
            }
            mHeader.putAll(header);
        }

        private void setGetDelParam(Map getParam, boolean isClear) {
            if (getParam == null) {
                return;
            }
            if (isClear) {
                mGetDelParams.clear();
            }
            mGetDelParams.putAll(getParam);
        }
    }

}
