package com.hyp.myweixin.utils;


import com.github.pagehelper.Page;
import com.hyp.myweixin.exception.MyDefinitionException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 实体转换工具类
 *
 * @author Carl
 */
@Slf4j
public class MyEntityUtil {


    /**
     * 赋默认值
     */
    public static Object entitySetDefaultValue(Object object) throws MyDefinitionException {
        final String defaultStr = "";
        final Date defaultDate = new Date();
        final BigDecimal defaultDecimal = new BigDecimal(0);
        final Timestamp defaultTimestamp = new Timestamp(System.currentTimeMillis());
        try {
            Class clazz = object.getClass();
            Field[] fields = clazz.getDeclaredFields();

            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                String fieldName = field.getName();
                Class fieldClass = field.getType();
                //设置访问权限
                field.setAccessible(true);
                if (isFieldValueNull(fieldName, object)) {
                    if (fieldClass == Integer.class) {
                        field.set(object, defaultDecimal.intValue());
                    } else if (fieldClass == Long.class) {
                        field.set(object, defaultDecimal.longValue());
                    } else if (fieldClass == Float.class) {
                        field.set(object, defaultDecimal.doubleValue());
                    } else if (fieldClass == BigDecimal.class) {
                        field.set(object, defaultDecimal);
                    } else if (fieldClass == Date.class) {
                        field.set(object, defaultDate);
                    } else if (fieldClass == String.class) {
                        field.set(object, defaultStr);
                    } else if (fieldClass == Date.class) {
                        field.set(object, defaultTimestamp);
                    }
                    //MySQL，需要对对主键做特殊处理
                } else if (isStringFieldValueNull(fieldName, object, fieldClass)) {
                    field.set(object, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("为对象赋予默认值错误，错误原因：{}",e.toString());
            throw new MyDefinitionException("对象赋予默认值错误");
        }
        return object;
    }

    /**
     * 判断字段是否为空
     */
    private static boolean isFieldValueNull(String fieldName, Object object) throws ClassNotFoundException {
        boolean isNUll = false;
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = object.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(object, new Object[]{});
            if (value == null) {
                isNUll = true;
            }
            return isNUll;
        } catch (Exception e) {
            return isNUll;
        }
    }

    /**
     * 判断主键是否为空值
     */
    private static boolean isStringFieldValueNull(String fieldName, Object object, Class fieldClass) throws ClassNotFoundException {
        boolean isNUll = false;
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = object.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(object, new Object[]{});
            if (value == null) {
                isNUll = true;
            } else {
                if (fieldClass == String.class && StringUtils.isBlank((String) value)) {
                    isNUll = true;
                }
            }
            return isNUll;
        } catch (Exception e) {
            return isNUll;
        }
    }


    /**
     * 实体列表转Vm
     *
     * @param source           原列表
     * @param vmClass          vm类
     * @param ignoreProperties 忽略的字段
     * @param <T>              泛型
     * @return vm列表
     */
    public static <T> List<T> entity2VMList(List<?> source, Class<T> vmClass, String... ignoreProperties) {
        List<T> target = (source instanceof Page ? new Page<T>() : new ArrayList<T>());
        if (source instanceof Page) {
            BeanUtils.copyProperties(source, target);
        }
        if (CollectionUtils.isEmpty(source)) {
            return target;
        }
        source.forEach(e -> {
            target.add(entity2VM(e, vmClass, ignoreProperties));
        });
        return target;
    }

    /**
     * 实体转VM
     *
     * @param source           原对象
     * @param vmClass          要转换的对象
     * @param ignoreProperties 忽略的属性
     * @param <T>              泛型
     * @return 转换后对象
     * @author Say
     */
    public static <T> T entity2VM(Object source, Class<T> vmClass, String... ignoreProperties) {
        if (null == source) {
            return null;
        }
        try {
            T target = vmClass.newInstance();
            BeanUtils.copyProperties(source, target, ignoreProperties);
            return target;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * VM转实体
     * 底层用的vm2Entity，只是方法名做区分
     *
     * @param source           vm
     * @param entClass         实体
     * @param ignoreProperties 忽略的属性
     * @param <T>              泛型
     * @return 转换后的对象
     * @author Say
     */
    public static <T> T vm2Entity(Object source, Class<T> entClass, String... ignoreProperties) {
        return entity2VM(source, entClass, ignoreProperties);
    }

    /**
     * VM转实体集合
     * 底层用的entity2VMList，只是方法名做区分
     *
     * @param source           原对象
     * @param entClass         实体
     * @param ignoreProperties 忽略的属性
     * @param <T>              泛型
     * @return 转换后的对象
     * @author Say
     */
    public static <T> List<T> vm2EntityList(List<?> source, Class<T> entClass, String... ignoreProperties) {
        return entity2VMList(source, entClass, ignoreProperties);
    }

    /**
     * Entity VM 互转
     *
     * @param object      数据源
     * @param laterObject 转换对象
     * @param <T>         泛型
     */
    public static <T> void copyProperties(final T object, T laterObject) {

        if (null == object || null == laterObject) {
            return;
        }

        ConcurrentHashMap<String, Method> getMethods = findGetMethods(object.getClass().getMethods());

        ConcurrentHashMap<String, Method> setMethods = findSetMethods(laterObject.getClass().getDeclaredMethods());

        Iterator<Map.Entry<String, Method>> iterator = getMethods.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, Method> entry = iterator.next();
            String methodName = entry.getKey();
            Method getMethod = entry.getValue();
            Method setMethod = setMethods.get(methodName);
            if (null == setMethod) {
                continue;
            }
            try {
                Object value = getMethod.invoke(object, new Object[]{});
                setMethod.invoke(laterObject, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取所有的get方法
     *
     * @param methods 所有的方法
     * @return 所有的get方法
     */
    private static ConcurrentHashMap<String, Method> findGetMethods(Method[] methods) {
        ConcurrentHashMap<String, Method> getMethodsMap = new ConcurrentHashMap<>();
        for (Method method : methods) {
            if (isGetMethod(method.getName())) {
                getMethodsMap.put(getMethodName(method.getName()), method);
            }
        }
        return getMethodsMap;
    }

    /**
     * 获取所有的set方法
     *
     * @param methods 所有的方法
     * @return 所有的set方法
     */
    private static ConcurrentHashMap<String, Method> findSetMethods(Method[] methods) {
        ConcurrentHashMap<String, Method> setMethodsMap = new ConcurrentHashMap<>();
        for (Method method : methods) {
            if (isSetMethod(method.getName())) {
                setMethodsMap.put(getMethodName(method.getName()), method);
            }
        }
        return setMethodsMap;
    }


    /**
     * 取方法名
     *
     * @param getMethodName 方法名称
     * @return 去掉get set的方法名
     */
    private static String getMethodName(String getMethodName) {
        String fieldName = getMethodName.substring(3, getMethodName.length());
        return fieldName;
    }

    /**
     * 判断是否是get方法
     *
     * @param methodName
     * @return
     */
    private static boolean isGetMethod(String methodName) {
        int index = methodName.indexOf("get");
        if (index == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否是set方法
     *
     * @param methodName 方法名
     * @return 是否为set 方法
     */
    private static boolean isSetMethod(String methodName) {
        int index = methodName.indexOf("set");
        if (index == 0) {
            return true;
        }
        return false;
    }

}
