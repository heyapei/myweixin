package com.hyp.myweixin.pojo.vo.result;

/**
 * <p>
 * Title: RESTful REST返回结果
 * </p>
 *
 * <p>
 * Description: 构建微服务数据返回对象
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2017 by unknown
 * </p>
 *
 * @author heyapei
 */
public class Result<T> {

    /**
     * 状态码
     */
    private String status;

    /**
     * 获取状态。
     *
     * @return 状态
     */
    public String getStatus() {
        return status;
    }

    /**
     * 状态信息,错误描述.
     */
    private String message;

    /**
     * 获取消息内容。
     *
     * @return 消息
     */
    public String getMessage() {
        return message;
    }

    /**
     * 数据.
     */
    private T data;

    /**
     * 获取数据内容。
     *
     * @return 数据
     */
    public T getData() {
        return data;
    }

    private Result(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    private Result(String status, String message) {
        this.status = status;
        this.message = message;
    }

    private Result(String message) {
        this.message = message;
    }

    /**
     * 创建一个带有<b>状态</b>、<b>消息</b>和<b>数据</b>的结果对象.
     *
     * @param status  状态
     * @param message 消息内容
     * @param data    数据
     * @return 结构数据
     */
    public static <T> Result<T> buildResult(Status status, String message, T data) {
        return new Result<T>(status.getCode(), message, data);
    }

    /**
     * 创建一个带有<b>状态</b>、<b>消息</b>和<b>数据</b>的结果对象.
     *
     * @param status  状态
     * @param message 消息内容
     * @return 结构数据
     */
    public static <T> Result<T> buildResult(Status status, String message) {
        return new Result<T>(status.getCode(), message);
    }

    /**
     * 创建一个带有<b>状态</b>和<b>数据</b>的结果对象.
     *
     * @param status 状态
     * @param data   数据
     * @return 结构数据
     */
    public static <T> Result<T> buildResult(Status status, T data) {
        return new Result<T>(status.getCode(), status.getReason(), data);
    }

    /**
     * 创建一个带有<b>状态</b>的结果对象.
     *
     * @param status 状态
     * @return 结构数据
     */
    public static <T> Result<T> buildResult(Status status) {
        return new Result<T>(status.getCode(), status.getReason());
    }

    /**
     * <p>
     * Title: 状态枚举
     * </p>
     *
     * <p>
     * Description: 用于Result构建时，规范状态值范围
     * </p>
     *
     * <p>
     * Copyright: Copyright (c) 2017 by unknown
     * </p>
     *
     * <p>
     * Company: yu feng
     * </p>
     *
     * @author: unknown
     * @version: 1.0
     * @date: 2017年11月28日 下午14:05:27
     */
    public enum Status {

        /**
         * 状态
         */
        OK("200", "正确"), BAD_REQUEST("400", "错误的请求"), UNAUTHORIZED("401", "禁止访问"), NOT_FOUND("404",
                "没有可用的数据"), PWD_EEOR("300",
                "密码错误"), EXIT("403",
                "已经存在"), INTERNAL_SERVER_ERROR("500",
                "服务器遇到了一个未曾预料的状况"), SERVICE_UNAVAILABLE("503", "服务器当前无法处理请求"), ERROR("9999", "数据不能为空");
        /**
         * 状态码,长度固定为6位的字符串.
         */
        private String code;

        /**
         * 错误信息.
         */
        private String reason;

        Status(String code, String reason) {
            this.code = code;
            this.reason = reason;
        }

        public String getCode() {
            return code;
        }

        public String getReason() {
            return reason;
        }

        @Override
        public String toString() {
            return code + ": " + reason;
        }

    }

}