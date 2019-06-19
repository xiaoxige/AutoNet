package cn.xiaoxige.autonet_api.interactors;

import cn.xiaoxige.autonet_api.abstracts.BaseUseCase;
import cn.xiaoxige.autonet_api.repository.AutoNetRepo1;
import io.reactivex.Flowable;

/**
 * @author by zhuxiaoan on 2018/5/21 0021.
 *         The branch of the way to upload a file request
 */

public class AutoNetPushFileUseCase extends BaseUseCase {

    private AutoNetRepo1 mRepo;
    private String mPushFileKey;
    private String mFilePath;

    public AutoNetPushFileUseCase(AutoNetRepo1 repo, String pushFileKey, String filePath) {
        this.mRepo = repo;
        this.mPushFileKey = pushFileKey;
        this.mFilePath = filePath;
    }

    @Override
    protected Flowable getFlowable() {
        return mRepo.pushFile(mPushFileKey, mFilePath);
    }
}