package com.B2007186.AdviseNutrition.controller;

import com.B2007186.AdviseNutrition.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/image")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;
    @GetMapping("{fileName}")
    public ResponseEntity<byte[]> downloadImageFromFileSystem(@PathVariable String fileName) throws IOException {
        String[] file = fileName.split("\\.");
        String type = file[1];
        String contentType = "image/" + type;
        System.out.println(contentType);
        byte[] imageData=imageService.downloadImageFromFileSystem(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(contentType))
                .body(imageData);

    }
}
