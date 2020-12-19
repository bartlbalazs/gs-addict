package hu.bartl.gsAddict.service;

import com.github.mustachejava.DefaultMustacheFactory;
import hu.bartl.gsAddict.config.EmailConfig;
import hu.bartl.gsAddict.model.Ad;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@AllArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender emailSender;

    private final EmailConfig emailConfig;

    @SneakyThrows
    public void sendNotification(Set<Ad> newAds) {
        if (newAds.isEmpty()) {
            log.info("No new ads found.");
            return;
        }

        log.info("New ads: {}", newAds.stream().map(Ad::getName).collect(Collectors.joining(", ")));

        var htlm = builtMailContent(newAds);
        var message = buildMailMessage(htlm);
        emailSender.send(message);

        log.debug("Notification sent.");
    }

    private String builtMailContent(Set<Ad> ads) throws IOException {
        var mf = new DefaultMustacheFactory();
        var m = mf.compile("mail_template.html");
        var writer = new StringWriter();

        var scopes = new HashMap<String, Object>();

        List<Ad> adsList = ads.stream()
                .sorted(Comparator.comparing(Ad::getName))
                .collect(Collectors.toList());

        scopes.put("ads", adsList);
        m.execute(writer, scopes).flush();
        return writer.toString();
    }

    private MimeMessage buildMailMessage(String htlm) throws MessagingException {
        var message = emailSender.createMimeMessage();
        var helper = new MimeMessageHelper(message, UTF_8.name());

        helper.setFrom(emailConfig.getReceiver());
        helper.setTo(emailConfig.getReceiver());
        message.setSubject("GS új hirdetések - " + DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now()), UTF_8.name());
        message.setContent(htlm, "text/html");
        return message;
    }
}
