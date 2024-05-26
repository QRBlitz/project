package kz.iitu.diploma.project.service.impl;

import kz.iitu.diploma.project.model.filestorage.FileStorageException;
import kz.iitu.diploma.project.model.filestorage.FileStorageProperties;
import kz.iitu.diploma.project.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    private final FileStorageProperties fileStorageProperties;

    @Override
    public String storeFile(MultipartFile file, String folder) {
        String fileName;
        if (file.getOriginalFilename().split("\\.").length > 1) {
            fileName = UUID.randomUUID() + "." + file.getOriginalFilename().split("\\.")[1];
        } else {
            fileName = UUID.randomUUID() + ".png";
        }
        try {
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence $fileName");
            }
            Path fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir() + "/" + folder).toAbsolutePath().normalize();
            try {
                Files.createDirectories(fileStorageLocation);
            } catch (Exception ex) {
                log.error("Could not create the directory where the uploaded files will be stored.{}", ex.getMessage());
                throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
            }
            Path targetLocation = fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            throw new FileStorageException("Could not store file $fileName. Please try again!", e);
        }
    }

}
