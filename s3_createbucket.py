import boto3
import botocore
import pprint
from datetime import datetime
pp = pprint.PrettyPrinter(indent=4)

def verify_bucket_name(s3Client, bucket):
    try:
        ## 버킷이 AWS에 이미 존재하는지 확인
        s3Client.head_bucket(Bucket=bucket)
        # 이전 명령어가 성공하면 버킷이 이미 계정에 존재합니다.
        raise SystemExit('This bucket has already been created')
    except botocore.exceptions.ClientError as e:
        error_code = int(e.response['Error']['Code'])
        if error_code == 404:
            ## 404 오류 코드를 받으면 해당 이름의 버킷이
            ## AWS 내 어디에도 존재하지 않습니다.
            print('Existing Bucket Not Found, please proceed')
        if error_code == 403:
            ## 403 오류 코드를 받으면 해당 이름의 버킷이 
            ## 다른 AWS 계정에 존재합니다.
            raise SystemExit('This bucket has already owned by another AWS Account')

def create_bucket(bucket_name):
    # 사용자 지정 세션을 사용하여 S3 리소스 생성
    session = boto3.session.Session(profile_name='default')

    # 세션 객체에서 리전 검색
    current_region = session.region_name

    # 사용자 지정 세션에서 High-level 리소스 생성
    resource = session.resource('s3')
    bucket = resource.Bucket(bucket_name)

    print("begin create bucket: {} ".format(bucket_name))
    current_dateTime = datetime.now()
    print(current_dateTime)

    # 리전별 엔드포인트에는 LocationConstraint 파라미터가 필요
    response = bucket.create(
        CreateBucketConfiguration={
            'LocationConstraint': current_region
        }
    )

if __name__ == '__main__':
    s3_client = boto3.client('s3')
    bucket_name = 'awsjam-your-nick'

    verify_bucket_name(s3_client, bucket_name)
    create_bucket(bucket_name)
