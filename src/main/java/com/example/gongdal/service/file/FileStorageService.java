package com.example.gongdal.service.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.example.gongdal.controller.file.adapter.FileAdapter;
import com.example.gongdal.entity.file.File;
import com.example.gongdal.repository.file.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageService {
    private final AmazonS3 amazonS3;
    private final FileRepository fileRepository;
    private final FileAdapter fileAdapter;
    @Value("${aws.bucket}")
    private String bucket;

    public File saveFile(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        String randomKey = UUID.randomUUID().toString() + extension;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        try {
            amazonS3.putObject(bucket, randomKey, multipartFile.getInputStream(), metadata);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return fileRepository.save(
                File.createFile(originalFilename, amazonS3.getUrl(bucket, originalFilename).toString(), randomKey));
    }

    public byte[] downloadFile(Long id) {
        File file = fileAdapter.validFile(id);

        S3Object object = amazonS3.getObject(new GetObjectRequest(bucket, file.getKey()));
        try {
            return IOUtils.toByteArray(object.getObjectContent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteFile(File file) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, file.getKey()));

        fileRepository.delete(file);
    }


}
