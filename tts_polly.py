from boto3 import client
polly = client("polly", region_name="ap-northeast-2")
response = polly.synthesize_speech(
        Text="안녕하세요, Amazon Text to Speech 서비스 polly 입니다. 즐거운 토요일 되세요.",
        OutputFormat="mp3",
        VoiceId="Seoyeon")

file = open('speech.mp3', 'wb')
file.write(response['AudioStream'].read())
file.close()