package com.B2007186.AdviseNutrition.dto;

import com.B2007186.AdviseNutrition.domain.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderForm {
    private HashMap<Long, Integer> productOrders;
}
