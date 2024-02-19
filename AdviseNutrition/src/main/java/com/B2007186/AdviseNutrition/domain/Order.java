package com.B2007186.AdviseNutrition.domain;

import com.B2007186.AdviseNutrition.domain.Users.Client;
import com.B2007186.AdviseNutrition.domain.Users.Seller;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Entity
@Table(name="orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    private LocalDateTime orderDate;

    //enum(ORDER, CANCEL)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne
    @JoinTable(name = "order_client", joinColumns = @JoinColumn(name = "order_id"), inverseJoinColumns = @JoinColumn(name = "client_id"))
    private Client buyer;

    @ManyToOne
    @JoinTable(name = "order_seller", joinColumns = @JoinColumn(name = "order_id"), inverseJoinColumns = @JoinColumn(name = "seller_id"))
    private Seller seller;

    /**
     * Cancel Order
     */
    public void cancel() {
        // Validate Delivery Status
        if(delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("Order cannot be canceled once they are delivered.");
        }

        this.setStatus(OrderStatus.CANCEL);
        for(OrderDetail orderDetail: orderDetails) {
            orderDetail.cancel();
        }
    }
    public Double getTotalPrice() {
        Double totalPrice = 0.0;
        for(OrderDetail orderDetail: orderDetails) {
            totalPrice += orderDetail.getTotalPrice();
        }

        return totalPrice;
    }
}
