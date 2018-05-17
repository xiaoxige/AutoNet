package cn.xiaoxige.autonet;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.trello.rxlifecycle2.components.RxActivity;

import cn.xiaoxige.annotation.AutoNetDisposableBaseUrlAnontation;
import cn.xiaoxige.annotation.AutoNetDisposableHeadAnnontation;
import cn.xiaoxige.annotation.AutoNetResponseEntityClass;
import cn.xiaoxige.annotation.AutoNetTypeAnontation;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataCallBack;

public class MainActivity extends RxActivity {

    private TextView tvResult;
    private Button btnGet;
    private Button btnPost;
    private Button btnNormalNet;
    private Button btnImmediateNet;
    private Button btnSendFile;
    private Button btnRecvFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        registerListener();
    }


    private void initView() {
        tvResult = (TextView) findViewById(R.id.tvResult);
        btnGet = (Button) findViewById(R.id.btnGet);
        btnPost = (Button) findViewById(R.id.btnPost);
        btnNormalNet = (Button) findViewById(R.id.btnNormalNet);
        btnImmediateNet = (Button) findViewById(R.id.btnImmediateNet);
        btnSendFile = (Button) findViewById(R.id.btnSendFile);
        btnRecvFile = (Button) findViewById(R.id.btnRecvFile);
    }

    private void registerListener() {
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivityTestCallbackAutoProxy.testLocalLink(MainActivity.this, 1);
            }
        });
    }


    @AutoNetResponseEntityClass(value = Object.class)
    public class TestCallback implements IAutoNetDataCallBack {

        @Override
        public void onSuccess(Object entity) {
            Toast.makeText(MainActivity.this, "测试", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed(Throwable throwable) {

        }

        @Override
        public void onEmpty() {

        }
    }

    @AutoNetResponseEntityClass(value = Object.class)
    @AutoNetDisposableBaseUrlAnontation("http://www.baidu.com")
    @AutoNetDisposableHeadAnnontation({
            "mediaType:application/json",
            "token:aaa"
    })
    public class TestCallback2 implements IAutoNetDataCallBack {
        @Override
        public void onSuccess(Object entity) {

        }

        @Override
        public void onFailed(Throwable throwable) {

        }

        @Override
        public void onEmpty() {

        }
    }

    @AutoNetResponseEntityClass(value = Object.class)
    @AutoNetTypeAnontation(reqType = AutoNetTypeAnontation.Type.STREAM)
    public class TestCallback3 implements IAutoNetDataCallBack {
        @Override
        public void onSuccess(Object entity) {

        }

        @Override
        public void onFailed(Throwable throwable) {

        }

        @Override
        public void onEmpty() {

        }
    }


}
