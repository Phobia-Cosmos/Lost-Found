package org.hnust.utils;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;


public class SendMailUtil {

    /**
     * 发送邮件代码
     *
     * @param targetEmail 目标用户邮箱
     * @param msg         发送的验证码信息
     */
    public static void sendEmailCode(String targetEmail, String msg) {
        try {
            // 创建邮箱对象
            SimpleEmail mail = new SimpleEmail();
            // 设置发送邮件的服务器
            mail.setHostName("smtp.qq.com");
            // "你的邮箱号"+ "上文开启SMTP获得的授权码"
            mail.setAuthentication("2078719076@qq.com", "tukhlhbmmvyafcjj");
            // 发送邮件 "你的邮箱号"+"发送时用的昵称"
            mail.setFrom("2078719076@qq.com", "终身学习团队");
            // 使用安全链接
            // mail.setSSLOnConnect(true);
            // 接收用户的邮箱
            mail.addTo(targetEmail);
            // 邮件的主题(标题)
            mail.setSubject("注册验证码");
            // 邮件的内容
            mail.setMsg(msg);
            // 发送
            mail.send();
        } catch (EmailException e) {
            e.printStackTrace();
        }
    }
}
