package cn.xiaoxige.autonet;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.RxActivity;

import cn.xiaoxige.annotation.AutoNetResponseEntityClass;
import cn.xiaoxige.autonet_api.data.responsentity.AutoResponseEntity;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataCallback;

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
            }
        });
    }



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
    @AutoNetResponseEntityClass(value = AutoResponseEntity.class)
    public class TestCallback2 implements IAutoNetDataCallback<AutoResponseEntity> {

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


}
