using Amazon.S3;
using Amazon;
using Amazon.S3.Model;
using Amazon.S3.Util;


public class CreateBucket
{
    public static async Task Main(string[] args)
    {
        IAmazonS3 s3Client = new AmazonS3Client(RegionEndpoint.USEast1);
        String bucketName = "dilt-sandbox-dotnet-demo";
        bucketExists(s3Client, bucketName);
        Task<bool> task = createBucketAsync(s3Client, bucketName);
        task.Wait();
        if(task.Result) { Console.WriteLine("Bucket created successfully.");}
    }

    private static void bucketExists(IAmazonS3 s3Client, string bucketName)
    {
        bool exists = false;

        exists = AmazonS3Util.DoesS3BucketExistV2Async(s3Client, bucketName).Result;
        if(exists) {
            Console.WriteLine("This bucket already exists.");
            System.Environment.Exit(1);
        }
        else 
        {
            Console.WriteLine("The bucket does not exist.");
        }
    }

    private static async Task<bool> createBucketAsync(IAmazonS3 s3Client, string bucketName)
    {
        try
        {
            var putBucketRequest = new PutBucketRequest
            {
                BucketName = bucketName,
                UseClientRegion = true,
            };

            var putBucketResponse = await s3Client.PutBucketAsync(putBucketRequest);
            return putBucketResponse.HttpStatusCode == System.Net.HttpStatusCode.OK;
        }
        catch (AmazonS3Exception ex)
        {
            Console.WriteLine($"Error creating bucket: '{ex.Message}'");
            return false;
        }
    }
}

