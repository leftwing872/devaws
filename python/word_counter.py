import boto3
import json

f = open("yesterday.txt" , 'r') #파일을 오픈한다
data = f.read() #f.read()로 yesterday.txt의 내용전체를 읽어온다.
print(data.count("yesterday")) #data 변수에 count를 활용하여 yesterday가 얼마나 들어있는지 세어준다.
f.close()

#close()를 써서 열려 있는 파일 객체를 닫아 주는 역할을 한다. 굳이 하지않아도 자동으로 닫아주기는 하지만
쓰기, 읽기모드로 열었던 파일을 닫지 않고 사용하려고 하면 오류가 발생 할 수도 있기 때문이다.
