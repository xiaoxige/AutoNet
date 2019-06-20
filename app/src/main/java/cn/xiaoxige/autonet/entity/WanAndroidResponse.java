package cn.xiaoxige.autonet.entity;

import java.util.List;


/**
 * @author by zhuxiaoan on 2018/12/18 0018.
 */
public class WanAndroidResponse extends BaseResponse {

    private List<WanAndroidEntity> data;

    public List<WanAndroidEntity> getData() {
        return data;
    }

    public void setData(List<WanAndroidEntity> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "WanAndroidResponse{" +
                "data=" + data +
                '}';
    }
}
