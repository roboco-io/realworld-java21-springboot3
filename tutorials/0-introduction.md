---
marp: true
theme: default
paginate: true
header: 'Claude Code와 함께하는 바이브 코딩'
---

# Claude Code와 함께하는 바이브 코딩
## AI 기반 개발 워크플로우 실습

RealWorld Java21 + Spring Boot 3 프로젝트로 배우는
AI Pair Programming

---

## 워크숍 개요

### 목표
- **바이브 코딩**을 구체적으로 실행하는 방법 학습
- Claude Code를 활용한 효율적인 개발 워크플로우 구축
- 프로젝트 컨텍스트 관리와 자동화 기법 습득

### 대상
- AI 도구를 활용한 생산성 향상에 관심 있는 개발자
- 테스트 커버리지와 코드 품질 자동화를 배우고 싶은 분
- Claude Code의 실전 활용법을 알고 싶은 분

---

## 워크숍 구성

### Part 1: 환경 설정 (30분)
- Java 21, GitHub CLI, Claude Code 설치
- RealWorld 프로젝트 Fork 및 Clone
- 개발 환경 검증

### Part 2: Claude Code 프로젝트 설정 (45분)
- CLAUDE.md 파일 구조와 작성법
- .claude/ 디렉토리 활용
- 커스텀 명령어 생성

---

## 워크숍 구성 (계속)

### Part 3: 테스트 커버리지 실습 (60분)
- JaCoCo 설정 및 측정
- 커버리지 분석 및 개선 전략
- 실전 테스트 작성

### Part 4: CI/CD 자동화 (45분)
- Spotless + Checkstyle 설정
- GitHub Actions 워크플로우
- 품질 게이트 구축

---

## 실습 프로젝트: RealWorld

### RealWorld란?
- **동일한 스펙**으로 구현된 미니 블로그 플랫폼
- 다양한 기술 스택 비교 가능
- 실전 수준의 복잡도

### 주요 기능
- 사용자 인증 (JWT)
- 게시글 CRUD
- 댓글, 좋아요, 팔로우
- 태그 시스템

---

## 프로젝트 기술 스택

### Backend
- **Java 21** - Virtual Threads, Pattern Matching
- **Spring Boot 3** - 최신 Spring 프레임워크
- **Spring Data JPA** - ORM 및 데이터 접근
- **H2 Database** - 인메모리 DB (MySQL 모드)

### 아키텍처
- **Multi-module** - core, persistence, api
- **DIP** - Dependency Inversion Principle
- **Layered Architecture** - 명확한 레이어 분리

---

## 프로젝트 구조

```
realworld-java21-springboot3/
├── module/
│   ├── core/           # 도메인 레이어 (비즈니스 로직)
│   └── persistence/    # 데이터 접근 레이어 (JPA)
├── server/
│   └── api/            # REST API 레이어
├── .claude/            # Claude Code 설정
│   ├── rules.md        # 작업 규칙
│   └── commands/       # 커스텀 명령어
├── CLAUDE.md           # 프로젝트 가이드
└── build.gradle.kts    # 빌드 설정
```

---

## Claude Code란?

### 공식 CLI 도구
- Anthropic이 개발한 공식 Claude Code CLI
- 터미널에서 Claude와 직접 대화
- 코드베이스 전체를 컨텍스트로 활용

### 주요 기능
- **파일 읽기/쓰기** - 코드 직접 수정
- **명령어 실행** - Bash, Gradle 등
- **프로젝트 이해** - 자동 컨텍스트 파악
- **Plan 모드** - 작업 전 계획 수립

---

## 바이브 코딩이란?

### 전통적 개발
```
개발자 → 코드 작성 → 테스트 → 디버깅 → 문서화
(모든 단계를 수동으로 수행)
```

### 바이브 코딩
```
개발자 → 의도 전달 → Claude Code
           ↓
자동 코드 작성 + 테스트 + 문서화
           ↓
개발자 → 검토 및 피드백 → 반복
```

**핵심**: 의도를 명확히 전달하고, AI가 구현하도록 하는 협업

---

## 바이브 코딩의 핵심 원칙

### 1. 명확한 의도 전달
❌ "코드 좀 고쳐줘"
✅ "UserService의 findById 메서드가 null을 반환할 때 NoSuchElementException을 던지도록 수정해줘"

### 2. 컨텍스트 제공
- CLAUDE.md로 프로젝트 구조 설명
- rules.md로 코딩 규칙 명시
- 커스텀 명령어로 워크플로우 자동화

### 3. 검증과 피드백
- 테스트로 검증 및 문서화

---

## 실습에서 배울 내용

### 기술적 스킬
- ✅ Java 21 + Spring Boot 3 프로젝트 구조 이해 및 문서화
- ✅ 테스트 커버리지 측정 및 개선
- ✅ 코드 품질 자동화 (Spotless, Checkstyle)
- ✅ CI/CD 파이프라인 구축

### AI 협업 스킬
- ✅ 효과적인 프롬프트 작성
- ✅ 프로젝트 컨텍스트 관리
- ✅ 반복 작업 자동화
- ✅ 규칙 기반 협업

---

## 실습 준비물

### 필수 설치
- Java 21 (OpenJDK 또는 Oracle JDK)
- GitHub CLI (`gh`)
- Claude Code CLI
- Git

### 계정 필요
- GitHub 계정
- Claude API 키 (Console 또는 Pro/Max 플랜)

### 권장 사항
- macOS 또는 Linux (Windows는 WSL 권장)
- 최소 8GB RAM
- 안정적인 인터넷 연결

---

## 워크숍 진행 방식

### 실습 중심
- 각 단계마다 직접 실습
- Claude Code와 실시간 대화
- 즉각적인 피드백과 개선

### 단계별 태그
```bash
v1.0.0-claude-rules   # CLAUDE.md 작성 및 커스텀 rule 작성
v1.1.0-test-coverage  # 테스트 커버리지 측정 및 개선
v1.1.0-ci-workflow    # CI/CD 구축
```

각 단계마다 Git 태그로 체크포인트 생성

---

## 학습 목표

### 이 워크숍 이후 여러분은...

✅ Claude Code를 프로젝트에 효과적으로 도입할 수 있습니다
✅ AI와 협업하여 생산성을 2-3배 높일 수 있습니다
✅ 테스트 커버리지를 체계적으로 관리할 수 있습니다
✅ 코드 품질을 자동으로 검증하는 시스템을 구축할 수 있습니다
✅ 반복 작업을 자동화하는 워크플로우를 만들 수 있습니다

---

## 시작하기 전에

### 마인드셋
- **AI는 도구입니다** - 명령하는 것이 아니라 협업하는 것
- **명확성이 핵심** - 모호한 요청은 모호한 결과를 만듦
- **반복과 개선** - 한 번에 완벽할 필요 없음
- **컨텍스트가 중요** - 프로젝트 정보를 잘 전달할수록 좋은 결과

### 실습 철학
> "하고 싶었지만 시간이 없어서 못했던 것들이
> 생산성 향상으로 가능해진다!"

---

## Q&A

### 질문이 있으신가요?

- Claude Code 사용 경험이 있으신가요?
- Java/Spring Boot 프로젝트 경험은?
- AI 도구로 개발해본 적이 있나요?
- 특별히 궁금한 부분이 있나요?

---

# 감사합니다!

## 함께 바이브 코딩을 시작해봅시다! 🚀
