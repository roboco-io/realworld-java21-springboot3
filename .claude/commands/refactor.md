---
description: 리팩토링 워크플로우
---

코드를 리팩토링합니다.

## 1. 리팩토링 목표 확인
사용자에게 질문:
- 리팩토링 대상 코드
- 개선 목표 (가독성, 성능, 재사용성 등)

## 2. 현재 코드 분석
1. **코드 스멜 식별**
   - 중복 코드
   - 긴 메서드 (20줄 초과)
   - 큰 클래스
   - 복잡한 조건문
   - 매직 넘버/문자열

2. **의존성 분석**
   - 어떤 코드가 사용 중인지
   - 테스트 존재 여부

## 3. 리팩토링 계획
- 단계별 작은 변경
- 각 단계마다 테스트 실행
- 한 번에 하나의 리팩토링만

## 4. 리팩토링 패턴 적용

### 메서드 추출
```java
// Before
public void process() {
    // 복잡한 로직 1
    // 복잡한 로직 2
    // 복잡한 로직 3
}

// After
public void process() {
    step1();
    step2();
    step3();
}
```

### 매직 넘버 제거
```java
// Before
if (user.getAge() > 18) { ... }

// After
private static final int ADULT_AGE = 18;
if (user.getAge() > ADULT_AGE) { ... }
```

### 조건문 단순화
```java
// Before
if (condition1) {
    if (condition2) {
        if (condition3) {
            // ...
        }
    }
}

// After
if (!condition1) return;
if (!condition2) return;
if (!condition3) return;
// ...
```

## 5. 테스트 실행
각 리팩토링 단계 후:
```bash
./gradlew test
```

## 6. 코드 리뷰
- `/review` 명령어로 개선 확인
- `.claude/rules.md` 규칙 준수 확인

## 7. 성능 검증
- 리팩토링 전/후 성능 비교 (필요시)
- SQL 쿼리 개수 확인 (P6Spy 로그)
