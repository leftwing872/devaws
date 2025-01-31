package com.module6;



import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;
import java.net.URISyntaxException;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketResponse;

public class CreateBucket {

        public static void main(String[] args) throws URISyntaxException {

            String bucketName = "dilt-sandbox-java-demo";

            System.out.format("Creating a bucket named %s\n", bucketName);
            ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create();
            Region region = Region.US_EAST_1;
            S3Client s3 = S3Client.builder()
                    .region(region)
                    .credentialsProvider(credentialsProvider)
                    .build();

            if(!bucketExisting(s3, bucketName))
            {
                createBucket (s3, bucketName);
            }
            //deleteBucket(s3, bucketName);
            s3.close();
        }
        private static boolean bucketExisting(S3Client s3, String bucketName) {
            boolean exists = false;
            try {
                HeadBucketRequest request = HeadBucketRequest.builder()
                        .bucket(bucketName)
                        .build();
                HeadBucketResponse result = s3.headBucket(request);
                if (result.sdkHttpResponse().statusCode() == 200) {
                    System.out.println("Bucket exists!");
                    exists = true;
                }

            } catch (AwsServiceException awsEx) {
                switch(awsEx.statusCode()) {
                    case 404:
                        System.out.println("No such bucket existing");
                        break;
                    case 400:
                        System.out.println("Attempted to access a bucket from a Region other than where it exists");
                        exists = true;
                        break;
                    case 403:
                        System.out.println("Permission errors in accessing bucket");
                        exists = true;
                        break;
                }
            }
            return exists;
        }

        public static void createBucket( S3Client s3Client, String bucketName) {

            try {
                S3Waiter s3Waiter = s3Client.waiter();
                CreateBucketRequest bucketRequest = CreateBucketRequest.builder()
                        .bucket(bucketName)
                        .build();

                s3Client.createBucket(bucketRequest);

                HeadBucketRequest bucketRequestWait = HeadBucketRequest.builder()
                        .bucket(bucketName)
                        .build();

                // Wait until the bucket is created and print out the response.
                WaiterResponse<HeadBucketResponse> waiterResponse = s3Waiter.waitUntilBucketExists(bucketRequestWait);
                waiterResponse.matched().response().ifPresent(System.out::println);
                System.out.println(bucketName +" is ready");

            } catch (S3Exception e) {
                System.err.println(e.toString());
                System.err.println(e.awsErrorDetails().errorMessage());
                System.exit(1);
            }
        }

        public static void deleteBucket( S3Client s3Client, String bucketName) {

        try {
            DeleteBucketRequest deleteBucketRequest = DeleteBucketRequest.builder()
                    .bucket(bucketName)
                    .build();



            s3Client.deleteBucket(deleteBucketRequest);

            System.out.println(bucketName +" is deleted");

        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
}
