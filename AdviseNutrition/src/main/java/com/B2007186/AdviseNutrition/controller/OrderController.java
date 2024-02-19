package com.B2007186.AdviseNutrition.controller;

import com.B2007186.AdviseNutrition.dto.OrderForm;
import com.B2007186.AdviseNutrition.service.OrderService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @PostMapping("/new")
    public ResponseEntity<Long> insert(@RequestBody OrderForm obj) throws MessagingException, UnsupportedEncodingException {
        return ResponseEntity.ok().body(orderService.create(obj));
    }
    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws MessagingException, UnsupportedEncodingException {
        return ResponseEntity.ok().body(orderService.cancelOrder(id));

    }
}
