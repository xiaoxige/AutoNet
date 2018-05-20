package cn.xiaoxige.autonet_api.subscriber;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import cn.xiaoxige.autonet_api.error.EmptyError;

/**
 * @author by xiaoxige on 2018/5/20.
 *         AutoNet default downstream receiver (Rxjava)
 */

public abstract class DefaultSubscriber<T> implements Subscriber<T> {

    @Override
    public void onSubscribe(Subscription s) {
        s.request(Integer.MAX_VALUE);
    }

    @Deprecated
    @Override
    public void onNext(T t) {
        defaultOnNext(t);
    }

    @Deprecated
    @Override
    public void onError(Throwable t) {
        if (t instanceof EmptyError) {
            defaultOnEmptyError();
        } else {
            defaultOnErrorWithNotEmpty(t);
        }
    }

    @Deprecated
    @Override
    public void onComplete() {
        defaultOnComplete();
    }

    protected void defaultOnNext(T entity) {
    }

    protected void defaultOnEmptyError() {
    }

    protected void defaultOnErrorWithNotEmpty(Throwable throwable) {
    }

    protected void defaultOnComplete() {
    }

}
