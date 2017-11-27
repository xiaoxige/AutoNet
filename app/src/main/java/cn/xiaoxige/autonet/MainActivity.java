package cn.xiaoxige.autonet;

import android.os.Bundle;
import android.util.Log;

import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.RxActivity;

import cn.xiaoxige.annotation.AutoNetAnontation;
import cn.xiaoxige.annotation.AutoNetBaseUrlKeyAnontation;
import cn.xiaoxige.annotation.AutoNetEncryptionAnontation;
import cn.xiaoxige.annotation.AutoNetPatternAnontation;
import cn.xiaoxige.annotation.AutoNetResponseEntityClass;
import cn.xiaoxige.autonet.MainActivityTwoCallbackAutoProxy;
import cn.xiaoxige.autonet.model.JsonTestRequestEntity;
import cn.xiaoxige.autonet.model.JsonTestResponseEntity;
import cn.xiaoxige.autonet.model.TestRequestEntity;
import cn.xiaoxige.autonet.model.TestResponseEntity;
import cn.xiaoxige.autonet_api.data.requestentity.IRequestEntity;
import cn.xiaoxige.autonet_api.data.responsentity.AutoResponseEntity;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataCallback;

public class MainActivity extends RxActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cn.xiaoxige.autonet.MainActivityTestCallbackAutoProxy.startSoftNet(new TestRequestEntity("xiaoxige", 22), bindUntilEvent(ActivityEvent.DESTROY), new TestCallback());

        JsonTestRequestEntity entity = new JsonTestRequestEntity();
        entity.setA("guidepage");
        entity.setM("ina_app");
        entity.setC("other");
        MainActivityTwoCallbackAutoProxy.startSoftNet(entity, bindUntilEvent(ActivityEvent.DESTROY), new TwoCallback());

    }

    @AutoNetResponseEntityClass(value = TestResponseEntity.class)
    @AutoNetEncryptionAnontation(value = false, key = 1)
    @AutoNetBaseUrlKeyAnontation(value = "default")
    @AutoNetPatternAnontation(value = AutoNetPatternAnontation.NetPattern.GET)
    @AutoNetAnontation(url = "/name")
    public class TestCallback implements IAutoNetDataCallback<TestResponseEntity> {

        @Override
        public void onSuccess(TestResponseEntity entity) {
            Log.e("TAG", "成功 " + entity.isJsonTransformationError + ",\n" + entity.autoResponseResult);
        }

        @Override
        public void onEmpty() {

        }

        @Override
        public void onError(Throwable throwable) {

        }
    }

    @AutoNetResponseEntityClass(value = JsonTestResponseEntity.class)
    @AutoNetEncryptionAnontation(value = false)
    @AutoNetBaseUrlKeyAnontation(value = "jsonTestBaseUrl")
    @AutoNetPatternAnontation(value = AutoNetPatternAnontation.NetPattern.GET)
    @AutoNetAnontation(url = "/init.php")
    public class TwoCallback implements IAutoNetDataCallback<JsonTestResponseEntity> {

        @Override
        public void onSuccess(JsonTestResponseEntity entity) {

            Log.e("TAG", "entity = " + entity.toString() + entity.autoResponseResult + ", " + entity.isJsonTransformationError);

        }

        @Override
        public void onEmpty() {
            Log.e("TAG", "空数据");
        }

        @Override
        public void onError(Throwable throwable) {
            Log.e("TAG", "错误");
        }
    }
}
