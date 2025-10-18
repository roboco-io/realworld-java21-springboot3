# server/api - REST API 레이어

이 모듈은 RealWorld 애플리케이션의 HTTP API 엔드포인트와 보안 설정을 담당합니다.

## ⚠️ 작업 전 필수 확인
**모든 작업 시 프로젝트 루트의 `.claude/rules.md` 파일을 반드시 참조하세요.**

## 모듈 목적

- **REST API 엔드포인트**: RealWorld 사양에 따른 HTTP API 제공
- **인증/인가**: JWT 기반 Spring Security 설정
- **DTO 변환**: 도메인 모델 ↔ API 응답/요청 변환
- **예외 처리**: HTTP 상태 코드로 변환

## 패키지 구조

### `api/` - 컨트롤러 및 DTO

#### 컨트롤러

모든 컨트롤러는 `@RestController`와 `@RequiredArgsConstructor` 사용:

- **UserController** (`/api/users`, `/api/user`)
  - `POST /api/users` - 회원가입 (인증 불필요)
  - `POST /api/users/login` - 로그인 (인증 불필요)
  - `GET /api/user` - 현재 사용자 조회 (인증 필요)
  - `PUT /api/user` - 사용자 정보 업데이트 (인증 필요)
  - 특징: 회원가입 후 자동 로그인 (307 Redirect)

- **ArticleController** (`/api/articles`)
  - `POST /api/articles` - 게시글 작성
  - `GET /api/articles` - 게시글 목록 (필터링: tag, author, favorited)
  - `GET /api/articles/feed` - 팔로우한 사용자의 게시글
  - `GET /api/articles/{slug}` - 게시글 상세 (인증 선택)
  - `PUT /api/articles/{slug}` - 게시글 수정
  - `DELETE /api/articles/{slug}` - 게시글 삭제

- **ArticleFavoriteController** (`/api/articles/{slug}/favorite`)
  - `POST /api/articles/{slug}/favorite` - 좋아요
  - `DELETE /api/articles/{slug}/favorite` - 좋아요 취소

- **ArticleCommentController** (`/api/articles/{slug}/comments`)
  - `POST /api/articles/{slug}/comments` - 댓글 작성
  - `GET /api/articles/{slug}/comments` - 댓글 목록 (인증 선택)
  - `DELETE /api/articles/{slug}/comments/{id}` - 댓글 삭제

- **UserRelationshipController** (`/api/profiles/{username}`)
  - `GET /api/profiles/{username}` - 프로필 조회 (인증 선택)
  - `POST /api/profiles/{username}/follow` - 팔로우
  - `DELETE /api/profiles/{username}/follow` - 언팔로우

- **TagController** (`/api/tags`)
  - `GET /api/tags` - 전체 태그 목록 (인증 불필요)

#### Request DTOs (`api/request/`)

모든 요청 DTO는 record로 정의:
- `SignupRequest` - 회원가입 요청
- `LoginUserRequest` - 로그인 요청
- `UpdateUserRequest` - 사용자 정보 업데이트
- `WriteArticleRequest` - 게시글 작성
- `EditArticleRequest` - 게시글 수정
- `WriteCommentRequest` - 댓글 작성

예시:
```java
public record SignupRequest(SignupUserRequest user) {
    public record SignupUserRequest(String email, String username, String password) {}
}
```

#### Response DTOs (`api/response/`)

- `UsersResponse` - 사용자 정보 + JWT 토큰
- `ProfileResponse` / `ProfilesResponse` - 사용자 프로필
- `ArticleResponse` - 게시글 정보
- `SingleArticleResponse` / `MultipleArticlesResponse` - 게시글 래퍼
- `ArticleCommentResponse` - 댓글 정보
- `SingleCommentResponse` / `MultipleCommentsResponse` - 댓글 래퍼
- `TagsResponse` - 태그 목록

특징:
- RealWorld 사양의 래핑 구조 준수: `{ "user": {...} }`, `{ "article": {...} }`
- 정적 팩토리 메서드: `from(domain, ...)` 패턴

### `config/` - 보안 및 설정

#### 보안 관련

**SecurityConfiguration**
```java
@Configuration
@EnableMethodSecurity
class SecurityConfiguration {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) {
        // OAuth2 Resource Server로 JWT 검증
        // CORS 설정 (개발용 - 모두 허용)
        // CSRF 비활성화 (REST API)
        // 세션 사용 안함 (STATELESS)
    }
}
```

주요 설정:
- **인증 불필요**: `POST /api/users`, `POST /api/users/login`, `GET /api/articles`, `GET /api/tags` 등
- **인증 필요**: 그 외 모든 엔드포인트
- **CORS**: 모든 origin 허용 (**주의**: 운영 환경에서는 변경 필요)
- **세션**: STATELESS (JWT만 사용)

**AuthTokenProvider**
```java
@Component
public class AuthTokenProvider {
    public String createAuthToken(User user) {
        // JWT 생성
        // Issuer: https://realworld.io
        // Subject: user.getId() (UUID)
        // Expiry: 5분 (300초)
    }
}
```

**AuthToken**
- `JwtAuthenticationToken` 래퍼 클래스
- Spring Security의 Authentication 객체
- 편의 메서드:
  - `tokenValue()`: JWT 문자열 반환
  - `userId()`: Subject에서 UUID 추출

**AuthTokenResolver**
- HTTP 요청에서 `Authorization` 헤더 파싱
- 표준: `Authorization: Token {jwt}` 또는 `Bearer {jwt}`

**AuthTokenConverter**
- JWT를 `AuthToken`으로 변환
- Spring Security의 `JwtAuthenticationConverter` 커스터마이징

**SecurityPasswordEncoderAdapter**
- Spring Security의 `PasswordEncoder`를 core의 `PasswordEncoder` 인터페이스로 어댑팅

#### 기타 설정

**ObjectMapperConfiguration**
```java
@Configuration
class ObjectMapperConfiguration {
    @Bean
    Jackson2ObjectMapperBuilder objectMapperBuilder() {
        // ISO-8601 날짜 포맷
        // null 필드 제외
        // 기타 직렬화 설정
    }
}
```

**ApplicationExceptionHandler**
```java
@RestControllerAdvice
class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {
    // IllegalArgumentException → 400 Bad Request
    // NoSuchElementException → 404 Not Found
    // AccessDeniedException → 403 Forbidden
    // AuthenticationException → 401 Unauthorized
    // ProblemDetail (RFC 7807) 형식 반환
}
```

### `mixin/` - Jackson Mixin

**AuthenticationAwareMixin**
- 인증 정보를 기반으로 응답 커스터마이징
- 예: 현재 사용자의 팔로우 여부, 좋아요 여부 등

## 인증 흐름

### 1. 회원가입 & 로그인
```
Client → POST /api/users (SignupRequest)
       → UserController.signup()
       → UserService.signup() (비밀번호 암호화)
       → 307 Redirect → POST /api/users/login
       → UserController.login()
       → AuthTokenProvider.createAuthToken()
       ← JWT 토큰 반환
```

### 2. 인증 API 호출
```
Client → GET /api/user
         Header: Authorization: Token {jwt}
       → SecurityFilterChain
       → AuthTokenResolver (헤더 파싱)
       → JwtDecoder (JWT 검증)
       → AuthTokenConverter (AuthToken 생성)
       → UserController.getUser(AuthToken)
       ← 사용자 정보 반환
```

### 3. JWT 만료 시
```
Client → API 요청
       → JwtDecoder (검증 실패)
       → BearerTokenAuthenticationEntryPoint
       ← 401 Unauthorized
```

## 보안 키 관리

### RSA 키 쌍
- **위치**: `src/main/resources/app.key` (private), `app.pub` (public)
- **용도**: JWT 서명 및 검증
- **설정**: `application.yaml`
  ```yaml
  security:
    key:
      private: classpath:app.key
      public: classpath:app.pub
  ```

### 키 생성 (필요시)
```bash
# 개인키 생성
openssl genrsa -out app.key 2048

# 공개키 추출
openssl rsa -in app.key -pubout -out app.pub
```

## 컨트롤러 작성 패턴

### 기본 패턴
```java
@RestController
@RequiredArgsConstructor
class NewController {
    private final NewService service;

    @GetMapping("/api/resources")
    public ResponseDTO getResources(AuthToken authToken) {
        // authToken.userId()로 현재 사용자 ID 획득
        var result = service.doSomething(authToken.userId());
        return ResponseDTO.from(result);
    }

    @PostMapping("/api/resources")
    public ResponseDTO createResource(
            AuthToken authToken,
            @RequestBody RequestDTO request) {
        var result = service.create(authToken.userId(), request);
        return ResponseDTO.from(result);
    }
}
```

### 인증 선택적 엔드포인트
```java
@GetMapping("/api/public-resource")
public ResponseDTO getPublicResource(
        @AuthenticationPrincipal(errorOnInvalidType = false) AuthToken authToken) {
    // authToken이 null일 수 있음 (비로그인 사용자)
    UUID userId = authToken != null ? authToken.userId() : null;
    var result = service.get(userId);
    return ResponseDTO.from(result);
}
```

**주의**: SecurityConfiguration에서 해당 엔드포인트를 `permitAll()`로 설정 필요

## DTO 작성 가이드

### Request DTO
```java
// RealWorld 사양: 중첩 구조
public record CreateResourceRequest(CreateResourceData resource) {
    public record CreateResourceData(
        String name,
        String description
    ) {}
}
```

### Response DTO
```java
public record ResourceResponse(
    String name,
    String description,
    LocalDateTime createdAt
) {
    public static ResourceResponse from(Resource domain) {
        return new ResourceResponse(
            domain.getName(),
            domain.getDescription(),
            domain.getCreatedAt()
        );
    }
}

// RealWorld 사양: 래핑
public record SingleResourceResponse(ResourceResponse resource) {
    public static SingleResourceResponse from(Resource domain) {
        return new SingleResourceResponse(ResourceResponse.from(domain));
    }
}
```

## 예외 처리

### 서비스에서 발생하는 예외
```java
// 400 Bad Request
throw new IllegalArgumentException("잘못된 요청입니다.");

// 404 Not Found
throw new NoSuchElementException("리소스를 찾을 수 없습니다.");

// 403 Forbidden (Spring Security)
throw new AccessDeniedException("권한이 없습니다.");
```

### 커스텀 예외 처리 추가
```java
@RestControllerAdvice
class ApplicationExceptionHandler {
    @ExceptionHandler(CustomException.class)
    ProblemDetail handle(CustomException e) {
        log.error(e.getMessage(), e);
        return ProblemDetail.forStatusAndDetail(
            HttpStatus.UNPROCESSABLE_ENTITY,
            e.getMessage()
        );
    }
}
```

## 테스트 작성

### 컨트롤러 테스트
```java
@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockBean private UserService userService;
    @MockBean private AuthTokenProvider tokenProvider;

    @Test
    void login_success() throws Exception {
        // given
        var user = new User("email", "username", "password");
        when(userService.login(any(), any())).thenReturn(user);
        when(tokenProvider.createAuthToken(any())).thenReturn("token");

        // when & then
        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "user": {
                            "email": "email",
                            "password": "password"
                        }
                    }
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.user.token").value("token"));
    }
}
```

## 빌드 & 실행

```bash
# 이 모듈 실행 (전체 애플리케이션)
./gradlew :realworld:bootRun

# 이 모듈만 빌드
./gradlew :realworld:build

# 이 모듈만 테스트
./gradlew :realworld:test

# 실행 가능한 JAR 생성
./gradlew :realworld:bootJar
# 생성 위치: server/api/build/libs/realworld-*.jar
```

## 의존성

- `module:core` (compileOnly) - 도메인 인터페이스
- `module:persistence` (runtimeOnly) - 런타임 주입
- Spring Web (REST API)
- Spring Security + OAuth2 Resource Server (JWT)
- Jakarta Persistence API (엔티티 참조)

## API 문서

- **OpenAPI 스펙**: `/api-docs/openapi.yaml`
- **온라인 문서**: https://1chz.github.io/realworld-java21-springboot3
- **RealWorld 사양**: https://realworld-docs.netlify.app/docs/specs/backend-specs/endpoints

## 보안 체크리스트

### 운영 배포 전 필수 변경사항

1. **CORS 설정 제한**
   ```java
   configuration.setAllowedOriginPatterns(List.of("https://yourdomain.com"));
   ```

2. **JWT 만료 시간 연장**
   ```java
   .expiresAt(now.plusSeconds(3600)) // 1시간
   ```

3. **RSA 키 교체**
   - 개발용 키 대신 운영용 키 생성
   - 키 파일을 환경 변수나 비밀 관리 시스템에서 로드

4. **에러 메시지 상세도 조정**
   - 운영 환경에서는 스택 트레이스 노출 금지
   - 일반적인 에러 메시지만 반환

## 트러블슈팅

### JWT 검증 실패
- 키 파일 경로 확인: `security.key.public`, `security.key.private`
- 키 형식 확인: PEM 형식의 RSA 키 필요
- 시스템 시간 확인: JWT의 `exp` 클레임과 서버 시간 동기화

### CORS 에러
- `SecurityConfiguration.corsConfigurationSource()` 설정 확인
- 프론트엔드 도메인이 허용 목록에 있는지 확인

### 401 Unauthorized
- Authorization 헤더 형식: `Token {jwt}` 또는 `Bearer {jwt}`
- JWT 만료 확인 (5분)
- SecurityConfiguration의 `permitAll()` 경로 확인
