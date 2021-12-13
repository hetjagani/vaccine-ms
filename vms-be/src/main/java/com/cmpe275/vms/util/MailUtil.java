package com.cmpe275.vms.util;

import com.cmpe275.vms.model.VerifyToken;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailUtil {
    private static final String USERNAME = "vms.cmpe275@gmail.com";
    private static final String PASSWORD = "Password@123";
    private static final String EMAIL_FROM = "vms.cmpe275@gmail.com";

    public static int sendMail(String text, String subject, String targetMail) throws MessagingException, AddressException {

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USERNAME, PASSWORD);
                    }
                });
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(EMAIL_FROM));
        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(targetMail)
        );
        message.setSubject(subject);
        message.setText(text);

        Transport.send(message);

        return 0;
    }

    public static String getVerificationMail(VerifyToken token, String verifyEndpoint) {
        return "Click on the below link to verify your account. \n" + verifyEndpoint + "?email=" + token.getEmail() + "&token=" + token.getToken();
    }
}
