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
    private Class mResponseEntityClass;

    public DoGetUsecase(AutoNetRepo repo, IRequestEntity requestEntity, Class responseEntityClass) {
        this.mRepo = repo;
        this.mEntity = requestEntity;
        this.mResponseEntityClass = responseEntityClass;
    }

    @Override
    public Flowable getFlowable() {
        return mRepo.doGet(mEntity, mResponseEntityClass);
    }
}
