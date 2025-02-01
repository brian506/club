# 1. Java 17 Base Image 사용
FROM openjdk:17-jdk-slim

# 2. 컨테이너 내 작업 디렉토리 설정
WORKDIR /app

# 3. 빌드된 JAR 파일 복사
COPY build/libs/club-0.0.1-SNAPSHOT.jar app.jar

# 4. 애플리케이션 실행에 필요한 포트 공개
EXPOSE 8080

# 5. Spring Boot 애플리케이션 실행
CMD ["java", "-jar", "app.jar"]

# Dockerfile
# container 를 실행하기 위한 이미지 빌드 과정을 담은 파일