#!/bin/bash

bucket_name=arch-class-eunhak-tags

for key in `aws s3api list-objects --bucket $bucket_name | jq '.Contents[].Key'`; do 
    pure_key=$(sed -e 's/^"//' -e 's/"$//' <<< $key)

    tags_key=$(aws s3api get-object-tagging --bucket $bucket_name --key $pure_key | jq '.TagSet[].Key')
    tags_value=$(aws s3api get-object-tagging --bucket $bucket_name --key $pure_key | jq '.TagSet[].Value')

    echo $pure_key "=>" $tags_key $tags_value
done
