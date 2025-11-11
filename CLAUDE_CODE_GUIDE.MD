# 💕 Pary Talk - Claude Code Guide

## 📌 Projekt Overview

**Dating app dla par** - KMP (Kotlin Multiplatform) z Compose Multiplatform.

**Specs:**
- Android (Ty) ↔ iOS (Dziewczyna)
- Komunikacja: NFC (brak backendu!)
- Baza: SQLite offline
- UI: Compose Multiplatform (jeden kod!)
- Pytania: ~25 seed data

---

## 🎯 Co Aplikacja Robi

1. **Setup (pierwszy raz):**
    - Ty: Wpisujesz imię "Maciek"
    - Zbliżasz Android do iPhone'a (NFC)
    - Dziewczyna wpisuje imię "Zofia"
    - Oba telefony zapisują konfigurację

2. **Granie:**
    - Klikasz "GRAJ"
    - Wyświetli się losowe pytanie
    - Przeczytacie pytanie głośnie
    - Odpowiadacie
    - Klikasz "NASTĘPNE PYTANIE"
    - Repeat...

3. **Historia:**
    - Każde pytanie zapisuje się lokalnie
    - Możesz filtrować po kategoriach
    - "Pierwsze zbliżenie" zaznaczone specjalnie 💕

---

## 📁 Struktura Projektu

```
dating-app-kmp/
├── build.gradle.kts                    ✅
├── settings.gradle.kts                 ✅
├── gradle.properties                   ✅
│
├── shared/                             # KMP Module
│   ├── build.gradle.kts                ✅
│   ├── src/commonMain/
│   │   ├── kotlin/
│   │   │   ├── models/ (4 files)       ✅
│   │   │   ├── data/
│   │   │   │   ├── DatabaseDriverFactory.kt ✅
│   │   │   │   ├── QuestionRepository.kt    ⏳ NEEDS WORK
│   │   │   │   └── SeedData.kt             ⏳ TODO
│   │   │   ├── domain/ (3 ViewModels)  ⏳ TODO
│   │   │   └── utils/                  ⏳ TODO
│   │   └── sqldelight/database.sq      ✅
│   ├── src/androidMain/                ✅
│   └── src/iosMain/                    ✅
│
├── composeApp/                         # UI Module
│   ├── build.gradle.kts                ✅
│   └── src/ (6 screens, components)    ⏳ TODO
│
├── design-prototype.html               # Reference ✅
├── Claude.md                           # Instructions
├── CLAUDE_CODE_GUIDE.md               # This file
└── .claude-code                        # Config
```

---

## 🗄️ Database

### 4 Tabele

**CATEGORY** - 5 kategorii pytań
```
🌟 Marzenia & Aspiracje
💕 Miłość & Relacje
😄 Śmieszne & Dziwne
🧠 Głębokie myśli
✈️ Przygoda & Podróże
```

**QUESTION** - ~25 pytań (seed)
```
"Co byś zrobił/a z gwarancją sukcesu?"
"Kiedy ostatnio czułeś/a motyle w brzuchu?"
...
```

**PLAYER_CONFIG** - Konfiguracja (jedna linia per device)
```
Maciek (Android) ↔ Zofia (iOS)
Setup: NFC / MANUAL / SOLO
```

**QUESTION_HISTORY** - Rosnąca historia
```
Pytanie | Kategoria | Czas | Czy Ty? | Pierwsze zbliżenie?
```

---

## 🔌 NFC Flow

### Android → iOS
```
Android (Ty):
1. Losuje pytanie
2. Serializes to JSON
3. Writes to NFC tag

iOS (Dziewczyna):
1. Scans NFC tag
2. Reads JSON
3. Deserializes
4. Saves locally
5. Shows: "✨ ZBLIŻENIE! ✨"
```

### Setup (First Time)
```
Android: "Zbliż iPhone"
iOS: "Zbliż do Android'a"
↓ (NFC scan)
Both: Save player_config
Both: Navigate to Home
```

---

## 🎮 Game Flow - V1 (Simple)

```
Home → GRAJ → Game Screen
             ↓
        Show Question
             ↓
        NASTĘPNE → New Question
             ↓
        Repeat...

        Or: HISTORIA → History Screen
                      ↓
                   Filter/Browse
                      ↓
                   Back → Home
```

---

## 💾 Models

**Question**
```kotlin
id, categoryId, text, createdAt
```

**Category**
```kotlin
id, name, emoji
```

**PlayerSetup**
```kotlin
playerId, playerName, partnerId, partnerName, deviceType, setupMethod, setupDate
```

**HistoryEntry**
```kotlin
id, questionId, questionText, categoryId, categoryName, categoryEmoji, askedAt, yourTurn, isFirstTouch
```

---

## 🔧 Repository Methods

Must implement:
```kotlin
getAllCategories(): List<Category>
getRandomQuestion(): Question?
getPlayerConfig(): PlayerSetup?
savePlayerConfig(setup: PlayerSetup)
getQuestionHistory(limit: Int): List<HistoryEntry>
insertQuestionHistory(entry: HistoryEntry)
getHistoryByCategory(categoryId: Long): List<HistoryEntry>
deleteHistory()
getFirstTouchEntry(): HistoryEntry?
```

---

## 🎨 UI Screens (6 Total)

1. **WelcomeScreen** - Imię + opcje (NFC/Manual/Solo)
2. **SetupNFCScreen** - "Zbliż telefon"
3. **ManualSetupScreen** - Wpisz dane partnera
4. **HomeScreen** - "Cześć Maciek! Partner: Zofia"
5. **GameScreen** - Pytanie + NASTĘPNE button
6. **HistoryScreen** - Lista + filtrowanie

---

## 🚀 Build

```bash
# Android
./gradlew :composeApp:installDebug

# iOS
./gradlew :composeApp:runDebugIosForeground
```

---

## 🎯 Next Steps (Priority)

**Phase 1:**
- [ ] Complete QuestionRepository
- [ ] Create SeedData.kt
- [ ] Create NFCManager (Android + iOS)

**Phase 2:**
- [ ] Create ViewModels (3x)

**Phase 3:**
- [ ] Create Screens (6x)
- [ ] Create Components
- [ ] Create Theme

---

**Check Claude.md for detailed work instructions!**