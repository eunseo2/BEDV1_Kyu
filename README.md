# 뀨의 민족 
뀨팀의 배달의 민족 클론 코딩


[뀨의 민족 사이트](http://3.36.99.19:8080/)

## :monocle_face: Team
|강희정|김은서|권태형|멘토|
|:---:|:---:|:---:|:---:|
|Product Owner|Scrum Master|Developer|Jansolikkun|
|<img src="https://user-images.githubusercontent.com/70589857/140476091-8bd10656-630d-4221-b6c0-fff3393be16e.png" width="350" height="250" />|<img src="https://user-images.githubusercontent.com/68772751/139533586-1edc542b-ab38-4b0e-ad30-8947aff3d70b.png" width="350" height="250" />|<img src="https://user-images.githubusercontent.com/70589857/140518400-c9d5f5d0-c634-437e-9508-14a0c44f0ec8.png" width="350" height="250" />|<img src="https://user-images.githubusercontent.com/70589857/140477361-8b3a07d5-b53a-4a11-8474-def8b0b9714e.png" width="400" height="250" />



##  :hammer: 개발환경
```
- SpringBoot 2.5.6
- MySQL (배포 DB)
- H2 (테스트 DB)
- Java 11
- Aws EC2, RDS
```

##  💻 실행환경
### Mysql
```
url: ${DB_URL}
username: ${DB_USER}
password: ${DB_PASSWORD}
```
### AWS S3 설정
```
accessKey: ${ACCESS_KEY}
secretKey: ${SECRET_KEY}
bucket: ${BUCKET_NAME}
static: ${AWS_REGION}
```

### 배포
```
./start.sh
./stop.sh
```
##  ⚙ 이슈 관리
```
- Github Issue
```
## **이슈 번호 자동화**

> node 가 설치되어 있어야 합니다.
> 
```
npm install
```

## 📝 설계 문서

### 1. API
- [Postman 문서 보러가기](https://documenter.getpostman.com/view/14790864/UVByJW6Q)
- [RestDocs 문서 보러가기](http://3.36.99.19:8080/docs)

### 2. ERD
- [ERD 문서 보러가기](https://www.notion.so/backend-devcourse/ERD-5cfdde92e2704700a5c3111c5f886397)

### 3. 프로젝트 관리
- [Notion 보러 가기](https://www.notion.so/backend-devcourse/437d09a9481a4d2b84c4d7b3022ac024)
- [칸반 보드 보러 가기](https://github.com/prgrms-be-devcourse/BEDV1_Kyu/projects/2)
