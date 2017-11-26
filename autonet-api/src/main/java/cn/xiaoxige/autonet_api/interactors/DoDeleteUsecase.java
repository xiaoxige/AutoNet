package cn.xiaoxige.autonet_api.interactors;

import cn.xiaoxige.autonet_api.base.BaseUsecase;
import cn.xiaoxige.autonet_api.data.requestentity.IRequestEntity;
import cn.xiaoxige.autonet_api.repository.AutoNetRepo;
import io.reactivex.Flowable;

/**
 * Created by zhuxiaoan on 2017/11/26.
 */

public class DoDeleteUsecase extends BaseUsecase {


    private AutoNetRepo mRepo;
    private IRequestEntity mEntity;

    public DoDeleteUsecase(AutoNetRepo repo, IRequestEntity entity) {
        this.mRepo = repo;
        this.mEntity = entity;
    }

    @Override
    public Flowable getFlowable() {
        return mRepo.doDelete(mEntity);
    }
}
