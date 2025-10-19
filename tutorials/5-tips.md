---
marp: true
theme: default
paginate: true
header: 'Claude Code 고급 활용법'
footer: 'Claude Code 바이브 코딩 워크숍'
---

# Claude Code 고급 활용법
## MCP, Plan 모드, YOLO 모드 마스터하기

생산성을 극대화하는 고급 기법들

---

## 이번 세션의 목표

### 학습 목표
- ✅ MCP (Model Context Protocol) 이해 및 활용
- ✅ Context7 MCP로 공식 문서 참조하기
- ✅ Atlassian MCP로 Jira/Confluence 연동하기
- ✅ Plan 모드로 구현 전 계획 수립하기
- ✅ YOLO 모드로 빠른 실행하기

### 실습 목표
- MCP 서버 설치 및 설정
- Plan 모드로 기능 설계
- YOLO 모드로 신속한 개발

**예상 소요 시간**: 60분

---

## Part 1: MCP (Model Context Protocol)

### MCP란?

**Model Context Protocol**: Claude Code가 외부 시스템과 통신하는 표준 프로토콜

```
Claude Code ←MCP→ Context7 (공식 문서)
            ←MCP→ Atlassian (Jira/Confluence)
            ←MCP→ GitHub (Issues/PRs)
            ←MCP→ Database (Query/Schema)
```

**핵심 개념**:
- 표준화된 통신 규약
- 플러그인 형태의 확장성
- 실시간 외부 데이터 접근

---

## MCP의 필요성

### 기존 방식의 한계

```
개발자: "Spring Boot 3의 @ConfigurationProperties 사용법 알려줘"

Claude: (학습 데이터 기반 답변)
→ 최신 버전 정보 부족
→ 공식 문서와 다를 수 있음
→ 프레임워크 업데이트 반영 불가
```

### MCP 사용 시

```
개발자: "Spring Boot 3의 @ConfigurationProperties 사용법 알려줘"

Claude → Context7 MCP → Spring Boot 공식 문서
→ 최신 공식 문서 직접 참조
→ 정확한 코드 예시
→ 버전별 차이점 명확
```

---

## MCP 서버 종류

### 1. Context7 (문서 검색)
- **용도**: 공식 라이브러리 문서 검색
- **지원**: Spring, React, Vue, Angular, Python 등

### 2. Atlassian (협업)
- **용도**: Jira 이슈, Confluence 문서
- **기능**: 이슈 조회/생성, 문서 검색

### 3. GitHub
- **용도**: Issues, Pull Requests, Code
- **기능**: 이슈 관리, PR 리뷰, 코드 검색

---

## Context7 MCP 설정

### 1단계: Smithery 가입 및 Claude code 선택

https://smithery.ai/server/@upstash/context7-mcp

### 2단계: 설치 스크립트 복사 및 붙여넣고 실행

```bash
claude mcp add --transport http upstash-context-7-mcp "https://server.smithery.ai/@upstash/context7-mcp/mcp"
```

### 3단계: 설치된 MCP 목록 확인
```
/mcp
```

---

## Context7 MCP 사용법

### 기본 사용

```
# Claude Code에서 직접 요청
> Spring Boot 3의 최신 @ConfigurationProperties 사용법을
  공식 문서를 참조해서 알려줘

# Claude가 자동으로:
1. Context7 MCP 호출
2. Spring Boot 공식 문서 검색
3. 최신 문서 기반 답변
```

---

## Context7 활용 시나리오

### 시나리오 1: 최신 API 사용

```
> Spring Boot 3.2에서 추가된 Virtual Threads 설정 방법을
  공식 문서를 참조해서 알려줘

Claude → Context7 → Spring Boot 3.2 문서
→ application.yaml 설정
→ @Async 어노테이션 사용
→ ThreadPoolTaskExecutor 설정
```

### 시나리오 2: 버전별 차이

```
> React 17과 18의 useEffect 차이점을 공식 문서 기준으로 비교해줘

Claude → Context7 → React 17/18 문서
→ Automatic Batching 변경사항
→ StrictMode 동작 차이
→ 마이그레이션 가이드
```

---

## Context7 활용 시나리오

### 시나리오 3: 실전 코드 생성

```
> Spring Data JPA의 Specification을 사용한 동적 쿼리를
  공식 문서 예시를 참고해서 구현해줘

Claude → Context7 → Spring Data JPA 문서
→ 정확한 인터페이스 구현
→ 공식 권장 패턴
→ 타입 안전성 보장
```

**장점**: 잘못된 API 사용 방지, 최신 베스트 프랙티스 적용

---

## Atlassian MCP 설정

### 1단계: API 토큰 생성

```
https://github.com/atlassian/atlassian-mcp-server 를 보고 MCP 설치해줘! OAuth 인증방식을 사용한다.
```

### 2단계: OAuth 웹 로그인하기

---

## Atlassian MCP 사용법

### Jira 이슈 조회

```
> PROJ-123 이슈의 상세 내용을 확인해줘

Claude → Atlassian MCP → Jira
→ 이슈 제목, 설명
→ 현재 상태, 담당자
→ 댓글, 첨부파일
```

---

## Atlassian MCP 활용 시나리오

### 시나리오 1: 이슈 기반 개발

```
> PROJ-456 이슈의 요구사항을 읽고 구현 계획을 세워줘

Claude:
1. Atlassian MCP로 이슈 조회
2. Acceptance Criteria 분석
3. 구현 단계 계획 수립
4. 필요한 API 설계
5. 테스트 시나리오 작성
```

---

## Atlassian MCP 활용 시나리오

### 시나리오 2: 이슈 자동 업데이트

```
> 이 기능 구현 완료했으니 PROJ-789 이슈를
  "Done" 상태로 변경하고 구현 내용을 코멘트로 남겨줘

Claude:
1. 구현 내용 정리
2. Atlassian MCP로 이슈 업데이트
3. 상태 변경 (In Progress → Done)
4. 구현 상세 내용 코멘트 추가
5. 관련 커밋 링크 추가
```

**효과**: 개발과 프로젝트 관리 자동 연동

---

## MCP 조합 활용

### 예시: 완벽한 워크플로우

```
# 1. Jira에서 요구사항 확인
> PROJ-100 이슈 확인

# 2. Confluence에서 설계 문서 참조
> "마이크로서비스 아키텍처" 문서 검색

# 3. Context7로 최신 프레임워크 문서 참조
> Spring Cloud Gateway 최신 설정 방법

# 4. 구현
> 위 정보를 바탕으로 API Gateway 구현

# 5. 이슈 업데이트
> PROJ-100을 "In Review"로 변경하고 PR 링크 추가
```

---

## Part 2: Plan 모드

### Plan 모드란?

**Plan 모드**: 코드 작성 전에 구현 계획을 수립하는 모드 (Shift+Tab으로 전환)

```
Traditional                 Plan Mode
───────────                ───────────
요청 → 즉시 구현           요청 → 계획 수립 → 검토 → 구현
     ↓                          ↓         ↓       ↓
   실수 가능               명확한 방향  조정  정확한 구현
```

**핵심 가치**:
- 구현 전 방향성 확인
- 잘못된 접근 조기 발견
- 복잡한 작업의 체계적 분해

---

## Plan 모드 활성화

### 방법 1: 명시적 요청

```
> Plan 모드로 게시글 북마크 기능을 설계해줘
```

### 방법 2: 복잡한 요청 시 자동 활성화

```
> 마이크로서비스로 전환하고 싶어.
  현재 모놀리식 구조를 분석하고 마이그레이션 계획 세워줘
```

Claude가 자동으로 복잡도를 감지하고 Plan 모드 진입

---

## Plan 모드 Best Practices

### ✅ 언제 사용하면 좋은가?

1. **복잡한 기능 추가**
   - 여러 모듈에 걸친 변경
   - 데이터베이스 스키마 변경
   - 새로운 기술 도입

2. **대규모 리팩토링**
   - 아키텍처 변경
   - 레거시 코드 개선
   - 모듈 재구성

3. **성능 최적화**
   - 병목 지점 분석 필요
   - 여러 최적화 옵션 비교
   - 단계별 개선 전략

---

## Plan 모드 Best Practices

### ✅ 효과적인 사용법

1. **구체적인 컨텍스트 제공**
   ```
   Bad:  "성능 개선해줘"
   Good: "게시글 목록 API가 500ms 걸려.
          N+1 쿼리 문제가 있는 것 같아.
          Plan 모드로 최적화 계획 세워줘"
   ```

2. **제약사항 명시**
   ```
   "Plan 모드로 설계해줘. 단,
   - 기존 API 스펙은 유지
   - 데이터베이스 마이그레이션 최소화
   - 테스트 커버리지 80% 이상 유지"
   ```

3. **단계별 피드백**
   - 계획의 각 단계를 검토
   - 수정 사항 즉시 반영
   - 최종 승인 후 구현

---

## Part 3: YOLO 모드 (권한 검사 하지 않음)

**주의**: 프로덕션 코드보다는 실험, 학습, 프로토타입에 적합

---

## YOLO 모드 활성화

```sh
echo 'alias claude-yolo="claude --dangerously-skip-permissions"' >> .zshrc
source .zshrc

claude-yolo
```
- Claude code 승인 없이 계속 작업하게 만듦
- 코드 작업과 같이 외부 위험한 작업과 격리됐을 때만 활용