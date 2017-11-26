package cn.xiaoxige.autonet_api.base;

import org.reactivestreams.Subscriber;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhuxiaoan on 2017/11/26.
 */

public abstract class BaseUsecase {

    public void execute(Subscriber subscriber, FlowableTransformer transformer) {

        Flowable flowable = getFlowable();
        if (flowable == null) {
            return;
        }
        if (transformer != null) {
            flowable = flowable.compose(transformer);
        }
        flowable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    public abstract Flowable getFlowable();
}
