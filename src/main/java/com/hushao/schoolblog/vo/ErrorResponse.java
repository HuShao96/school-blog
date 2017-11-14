package com.hushao.schoolblog.vo;

/**
 * 返回对象
 */

public class ErrorResponse  {
    private Boolean success;//处理是否成功
    private String message;//处理后消息提示
    private Object body;//处理后数据

    public ErrorResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ErrorResponse(Boolean success, String message, Object body) {
        this.success = success;
        this.message = message;
        this.body = body;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Response{" +
                "success=" + success +
                ", message=" + message +
                ", body=" + body +
                '}';
    }
}
