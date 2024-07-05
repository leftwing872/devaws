import boto3

s3_client = boto3.client('s3', region_name='us-east-1')

bucket = "dilt-sandbox"

paginator = s3_client.get_paginator('list_objects')
page_iterator = paginator.paginate(Bucket=bucket,
                                   #Prefix="a",
                                   PaginationConfig={'MaxItems': 9999, 'PageSize': 2})

for page in page_iterator:
    json_data = page['Contents']
    print(json_data)
    for item in json_data:
        print(item['Key'])