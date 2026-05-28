# JinyApp

**JinyApp**은 Spring Boot 3.5.0을 기반으로 구축된 현대적인 웹 게시판 서비스입니다. 효율적인 데이터 관리와 보안, 그리고 빠른 성능을 목표로 설계되었습니다.

## ✨ 주요 기능
- **게시판 (Post)**: 게시글 작성, 수정, 삭제, 조회 및 비밀글 기능
- **댓글 (Comment)**: 게시글에 대한 자유로운 의견 교환
- **보안**: Spring Security를 통한 접근 제어 및 Jsoup을 이용한 HTML Sanitization (XSS 방지)
- **성능**: Redis를 활용한 데이터 캐싱 및 세션 관리 (예정)
- **DB**: JPA를 통한 객체 지향적 데이터 관리 및 MySQL 8.0 연동

## 🛠 기술 스택
- **Backend**: Java 21, Spring Boot 3.5.0, Spring Data JPA, Spring Security
- **Frontend**: Thymeleaf, JavaScript, CSS3, HTML5
- **Database**: MySQL 8.0, Redis
- **Infra**: Docker Compose

## 🚀 시작하기

### 1. 인프라 구축 (Docker)
애플리케이션 실행에 필요한 MySQL과 Redis를 Docker로 실행합니다. 프로젝트 루트 디렉토리에서 다음 명령어를 입력하세요.
```bash
docker-compose up -d
```

### 2. 애플리케이션 실행
터미널에서 아래 명령어를 실행하거나 IDE에서 `JinyAppApplication`을 실행합니다.
```bash
./gradlew bootRun
```
- **접속 주소**: `http://localhost:8080`

## 📂 프로젝트 구조
- `src/main/java`: 서비스 비즈니스 로직 및 도메인 모델
- `src/main/resources`: 설정 파일 및 뷰 템플릿
- `scripts`: 배포 및 관리용 스크립트

## 📝 라이선스
이 프로젝트는 교육 및 개인 학습용으로 제작되었습니다.
