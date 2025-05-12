package com.kokos.onlineshop.service;

import com.kokos.onlineshop.domain.entity.EmailTemplateName;
import com.kokos.onlineshop.domain.entity.Order;
import com.kokos.onlineshop.domain.entity.OrderItem;
import com.kokos.onlineshop.domain.entity.Product;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public void sendAccountActivationToken(String email,
                                           String fullName,
                                           EmailTemplateName emailTemplateName,
                                           String token) throws MessagingException {
        String templateName = emailTemplateName.name();
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED, StandardCharsets.UTF_8.name());

        helper.setFrom("ivan.kokoshnikov@gmail.com");
        helper.setTo(email);
        helper.setSubject(emailTemplateName.getSubject());

        Map<String, Object> properties = new HashMap<>();
        properties.put("activation_code", token);
        properties.put("username", fullName);
        Context context = new Context();
        context.setVariables(properties);
        String text = templateEngine.process(templateName, context);

        helper.setText(text, true);

        mailSender.send(message);
    }

    public void sendOrderConfirmation(String email,
                                      String fullName,
                                      EmailTemplateName emailTemplateName,
                                      Order order) throws MessagingException {
        String templateName = emailTemplateName.name();
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED, StandardCharsets.UTF_8.name());

        helper.setFrom("ivan.kokoshnikov@gmail.com");
        helper.setTo(email);
        helper.setSubject(emailTemplateName.getSubject());

        Map<String, Object> properties = new HashMap<>();
        properties.put("username", fullName);
        properties.put("total_amount", order.getTotalAmount());
        Set<String> orderItemNames = order.getOrderItems().stream()
                .map(OrderItem::getProduct)
                .map(Product::getTitle)
                .collect(Collectors.toSet());
        properties.put("order_items", orderItemNames);
        Context context = new Context();
        context.setVariables(properties);
        String text = templateEngine.process(templateName, context);

        helper.setText(text, true);

        mailSender.send(message);
    }
}
