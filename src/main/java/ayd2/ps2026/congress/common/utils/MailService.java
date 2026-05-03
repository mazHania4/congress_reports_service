package ayd2.ps2026.congress.common.utils;

import ayd2.ps2026.congress.common.config.AppProperties;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 *
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-08-30
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final AppProperties appProperties;
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    private static final String THYMELEAF_NOTIFICATION = "NotificationTemp";

    @Async
    public void sendNotificationEmail(String recipientEmail, String title, String message) {
        Context context = new Context();

        context.setVariable("title", title);
        context.setVariable("message", message);

        String html = templateEngine.process(THYMELEAF_NOTIFICATION, context);

        try {
            MimeMessage mimeMessage = buildMimeMessage(recipientEmail, title, html);
            mailSender.send(mimeMessage);

        } catch (MessagingException | MailException e) {
            log.error("Error al enviar el correo de notificación a {}", recipientEmail, e);
        }
    }


    /**
     * Construye un mensaje MIME listo para ser enviado por correo electrónico.
     * 
     * Este método configura un mensaje con contenido HTML, destinatario, asunto
     * y remitente personalizado utilizando el nombre de la aplicación.
     *
     */
    private MimeMessage buildMimeMessage(String to, String subject, String htmlContent) throws MessagingException {
        // crea un nuevo mensaje mime vacío
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        // envuelve el mensaje con un helper para configurar su contenido
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        // establece el contenido html del mensaje
        helper.setText(htmlContent, true);

        // define el destinatario del correo
        helper.setTo(to);

        // establece el asunto del correo
        helper.setSubject(subject);

        // define el remitente usando el nombre de la aplicación y el correo configurado
        helper.setFrom("Congress platform <" + appProperties.getMailFrom() + ">");

        // retorna el mensaje construido
        return mimeMessage;
    }

}
