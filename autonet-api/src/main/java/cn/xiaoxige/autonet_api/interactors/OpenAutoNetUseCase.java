package cn.xiaoxige.autonet_api.interactors;

import cn.xiaoxige.autonet_api.abstracts.BaseUseCase;
import cn.xiaoxige.autonet_api.repository.AutoNetRepo1;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author by zhuxiaoan on 2018/5/21 0021.
 * The branch of the basic network request
 */

public class OpenAutoNetUseCase extends BaseUseCase {

    private AutoNetRepo1 mRepo;
    private int netState;
    private FlowableTransformer mTransformer;

    public OpenAutoNetUseCase(AutoNetRepo1 repo, int netState, FlowableTransformer transformer) {
        super();
        this.mRepo = repo;
        this.netState = netState;
        this.mTransformer = transformer;
    }

    @Override
    public Flowable getFlowable() {
        Flowable flowable;
        if (this.netState == BaseUseCase.NET_GET) {
            flowable = this.mRepo.doNetGet();
        } else if (this.netState == BaseUseCase.NET_POST) {
            flowable = this.mRepo.doNetPost();
        } else if (this.netState == BaseUseCase.NET_DELETE) {
            flowable = this.mRepo.doDelete();
        } else if (this.netState == BaseUseCase.NET_PUT) {
            flowable = this.mRepo.doPut();
        } else if (this.netState == BaseUseCase.LOCAL) {
            flowable = this.mRepo.doLocal();
        } else {
            throw new IllegalArgumentException("Please refer to the way of your request next year.");
        }
        if (flowable == null) {
            throw new IllegalArgumentException("Please make sure that the upstream cannot be empty.");
        }
        if (mTransformer != null) {
            //noinspection unchecked
            flowable = flowable.compose(mTransformer);
        }

        //noinspection unchecked
        return flowable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }
}
