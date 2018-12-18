package cn.xiaoxige.recommendeddemo.main.response;

import java.util.List;

import cn.xiaoxige.recommendeddemo.base.BaseResponse;

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
}
