package com.hyp.myweixin.utils;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * 判断是否有错误工具类
 *
 * @author heyapei
 */
@Component
public class MyErrorList {
    private ArrayList al;

    public MyErrorList() {
        al = new ArrayList();
    }

    public boolean hasErrors() {
        if (al.size() > 0) {
            return true;
        }
        return false;
    }

    public boolean noErrors() {
        if (al.size() > 0) {
            return false;
        }
        return true;
    }

    public int count() {
        return al.size();
    }

    public void add(String e) {
        al.add(e);
    }

    public String toPlainString() {
        StringBuffer ret = new StringBuffer();
        if (al.size() > 0) {
            for (int i = 0; i < al.size(); i++) {
                if (i > 0) {
                    ret.append("；");
                }
                ret.append(al.get(i));
            }
        }
        return ret.toString();
    }

    @Override
    public String toString() {
        return "ErrorList{" +
                "al=" + al +
                '}';
    }

    public String toHtmlString() {
        StringBuffer ret = new StringBuffer();
        if (al.size() > 0) {
            ret.append("<UL>");
            for (int i = 0; i < al.size(); i++) {
                ret.append("<LI>").append(al.get(i)).append("</LI>");
            }
            ret.append("</UL>");
        }
        return ret.toString();
    }

    public String toJsString() {
        StringBuffer ret = new StringBuffer();
        if (al.size() > 0) {
            for (int i = 0; i < al.size(); i++) {
                ret.append(al.get(i)).append("\\n");
            }
        }
        return ret.toString();
    }
}