---
marp: true
theme: default
paginate: true
header: '테스트 커버리지 측정 및 개선'
footer: 'Claude Code 바이브 코딩 워크숍'
---

# 테스트 커버리지 측정 및 개선
## Claude Code와 함께 코드 품질 향상하기

---

## 이번 세션의 목표

### 바이브 코딩으로 테스트 커버리지 측정 및 개선
- ✅ Claude Code에게 JaCoCo 설정 요청하기
- ✅ 커버리지 측정 및 분석 요청하기
- ✅ 테스트 코드 자동 생성하기
- ✅ 반복 작업 자동화하기

### 실습 결과물
- JaCoCo로 커버리지 측정 환경 구축
- TagController 테스트 작성
- 커버리지 개선 (Before/After 비교)

---

## 왜 테스트 커버리지가 중요한가?

### 품질 지표
- ✅ **버그 감소** - 테스트된 코드는 안전
- ✅ **리팩토링 안전성** - 변경 시 회귀 방지
- ✅ **문서화 효과** - 테스트가 사용 예시
- ✅ **신뢰성 향상** - 배포 자신감

### 주의사항
- ⚠️ 100% ≠ 완벽한 테스트
- ⚠️ 테스트의 **질**이 더 중요

**바이브 코딩으로 효율적으로 관리하자!**

---

## 1단계: Claude Code에게 JaCoCo 설정 요청하기

**핵심 원칙**: 명확한 의도 전달

```
JaCoCo 플러그인을 설정해줘.
요구사항:
- 루트 build.gradle.kts에 플러그인 추가
- 모든 서브프로젝트에 JaCoCo 적용
- 테스트 후 자동으로 리포트 생성
- 전체 프로젝트 통합 리포트 생성 태스크 추가
- HTML과 XML 리포트 둘 다 생성
- JaCoCo 버전은 0.8.11 사용

.claude/rules.md의 빌드 규칙을 따라줘.
```

---

## Claude Code의 응답

### 자동 설정 완료

```
✅ build.gradle.kts에 jacoco 플러그인 추가
✅ allprojects에 jacoco 설정 적용
✅ subprojects에 테스트 리포트 자동 생성 설정
✅ jacocoRootReport 태스크 생성

다음 명령어로 커버리지를 측정할 수 있습니다:
./gradlew clean test jacocoRootReport
```

**5분 만에 완료!** 수동 설정보다 3-4배 빠름

---

## 설정 결과 확인

### 생성된 파일 확인

```bash
# build.gradle.kts 확인
cat build.gradle.kts
```

### 핵심 설정 내용
- `jacoco` 플러그인 활성화
- `jacocoRootReport` 태스크 생성
- HTML/XML 리포트 자동 생성

**Claude Code가 모든 설정을 완료했습니다!**

---

## 2단계: 초기 커버리지 측정

```bash
# 전체 테스트 및 커버리지 생성
./gradlew clean test jacocoRootReport

# 리포트 열기
open build/reports/jacoco/aggregate/index.html
```

**예상 소요 시간**: 30초 ~ 1분

---

## 측정 결과 분석 (Before)

### 전체 커버리지

| 메트릭 | 커버리지 |
|--------|---------|
| **Instruction** | **46%** |
| **Branch** | **49%** |

### 패키지별 상태

| 패키지 | 커버리지 | 상태 |
|--------|---------|------|
| `service` | 100% | ✅ 완벽 |
| `api` | **9%** | ❌ 매우 낮음 (문제) |
| `persistence` | 11% | ❌ 낮음 |

---

## 3단계: Claude Code에게 분석 요청하기

```
JaCoCo 리포트를 분석해줘.

다음을 알려줘:
1. 커버리지가 가장 낮은 패키지 5개
2. 각 패키지의 문제점
3. 개선 우선순위 추천
4. 빠르게 개선할 수 있는 Quick Win 찾기

테스트 작성이 쉽고 효과가 큰 것부터 추천해줘.
```

---

## Claude Code의 분석 결과

### 우선순위 추천

```
📊 분석 결과:

우선순위 1: TagController (9%)
- 이유: 메서드 1개, 의존성 최소, 10분 내 작성 가능
- 효과: API 패키지 커버리지 개선

우선순위 2: UserController
- 이유: 인증 관련 핵심 기능
- 주의: Security 설정 필요

우선순위 3: ArticleController
- 이유: 핵심 비즈니스 로직
- 주의: 복잡도가 높아 시간 소요

👉 TagController부터 시작하는 것을 추천합니다!
```

---

## 4단계: 테스트 작성 요청하기

**핵심 원칙**: 컨텍스트 제공

```
/sc:test TagController에 대한 테스트를 작성해줘.

요구사항:
- @WebMvcTest 사용
- Security 필터 비활성화 (addFilters = false)
- TagService는 @MockBean으로 모킹
- 정상 케이스: 태그 3개 반환
- 엣지 케이스: 빈 배열 반환
- JsonPath로 응답 검증
- Given-When-Then 패턴 사용
- DisplayName으로 테스트 설명 추가

.claude/rules.md의 테스트 규칙을 따라줘.
```

---

## 5단계: 테스트 코드 리뷰

### Claude Code와 대화하기

```
/sc:analyze 테스트 코드를 리뷰해줘.

다음을 확인해줘:
1. Given-When-Then 패턴이 명확한가?
2. 엣지 케이스를 충분히 커버하는가?
3. .claude/rules.md의 테스트 규칙을 따르는가?
4. 개선할 점이 있는가?
```

---

## 테스트 실행

### 단일 테스트 실행

```bash
./gradlew :realworld:test --tests "*.TagControllerTest"
```

### 결과 확인

```
TagController 테스트 > GET /api/tags - 모든 태그를 조회한다 PASSED
TagController 테스트 > GET /api/tags - 빈 배열 반환 PASSED

BUILD SUCCESSFUL in 8s
```

**성공!** Claude Code가 작성한 테스트가 모두 통과

---

## 커버리지 재측정

### After 측정

```bash
./gradlew clean test jacocoRootReport
open build/reports/jacoco/aggregate/index.html
```

### 결과 확인
- API 패키지: 9% → 10% (+1%)
- Response DTO: 10% → 16% (+6%)
- 총 26개 명령어 커버

**간접 효과**: TagsResponse도 함께 커버됨!

---

## Before vs After 비교

### 투자 대비 효과 (ROI)

```
투자: 10분 (Claude Code와 대화)
효과:
  - 테스트 2개 자동 생성
  - API 패키지 커버리지 +11% 증가율
  - Response DTO 커버리지 +60% 증가율

수동 작성 대비: 3-4배 빠름
에러 가능성: 낮음 (규칙 준수)
```

**바이브 코딩의 힘!**

---

## 다음 개선 대상

### 우선순위 리스트

```
✅ TagController (완료)
⬜ UserController (다음)
⬜ ArticleController
⬜ ArticleFavoriteController
⬜ ArticleCommentController
```

### 목표 커버리지
- API 레이어: 9% → **70%**
- 전체 프로젝트: 46% → **70%**

**Claude Code와 함께라면 가능!**

---

## Claude Code 활용 꿀팁

### 커버리지 기준 설정

```
커버리지 검증 태스크를 추가해줘.

요구사항:
- 전체 프로젝트: 70% 이상
- 패키지별: 60% 이상
- 빌드 시 자동 검증
- 기준 미달 시 빌드 실패

jacocoTestCoverageVerification 태스크를 사용해줘.
```

---

## 실습 체크리스트

### 완료 확인

- [ ] Claude Code에게 JaCoCo 설정 요청
- [ ] 초기 커버리지 측정 (Before)
- [ ] Claude Code에게 분석 요청
- [ ] TagController 테스트 작성 요청
- [ ] 테스트 실행 및 통과 확인
- [ ] 커버리지 재측정 (After)
- [ ] Before/After 비교 분석

**모두 완료하면 다음 세션으로!**

---

## Q&A

### 질문이 있으신가요?

- Claude Code에게 어떻게 요청하는가?
- 에러 발생 시 어떻게 대화하는가?
- 반복 작업을 어떻게 자동화하는가?
- 다음 단계는 무엇인가?

---

# 테스트 커버리지 개선 완료! 🎉

## 다음: CI/CD 자동화
