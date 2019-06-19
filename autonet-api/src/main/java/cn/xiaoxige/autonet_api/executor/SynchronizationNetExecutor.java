package cn.xiaoxige.autonet_api.executor;

import cn.xiaoxige.autonet_api.distributor.ILocalDistributor;

/**
 * @author xiaoxige
 * @date 2019/6/19 0019 13:14
 * -
 * email: xiaoxigexiaoan@outlook.com
 * desc: Synchronized Executors
 */
class SynchronizationNetExecutor<T> {

    private ILocalDistributor<T> mDistributor;

    SynchronizationNetExecutor(ILocalDistributor<T> distributor) {
        this.mDistributor = distributor;
    }

    T start() throws Exception {
        return mDistributor.start(null);
    }

}
