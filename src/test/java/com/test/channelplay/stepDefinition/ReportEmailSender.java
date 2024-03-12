package com.test.channelplay.stepDefinition;

import org.testng.annotations.AfterSuite;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import javax.activation.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class ReportEmailSender {
    @AfterSuite
    public void sendEmailWithAttachment() {
        try {
            Properties properties = new Properties();
            properties.put("mail.smtp.host", "smtp.sendgrid.net");
            properties.put("mail.smtp.port", "587"); // Use port 587 for TLS
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("apikey", "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");
                }
            });

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("noreply@channelplay.in"));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress("taufik@getassistive.com"));
            message.setSubject("Automation Test Report");

            // Create a multipart message
            Multipart multipart = new MimeMultipart();

            // Text message part
            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText("Hi Team,\n\nAutomation flow triggered. PFA the generated test report(Prod).\n\nThanks & Regards,\nQA Engineer.");
            multipart.addBodyPart(textBodyPart);

            // Attachment part
            MimeBodyPart attachmentBodyPart = new MimeBodyPart();

            String filePath = "C:\\Users\\MD Taufik\\Documents\\GitHub\\channelplay-automated-testing\\Reports\\sample.pdf"; // Replace with the actual file path

            File file = new File(filePath);

            if (file.exists()) {
                FileInputStream fileInputStream = new FileInputStream(file);

                attachmentBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(fileInputStream, "text/plain")));
                attachmentBodyPart.setFileName(file.getName());
                multipart.addBodyPart(attachmentBodyPart);
            } else {
                System.out.println("File not found at: " + filePath);
                return;
            }

            message.setContent(multipart);

            Transport.send(message);
            System.out.println("Email sent successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

