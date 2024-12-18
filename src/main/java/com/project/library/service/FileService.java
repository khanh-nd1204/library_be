package com.project.library.service;

import com.project.library.dto.ImageDTO;
import com.project.library.entity.ImageEntity;
import com.project.library.repository.ImageRepo;
import com.project.library.util.FileStoreException;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ImageRepo imageRepo;

    public String generateUniqueFileName(String originalFileName) {
        String fileExtension = "";
        int dotIndex = originalFileName.lastIndexOf('.');
        if (dotIndex >= 0) {
            fileExtension = originalFileName.substring(dotIndex);
        }
        return UUID.randomUUID() + fileExtension;
    }

    public ImageDTO store(MultipartFile file, String folder) throws URISyntaxException, IOException {
        String finalName = generateUniqueFileName(file.getOriginalFilename());
        URI uri = new URI(baseUri + folder + "/" + finalName);
        Path path = Paths.get(uri);
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, path,
                    StandardCopyOption.REPLACE_EXISTING);
        }
        ImageEntity image = new ImageEntity();
        image.setImageUrl("/upload/" + folder + "/" + finalName);
        image.setFolder(folder);
        imageRepo.save(image);
        return new ImageDTO(image.getId(), image.getImageUrl(), image.getFolder());
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
        List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png");
        boolean isValidExtension = allowedExtensions.stream().anyMatch(ext ->
                fileName.toLowerCase().endsWith("." + ext));
        if (!isValidExtension) {
            throw new FileStoreException("Invalid file type. Only allow " + allowedExtensions);
        }
    }

}
