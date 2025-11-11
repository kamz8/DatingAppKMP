# 📋 Plan Działania - Pary Talk

## Status: 🟡 W Trakcie

### ✅ Ukończone
- [x] Gradle setup
- [x] Database schema (database.sq)
- [x] Models (Question, Category, PlayerSetup, HistoryEntry)
- [x] DatabaseDriverFactory (Android + iOS)

---

## 🎯 Phase 1: Repository + NFC

### 1. QuestionRepository.kt - Uzupełnić implementację
**Priorytet: 🔴 WYSOKI**

Metody do zaimplementowania:
- `getAllCategories(): List<Category>`
- `getRandomQuestion(): Question?`
- `getPlayerConfig(): PlayerSetup?`
- `savePlayerConfig(setup: PlayerSetup)`
- `getQuestionHistory(limit: Int): List<HistoryEntry>`
- `insertQuestionHistory(entry: HistoryEntry)`
- `getHistoryByCategory(categoryId: Long): List<HistoryEntry>`
- `deleteHistory()`
- `getFirstTouchEntry(): HistoryEntry?`

**Wymaga:**
- `withContext(Dispatchers.IO)` dla wszystkich DB queries
- Try-catch error handling
- Proper mapping z SQLDelight queries do models

---

### 2. SeedData.kt - Dane startowe
**Priorytet: 🔴 WYSOKI**

**5 Kategorii:**
- 🌟 Marzenia & Aspiracje
- 💕 Miłość & Relacje
- 😄 Śmieszne & Dziwne
- 🧠 Głębokie myśli
- ✈️ Przygoda & Podróże

**~25 Pytań** (przykłady):
- "Co byś zrobił/a z gwarancją sukcesu?"
- "Kiedy ostatnio czułeś/a motyle w brzuchu?"
- "Gdybyś mógł/a teleportować się, dokąd byś poszedł/a?"

**Funkcja:**
```kotlin
suspend fun seedDatabase(database: Database)
```

---

### 3. NFCManager - Android
**Priorytet: 🟠 ŚREDNI**

**Lokalizacja:** `shared/src/androidMain/kotlin/nfc/NFCManagerAndroid.kt`

**Funkcje:**
- `writeNFC(data: PlayerSetup)` - zapis przez NFC
- `readNFC(): PlayerSetup?` - odczyt z NFC
- `isNFCAvailable(): Boolean`
- `enableNFC(activity: Activity)`

**Wymaga:**
- Android NFC API
- Serialization (Kotlinx.serialization)

---

### 4. NFCManager - iOS
**Priorytet: 🟠 ŚREDNI**

**Lokalizacja:** `shared/src/iosMain/kotlin/nfc/NFCManagerIOS.kt`

**Funkcje:**
- `writeNFC(data: PlayerSetup)`
- `readNFC(): PlayerSetup?`
- `isNFCAvailable(): Boolean`

**Wymaga:**
- CoreNFC framework
- Swift interop

---

## 🎮 Phase 2: ViewModels

### 5. SetupViewModel
**Priorytet: 🟠 ŚREDNI**

**Funkcje:**
- `initDB()` - inicjalizacja + seed
- `startNFCSetup()` - rozpoczęcie NFC setup
- `startManualSetup(playerName: String, partnerName: String)` - setup manualny
- `startSoloMode(playerName: String)` - tryb solo
- `onNFCSuccess(setup: PlayerSetup)` - callback po NFC

**State:**
```kotlin
sealed class SetupState {
    object Initial
    object Loading
    data class NFCReady(val playerName: String)
    data class Success(val setup: PlayerSetup)
    data class Error(val message: String)
}
```

---

### 6. GameViewModel
**Priorytet: 🟠 ŚREDNI**

**Funkcje:**
- `nextQuestion()` - losuj następne pytanie
- `getPlayerSetup()` - pobierz config gracza
- `recordQuestion(isFirstTouch: Boolean)` - zapisz do historii

**State:**
```kotlin
data class GameState(
    val currentQuestion: Question?,
    val playerSetup: PlayerSetup?,
    val isLoading: Boolean,
    val error: String?
)
```

---

### 7. HistoryViewModel
**Priorytet: 🟡 NISKI**

**Funkcje:**
- `getHistory()` - pobierz całą historię
- `filterByCategory(categoryId: Long?)` - filtruj po kategorii
- `getFirstTouch()` - pobierz pierwsze zbliżenie

**State:**
```kotlin
data class HistoryState(
    val entries: List<HistoryEntry>,
    val selectedCategory: Long?,
    val isLoading: Boolean
)
```

---

## 🎨 Phase 3: Screens

### 8. WelcomeScreen
**Priorytet: 🟡 NISKI**

**UI:**
- Input: Imię gracza
- 3 buttony: NFC Setup / Manual Setup / Solo Mode
- Logo/tytuł aplikacji

---

### 9. SetupNFCScreen
**Priorytet: 🟡 NISKI**

**UI:**
- Animacja: "Zbliż telefon..."
- Status NFC (gotowy/skanuje/sukces)
- Cancel button

---

### 10. ManualSetupScreen
**Priorytet: 🟡 NISKI**

**UI:**
- Input: Imię partnera
- Confirm button
- Back button

---

### 11. HomeScreen
**Priorytet: 🟡 NISKI**

**UI:**
- "Cześć [Imię]! Partner: [Imię Partnera]"
- Button: GRAJ
- Button: HISTORIA
- Settings icon (opcjonalnie)

---

### 12. GameScreen
**Priorytet: 🟠 ŚREDNI**

**UI:**
- QuestionCard (kategoria emoji + nazwa, pytanie)
- Button: NASTĘPNE PYTANIE
- Button: Powrót do HOME
- "✨ ZBLIŻENIE! ✨" animation (jeśli first touch)

---

### 13. HistoryScreen
**Priorytet: 🟡 NISKI**

**UI:**
- Lista HistoryItem (kategoria, pytanie, czas)
- CategoryFilter (dropdown/chips)
- First Touch marker 💕
- Back button

---

## 🎨 Phase 4: Components + Theme

### 14. Theme
**Priorytet: 🟡 NISKI**

**Colors:**
```kotlin
val Pink = Color(0xFFE91E63)      // Primary
val Blue = Color(0xFF2196F3)      // Secondary
val Dark = Color(0xFF212121)      // Text dark
val Light = Color(0xFFFAFAFA)     // Background light
```

**Typography:**
```kotlin
h1: 32.sp, Bold
h2: 24.sp, SemiBold
body: 16.sp, Normal
```

---

### 15. Components
**Priorytet: 🟡 NISKI**

**QuestionCard:**
- Kategoria (emoji + nazwa)
- Pytanie (tekst)
- Card z rounded corners + shadow

**HistoryItem:**
- Kategoria (emoji)
- Pytanie (tekst skrócony)
- Czas
- First touch marker

**CategoryFilter:**
- Chips z kategoriami
- All / poszczególne kategorie

---

## 🚀 Kolejność Realizacji

1. ✅ **QuestionRepository.kt** → Foundation
2. ✅ **SeedData.kt** → Dane do testowania
3. 🟠 **GameViewModel** → Najprostsza logika
4. 🟠 **GameScreen** → Pierwszy działający flow
5. 🟡 **Theme** → Style dla UI
6. 🟡 **SetupViewModel + WelcomeScreen** → Onboarding
7. 🟡 **NFCManager** → Zaawansowana funkcja
8. 🟡 **HistoryViewModel + HistoryScreen** → Dodatkowa funkcja
9. 🟡 **Pozostałe screens + components** → Dopracowanie

---

## 📝 Notatki

- **Test każdej fazy** przed przejściem dalej
- **Gradle sync** po każdej większej zmianie
- **NFC testing** wymaga fizycznych urządzeń
- **First touch animation** - nice to have, nie blocker

---

**Ostatnia aktualizacja:** 2025-11-07
