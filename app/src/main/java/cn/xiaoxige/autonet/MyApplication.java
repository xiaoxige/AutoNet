package cn.xiaoxige.autonet;

import android.app.Application;

import cn.xiaoxige.autonet_api.AutoNet;
import cn.xiaoxige.autonet_api.config.AutoNetConfig;


/**
 * Created by 小稀革 on 2017/11/26.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AutoNetConfig config = new AutoNetConfig.Builder()
                .setDefaultDomainName("https:www.baidu.com")
                .isOpenStetho(true)
                .build();

        AutoNet.getInstance().initAutoNet(config);

    }
}
