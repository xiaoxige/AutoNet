package cn.xiaoxige.autonet_api.util;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @author by zhuxiaoan on 2017/11/8 0008.
 *         执行OKHTTP请求
 */

public class OkHttpUtil {

    public static <T> T execute(Call<T> call) throws Exception {
        Response<T> response;
        try {
            response = call.execute();
        } catch (Exception e) {
            return null;
        }
        return response.body();
    }
}
