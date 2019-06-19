package cn.xiaoxige.autonet_api.distributor;

import java.io.File;

import cn.xiaoxige.annotation.AutoNetTypeAnontation;
import cn.xiaoxige.autonet_api.flowable.DefaultFlowable;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetCallBack;
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
public class AutoNetFlowableDistributor<F, T> implements IDistributorExecution<T> {

    private IDistributorExecution<T> mExecution;
    private IAutoNetCallBack mCallBack;
    private FlowableTransformer mTransformer;

    public AutoNetFlowableDistributor(IDistributorExecution<T> distributor, IAutoNetCallBack callBack, FlowableTransformer transformer) {
        this.mExecution = distributor;
        this.mCallBack = callBack;
        this.mTransformer = transformer;
    }

    public F getFlowable(boolean isDefault) {

        Flowable<Object> flowable = DefaultFlowable.create(new FlowableOnSubscribe<Object>() {
            @Override
            public void subscribe(final FlowableEmitter<Object> emitter) throws Exception {
                T t = start(new IAutoNetFileCallBack() {
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

    @Override
    public T start(IAutoNetFileCallBack callBack) throws Exception {
        return this.mExecution.start(callBack);
    }

    @Override
    public T startNonFileRequest() throws Exception {
        return this.mExecution.startNonFileRequest();
    }

    @Override
    public T startFileRequest(IAutoNetFileCallBack callBack) throws Exception {
        return this.mExecution.startFileRequest(callBack);
    }

}
