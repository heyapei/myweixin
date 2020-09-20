package com.hyp.myweixin.service.impl;


import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.dto.mail.MailDTO;
import com.hyp.myweixin.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Map;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/5/27 19:06
 * @Description: TODO
 */
@Service
@Slf4j
public class MailServiceImpl implements MailService {

    @Resource
    private TemplateEngine templateEngine;

    @Resource
    private JavaMailSender javaMailSender;

    @Value("${mail.fromMail.addr}")
    private String from;


    /**
     * 发送模板邮件 使用thymeleaf模板 异步处理
     * 若果使用freemarker模板
     * Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
     * configuration.setClassForTemplateLoading(this.getClass(), "/templates");
     * String emailContent = FreeMarkerTemplateUtils.processTemplateIntoString(configuration.getTemplate("mail.ftl"), params);
     *
     * @param mailVO
     */
    @Async("threadPoolTaskExecutor")
    @Override
    public void sendTemplateMailAsync(MailDTO mailVO) {

        sendTemplateMail(mailVO);
    }

    /**
     * 异步发送纯文本邮件
     *
     * @param mailVO
     */
    @Async("threadPoolTaskExecutor")
    @Override
    public void sendTextMailAsync(MailDTO mailVO) {
        //建立邮件消息
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            //是否发送的邮件是富文本（附件，图片，html等）
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom(new InternetAddress(from));// 发送人的邮箱
            messageHelper.setTo(mailVO.getEmail());//发给谁  对方邮箱
            messageHelper.setSubject(mailVO.getTitle());//标题
            // 设置为不为html
            messageHelper.setText(mailVO.getContent(), false);
            //发送
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("纯文本邮件发送失败->message:{}", e.getMessage());
        }


    }


    /**
     * 纯文本邮件
     *
     * @param mailVO
     */
    //@Async //不解释不懂自行百度，友情提示：有坑
    @Override
    public void sendTextMail(MailDTO mailVO) {
        /*//建立邮件消息
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from); // 发送人的邮箱
        message.setSubject(mail.getTitle()); //标题
        message.setTo(mail.getEmail()); //发给谁  对方邮箱
        message.setText(mail.getContent()); //内容
        try {
            javaMailSender.send(message); //发送
        } catch (MailException e) {
            log.error("纯文本邮件发送失败->message:{}", e.getMessage());

        }*/
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            //是否发送的邮件是富文本（附件，图片，html等）
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom(new InternetAddress(from));// 发送人的邮箱
            messageHelper.setTo(mailVO.getEmail());//发给谁  对方邮箱
            messageHelper.setSubject(mailVO.getTitle());//标题
            // 设置为不为html
            messageHelper.setText(mailVO.getContent(), false);
            //发送
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("纯文本邮件发送失败->message:{}", e.getMessage());
        }


    }

    /**
     * 发送的邮件是富文本（附件，图片，html等）
     *
     * @param mailVO
     * @param isShowHtml 是否解析html
     */
    @Override
    public void sendHtmlMail(MailDTO mailVO, boolean isShowHtml) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            //是否发送的邮件是富文本（附件，图片，html等）
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom(new InternetAddress(from));// 发送人的邮箱
            messageHelper.setTo(mailVO.getEmail());//发给谁  对方邮箱
            messageHelper.setSubject(mailVO.getTitle());//标题
            messageHelper.setText(mailVO.getContent(), isShowHtml);//false，显示原始html代码，无效果
            //判断是否有附加图片等
            if (mailVO.getAttachment() != null && mailVO.getAttachment().size() > 0) {
                mailVO.getAttachment().entrySet().stream().forEach(entrySet -> {
                    try {
                        File file = new File(String.valueOf(entrySet.getValue()));
                        if (file.exists()) {
                            messageHelper.addAttachment(entrySet.getKey(), new FileSystemResource(file));
                        }
                    } catch (MessagingException e) {
                        log.error("附件发送失败->message:{}", e.getMessage());
                    }
                });
            }
            //发送
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("富文本邮件发送失败->message:{}", e.getMessage());
        }
    }

    /**
     * 发送模板邮件 使用thymeleaf模板
     * 若果使用freemarker模板
     * Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
     * configuration.setClassForTemplateLoading(this.getClass(), "/templates");
     * String emailContent = FreeMarkerTemplateUtils.processTemplateIntoString(configuration.getTemplate("mail.ftl"), params);
     *
     * @param mailVO
     */
    @Override
    public void sendTemplateMail(MailDTO mailVO) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            // messageHelper.setFrom(from);// 发送人的邮箱
            messageHelper.setFrom(new InternetAddress(from));
            messageHelper.setTo(mailVO.getEmail());//发给谁  对方邮箱
            messageHelper.setSubject(mailVO.getTitle()); //标题
            /*判断传过来的数据有没有指定页面数据*/
            String pageName = "defaultReturnRichTextMail";
            String pageNameKey = "pageName";
            Map<String, Object> attachment = mailVO.getAttachment();
            if (attachment != null) {
                if (attachment.containsKey(pageNameKey)) {
                    pageName = attachment.get(pageNameKey).toString();
                }
            }
            //使用模板thymeleaf
            //Context是导这个包import org.thymeleaf.context.Context;
            Context context = new Context();
            //定义模板数据
            context.setVariables(mailVO.getAttachment());
            //获取thymeleaf的html模板
            //指定模板路径
            String emailContent = templateEngine.process("mail/" + pageName, context);
            messageHelper.setText(emailContent, true);
            //发送邮件
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("模板邮件发送失败->message:{}", e.getMessage());
            throw new MyDefinitionException("邮件发送错误");
        }
    }

}
