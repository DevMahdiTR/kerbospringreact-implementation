package org.example.authentication.service.file;

import lombok.extern.slf4j.Slf4j;
import org.example.authentication.exceptions.custom.FileDeletionException;
import org.example.authentication.model.file.FileData;
import org.example.authentication.utility.CustomerResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.UUID;

@Service
@Slf4j
public class FileServiceImpl implements FileService{

    private final Path FILE_SYSTEM_PATH = Paths.get("")
            .toAbsolutePath()
            .resolve("src")
            .resolve("main")
            .resolve("resources")
            .resolve("FileSystem");

    public FileData uploadedFile(@NotNull final MultipartFile file) {
        if (file.isEmpty()) {
            log.error("File is empty");
            throw new IllegalArgumentException("File is empty");
        }

        String originalFileName = file.getOriginalFilename();
        String extension = getExtension(originalFileName);
        String fileName = getFileNameWithoutExtension(originalFileName);
        String filePath = generateUniqueFilePath(fileName, extension);

        try {
            Files.copy(file.getInputStream(), Paths.get(filePath));
            log.info("File uploaded successfully: {}", originalFileName);
        } catch (IOException e) {
            log.error("Failed to upload file: {}", originalFileName, e);
            throw new RuntimeException("Failed to save file: " + originalFileName, e);
        }

        return FileData.builder()
                .name(originalFileName)
                .type(file.getContentType())
                .filePath(filePath)
                .build();
    }

    public ResponseEntity<byte[]> downloadFile(@NotNull final String filePath) throws IOException {
        byte[] fileBytes;
        try {
            fileBytes = Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            log.error("Failed to read file: {}",filePath);
            throw new IOException("Failed to read file: " + filePath, e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", new File(filePath).getName());
        headers.setContentType(MediaType.parseMediaType(determineContentType(filePath)));

        return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
    }

    @Override
    public void deleteFile(final @NotNull String filePath) throws FileDeletionException {
        File fileToDelete = new File(filePath);
        boolean isDeleted = fileToDelete.delete();
        if (!isDeleted) {
            log.error("Failed to delete the file at path: '" + filePath + "'. " + "Please ensure that the file exists and you have the necessary permissions.");
            throw new FileDeletionException("Failed to delete the file at path: '" + filePath + "'. " + "Please ensure that the file exists and you have the necessary permissions.");
        }
    }

    public String determineContentType(@NotNull String filePath) {

        String extension = filePath.substring(filePath.lastIndexOf(".") + 1).toLowerCase();

        HashMap<String, String> extensionToContentTypeMap = new HashMap<>();
        extensionToContentTypeMap.put("png", "image/png");
        extensionToContentTypeMap.put("jpg", "image/jpeg");
        extensionToContentTypeMap.put("jpeg", "image/jpeg");
        extensionToContentTypeMap.put("gif", "image/gif");
        extensionToContentTypeMap.put("bmp", "image/bmp");
        extensionToContentTypeMap.put("ico", "image/vnd.microsoft.icon");
        extensionToContentTypeMap.put("tiff", "image/tiff");
        return extensionToContentTypeMap.getOrDefault(extension, "application/octet-stream");
    }

    private String getExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1) {
            log.error("File name does not contain an extension");
            throw new IllegalArgumentException("File name does not contain an extension");
        }
        return fileName.substring(dotIndex);
    }
    private String getFileNameWithoutExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1) {
            log.error("File name does not contain an extension");
            throw new IllegalArgumentException("File name does not contain an extension");
        }
        return fileName.substring(0, dotIndex);
    }
    private String generateUniqueFilePath(String fileName, String extension) {
        String filePath;
        do {
            filePath = FILE_SYSTEM_PATH.resolve(fileName + UUID.randomUUID() + extension).toString();
        } while (Files.exists(Paths.get(filePath)));
        return filePath;
    }

}
