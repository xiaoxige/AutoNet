package cn.xiaoxige.autonet.entity;

import java.util.List;


/**
 * @author by zhuxiaoan on 2018/12/18 0018.
 */
public class MainResponse extends BaseResponse {

    private List<MainEntity> data;

    public List<MainEntity> getData() {
        return data;
    }

    public void setData(List<MainEntity> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MainResponse{" +
                "data=" + data +
                '}';
    }
}
