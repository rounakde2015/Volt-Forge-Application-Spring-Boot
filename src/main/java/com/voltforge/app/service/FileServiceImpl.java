package com.voltforge.app.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImageInServer(String path, MultipartFile productImageFile) throws IOException {
        String originalProductImageFileName = productImageFile.getOriginalFilename();

        String randomFileID = UUID.randomUUID().toString();
        String productImageFileName = randomFileID.concat(originalProductImageFileName.substring(originalProductImageFileName.lastIndexOf('.')));
        String productImageFilePath = path + File.separator + productImageFileName;

        File folder = new File(path);

        if (!folder.exists()) {
            folder.mkdir();
        }

        Files.copy(productImageFile.getInputStream(), Paths.get(productImageFilePath));

        return productImageFileName;
    }
}
