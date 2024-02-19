package com.B2007186.AdviseNutrition.service;

import com.B2007186.AdviseNutrition.domain.Order;
import com.B2007186.AdviseNutrition.domain.Users.User;
import com.B2007186.AdviseNutrition.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailSenderService {

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    public void sendVerificationEmail(User user)
                    throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "huyb2007186@student.ctu.edu.vn";
        String senderName = "Huy MTV";
        String subject = "Please verify your registration";
        String content = "Dear [[NAME]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Huy MTV";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[NAME]]", user.getFullName());
        String verifyURL = "http://localhost:8080" + "/api/v1/auth/verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);

    }

    public void sendPurchasedConfirmationEmail(Order order) throws MessagingException, UnsupportedEncodingException {
        String userame = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUserName(userame).get();
        String toAddress = user.getEmail();
        String fromAddress = "huyb2007186@student.ctu.edu.vn";
        String senderName = "Huy MTV";
        String subject = "Thank you for your purchase order id [[ORDER]]";
        String content = "Hello  [[NAME]],<br>"
                + "Thank you for your recent transaction on our website<br>"
                + "Here are the details of your order:<br><br>"
                + "- Order Number: [[Order Number]]<br>"
                + "- Date of Purchase: [[Purchase Date]]<br>"
                + "- Total Amount: [[Total Amount]]<br><br>"
                + "Your order is now being prepared for shipment. You can expect to receive a confirmation email with tracking information once your package has been dispatched.<br><br>"
                + "Should you have any questions or concerns regarding your order, please feel free to reach out to our customer service team at [[Customer Service Email]] or call us at [[Customer Service Phone Number]]. Our dedicated team is always here to assist you.<br><br>"
                + "Refunds and/or returns may be granted for many products on our website. Learn more about our website refunds here, or request a refund here."
                + "Thank you,<br>"
                + "Huy MTV";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        subject = subject.replace("[[ORDER]]", order.getId().toString());
        content = content.replace("[[NAME]]", user.getFullName());
        content = content.replace("[[Order Number]]", order.getId().toString());
        content = content.replace("[[Purchase Date]]", order.getOrderDate().toString());
        content = content.replace("[[Total Amount]]", order.getTotalPrice().toString());
        content = content.replace("[[Customer Service Email]]", "support.noxus@runeterra.com");
        content = content.replace("[[Customer Service Phone Number]]", "0123 456 789");
        
        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }

    public void sendCanceledConfirmationEmail(Order order) throws MessagingException, UnsupportedEncodingException {
        String userame = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUserName(userame).get();
        String toAddress = user.getEmail();
        String fromAddress = "huyb2007186@student.ctu.edu.vn";
        String senderName = "Huy MTV";
        String subject = "Order Cancellation Confirmation [[ORDER]]";
        String content = "Hello [[Customer's Name]],<br><br>"
                + "We regret to inform you that we have received your request to cancel the following order:<br><br>"
                + "- Order Number: [[Order Number]]<br>"
                + "- Date of Purchase: [[Purchase Date]]<br>"
                + "- Product: [[Product Name]]<br><br>"
                + "We understand that circumstances may change, and we are here to assist you with your request.<br><br>"
                + "To proceed with the cancellation process and ensure a smooth refund, please confirm your request by replying to this email or contacting our customer service team at [[Customer Service Email]] or by phone at [[Customer Service Phone Number]].<br><br>"
                + "Please note that if your order has already been processed and shipped, we may not be able to cancel it immediately. In such cases, you may be required to return the item upon receipt in accordance with our return policy.<br><br>"
                + "We apologize for any inconvenience this may cause and appreciate your understanding.<br><br>"
                + "Thank you for choosing our service. We value your patronage and look forward to serving you in the future.<br><br>"
                + "Thank you,<br><br>"
                + "Huy MTV<br>";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        subject = subject.replace("[[ORDER]]", order.getId().toString());
        content = content.replace("[[NAME]]", user.getFullName());
        content = content.replace("[[Order Number]]", order.getId().toString());
        content = content.replace("[[Purchase Date]]", order.getOrderDate().toString());
        content = content.replace("[[Customer Service Email]]", "support.noxus@runeterra.com");
        content = content.replace("[[Customer Service Phone Number]]", "0123 456 789");

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }
}
