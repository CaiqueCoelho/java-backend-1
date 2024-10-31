package com.example.demo.services;

import com.example.demo.config.FileStorageConfig;
import com.example.demo.exceptions.FileStorageException;
import com.example.demo.exceptions.MyFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    Logger logger = Logger.getLogger(FileStorageService.class.getName());

    @Autowired
    public FileStorageService(FileStorageConfig fileStorageConfig) {
        try {
            // Get the root path of the project
            Path projectRootPath = Path.of(FileStorageConfig.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath())
                    .toAbsolutePath().normalize();

            // Append "uploadedFiles" directory to the root path
            this.fileStorageLocation = projectRootPath.resolve("uploadedFiles").toAbsolutePath().normalize();

            // Create directories if they don't exist
            logger.info("File storage location: " + this.fileStorageLocation);
            Files.createDirectories(this.fileStorageLocation);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Could not determine the file storage location.", e);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception ex) {
            throw new FileStorageException("Could not store the file. Error: " + ex.getMessage());
        }
        return fileName;
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new FileStorageException("File not found " + fileName);
            }
        } catch (Exception ex) {
            throw new MyFileNotFoundException("File not found: " + fileName, ex);
        }
    }
}
