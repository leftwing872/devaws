import boto3

dynamodb = boto3.resource('dynamodb', region_name='ap-northeast-2')

table = dynamodb.Table('TblDreamCar')
table.delete()


table = dynamodb.Table('TblDreamCar2')
table.delete()

