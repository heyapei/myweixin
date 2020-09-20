package com.hyp.myweixin.service;


import com.hyp.myweixin.pojo.dto.mail.MailDTO;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/5/27 19:06
 * @Description: TODO
 */
public interface MailService {
    /**
     * 纯文本邮件
     *
     * @param mailVO
     */
    void sendTextMail(MailDTO mailVO);


    /**
     * 异步发送纯文本邮件
     *
     * @param mailVO
     */
    void sendTextMailAsync(MailDTO mailVO);

    /**
     * 发送的邮件是富文本（附件，图片，html等）
     *
     * @param mailVO
     * @param isShowHtml 是否解析html
     */
    void sendHtmlMail(MailDTO mailVO, boolean isShowHtml);

    /**
     * 发送模板邮件 使用thymeleaf模板
     * <p>
     * 若果使用freemarker模板
     * Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
     * configuration.setClassForTemplateLoading(this.getClass(), "/templates");
     * String emailContent = FreeMarkerTemplateUtils.processTemplateIntoString(configuration.getTemplate("mail.ftl"), params);
     *
     * @param mailVO mailVO中title邮件标题；content内容；email接收人的邮件地址：
     *               attachment附件信息；使用thymeleaf的时候值就放在这个里面 然后前后端对应好key值
     */
    void sendTemplateMail(MailDTO mailVO);


    /**
     * 发送模板邮件 使用thymeleaf模板 异步处理
     * 若果使用freemarker模板
     * Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
     * configuration.setClassForTemplateLoading(this.getClass(), "/templates");
     * String emailContent = FreeMarkerTemplateUtils.processTemplateIntoString(configuration.getTemplate("mail.ftl"), params);
     *
     * @param mailVO
     */
    void sendTemplateMailAsync(MailDTO mailVO);

}
