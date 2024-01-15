package com.B2007186.AdviseNutrition.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterReq {
    private String userName;
    private String password;
    private String firstname;
    private String lastname;
    private String email;
}
