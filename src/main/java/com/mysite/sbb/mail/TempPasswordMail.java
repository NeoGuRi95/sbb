package com.mysite.sbb.mail;

import java.io.UnsupportedEncodingException;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.mysite.sbb.exception.EmailException;
import com.mysite.sbb.exception.ErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TempPasswordMail implements MailService {

    private final JavaMailSender javaMailSender;

    private String ePw;

    @Override
    public MimeMessage createMessage(String to)
        throws UnsupportedEncodingException, MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(RecipientType.TO, to);
        message.setSubject("sbb 임시 비밀번호");

        String msgg = "";
        msgg += "<div style='margin:100px;'>";
        msgg +=
            "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg += "<h3 style='color:blue;'>임시 비밀번호입니다.</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "CODE : <strong>";
        msgg += ePw + "</strong><div><br/> ";
        msgg += "</div>";
        message.setText(msgg, "utf-8", "html");
        message.setFrom(new InternetAddress("jea5158@gmail.com", "sbb_Admin"));

        return message;
    }

    @Override
    public void sendSimpleMessage(String to, String pw) {
        this.ePw = pw;
        MimeMessage message;
        try {
            message = createMessage(to);
        } catch (UnsupportedEncodingException | MessagingException e) {
            e.printStackTrace();
            throw new EmailException(ErrorCode.CREATE_MAIL_FAIL);
        }
        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
            throw new EmailException(ErrorCode.SEND_MAIL_FAIL);
        }
    }

}
