---
marp: true
theme: default
paginate: true
header: 'CI/CD 워크플로우 구축'
footer: 'Claude Code 바이브 코딩 워크숍'
---

# CI/CD 워크플로우 구축
## Claude Code와 함께 품질 자동화하기

---

## 이번 세션의 목표

### 바이브 코딩으로 CI/CD 구축하기
- ✅ Claude Code에게 Checkstyle 설정 요청하기
- ✅ GitHub Actions 워크플로우 생성 요청하기
- ✅ PR 자동화 시스템 구축하기
- ✅ 자동 품질 검증 시스템 구축하기

### 실습 결과물
- Checkstyle 린터 설정
- GitHub Actions CI 파이프라인
- PR 자동 코멘트 시스템

**예상 소요 시간**: 45분

---

## 현재 상황의 문제점

### 수동 품질 관리의 한계

```
개발자: 코드 작성
개발자: ./gradlew spotlessApply (잊어버림)
개발자: git commit && git push

→ main 브랜치에 포맷 안 맞는 코드
→ 다른 개발자가 발견
→ 수정 요청 → 다시 작업
→ 비효율적!
```

**해결책**: 자동화!

---

## 목표: 품질 검증 워크플로우 구축하기

### 이상적인 워크플로우

```
개발자: 코드 작성
개발자: git push

→ GitHub Actions 자동 실행
→ Spotless 검증 ✅
→ Checkstyle 검증 ✅
→ 테스트 실행 ✅
→ 커버리지 측정 ✅
→ 모두 통과 시 merge 허용

실패 시 → PR에 자동 코멘트 → 개발자 수정
```

**바이브 코딩으로 이 모든 걸 자동화!**

---

## Spotless vs Checkstyle

### 역할 분담

**Spotless (자동 수정 가능)**:
- 들여쓰기, 공백, import 정렬
- 코드 포맷팅
- `./gradlew spotlessApply`로 자동 수정

**Checkstyle (수동 수정 필요)**:
- 파일 길이, 메서드 길이
- 파라미터 개수, 복잡도
- 설계 개선 필요

**함께 사용해야 완전한 품질 보장!**

---

## 1단계: Checkstyle 설정 요청

### Claude Code와 대화하기

```
Checkstyle 설정을 추가해줘.

요구사항:
1. config/checkstyle/checkstyle.xml 파일 생성
2. 다음 규칙 포함:
   - 파일 길이: 최대 500줄
   - 라인 길이: 최대 120자
   - 메서드 길이: 최대 150줄
   - 파라미터 개수: 최대 7개
3. build.gradle.kts에 checkstyle 플러그인 추가
4. 빌드 시 자동 검증되도록 설정

.claude/rules.md의 빌드 규칙을 따라줘.
```

---

## Claude Code의 응답

### 자동 설정 완료

```
✅ config/checkstyle/checkstyle.xml 생성
✅ build.gradle.kts에 checkstyle 플러그인 추가
✅ 빌드 시 자동 검증 설정 완료

다음 명령어로 Checkstyle을 실행할 수 있습니다:
./gradlew checkstyleMain checkstyleTest

리포트 위치:
build/reports/checkstyle/main.html
```

**5분 만에 완료!**

---

## Checkstyle 실행 및 확인

### 검증 실행

```bash
# Checkstyle 실행
./gradlew checkstyleMain checkstyleTest

# 리포트 열기
open build/reports/checkstyle/main.html
```

### 위반 사항 발견 시

```
[ERROR] LineLength: Line is longer than 120 characters
[ERROR] MethodLength: Method length is 180 (max: 150)
[ERROR] ParameterNumber: More than 7 parameters
```

---

## 2단계: Checkstyle 위반 수정 요청

### Claude Code에게 수정 요청

```
Checkstyle 검증 결과:
- ArticleController.java:45 - 라인 길이 초과 (135자)
- UserService.java:120 - 메서드 길이 초과 (180줄)

다음을 수정해줘:
1. 긴 라인은 적절히 분리
2. 긴 메서드는 작은 메서드로 분할
3. 가독성을 유지하면서 리팩토링

.claude/rules.md의 코딩 규칙을 따라줘.
```

**Claude Code가 자동으로 리팩토링!**

---

## 3단계: GitHub Actions 워크플로우 생성 요청

### Claude Code와 대화하기

**핵심 원칙**: 원하는 모든 단계를 명확히 나열

```
GitHub Actions CI 워크플로우를 생성해줘.

파일: .github/workflows/ci.yml

실행 트리거:
- main 브랜치에 push, PR 생성

검증 단계 (순서대로):
1. 코드 체크아웃
2. JDK 21 설정
3. Gradle 빌드
4. 테스트 실행
5. JaCoCo 커버리지 측정
```

---

## Claude Code의 응답

### 워크플로우 생성 완료

```yaml
✅ .github/workflows/ci.yml 생성

주요 기능:
- Spotless 검증 + 실패 시 PR 코멘트
- Checkstyle 검증 + 리포트 업로드
- 테스트 실행 + 결과 업로드
- 커버리지 측정 + PR 코멘트

다음 명령어로 커밋하세요:
git add .github/workflows/ci.yml
git commit -m "ci: Add GitHub Actions workflow"
git push origin main
```

**10분 만에 완성!**

---

## 워크플로우 테스트

### 첫 실행

```bash
# 1. 파일 추가 및 커밋
git add .github/workflows/ci.yml \
        config/checkstyle/checkstyle.xml \
        build.gradle.kts

git commit -m "ci: Add CI/CD workflow

- Add Checkstyle configuration
- Add GitHub Actions workflow
- Add automated quality checks"

# 2. 푸시
git push origin main
```

---

## GitHub에서 확인

### Actions 탭에서 모니터링

**확인 사항**:
1. Repository → Actions 탭 이동
2. "CI" 워크플로우 실행 확인
3. 각 단계별 로그 확인
4. 성공/실패 여부 확인

**모든 단계가 ✅로 표시되면 성공!**

---

## 4단계: PR 워크플로우 테스트

### Feature 브랜치 생성

```bash
# 새 브랜치 생성
git checkout -b feature/add-bookmark

# (BookmarkService 코드 작성)
# 일부러 Spotless 규칙 위반 (들여쓰기 엉망)

# 커밋 및 푸시
git add .
git commit -m "feat: Add bookmark feature"
git push origin feature/add-bookmark
```

---

## PR 생성 및 자동 검증

### GitHub에서 PR 생성

**자동으로 실행되는 것들**:
1. ✅ Spotless 검증
2. ✅ Checkstyle 검증
3. ✅ 빌드 및 테스트
4. ✅ 커버리지 측정

### 실패 시 자동 코멘트

```
❌ Spotless check failed!

Please run `./gradlew spotlessApply` locally
and commit the changes.
```

**PR에 자동으로 코멘트 달림!**

---

## 5단계: 에러 수정

### Claude Code에게 요청

```
PR에서 Spotless 검증이 실패했어.

다음 작업을 해줘:
1. ./gradlew spotlessApply 실행
2. 변경된 파일 커밋
3. 커밋 메시지: "style: Apply Spotless formatting"

그리고 푸시해줘.
```

### Claude Code의 응답

```bash
✅ Spotless 포맷팅 적용
✅ 변경 사항 커밋
✅ 푸시 완료

PR에서 CI가 자동으로 재실행됩니다.
```

---

## 커버리지 자동 코멘트

### PR에 커버리지 리포트 표시

**자동으로 추가되는 코멘트**:

```
📊 Coverage Report

Overall Coverage: 72%
Changed Files Coverage: 85%

✅ All coverage requirements met!

Files:
- BookmarkService.java: 90%
- BookmarkController.java: 80%
```

**변경된 파일의 커버리지를 자동으로 알려줌!**

---

## 6단계: Branch Protection 설정

### GitHub Settings에서 설정

**Settings → Branches → Add rule**

```
Branch name pattern: main

규칙:
✅ Require status checks to pass before merging
   - CI / quality-check
✅ Require branches to be up to date
✅ Require linear history
```

**효과**: CI 통과하지 않으면 merge 불가!

---

## 전체 워크플로우 흐름

### 품질 검증 워크플로우

```
개발자: Feature 브랜치에서 코드 작성
   ↓
개발자: git push
   ↓
GitHub Actions: 자동 실행
   ├─ Spotless ✅ → 통과
   ├─ Checkstyle ✅ → 통과
   ├─ Build ✅ → 통과
   ├─ Test ✅ → 통과
   └─ Coverage ✅ → 통과
   ↓
GitHub: PR에 커버리지 코멘트 자동 추가
   ↓
개발자: Merge (또는 수정)
```

**모든 게 자동!**

---

## Before vs After 비교

### 투자 대비 효과 (ROI)

| 지표 | Before | After | 개선 |
|------|--------|-------|------|
| **PR 리뷰 시간** | 2시간 | 30분 | -75% |
| **코드 스타일 이슈** | 10건/주 | 0건 | -100% |
| **빌드 실패율** | 15% | 2% | -87% |
| **수동 검증 시간** | 4시간/주 | 0시간 | -100% |

**총 시간 절약**: 주당 약 6시간

---

## CI/CD 최적화 팁

### 캐싱 추가 요청

```
GitHub Actions 워크플로우에 Gradle 캐싱을 추가해줘.

요구사항:
- Gradle 패키지 캐싱
- Wrapper 캐싱
- 빌드 시간 단축

actions/cache@v4 사용해줘.
```

**빌드 시간 50% 단축!**

---

## 실습 체크리스트

### 완료 확인

- [ ] Checkstyle 설정 요청
- [ ] 로컬에서 Checkstyle 실행 확인
- [ ] GitHub Actions 워크플로우 생성 요청
- [ ] main 브랜치에 워크플로우 푸시
- [ ] GitHub Actions 실행 확인
- [ ] Feature 브랜치로 PR 테스트
- [ ] 실패 시 자동 코멘트 확인
- [ ] Branch Protection 설정

**모두 완료하면 CI/CD 마스터!**

---

## 핵심 요약

### 바이브 코딩으로 CI/CD 구축

**시간 절약**:
- 수동 설정: 4-5시간
- Claude Code: 30분
- **8-10배 빠름**

**품질 향상**:
- 자동 검증으로 실수 방지
- 일관된 코드 품질 유지
- PR 리뷰 시간 단축

**학습 효과**:
- CI/CD 모범 사례 학습
- GitHub Actions 패턴 이해

---

## 성과 분석

### 바이브 코딩의 효과

**투자**: 30분 (Claude Code와 대화)

**효과**:
- Checkstyle 설정 자동 생성
- GitHub Actions 워크플로우 완성
- PR 자동화 시스템 구축
- 품질 검증 워크플로우 구축

**ROI**: 30분 투자 → 주당 6시간 절약 = **2400% ROI**

---

## Q&A

### 질문이 있으신가요?

- Claude Code에게 어떻게 CI/CD를 요청하는가?
- 에러 발생 시 어떻게 대화하는가?
- 반복 작업을 어떻게 자동화하는가?
- 실무에 어떻게 적용하는가?

---

# CI/CD 구축 완료! 🎉
