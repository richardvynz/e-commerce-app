package com.richardvinz.eCommerce_App.product.service;

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
    public String uploadFile(String path, MultipartFile file) throws IOException {
//        file name of current / original file
        String originalFileName = file.getOriginalFilename();
//        generate unique name
        String randomId = UUID.randomUUID().toString();
        String fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));
        String filePath = path + File.separator + fileName;

//        Check if file path exist if not create it
        File folder = new File(path);
        if(!folder.exists()){
            folder.mkdir();
        }
//        upload to server
        Files.copy(file.getInputStream(), Paths.get(filePath));

        return fileName;

    }
}
