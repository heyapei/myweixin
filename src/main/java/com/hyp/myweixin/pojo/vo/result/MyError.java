package com.hyp.myweixin.pojo.vo.result;

import lombok.Data;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2020/4/15 18:08
 * @Description: TODO
 */
@Data
public class MyError {
    private Integer errorCode;
    private String codeMsg;
    private String errorMsg;

    public MyError() {
    }

    public MyError(Integer errorCode, String codeMsg, String errorMsg) {
        this.errorCode = errorCode;
        this.codeMsg = codeMsg;
        this.errorMsg = errorMsg;
    }
}
