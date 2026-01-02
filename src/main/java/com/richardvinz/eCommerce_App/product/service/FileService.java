package com.richardvinz.eCommerce_App.product.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    String uploadFile(String path, MultipartFile file) throws IOException;
}
