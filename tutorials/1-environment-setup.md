---
marp: true
theme: default
paginate: true
header: '환경 설정'
---

# 환경 설정
## 개발 환경 준비하기

Java 21, GitHub CLI, Claude Code CLI 설치

---

## 이번 세션의 목표

### 설치할 도구들
- ✅ **Java 21** - 프로젝트 실행 환경
- ✅ **GitHub CLI** - Git 작업 자동화
- ✅ **Claude Code CLI** - AI 협업 도구

### 검증 사항
- 모든 도구가 정상 작동
- RealWorld 프로젝트 빌드 성공
- Claude Code 인증 완료

**예상 소요 시간**: 30분

---

## Java 21 설치: macOS

### 방법 1: Homebrew (권장)

```bash
# OpenJDK 21 설치
brew install openjdk@21

# 심볼릭 링크 생성
sudo ln -sfn $(brew --prefix openjdk@21)/libexec/openjdk.jdk \
  /Library/Java/JavaVirtualMachines/openjdk-21.jdk

# 환경 변수 설정
echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 21)' >> ~/.zshrc
echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.zshrc
source ~/.zshrc

# 확인
java --version
```

---

## Java 21 설치: macOS (계속)

### 방법 2: SDKMAN (버전 관리 필요 시)

```bash
# SDKMAN 설치
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"

# Java 21 설치
sdk install java 21.0.5-tem

# 기본 버전으로 설정
sdk default java 21.0.5-tem

# 확인
java --version
```

**장점**: 여러 Java 버전 관리 가능

---

## Java 21 설치: Windows

### Windows Subsystem for Linux (WSL) 권장

```powershell
# 1. WSL 설치 (PowerShell 관리자 모드)
wsl --install

# 2. 시스템 재부팅

# 3. WSL Ubuntu 실행
wsl

# WSL 내부에서 Java 설치
curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
sudo apt-get install -y nodejs
```

**이유**: Claude Code는 Unix 환경에 최적화

---

## Java 21 설치: Windows (계속)

### 네이티브 Windows (대안)

```powershell
# Scoop 패키지 매니저 사용
scoop install openjdk21

# 또는 winget
winget install Microsoft.OpenJDK.21

# 환경 변수 확인
$env:JAVA_HOME
java -version
```

**참고**: Git Bash에서 Claude Code 실행 가능

---

## Java 설치 확인

### 필수 검증 단계

```bash
# 1. Java 버전 확인
java --version
# 출력 예시:
# openjdk 21.0.5 2024-10-15
# OpenJDK Runtime Environment (build 21.0.5+11)

# 2. Java 컴파일러 확인
javac --version
# 출력: javac 21.0.5

# 3. JAVA_HOME 확인
echo $JAVA_HOME
# 출력: /Library/Java/JavaVirtualMachines/openjdk-21.jdk/Contents/Home
```

**모두 정상 출력되면 성공!** ✅

---

## GitHub CLI 설치

### 왜 GitHub CLI?

**기능**:
- ✅ PR 생성/관리 CLI에서
- ✅ Issue 작업 자동화
- ✅ Repository 관리
- ✅ GitHub Actions 제어

**Claude Code 통합**: Claude가 PR을 자동으로 생성 가능

---

## GitHub CLI 설치: macOS

```bash
# Homebrew로 설치
brew install gh

# 설치 확인
gh --version
```

### GitHub CLI 설치: Windows

```powershell
# Scoop 사용
scoop install gh

# 또는 winget
winget install GitHub.cli

# 확인
gh --version
```

---

## GitHub CLI 인증

### 로그인 과정

```bash
# 인증 시작
gh auth login

# 프롬프트 응답:
# 1. GitHub.com 선택
# 2. HTTPS 프로토콜 선택
# 3. Authenticate 선택
# 4. 웹 브라우저에서 권한 부여

# 인증 확인
gh auth status
```

---

## Claude Code CLI 설치

### 시스템 요구사항

- **Node.js**: 18.0 이상
- **RAM**: 최소 4GB
- **네트워크**: 인터넷 연결 필수
- **셸**: Bash, Zsh, Fish 권장

### Node.js 설치 (필요시)

```bash
# macOS
brew install node

# Windows (WSL 또는 Scoop)
scoop install nodejs
```

---

## Claude Code CLI 설치: 실행

### npm으로 전역 설치

```bash
# Claude Code 설치
npm install -g @anthropic-ai/claude-code

# 설치 확인
claude --version

# 도움말 확인
claude --help
```

---

## Claude Code 인증

### 인증 옵션

**옵션 1: Claude Console**
- https://console.anthropic.com 에서 API 키 생성
- 활성 청구 계정 필요
- API 사용량 기반 과금

**옵션 2: Claude App (Pro/Max 플랜)**
- Claude.ai Pro 또는 Max 구독자
- 웹 앱과 동일한 계정 사용

---

## Claude Code 인증: 실행

### 초기 실행

```bash
# Claude Code 시작
claude

# 프롬프트가 표시되면:
# 1. API 키 입력 또는
# 2. Claude App 로그인 선택
```

### 환경 변수로 설정 (선택사항)

```bash
# API 키 설정
export ANTHROPIC_API_KEY="your_api_key_here"

# 영구 설정
echo 'export ANTHROPIC_API_KEY="your_key"' >> ~/.zshrc
source ~/.zshrc
```

---

## RealWorld 프로젝트 준비

### Fork 및 Clone

```bash
# 1. GitHub에서 Fork
# https://github.com/1chz/realworld-java21-springboot3
# Fork 버튼 클릭

# 2. Clone (your-username을 본인 계정으로)
git clone https://github.com/your-username/realworld-java21-springboot3.git

# 3. 디렉토리 이동
cd realworld-java21-springboot3
```

---

## 프로젝트 빌드 검증

### 첫 빌드 실행

```bash
# 전체 빌드 (테스트 포함)
./gradlew clean build

# 예상 소요 시간: 30초 ~ 1분
```

**성공 출력**:
```
BUILD SUCCESSFUL in 45s
```

---

## 설치 확인 체크리스트

### 모든 도구 테스트

```bash
# Java 확인
java --version

# GitHub CLI 확인
gh --version

# Claude Code 확인
claude --version

# Git 확인
git --version
```

---

## Claude Code 기본 사용법

### 프로젝트에서 시작

```bash
# 프로젝트 디렉토리에서
cd realworld-java21-springboot3
claude

# 대화 시작
> 이 프로젝트의 구조를 분석해줘

# Claude가 파일을 읽고 분석 시작
```

### 종료
```
Ctrl+C 또는 /exit
```

---

## Claude Code 편의 기능

### Alias 설정 (선택사항)

```bash
# 승인 없이 계속 작업
echo 'alias claude-yolo="claude --dangerously-skip-permissions"' >> ~/.zshrc
source ~/.zshrc

# 사용
claude-yolo
```

**주의**: 외부 위험이 없는 격리된 환경에서만 사용!

### 프로젝트 상태 확인

```bash
# 현재 세션 상태
claude --status

# 설정 확인
claude config
```

---

## 환경 설정 완료 확인

### 최종 검증

```bash
# 1. Java 21 실행
java --version | grep "21"

# 2. 프로젝트 빌드
./gradlew clean build

# 3. 애플리케이션 실행
./gradlew realworld:bootRun
# (Ctrl+C로 종료)

# 4. GitHub CLI 인증
gh auth status

# 5. Claude Code 실행
claude --version
```

**모두 성공하면 환경 설정 완료!** 🎉

---

## 다음 단계 미리보기

### Claude Code 프로젝트 설정

다음 세션에서 배울 내용:
- **CLAUDE.md** 파일 작성
- **.claude/** 디렉토리 구조
- **커스텀 명령어** 생성
- **규칙 기반 협업** 시스템

**준비사항**:
- 환경 설정 완료
- Claude Code 인증 완료
- 프로젝트 빌드 성공

---

## Tips & Best Practices

### 개발 환경 관리

✅ **SDKMAN 사용** - 여러 Java 버전 관리
✅ **Homebrew 업데이트** - 정기적으로 `brew update`
✅ **환경 변수 정리** - .zshrc 파일 깔끔하게 유지

### Claude Code 사용

✅ **프로젝트별 세션** - 각 프로젝트에서 claude 실행
✅ **권한 확인** - 중요한 변경은 직접 검토
✅ **컨텍스트 유지** - 긴 작업은 한 세션에서

---

## 참고 자료

### 공식 문서
- [Java 21 Release Notes](https://openjdk.org/projects/jdk/21/)
- [GitHub CLI 문서](https://cli.github.com/)
- [Claude Code 문서](https://docs.anthropic.com/claude-code)

### 프로젝트
- [RealWorld Spec](https://realworld-docs.netlify.app/)
- [Spring Boot 3 문서](https://spring.io/projects/spring-boot)

---

## Q&A

### 질문이 있으신가요?

- Java 21 설치 문제?
- Claude Code 인증 이슈?
- Gradle 빌드 오류?
- 기타 환경 설정 관련?

**다음 세션 전에 모든 문제를 해결합시다!**

---

# 환경 설정 완료! 🎉

## 다음: Claude Code 프로젝트 설정
### CLAUDE.md와 .claude/ 디렉토리 활용법
