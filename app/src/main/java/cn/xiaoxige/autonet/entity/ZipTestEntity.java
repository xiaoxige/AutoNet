package cn.xiaoxige.autonet.entity;

/**
 * @author by zhuxiaoan on 2018/10/17 0017.
 */
public class ZipTestEntity {

    private String baiduWebMsg;
    private TestResponseEntity entity;

    public String getBaiduWebMsg() {
        return baiduWebMsg;
    }

    public void setBaiduWebMsg(String baiduWebMsg) {
        this.baiduWebMsg = baiduWebMsg;
    }

    public TestResponseEntity getEntity() {
        return entity;
    }

    public void setEntity(TestResponseEntity entity) {
        this.entity = entity;
    }

    @Override
    public String toString() {
        return "ZipTestEntity{" +
                "baiduWebMsg='" + baiduWebMsg + '\'' +
                ", entity=" + entity +
                '}';
    }
}
