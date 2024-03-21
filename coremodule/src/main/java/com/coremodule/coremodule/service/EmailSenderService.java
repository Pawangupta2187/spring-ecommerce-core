package com.coremodule.coremodule.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("emailSenderService")
//@EnableAsync
public class EmailSenderService {

    private JavaMailSender javaMailSender;

    @Autowired
    public EmailSenderService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendEmail(SimpleMailMessage email) {

        try{
            javaMailSender.send(email);
            System.out.println("Mail Sent");
        }
        catch (MailException mailException)
        {
            mailException.printStackTrace();
            throw new RuntimeException(mailException.getMessage());
        }

    }
    public SimpleMailMessage CreateBodyForMail(String setTo,String subject,String text)
    {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(setTo);
        mailMessage.setSubject(subject);
        mailMessage.setFrom("pawan.gupta@tothenew.com");
        mailMessage.setText(text);
        return mailMessage;

    }


}