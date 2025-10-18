# module/persistence - 데이터 접근 레이어

이 모듈은 `module:core`의 저장소 인터페이스를 JPA로 구현하고 데이터베이스 설정을 담당합니다.

## ⚠️ 작업 전 필수 확인
**모든 작업 시 프로젝트 루트의 `.claude/rules.md` 파일을 반드시 참조하세요.**

## 모듈 목적

- **저장소 구현**: core 모듈의 저장소 인터페이스를 Spring Data JPA로 구현
- **데이터베이스 설정**: H2 in-memory database (MySQL 모드) 설정
- **캐싱**: Caffeine 캐시 설정 및 관리
- **쿼리 로깅**: P6Spy를 통한 SQL 로깅 및 포매팅

## 패키지 구조

### `persistence/` - 저장소 구현체

#### 어댑터 패턴
모든 저장소는 **어댑터 패턴**으로 구현:
1. `*JpaRepository` - Spring Data JPA 인터페이스 (자동 구현)
2. `*RepositoryAdapter` - core의 저장소 인터페이스 구현체

예시:
```java
// Spring Data JPA 인터페이스
interface UserJpaRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}

// Core 인터페이스 구현
@Repository
class UserRepositoryAdapter implements UserRepository {
    private final UserJpaRepository jpaRepository;

    @Override
    public User save(User user) {
        return jpaRepository.save(user);
    }
}
```

#### 주요 구현체

- **UserRepositoryAdapter**
  - `UserJpaRepository` 위임
  - 복잡한 업데이트 로직: `updateUserDetails()` - 중복 체크 포함
  - 메서드: save, findById, findByEmail, findByUsername, exists 계열

- **ArticleRepositoryAdapter**
  - `ArticleJpaRepository` 위임
  - Specification 기반 동적 쿼리 (`ArticleSpecifications` 사용)
  - 페이징 및 정렬: `findAll(ArticleFacets)` - Pageable 변환
  - 상세 정보 조회: `findArticleDetails(Article)` - 좋아요 수, 태그 등 집계

- **ArticleCommentRepositoryAdapter**
  - 댓글 CRUD
  - 게시글별 댓글 조회

- **TagRepositoryAdapter**
  - 태그 조회 및 생성
  - 캐싱 적용: `@Cacheable(CacheName.ALL_TAGS)`

- **ArticleFavoriteRepositoryAdapter**
  - 좋아요 추가/삭제
  - 중복 좋아요 체크

- **UserRelationshipRepositoryAdapter**
  - 팔로우/언팔로우
  - 팔로우 여부 확인

#### JPA Specifications

**ArticleSpecifications**: 동적 쿼리 생성을 위한 Specification 패턴
```java
Specification<Article> hasAuthorName(String authorName)
Specification<Article> hasTagName(String tagName)
Specification<Article> hasFavoritedUsername(String favoritedUsername)
```

사용 예시:
```java
var spec = Specification.where(hasAuthorName("john"))
                        .and(hasTagName("java"));
var articles = articleJpaRepository.findAll(spec, pageable);
```

### `config/` - 설정 클래스

#### JpaConfiguration
```java
@Configuration
@EnableJpaRepositories(basePackages = "io.zhc1.realworld.persistence")
@EntityScan(basePackages = "io.zhc1.realworld.model")
class JpaConfiguration {}
```
- JPA 저장소 스캔 위치 지정
- 엔티티 스캔 위치 지정 (core 모듈)

#### CacheConfiguration
```java
@Configuration
@EnableCaching
public class CacheConfiguration {
    @Bean
    CacheManager cacheManager() {
        // Caffeine 캐시: 1분 TTL, 최대 500개
    }
}
```

**캐시 이름** (`CacheName`):
- `ALL_TAGS`: 전체 태그 목록 캐싱

**캐시 사용**:
```java
@Cacheable(CacheName.ALL_TAGS)
public List<Tag> findAll() { ... }
```

#### DataSourceConfiguration
P6Spy SQL 로깅 설정:
- 쿼리 포매팅: DDL과 DML 구분하여 pretty print
- 실행 시간 측정
- 호출 스택 추적: `io.zhc1.realworld` 패키지만 표시

**로그 형식**:
```
----------------------------------------------------------------------------------------------------
                                        QUERY LOG
----------------------------------------------------------------------------------------------------
    select user0_.id as id1_4_0_,
           user0_.email as email2_4_0_
    from users user0_
    where user0_.email=?
----------------------------------------------------------------------------------------------------
- Connection ID                           : 1
- Execution Time                          : 5 ms
- Call Stacks                             :
     1. io.zhc1.realworld.service.UserService.login(UserService.java:74)
     2. io.zhc1.realworld.api.UserController.login(UserController.java:45)
----------------------------------------------------------------------------------------------------
```

## 데이터베이스 설정

### application.yaml
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb?MODE=MYSQL;
  jpa:
    open-in-view: false
    hibernate:
      ddl-auto: create-drop

decorator:
  datasource:
    p6spy:
      enable-logging: true
```

### 주요 설정
- **H2 Database**: 인메모리, MySQL 호환 모드
- **DDL**: 애플리케이션 시작 시 자동 생성, 종료 시 삭제
- **Open-in-View**: 비활성화 (성능 향상)
- **P6Spy**: 쿼리 로깅 활성화

### 스키마 참조
`src/main/resources/schema.sql` - 실제 DDL 참조용 (자동 실행 안됨)

## 테스트 작성 가이드

### 통합 테스트
```java
@DataJpaTest
class UserRepositoryAdapterTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void save_success() {
        // given
        var user = new User("email@test.com", "username", "password");

        // when
        var saved = userRepository.save(user);

        // then
        assertThat(saved.getId()).isNotNull();
    }
}
```

### 중요 사항
- `@DataJpaTest`: JPA 컴포넌트만 로드하는 슬라이스 테스트
- 실제 H2 DB 사용 (인메모리)
- 각 테스트 후 자동 롤백

## 저장소 구현 추가하기

### 1. JPA 인터페이스 생성
```java
interface NewEntityJpaRepository extends JpaRepository<NewEntity, Integer> {
    Optional<NewEntity> findByName(String name);
}
```

### 2. 어댑터 구현
```java
@Repository
@RequiredArgsConstructor
class NewEntityRepositoryAdapter implements NewEntityRepository {
    private final NewEntityJpaRepository jpaRepository;

    @Override
    public NewEntity save(NewEntity entity) {
        return jpaRepository.save(entity);
    }

    @Override
    public Optional<NewEntity> findByName(String name) {
        return jpaRepository.findByName(name);
    }
}
```

### 3. 복잡한 쿼리 필요 시
Specification 패턴 사용:
```java
final class NewEntitySpecifications {
    static Specification<NewEntity> hasStatus(String status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) return null;
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }
}

// 사용
var spec = Specification.where(hasStatus("active"));
var results = jpaRepository.findAll(spec);
```

## 캐싱 전략

### 캐시 추가
1. `CacheName`에 상수 추가:
```java
public interface CacheName {
    String ALL_TAGS = "all-tags";
    String NEW_CACHE = "new-cache";
}
```

2. 저장소 메서드에 `@Cacheable` 적용:
```java
@Cacheable(CacheName.NEW_CACHE)
public List<NewEntity> findAll() {
    return jpaRepository.findAll();
}
```

3. 캐시 무효화:
```java
@CacheEvict(value = CacheName.NEW_CACHE, allEntries = true)
public NewEntity save(NewEntity entity) {
    return jpaRepository.save(entity);
}
```

### 캐시 설정 커스터마이징
`CacheConfiguration`에서 캐시별 설정 가능:
```java
cacheManager.registerCustomCache(
    "custom-cache",
    Caffeine.newBuilder()
        .expireAfterWrite(5, TimeUnit.MINUTES)
        .maximumSize(1000)
        .build()
);
```

## 쿼리 최적화 팁

### 1. N+1 문제 해결
```java
// Fetch Join 사용
@Query("SELECT a FROM Article a JOIN FETCH a.author WHERE a.slug = :slug")
Optional<Article> findBySlugWithAuthor(@Param("slug") String slug);
```

### 2. DTO Projection
```java
interface ArticleSummary {
    String getTitle();
    String getAuthorName();
}

List<ArticleSummary> findAllProjectedBy();
```

### 3. Batch Size 설정
```yaml
spring:
  jpa:
    properties:
      hibernate:
        default_batch_fetch_size: 100
```

## 빌드 & 테스트

```bash
# 이 모듈만 테스트
./gradlew :module:persistence:test

# 이 모듈만 빌드
./gradlew :module:persistence:build

# SQL 로그 확인하며 테스트
./gradlew :module:persistence:test --info
```

## 의존성

- `module:core` (구현 대상)
- Spring Data JPA (저장소 구현)
- H2 Database (개발용 DB)
- Caffeine (캐시 구현체)
- P6Spy (SQL 로깅)

## 트러블슈팅

### 캐시가 동작하지 않을 때
1. `@EnableCaching` 확인
2. 캐시 이름 오타 확인
3. 메서드가 public인지 확인 (프록시 필요)

### N+1 쿼리 문제
1. P6Spy 로그로 쿼리 횟수 확인
2. Fetch Join 또는 `@EntityGraph` 사용
3. Batch Fetch Size 설정

### H2 MySQL 모드 문제
특정 MySQL 함수가 동작하지 않을 수 있음:
- H2 함수로 대체하거나
- 테스트용 MySQL Testcontainer 사용 검토
