package cn.xiaoxige.autonet;

import android.app.Application;

import java.util.HashMap;
import java.util.Map;

import cn.xiaoxige.autonet_api.AutoNet;
import cn.xiaoxige.autonet_api.config.AutoNetConfig;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetEncryptionCallback;

/**
 * Created by 小稀革 on 2017/11/26.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Map mapBaseUrl = new HashMap();
        mapBaseUrl.put("jsonTestBaseUrl", "http://api.news18a.com");
        AutoNetConfig config = new AutoNetConfig.Buidler()
                .setBaseUrl("http://www.baidu.com")
                .addBaseUrl(mapBaseUrl)
                .build();
        AutoNet.getInstance().init(this, config).setAutoNetEncryption(new IAutoNetEncryptionCallback() {
            @Override
            public String encryption(long encryptionKey, String beforeValue) {
                return beforeValue;
            }
        });
    }
}
