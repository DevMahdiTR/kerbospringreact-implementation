package org.example.authentication.service.file;

import org.example.authentication.model.file.FileData;
import org.example.authentication.utility.CustomerResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    public FileData uploadedFile(@NotNull final MultipartFile file) throws IOException ;
    public ResponseEntity<byte[]> downloadFile(@NotNull final String filePath) throws IOException ;
    public void deleteFile(@NotNull final String filePath) throws IOException;
}
