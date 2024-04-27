package com.B2007186.AdviseNutrition.service;

import com.B2007186.AdviseNutrition.domain.Address;
import com.B2007186.AdviseNutrition.domain.Users.User;
import com.B2007186.AdviseNutrition.dto.AddressDTO;
import com.B2007186.AdviseNutrition.dto.InfoReq;
import com.B2007186.AdviseNutrition.dto.InfoRes;
import com.B2007186.AdviseNutrition.repository.TokenRepository;
import com.B2007186.AdviseNutrition.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final String FOLDER_PATH = "D:\\CTU\\NLNganh\\uploads\\";
    private final String BASE_IMAGE_URL = "http://localhost:8080/api/v1/image/";

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailSenderService emailSenderService;

    private static String generateUniqueFilename() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String timeStamp = dateFormat.format(new Date());
        return timeStamp + "_" + UUID.randomUUID().toString().substring(0, 4); // Append a portion of UUID
    }
    public Optional<InfoRes> getProfile() {

        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUserName(username).get();
        String[] filePath=user.getAvatar().split("\\\\");
        String filename = filePath[filePath.length - 1];
        String avatarLink = BASE_IMAGE_URL + filename;
        AddressDTO addressDTO = AddressDTO.builder()
                .street(user.getAddress().getStreet())
                .ward(user.getAddress().getWard())
                .district(user.getAddress().getDistrict())
                .province(user.getAddress().getProvince())
                .build();
        var info = InfoRes.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .avatar(avatarLink)
                .email(user.getEmail())
                .Birth(user.getBirth())
                .gender(user.getGender())
                .phone(user.getPhone())
                .address(addressDTO)
                .build();
        return Optional.of(info);
    }

    public Optional<InfoRes> updateProfile(InfoReq profile) throws IOException {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUserName(username).get();

        String originalFilename = profile.getAvatar().getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = generateUniqueFilename() + fileExtension;
        String filePath = FOLDER_PATH + newFilename;
        profile.getAvatar().transferTo(new File(filePath));
        Address address = Address.builder()
                .street(profile.getStreet())
                .ward(profile.getWard())
                .district(profile.getDistrict())
                .province(profile.getProvince())
                .build();
        user.setAddress(address);
        user.setFirstName(profile.getFirstName());
        user.setLastName(profile.getLastName());
        user.setPhone(profile.getPhone());
        user.setBirth(profile.getBirth());
        user.setGender(profile.getGender());
        user.setAvatar(filePath);
        userRepository.save(user);
        String avatarLink = BASE_IMAGE_URL + newFilename;
        AddressDTO addressDTO = AddressDTO.builder()
                .street(user.getAddress().getStreet())
                .ward(user.getAddress().getWard())
                .district(user.getAddress().getDistrict())
                .province(user.getAddress().getProvince())
                .build();
        var info = InfoRes.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .avatar(avatarLink)
                .email(user.getEmail())
                .Birth(user.getBirth())
                .gender(user.getGender())
                .phone(user.getPhone())
                .address(addressDTO)
                .build();
        return Optional.of(info);
    }
}
