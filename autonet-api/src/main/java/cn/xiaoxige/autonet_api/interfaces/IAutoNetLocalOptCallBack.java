package cn.xiaoxige.autonet_api.interfaces;

import java.util.Map;

/**
 * @author by zhuxiaoan on 2018/5/21 0021.
 *         AutoNet operates the callback of the local database and gives it to the user to process it (in the thread).
 */

public interface IAutoNetLocalOptCallBack extends IAutoNetCallBack {

    /**
     * The local database operation is given to the user himself to process (the method is in the thread).
     *
     * @param request
     * @return
     */
    Object optLocalData(Map request);
}
