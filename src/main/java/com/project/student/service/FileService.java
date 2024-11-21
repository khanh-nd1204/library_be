package com.project.student.service;

import com.project.student.dto.FileDTO;
import com.project.student.util.FileStoreException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {

    @Value("${file.upload.base-uri}")
    private String baseUri;

    public String generateUniqueFileName(String originalFileName) {
        String fileExtension = "";
        int dotIndex = originalFileName.lastIndexOf('.');
        if (dotIndex >= 0) {
            fileExtension = originalFileName.substring(dotIndex);
        }
        return UUID.randomUUID() + fileExtension;
    }

    public FileDTO store(MultipartFile file, String folder) throws URISyntaxException, IOException {
        String finalName = generateUniqueFileName(file.getOriginalFilename());
        URI uri = new URI(baseUri + folder + "/" + finalName);
        Path path = Paths.get(uri);
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, path,
                    StandardCopyOption.REPLACE_EXISTING);
        }
        return new FileDTO(finalName, baseUri + folder + "/" + finalName);
    }

    public void validate(MultipartFile file, String folder) throws URISyntaxException {
        if (file == null || file.isEmpty()) {
            throw new FileStoreException("File is null or empty");
        }
        if (folder == null || folder.isEmpty()) {
            throw new FileStoreException("Folder upload is null or empty");
        }

        URI uri = new URI(baseUri + folder);
        Path path = Paths.get(uri);
        File tmpDir = new File(path.toString());
        if (!tmpDir.isDirectory()) {
            throw new FileStoreException("Folder upload not exist");
        }

        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png");
        boolean isValidExtension = allowedExtensions.stream().anyMatch(ext ->
                fileName.toLowerCase().endsWith("." + ext));
        if (!isValidExtension) {
            throw new FileStoreException("Invalid file type. Only allow " + allowedExtensions);
        }
    }

}
