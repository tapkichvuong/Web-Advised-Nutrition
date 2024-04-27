package com.B2007186.AdviseNutrition.service;

import com.B2007186.AdviseNutrition.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
@Service
@RequiredArgsConstructor
public class ImageService {
    private final String FOLDER_PATH = "D:\\CTU\\NLNganh\\uploads\\";
    public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
        String filePath = FOLDER_PATH + fileName;
        return Files.readAllBytes(new File(filePath).toPath());
    }
}
