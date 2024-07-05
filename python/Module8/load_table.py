import boto3
import json

dynamodb = boto3.resource('dynamodb', region_name='us-east-1')

table = dynamodb.Table('LUDreamCar')

with open("dreamcardata.json") as json_file:
    dreamcardata = json.load(json_file)
    for dreamcar in dreamcardata:
        brand = dreamcar['brand']
        model = dreamcar['model']
        color = dreamcar['color']

        print("Adding car:", brand, model,color)

        table.put_item(
           Item={
               'brand': brand,
               'model': model,
               'color': color