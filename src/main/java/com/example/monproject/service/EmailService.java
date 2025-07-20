package com.example.monproject.service;

import com.example.monproject.model.Donneur;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email : " + e.getMessage(), e);
        }
    }
    public void sendHtmlEmailToMultipleRecipients(Iterable<String> recipients, String subject, String htmlBody) {
        for (String email : recipients) {
            sendHtmlEmail(email, subject, htmlBody);
        }
    }
    public void sendRemerciementEmail(Donneur donneur, String typeCollecte, LocalDate date, LocalTime heure) {
        Context context = new Context();
        context.setVariable("nom", donneur.getNom());
        context.setVariable("prenom", donneur.getPrenom());
        context.setVariable("typeCollecte", typeCollecte);
        context.setVariable("date", date);
        context.setVariable("heure", heure);

        String htmlContent = templateEngine.process("mail/remercie", context);

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(donneur.getEmail());
            helper.setSubject("Merci pour votre don !");
            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'e-mail de remerciement", e);
        }
    }

}
