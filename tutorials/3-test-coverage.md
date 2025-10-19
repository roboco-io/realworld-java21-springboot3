---
marp: true
theme: default
paginate: true
header: '테스트 커버리지 측정 및 개선'
footer: 'Claude Code 바이브 코딩 워크숍'
---

# 테스트 커버리지 측정 및 개선
## JaCoCo로 코드 품질 향상하기

From 46% to 70%+ Coverage

---

## 이번 세션의 목표

### 학습 목표
- ✅ JaCoCo 설정 및 사용법
- ✅ 커버리지 리포트 분석 방법
- ✅ 효과적인 테스트 작성 전략
- ✅ 커버리지 개선 실전 연습

### 실습 목표
- JaCoCo 플러그인 설정
- 전체 프로젝트 커버리지 측정
- 커버리지 낮은 영역 개선
- Before/After 비교

**예상 소요 시간**: 60분

---

## 테스트 커버리지란?

### 정의
**코드의 몇 %가 테스트되었는가?**

```java
public class Calculator {
    public int add(int a, int b) {    // Line 1
        return a + b;                  // Line 2
    }

    public int subtract(int a, int b) { // Line 3
        return a - b;                   // Line 4
    }
}

// 테스트
@Test
void testAdd() {
    assertEquals(5, calculator.add(2, 3));
}

// 커버리지: 50% (Line 1-2만 테스트, Line 3-4는 미테스트)
```

---

## 왜 커버리지가 중요한가?

### 품질 지표
- ✅ **버그 감소** - 테스트된 코드는 안전
- ✅ **리팩토링 안전성** - 변경 시 회귀 방지
- ✅ **문서화 효과** - 테스트가 사용 예시
- ✅ **신뢰성 향상** - 배포 자신감

### 주의사항
- ⚠️ 100% ≠ 완벽한 테스트
- ⚠️ 커버리지만으로는 품질 보장 불가
- ⚠️ 테스트의 **질**이 더 중요

---

## JaCoCo란?

### Java Code Coverage Library

**특징**:
- ✅ 무료 오픈소스
- ✅ Gradle/Maven 통합
- ✅ 다양한 메트릭
- ✅ HTML/XML 리포트

**메트릭 종류**:
- **Instruction**: 바이트코드 명령어 (가장 정확)
- **Branch**: if/switch 분기
- **Line**: 소스 코드 라인
- **Method**: 메서드 실행
- **Class**: 클래스 실행

---

## JaCoCo 메트릭 이해

### Instruction Coverage (권장)

```java
public String getName(User user) {
    return user != null ? user.getName() : "Guest";
}

// 바이트코드 (간략화):
// 1. ALOAD 1        (user 로드)
// 2. IFNULL L1      (null 체크)
// 3. INVOKEVIRTUAL  (getName 호출)
// 4. GOTO L2
// 5. L1: LDC "Guest"
// 6. L2: ARETURN

// 테스트가 user != null만 커버 → 50% instruction coverage
```

**Why Instruction?** 가장 세밀하고 정확한 측정

---

## JaCoCo 메트릭 이해 (계속)

### Branch Coverage

```java
public int calculate(int a, int b, boolean add) {
    if (add) {              // Branch 1
        return a + b;
    } else {                // Branch 2
        return a - b;
    }
}

// 테스트가 add=true만 커버 → 50% branch coverage
```

### Line Coverage

```java
System.out.println("A"); doSomething(); doOther(); // 1 line, 3 instructions

// Line coverage는 부정확할 수 있음
```

---

## JaCoCo 설정: build.gradle.kts

### 1단계: 플러그인 추가

```kotlin
// 루트 build.gradle.kts

plugins {
    java
    jacoco  // JaCoCo 플러그인 추가
    alias(libs.plugins.spotless)
    alias(libs.plugins.spring.boot) apply false
}

allprojects {
    group = "io.zhc1"

    plugins.apply("jacoco")  // 모든 프로젝트에 적용

    jacoco {
        toolVersion = "0.8.11"  // JaCoCo 버전
    }
}
```

---

## JaCoCo 설정: build.gradle.kts (계속)

### 2단계: 리포트 생성 설정

```kotlin
subprojects {
    tasks.withType<Test> {
        useJUnitPlatform()
        finalizedBy(tasks.jacocoTestReport)  // 테스트 후 리포트 생성
    }

    tasks.jacocoTestReport {
        dependsOn(tasks.test)
        reports {
            xml.required.set(true)   // CI/CD용 XML
            html.required.set(true)  // 사람용 HTML
        }
    }
}
```

---

## JaCoCo 설정: 통합 리포트

### 3단계: 전체 프로젝트 리포트

```kotlin
// 루트 build.gradle.kts

tasks.register<JacocoReport>("jacocoRootReport") {
    dependsOn(subprojects.map { it.tasks.withType<Test>() })

    // 모든 서브프로젝트의 실행 데이터 수집
    executionData(subprojects.flatMap {
        it.tasks.withType<Test>().map { task ->
            task.extensions.getByType<JacocoTaskExtension>().destinationFile
        }
    })

    // 소스 및 클래스 디렉토리 설정
    subprojects.forEach { subproject ->
        val sourceSets = subproject.extensions.getByType<SourceSetContainer>()
        sourceDirectories.from(sourceSets.getByName("main").allSource.srcDirs)
        classDirectories.from(sourceSets.getByName("main").output)
    }

    reports {
        xml.required.set(true)
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/aggregate"))
    }
}
```

---

## Claude Code로 JaCoCo 설정

### 자동 설정 요청

```
> JaCoCo 플러그인을 설정해줘.
  다음 요구사항을 따라줘:

  1. 루트 build.gradle.kts에 플러그인 추가
  2. 모든 서브프로젝트에 JaCoCo 적용
  3. 테스트 후 자동으로 리포트 생성
  4. 전체 프로젝트 통합 리포트 생성 태스크 추가
  5. HTML과 XML 리포트 둘 다 생성

  JaCoCo 버전은 0.8.11을 사용해줘.
```

**Claude가 자동으로 모든 설정 완료!**

---

## 커버리지 측정: 실습

### 초기 측정

```bash
# 1. Gradle 데몬 재시작 (설정 변경 후)
./gradlew --stop

# 2. 전체 테스트 및 커버리지 생성
./gradlew clean test jacocoRootReport

# 3. 리포트 열기
open build/reports/jacoco/aggregate/index.html
```

**예상 소요 시간**: 30초 ~ 1분

---

## 커버리지 리포트 구조

### HTML 리포트 탐색

```
build/reports/jacoco/aggregate/
├── index.html                    # 메인 리포트
├── io.zhc1.realworld/            # 패키지별
│   ├── service/                  # 서비스 패키지
│   │   └── ArticleService.html  # 클래스 상세
│   ├── model/
│   ├── api/
│   └── persistence/
└── jacoco-resources/             # CSS, JS
```

### 색상 의미
- 🟢 **초록색**: 커버된 코드
- 🟡 **노란색**: 부분 커버 (일부 분기만)
- 🔴 **빨간색**: 미커버 코드

---

## 초기 측정 결과 분석

### 전체 커버리지 (Before)

| 메트릭 | 커버리지 | 상세 |
|--------|---------|------|
| **Instruction** | **46%** | 1,665 / 3,599 |
| **Branch** | **49%** | 145 / 292 |
| **Line** | **48%** | 355 / 739 |
| **Method** | **50%** | 155 / 309 |
| **Class** | **61%** | 41 / 67 |

**해석**: 절반의 코드만 테스트됨

---

## 패키지별 커버리지 분석

### 결과 테이블

| 패키지 | Instruction | 상태 | 우선순위 |
|--------|-------------|------|----------|
| `service` | **100%** | ✅ 완벽 | - |
| `model` | 66% | ⚠️ 양호 | 중 |
| `config` | 57% | ⚠️ 보통 | 중 |
| `api.response` | 10% | ❌ 낮음 | 높음 |
| `api` | **9%** | ❌ 매우 낮음 | **최우선** |
| `persistence` | 11% | ❌ 낮음 | 높음 |
| `api.request` | 0% | ❌ 없음 | 높음 |

**문제**: API 레이어가 거의 테스트되지 않음!

---

## 커버리지 개선 전략

### 1. ROI 분석 (투자 대비 효과)

**효과성 = 영향도 × (100 - 현재커버리지) / 복잡도**

### 2. 우선순위 매트릭스

```
높은 영향도 + 낮은 커버리지 = 최우선
   ↓
   API 컨트롤러 (9%)
   Persistence (11%)
   Request DTO (0%)
```

### 3. Quick Win 선정
**TagController**: 단순 + 효과적 + 빠름

---

## 테스트 대상 선정: TagController

### 분석

```java
@RestController
@RequiredArgsConstructor
class TagController {
    private final TagService tagService;

    @GetMapping("/api/tags")
    TagsResponse getAllTags() {
        return new TagsResponse(tagService.getAllTags());
    }
}
```

**선정 이유**:
- ✅ **단순성**: 메서드 1개
- ✅ **의존성 최소**: TagService만
- ✅ **빠른 성공**: 10분 내 작성 가능
- ✅ **명확한 효과**: 측정 가능

---

## 테스트 코드 작성: 준비

### 테스트 전략

**Given-When-Then 패턴**:
1. **Given**: 태그 3개 존재
2. **When**: GET /api/tags 호출
3. **Then**: 200 OK, 3개 태그 배열 반환

### Spring MVC 테스트

```java
@WebMvcTest(controllers = TagController.class)
@AutoConfigureMockMvc(addFilters = false)  // Security 비활성화
@DisplayName("TagController 테스트")
class TagControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TagService tagService;
}
```

---

## 테스트 코드 작성: 구현

### 정상 케이스

```java
@Test
@DisplayName("GET /api/tags - 모든 태그를 조회한다")
void getAllTags_ShouldReturnAllTags() throws Exception {
    // given
    Tag tag1 = new Tag("java");
    Tag tag2 = new Tag("spring");
    Tag tag3 = new Tag("testing");

    given(tagService.getAllTags())
        .willReturn(List.of(tag1, tag2, tag3));

    // when & then
    mockMvc.perform(get("/api/tags"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.tags").isArray())
        .andExpect(jsonPath("$.tags.length()").value(3))
        .andExpect(jsonPath("$.tags[0]").value("java"))
        .andExpect(jsonPath("$.tags[1]").value("spring"))
        .andExpect(jsonPath("$.tags[2]").value("testing"));
}
```

---

## 테스트 코드 작성: 엣지 케이스

### 빈 데이터 케이스

```java
@Test
@DisplayName("GET /api/tags - 태그가 없을 때 빈 배열을 반환한다")
void getAllTags_WhenNoTags_ShouldReturnEmptyArray() throws Exception {
    // given
    given(tagService.getAllTags()).willReturn(List.of());

    // when & then
    mockMvc.perform(get("/api/tags"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.tags").isArray())
        .andExpect(jsonPath("$.tags.length()").value(0));
}
```

**엣지 케이스를 항상 고려!**

---

## Claude Code로 테스트 작성

### 자동 생성 요청

```
> TagController에 대한 테스트를 작성해줘.
  다음 요구사항을 따라줘:

  1. @WebMvcTest 사용
  2. Security 필터 비활성화
  3. TagService는 @MockBean으로 모킹
  4. 정상 케이스: 태그 3개 반환
  5. 엣지 케이스: 빈 배열 반환
  6. JsonPath로 응답 검증
  7. Given-When-Then 패턴 사용
  8. DisplayName 추가

  .claude/rules.md의 테스트 규칙을 따라줘.
```

---

## 테스트 실행

### 단일 테스트 실행

```bash
# 특정 테스트 클래스만
./gradlew :realworld:test --tests "*.TagControllerTest"

# 성공 출력:
# TagController 테스트 > GET /api/tags - 모든 태그를 조회한다 PASSED
# TagController 테스트 > GET /api/tags - 태그가 없을 때... PASSED
#
# BUILD SUCCESSFUL in 8s
```

---

## 문제 해결: 401 Unauthorized

### 증상

```
java.lang.AssertionError: Status expected:<200> but was:<401>
```

### 원인
Spring Security가 인증 요구

### 해결

```java
@WebMvcTest(controllers = TagController.class)
@AutoConfigureMockMvc(addFilters = false)  // ← 이 라인 추가
class TagControllerTest {
    // ...
}
```

**`addFilters = false`로 Security 필터 비활성화**

---

## 커버리지 재측정

### After 측정

```bash
# 전체 테스트 및 커버리지
./gradlew clean test jacocoRootReport

# 리포트 열기
open build/reports/jacoco/aggregate/index.html
```

---

## Before vs After 비교

### API 패키지 (io.zhc1.realworld.api)

| 메트릭 | Before | After | 변화 |
|--------|--------|-------|------|
| Instructions | 57 / 592 (9%) | 64 / 592 (10%) | **+7 (+1%)** |
| Lines | 108 / 114 | 107 / 114 | +1 |
| Methods | 21 / 27 | 20 / 27 | +1 |

### Response DTO (io.zhc1.realworld.api.response)

| 메트릭 | Before | After | 변화 |
|--------|--------|-------|------|
| Instructions | 30 / 289 (10%) | 49 / 289 (16%) | **+19 (+6%)** |
| Lines | 58 / 61 | 55 / 61 | +3 |

**간접 효과**: DTO도 함께 커버됨!

---

## 성과 분석

### 정량적 성과

```
투자: 테스트 2개 (약 10분)
효과:
  - API 패키지: 9% → 10% (+11% 증가율)
  - Response DTO: 10% → 16% (+60% 증가율)
  - 총 26개 명령어 커버

ROI: 60% 증가 / 10분 = 6% 증가/분 (매우 효율적!)
```

### 정성적 성과
- ✅ TagController 완전히 커버
- ✅ TagsResponse 생성자 커버
- ✅ 회귀 방지 테스트 확보
- ✅ API 사용 예시 문서화

---

## 학습 포인트

### 1. 작은 것부터 시작

```
❌ ArticleController (복잡, 20개 메서드)
✅ TagController (단순, 1개 메서드)

→ 빠른 성공 → 동기 부여 → 지속 가능
```

### 2. 간접 효과 활용

```
TagController 테스트 작성
  ↓
TagsResponse 생성자 자동 커버
  ↓
Tag.getName() 자동 커버
  ↓
1개 테스트로 3개 클래스 커버
```

---

## 학습 포인트 (계속)

### 3. 메트릭 선택

**Instruction Coverage 사용 이유**:
- ✅ 가장 정확
- ✅ 컴파일러 최적화 무관
- ✅ 바이트코드 기반

**Line Coverage 문제**:
```java
// 1 line, 3 instructions
System.out.println("A"); doSomething(); doOther();

// Line coverage: 100%
// Instruction coverage: 33% (첫 번째만 테스트)
```

---

## 학습 포인트 (계속)

### 4. Given-When-Then 패턴

**명확한 구조**:
```java
@Test
void testName() {
    // Given - 준비
    Tag tag = new Tag("java");
    given(service.get()).willReturn(tag);

    // When - 실행
    var result = controller.getTags();

    // Then - 검증
    assertThat(result.tags()).contains("java");
}
```

**가독성 향상 + 유지보수 용이**

---

## 다음 개선 대상

### 우선순위 리스트

```
✅ TagController (완료)
⬜ UserController
⬜ ArticleController
⬜ ArticleFavoriteController
⬜ ArticleCommentController
⬜ Persistence 레이어
```

### 목표 커버리지
- API 레이어: 10% → **70%**
- Response DTO: 16% → **80%**
- 전체 프로젝트: 46% → **70%**

---

## Claude Code 활용 팁

### 반복 작업 자동화

```
> 다음 컨트롤러들에 대한 테스트를 작성해줘:
  - UserController
  - ArticleController

  TagControllerTest와 동일한 패턴으로 작성해줘.
  각 엔드포인트마다 정상 케이스와 엣지 케이스를 포함해줘.
```

### 커버리지 분석 요청

```
> JaCoCo 리포트를 분석하고
  커버리지가 가장 낮은 상위 5개 클래스를 찾아줘.
  각 클래스에 대한 테스트 작성 우선순위를 추천해줘.
```

---

## 커버리지 기준 설정

### Gradle Task로 강제

```kotlin
tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                minimum = "0.70".toBigDecimal()  // 70% 이상
            }
        }
        rule {
            element = "PACKAGE"
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.60".toBigDecimal()  // 패키지별 60% 이상
            }
        }
    }
}

tasks.check {
    dependsOn(tasks.jacocoTestCoverageVerification)
}
```

---

## 커버리지 기준 설정 (계속)

### 실행

```bash
# 빌드 시 커버리지 검증
./gradlew build

# 기준 미달 시 빌드 실패
# > Task :jacocoTestCoverageVerification FAILED
# Rule violated for bundle: Coverage ratio is 0.46, minimum is 0.70
```

**품질 게이트 역할**

---

## Best Practices

### ✅ DO (권장)

**1. 작은 것부터**
```
단순한 컨트롤러 → 복잡한 컨트롤러
단위 테스트 → 통합 테스트
```

**2. 엣지 케이스 포함**
```java
// 정상 케이스
// null 케이스
// 빈 컬렉션
// 경계값
```

**3. 명확한 검증**
```java
.andExpect(jsonPath("$.tags[0]").value("java"))  // 구체적
```

---

## Best Practices (계속)

### ❌ DON'T (피할 것)

**1. 100% 집착**
```
100% 커버리지 != 완벽한 테스트
테스트의 질 > 커버리지 수치
```

**2. 의미 없는 테스트**
```java
// Bad
@Test
void testGetter() {
    user.getName();  // 검증 없음!
}
```

**3. 커버리지만 보기**
```
커버리지 = 양적 지표
테스트 품질 = 질적 지표
→ 둘 다 중요
```

---

## 트러블슈팅

### 리포트가 생성되지 않음

```bash
# 1. 캐시 삭제
./gradlew clean

# 2. Gradle 데몬 재시작
./gradlew --stop

# 3. 다시 실행
./gradlew test jacocoRootReport

# 4. 리포트 위치 확인
ls -la build/reports/jacoco/aggregate/
```

---

## 트러블슈팅 (계속)

### 특정 클래스가 리포트에 없음

**원인**: Lombok 생성 코드, DTO, Config 클래스
**해결**: 문제 없음 (테스트 불필요한 경우 많음)

### 커버리지가 줄어듦

**원인**: 새 코드 추가 + 테스트 없음
**해결**:
```bash
# 변경된 파일 확인
git diff --name-only main

# 해당 파일 테스트 작성
```

---

## Git 작업: 태그 생성

### 커버리지 개선 저장

```bash
# 변경사항 확인
git status

# 추가
git add build.gradle.kts \
        server/api/src/test/java/io/zhc1/realworld/api/TagControllerTest.java

# 커밋
git commit -m "test: Add TagController test and JaCoCo configuration

- Configure JaCoCo plugin in build.gradle.kts
- Add jacocoRootReport task for aggregated coverage
- Add TagControllerTest with @WebMvcTest
- Coverage improvement: API 9% → 10%, Response DTO 10% → 16%

🤖 Generated with Claude Code"

# 태그
git tag -a v1.2.0-test-coverage -m "Add test coverage with JaCoCo"

# 푸시
git push origin main --tags
```

---

## 다음 단계 미리보기

### CI/CD 자동화

다음 세션에서 배울 내용:
- **Spotless + Checkstyle** 설정
- **GitHub Actions** 워크플로우
- **자동 품질 검증**
- **커버리지 배지** 추가

**자동화 효과**:
- PR마다 자동 테스트
- 커버리지 자동 측정
- 코드 품질 자동 검증

---

## 핵심 요약

### 배운 내용
✅ **JaCoCo 설정** - Gradle 플러그인 통합
✅ **커버리지 측정** - HTML/XML 리포트
✅ **효과적인 테스트** - Given-When-Then 패턴
✅ **개선 전략** - ROI 기반 우선순위

### 성과
📈 API 패키지: 9% → 10%
📈 Response DTO: 10% → 16%
📈 총 26개 명령어 커버
📈 2개 회귀 방지 테스트 확보

---

## 실습 체크리스트

### 완료 확인

- [ ] JaCoCo 플러그인 설정
- [ ] jacocoRootReport 태스크 생성
- [ ] 초기 커버리지 측정 (Before)
- [ ] TagControllerTest 작성
- [ ] 테스트 실행 및 통과 확인
- [ ] 커버리지 재측정 (After)
- [ ] Before/After 비교 분석
- [ ] Git 태그 생성 (v1.2.0-test-coverage)

**모두 완료하면 다음 세션으로!**

---

## 참고 자료

### 문서
- [JaCoCo 공식 문서](https://www.jacoco.org/jacoco/trunk/doc/)
- [Gradle JaCoCo Plugin](https://docs.gradle.org/current/userguide/jacoco_plugin.html)
- [Spring Boot Testing](https://spring.io/guides/gs/testing-web/)

### 도구
- [Codecov](https://codecov.io/) - 커버리지 추적
- [SonarQube](https://www.sonarqube.org/) - 코드 품질 분석

---

## Q&A

### 질문이 있으신가요?

- JaCoCo 설정 관련?
- 커버리지 분석 방법?
- 테스트 작성 전략?
- Claude Code 활용 팁?

---

# 테스트 커버리지 개선 완료! 🎉

## 다음: CI/CD 자동화
### GitHub Actions로 품질 게이트 구축하기
