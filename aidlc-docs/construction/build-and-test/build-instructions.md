# Build Instructions

## Prerequisites
- **JDK**: 21+
- **Build Tool**: Gradle 8.x (wrapper 포함)
- **Database**: MySQL 8.0 (런타임), H2 (테스트 자동)
- **OS**: macOS / Linux / Windows

## Environment Variables

```bash
# application.yml 또는 환경변수로 설정
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/table_order
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=<password>
JWT_SECRET=<base64-encoded-256bit-key>
```

## Build Steps

### 1. 의존성 설치 및 빌드
```bash
cd table-order/backend
./gradlew clean build
```

### 2. 빌드 성공 확인
- 출력: `BUILD SUCCESSFUL`
- 아티팩트: `build/libs/table-order-0.0.1-SNAPSHOT.jar`

### 3. 애플리케이션 실행
```bash
# DB 스키마 초기화 (최초 1회)
mysql -u root -p table_order < src/main/resources/db/init.sql

# 실행
java -jar build/libs/table-order-0.0.1-SNAPSHOT.jar
```

## Troubleshooting

### Gradle Wrapper 실행 오류
- `gradle-wrapper.jar`에 `Main-Class: org.gradle.wrapper.GradleWrapperMain` manifest 확인
- 권한: `chmod +x gradlew`

### 컴파일 오류
- JDK 21 설치 확인: `java -version`
- Gradle JDK 설정: `JAVA_HOME` 환경변수 확인
