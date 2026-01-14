# JinyApp

Spring Boot, JPA, Thymeleaf, Redis를 활용한 게시판 프로젝트입니다.

## 🛠 환경 구축 (Docker)

애플리케이션 실행에 필요한 MySQL과 Redis는 Docker를 통해 간편하게 실행할 수 있습니다.

### 1. Docker 컨테이너 실행
프로젝트 루트 디렉토리에서 아래 명령어를 실행합니다:
```bash
docker-compose up -d
```

### 2. 컨테이너 구성 정보
- **MySQL (8.0)**
  - Port: `3306`
  - Database: `jinyapp_db`
  - Root Password: `1234`
- **Redis (latest)**
  - Port: `6379`

### 3. 컨테이너 중지
```bash
docker-compose down
```

## 🚀 애플리케이션 실행
1. Docker 컨테이너가 정상적으로 실행 중인지 확인합니다.
2. IDE(IntelliJ 등)에서 `JinyAppApplication`을 실행하거나, 터미널에서 아래 명령어를 실행합니다:
```bash
./gradlew bootRun
```
