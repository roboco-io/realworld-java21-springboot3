# RealWorld 프로젝트 작업 규칙

⚠️ **이 파일은 모든 코드 작성, 수정, 리뷰 시 반드시 준수해야 하는 필수 규칙입니다.**

이 파일은 RealWorld Java21 Spring Boot 3 프로젝트의 코딩 규칙과 작업 지침을 정의합니다.

## 🚨 중요 공지

**Claude Code는 다음 모든 작업 시 이 파일의 규칙을 반드시 준수해야 합니다:**

- ✅ 새 코드 작성
- ✅ 기존 코드 수정
- ✅ 리팩토링
- ✅ 버그 수정
- ✅ 테스트 작성
- ✅ 코드 리뷰
- ✅ 문서 작성

**규칙을 위반한 코드는 작성하지 마세요. 불확실한 경우 사용자에게 질문하세요.**

## 코딩 표준

### Java 코드 스타일

1. **Palantir Java Format 준수**
   - 모든 코드는 `./gradlew spotlessApply` 실행 후 커밋
   - import 순서: java → jakarta → org → com → net → io → lombok → io.zhc1

2. **Checkstyle 규칙 준수**
   - 빌드 시 자동으로 Checkstyle 검증 실행
   - 주요 규칙:
     - 파일 길이: 최대 500줄
     - 라인 길이: 최대 120자
     - 메서드 길이: 최대 150줄
     - 파라미터 개수: 최대 7개
     - 테스트 메서드명: BDD 스타일 허용 (when_, given_, should_)
   - 검증 명령어: `./gradlew checkstyleMain checkstyleTest`

3. **Lombok 사용 규칙**
   - `@RequiredArgsConstructor`: 생성자 주입 (권장)
   - `@Getter`: 엔티티 및 DTO
   - `@NoArgsConstructor(access = AccessLevel.PROTECTED)`: JPA 엔티티
   - `@Slf4j`: 로깅이 필요한 클래스
   - `@Data`, `@Builder` 사용 금지 (명시성 저하)

4. **명명 규칙**
   - 클래스: PascalCase (예: `UserService`)
   - 메서드/변수: camelCase (예: `findByEmail`)
   - 상수: UPPER_SNAKE_CASE (예: `MAX_SIZE`)
   - 패키지: lowercase (예: `io.zhc1.realworld.model`)

### 아키텍처 규칙

1. **모듈 의존성**
   ```
   server/api (compileOnly)──→ module/core
                                     ↑
   module/persistence ──────────────┘
   ```
   - core는 다른 모듈에 의존하지 않음
   - persistence는 core만 의존
   - api는 core를 compileOnly, persistence를 runtimeOnly로 의존

2. **레이어 분리**
   - Controller: HTTP 요청/응답만 처리
   - Service: 비즈니스 로직
   - Repository: 데이터 접근
   - Entity: 도메인 모델 및 검증

3. **예외 처리**
   - 검증 실패: `IllegalArgumentException`
   - 리소스 없음: `NoSuchElementException`
   - 권한 없음: `AccessDeniedException`
   - 절대 예외를 숨기지 말 것

## 테스트 규칙

### 단위 테스트

1. **테스트 클래스 네이밍**
   - 형식: `{TargetClass}Test`
   - 예: `UserServiceTest`, `ArticleRepositoryAdapterTest`

2. **테스트 메서드 네이밍**
   - 형식: `{method}_{scenario}_{expectedResult}`
   - 예: `login_validCredentials_success`
   - 예: `signup_duplicateEmail_throwsException`

3. **예외 검증 규칙 (중요!)**
   ```java
   // ✅ 좋은 예: 예외 타입만 검증
   assertThrows(IllegalArgumentException.class, () -> {
       userService.signup(invalidRegistry);
   });

   // ❌ 나쁜 예: 예외 메시지 검증 (취약함)
   var exception = assertThrows(IllegalArgumentException.class, () -> {
       userService.signup(invalidRegistry);
   });
   assertEquals("email is required", exception.getMessage()); // 금지!
   ```

4. **Given-When-Then 패턴**
   ```java
   @Test
   void testMethod() {
       // given (준비)
       var user = new User("email", "username", "password");
       when(repository.save(any())).thenReturn(user);

       // when (실행)
       var result = service.create(user);

       // then (검증)
       assertNotNull(result);
       verify(repository).save(any(User.class));
   }
   ```

5. **빌드 검증 필수**
   - 테스트 작성 후 반드시 `./gradlew test` 실행
   - 빌드 실패 시 즉시 수정

### 통합 테스트

1. **@DataJpaTest**: JPA 레이어 테스트
2. **@WebMvcTest**: Controller 레이어 테스트
3. 실제 DB 사용 (H2 인메모리)

## 데이터베이스 규칙

### ID 전략

1. **User 엔티티**: UUID 사용
   - 이유: 보안, 분산 환경 대비
   ```java
   @Id
   @GeneratedValue(strategy = GenerationType.UUID)
   private UUID id;
   ```

2. **기타 엔티티**: Integer AUTO_INCREMENT
   - 이유: 성능, 단순성
   ```java
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer id;
   ```

### JPA 엔티티 작성 규칙

1. **필수 어노테이션**
   ```java
   @Entity
   @Getter
   @NoArgsConstructor(access = AccessLevel.PROTECTED)
   public class MyEntity {
       // ...
   }
   ```

2. **필수 필드 검증**
   - 생성자에서 null/blank 체크
   ```java
   public User(String email, String username, String password) {
       if (email == null || email.isBlank()) {
           throw new IllegalArgumentException("email must not be null or blank.");
       }
       this.email = email;
       // ...
   }
   ```

3. **Setter 규칙**
   - blank 값은 로그 후 무시 (업데이트 안함)
   ```java
   public void setEmail(String email) {
       if (email == null || email.isBlank()) {
           log.info("not set because the email is blank");
           return;
       }
       this.email = email;
   }
   ```

4. **관계 매핑**
   - 기본: LAZY 로딩
   - 필요시에만 EAGER 또는 Fetch Join
   - `open-in-view: false` 설정 유지

## API 설계 규칙

### REST API 규칙

1. **RealWorld 사양 준수**
   - 요청/응답 래핑: `{ "user": {...} }`, `{ "article": {...} }`
   - 엔드포인트 명명: `/api/resources` 형식

2. **HTTP 메서드**
   - GET: 조회 (멱등성)
   - POST: 생성
   - PUT: 전체 업데이트
   - PATCH: 부분 업데이트 (미사용)
   - DELETE: 삭제 (멱등성)

3. **HTTP 상태 코드**
   - 200 OK: 성공
   - 201 Created: 생성 성공
   - 400 Bad Request: 잘못된 요청
   - 401 Unauthorized: 인증 실패
   - 403 Forbidden: 권한 없음
   - 404 Not Found: 리소스 없음
   - 500 Internal Server Error: 서버 에러

### DTO 규칙

1. **Request DTO**: record 사용
   ```java
   public record CreateUserRequest(UserData user) {
       public record UserData(String email, String username, String password) {}
   }
   ```

2. **Response DTO**: record + 정적 팩토리 메서드
   ```java
   public record UserResponse(String email, String username) {
       public static UserResponse from(User user) {
           return new UserResponse(user.getEmail(), user.getUsername());
       }
   }
   ```

## 보안 규칙

### JWT 인증

1. **토큰 만료**: 5분 (개발), 운영 환경에서 조정 필요
2. **RSA 키**: app.key (private), app.pub (public)
3. **헤더 형식**: `Authorization: Token {jwt}`

### 비밀번호

1. **암호화**: Spring Security BCryptPasswordEncoder
2. **저장**: 암호화된 값만 DB 저장
3. **검증**: `PasswordEncoder.matches()` 사용

### CORS

1. **개발**: 모든 origin 허용
2. **운영**: 특정 도메인만 허용 (반드시 변경)

## Git 워크플로우 규칙

### 커밋 메시지

1. **형식**: `타입: 간결한 설명`
   - feat: 새 기능
   - fix: 버그 수정
   - refactor: 리팩토링
   - test: 테스트 추가/수정
   - docs: 문서 수정
   - style: 코드 포맷팅
   - chore: 기타 작업

2. **커밋 메시지 본문 형식** (선택 사항)
   ```
   타입: 간결한 설명

   - 변경 사항 상세 설명 (선택)
   - 추가 컨텍스트나 이유 설명 (선택)

   🤖 Generated with [Claude Code](https://claude.com/claude-code)

   Co-Authored-By: Claude <noreply@anthropic.com>
   ```

3. **예시**
   - `feat: Add user follow/unfollow feature`
   - `fix: Fix article slug generation bug`
   - `refactor: Extract common validation logic`
   - `test: Add UserService unit tests`
   - `docs: Add comprehensive Claude Code collaboration guidelines`

### 커밋 전 체크리스트

- [ ] `./gradlew spotlessApply` 실행
- [ ] `./gradlew build` 성공 확인 (테스트, 린트, 커버리지 포함)
- [ ] 불필요한 주석 제거
- [ ] console.log, System.out.println 제거
- [ ] TODO 주석 확인 및 처리

### CI/CD 규칙

1. **GitHub Actions 워크플로우**
   - main 브랜치에 push 시 자동 빌드 실행
   - Pull Request 생성 시 자동 빌드 및 커버리지 리포트
   - 빌드 단계:
     1. Spotless 코드 포맷 검증
     2. Checkstyle 린트 검증
     3. 전체 빌드 (테스트 포함)
     4. JaCoCo 커버리지 리포트 생성
   - 빌드 실패 시 merge 불가

2. **빌드 성공 기준**
   - 모든 테스트 통과
   - Checkstyle 위반 0건 (maxWarnings = 0)
   - Spotless 포맷 준수
   - 빌드 에러 없음

3. **커버리지 요구사항**
   - 전체 커버리지: 최소 70%
   - 변경된 파일 커버리지: 최소 80%
   - PR에 자동으로 커버리지 리포트 댓글 추가

### Git 태그 규칙

1. **태그 형식**: Semantic Versioning 사용
   - `v{major}.{minor}.{patch}` (예: v1.0.0)
   - 설명적 접미사 가능 (예: v1.0.0-claude-rules)

2. **태그 생성**
   ```bash
   # Annotated 태그 생성 (권장)
   git tag -a v1.0.0 -m "Release: 릴리즈 설명"

   # 태그 푸시
   git push --tags
   ```

3. **태그 사용 시점**
   - 주요 기능 완성 시
   - 프로젝트 마일스톤 달성 시
   - 중요한 규칙이나 구조 변경 시
   - 배포 준비 완료 시

### Claude Code와 협업 시 Git 워크플로우

1. **일반 개발 워크플로우**
   ```bash
   # 1. 작업 수행 (코드 작성, 수정)
   # 2. 코드 포매팅
   ./gradlew spotlessApply

   # 3. 테스트 실행
   ./gradlew test

   # 4. 변경 사항 확인
   git status
   git diff

   # 5. 스테이징
   git add [파일명 또는 .]

   # 6. 커밋
   git commit -m "타입: 간결한 설명

   🤖 Generated with [Claude Code](https://claude.com/claude-code)

   Co-Authored-By: Claude <noreply@anthropic.com>"

   # 7. 푸시
   git push
   ```

2. **마일스톤 달성 시 워크플로우**
   ```bash
   # 1-7. 위 일반 워크플로우 수행

   # 8. 태그 생성
   git tag -a v1.0.0 -m "Release: 릴리즈 설명

   주요 변경 사항:
   - 기능 A 추가
   - 기능 B 개선
   - 버그 C 수정"

   # 9. 태그 푸시
   git push --tags
   ```

3. **Claude Code 자동 처리 사항**
   - 커밋 메시지에 Claude Code 서명 자동 추가
   - HEREDOC 형식으로 멀티라인 커밋 메시지 작성
   - 스테이징 → 커밋 → 푸시를 순차적으로 실행
   - 태그 생성 시 annotated 태그 사용

### 주의 사항

1. **절대 하지 말 것**
   - `git push --force` (특히 main/master 브랜치)
   - 테스트 실패 상태에서 커밋
   - 빌드 실패 상태에서 커밋
   - spotlessApply 미실행 상태에서 커밋

2. **권장 사항**
   - 논리적으로 관련된 변경사항끼리 커밋
   - 커밋 메시지는 명확하고 간결하게
   - 대규모 변경은 작은 단위로 분할 커밋
   - 태그는 의미 있는 시점에만 생성

## 성능 최적화 규칙

### 쿼리 최적화

1. **N+1 문제 방지**
   - Fetch Join 사용
   - @EntityGraph 활용
   - Batch Fetch Size 설정

2. **인덱싱**
   - 자주 조회되는 컬럼: @Column(unique = true) 또는 @Index
   - 복합 인덱스: @Table(indexes = {...})

### 캐싱

1. **Caffeine 캐시 사용**
   - 변경 빈도 낮은 데이터 (예: 전체 태그 목록)
   - TTL: 1분 (기본값, 조정 가능)

2. **캐시 무효화**
   - @CacheEvict: 데이터 변경 시

## 문서화 규칙

### Javadoc

1. **Public API만 작성**
   - Public 메서드, Public 클래스
   - 내부 구현은 주석으로

2. **형식**
   ```java
   /**
    * 사용자를 이메일로 조회합니다.
    *
    * @param email 조회할 이메일
    * @return 사용자 정보
    * @throws NoSuchElementException 사용자를 찾을 수 없는 경우
    */
   public User findByEmail(String email) { ... }
   ```

### 주석

1. **왜(Why)를 설명**
   - 무엇(What)은 코드로 표현
   - 왜 이렇게 구현했는지 설명

2. **복잡한 로직**
   - 알고리즘 설명
   - 비즈니스 규칙 설명

## 에러 처리 규칙

1. **절대 에러 무시 금지**
   ```java
   // ❌ 절대 금지
   try {
       // ...
   } catch (Exception e) {
       // 아무것도 안함
   }

   // ✅ 최소한 로깅
   try {
       // ...
   } catch (Exception e) {
       log.error("Error occurred", e);
       throw new RuntimeException("처리 실패", e);
   }
   ```

2. **적절한 로그 레벨**
   - ERROR: 시스템 오류, 복구 불가능
   - WARN: 경고, 시스템은 동작
   - INFO: 중요한 비즈니스 로직
   - DEBUG: 디버깅 정보
   - TRACE: 상세 추적

## 작업 완료 전 필수 체크리스트

### 반드시 수행해야 할 검증

1. **빌드 성공 확인 (필수)**
   ```bash
   # 작업을 마치기 전 반드시 실행
   ./gradlew build
   ```
   - 빌드가 실패하면 작업 완료로 간주하지 않음
   - 빌드 실패 시 즉시 원인을 파악하고 수정
   - 모든 테스트가 통과해야 함

2. **코드 포맷팅 확인**
   ```bash
   # 커밋 전 필수
   ./gradlew spotlessApply
   ```

3. **테스트 실행 확인**
   ```bash
   # 테스트만 별도 실행 가능
   ./gradlew test
   ```

**⚠️ 경고: 위 체크리스트를 통과하지 못한 코드는 절대 작업 완료로 간주하지 않습니다.**

## 참조 파일

- 전체 프로젝트 가이드: `/CLAUDE.md`
- 도메인 레이어: `/module/core/CLAUDE.md`
- 데이터 접근 레이어: `/module/persistence/CLAUDE.md`
- API 레이어: `/server/api/CLAUDE.md`
