package com.module6;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class GetObject {
    public static void main(String[] args) {
        ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create();
        Region region = Region.US_EAST_1;
        S3Client s3 = S3Client.builder()
                .region(region)
                .credentialsProvider(credentialsProvider)
                .build();

        String bucketName = "dilt-sandbox-java";
        String keyName = "airports.csv";

        printHeadObject(s3, bucketName, keyName);
        byte[] objectBytes = getObjectBytes(s3, buildObjectRequest(bucketName, keyName));
        writeBytesToFile(objectBytes, "./" + keyName);
        s3.close();
    }

    private static GetObjectRequest buildObjectRequest(String bucketName, String key){
        return GetObjectRequest
                .builder()
                .key(key)
                .bucket(bucketName)
                .build();
    }

    private static void printHeadObject(S3Client s3, String bucketName, String key) {
        HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        HeadObjectResponse headObjectResponse = s3.headObject(headObjectRequest);
        System.out.println("Object metadata: " + headObjectResponse.toString());
    }

    private static byte[] getObjectBytes(S3Client s3, GetObjectRequest objectRequest) {
        ResponseBytes<GetObjectResponse> objectBytes = s3.getObjectAsBytes(objectRequest);
        return objectBytes.asByteArray();
    }

    private static void writeBytesToFile(byte[] data, String path) {
        try {
            // Write the data to a local file.
            File myFile = new File(path);
            OutputStream os = new FileOutputStream(myFile);
            os.write(data);
            System.out.println("Successfully obtained bytes from an S3 object");
            os.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
