---
description: 새 기능 추가 워크플로우
---

새 기능을 추가합니다. 다음 단계를 따라주세요:

## 1. 요구사항 확인
사용자에게 다음을 질문:
- 기능 설명
- 영향받는 모듈 (core, persistence, api)
- API 엔드포인트 필요 여부

## 2. 설계
- 필요한 엔티티/DTO 식별
- 레이어별 구현 계획
- 데이터베이스 변경 필요성

## 3. 구현 순서
다음 순서로 구현:

### 3.1 Core 모듈
1. 도메인 엔티티 추가/수정 (`module/core/model/`)
   - `.claude/rules.md`의 엔티티 작성 규칙 준수
   - 필수 필드 검증 추가
2. 저장소 인터페이스 정의
3. 서비스 구현
   - `@Service`, `@RequiredArgsConstructor` 사용
   - 적절한 예외 처리

### 3.2 Persistence 모듈
1. JPA Repository 인터페이스
2. Repository Adapter 구현
3. 필요시 Specification 추가

### 3.3 API 모듈
1. Request/Response DTO (record 사용)
2. Controller 구현
   - RealWorld 사양 준수
   - 적절한 인증/인가 설정
3. SecurityConfiguration 업데이트 (필요시)

## 4. 테스트
- `/test` 명령어로 테스트 생성 및 실행
- 단위 테스트 (Service, Repository)
- 통합 테스트 (Controller)

## 5. 코드 포맷팅
```bash
./gradlew spotlessApply
```

## 6. 최종 검증
- `/review` 명령어로 코드 리뷰
- `./gradlew build` 성공 확인

## 7. 문서화
- 필요시 해당 모듈의 CLAUDE.md 업데이트
