package cn.xiaoxige.autonet;

import android.widget.TextView;

import cn.xiaoxige.annotation.AutoNetResponseEntityClass;
import cn.xiaoxige.autonet_api.data.responsentity.AutoResponseEntity;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataCallback;

/**
 * Created by zhuxiaoan on 2017/12/2.
 */

public class NormalClassNet {

    private TextView mTextView;

    public NormalClassNet() {
    }

    public NormalClassNet(TextView textView) {
        mTextView = textView;
    }

    @AutoNetResponseEntityClass(value = AutoResponseEntity.class)
    public class TestCallback implements IAutoNetDataCallback<AutoResponseEntity> {

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
    }

}
