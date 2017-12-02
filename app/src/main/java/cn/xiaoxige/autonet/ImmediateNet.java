package cn.xiaoxige.autonet;

import android.widget.TextView;

import cn.xiaoxige.annotation.AutoNetResponseEntityClass;
import cn.xiaoxige.autonet_api.data.responsentity.AutoResponseEntity;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataCallback;

/**
 * Created by zhuxiaoan on 2017/12/2.
 */


@AutoNetResponseEntityClass(value = AutoResponseEntity.class)
public class ImmediateNet implements IAutoNetDataCallback<AutoResponseEntity> {
    private TextView mTextView;

    public ImmediateNet() {
    }


    @Override
    public void onSuccess(AutoResponseEntity entity) {
        mTextView.setText("返回：" + entity.autoResponseResult + "\n" + "是否转Json对象失败：" + entity.isJsonTransformationError);
    }

    @Override
    public void onEmpty() {
        mTextView.setText("Get请求为空");
    }

    @Override
    public void onError(Throwable throwable) {
        mTextView.setText(throwable.toString());
    }

    public void setmTextView(TextView mTextView) {
        this.mTextView = mTextView;
    }
}
