# WordOfTheDay

**WordOfTheDay**는 Kotlin Multiplatform 기반의 영어 학습 앱입니다.  
Android, iOS, Desktop에서 동일한 코어 로직으로 동작하며, 텍스트 입력/음성 인식 기능을 활용하여 학습할 수 있습니다.(현재 음성인식기능은 Android, iOS만 지원)

## 🔧 기술 스택

- Kotlin Multiplatform (Android / iOS / Desktop)
- Jetpack Compose Multiplatform (UI)
- SpeechRecognizer (Android 음성 인식) / SFSpeechRecognizer (iOS 음성 인식)
- Koin (DI)
- Ktor (네트워크 통신)
- Gradle Kotlin DSL


## 🎯 주요 기능

- ✅ 매일 추천 영어 단어 제시
- 🎤 실시간 음성 인식으로 문장 말하기 연습
- 📊 학습 기록 저장 및 연속 학습일수 관리
- 🧠 난이도별 단어 학습 (BEGINNER, INTERMEDIATE, ADVANCED)
