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
    CLAUDE.md (프로젝트 가이드)
    - 프로젝트 구조
    - 빌드 명령어
    - 아키텍처 패턴
    - 개발 가이드라인
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
```
Claude Code가 자동으로 CLAUDE.md 찾아서 내용 읽고 컨텍스트로 활용

---

## CLAUDE.md 생성: 실습

### 1단계: Claude Code 시작

```bash
cd realworld-java21-springboot3
claude
```

### 2단계: 프로젝트 분석 요청

```
/init
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
./gradlew realworld:bootRun
```

---

## CLAUDE.md 핵심 구조

### 필수 섹션

1. **프로젝트 개요**
   - 기술 스택, 주요 기능, 아키텍처 패턴

2. **빌드 시스템**
   - 실행 명령어, 테스트 명령어, 빌드 명령어

3. **모듈 구조**
   - 각 모듈의 역할, 의존성 관계

---

## CLAUDE.md 핵심 구조

4. **개발 가이드라인**
   - 코딩 규칙, 네이밍 컨벤션, 아키텍처 원칙

5. **테스트 전략**
   - 테스트 작성 규칙, 실행 방법, 커버리지 기준

6. **문제 해결**
   - 일반적인 오류, 해결 방법

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

## rules.md 핵심 내용

### 2. 아키텍처 규칙

```markdown
## 아키텍처 규칙

### 모듈 의존성
api (compileOnly) → core ← persistence (implements)

### 레이어 분리
- Controller: HTTP 처리만
- Service: 비즈니스 로직
- Repository: 데이터 접근

### 예외 처리
- Not found: `NoSuchElementException`
- 검증 실패: `IllegalArgumentException`
```

---

## rules.md 핵심 내용

### 3. 테스트 규칙

```markdown
## 테스트 규칙

### ❌ 절대 금지: 예외 메시지 검증
// Bad - 메시지 변경 시 테스트 깨짐
val exception = shouldThrow<IllegalArgumentException> { ... }
exception.message shouldBe "User not found"

### ✅ 올바른 방법: 예외 타입만 검증
// Good - 안정적인 테스트
shouldThrow<IllegalArgumentException> { ... }
```
**이유**: 예외 메시지는 변경 가능, 타입은 계약

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

## 2. 아키텍처 규칙 검증
- 모듈 의존성 방향 확인
- 레이어 분리 원칙 준수

## 3. 테스트 규칙 검증
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
./gradlew test

### 2. 커버리지 확인
./gradlew jacocoRootReport

### 3. 리포트 요약
- 전체 커버리지 퍼센트
- 개선이 필요한 영역
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

## 예시
```

---

## 커스텀 명령어 사용: 실습

### /check-rules 사용

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

## 커스텀 명령어 셋 사용: Super Claude

### Super Claude 사용

https://github.com/SuperClaude-Org/SuperClaude_Framework

```sh
pip install SuperClaude && pip upgrade SuperClaude && SuperClaude install
```

```
"https://github.com/SuperClaude-Org/SuperClaude_Framework"에 나와있는 프레임워크 설치해줘!
```

---

## 커스텀 명령어 셋 사용: Super Claude

### Super Claude 사용 (이미 잘 구현된 커스텀 명령어)

```
/sc:implement "@prd.md 보고 구현해줘." --type backend --focus performance
```

```
/sc:document "git 최신 커밋 3개의 변경사항을 보고 문서화해줘."
```

---

## 전체 시스템 아키텍처

### Claude Code 협업 시스템

```
CLAUDE.md (프로젝트 구조)
- 빌드 명령어
- 모듈 아키텍처
- 개발 가이드
             ↓
.claude/rules.md (작업 규칙)
- 코딩 표준
- 아키텍처 규칙
- 테스트 규칙
             ↓
.claude/commands/ (워크플로우)
- /review, /test
- /new-feature, /fix-bug
```
---

## 실전 워크플로우 예시

### 시나리오: 새 기능 추가

```bash
# 1. 규칙 확인
> /check-rules 새 기능 추가

# 2. 기능 구현
> /sc:implement "게시글 북마크"

# Claude가 자동으로:
# - Core 모듈 작업, Persistence 모듈 작업, API 모듈 작업, 테스트 작성

# 3. 검증
> /review, /test

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

### 커스텀 명령어
✅ **단일 책임** - 한 명령어는 한 가지 작업
✅ **명확한 이름** - /review, /test 등

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

## 실습 체크리스트

### 완료 확인

- [ ] 루트 CLAUDE.md 생성
- [ ] 모듈별 CLAUDE.md 생성
- [ ] .claude/rules.md 작성
- [ ] 커스텀 명령어 3개 이상 생성
- [ ] 커스텀 명령어 테스트
- [ ] 새 Claude 세션에서 동작 확인

**모두 완료하면 다음 세션으로!**

---

## 참고 자료

### 문서
- [Claude Code 공식 문서](https://docs.claude.com/en/docs/claude-code/overview)

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
