package cn.xiaoxige.autonet.entity;

import cn.xiaoxige.autonet_api.interfaces.IAutoNetResponse;

/**
 * @author by zhuxiaoan on 2018/5/22 0022.
 */

public class TestResponseEntity implements IAutoNetResponse {

    private String status;

    private long code;

    private String message;

    private Data data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "JsonTestResponseEntity{" +
                "status='" + status + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public static class Data {
        private String img;
        private String url;
        private String type;
        private String num_times;

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getNum_times() {
            return num_times;
        }

        public void setNum_times(String num_times) {
            this.num_times = num_times;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "img='" + img + '\'' +
                    ", url='" + url + '\'' +
                    ", type='" + type + '\'' +
                    ", num_times='" + num_times + '\'' +
                    '}';
        }
    }

}
