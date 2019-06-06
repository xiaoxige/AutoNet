package cn.xiaoxige.autonet;


import android.os.Bundle;

import com.trello.rxlifecycle2.components.RxActivity;

import java.util.List;

import cn.xiaoxige.autonet.entity.TestResponseEntity;
import cn.xiaoxige.autonet_api.AutoNet;
import cn.xiaoxige.autonet_api.abstracts.AbsAutoNetCallback;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataBeforeCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataCallBack;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;

public class MainActivity extends RxActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            TestResponseEntity testResponseEntity = AutoNet.getInstance().createNet().synchronizationNet(TestResponseEntity.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Flowable flowable = AutoNet.getInstance().createNet().getFlowable(TestResponseEntity.class);

        AutoNet.getInstance().createNet().start(new TestCallback1());

    }

    private class TestCallback extends AbsAutoNetCallback<TestResponseEntity, List<String>> {

    }

    private class TestCallback1 implements IAutoNetDataCallBack<TestResponseEntity>, IAutoNetDataBeforeCallBack<String, List<String>> {

        @Override
        public void onFailed(Throwable throwable) {

        }

        @Override
        public void onEmpty() {

        }

        @Override
        public void onSuccess(TestResponseEntity entity) {

        }


        @Override
        public boolean handlerBefore(String s, FlowableEmitter<List<String>> emitter) {
            return false;
        }
    }

}
