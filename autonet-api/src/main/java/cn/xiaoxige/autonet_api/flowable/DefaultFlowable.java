package cn.xiaoxige.autonet_api.flowable;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;

/**
 * @author by zhuxiaoan on 2017/11/8 0008.
 *         默认使用的背压方式
 */

public abstract class DefaultFlowable<T> extends Flowable<T> {

    public static <T> Flowable<T> create(FlowableOnSubscribe<T> source) {
        return Flowable.create(source, BackpressureStrategy.LATEST);
    }
}
