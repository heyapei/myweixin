/*
+--------------------------------------------------------------------------
|   mtons [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.hyp.myweixin.exception;

/**
 * @author langhsu
 */
public class MyDefinitionException extends RuntimeException {
    private static final long serialVersionUID = -7443213283905815106L;
    private int code;

    public MyDefinitionException() {
    }

    /**
     * MyDefinitionException
     *
     * @param code 错误代码
     */
    public MyDefinitionException(int code) {
        super("code=" + code);
        this.code = code;
    }

    /**
     * MyDefinitionException
     *
     * @param message 错误消息
     */
    public MyDefinitionException(String message) {
        super(message);
    }

    /**
     * MyDefinitionException
     *
     * @param cause 捕获的异常
     */
    public MyDefinitionException(Throwable cause) {
        super(cause);
    }

    /**
     * MyDefinitionException
     *
     * @param message 错误消息
     * @param cause   捕获的异常
     */
    public MyDefinitionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * MyDefinitionException
     *
     * @param code    错误代码
     * @param message 错误消息
     */
    public MyDefinitionException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "MyDefinitionException{" +
                "code=" + code +
                '}';
    }
}
