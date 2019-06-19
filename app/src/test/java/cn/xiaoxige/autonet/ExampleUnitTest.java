package cn.xiaoxige.autonet;

import android.support.v4.util.ArrayMap;
import android.util.Log;

import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.xiaoxige.autonet.entity.ITestA;
import cn.xiaoxige.autonet.entity.ITestB;
import cn.xiaoxige.autonet.entity.TestEntity;
import cn.xiaoxige.autonet.entity.TestResponseEntity;
import cn.xiaoxige.autonet_api.AutoNet;
import cn.xiaoxige.autonet_api.abstracts.AbsAutoNetCallback;
import cn.xiaoxige.autonet_api.config.AutoNetConfig;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetBodyCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetEncryptionCallback;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetHeadCallBack;
import cn.xiaoxige.autonet_api.util.ClassInstanceofPlusUtil;
import cn.xiaoxige.autonet_api.util.GenericParadigmUtil;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.subscribers.DefaultSubscriber;
import okhttp3.Headers;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {

        Class<?> listClass = List.class;

        List<String> strings = new ArrayList<>();

        Class temp = strings.getClass();

        boolean b = ClassInstanceofPlusUtil.instanceOf(strings, listClass);

        Class testAClass = ITestA.class;
        TestEntity testEntity = new TestEntity();
        boolean bb = ClassInstanceofPlusUtil.instanceOf(testEntity, TestEntity.class);

        while (true) {
            if (temp == null) {
                break;
            }
            Class[] interfaces = temp.getInterfaces();
            for (Class aClass : interfaces) {
                System.out.println(aClass.getName());
                if (aClass.equals(listClass)) {
                    System.out.println("xiaoxige");
                }
            }
            System.out.println(temp.getName());
            temp = temp.getSuperclass();
        }

        Map map = new ArrayMap();
        ((ArrayMap) map).put("one", 1);
        ((ArrayMap) map).put("two", "xiaoxige");
        ((ArrayMap) map).put("three", 3.3f);
        ((ArrayMap) map).put("fore", 3L);

        System.out.println(map.get("one").toString());
        System.out.println(map.get("two").toString());
        System.out.println(map.get("three").toString());
        System.out.println(map.get("fore").toString());
    }

    private class TestCallback extends AbsAutoNetCallback<TestResponseEntity, List<String>> {

    }

    private void initAutoNet() {
        Map<String, Object> heads = new ArrayMap<>();
        heads.put("token", "0");
        heads.put("userId", "A");

        Map<String, String> domainNames = new ArrayMap<>();
        domainNames.put("pppig", "https://www.pangpangpig.com");
        domainNames.put("upFile", "http://testimage.xxxx.com:8080");
        domainNames.put("test", "http://192.168.1.38:8080");

        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
            }
        });

        AutoNetConfig config = new AutoNetConfig.Builder()
                .isOpenStetho(false)
                .setDefaultDomainName("https:www.baidu.com")
                .setHeadParam(heads)
                .setDomainName(domainNames)
                .build();

        AutoNet.getInstance().initAutoNet(null, config).setEncryptionCallback(new IAutoNetEncryptionCallback() {
            @Override
            public String encryption(Long key, String encryptionContent) {
                Log.e("TAG", "加密信息： key = " + key + ", encryptionContent = " + encryptionContent);
                return encryptionContent;
            }
        }).setHeadsCallback(new IAutoNetHeadCallBack() {
            @Override
            public void head(Object flag, Headers headers) {
                Log.e("TAG", "flag = " + flag);
                Log.e("TAG", "头部回调：" + headers);
            }
        }).setBodyCallback(new IAutoNetBodyCallBack() {
            @Override
            public boolean body(Object flag, String body) throws Exception {
                Log.e("TAG", "flag = " + flag);
                Log.e("TAG", "body： " + body);
                return false;
            }


        }).updateOrInsertDomainNames("jsonTestBaseUrl", "http://api.news18a.com");
    }
}