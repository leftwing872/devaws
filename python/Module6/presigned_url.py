import boto3

bucket = "dilt-sandbox"
object_key = "dev-capston.png"

url = boto3.client('s3', region_name='us-east-1').generate_presigned_url(
    ClientMethod='get_object',
    Params={'Bucket': bucket, 'Key': object_key},
    ExpiresIn=3600
)

print(url)