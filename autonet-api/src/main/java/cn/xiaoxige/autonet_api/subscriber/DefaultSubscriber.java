package cn.xiaoxige.autonet_api.subscriber;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import cn.xiaoxige.autonet_api.error.EmptyException;

/**
 * @author by zhuxiaoan on 2017/11/8 0008.
 *         RxJava 下游的自定义
 */

public abstract class DefaultSubscriber<T> implements Subscriber<T> {
    @Override
    public void onSubscribe(Subscription s) {
        s.request(Integer.MAX_VALUE);
    }

    @Deprecated
    @Override
    public void onNext(T t) {
        DefaultOnNext(t);
    }

    @Deprecated
    @Override
    public void onError(Throwable t) {
        if (t instanceof EmptyException) {
            DefaultOnEmpty();
        } else {
            DefaultOnError(t);
        }
    }


    @Deprecated
    @Override
    public void onComplete() {
        DefaultOnComplete();
    }

    public void DefaultOnNext(T data) {
    }

    public void DefaultOnError(Throwable throwable) {
    }

    public void DefaultOnEmpty() {
    }

    public void DefaultOnComplete() {
    }

}
