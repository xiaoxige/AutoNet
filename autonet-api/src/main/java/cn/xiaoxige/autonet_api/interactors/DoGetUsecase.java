package cn.xiaoxige.autonet_api.interactors;

import cn.xiaoxige.autonet_api.base.BaseUsecase;
import cn.xiaoxige.autonet_api.data.requestentity.IRequestEntity;
import cn.xiaoxige.autonet_api.repository.AutoNetRepo;
import io.reactivex.Flowable;

/**
 * Created by zhuxiaoan on 2017/11/26.
 */

public class DoGetUsecase extends BaseUsecase {

    private AutoNetRepo mRepo;
    private IRequestEntity mEntity;

    public DoGetUsecase(AutoNetRepo repo, IRequestEntity requestEntity) {
        this.mRepo = repo;
        this.mEntity = requestEntity;
    }

    @Override
    public Flowable getFlowable() {
        return mRepo.doGet(mEntity);
    }
}
