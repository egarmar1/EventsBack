package com.kike.message.functions;

import com.kike.message.dto.EmailDto;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.UrlResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

@Configuration
@AllArgsConstructor
public class MessageFunctions {

    private static final Logger log = LoggerFactory.getLogger(MessageFunctions.class);
    private JavaMailSender javaMailSender;
    private WebClient.Builder webClientBuilder;

    @Bean
    public Consumer<EmailDto> sendEmail() {
        return (emailDto -> {
            try {
                // Crear el mensaje de correo
                MimeMessage message = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);

                // Configurar los detalles del correo
                helper.setFrom(emailDto.getSenderEmail());  // Remitente
                helper.setTo(emailDto.getRecipientEmail());  // Destinatario
                helper.setSubject(emailDto.getSubject());  // Asunto
                helper.setText(emailDto.getBody(), true);  // Cuerpo del correo (true para HTML)

                if (emailDto.getUrlImage() != null && !emailDto.getUrlImage().isEmpty()){
                    byte[] qrImage = fetchQrCodeWithJwt(emailDto.getUrlImage(), emailDto.getJwt().toString());
                    helper.addAttachment("qr-code.png", new ByteArrayDataSource(qrImage,"image/png"));
                }

                // Enviar el correo
                javaMailSender.send(message);
                log.info("Email sent successfully to: {}", emailDto.getRecipientEmail());

            } catch (Exception e) {
                log.error("Failed to send email to: {}", emailDto.getRecipientEmail(), e);
            }
        });


    }

    private byte[] fetchQrCodeWithJwt(String qrCodeUrl, String jwtToken){

        return webClientBuilder
                .build()
                .get()
                .uri(qrCodeUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer : " + jwtToken)
                .accept(MediaType.IMAGE_PNG)
                .retrieve()
                .bodyToMono(byte[].class)
                .block();
    }
}
