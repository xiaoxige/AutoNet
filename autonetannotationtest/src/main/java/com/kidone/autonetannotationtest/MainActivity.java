package com.kidone.autonetannotationtest;

import android.app.Activity;
import android.os.Bundle;

import cn.xiaoxige.annotation.AutoNetAnontation;
import cn.xiaoxige.annotation.AutoNetBaseUrlKeyAnontation;
import cn.xiaoxige.annotation.AutoNetDisposableBaseUrlAnontation;
import cn.xiaoxige.annotation.AutoNetDisposableHeadAnnontation;
import cn.xiaoxige.annotation.AutoNetEncryptionAnontation;
import cn.xiaoxige.annotation.AutoNetMediaTypeAnontation;
import cn.xiaoxige.annotation.AutoNetPatternAnontation;
import cn.xiaoxige.annotation.AutoNetResponseEntityClass;
import cn.xiaoxige.annotation.AutoNetStrategyAnontation;
import cn.xiaoxige.annotation.AutoNetTargetEntityClass;
import cn.xiaoxige.annotation.AutoNetTypeAnontation;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetCallBack;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @AutoNetAnontation(value = "/getUserInfo", flag = 1000)
    @AutoNetBaseUrlKeyAnontation
    @AutoNetDisposableBaseUrlAnontation(value = "https://www.baidu.com")
    @AutoNetDisposableHeadAnnontation(value = {
            "uid:0",
            "token:A"
    })
    @AutoNetEncryptionAnontation(key = 666, value = true)
    @AutoNetMediaTypeAnontation(value = "application/json")
    @AutoNetPatternAnontation(value = AutoNetPatternAnontation.NetPattern.POST)
    @AutoNetResponseEntityClass(TestResponseEntity.class)
    @AutoNetStrategyAnontation(value = AutoNetStrategyAnontation.NetStrategy.LOCAL_NET)
//    @AutoNetTargetEntityClass(value = TestTargetEntity.class)
    @AutoNetTypeAnontation(reqType = AutoNetTypeAnontation.Type.FORM, resType = AutoNetTypeAnontation.Type.JSON)
    public class AutoNetCallback implements IAutoNetCallBack {

    }

}
