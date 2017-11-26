package cn.xiaoxige.autonet;

import android.app.Application;

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

        AutoNetConfig config = new AutoNetConfig.Buidler()
                .setBaseUrl("http://www.baidu.com")
                .build();
        AutoNet.getInstance().init(config).setAutoNetEncryption(new IAutoNetEncryptionCallback() {
            @Override
            public String encryption(String beforeValue) {
                return "";
            }
        });
    }
}
