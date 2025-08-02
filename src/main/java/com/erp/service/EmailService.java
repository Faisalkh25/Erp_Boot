package com.erp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmployeeDetails(String toPersonalEmail, String companyEmail, String empCode, String password) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toPersonalEmail);
            helper.setSubject("Welcome to Barrownz - Your Login Details");

            String content = "<html><body>"
                    + "<div style='font-family: Arial, sans-serif;'>"
                    + "<h2>Welcome to Barrownz Group Pvt. Ltd.</h2>"
                    + "<p>Dear Employee,</p>"
                    + "<p>We are pleased to welcome you to our organization. Please find your login credentials below:</p>"
                    + "<hr>"
                    + "<p><strong>👤 Employee Code:</strong> " + empCode + "<br>"
                    + "<strong>📧 Company Email:</strong> " + companyEmail + "<br>"
                    + "<strong>🔐 Temporary Password:</strong> " + password + "</p>"
                    + "<hr>"
                    + "<p>📌 Please make sure to change your password after your first login.</p>"
                    + "<p>If you face any issues accessing your account, feel free to contact the IT Helpdesk.</p>"
                    + "<br><p>Best regards,<br>"
                    + "HR Department<br>"
                    + "Barrownz Tech Pvt. Ltd.<br>"
                    + "📞 +91-9305667841 | 🌐 <a href='https://barrownz.com'>www.barrownz.com</a></p>"
                    + "</div></body></html>";

            helper.setText(content, true);
            mailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
