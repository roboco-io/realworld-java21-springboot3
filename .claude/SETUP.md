# Claude Code 규칙 자동 참조 설정 가이드

이 가이드는 `.claude/rules.md`를 **자동으로** 참조하도록 만드는 방법을 설명합니다.

## 현재 설정 상태

✅ **완료된 설정:**
1. `.claude/rules.md` - 프로젝트 작업 규칙 정의
2. 루트 `CLAUDE.md` - 규칙 파일 참조 명시
3. 모듈별 `CLAUDE.md` - 규칙 파일 참조 명시
4. 커스텀 명령어들 - 규칙 준수 명시

## 규칙 자동 참조 동작 방식

### 1. Claude Code가 프로젝트를 읽을 때
```
새 세션 시작
    ↓
루트 CLAUDE.md 읽기
    ↓
"⚠️ 필수 규칙" 섹션 발견
    ↓
.claude/rules.md 참조 필요성 인지
    ↓
작업 시 rules.md 내용 고려
```

### 2. 커스텀 명령어 사용 시
```
/review 또는 /new-feature 실행
    ↓
명령어 프롬프트 로드
    ↓
".claude/rules.md를 준수하세요" 포함
    ↓
Claude Code가 규칙 확인
```

### 3. 규칙 준수 검증
```
/check-rules 실행
    ↓
.claude/rules.md 전체 읽기
    ↓
관련 규칙 요약 제공
    ↓
준수 확약 메시지 출력
```

## 추가 강화 방법 (선택 사항)

### 방법 1: 글로벌 CLAUDE.md에 추가

프로젝트별이 아닌 **모든 프로젝트**에서 규칙 파일을 자동 참조하려면:

**파일 위치:** `~/.claude/CLAUDE.md`

**추가할 내용:**
```markdown
## 프로젝트 규칙 자동 참조

작업 시작 전 다음 파일들을 자동으로 확인하세요:
1. 프로젝트 루트의 `CLAUDE.md`
2. 프로젝트 루트의 `.claude/rules.md` (있는 경우)
3. 현재 작업 중인 모듈의 `CLAUDE.md` (있는 경우)

이 파일들에 정의된 규칙을 반드시 준수하세요.
```

### 방법 2: Pre-commit Hook (Git)

규칙 위반 코드를 커밋 전에 자동 검증:

**파일 위치:** `.git/hooks/pre-commit`

```bash
#!/bin/bash

echo "🔍 Checking code style..."
./gradlew spotlessCheck

if [ $? -ne 0 ]; then
    echo "❌ Code style check failed. Run: ./gradlew spotlessApply"
    exit 1
fi

echo "✅ Code style check passed"
exit 0
```

실행 권한 부여:
```bash
chmod +x .git/hooks/pre-commit
```

### 방법 3: IDE 설정 (IntelliJ IDEA)

**File Watchers 설정:**
1. Settings → Tools → File Watchers
2. Add → Custom
3. Name: "Claude Rules Reminder"
4. File type: Java
5. Program: echo
6. Arguments: "Remember to follow .claude/rules.md!"

## 규칙 준수 워크플로우

### 새 기능 추가 시
```bash
1. /check-rules           # 관련 규칙 확인
2. /new-feature [기능]    # 규칙 준수하며 구현
3. /test                  # 테스트 규칙 준수 확인
4. /review                # 최종 규칙 준수 검증
5. ./gradlew spotlessApply # 코드 스타일 적용
6. git commit             # 커밋
```

### 버그 수정 시
```bash
1. /check-rules           # 관련 규칙 확인
2. /fix-bug [설명]        # 규칙 준수하며 수정
3. /test                  # 회귀 테스트
4. /review                # 규칙 준수 검증
5. git commit             # 커밋
```

### 리팩토링 시
```bash
1. /check-rules           # 관련 규칙 확인
2. /refactor [대상]       # 규칙 준수하며 리팩토링
3. /test                  # 기능 동작 확인
4. /review                # 개선 검증
5. git commit             # 커밋
```

## 규칙 위반 방지 체크리스트

작업 전:
- [ ] `/check-rules`로 관련 규칙 확인
- [ ] CLAUDE.md에서 모듈 구조 확인

작업 중:
- [ ] `.claude/rules.md`의 코딩 표준 준수
- [ ] 테스트 규칙 준수 (특히 예외 메시지 검증 금지!)
- [ ] 아키텍처 규칙 준수 (모듈 의존성)

작업 후:
- [ ] `/test`로 테스트 생성 및 실행
- [ ] `/review`로 규칙 준수 검증
- [ ] `./gradlew spotlessApply` 실행
- [ ] `./gradlew build` 성공 확인

## FAQ

### Q: Claude Code가 규칙을 무시하면?
A: 명시적으로 상기시키세요:
```
".claude/rules.md 파일의 [특정 규칙]을 준수해주세요."
```

### Q: 규칙이 너무 많아서 잊어버리면?
A: `/check-rules` 명령어로 작업 시작 전 관련 규칙만 확인하세요.

### Q: 새 규칙을 추가하려면?
A: `.claude/rules.md` 파일을 직접 수정하세요. 즉시 반영됩니다.

### Q: 특정 규칙을 임시로 무시하려면?
A: **권장하지 않습니다.** 규칙은 프로젝트 품질을 위한 것이므로, 예외가 필요하다면 팀과 논의 후 rules.md를 수정하세요.

## 규칙 업데이트 이력

변경 사항이 있을 때마다 여기에 기록하세요:

### 2025-01-XX
- 초기 규칙 파일 생성
- 코딩 표준, 아키텍처, 테스트 규칙 정의

---

**이제 모든 작업 시 `.claude/rules.md`가 자동으로 참조됩니다!** 🎉
