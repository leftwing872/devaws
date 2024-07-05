import boto3

s3 = boto3.client('s3')

# "SELECT * FROM s3object s where s.\"Name\" in ('Jane', 'Sam') "
# "SELECT * FROM s3object s where s.\"Name\" = 'Jane'",
resp = s3.select_object_content(
    Bucket='arch-s3-select',
    Key='sample_data.csv',
    ExpressionType='SQL',
    Expression="SELECT Name, PhoneNumber FROM s3object s where s.\"Name\" in ('Jane', 'Sam') ",
    InputSerialization = {'CSV': {"FileHeaderInfo": "Use"}, 'CompressionType': 'NONE'},
    OutputSerialization = {'CSV': {}},
)

for event in resp['Payload']:
    print('begin event')
    print(event)

    if 'Records' in event:
        records = event['Records']['Payload'].decode('utf-8')
        print(records)
        
    elif 'Stats' in event:
        statsDetails = event['Stats']['Details']
        print("Stats details bytesScanned: ")
        print(statsDetails['BytesScanned'])
        print("Stats details bytesProcessed: ")
        print(statsDetails['BytesProcessed'])
        print("Stats details bytesReturned: ")
        print(statsDetails['BytesReturned'])