package com.B2007186.AdviseNutrition.service;

import com.B2007186.AdviseNutrition.domain.*;
import com.B2007186.AdviseNutrition.domain.Users.Client;
import com.B2007186.AdviseNutrition.domain.Users.User;
import com.B2007186.AdviseNutrition.dto.OrderForm;
import com.B2007186.AdviseNutrition.repository.OrderRepository;
import com.B2007186.AdviseNutrition.repository.ProductRepository;
import com.B2007186.AdviseNutrition.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final EmailSenderService emailSenderService;
    /**
     * Register Order
     */
    @Transactional
    public Long create(OrderForm form) throws MessagingException, UnsupportedEncodingException {
        //Retrieve Entities
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUserName(username).get();


        //Create Delivery Info
        Delivery delivery = new Delivery();
        delivery.setAddress(user.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        //Create Order Item Info
        HashMap<Long, Integer> productOrders = form.getProductOrders();
        List<OrderDetail> orderDetails = new ArrayList<>();
        productOrders.forEach((productId, quantity)-> {
            Product product = productRepository.findById(productId).get();
            OrderDetail orderDetail = OrderDetail.builder()
                    .product(product)
                    .unitPrice(product.getPrice())
                    .quantity(quantity)
                    .build();
            product.decreaseStock(quantity);
            orderDetails.add(orderDetail);
        });

        //Create Order
        Order order = Order.builder()
                .buyer((Client) user)
                .delivery(delivery)
                .orderDetails(orderDetails)
                .orderDate(LocalDateTime.now())
                .build();

        //Register Order
        orderRepository.save(order);
        emailSenderService.sendPurchasedConfirmationEmail(order);
        return order.getId();
    }


    /**
     * Cancel Order
     */
    @Transactional
    public String cancelOrder(Long orderId) throws MessagingException, UnsupportedEncodingException {
        //Retrieve Order Entity
        Order order = orderRepository.findById(orderId).get();

        //Cancel Order
        order.cancel();
        emailSenderService.sendCanceledConfirmationEmail(order);

        return "Order has been canceled successfully";
    }
}
