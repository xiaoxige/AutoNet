package cn.xiaoxige.autonet;


import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.RxActivity;

import org.reactivestreams.Subscription;

import java.io.File;
import java.util.Map;
import java.util.Random;

import cn.xiaoxige.annotation.AutoNetAnontation;
import cn.xiaoxige.annotation.AutoNetBaseUrlKeyAnontation;
import cn.xiaoxige.annotation.AutoNetPatternAnontation;
import cn.xiaoxige.annotation.AutoNetResponseEntityClass;
import cn.xiaoxige.annotation.AutoNetStrategyAnontation;
import cn.xiaoxige.annotation.AutoNetTypeAnontation;
import cn.xiaoxige.autonet.entity.TestARequest;
import cn.xiaoxige.autonet.entity.TestRequest;
import cn.xiaoxige.autonet.entity.TestResponseEntity;
import cn.xiaoxige.autonet.entity.ZipTestEntity;
import cn.xiaoxige.autonet_api.AutoNet;
import cn.xiaoxige.autonet_api.abstracts.AbsAutoNetCallback;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataBeforeCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetFileCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetLocalOptCallBack;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableSubscriber;
import io.reactivex.functions.BiFunction;

public class MainActivity extends RxActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

}
