package com.example.backend.utils;

import java.io.File;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public class PictureUtils {
	
	 private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

	    public static String uploadFile(MultipartFile file) {
	        if (file != null && !file.isEmpty()) {
	            try {
	                File uploadDirFile = new File(UPLOAD_DIR);
	                if (!uploadDirFile.exists()) {
	                    uploadDirFile.mkdirs();
	                }

	                String filePath = UPLOAD_DIR + System.currentTimeMillis() + "_" + file.getOriginalFilename();
	                File convFile = new File(filePath);

	                file.transferTo(convFile);

	                return filePath;
	            } catch (IOException e) {
	                e.printStackTrace();
	                throw new RuntimeException("Failed to store file " + e.getMessage());
	            }
	        }
	        return null;
	    }

}
