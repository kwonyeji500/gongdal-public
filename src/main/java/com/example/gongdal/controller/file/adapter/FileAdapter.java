package com.example.gongdal.controller.file.adapter;

import com.example.gongdal.config.exception.code.ErrorResponseCode;
import com.example.gongdal.config.exception.error.CustomRuntimeException;
import com.example.gongdal.entity.file.File;
import com.example.gongdal.repository.file.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileAdapter {
    private final FileRepository fileRepository;

    public File validFile(Long id) {
        return fileRepository.findById(id).orElseThrow(
                () -> new CustomRuntimeException(ErrorResponseCode.NOT_FOUND_FILE));
    }
}
