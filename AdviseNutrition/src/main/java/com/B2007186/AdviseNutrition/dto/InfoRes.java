package com.B2007186.AdviseNutrition.dto;

import com.B2007186.AdviseNutrition.domain.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InfoRes {
    private String email;
    private String phone;
    private String userName;
    private String firstName;
    private String lastName;
    private String avatar;
    private Gender gender;
    private LocalDate Birth;
    private AddressDTO address;
}
