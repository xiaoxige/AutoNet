package cn.xiaoxige.autonet_api.executor;

import java.io.File;

import cn.xiaoxige.autonet_api.distributor.AutoNetSupportLocalDistributor;
import cn.xiaoxige.autonet_api.distributor.ILocalDistributor;
import cn.xiaoxige.autonet_api.error.CustomError;
import cn.xiaoxige.autonet_api.flowable.DefaultFlowable;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataBeforeCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetFileCallBack;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author xiaoxige
 * @date 2019/6/18 0018 16:20
 * -
 * email: xiaoxigexiaoan@outlook.com
 * desc: Distribution of Upstream Transmitter in AutoNet
 */
public class AutoNetFlowableExecutor<F, T> {

    private ILocalDistributor<T> mExecution;
    private Class mResponseClass;
    private Class mTargetClass;
    private IAutoNetCallBack mCallBack;
    private FlowableTransformer mTransformer;

    AutoNetFlowableExecutor(ILocalDistributor<T> distributor, Class responseClazz, Class targetClazz, IAutoNetCallBack callBack, FlowableTransformer transformer) {
        this.mExecution = distributor;
        this.mResponseClass = responseClazz;
        this.mTargetClass = targetClazz;
        this.mCallBack = callBack;
        this.mTransformer = transformer;
    }

    public F getFlowable(boolean isDefault) {
        return getFlowable(isDefault, false);
    }

    public F getFlowable(boolean isDefault, final boolean isForceLocal) {

        Flowable<Object> flowable = DefaultFlowable.create(new FlowableOnSubscribe<Object>() {
            @Override
            public void subscribe(final FlowableEmitter<Object> emitter) throws Exception {
                T t = isForceLocal ? mExecution.optLocalDistributor() : mExecution.start(new IAutoNetFileCallBack() {
                    @Override
                    public void onProgress(float progress) {
                        if (mCallBack instanceof IAutoNetFileCallBack) {
                            emitter.onNext(progress);
                        }
                    }

                    @Override
                    public void onComplete(File file) {
                        // If the file is downloaded, the callback will not be invoked
                        if (mCallBack instanceof IAutoNetFileCallBack) {
                            emitter.onNext(file);
                        }
                    }
                });

                if (mCallBack instanceof IAutoNetDataBeforeCallBack) {
                    //noinspection unchecked
                    boolean isInterception = ((IAutoNetDataBeforeCallBack) mCallBack).handlerBefore(t, emitter);
                    if (isInterception) {
                        emitter.onComplete();
                        return;
                    }
                }

                if (!mResponseClass.equals(mTargetClass)) {
                    throw new RuntimeException("Entities and target types are inconsistent. Check whether handler Before is rewritten or not handled manually? \n" +
                            "detail response class:" + mResponseClass.getName() + " not is Target class:" + mTargetClass.getName());
                }

                emitter.onNext(t);
                emitter.onComplete();
            }
        });

        // AutoNet defaults to asynchronous requests
        if (isDefault) {
            flowable = flowable.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io());
        }

        if (this.mTransformer != null) {
            //noinspection unchecked
            flowable = flowable.compose(this.mTransformer);
        }

        //noinspection unchecked
        return (F) flowable;

    }

}
