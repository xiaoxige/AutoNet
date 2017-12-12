package cn.xiaoxige.autonet_api.interactors;

import java.io.File;

import cn.xiaoxige.autonet_api.base.BaseUsecase;
import cn.xiaoxige.autonet_api.data.requestentity.IRequestEntity;
import cn.xiaoxige.autonet_api.repository.AutoNetRepo;
import io.reactivex.Flowable;

/**
 * @author by zhuxiaoan on 2017/12/12 0012.
 */

public class DoPullStreamGetUsecase extends BaseUsecase {

    private AutoNetRepo mRepo;
    private IRequestEntity mRequestEntity;
    private File mFile;

    public DoPullStreamGetUsecase(AutoNetRepo repo, IRequestEntity requestEntity, File file) {
        this.mRepo = repo;
        this.mRequestEntity = requestEntity;
        this.mFile = file;
    }

    @Override
    public Flowable getFlowable() {
        return mRepo.doPullStreamGet(mRequestEntity, mFile);
    }
}
