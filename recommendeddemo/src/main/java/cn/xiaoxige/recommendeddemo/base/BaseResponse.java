package cn.xiaoxige.recommendeddemo.base;

public class BaseResponse {

    private int code = 0;
    private String message;


    /****
     * 是否成功
     * @return
     */
    public boolean isSuccess() {
        return this.code == 0;
    }

    /****
     * 是否token失效
     * @return
     */
    public boolean isTokenInvalid() {
        return this.code == 1003;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
