package com.module6;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class S3Select {
    public static void main(String[] args) {
        ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create();
        Region region = Region.US_EAST_1;
        S3AsyncClient s3 = S3AsyncClient.builder()
                .region(region)
                .credentialsProvider(credentialsProvider)
                .build();

        String bucketName = "dilt-sandbox-java";
        String keyName = "airports.csv";

        selectContentFromObject(s3, bucketName, keyName);
        s3.close();
    }

    private static void selectContentFromObject(S3AsyncClient s3, String bucketName, String keyName) {
        InputSerialization inputSerialization = InputSerialization.builder()
                .csv(CSVInput.builder().fileHeaderInfo("Use").build())
                .build();

        OutputSerialization outputSerialization = OutputSerialization.builder()
                .csv(CSVOutput.builder().build())
                .build();

        SelectObjectContentRequest request = SelectObjectContentRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .expressionType("SQL")
                .expression("select * from s3object s where s.\"iso_country\" like '%US%' and s.\"type\" = 'closed' ")
                .inputSerialization(inputSerialization)
                .outputSerialization(outputSerialization)
                .build();

        ResponseHandler responseHandler = new ResponseHandler();

        try {
            s3.selectObjectContent(request, responseHandler).get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error: " + e.getMessage());
        }

        RecordsEvent response = (RecordsEvent) responseHandler.receivedEvents.stream()
                .filter(e -> e.sdkEventType() == SelectObjectContentEventStream.EventType.RECORDS)
                .findFirst()
                .orElse(null);

        System.out.println(response.payload().asUtf8String());
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

//    private static void writeBytesToFile(byte[] data, String path) {
//        try {
//            // Write the data to a local file.
//            File myFile = new File(path);
//            OutputStream os = new FileOutputStream(myFile);
//            os.write(data);
//            System.out.println("Successfully obtained bytes from an S3 object");
//            os.close();
//
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }

    private static class ResponseHandler implements SelectObjectContentResponseHandler {
        private SelectObjectContentResponse response;
        private List<SelectObjectContentEventStream> receivedEvents = new ArrayList<>();
        private Throwable exception;

        @Override
        public void responseReceived(SelectObjectContentResponse response) {
            this.response = response;
        }

        @Override
        public void onEventStream(SdkPublisher<SelectObjectContentEventStream> publisher) {
            publisher.subscribe(receivedEvents::add);
        }

        @Override
        public void exceptionOccurred(Throwable throwable) {
            exception = throwable;
        }

        @Override
        public void complete() {
        }
    }
}

