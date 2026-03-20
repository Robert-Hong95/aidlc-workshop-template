# Requirements Verification Questions

요구사항 문서를 분석한 결과, 아래 항목들에 대한 확인이 필요합니다.
각 질문의 [Answer]: 태그 뒤에 선택지 알파벳을 입력해주세요.

---

## Question 1
Backend 기술 스택으로 어떤 것을 사용하시겠습니까?

A) Node.js + Express (JavaScript/TypeScript)
B) Python + FastAPI
C) Java + Spring Boot
D) Go + Gin/Echo
E) Other (please describe after [Answer]: tag below)

[Answer]: C

## Question 2
Frontend 기술 스택으로 어떤 것을 사용하시겠습니까?

A) React (JavaScript/TypeScript)
B) Vue.js
C) Next.js (React 기반 풀스택)
D) Other (please describe after [Answer]: tag below)

[Answer]: C

## Question 3
데이터베이스로 어떤 것을 사용하시겠습니까?

A) PostgreSQL
B) MySQL
C) Amazon DynamoDB (NoSQL)
D) MongoDB
E) Other (please describe after [Answer]: tag below)

[Answer]: B

## Question 4
배포 환경은 어떻게 계획하고 계십니까?

A) AWS (EC2, ECS, Lambda 등)
B) 로컬 개발 환경만 (Docker Compose 등)
C) Vercel/Netlify (Frontend) + AWS (Backend)
D) Other (please describe after [Answer]: tag below)

[Answer]: B

## Question 5
메뉴 이미지 관리 방식은 어떻게 하시겠습니까? (요구사항에 이미지 URL로 명시되어 있으나 저장소 확인 필요)

A) 외부 이미지 URL 직접 입력 (별도 업로드 없음)
B) AWS S3 등 클라우드 스토리지에 업로드
C) 서버 로컬 파일 시스템에 업로드
D) Other (please describe after [Answer]: tag below)

[Answer]: C

## Question 6
매장(Store) 관리 범위는 어떻게 되나요? MVP에서 매장 생성/관리 기능이 필요합니까?

A) 단일 매장만 지원 (DB에 직접 seed 데이터로 생성)
B) 다중 매장 지원 + 매장 등록 API 필요
C) 다중 매장 지원하되, 매장 생성은 DB seed로 처리
D) Other (please describe after [Answer]: tag below)

[Answer]: B

## Question 7
고객용 인터페이스의 접근 방식은 어떻게 되나요?

A) 태블릿 전용 웹앱 (고정 URL 접속)
B) QR 코드 스캔으로 접속하는 모바일 웹
C) 태블릿 전용 + QR 코드 모바일 웹 모두 지원
D) Other (please describe after [Answer]: tag below)

[Answer]: C

## Question 8
관리자 인터페이스의 접근 방식은 어떻게 되나요?

A) 데스크톱 브라우저 전용 웹앱
B) 태블릿/모바일에서도 사용 가능한 반응형 웹앱
C) Other (please describe after [Answer]: tag below)

[Answer]: B

## Question 9
메뉴 관리 기능이 MVP 범위에 포함되어야 합니까? (요구사항 3.2.4에 정의되어 있으나 MVP 섹션에는 명시되지 않음)

A) 포함 - 관리자가 UI에서 메뉴 CRUD 가능해야 함
B) 제외 - DB seed 데이터로 메뉴 관리, MVP 이후 추가
C) 기본만 포함 - 메뉴 조회/수정만 가능, 등록/삭제는 DB에서 직접
D) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 10
주문 상태 실시간 업데이트(고객 화면)가 MVP에 포함되어야 합니까? (요구사항에 "선택사항"으로 표시됨)

A) 포함 - 고객도 SSE로 주문 상태 실시간 확인
B) 제외 - 고객은 페이지 새로고침으로 상태 확인
C) Other (please describe after [Answer]: tag below)

[Answer]: A
