package com.example.krisinoteBackend.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailingService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    public void sendHtmlEmail(String to, String verificationCode) throws MessagingException, MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        message.setRecipients(MimeMessage.RecipientType.TO, to);
        message.setSubject("NoReply: Verification Code - Krisinote");

        String htmlContent = "<div style=\"background-color: #f5f5f5; padding: 20px; font-family: sans-serif; font-size: 16px; color: #333333;\"> \n" +
                "                <div style=\"max-width: 600px; margin: 0 auto; background-color: #90caf9; padding: 20px; border-radius: 5px;\">\n" +
                "                    <div style=\"text-align: center;\">\n" +
                "                        <div style=\"display: inline-block; background-color: #f5f5f5; padding: 10px 20px; border-radius: 5px;\">\n" +
                "                        <h1 style=\"font-size: 48px; margin-bottom: 0; margin-top: 0; color: #003554\">KN</h1>\n" +
                "                    </div>\n" +
                "\n" +
                "                </div>\n" +
                "                    <div style=\"text-align: center; margin-top: 20px;\">\n" +
                "                        <h1 style=\"font-size: 24px; margin-bottom: 0;\">Verification Code</h1>\n" +
                "                        <p style=\"font-size: 18px; margin-top: 0;\">Please enter the following code to verify your account.</p>\n" +
                "                    </div>\n" +
                "                    <div style=\"text-align: center; margin-top: 20px;\">\n" +
                "\n" +
                "                        <div style=\"display: inline-block; background-color: #003554; padding: 10px 20px; border-radius: 5px;\">\n" +
                "                            <h1 style=\"font-size: 24px; margin-bottom: 0; margin-top: 0; color: #ffffff \">"+ verificationCode + "</h1>\n" +
                "                        </div>\n" +
                "                    </div>\n" +
                "                    <div style=\"text-align: center; margin-top: 20px;\">\n" +
                "                        <p style=\"font-size: 18px; margin-top: 0;\">If you did not request this, please ignore this email.</p>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "            </div>";
        message.setContent(htmlContent, "text/html; charset=utf-8");

        mailSender.send(message);
    }
}
