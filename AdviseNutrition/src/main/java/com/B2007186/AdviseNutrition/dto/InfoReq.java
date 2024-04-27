package com.B2007186.AdviseNutrition.dto;


import com.B2007186.AdviseNutrition.domain.Address;
import com.B2007186.AdviseNutrition.domain.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InfoReq {
    private String phone;
    private String firstName;
    private String lastName;
    private MultipartFile avatar;
    private Gender gender;
    private LocalDate Birth;
    private String street;
    private String ward;
    private String district;
    private String province;
}
