package cn.xiaoxige.autonet;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.RxActivity;

import cn.xiaoxige.annotation.AutoNetAnontation;
import cn.xiaoxige.annotation.AutoNetBaseUrlKeyAnontation;
import cn.xiaoxige.annotation.AutoNetEncryptionAnontation;
import cn.xiaoxige.annotation.AutoNetPatternAnontation;
import cn.xiaoxige.annotation.AutoNetResponseEntityClass;
import cn.xiaoxige.autonet.model.JsonTestRequestEntity;
import cn.xiaoxige.autonet.model.JsonTestResponseEntity;
import cn.xiaoxige.autonet_api.data.responsentity.AutoResponseEntity;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataCallback;

public class MainActivity extends RxActivity {

    private TextView tvResult;
    private Button btnGet;
    private Button btnPost;
    private Button btnNormalNet;
    private Button btnImmediateNet;

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
        btnNormalNet = (Button)findViewById(R.id.btnNormalNet);
        btnImmediateNet = (Button)findViewById(R.id.btnImmediateNet);
    }

    private void registerListener() {
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvResult.setText("正在请求");
                cn.xiaoxige.autonet.MainActivityTestCallbackAutoProxy.startSoftNet(MainActivity.this, bindUntilEvent(ActivityEvent.DESTROY));
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvResult.setText("正在请求");
                JsonTestRequestEntity entity = new JsonTestRequestEntity();
                entity.setA("guidepage");
                entity.setM("ina_app");
                entity.setC("other");
                cn.xiaoxige.autonet.MainActivityTwoCallbackAutoProxy.startSoftNet(MainActivity.this, entity, bindUntilEvent(ActivityEvent.DESTROY));
            }
        });

        btnNormalNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvResult.setText("正在请求");
                NormalClassNet normalClassNet = new NormalClassNet(tvResult);
                cn.xiaoxige.autonet.NormalClassNetTestCallbackAutoProxy.startUnSoftNet(normalClassNet);
            }
        });

        btnImmediateNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvResult.setText("正在请求");
                ImmediateNet immediateNet = new ImmediateNet();
                immediateNet.setmTextView(tvResult);
                cn.xiaoxige.autonet.autonetImmediateNetAutoProxy.startUnSoftNet(immediateNet);
            }
        });
    }


    /**
     * 场景： 不加密， get请求， 默认的baseUrl， url为"/", 不加密
     */
    @AutoNetResponseEntityClass(value = AutoResponseEntity.class)
    public class TestCallback implements IAutoNetDataCallback<AutoResponseEntity> {

        @Override
        public void onSuccess(AutoResponseEntity entity) {
            Toast.makeText(MainActivity.this, "Get成功", Toast.LENGTH_SHORT).show();
            tvResult.setText("返回：" + entity.autoResponseResult + "\n" + "是否转Json对象失败：" + entity.isJsonTransformationError);
        }

        @Override
        public void onEmpty() {
            Toast.makeText(MainActivity.this, "Get数据为空", Toast.LENGTH_SHORT).show();
            tvResult.setText("Get请求为空");
        }

        @Override
        public void onError(Throwable throwable) {
            Toast.makeText(MainActivity.this, "Get数据出错...", Toast.LENGTH_SHORT).show();
            tvResult.setText(throwable.toString());
        }
    }

    /**
     * 场景： 加密， 使用key为jsonTestBaseUrl的BaseUrl, Get请求， url = "/init.php"
     * 参数在调用发送请求的时候
     */
    @AutoNetResponseEntityClass(value = JsonTestResponseEntity.class)
    @AutoNetEncryptionAnontation(value = true, key = 1)
    @AutoNetBaseUrlKeyAnontation(value = "jsonTestBaseUrl")
    @AutoNetPatternAnontation(value = AutoNetPatternAnontation.NetPattern.GET)
    @AutoNetAnontation(url = "/init.php")
    public class TwoCallback implements IAutoNetDataCallback<JsonTestResponseEntity> {

        @Override
        public void onSuccess(JsonTestResponseEntity entity) {
            Toast.makeText(MainActivity.this, "Post成功", Toast.LENGTH_SHORT).show();
            tvResult.setText("返回：" + entity.autoResponseResult + "\n" + "是否转Json对象失败："
                    + entity.isJsonTransformationError + "\n\n"
                    + "json： " + entity.toString());
        }

        @Override
        public void onEmpty() {
            Toast.makeText(MainActivity.this, "Post数据为空", Toast.LENGTH_SHORT).show();
            tvResult.setText("Post数据为空");
        }

        @Override
        public void onError(Throwable throwable) {
            Toast.makeText(MainActivity.this, "Post数据出错", Toast.LENGTH_SHORT).show();
            tvResult.setText(throwable.toString());
        }
    }
}
