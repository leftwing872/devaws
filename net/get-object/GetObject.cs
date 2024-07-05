using Amazon.S3.Util;
using Amazon.S3;
using Amazon;
using Amazon.S3.Model;

public class GetObject
{
    public static async Task Main(string[] args)
    {
        const string bucketName = "dilt-sandbox-python";
        const string keyName = "airports.csv";

        IAmazonS3 s3Client = new AmazonS3Client(RegionEndpoint.USEast1);
        await PrintObjectMetadata(s3Client, bucketName, keyName);
        Task<bool> task = ReadObjectDataAsync(s3Client, bucketName, keyName);
        task.Wait();
        if(task.Result) { Console.WriteLine("The object downloaded successfully.");}
    }

    private static async Task<bool> ReadObjectDataAsync(IAmazonS3 s3Client, string bucketName, string objectName)
    {
        string responseBody = string.Empty;

        GetObjectRequest request = new GetObjectRequest
        {
            BucketName = bucketName,
            Key = objectName,
        };

        using GetObjectResponse response = await s3Client.GetObjectAsync(request);

        try
        {
            await response.WriteResponseStreamToFileAsync($"./{objectName}", true, CancellationToken.None);
            return response.HttpStatusCode == System.Net.HttpStatusCode.OK;
        }
        catch (AmazonS3Exception ex)
        {
            // If the bucket or the object do not exist
            Console.WriteLine($"Error: '{ex.Message}'");
            return false;
        }
    }

    public static async Task PrintObjectMetadata(IAmazonS3 client, string bucketName, string objectName) {
        List<ObjectAttributes> objectAttributes = new List<ObjectAttributes>();
        objectAttributes.Add("StorageClass");
        objectAttributes.Add("ObjectSize");

        GetObjectAttributesRequest request = new GetObjectAttributesRequest {
            BucketName = bucketName,
            Key = objectName,
            ObjectAttributes = objectAttributes
        };
        CancellationToken token = new CancellationToken();
        GetObjectAttributesResponse response = await client.GetObjectAttributesAsync(request, token);

        Console.WriteLine("Object storage class: " + response.StorageClass + " , Object size: " + response.ObjectSize);
    }
}
