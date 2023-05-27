package com.example.mediaServices.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MediaTest {

    private final Storage storage;



    public String uploadFile( byte[] bytes) {
        System.out.println("testServices");
        String bucketName="test";
        String objectName="imgtest";
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        Blob blob = storage.create(blobInfo, bytes);

        return blob.getMediaLink();
    }

    public String getFileUrl(String bucketName, String objectName) {
        return String.format("https://storage.googleapis.com/%s/%s", bucketName, objectName);
    }

    public byte[] downloadFile(String bucketName, String objectName) throws IOException {
        Blob blob = storage.get(BlobId.of(bucketName, objectName));

        return blob.getContent();
    }
    public String print(){
        return "hello there";
    }
}
