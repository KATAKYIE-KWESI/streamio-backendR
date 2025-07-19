package com.signup_streamioapp.streamioapp.streamservices;

import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void sendConfirmationEmail(String to, String token) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(buildConfirmationEmail(to, token), true);
            helper.setTo(to);
            helper.setSubject("Confirm your email");
            helper.setFrom("no-reply@streamio.com");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new IllegalStateException("Failed to send confirmation email");
        }
    }

    @Async
    public void sendResetPasswordEmail(String to, String token) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(buildResetPasswordEmail(to, token), true);
            helper.setTo(to);
            helper.setSubject("Reset your password");
            helper.setFrom("no-reply@streamio.com");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new IllegalStateException("Failed to send reset password email");
        }
    }

    private String buildConfirmationEmail(String email, String token) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;color:#0b0c0c;padding:20px\">\n" +
                "  <div style=\"text-align:center; margin-bottom:20px\">\n" +
                "    <img src=\"https://i.imgur.com/3lWcD4Z.jpeg\" alt=\"Streamio Logo\" style=\"max-width:150px;height:auto;border-radius:10px;\">\n"
                +
                "  </div>\n" +
                "  <p style=\"font-size:19px\">Hi " + email + ",</p>\n" +
                "  <p>Thanks for registering! Click the button below to activate your account:</p>\n" +
                "  <div style=\"margin:20px 0; text-align:center\">\n" +
                "    <a href=\"http://localhost:8080/api/v1/confirm-account?token=" + token +
                "\" style=\"background-color:#E50914;color:#fff;padding:10px 20px;text-decoration:none;border-radius:5px;font-size:18px\">Activate Now</a>\n"
                +
                "  </div>\n" +
                "  <p>This link will expire in 15 minutes.</p>\n" +
                "  <hr style=\"margin:30px 0;\">\n" +
                "  <p>Need help? Chat with us on WhatsApp:</p>\n" +
                "  <div style=\"text-align:center\">\n" +
                "    <a href=\"https://wa.me/233501234567\" target=\"_blank\" style=\"text-decoration:none;background-color:#25D366;color:white;padding:10px 20px;border-radius:5px;font-size:16px\">\n"
                +
                "      <img src=\"https://upload.wikimedia.org/wikipedia/commons/6/6b/WhatsApp.svg\" alt=\"WhatsApp\" style=\"height:20px;vertical-align:middle;margin-right:8px;\">\n"
                +
                "      Chat on WhatsApp\n" +
                "    </a>\n" +
                "  </div>\n" +
                "  <p style=\"margin-top:30px\">Thanks,<br>The Streamio Team</p>\n" +
                "</div>";
    }

    private String buildResetPasswordEmail(String email, String token) {
        String deepLink = "exp://172.20.10.3:19000/--/reset-password?token=" + token;

        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;color:#0b0c0c;padding:20px\">\n" +
                "  <h2 style=\"text-align:center\">Hi " + email + ",</h2>\n" +
                "  <p>You requested to reset your password. Click the button below:</p>\n" +
                "  <div style=\"text-align:center;margin:20px 0\">\n" +
                "    <a href=\"" + deepLink +
                "\" style=\"background-color:#E50914;color:#fff;padding:12px 24px;text-decoration:none;border-radius:5px;font-size:18px\">\n"
                +
                "      Reset Password\n" +
                "    </a>\n" +
                "  </div>\n" +
                "  <p>If the button doesn't work, copy and paste this link into your browser:</p>\n" +
                "  <p><a href=\"" + deepLink + "\">" + deepLink + "</a></p>\n" +
                "  <p>This link will expire in 15 minutes.</p>\n" +
                "  <hr style=\"margin:30px 0;\">\n" +
                "  <p>Need help? Chat with us on WhatsApp:</p>\n" +
                "  <div style=\"text-align:center\">\n" +
                "    <a href=\"https://wa.me/233550190950\" target=\"_blank\" style=\"text-decoration:none;background-color:#25D366;color:white;padding:10px 20px;border-radius:5px;font-size:16px\">\n"
                +
                "      <img src=\"https://upload.wikimedia.org/wikipedia/commons/6/6b/WhatsApp.svg\" alt=\"WhatsApp\" style=\"height:20px;vertical-align:middle;margin-right:8px;\">\n"
                +
                "      Chat on WhatsApp\n" +
                "    </a>\n" +
                "  </div>\n" +
                "  <p style=\"margin-top:30px\">Thanks,<br>The Streamio Team</p>\n" +
                "</div>";
    }
}