package cn.xiaoxige.autonet_api.abstracts;

import org.reactivestreams.Subscriber;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author by zhuxiaoan on 2018/5/21 0021.
 *         Top layer request branch abstraction
 */

public abstract class BaseUsecase {

    public void execute(Subscriber subscriber, FlowableTransformer transformer) {

        Flowable flowable = getFlowable();
        if (flowable == null) {
            return;
        }
        if (transformer != null) {
            //noinspection unchecked
            flowable = flowable.compose(transformer);
        }
        //noinspection unchecked
        flowable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    protected abstract Flowable getFlowable();
}
