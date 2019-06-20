package cn.xiaoxige.autonet.entity;

import java.util.List;

/**
 * @author xiaoxige
 * @date 2019/6/20 0020 13:18
 * -
 * email: xiaoxigexiaoan@outlook.com
 * desc: TODO
 */
public class ZipEntity {

    private List<WanAndroidEntity> data;
    private String baidu;

    public List<WanAndroidEntity> getData() {
        return data;
    }

    public void setData(List<WanAndroidEntity> data) {
        this.data = data;
    }

    public String getBaidu() {
        return baidu;
    }

    public void setBaidu(String baidu) {
        this.baidu = baidu;
    }

    @Override
    public String toString() {
        return "ZipEntity{" +
                "data=" + data +
                ", baidu='" + baidu + '\'' +
                '}';
    }
}
