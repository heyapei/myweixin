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
    @Override
    public String toString() {
        return "Result{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

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
        OK("200", "请求成功"),
        BAD_REQUEST("400", "错误的请求"),
        UNAUTHORIZED("401", "禁止访问"),
        NOT_FOUND("404", "没有可用的数据"),
        PWD_EEOR("300", "密码错误"),
        EXIT("403", "已经存在"),
        INTERNAL_SERVER_ERROR("500", "服务器遇到了一个未曾预料的状况"),
        SERVICE_UNAVAILABLE("503", "服务器当前无法处理请求"),
        ERROR("9999", "数据不能为空"),
        /* 令牌失效 */
        TOKEN_INVALID("401", "令牌失效"),
        SERVER_ERROR("400", "服务器错误"),
        /*参数错误 10001-19999 */
        PARAM_IS_INVALID("10001", "参数无效"),
        PARAM_IS_BLANK("10002", "参数为空"),
        PARAM_TYPE_BIND_ERROR("10003", "参数类型错误"),
        PARAM_NOT_COMPLETE("10004", "参数缺失"),

        /* 用户错误：20001-29999*/
        USER_NOT_LOGGED_IN("20001", "用户未登录"),
        USER_LOGIN_ERROR("20002", "账号不存在或密码错误"),
        USER_ACCOUNT_FORBIDDEN("20003", "账号已被禁用"),
        USER_NOT_EXIST("20004", "用户不存在"),
        USER_HAS_EXISTED("20005", "用户已存在"),
        Cert_HAS_EXISTED("20006", "认证已存在"),


        /* 业务错误：30001-39999 */
        CREATE_FAIL("30001", "创建失败"),


        /* 系统错误：40001-49999 */
        SYSTEM_INNER_ERROR("40001", "系统繁忙，请稍后重试"),


        /* 数据错误：50001-599999 */
        RESULE_DATA_NONE("50001", "数据未找到"),
        DATA_IS_WRONG("50002", "数据有误"),
        DATA_ALREADY_EXISTED("50003", "数据已存在"),


        /* 接口错误：60001-69999 */
        INTERFACE_INNER_INVOKE_ERROR("60001", "内部系统接口调用异常"),
        INTERFACE_OUTTER_INVOKE_ERROR("60002", "外部系统接口调用异常"),
        INTERFACE_FORBID_VISIT("60003", "该接口禁止访问"),
        INTERFACE_ADDRESS_INVALID("60004", "接口地址无效"),
        INTERFACE_REQUEST_TIMEOUT("60005", "接口请求超时"),
        INTERFACE_EXCEED_LOAD("60006", "接口负载过高"),


        /* 权限错误：70001-79999 */
        PERMISSION_NO_ACCESS("70001", "只有标签 Owner ,才具备删除权限"),
        PERMISSION_NO_PHONE_ACCESS("70002", "此认证标签已有员工认证，不可以进行删除");


        /**
         * 状态码,长度固定为6位的字符串.
         */
        private String statusCode;

        /**
         * 错误信息.
         */
        private String statusMessage;

        Status(String statusCode, String statusMessage) {
            this.statusCode = statusCode;
            this.statusMessage = statusMessage;
        }

        public String getCode() {
            return statusCode;
        }

        public String getReason() {
            return statusMessage;
        }

        @Override
        public String toString() {
            return statusCode + ": " + statusMessage;
        }

    }

}