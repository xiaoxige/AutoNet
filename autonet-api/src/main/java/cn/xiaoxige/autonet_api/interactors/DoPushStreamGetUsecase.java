package cn.xiaoxige.autonet_api.interactors;

import java.io.File;

import cn.xiaoxige.autonet_api.base.BaseUsecase;
import cn.xiaoxige.autonet_api.data.requestentity.IRequestEntity;
import cn.xiaoxige.autonet_api.repository.AutoNetRepo;
import io.reactivex.Flowable;

/**
 * @author by zhuxiaoan on 2017/12/12 0012.
 */

public class DoPushStreamGetUsecase extends BaseUsecase {
    private AutoNetRepo mRepo;
    private IRequestEntity mRequestEntity;
    private Class mResponseEntityClass;
    private String mMediaType;
    private String mFileKey;
    private File mFile;


    public DoPushStreamGetUsecase(AutoNetRepo repo, IRequestEntity requestEntity, Class responseEntityClass,
                                  String mediaType, String fileKey, File file) {
        this.mRepo = repo;
        this.mRequestEntity = requestEntity;
        this.mResponseEntityClass = responseEntityClass;
        this.mMediaType = mediaType;
        this.mFileKey = fileKey;
        this.mFile = file;
    }

    @Override
    public Flowable getFlowable() {
        return mRepo.doPushStreamGet(mRequestEntity, mResponseEntityClass, mMediaType, mFileKey, mFile);
    }
}
