package com.bdr.backend.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.web.multipart.MultipartFile;

public class PictureUtils {
	
	private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/src/main/resources/static/uploads/";

	    public static String uploadFile(MultipartFile file) {
	        if (file != null && !file.isEmpty()) {
	            try {
	                File uploadDirFile = new File(UPLOAD_DIR);
	                if (!uploadDirFile.exists()) {
	                    uploadDirFile.mkdirs();
	                }

	                Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
	                File convFile = new File(path.toString());
	                
	                // save the file to the uploads directory
	                file.transferTo(convFile);
	                
	                //return uploads + the file name to store it in the database
	                return "/uploads/" + file.getOriginalFilename();
	                
	            } catch (IOException e) {
	                e.printStackTrace();
	                throw new RuntimeException("Failed to store file " + e.getMessage());
	            }
	        }
	        return null;
	    }

}
