package cn.xiaoxige.autonet_api.flowable;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;

/**
 * @author by xiaoxige on 2018/5/20.
 *         AutoNet default upstream transmitter (Rxjava)
 */

public abstract class DefaultFlowable<T> extends Flowable<T> {

    public static <T> Flowable<T> create(FlowableOnSubscribe<T> source) {
        return Flowable.create(source, BackpressureStrategy.LATEST);
    }
}
