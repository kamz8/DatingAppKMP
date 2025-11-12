# 📋 TODO List - Pary Talk App

## 🔥 Priorytet: Wysoki

### 1. ✅ **System sesji (ZROBIONE)**
- [x] Pytania nie powtarzają się w sesji
- [x] Licznik pytań w sesji
- [x] Przycisk "Nowa sesja"

### 2. ⏳ **NFC Setup Screen**
**Lokalizacja:** `App.kt:78`
```kotlin
// TODO: Implement NFC setup screen
// For now, just show manual setup
currentScreen = Screen.ManualSetup(playerName)
```
**Opis:** Implementacja ekranu NFC setup dla synchronizacji między dwoma urządzeniami
- [ ] NFCSetupScreen.kt (UI)
- [ ] NFCManager.kt (Android - expect/actual)
- [ ] NFCManager.kt (iOS - expect/actual)
- [ ] NFC data transfer protocol
- [ ] Error handling

### 3. ✅ **Customowy Launcher Icon (ZROBIONE)**
**Lokalizacja:** `composeApp/src/androidMain/res/`
- [x] ic_launcher.xml (adaptive icon) - działa z istniejącym
- [x] ic_launcher_background.xml (Pink gradient background)
- [x] ic_launcher_foreground.xml (White heart + question mark)
- [x] Design: Serce z małym sercem i znakiem zapytania na różowym tle

---

## 🎨 Design & UX

### 4. ⏳ **Deprecated API warnings**
**Lokalizacja:**
- `ManualSetupScreen.kt:98` - Divider → HorizontalDivider
- `WelcomeScreen.kt:129` - Divider → HorizontalDivider

### 5. ✅ **Animacje i transitions (ZROBIONE)**
- [x] Slide animations między ekranami (AnimatedContent)
- [x] Scale + Fade animation dla pytań w GameScreen
- [x] Smooth transitions w nawigacji (300ms slide)
- [x] Animated list items w HistoryScreen (animateItem)

### 6. ⏳ **Dark Mode**
- [ ] Theme switcher
- [ ] Dark color palette
- [ ] Persistencja preferencji użytkownika

---

## 🚀 Features

### 7. ⏳ **Filtrowanie pytań po kategorii**
- [ ] Wybór kategorii przed rozpoczęciem gry
- [ ] Losowanie tylko z wybranych kategorii
- [ ] Zapisanie preferencji kategorii

### 8. ⏳ **Statystyki**
- [ ] Ile pytań zadaliście razem
- [ ] Najbardziej popularne kategorie
- [ ] Wykresy i visualizacje

### 9. ⏳ **Edycja własnych pytań**
- [ ] Dodawanie customowych pytań
- [ ] Edycja istniejących pytań
- [ ] Usuwanie pytań

### 10. ⏳ **Eksport/Import danych**
- [ ] Eksport historii do JSON/CSV
- [ ] Backup bazy danych
- [ ] Import pytań z pliku

---

## 🔧 Technical Debt

### 11. ⏳ **Beta warnings**
**Lokalizacja:** `DatabaseDriverFactory.kt`
```
'expect'/'actual' classes are in Beta
```
- [ ] Rozważyć użycie `-Xexpect-actual-classes` flag
- [ ] Lub zignorować warning jeśli API jest stabilne

### 12. ⏳ **Error handling improvements**
- [ ] Lepsze error messages dla użytkownika
- [ ] Crash reporting (Firebase Crashlytics?)
- [ ] Logging system

### 13. ⏳ **Performance**
- [ ] Lazy loading pytań
- [ ] Database indices
- [ ] Memory profiling

---

## 🧪 Testing

### 14. ⏳ **Unit Tests**
- [x] Podstawowe smoke tests (3 testy)
- [ ] Repository tests (z in-memory DB)
- [ ] ViewModel tests (z Turbine)
- [ ] Integration tests

### 15. ⏳ **UI Tests**
- [ ] Espresso/Compose UI tests
- [ ] Screenshot tests
- [ ] E2E tests dla głównych flow

---

## 📱 Platform Specific

### 16. ⏳ **iOS**
- [ ] Test na prawdziwym urządzeniu iOS
- [ ] iOS specific NFC implementation
- [ ] App Store assets

### 17. ⏳ **Android**
- [ ] Material You dynamic colors
- [ ] Widgets
- [ ] Shortcuts
- [ ] Google Play assets

---

## 📝 Documentation

### 18. ⏳ **User Documentation**
- [ ] README.md z instrukcją użytkowania
- [ ] FAQ
- [ ] Privacy Policy
- [ ] Terms of Service

### 19. ⏳ **Developer Documentation**
- [x] CLAUDE.md (instrukcje dla AI)
- [x] PLAN.md (plan rozwoju)
- [ ] Architecture Decision Records (ADR)
- [ ] API documentation

---

## 🎯 Roadmap

### Faza 1: MVP ✅ (COMPLETED)
- [x] Database setup
- [x] 100 pytań w 8 kategoriach
- [x] Podstawowa nawigacja
- [x] System sesji (no duplicates)

### Faza 2: Polish 🔄 (IN PROGRESS)
- [x] Customowy launcher icon
- [ ] NFC setup
- [ ] Dark mode
- [x] Animations

### Faza 3: Advanced Features
- [ ] Custom questions
- [ ] Statistics
- [ ] Category filters
- [ ] Cloud sync (opcjonalnie)

### Faza 4: Release
- [ ] Beta testing
- [ ] Play Store listing
- [ ] App Store listing
- [ ] Marketing materials

---

## 💡 Ideas for Future

- [ ] Daily notifications z pytaniem dnia
- [ ] Achievements/Badges system
- [ ] Couples challenges
- [ ] Social sharing
- [ ] Multi-language support
- [ ] Voice questions (TTS)
- [ ] Integration z kalendarzem (date night reminders)

---

**Last Updated:** 2025-11-11
**Total TODOs:** 19 categories
**Priority Items:** NFC Setup, Launcher Icon, Animations
