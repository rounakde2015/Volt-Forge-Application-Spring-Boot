package com.voltforge.app.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    String uploadImageInServer(String path, MultipartFile productImageFile) throws IOException;
}
