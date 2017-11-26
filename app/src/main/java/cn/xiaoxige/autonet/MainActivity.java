package cn.xiaoxige.autonet;

import android.app.Activity;
import android.os.Bundle;

import cn.xiaoxige.annotation.AutoNetAnontation;
import cn.xiaoxige.annotation.AutoNetBaseUrlKeyAnontation;
import cn.xiaoxige.annotation.AutoNetEncryptionAnontation;
import cn.xiaoxige.annotation.AutoNetPatternAnontation;
import cn.xiaoxige.autonet.model.TestResponseEntity;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataCallback;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @AutoNetEncryptionAnontation(value = false)
    @AutoNetBaseUrlKeyAnontation(value = "default")
    @AutoNetPatternAnontation(value = AutoNetPatternAnontation.NetPattern.POST)
    @AutoNetAnontation(url = "/")
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
}
