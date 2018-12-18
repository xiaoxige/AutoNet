package cn.xiaoxige.recommendeddemo.base;

public class BaseResponse {

    private int errorCode = 0;
    private String errorMsg;


    /****
     * 是否成功
     * @return
     */
    public boolean isSuccess() {
        return this.errorCode == 0;
    }

    /****
     * 是否token失效
     * @return
     */
    public boolean isTokenInvalid() {
        return this.errorCode == -1001;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
