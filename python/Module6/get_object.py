import boto3
import logging as logger

from botocore.exceptions import ClientError

s3_client = boto3.client('s3', region_name='us-east-1')

bucket = "dilt-sandbox-python"
object_key = "airports.csv"

def get_object(bucket_name, object_key):
    with open('airports_download.csv', 'wb') as f:
        s3_client.download_fileobj(bucket_name, object_key, f)

def head_object(bucket_name, object_key):
    response = s3_client.head_object(
        Bucket=bucket_name,
        Key=object_key
    )

    print(response)

head_object(bucket, object_key)
get_object(bucket, object_key)