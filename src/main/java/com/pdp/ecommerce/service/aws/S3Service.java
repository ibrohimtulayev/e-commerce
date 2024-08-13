package com.pdp.ecommerce.service.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket-name}")
    private String BUCKET_NAME;

    public String uploadFile(MultipartFile multipartFile) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String todayDate = dateTimeFormatter.format(LocalDate.now());
        String fileName = "";
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(multipartFile.getContentType());
            objectMetadata.setContentLength(multipartFile.getSize());
            fileName = todayDate + "/" + multipartFile.getOriginalFilename();

            amazonS3.putObject(new PutObjectRequest(BUCKET_NAME, fileName, multipartFile.getInputStream(), objectMetadata));

        } catch (IOException e) {
            throw new RuntimeException("Error uploading file to S3", e);
        }
        return amazonS3.getUrl(BUCKET_NAME, fileName).toString();
    }

    public String uploadFile(byte[] fileData, String originalFileName, String contentType) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String todayDate = dateTimeFormatter.format(LocalDate.now());
        String fileName = "";
        try {
            // Convert byte array to InputStream
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileData);

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(contentType);
            objectMetadata.setContentLength(fileData.length);

            // Create file name using today's date and the original file name
            fileName = todayDate + "/" + originalFileName;

            // Upload the file to S3
            amazonS3.putObject(new PutObjectRequest(BUCKET_NAME, fileName, byteArrayInputStream, objectMetadata));

        } catch (Exception e) {
            throw new RuntimeException("Error uploading file to S3", e);
        }
        // Return the file URL
        return amazonS3.getUrl(BUCKET_NAME, fileName).toString();
    }

}
