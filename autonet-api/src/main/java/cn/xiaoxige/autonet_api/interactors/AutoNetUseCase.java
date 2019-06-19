package cn.xiaoxige.autonet_api.interactors;

import cn.xiaoxige.autonet_api.abstracts.BaseUseCase;
import cn.xiaoxige.autonet_api.repository.AutoNetRepo1;
import io.reactivex.Flowable;

/**
 * @author by zhuxiaoan on 2018/5/21 0021.
 *         The branch of the basic network request
 */

public class AutoNetUseCase extends BaseUseCase {

    private AutoNetRepo1 mRepo;
    private int netState;

    public AutoNetUseCase(AutoNetRepo1 repo, int netState) {
        super();
        this.mRepo = repo;
        this.netState = netState;
    }

    @Override
    protected Flowable getFlowable() {
        if (this.netState == BaseUseCase.NET_GET) {
            return this.mRepo.doNetGet();
        } else if (this.netState == BaseUseCase.NET_POST) {
            return this.mRepo.doNetPost();
        } else if (this.netState == BaseUseCase.NET_DELETE) {
            return this.mRepo.doDelete();
        } else if (this.netState == BaseUseCase.NET_PUT) {
            return this.mRepo.doPut();
        } else if (this.netState == BaseUseCase.LOCAL) {
            return this.mRepo.doLocal();
        } else {
            throw new IllegalArgumentException("Please refer to the way of your request next year.");
        }
    }
}
