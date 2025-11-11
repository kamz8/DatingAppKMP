# 🤖 Claude Code - Instrukcje Pracy

## 📌 Cel Projektu

Aplikacja **"Pary Talk"** - KMP Dating App dla par (Android + iOS).

**Główna funkcjonalność:**
- Losowe pytania z kategorii
- Setup przez NFC (raz na początek)
- Historia pytań lokalnie
- Brak backendu (P2P)

---

## 🎯 Jak Pracować z Projektem

### 1. **Zanim Zaczniesz**

Przeczytaj:
- `CLAUDE_CODE_GUIDE.md` - overview projektu
- `design-prototype.html` - UI/UX design
- `.claude-code` - konfiguracja

### 2. **Rozmowa z Tobą (User)**

Zawsze pytaj o:
- ✅ Czy dodać feature czy skupić się na V1?
- ✅ Czy zmienić UI/design?
- ✅ Czy to powinno działać inaczej?

Nie rób bez pytania:
- ❌ Big refactors
- ❌ Zmiana struktury danych
- ❌ Nowe screens/features

### 3. **Generuj Kod**

```
ZAWSZE:
1. Czytaj plik context - co już jest
2. Pytaj o clarification jeśli potrzebujesz
3. Pisz kod w sekcjach (nie cały plik naraz)
4. Explain co robisz
5. Daj link do pliku
6. Pytaj co dalej

NIGDY:
- Nie pisz gigantycznych plików bez podziału
- Nie rób assumptions - pytaj!
- Nie zmieniaj kodu bez warningi
```

### 4. **Struktura Odpowiedzi**

```
## ✅ [Nazwa Taska]

**Co robimy:**
- Punkt 1
- Punkt 2

**Kod:**
[file_create/str_replace]

**Wyjaśnienie:**
Co to robi i dlaczego

**Następnie:**
Jakie są kolejne kroki?

**Pytanie:**
Czy mogę kontynuować z [X]?
```

---

## 🏗️ Prioritety (Do Robienia)

### Phase 1: Repository + NFC (PIERWSZA)
```
1. ✅ Database.sq (schema)
2. ✅ Models (Question, Category, PlayerSetup)
3. ✅ DatabaseDriverFactory (Android + iOS)
4. ⏳ QuestionRepository.kt (COMPLETE)
   - getAllCategories()
   - getRandomQuestion()
   - getPlayerConfig()
   - savePlayerConfig()
   - getQuestionHistory()
   - insertQuestionHistory()
5. ⏳ SeedData.kt (load initial questions)
6. ⏳ NFCManager.kt (Android)
7. ⏳ NFCManager.kt (iOS)
```

### Phase 2: ViewModels (DRUGA)
```
1. SetupViewModel
   - initDB()
   - startNFCSetup()
   - startManualSetup()
   - startSoloMode()
   - onNFCSuccess()

2. GameViewModel
   - nextQuestion()
   - getPlayerSetup()
   - recordQuestion()

3. HistoryViewModel
   - getHistory()
   - filterByCategory()
```

### Phase 3: Screens (TRZECIA)
```
1. WelcomeScreen
2. SetupNFCScreen
3. ManualSetupScreen
4. HomeScreen
5. GameScreen (+ FirstTouchAnimation)
6. HistoryScreen
```

### Phase 4: Components + Theme (CZWARTA)
```
1. QuestionCard
2. HistoryItem
3. CategoryFilter
4. Theme (colors, typography)
```

---

## 📝 Kod - Standardy

### Imports
```kotlin
// Zawsze alfabetycznie
import androidx.compose.foundation.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.*
```

### Naming
```kotlin
// Variables
val currentQuestion = ...
var isLoading = ...
private val _state = MutableStateFlow()

// Functions
fun nextQuestion() { }
suspend fun loadQuestions() { }

// Classes
class GameViewModel { }
data class Question { }
sealed class Screen { }

// Properties
val question: StateFlow<Question?>
```

### Suspend Functions
```kotlin
// ALWAYS use withContext(Dispatchers.IO) dla DB
suspend fun getQuestion(): Question? = withContext(Dispatchers.IO) {
    // database query
}
```

### @Serializable
```kotlin
// ZAWSZE dla modeli które przesyłamy przez NFC
@Serializable
data class PlayerSetup(...)

@Serializable
enum class SetupMethod { NFC, MANUAL, SOLO }
```

### Composable
```kotlin
@Composable
fun MyScreen(
    viewModel: MyViewModel,
    onNavigate: (Screen) -> Unit
) {
    val state by viewModel.state.collectAsState()
    
    when (state) {
        // UI
    }
}
```

---

## 🔄 Wzór Pracy

### Step 1: Understand
```
- Co dokładnie needs to be done?
- Czy data model wystarczy?
- Czy API (repository) wystarczy?
```

### Step 2: Ask
```
- User, czy to jest OK?
- Czy coś zmienić?
- Czy to jest priority czy later?
```

### Step 3: Code
```
- Write one file/function at a time
- Explain what it does
- Link to created file
```

### Step 4: Connect
```
- Gdzie to się podłącza?
- Jakie dependencies?
- Czy coś innego trzeba updatować?
```

### Step 5: Test
```
- Czy to się builduje?
- Czy logika ma sense?
- Czy jest error handling?
```

---

## 💾 File Operations

### Czytaj istniejący plik
```
use: view /path/to/file.kt
```

### Twórz nowy plik
```
use: create_file
```

### Edytuj plik
```
use: str_replace
- Find unique string
- Replace with new code
```

---

## 🚨 Error Handling

### Zawsze dodaj try-catch
```kotlin
try {
    val result = repository.getData()
} catch (e: Exception) {
    println("Error: ${e.message}")
    _error.value = e
}
```

### ViewModel error state
```kotlin
private val _error = MutableStateFlow<String?>(null)
val error = _error.asStateFlow()
```

### UI error display
```kotlin
error?.let {
    Text("Błąd: $it", color = Color.Red)
}
```

---

## 📱 Platform-Specific Code

### Android Only
```kotlin
// shared/src/androidMain/kotlin/
class NFCManagerAndroid(context: Context) {
    // Android specific NFC code
}
```

### iOS Only
```kotlin
// shared/src/iosMain/kotlin/
class NFCManagerIOS {
    // iOS specific NFC code
}
```

### Shared (Both)
```kotlin
// shared/src/commonMain/kotlin/
interface NFCManager {
    fun readNFC()
    fun writeNFC()
}
```

---

## 🎨 UI - Compose Patterns

### State Management
```kotlin
val state by viewModel.state.collectAsState()

when (state) {
    Loading -> LoadingScreen()
    Ready -> ContentScreen()
    Error -> ErrorScreen()
}
```

### Navigation
```kotlin
var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }

when (currentScreen) {
    Screen.Home -> HomeScreen { currentScreen = Screen.Game }
    Screen.Game -> GameScreen { currentScreen = Screen.History }
}
```

### Colors
```kotlin
Color(0xFFE91E63)  // Pink (primary)
Color(0xFF2196F3)  // Blue (secondary)
Color(0xFF212121)  // Dark
Color(0xFFFAFAFA)  // Light
```

### Typography
```kotlin
Text(
    "Title",
    fontSize = 32.sp,
    fontWeight = FontWeight.Bold
)
```

---

## 🧪 Testing Checklist

- [ ] Projekt builduje bez erroru
- [ ] Gradle sync OK
- [ ] Imports OK
- [ ] Funkcje mają bodies
- [ ] Error handling present
- [ ] Type safety (nie Any)
- [ ] Coroutines prawnie użyte
- [ ] StateFlow/MutableStateFlow properly used

---

## 📞 Komunikacja z Userem

### Zawsze mów:
```
"Czy mogę teraz zrobić [X]?"
"To będzie działać tak [wyjaśnienie]"
"Następnie powinniśmy [kolejny krok]"
"Czy to jest OK, czy zmienić?"
```

### Nie rób bez pytania:
```
"Mogę dodać feature [X]?"
"Mogę refactorować [Y]?"
"Mogę zmienić design [Z]?"
```

---

## 🎯 Current Status

**Completed:**
- ✅ Gradle setup
- ✅ Database schema
- ✅ Models
- ✅ DatabaseDriverFactory

**In Progress:**
- ⏳ QuestionRepository (needs full implementation)

**TODO:**
- ⏳ SeedData
- ⏳ NFCManager (Android + iOS)
- ⏳ All ViewModels
- ⏳ All Screens
- ⏳ Components + Theme

---

## 🚀 Ready?

Na pytania użytkownika zawsze:
1. Przeczytaj kontekst
2. Pytaj jeśli nie wiesz
3. Pisz step-by-step
4. Explain Everything
5. Link do kodu
6. Ask next steps

**Let's build Pary Talk! 💕**