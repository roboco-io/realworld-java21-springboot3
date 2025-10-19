# GitHub Actions Workflows

이 디렉토리는 프로젝트의 CI/CD 워크플로우를 정의합니다.

## 워크플로우 목록

### CI Workflow (`ci.yml`)

main 브랜치에 push되거나 Pull Request가 생성될 때 자동으로 실행됩니다.

#### 실행 조건
- `push` 이벤트: main 브랜치
- `pull_request` 이벤트: main 브랜치 대상

#### 실행 단계

1. **코드 체크아웃**
   - 최신 코드를 가져옵니다

2. **JDK 21 설정**
   - Temurin 배포판 사용
   - Gradle 캐시 활성화로 빌드 속도 향상

3. **Gradle Build**
   - `./gradlew build` 한 번의 명령으로 모든 검증 수행
   - 자동으로 포함되는 검증 단계:
     - `spotlessCheck`: 코드 포맷팅 검증
     - `checkstyleMain`, `checkstyleTest`: 린트 검증
     - `test`: 모든 단위 테스트 실행
     - `jacocoTestReport`: 커버리지 측정
   - 어느 단계에서든 실패하면 빌드 중단

4. **테스트 결과 업로드**
   - 테스트 결과 XML 파일을 artifact로 저장
   - 빌드 실패해도 업로드 (if: always())

## 로컬에서 CI 검증하기

CI와 동일한 검증을 로컬에서 실행할 수 있습니다:

```bash
# CI와 완전히 동일한 명령어 - 이것만 실행하면 됩니다!
./gradlew build

# build 태스크는 자동으로 다음을 실행합니다:
# - spotlessCheck (코드 포맷팅)
# - checkstyleMain, checkstyleTest (린트)
# - test (테스트)
# - jacocoTestReport (커버리지)
```

### 개별 검증 실행 (선택사항)

특정 단계만 확인하고 싶을 때:

```bash
# 코드 포맷팅만 검증
./gradlew spotlessCheck

# 린트만 검증
./gradlew checkstyleMain checkstyleTest

# 테스트만 실행
./gradlew test

# 커버리지 리포트만 생성
./gradlew jacocoTestReport
open build/reports/jacoco/test/html/index.html
```

## 빌드 상태 확인

- 빌드 상태는 README.md 상단의 배지로 확인 가능
- 자세한 로그는 GitHub Actions 탭에서 확인

## 트러블슈팅

### Spotless 실패
```bash
# 로컬에서 자동 수정
./gradlew spotlessApply

# 다시 커밋
git add .
git commit -m "style: Apply Spotless formatting"
```

### Checkstyle 실패
- 리포트 확인: `build/reports/checkstyle/main.html`
- 120자 제한, 메서드 길이, 파라미터 개수 등 확인
- 수동으로 코드 수정 필요

### 테스트 실패
```bash
# 로컬에서 테스트 실행
./gradlew test

# 실패한 테스트 확인
cat build/test-results/test/*.xml
```

### 커버리지 부족
- 테스트 추가 작성
- 중요한 비즈니스 로직에 우선순위
- 리포트 확인: `build/reports/jacoco/jacocoRootReport/html/index.html`

## 권장 사항

1. **커밋 전 로컬 검증**
   - 항상 `./gradlew build` 성공 확인 후 커밋
   - CI 실패로 인한 재작업 방지

2. **PR 생성 전 확인**
   - 모든 테스트 통과 확인
   - 커버리지 요구사항 충족 확인

3. **빌드 실패 시 즉시 수정**
   - main 브랜치의 빌드는 항상 성공 상태 유지
   - 실패한 빌드는 최우선으로 수정
