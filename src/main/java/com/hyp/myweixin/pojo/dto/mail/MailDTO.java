package com.hyp.myweixin.pojo.dto.mail;

import java.util.Map;

/**
 * 邮件接收参数
 *
 * @author heyapei
 */

public class MailDTO {

    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 接收人邮件地址
     */
    private String email;
    /**
     * 附加，value 文件的绝对地址/动态模板数据
     */
    private Map<String, Object> attachment;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, Object> getAttachment() {
        return attachment;
    }

    public void setAttachment(Map<String, Object> attachment) {
        this.attachment = attachment;
    }

    @Override
    public String toString() {
        return "MailVO{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", email='" + email + '\'' +
                ", attachment=" + attachment +
                '}';
    }
}