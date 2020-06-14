package com.hyp.myweixin.utils;

import java.util.UUID;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/14 17:53
 * @Description: TODO
 */
public class MyCommonUtil {




    /**
     * 生成不带符号的uuid
     * @return
     */
    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成带符号的uuid
     * @return
     */
    public static String getUUID2(){
        return UUID.randomUUID().toString();
    }



}
