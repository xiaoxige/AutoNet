package cn.xiaoxige.autonet;

import android.os.Bundle;

import com.google.gson.Gson;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.RxActivity;

import cn.xiaoxige.annotation.AutoNetAnontation;
import cn.xiaoxige.annotation.AutoNetBaseUrlKeyAnontation;
import cn.xiaoxige.annotation.AutoNetEncryptionAnontation;
import cn.xiaoxige.annotation.AutoNetPatternAnontation;
import cn.xiaoxige.annotation.AutoNetResponseEntityClass;
import cn.xiaoxige.autonet.MainActivityTestCallbackAutoProxy;
import cn.xiaoxige.autonet.model.ITestEntity;
import cn.xiaoxige.autonet.model.TestEntity;
import cn.xiaoxige.autonet.model.TestRequestEntity;
import cn.xiaoxige.autonet.model.TestResponseEntity;
import cn.xiaoxige.autonet_api.data.responsentity.IResponseEntity;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataCallback;

public class MainActivity extends RxActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivityTestCallbackAutoProxy.startSoftNet(new TestRequestEntity("xiaoxige", 22), bindUntilEvent(ActivityEvent.DESTROY), new TestCallback());
    }

    @AutoNetResponseEntityClass(value = TestResponseEntity.class)
    @AutoNetEncryptionAnontation(value = false)
    @AutoNetBaseUrlKeyAnontation(value = "default")
    @AutoNetPatternAnontation(value = AutoNetPatternAnontation.NetPattern.GET)
    @AutoNetAnontation(url = "/name")
    public class TestCallback implements IAutoNetDataCallback<TestResponseEntity> {

        @Override
        public void onSuccess(TestResponseEntity entity) {

        }

        @Override
        public void onEmpty() {

        }

        @Override
        public void onError(Throwable throwable) {

        }
    }

    @AutoNetEncryptionAnontation(value = false)
    @AutoNetBaseUrlKeyAnontation(value = "default")
    @AutoNetPatternAnontation(value = AutoNetPatternAnontation.NetPattern.POST)
    @AutoNetAnontation(url = "/")
    public class TwoCallback implements IAutoNetDataCallback {

        @Override
        public void onSuccess(IResponseEntity entity) {

        }

        @Override
        public void onEmpty() {

        }

        @Override
        public void onError(Throwable throwable) {

        }
    }
}
