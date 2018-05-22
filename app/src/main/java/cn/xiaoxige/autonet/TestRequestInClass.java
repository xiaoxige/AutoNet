package cn.xiaoxige.autonet;

import android.content.Context;
import android.widget.Toast;

import cn.xiaoxige.annotation.AutoNetPatternAnontation;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataCallBack;

/**
 * @author by zhuxiaoan on 2018/5/22 0022.
 */


@AutoNetPatternAnontation(AutoNetPatternAnontation.NetPattern.POST)
public class TestRequestInClass implements IAutoNetDataCallBack {

    private Context context;

    public TestRequestInClass(Context context) {
        this.context = context;
    }

    @Override
    public void onFailed(Throwable throwable) {
        Toast.makeText(context, "请求出错：" + throwable.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEmpty() {
        Toast.makeText(context, "请求出错", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(Object entity) {
        Toast.makeText(context, "请求成功" + entity.toString(), Toast.LENGTH_SHORT).show();
    }
}
