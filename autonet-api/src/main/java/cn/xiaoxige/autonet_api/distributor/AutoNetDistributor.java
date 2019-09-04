package cn.xiaoxige.autonet_api.distributor;

import java.io.File;

import cn.xiaoxige.annotation.AutoNetPatternAnontation;
import cn.xiaoxige.annotation.AutoNetTypeAnontation;
import cn.xiaoxige.autonet_api.constant.AutoNetConstant;
import cn.xiaoxige.autonet_api.error.NoNetError;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetFileCallBack;
import cn.xiaoxige.autonet_api.repository.AutoNetRepo;
import cn.xiaoxige.autonet_api.util.AutoNetTypeUtil;
import cn.xiaoxige.autonet_api.util.NetUtil;

/**
 * @author xiaoxige
 * @date 2019/6/17 0017 17:15
 * -
 * email: xiaoxigexiaoan@outlook.com
 * desc: AutoNet Distributor, which distributes requests
 */
public class AutoNetDistributor<T> implements IDistributor<T> {

    private AutoNetPatternAnontation.NetPattern mNetPattern;
    private AutoNetTypeAnontation.Type mReqType;
    private AutoNetTypeAnontation.Type mResType;
    private String mPushFileKey;
    private String mFilePath;
    private String mFileName;
    private AutoNetRepo<T> mRepo;
    private IAutoNetFileCallBack mAccompanyFileCallback;

    public AutoNetDistributor(AutoNetPatternAnontation.NetPattern netPattern,
                              AutoNetTypeAnontation.Type reqType, AutoNetTypeAnontation.Type resType, String pushFileKey, String filePath, String fileName, IAutoNetFileCallBack accompanyFileCallback, AutoNetRepo<T> repo) {
        this.mNetPattern = netPattern;
        this.mReqType = reqType;
        this.mResType = resType;
        this.mPushFileKey = pushFileKey;
        this.mFilePath = filePath;
        this.mFileName = fileName;
        this.mAccompanyFileCallback = accompanyFileCallback;
        this.mRepo = repo;
    }

    @Override
    public T start(IAutoNetFileCallBack callBack) throws Exception {
        T t;
        if (AutoNetTypeUtil.isFileOperation(this.mReqType, this.mResType)) {
            t = startFileRequest(callBack);
        } else {
            t = startNonFileRequest();
        }
        return t;
    }

    private T startNonFileRequest() throws Exception {
        // check network
        assertNetWork();

        T t = null;
        if (AutoNetPatternAnontation.NetPattern.GET.equals(this.mNetPattern)) {
            // get request
            t = this.mRepo.doNetGet();
        } else if (AutoNetPatternAnontation.NetPattern.POST.equals(this.mNetPattern)) {
            // post request
            t = this.mRepo.doNetPost();
        } else if (AutoNetPatternAnontation.NetPattern.DELETE.equals(this.mNetPattern)) {
            // delete request
            t = this.mRepo.doDelete();
        } else if (AutoNetPatternAnontation.NetPattern.PUT.equals(this.mNetPattern)) {
            // put request
            t = this.mRepo.doPut();
        }

        return t;
    }

    private T startFileRequest(final IAutoNetFileCallBack callBack) throws Exception {
        // check network
        assertNetWork();

        IAutoNetFileCallBack transmitCallback = new IAutoNetFileCallBack() {
            @Override
            public void onProgress(float progress) {
                // transmit file progress
                if (callBack != null) {
                    callBack.onProgress(progress);
                }
                // User monitoring
                if (mAccompanyFileCallback != null) {
                    mAccompanyFileCallback.onProgress(progress);
                }
            }

            @Override
            public void onComplete(File file) {
                // transmit file progress
                if (callBack != null) {
                    callBack.onComplete(file);
                }
                // User monitoring
                if (mAccompanyFileCallback != null) {
                    mAccompanyFileCallback.onComplete(file);
                }
            }
        };

        T t = null;
        if (AutoNetTypeUtil.isPushFileOperation(this.mReqType)) {
            t = this.mRepo.pushFile(this.mPushFileKey, this.mFilePath, transmitCallback);
        } else if (AutoNetTypeUtil.isPullFileOperation(this.mResType)) {
            t = this.mRepo.pullFile(this.mFilePath, this.mFileName, transmitCallback);
        }
        return t;
    }

    private void assertNetWork() throws Exception {
        if (!NetUtil.isNetworkAvailable(AutoNetConstant.sAutoNetContext)) {
            throw new NoNetError();
        }
    }

}
