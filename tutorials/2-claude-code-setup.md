---
marp: true
theme: default
paginate: true
header: 'Claude Code 프로젝트 설정'
footer: 'Claude Code 바이브 코딩 워크숍'
---

# Claude Code 프로젝트 설정
## CLAUDE.md와 규칙 기반 협업

프로젝트 컨텍스트 관리의 모든 것

---

## 이번 세션의 목표

### 학습 목표
- ✅ CLAUDE.md 파일의 역할과 구조 이해
- ✅ .claude/ 디렉토리 활용법
- ✅ 커스텀 명령어 생성
- ✅ 규칙 기반 협업 시스템 구축

### 실습 목표
- 프로젝트 문서 자동 생성
- 작업 규칙 정의
- 반복 작업 자동화

**예상 소요 시간**: 45분

---

## 문제 상황: AI와의 협업

### 전통적인 방식의 문제점

```
개발자: "UserService 코드를 리팩토링해줘"

Claude: "어떤 규칙을 따라야 하나요?"
       "Lombok을 사용하나요?"
       "테스트는 어떻게 작성하나요?"
       "예외 처리는 어떻게 하나요?"

개발자: (매번 같은 설명 반복...)
```

**비효율**: 컨텍스트를 매번 설명해야 함

---

## 해결책: 프로젝트 문서화

### CLAUDE.md의 역할

```
┌─────────────────────────────────────────┐
│         CLAUDE.md (프로젝트 가이드)      │
│  - 프로젝트 구조                         │
│  - 빌드 명령어                           │
│  - 아키텍처 패턴                         │
│  - 개발 가이드라인                       │
└─────────────────────────────────────────┘
             ↓
    Claude Code가 자동으로 읽음
             ↓
    프로젝트 컨텍스트 이해
             ↓
    적절한 코드 생성
```

---

## CLAUDE.md란?

### 공식 컨벤션
- **위치**: 프로젝트 루트 디렉토리
- **이름**: `CLAUDE.md` (대문자)
- **형식**: Markdown
- **역할**: Claude Code를 위한 프로젝트 가이드

### 자동 인식
```bash
cd your-project
claude

# Claude Code가 자동으로:
# 1. CLAUDE.md 찾기
# 2. 내용 읽기
# 3. 컨텍스트 활용
```

---

## CLAUDE.md 생성: 실습

### 1단계: Claude Code 시작

```bash
cd realworld-java21-springboot3
claude
```

### 2단계: 프로젝트 분석 요청

```
> 이 프로젝트를 분석하고 CLAUDE.md 파일을 작성해줘.
  다음 내용을 포함해줘:
  - 프로젝트 개요
  - 빌드 명령어
  - 모듈 아키텍처
  - 테스트 실행 방법
  - 개발 가이드라인
```

---

## CLAUDE.md 생성: 결과

### Claude가 생성한 파일

```markdown
# CLAUDE.md

이 파일은 Claude Code가 이 저장소에서 작업할 때
필요한 가이드를 제공합니다.

## 프로젝트 개요
Java 21, Spring Boot 3, H2 데이터베이스를 사용한
RealWorld API 구현체입니다.

## 빌드 시스템 & 명령어

### 애플리케이션 실행
```bash
./gradlew realworld:bootRun
```
...
```

---

## CLAUDE.md 핵심 구조

### 필수 섹션

1. **프로젝트 개요**
   - 기술 스택
   - 주요 기능
   - 아키텍처 패턴

2. **빌드 시스템**
   - 실행 명령어
   - 테스트 명령어
   - 빌드 명령어

3. **모듈 구조**
   - 각 모듈의 역할
   - 의존성 관계

---

## CLAUDE.md 핵심 구조 (계속)

4. **개발 가이드라인**
   - 코딩 규칙
   - 네이밍 컨벤션
   - 아키텍처 원칙

5. **테스트 전략**
   - 테스트 작성 규칙
   - 실행 방법
   - 커버리지 기준

6. **문제 해결**
   - 일반적인 오류
   - 해결 방법

---

## 모듈별 CLAUDE.md

### 계층적 문서 구조

```
realworld-java21-springboot3/
├── CLAUDE.md                    # 전체 프로젝트 가이드
├── module/
│   ├── core/
│   │   └── CLAUDE.md            # Core 모듈 상세
│   └── persistence/
│       └── CLAUDE.md            # Persistence 모듈 상세
└── server/
    └── api/
        └── CLAUDE.md            # API 모듈 상세
```

**장점**: 각 모듈 작업 시 상세 컨텍스트 제공

---

## 모듈별 CLAUDE.md 실습

### Core 모듈 문서 생성

```
> module/core 디렉토리를 분석하고 CLAUDE.md를 작성해줘.
  이 모듈의 역할과 주요 클래스, 사용 패턴을 설명해줘.
```

### 생성 결과

```markdown
# Core Module - Domain Layer

## 역할
순수한 비즈니스 로직과 도메인 모델을 포함합니다.
다른 모듈에 의존하지 않습니다.

## 주요 패키지
- `model/`: 도메인 엔티티와 저장소 인터페이스
- `service/`: 비즈니스 로직 서비스
...
```

---

## .claude/ 디렉토리 구조

### 전체 구조

```
.claude/
├── README.md              # Claude 설정 개요
├── SETUP.md               # 설정 가이드
├── rules.md               # 작업 규칙 (핵심!)
└── commands/              # 커스텀 명령어
    ├── review.md          # /review 명령어
    ├── test.md            # /test 명령어
    ├── new-feature.md     # /new-feature 명령어
    ├── fix-bug.md         # /fix-bug 명령어
    └── refactor.md        # /refactor 명령어
```

---

## rules.md: 작업 규칙 정의

### CLAUDE.md vs rules.md

**CLAUDE.md (What)**:
- 프로젝트가 **무엇**인지
- 구조가 **어떻게** 되어 있는지
- **어떤** 도구를 사용하는지

**rules.md (How)**:
- **어떻게** 코드를 작성할지
- **어떤** 규칙을 따를지
- **어떻게** 테스트할지

---

## rules.md 생성: 실습

### 작업 규칙 정의

```
> .claude/rules.md 파일을 생성하고 다음 규칙들을 정의해줘:

  1. 코딩 표준 (Lombok 사용 규칙, 네이밍 규칙)
  2. 아키텍처 규칙 (모듈 의존성, 레이어 분리)
  3. 테스트 규칙 (특히 예외 메시지 검증 금지!)
  4. 데이터베이스 규칙
  5. API 설계 규칙
  6. 보안 규칙
```

---

## rules.md 핵심 내용

### 1. 코딩 표준

```markdown
## 코딩 표준

### Lombok 사용
- `@RequiredArgsConstructor`: 생성자 주입
- `@Getter`/`@Setter`: 필요시만 사용
- `@Builder`: 복잡한 객체 생성
- `@Value`: 불변 객체

### 네이밍 규칙
- 클래스: PascalCase
- 메서드/변수: camelCase
- 상수: UPPER_SNAKE_CASE
```

---

## rules.md 핵심 내용 (계속)

### 2. 아키텍처 규칙

```markdown
## 아키텍처 규칙

### 모듈 의존성
```
api (compileOnly) → core ← persistence (implements)
```

### 레이어 분리
- Controller: HTTP 처리만
- Service: 비즈니스 로직
- Repository: 데이터 접근

### 예외 처리
- Not found: `NoSuchElementException`
- 검증 실패: `IllegalArgumentException`
```

---

## rules.md 핵심 내용 (계속)

### 3. 테스트 규칙 ⭐ (가장 중요!)

```markdown
## 테스트 규칙

### ❌ 절대 금지: 예외 메시지 검증
```java
// Bad - 메시지 변경 시 테스트 깨짐
val exception = shouldThrow<IllegalArgumentException> { ... }
exception.message shouldBe "User not found"
```

### ✅ 올바른 방법: 예외 타입만 검증
```java
// Good - 안정적인 테스트
shouldThrow<IllegalArgumentException> { ... }
```

**이유**: 예외 메시지는 변경 가능, 타입은 계약
```

---

## 규칙 참조 강제하기

### 문제: 규칙을 무시하는 경우

```
개발자: "UserService에 메서드 추가해줘"
Claude: (rules.md 보지 않고 작성)
→ 규칙 위반 코드 생성
```

### 해결: 다층 경고 시스템

**레벨 1**: 루트 CLAUDE.md에 경고
**레벨 2**: 각 모듈 CLAUDE.md에 경고
**레벨 3**: rules.md 자체에 경고
**레벨 4**: /check-rules 명령어

---

## 규칙 참조 강제: 구현

### 루트 CLAUDE.md 수정

```markdown
# CLAUDE.md

## ⚠️ 필수 규칙

**모든 작업 시 반드시 `.claude/rules.md` 파일을
참조해야 합니다.**

이 파일에는 다음이 포함되어 있습니다:
- 코딩 표준
- 아키텍처 규칙
- 테스트 규칙 (예외 메시지 검증 금지!)
- ...

**작업 전 반드시 확인하세요!**
```

---

## 규칙 참조 강제: rules.md

### rules.md 상단에 경고

```markdown
# ⚠️⚠️⚠️ 중요: 이 파일은 모든 작업의 기준입니다 ⚠️⚠️⚠️

**이 규칙을 무시하고 작업하지 마세요!**

모든 코드 변경, 새 기능 추가, 버그 수정 시
이 규칙을 반드시 따라야 합니다.

불확실한 경우 `/check-rules` 명령어를 사용하세요.

---
```

---

## 커스텀 명령어: 개요

### 명령어란?

**Slash 명령어**:
```
/review          # 코드 리뷰
/test            # 테스트 실행
/new-feature     # 새 기능 추가
```

**위치**: `.claude/commands/`

**동작**:
1. 사용자가 `/review` 입력
2. Claude가 `.claude/commands/review.md` 읽기
3. 명령어 내용 실행

---

## 커스텀 명령어: review.md

### 코드 리뷰 자동화

```markdown
# /review - 코드 리뷰

당신은 전문 코드 리뷰어입니다.
다음 순서로 코드를 검토하세요:

## 1. 코딩 표준 검증
- `.claude/rules.md`의 코딩 표준 확인
- Lombok 사용 규칙 준수 여부
- 네이밍 규칙 준수 여부

## 2. 아키텍처 규칙 검증
- 모듈 의존성 방향 확인
- 레이어 분리 원칙 준수
- 예외 처리 규칙 확인

## 3. 테스트 규칙 검증 ⭐
- 예외 메시지 검증 여부 확인 (금지!)
- 테스트 네이밍 규칙 확인
```

---

## 커스텀 명령어: test.md

### 테스트 자동화

```markdown
# /test - 테스트 실행 및 검증

## 단계

### 1. 테스트 실행
```bash
./gradlew test
```

### 2. 실패 시 분석
- 오류 메시지 확인
- 스택 트레이스 분석
- 실패 원인 설명

### 3. 커버리지 확인
```bash
./gradlew jacocoRootReport
```

### 4. 리포트 요약
- 전체 커버리지 퍼센트
- 개선이 필요한 영역
```

---

## 커스텀 명령어: new-feature.md

### 새 기능 추가 워크플로우

```markdown
# /new-feature - 새 기능 추가

사용자에게 기능 설명을 요청하고 다음 순서로 구현:

## 1. 요구사항 확인
- 기능 상세 설명 요청
- 엔티티, API, 비즈니스 로직 파악

## 2. Core 모듈 작업
1. `module/core/src/main/java/io/zhc1/realworld/model/`
   - 엔티티 클래스 생성
2. `module/core/src/main/java/io/zhc1/realworld/model/`
   - Repository 인터페이스 정의
3. `module/core/src/main/java/io/zhc1/realworld/service/`
   - Service 인터페이스 및 구현 작성
```

---

## 커스텀 명령어: new-feature.md (계속)

```markdown
## 3. Persistence 모듈 작업
1. `module/persistence/.../persistence/`
   - JPA Repository 구현
   - Specification (필요시)

## 4. API 모듈 작업
1. `server/api/.../api/response/`
   - Response DTO 작성
2. `server/api/.../api/request/`
   - Request DTO 작성 (필요시)
3. `server/api/.../api/`
   - Controller 작성

## 5. 테스트 작성
- Service 단위 테스트
- Controller 통합 테스트

## 6. 검증
```bash
./gradlew build
```
```

---

## 커스텀 명령어: check-rules.md

### 규칙 확인 명령어

```markdown
# /check-rules - 규칙 확인

`.claude/rules.md` 파일을 읽고 다음 작업과
관련된 규칙을 요약하세요:

## 입력
사용자가 수행하려는 작업 설명

## 출력
해당 작업에 적용되는 규칙 목록:
- 코딩 표준
- 아키텍처 규칙
- 테스트 규칙
- 기타 관련 규칙

## 예시
작업: "UserService에 새 메서드 추가"
관련 규칙:
- Lombok `@RequiredArgsConstructor` 사용
- 검증 실패 시 `IllegalArgumentException`
- 테스트에서 예외 메시지 검증 금지
```

---

## 커스텀 명령어 사용: 실습

### 1. /check-rules 사용

```
> /check-rules 새 기능 추가

Claude 응답:
"새 기능 추가 시 다음 규칙을 따라야 합니다:

1. 모듈 순서: Core → Persistence → API
2. Lombok 어노테이션 사용
3. 예외 처리: IllegalArgumentException, NoSuchElementException
4. 테스트: 예외 타입만 검증, 메시지 검증 금지
5. 커밋 전: ./gradlew spotlessApply 실행
..."
```

---

## 커스텀 명령어 사용: 실습 (계속)

### 2. /new-feature 사용

```
> /new-feature

Claude: "어떤 기능을 추가하시겠습니까?"

사용자: "게시글 북마크 기능"

Claude: "알겠습니다. 다음 순서로 구현하겠습니다:
1. Core 모듈에 Bookmark 엔티티 생성
2. BookmarkRepository 인터페이스 정의
3. BookmarkService 구현
..."

(자동으로 모든 단계 수행)
```

---

## 전체 시스템 아키텍처

### Claude Code 협업 시스템

```
┌─────────────────────────────────────────┐
│  CLAUDE.md (프로젝트 구조)              │
│  - 빌드 명령어                          │
│  - 모듈 아키텍처                        │
│  - 개발 가이드                          │
└────────────┬────────────────────────────┘
             ↓
┌────────────┴────────────────────────────┐
│  .claude/rules.md (작업 규칙)          │
│  - 코딩 표준                            │
│  - 아키텍처 규칙                        │
│  - 테스트 규칙                          │
└────────────┬────────────────────────────┘
             ↓
┌────────────┴────────────────────────────┐
│  .claude/commands/ (워크플로우)        │
│  - /review, /test                       │
│  - /new-feature, /fix-bug               │
└─────────────────────────────────────────┘
```

---

## 실전 워크플로우 예시

### 시나리오: 버그 수정

```bash
# 1. 규칙 확인
> /check-rules 버그 수정

# 2. 버그 수정 명령
> /fix-bug "좋아요 중복 카운트 버그"

# Claude가 자동으로:
# - 문제 분석
# - 수정 코드 작성
# - 테스트 추가
# - 빌드 검증

# 3. 코드 리뷰
> /review

# 4. 테스트 실행
> /test

# 5. 커밋
./gradlew spotlessApply
git add .
git commit -m "fix: 좋아요 중복 카운트 버그 수정"
```

---

## 실전 워크플로우 예시 (계속)

### 시나리오: 새 기능 추가

```bash
# 1. 규칙 확인
> /check-rules 새 기능 추가

# 2. 기능 구현
> /new-feature "게시글 북마크"

# Claude가 자동으로:
# - Core 모듈 작업
# - Persistence 모듈 작업
# - API 모듈 작업
# - 테스트 작성

# 3. 검증
> /test
> /review

# 4. 빌드 확인
./gradlew build
```

---

## 핵심 성과

### 일관성 있는 협업
- ✅ 새로운 Claude 세션도 프로젝트 즉시 이해
- ✅ 동일한 규칙으로 일관된 코드 작성
- ✅ 컨텍스트 손실 최소화

### 자동화된 워크플로우
- ✅ 반복 작업을 명령어로 자동화
- ✅ 규칙 준수를 자동으로 검증
- ✅ 실수 가능성 감소

### 명확한 규칙 관리
- ✅ 프로젝트 구조와 작업 규칙 분리
- ✅ 규칙 변경 시 한 곳만 수정
- ✅ 규칙 준수 강제 시스템

---

## Best Practices

### CLAUDE.md 작성
✅ **명확하고 간결하게** - 핵심 정보만
✅ **예시 포함** - 명령어 실행 예시
✅ **최신 상태 유지** - 프로젝트 변경 시 업데이트

### rules.md 작성
✅ **구체적으로** - 모호하지 않게
✅ **예시 코드** - Good/Bad 예시
✅ **우선순위** - 중요한 규칙 강조

### 커스텀 명령어
✅ **단일 책임** - 한 명령어는 한 가지 작업
✅ **명확한 이름** - /review, /test 등
✅ **단계별 설명** - 순서 명시

---

## 안티패턴 (피해야 할 것)

### ❌ 너무 많은 정보
```markdown
# Bad
CLAUDE.md에 200페이지 분량의 모든 세부사항
→ Claude가 핵심을 놓칠 수 있음
```

### ❌ 모호한 규칙
```markdown
# Bad
"좋은 코드를 작성하세요"
→ 구체적이지 않음

# Good
"Service 클래스는 @RequiredArgsConstructor를 사용하여
생성자 주입을 구현하세요"
```

---

## 안티패턴 (계속)

### ❌ 규칙과 구조 혼재
```markdown
# Bad - CLAUDE.md에 모든 것
프로젝트 구조 + 코딩 규칙 + 작업 절차
→ 유지보수 어려움

# Good - 분리
CLAUDE.md: 프로젝트 구조
rules.md: 작업 규칙
commands/: 워크플로우
```

### ❌ 명령어 남발
```
/do-everything
/magic-fix
→ 명확한 책임 없음
```

---

## Git 작업: 태그 생성

### 현재 상태 저장

```bash
# 변경사항 확인
git status

# 추가
git add CLAUDE.md module/*/CLAUDE.md server/*/CLAUDE.md .claude/

# 커밋
git commit -m "docs: Add Claude Code configuration

- Add CLAUDE.md files for project structure
- Add .claude/rules.md for coding standards
- Add custom commands (/review, /test, /new-feature)
- Add multi-layer warning system for rule enforcement

🤖 Generated with Claude Code"

# 태그 생성
git tag -a v1.1.0-claude-docs -m "Claude Code documentation and configuration"

# 푸시
git push origin main --tags
```

---

## 검증: 동작 확인

### 새 Claude 세션으로 테스트

```bash
# 1. Claude 재시작
exit
claude

# 2. 프로젝트 이해 확인
> 이 프로젝트의 아키텍처를 설명해줘

# Claude가 CLAUDE.md 기반으로 설명

# 3. 규칙 확인
> /check-rules 새 기능 추가

# Claude가 rules.md 기반으로 규칙 설명

# 4. 명령어 실행
> /review
```

**모두 동작하면 성공!** ✅

---

## 트러블슈팅

### Claude가 CLAUDE.md를 읽지 않음

**원인**: 파일명 대소문자 오류
**해결**:
```bash
# 반드시 대문자
mv claude.md CLAUDE.md
```

### 커스텀 명령어가 작동하지 않음

**원인**: .claude/commands/ 디렉토리 구조 오류
**해결**:
```bash
# 구조 확인
ls -la .claude/commands/
# *.md 파일들이 있어야 함
```

---

## 트러블슈팅 (계속)

### rules.md가 참조되지 않음

**해결**: 명시적 참조 추가
```
> .claude/rules.md 파일을 읽고
  해당 규칙에 따라 작업해줘
```

### 모듈별 CLAUDE.md 충돌

**원인**: 루트와 모듈 문서 내용 불일치
**해결**: 루트는 개요, 모듈은 상세로 분리

---

## 고급 활용: 프로젝트 템플릿

### 다른 프로젝트에 적용

```bash
# 1. 템플릿 복사
cp -r realworld/.claude/ my-project/.claude/
cp realworld/CLAUDE.md my-project/CLAUDE.md

# 2. 프로젝트에 맞게 수정
# - CLAUDE.md: 프로젝트 정보 변경
# - rules.md: 프로젝트 규칙 반영
# - commands/: 필요한 명령어 추가/제거

# 3. Claude로 자동 수정
> 이 프로젝트에 맞게 CLAUDE.md를 수정해줘
```

---

## 고급 활용: 팀 협업

### 팀 규칙 공유

```
.claude/
├── rules.md              # 팀 전체 규칙
├── rules-frontend.md     # 프론트엔드 규칙
├── rules-backend.md      # 백엔드 규칙
└── commands/
    ├── review.md         # 공통 리뷰
    ├── review-fe.md      # FE 전용 리뷰
    └── review-be.md      # BE 전용 리뷰
```

**사용**:
```
> .claude/rules-backend.md 규칙을 따라
  새 API를 추가해줘
```

---

## 다음 단계 미리보기

### 테스트 커버리지 측정

다음 세션에서 배울 내용:
- **JaCoCo** 설정 및 사용
- **커버리지 분석** 방법
- **테스트 작성** 전략
- **커버리지 개선** 실습

**Claude Code 활용**:
- `/test` 명령어로 자동 테스트
- 커버리지 분석 자동화
- 테스트 코드 자동 생성

---

## 핵심 요약

### 배운 내용
✅ **CLAUDE.md** - 프로젝트 구조 문서화
✅ **.claude/rules.md** - 작업 규칙 정의
✅ **커스텀 명령어** - 워크플로우 자동화
✅ **다층 경고 시스템** - 규칙 준수 강제

### 핵심 원칙
💡 **구조와 규칙은 분리**
💡 **명확하고 구체적으로**
💡 **자동화할 수 있는 것은 자동화**
💡 **팀과 규칙 공유**

---

## 실습 체크리스트

### 완료 확인

- [ ] 루트 CLAUDE.md 생성
- [ ] 모듈별 CLAUDE.md 생성
- [ ] .claude/rules.md 작성
- [ ] 커스텀 명령어 3개 이상 생성
- [ ] /check-rules 명령어 테스트
- [ ] /review 명령어 테스트
- [ ] Git 태그 생성 (v1.1.0-claude-docs)
- [ ] 새 Claude 세션에서 동작 확인

**모두 완료하면 다음 세션으로!**

---

## 참고 자료

### 문서
- [Claude Code 공식 문서](https://docs.anthropic.com/claude-code)
- [Markdown 가이드](https://www.markdownguide.org/)

### 예시 프로젝트
- [RealWorld Claude Config](https://github.com/1chz/realworld-java21-springboot3)
- [SuperClaude Framework](https://github.com/SuperClaude-Org/SuperClaude_Framework)

---

## Q&A

### 질문이 있으신가요?

- CLAUDE.md 작성 관련?
- rules.md 구조 관련?
- 커스텀 명령어 작성?
- 팀 협업 적용 방법?

---

# CLAUDE.md 설정 완료! 🎉

## 다음: 테스트 커버리지 측정 및 개선
### JaCoCo로 코드 품질 향상하기
