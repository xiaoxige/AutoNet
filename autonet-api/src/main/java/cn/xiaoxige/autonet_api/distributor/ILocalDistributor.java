package cn.xiaoxige.autonet_api.distributor;

/**
 * @author xiaoxige
 * @date 2019/6/19 0019 13:17
 * -
 * email: xiaoxigexiaoan@outlook.com
 * desc: Support for local distributors
 */
public interface ILocalDistributor<T> extends IDistributor<T> {

    /**
     * Local Operational Processing
     *
     * @return
     * @throws Exception
     */
    T optLocalDistributor() throws Exception;

}
