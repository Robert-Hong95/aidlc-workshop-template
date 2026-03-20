# Build Instructions

## Prerequisites
- **Java**: 17 (Amazon Corretto)
- **Gradle**: 8.12.1 (wrapper 포함)
- **Docker**: Docker Compose (MySQL 8.0)
- **OS**: macOS (aarch64)

## Build Steps

### 1. Java 환경 설정
```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
```

### 2. DB 시작
```bash
cd table-order/backend
docker-compose up -d
```

### 3. Build
```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
cd table-order/backend
./gradlew clean build
```

### 4. 실행
```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
cd table-order/backend
./gradlew bootRun
```
- 서버: http://localhost:8080

## Build Artifacts
- `build/libs/backend-0.0.1-SNAPSHOT.jar`
