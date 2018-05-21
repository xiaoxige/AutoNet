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

public abstract class BaseUseCase {

    public static final int NET_GET = 0x01;
    public static final int NET_POST = 0x02;
    public static final int NET_PUT = 0x03;
    public static final int NET_DELETE = 0x04;
    public static final int LOCAL = 0x05;

    protected int netState = NET_GET;

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
