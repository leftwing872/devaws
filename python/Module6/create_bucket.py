import boto3
import botocore

s3_client = boto3.client('s3', region_name='us-east-1')

def verify_bucket_name(bucket_name):
    try:
        s3_client.head_bucket(Bucket=bucket_name)
        raise SystemExit('This bucket has already been created.')
    except botocore.exceptions.ClientError as e:
        error_code = int(e.response['Error']['Code'])
        if error_code == 404:
            print('Existing Bucket Not Found, please proceed')
        if error_code == 403:
            raise SystemExit('This bucket has already owned by another AWS Account')


def create_bucket(bucket_name):
    s3_client.create_bucket(Bucket=bucket_name)

    waiter = s3_client.get_waiter('bucket_exists')
    waiter.wait(Bucket=bucket_name)
    print('Success!')

bucket_name = "dilt-sandbox-python-demo"

verify_bucket_name(bucket_name)
create_bucket(bucket_name)
#s3_client.delete_bucket(Bucket=bucket_name)
