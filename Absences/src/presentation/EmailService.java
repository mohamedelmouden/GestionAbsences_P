package presentation;

import java.util.Properties;
import java.util.Random;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailService {

    // ✅ Mets ton adresse Gmail et mot de passe d'application ici
    private static final String EMAIL_EXPEDITEUR = "elmouden0778@gmail.com";
    private static final String MOT_DE_PASSE_APP = "hypfcmpwbjeezkus"; // mot de passe d'application Gmail

    // Générer un mot de passe aléatoire 8 caractères
    public static String genererMotDePasse() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++)
            sb.append(chars.charAt(random.nextInt(chars.length())));
        return sb.toString();
    }

    // Envoyer le nouveau mot de passe par email
    public static boolean envoyerNouveauMotDePasse(String destinataire, String username, String nouveauMdp) {
        Properties props = new Properties();
        props.put("mail.smtp.auth",            "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host",            "smtp.gmail.com");
        props.put("mail.smtp.port",            "587");
        props.put("mail.smtp.ssl.trust",       "smtp.gmail.com");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_EXPEDITEUR, MOT_DE_PASSE_APP);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_EXPEDITEUR));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinataire));
            message.setSubject("Réinitialisation de votre mot de passe — Gestion RH");

            String contenu =
                "<html><body style='font-family:Segoe UI;background:#0d1117;color:#e6edf3;padding:30px'>" +
                "<h2 style='color:#388bfd'>Gestion des Absences — RH</h2>" +
                "<p>Bonjour <b>" + username + "</b>,</p>" +
                "<p>Votre mot de passe a été réinitialisé.</p>" +
                "<p>Votre nouveau mot de passe est :</p>" +
                "<h2 style='background:#1e252e;padding:12px 24px;border-radius:8px;" +
                "border:1px solid #388bfd;color:#3fb950;letter-spacing:4px'>" + nouveauMdp + "</h2>" +
                "<p style='color:#8b949e'>Connectez-vous et changez ce mot de passe dès que possible.</p>" +
                "<hr style='border-color:#30363d'/>" +
                "<p style='color:#8b949e;font-size:12px'>Gestion RH — Système automatique</p>" +
                "</body></html>";

            message.setContent(contenu, "text/html; charset=utf-8");
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}