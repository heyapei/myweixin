package com.hyp.myweixin.utils;

import com.hyp.myweixin.exception.MyDefinitionException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2020/7/22 13:29
 * @Description: TODO
 * <p>
 * 注意：
 * 其中Journal是一个实体类 StatusEnum 只是其中一个数据的枚举数据
 * 如果有单独出来的枚举类也是相同使用
 * <p>
 * 方法中使用的getCode 和 getMsg 都是枚举类中 应该有的属性对应的get方法
 * 可以直接在调用的时候直接进行更改
 * <p>
 * 1.       Class<Journal.StatusEnum> clasz = Journal.StatusEnum.class;
 * Map<Object, String> map = EnumUtil.EnumToMap(clasz);
 * JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(map));
 * 可以将枚举类型转成map然后再给转换成JSON
 * 2.       String des = (String) EnumUtil.getEnumDescriotionByValue(2, clasz);
 * System.out.println("------------：" + des);
 * 获取指定数据code的msg的数据
 * 3.
 * Object valueget = EnumUtil.getEnumDescriotionByValue("你在哪里呀", clasz, "getMsg", "getCode");
 * System.out.println("-----：" + valueget);
 * 通过描述msg获取code的值
 * 4.
 * String field = (String) EnumUtil.getEnumKeyByValue("你好", clasz, "getMsg");
 * System.out.println("--------：" + field);
 * 可以通过msg获取整个枚举数据
 * 5.
 * String fielda1 = (String) EnumUtil.getEnumKeyByValue("1", clasz, "getCode");
 * System.out.println("----：" + fielda1);
 * 和第4个类型相同 通过code获取整个枚举数据
 * 6.
 * Journal.StatusEnum str = Enum.valueOf(Journal.StatusEnum.class, "NO");
 * System.out.println("根据字段名称取值---------：" + str.getMsg());
 * System.out.println("获取枚举所有字段---------：" + Arrays.toString(Journal.StatusEnum.values()));
 */
@Slf4j
public class MyEnumUtil {


    /**
     * 用于简单的枚举数据判断是否包含某个value值
     * (
     * ---适用于这种简单的枚举数据 判断的value是这个msg值
     * ActiveStatusEnum(Integer code, String msg) {
     * this.code = code;
     * this.msg = msg;
     * }
     * ---
     * )
     * 因为可以把枚举转换成map然后在进行判断
     *
     * @param value 需要判断的value值
     * @param enumT 枚举数据
     * @return boolean 是否存在
     */
    public static Boolean enumValueRight(Object value, Class enumT) {
        Boolean valueRight = false;
        Map<Object, String> map = MyEnumUtil.EnumToMap(enumT);
        for (Map.Entry<Object, String> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                valueRight = true;
                break;
            }
        }
        return valueRight;
    }


    /**
     * 用于简单的枚举数据判断是否包含某个key值
     * (
     * ---适用于这种简单的枚举数据 判断的key是这个code值
     * ActiveStatusEnum(Integer code, String msg) {
     * this.code = code;
     * this.msg = msg;
     * }
     * ---
     * )
     * 因为可以把枚举转换成map然后在进行判断
     *
     * @param key   需要判断的key值
     * @param enumT 枚举数据
     * @return boolean 是否存在
     */
    public static Boolean enumKeyRight(Object key, Class enumT) {
        Boolean keyRight = false;
        Map<Object, String> map = MyEnumUtil.EnumToMap(enumT);
        for (Map.Entry<Object, String> entry : map.entrySet()) {
            if (entry.getKey().equals(key)) {
                keyRight = true;
                break;
            }
        }
        return keyRight;
    }


    /**
     * 根据反射，通过方法名称获取方法值，忽略大小写的
     *
     * @param methodName
     * @param obj
     * @param args
     * @return return value
     */
    private static <T> Object getMethodValue(String methodName, T obj,
                                             Object... args) {
        Object resut = "";
        // boolean isHas = false;
        try {
            /********************************* start *****************************************/
            Method[] methods = obj.getClass().getMethods(); //获取方法数组，这里只要共有的方法
            if (methods.length <= 0) {
                return resut;
            }
            // String methodstr=Arrays.toString(obj.getClass().getMethods());
            // if(methodstr.indexOf(methodName)<0){ //只能粗略判断如果同时存在 getValue和getValues可能判断错误
            // return resut;
            // }
            // List<Method> methodNamelist=Arrays.asList(methods); //这样似乎还要循环取出名称
            Method method = null;
            for (int i = 0, len = methods.length; i < len; i++) {
                if (methods[i].getName().equalsIgnoreCase(methodName)) { //忽略大小写取方法
                    // isHas = true;
                    methodName = methods[i].getName(); //如果存在，则取出正确的方法名称
                    method = methods[i];
                    break;
                }
            }
            // if(!isHas){
            // return resut;
            // }
            /*************************** end ***********************************************/
            // Method method = obj.getClass().getDeclaredMethod(methodName); ///确定方法
            if (method == null) {
                return resut;
            }
            resut = method.invoke(obj, args); //方法执行
            if (resut == null) {
                resut = "";
            }
            return resut; //返回结果
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resut;
    }


    /**
     * 通过value值获取对应的描述信息 第二中方式
     * 使用方式
     * PaymentTypeEnum paymentTypeEnum=getByIntegerTypeCode(PaymentTypeEnum.class,"getCode",1);
     * String message=paymentTypeEnum.getMessage();
     *
     * @param clazz
     * @param getTypeCodeMethodName
     * @param typeCode
     * @param <T>
     * @return
     */
    public static <T extends Enum<T>> T getByIntegerTypeCode(Class<T> clazz, String getTypeCodeMethodName, Integer typeCode) throws MyDefinitionException {
        T result = null;
        try {
            //Enum接口中没有values()方法，我们仍然可以通过Class对象取得所有的enum实例
            T[] arr = clazz.getEnumConstants();
            //获取定义的方法
            Method targetMethod = clazz.getDeclaredMethod(getTypeCodeMethodName);
            Integer typeCodeVal = null;
            //遍历枚举定义
            for (T entity : arr) {
                //获取传过来方法的
                typeCodeVal = Integer.valueOf(targetMethod.invoke(entity).toString());
                //执行的方法的值等于参数传过来要判断的值
                if (typeCodeVal.equals(typeCode)) {
                    //返回这个枚举
                    result = entity;
                    break;
                }
            }
        } catch (IllegalAccessException e) {
            log.error("通过方法获取枚举信息值错误,错误原因:{}", e.toString());
            throw new MyDefinitionException("通过方法获取枚举信息值错误");
        } catch (IllegalArgumentException e) {

            log.error("通过方法获取枚举信息值错误,错误原因:{}", e.toString());
            throw new MyDefinitionException("通过方法获取枚举信息值错误");
        } catch (InvocationTargetException e) {
            log.error("通过方法获取枚举信息值错误,错误原因:{}", e.toString());
            throw new MyDefinitionException("通过方法获取枚举信息值错误");
        } catch (NoSuchMethodException e) {
            log.error("通过方法获取枚举信息值错误,错误原因:{}", e.toString());
            throw new MyDefinitionException("通过方法获取枚举信息值错误");
        } catch (SecurityException e) {
            log.error("通过方法获取枚举信息值错误,错误原因:{}", e.toString());
            throw new MyDefinitionException("通过方法获取枚举信息值错误");
        }
        return result;
    }


    /**
     * 通过value值获取对应的描述信息
     *
     * @param value
     * @param enumT
     * @param methodNames 指定枚举类的get方法名
     * @return enum description
     */
    public static <T> Object getEnumDescriptionByValue(Object value,
                                                       Class<T> enumT, String... methodNames) {
        if (!enumT.isEnum()) { //不是枚举则返回""
            return "";
        }
        T[] enums = enumT.getEnumConstants();  //获取枚举的所有枚举属性，似乎这几句也没啥用，一般既然用枚举，就一定会添加枚举属性
        if (enums == null || enums.length <= 0) {
            return "";
        }
        int count = methodNames.length;
        String valueMathod = "getCode";    //默认获取枚举value方法，与接口方法一致
        String desMathod = "getMsg"; //默认获取枚举description方法
        if (count >= 1 && !"".equals(methodNames[0])) {
            valueMathod = methodNames[0];
        }
        if (count == 2 && !"".equals(methodNames[1])) {
            desMathod = methodNames[1];
        }
        for (int i = 0, len = enums.length; i < len; i++) {
            T t = enums[i];
            try {
                Object resultValue = getMethodValue(valueMathod, t);//获取枚举对象value
                if (resultValue.toString().equals(value + "")) {
                    Object resultDes = getMethodValue(desMathod, t); //存在则返回对应描述
                    return resultDes;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * 通过枚举value或者自定义值及方法获取枚举属性值
     *
     * @param value
     * @param enumT
     * @param methodNames 指定枚举类的get方法名
     * @return enum key
     */
    public static <T> String getEnumKeyByValue(Object value, Class<T> enumT,
                                               String... methodNames) {
        if (!enumT.isEnum()) {
            return "";
        }
        T[] enums = enumT.getEnumConstants();
        if (enums == null || enums.length <= 0) {
            return "";
        }
        int count = methodNames.length;
        String valueMathod = "getMsg"; //默认方法
        if (count >= 1 && !"".equals(methodNames[0])) { //独立方法
            valueMathod = methodNames[0];
        }
        for (int i = 0, len = enums.length; i < len; i++) {
            T tobj = enums[i];
            try {
                Object resultValue = getMethodValue(valueMathod, tobj);
                if (resultValue != null
                        && resultValue.toString().equals(value + "")) { //存在则返回对应值
                    return tobj + "";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }


    /**
     * 枚举转map结合value作为map的key,description作为map的value
     *
     * @param enumT
     * @param methodNames
     * @return enum mapcolloction
     */
    public static <T> Map<Object, String> EnumToMap(Class<T> enumT,
                                                    String... methodNames) {
        Map<Object, String> enummap = new HashMap<Object, String>();
        if (!enumT.isEnum()) {
            return enummap;
        }
        T[] enums = enumT.getEnumConstants();
        if (enums == null || enums.length <= 0) {
            return enummap;
        }
        int count = methodNames.length;
        String valueMathod = "getCode"; //默认接口value方法
        String desMathod = "getMsg";//默认接口description方法
        if (count >= 1 && !"".equals(methodNames[0])) { //扩展方法
            valueMathod = methodNames[0];
        }
        if (count == 2 && !"".equals(methodNames[1])) {
            desMathod = methodNames[1];
        }
        for (int i = 0, len = enums.length; i < len; i++) {
            T tobj = enums[i];
            try {
                Object resultValue = getMethodValue(valueMathod, tobj); //获取value值
                if ("".equals(resultValue)) {
                    continue;
                }
                Object resultDes = getMethodValue(desMathod, tobj); //获取description描述值
                if ("".equals(resultDes)) { //如果描述不存在获取属性值
                    resultDes = tobj;
                }
                enummap.put(resultValue, resultDes + "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return enummap;
    }


}
