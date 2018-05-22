package cn.xiaoxige.autonet;


import android.os.Bundle;
import android.support.annotation.MainThread;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.RxActivity;

import java.io.File;
import java.util.Random;

import cn.xiaoxige.annotation.AutoNetAnontation;
import cn.xiaoxige.annotation.AutoNetBaseUrlKeyAnontation;
import cn.xiaoxige.annotation.AutoNetDisposableBaseUrlAnontation;
import cn.xiaoxige.annotation.AutoNetDisposableHeadAnnontation;
import cn.xiaoxige.annotation.AutoNetPatternAnontation;
import cn.xiaoxige.annotation.AutoNetResponseEntityClass;
import cn.xiaoxige.annotation.AutoNetStrategyAnontation;
import cn.xiaoxige.annotation.AutoNetTypeAnontation;
import cn.xiaoxige.autonet.entity.TestRequest;
import cn.xiaoxige.autonet.entity.TestResponseEntity;
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
    private Button btnLocalNet;
    private Button btnNetLocal;
    private Button btnRequestInClass;
    private Button btnChainRequest;
    private Button btnSendFile;
    private Button btnRecvFile;
    private Button btnUpdateHeadToken;
    private Button btnRemoveHeadToken;

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
        btnRequestInClass = (Button) findViewById(R.id.btnRequestInClass);
        btnChainRequest = (Button) findViewById(R.id.btnChainRequest);
        btnSendFile = (Button) findViewById(R.id.btnSendFile);
        btnRecvFile = (Button) findViewById(R.id.btnRecvFile);
        btnUpdateHeadToken = (Button) findViewById(R.id.btnUpdateHeadToken);
        btnRemoveHeadToken = (Button) findViewById(R.id.btnRemoveHeadToken);
        btnLocalNet = (Button) findViewById(R.id.btnLocalNet);
        btnNetLocal = (Button) findViewById(R.id.btnNetLocal);
    }

    private void registerListener() {

        // get请求
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 无参数请求
//                MainActivitydoGetAutoProxy.startNet(MainActivity.this);

                // 带有参数的请求
//                MainActivitydoGetAutoProxy.startNet(MainActivity.this, new TestRequest("ina_app", "other", "guidepage"));

                // 绑定生命周期的请求
                MainActivitydoGetAutoProxy.startNet(MainActivity.this, new TestRequest("ina_app", "other", "guidepage"), bindUntilEvent(ActivityEvent.DESTROY));


            }
        });

        // post请求
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 无参数请求
//                MainActivitydoPostAutoProxy.startNet(MainActivity.this);

                // 带有参数的请求
//                MainActivitydoPostAutoProxy.startNet(MainActivity.this, new TestRequest());

                // 绑定生命周期的请求
                MainActivitydoPostAutoProxy.startNet(MainActivity.this, new TestRequest(), bindUntilEvent(ActivityEvent.DESTROY));
            }
        });

        // 先本地后网络
        btnLocalNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivitydoLocalNetAutoProxy.startNet(MainActivity.this);
            }
        });

        btnNetLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivitydoNetLocalAutoProxy.startNet(MainActivity.this);
            }
        });

        // 直接在类上加注解请求
        btnRequestInClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestRequestInClass testRequestInClass = new TestRequestInClass(MainActivity.this);
                TestRequestInClassAutoProxy.startNet(testRequestInClass);
            }
        });

        // 接受文件
        btnRecvFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = getExternalFilesDir(null).toString();
                MainActivityPullFileAutoProxy.pullFile(MainActivity.this, path, "pppig.apk");
            }
        });

        // 发送文件
        btnSendFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = getExternalFilesDir(null).toString();
                MainActivityPushFileAutoProxy.pushFile(MainActivity.this, "upload", path + File.separator + "a.png");
            }
        });
        // 链式调用
        btnChainRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoNet.getInstance().createNet()
                        .setDomainNameKey("pppig")
                        .start(new IAutoNetDataSuccessCallBack() {
                            @Override
                            public void onSuccess(Object entity) {
                                tvResult.setText(entity.toString());
                            }
                        });
            }
        });
        // 修改头信息token信息
        btnUpdateHeadToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = new Random().nextInt(100);
                AutoNet.getInstance().updateOrInsertHead("token", "" + i);
            }
        });

        // 删除头信息token信息
        btnRemoveHeadToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoNet.getInstance().removeHead("token");
            }
        });
    }

    @AutoNetResponseEntityClass(TestResponseEntity.class)
    @AutoNetPatternAnontation(AutoNetPatternAnontation.NetPattern.GET)
    @AutoNetAnontation("/init.php")
    @AutoNetBaseUrlKeyAnontation("jsonTestBaseUrl")
    public class doGet implements IAutoNetDataCallBack<TestResponseEntity> {
        StringBuffer buffer = new StringBuffer();

        @Override
        public void onFailed(Throwable throwable) {
            buffer.append("请求失败了：" + throwable.toString());
            tvResult.setText(buffer.toString());
        }

        @Override
        public void onEmpty() {
            buffer.append("请求失败了");
            tvResult.setText(buffer.toString());
        }

        @Override
        public void onSuccess(TestResponseEntity entity) {
            buffer.append("json数据请求成功\n" + entity.toString());
            tvResult.setText(buffer.toString());
        }
    }

    @AutoNetPatternAnontation(AutoNetPatternAnontation.NetPattern.POST)
    public class doPost implements IAutoNetDataCallBack {
        StringBuffer buffer = new StringBuffer();

        @Override
        public void onFailed(Throwable throwable) {
            buffer.append("请求失败了：" + throwable.toString());
            tvResult.setText(buffer.toString());
        }

        @Override
        public void onEmpty() {
            buffer.append("请求失败了");
            tvResult.setText(buffer.toString());
        }

        @Override
        public void onSuccess(Object entity) {
            buffer.append("请求成功了\n" + entity.toString());
            tvResult.setText(buffer.toString());
        }
    }

    @AutoNetStrategyAnontation(AutoNetStrategyAnontation.NetStrategy.LOCAL_NET)
    public class doLocalNet implements IAutoNetDataCallBack, IAutoNetLocalOptCallBack {

        StringBuffer buffer = new StringBuffer();

        @Override
        public void onFailed(Throwable throwable) {
            buffer.append("请求失败了：" + throwable.toString());
            tvResult.setText(buffer.toString());
        }

        @Override
        public void onEmpty() {
            buffer.append("请求失败了");
            tvResult.setText(buffer.toString());
        }

        @Override
        public void onSuccess(Object entity) {
            buffer.append("成功了\n" + entity.toString());
            tvResult.setText(buffer.toString());
        }

        @Override
        public Object optLocalData(IAutoNetRequest request) {
            // 本地数据交给用户处理
            return "\n这是本地数据,hahahahaha\n";
        }
    }

    @AutoNetStrategyAnontation(AutoNetStrategyAnontation.NetStrategy.NET_LOCAL)
    public class doNetLocal implements IAutoNetDataCallBack, IAutoNetLocalOptCallBack {

        StringBuffer buffer = new StringBuffer();

        @Override
        public void onFailed(Throwable throwable) {
            buffer.append("请求失败了：" + throwable.toString());
            tvResult.setText(buffer.toString());
        }

        @Override
        public void onEmpty() {
            buffer.append("请求失败了");
            tvResult.setText(buffer.toString());
        }

        @Override
        public void onSuccess(Object entity) {
            buffer.append("成功了\n" + entity.toString());
            tvResult.setText(buffer.toString());
        }

        @Override
        public Object optLocalData(IAutoNetRequest request) {
            // 本地数据交给用户处理
            return "\n这是本地数据,hahahahaha\n";
        }
    }

    @AutoNetBaseUrlKeyAnontation("upFile")
    @AutoNetTypeAnontation(reqType = AutoNetTypeAnontation.Type.STREAM)
    @AutoNetPatternAnontation(AutoNetPatternAnontation.NetPattern.POST)
    public class PushFile implements IAutoNetDataCallBack, IAutoNetFileCallBack {

        StringBuffer buffer = new StringBuffer();

        @Override
        public void onFailed(Throwable throwable) {
            buffer.append("发送文件出错:\n" + throwable.toString() + "\n");
            tvResult.setText(buffer.toString());
        }

        @Override
        public void onEmpty() {
            buffer.append("发送文件出错");
            tvResult.setText(buffer.toString());
        }

        @Override
        public void onSuccess(Object entity) {
            buffer.append("发送文件成功， 服务器并返回:\n" + entity.toString() + "\n");
            tvResult.setText(buffer.toString());
        }

        @Override
        public void onPregress(float progress) {
            buffer.append("发送文件进度：" + progress + "\n");
            tvResult.setText(buffer.toString());
        }

        @Override
        public void onComplete(File file) {
            buffer.append("文件发送成功， 文件：" + file.toString() + "\n");
            tvResult.setText(buffer.toString());
        }
    }

    @AutoNetBaseUrlKeyAnontation("pppig")
    @AutoNetTypeAnontation(resType = AutoNetTypeAnontation.Type.STREAM)
    @AutoNetAnontation("/apk/downLoad/android_4.2.4.apk")
    public class PullFile implements IAutoNetDataCallBack, IAutoNetFileCallBack {
        StringBuffer buffer = new StringBuffer();

        @Override
        public void onFailed(Throwable throwable) {
            buffer.append("接收文件出错:\n" + throwable.toString() + "\n");
            tvResult.setText(buffer.toString());
        }

        @Override
        public void onEmpty() {
            buffer.append("接收文件出错");
            tvResult.setText(buffer.toString());
        }

        @Override
        public void onSuccess(Object entity) {
            // 不会被执行
        }

        @Override
        public void onPregress(float progress) {
            buffer.append("接收进度：" + progress + "\n");
            tvResult.setText(buffer.toString());
        }

        @Override
        public void onComplete(File file) {
            buffer.append("文件接收成功， 文件：" + file.toString() + "\n");
            tvResult.setText(buffer.toString());
        }
    }

}
