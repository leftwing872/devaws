import boto3
import time
dynamodb = boto3.resource('dynamodb', region_name='us-east-1')
tableName = "Note2"

def create_table():
    print("Creating table...")
    table = dynamodb.create_table(
        TableName=tableName,
        KeySchema=[
            {
                'AttributeName': 'UserId',
                'KeyType': 'HASH'  #Partition key
            },
            {
                'AttributeName': 'NoteId',
                'KeyType': 'RANGE'  #Sort key
            }
        ],
        AttributeDefinitions=[
            {
                'AttributeName': 'UserId',
                'AttributeType': 'S'
            },
            {
                'AttributeName': 'NoteId',
                'AttributeType': 'N'
            },

        ],
        ProvisionedThroughput={
            'ReadCapacityUnits': 10,
            'WriteCapacityUnits': 10
        }
    )

    print("Begin wait")
    table.wait_until_exists()
    print("End wait")
    print("Table has been created!")

def update_table():
    table = dynamodb.Table(tableName)
    table.update(
        ProvisionedThroughput={
            'ReadCapacityUnits': 5,
            'WriteCapacityUnits': 5
        }
    )
    
    while True:
        response = table.meta.client.describe_table(TableName=tableName)
        print(response['Table']['TableStatus'])
        time.sleep(0.1)
        if response['Table']['TableStatus'] == 'ACTIVE':
            break
    
    print("Table has been updated!")

def delete_table():
    response = dynamodb.meta.client.delete_table(
        TableName=tableName
    )
    
    print("Table has been deleted!")

def list_tables():
    response = dynamodb.meta.client.list_tables(
        Limit=10
    )
    print(response)

create_table()
update_table()
delete_table()
list_tables()