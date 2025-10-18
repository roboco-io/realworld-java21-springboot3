# module/core - 도메인 레이어

이 모듈은 RealWorld 애플리케이션의 핵심 비즈니스 로직과 도메인 모델을 포함합니다.

## ⚠️ 작업 전 필수 확인
**모든 작업 시 프로젝트 루트의 `.claude/rules.md` 파일을 반드시 참조하세요.**

## 모듈 목적

- **순수한 도메인 로직**: 외부 프레임워크나 인프라에 의존하지 않는 핵심 비즈니스 규칙
- **도메인 모델 정의**: JPA 엔티티 및 값 객체
- **저장소 인터페이스**: 데이터 접근 계약 정의 (구현은 persistence 모듈)
- **서비스 레이어**: 비즈니스 로직 조율

## 패키지 구조

### `model/` - 도메인 엔티티 및 저장소 인터페이스

#### 주요 엔티티
- **User**: 사용자 (UUID 기반 ID)
  - 필수 필드: email, username, password
  - 선택 필드: bio, imageUrl
  - 유효성 검증: 생성자 및 setter에서 null/blank 체크
  - 비밀번호 암호화: `encryptPassword(PasswordEncoder, String)`

- **Article**: 게시글 (Integer 기반 ID)
  - 필수 필드: author, slug, title, description, content
  - 관계: `@ManyToOne` author, `@OneToMany` articleTags/comments/favorites
  - Slug 생성: `generateSlug(String title)` - 제목 기반 URL 친화적 문자열
  - 업데이트: `update(String title, String description, String content)`

- **ArticleComment**: 게시글 댓글 (Integer 기반 ID)
  - 필수 필드: article, author, content
  - 관계: `@ManyToOne` article, author

- **Tag**: 태그 (Integer 기반 ID)
  - 필수 필드: name (unique)

- **ArticleTag**: 게시글-태그 중간 엔티티
  - 복합키 사용, 게시글과 태그 연결

- **ArticleFavorite**: 게시글 좋아요 (Integer 기반 ID)
  - 관계: `@ManyToOne` article, user
  - 중복 방지: `@Table(uniqueConstraints)`

- **UserFollow**: 사용자 팔로우 (Integer 기반 ID)
  - 관계: `@ManyToOne` follower, following
  - 중복 방지: `@Table(uniqueConstraints)`

#### 값 객체
- **UserRegistry**: 사용자 가입 정보 (record)
- **ArticleFacets**: 게시글 조회 필터링 조건 (record)
- **ArticleDetails**: 게시글 상세 정보 (record)

#### 저장소 인터페이스
모든 저장소 인터페이스는 `persistence` 모듈에서 구현됩니다:
- `UserRepository`: 사용자 CRUD 및 조회
- `ArticleRepository`: 게시글 CRUD, 필터링, 상세 정보 조회
- `ArticleCommentRepository`: 댓글 CRUD
- `TagRepository`: 태그 CRUD
- `ArticleFavoriteRepository`: 좋아요 CRUD
- `UserRelationshipRepository`: 팔로우 관계 관리

#### 인터페이스
- **PasswordEncoder**: 비밀번호 암호화 계약
  ```java
  String encode(String rawPassword);
  boolean matches(String rawPassword, String encodedPassword);
  ```

### `service/` - 비즈니스 로직

모든 서비스는 `@Service`와 `@RequiredArgsConstructor`를 사용하여 생성자 주입:

- **UserService**: 사용자 관리
  - `signup(UserRegistry)`: 사용자 가입 (중복 체크, 비밀번호 암호화)
  - `login(String email, String password)`: 로그인 검증
  - `getUser(UUID/String)`: 사용자 조회
  - `updateUserDetails(...)`: 사용자 정보 업데이트

- **ArticleService**: 게시글 관리
  - `createArticle(User author, ...)`: 게시글 생성 (slug 자동 생성)
  - `getArticle(String slug)`: 게시글 조회
  - `getArticles(ArticleFacets)`: 게시글 목록 (필터링, 페이징)
  - `updateArticle(...)`: 게시글 수정
  - `deleteArticle(...)`: 게시글 삭제
  - `favoriteArticle(...)`: 좋아요 토글

- **ArticleCommentService**: 댓글 관리
  - `createComment(...)`: 댓글 생성
  - `getComments(String slug)`: 게시글의 댓글 목록
  - `deleteComment(...)`: 댓글 삭제

- **UserRelationshipService**: 팔로우 관리
  - `follow(...)`: 사용자 팔로우
  - `unfollow(...)`: 팔로우 취소
  - `isFollowing(...)`: 팔로우 여부 확인

- **TagService**: 태그 관리
  - `getTags()`: 모든 태그 조회

## 주요 설계 원칙

### 1. 도메인 주도 설계 (DDD)
- 엔티티는 자신의 불변성을 스스로 보장
- 비즈니스 규칙은 엔티티 내부에 캡슐화
- 서비스는 엔티티 간 조율 역할

### 2. 의존성 역전 원칙 (DIP)
- 저장소는 인터페이스로만 정의
- 구현체는 `persistence` 모듈이 제공
- 서비스는 인터페이스에만 의존

### 3. 유효성 검증
- 생성자에서 필수 값 검증
- Setter에서 blank 값 처리 (로그 후 무시)
- 비즈니스 규칙 위반 시 `IllegalArgumentException`
- 존재하지 않는 리소스 조회 시 `NoSuchElementException`

### 4. ID 전략
- **User**: UUID (보안성, 분산 환경)
- **나머지 엔티티**: Integer auto-increment (성능)

## 테스트 작성 가이드

### 단위 테스트 위치
`src/test/java/io/zhc1/realworld/service/`

### 테스트 패턴
```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @InjectMocks private UserService userService;

    @Test
    void signup_success() {
        // given
        var registry = new UserRegistry("email", "user", "password");
        when(userRepository.existsBy(any(), any())).thenReturn(false);

        // when
        userService.signup(registry);

        // then
        verify(userRepository).save(any(User.class));
    }

    @Test
    void signup_duplicateUser_throwsException() {
        // given
        var registry = new UserRegistry("email", "user", "password");
        when(userRepository.existsBy(any(), any())).thenReturn(true);

        // when & then
        assertThrows(IllegalArgumentException.class,
            () -> userService.signup(registry));
    }
}
```

### 중요 규칙
- **예외 타입만 검증**: 메시지는 검증하지 않음
- **Mockito 사용**: 저장소는 항상 모킹
- **행위 검증**: `verify()`로 저장소 메서드 호출 확인

## 새 도메인 기능 추가하기

### 1. 새 엔티티 추가
```java
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String requiredField;

    public NewEntity(String requiredField) {
        if (requiredField == null || requiredField.isBlank()) {
            throw new IllegalArgumentException("필드는 필수입니다.");
        }
        this.requiredField = requiredField;
    }
}
```

### 2. 저장소 인터페이스 추가
```java
public interface NewEntityRepository {
    NewEntity save(NewEntity entity);
    Optional<NewEntity> findById(Integer id);
    // 필요한 조회 메서드들...
}
```

### 3. 서비스 추가
```java
@Service
@RequiredArgsConstructor
public class NewEntityService {
    private final NewEntityRepository repository;

    public NewEntity create(String field) {
        var entity = new NewEntity(field);
        return repository.save(entity);
    }
}
```

### 4. 테스트 작성
```java
@ExtendWith(MockitoExtension.class)
class NewEntityServiceTest {
    @Mock private NewEntityRepository repository;
    @InjectMocks private NewEntityService service;

    @Test
    void create_success() {
        // 테스트 구현...
    }
}
```

## 빌드 & 테스트

```bash
# 이 모듈만 테스트
./gradlew :module:core:test

# 이 모듈만 빌드
./gradlew :module:core:build

# 특정 테스트 실행
./gradlew :module:core:test --tests "*.UserServiceTest"
```

## 의존성

- Jakarta Persistence API (JPA 엔티티 정의)
- Lombok (보일러플레이트 코드 제거)
- Spring Boot Starter (서비스 어노테이션)
- Spring Boot Starter Test (테스트)
