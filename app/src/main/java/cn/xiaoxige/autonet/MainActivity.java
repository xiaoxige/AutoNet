package cn.xiaoxige.autonet;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.trello.rxlifecycle2.components.RxActivity;

import java.io.File;

import cn.xiaoxige.annotation.AutoNetAnontation;
import cn.xiaoxige.annotation.AutoNetDisposableBaseUrlAnontation;
import cn.xiaoxige.annotation.AutoNetDisposableHeadAnnontation;
import cn.xiaoxige.annotation.AutoNetPatternAnontation;
import cn.xiaoxige.annotation.AutoNetResponseEntityClass;
import cn.xiaoxige.annotation.AutoNetStrategyAnontation;
import cn.xiaoxige.annotation.AutoNetTypeAnontation;
import cn.xiaoxige.autonet_api.AutoNet;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataSuccessCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetFileCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetLocalOptCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetRequest;

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
//                MainActivityTestCallbackAutoProxy.testLocalLink(MainActivity.this, 1);
                String path = getExternalFilesDir(null).toString();
//                MainActivityTestCallback2AutoProxy.pullFile(MainActivity.this,
//                        new TestEntity("fdafdas", 330), path, "xiaoxige.png");

//                AutoNet.getInstance().createNet()
//                        .setBaseUrl("https://www.baidu.com")
//                        .setResponseClazz(String.class)
//                        .start(new IAutoNetDataSuccessCallBack() {
//                            @Override
//                            public void onSuccess(Object entity) {
//                                Log.e("TAG", "" + entity);
//                            }
//                        });
//
//                MainActivityTestCallbackAutoProxy.startNet(MainActivity.this);


                String filePath = path + File.separator + "a.png";
                MainActivityTestCallback3AutoProxy.pushFile(MainActivity.this, new TestEntity("xiaoxige", 123), "upload", filePath);
            }
        });
    }


    @AutoNetResponseEntityClass(value = Object.class)
    @AutoNetStrategyAnontation(AutoNetStrategyAnontation.NetStrategy.LOCAL)
    public class TestCallback implements IAutoNetDataCallBack, IAutoNetLocalOptCallBack {

        @Override
        public void onSuccess(Object entity) {
            Toast.makeText(MainActivity.this, "测试" + (entity == null ? "" : entity.toString()), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed(Throwable throwable) {

        }

        @Override
        public void onEmpty() {

        }

        @Override
        public Object optLocalData(IAutoNetRequest request) {
            Log.e("TAG", "哇哈");
            return "哇哈";
        }
    }

    @AutoNetResponseEntityClass(value = Object.class)
    @AutoNetDisposableBaseUrlAnontation("https://www.pangpangpig.com")
    @AutoNetAnontation("/apk/downLoad/android_4.2.4.apk")
    @AutoNetTypeAnontation(resType = AutoNetTypeAnontation.Type.STREAM)
    @AutoNetDisposableHeadAnnontation({
            "mediaType:application/json",
            "token:aaa"
    })
    @AutoNetPatternAnontation(AutoNetPatternAnontation.NetPattern.GET)
    public class TestCallback2 implements IAutoNetDataCallBack, IAutoNetFileCallBack {
        @Override
        public void onSuccess(Object entity) {
            Log.e("TAG", "成功了" + entity.toString());
        }

        @Override
        public void onFailed(Throwable throwable) {
            Log.e("TAG", "失败了" + throwable);
        }

        @Override
        public void onEmpty() {
            Log.e("TAG", "数据为空了");
        }

        @Override
        public void onPregress(float progress) {
            Log.e("TAG", "" + progress);
        }

        @Override
        public void onComplete(File file) {
            Log.e("TAG", "" + file.toString());
        }
    }

    @AutoNetDisposableBaseUrlAnontation("http://testimage.hxkid.com:4869")
    @AutoNetTypeAnontation(reqType = AutoNetTypeAnontation.Type.STREAM)
    @AutoNetPatternAnontation(AutoNetPatternAnontation.NetPattern.POST)
    public class TestCallback3 implements IAutoNetDataCallBack, IAutoNetFileCallBack {

        @Override
        public void onSuccess(Object entity) {
            Log.e("TAG", "成功了" + entity.toString());
        }

        @Override
        public void onFailed(Throwable throwable) {
            Log.e("TAG", "失败了" + throwable);
        }

        @Override
        public void onEmpty() {
            Log.e("TAG", "数据为空了");
        }

        @Override
        public void onPregress(float progress) {
            Log.e("TAG", "progress = " + progress);
        }

        @Override
        public void onComplete(File file) {
            Log.e("TAG", "file = " + file.toString());
        }
    }


}
