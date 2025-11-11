package com.example.datingappkmp.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

object SeedData {

    suspend fun seedDatabase(repository: QuestionRepository) = withContext(Dispatchers.IO) {
        try {
            // Check if already seeded
            val existingCategories = repository.getAllCategories()
            if (existingCategories.isNotEmpty()) {
                println("Database already seeded, skipping...")
                return@withContext
            }

            println("Seeding database...")

            // Insert categories
            val categories = listOf(
                "Marzenia & Aspiracje" to "🌟",
                "Miłość & Relacje" to "💕",
                "Śmieszne & Dziwne" to "😄",
                "Głębokie myśli" to "🧠",
                "Przygoda & Podróże" to "✈️",
                "Flirt" to "😘",
                "Pikantne" to "🔥",
                "Neuroatypowość" to "🧩"
            )

            categories.forEach { (name, emoji) ->
                repository.insertCategory(name, emoji)
            }

            val currentTime = System.currentTimeMillis()

            // Get category IDs (assuming they're inserted in order starting from 1)
            val categoryIds = repository.getAllCategories().associateBy { it.name }

            // Marzenia & Aspiracje 🌟 (12 pytań)
            val dreamsQuestions = listOf(
                "Co byś zrobił/a z gwarancją sukcesu?",
                "Jaki jest Twój największy cel życiowy?",
                "Kim chciałbyś/chciałabyś być za 10 lat?",
                "Gdybyś mógł/a zmienić jedną rzecz w swoim życiu, co by to było?",
                "O czym marzysz, ale nikomu o tym nie mówisz?",
                "Jaki jest Twój największy, nieodkryty talent?",
                "Co chciałbyś/chciałabyś osiągnąć przed końcem roku?",
                "Gdybyś miał/a napisać książkę, o czym by była?",
                "Jaki projekt pasji chciałbyś/chciałabyś zrealizować?",
                "Czym chciałbyś/chciałabyś być znany/a?",
                "Jakie umiejętności chciałbyś/chciałabyś opanować?",
                "Jak wygląda Twój idealny dzień za 5 lat?"
            )

            // Miłość & Relacje 💕 (13 pytań)
            val loveQuestions = listOf(
                "Kiedy ostatnio czułeś/a motyle w brzuchu?",
                "Co sprawia, że czujesz się kochany/a?",
                "Jaki był Twój najbardziej romantyczny moment?",
                "Czego najbardziej potrzebujesz w związku?",
                "Jak wyobrażasz sobie idealną randkę?",
                "Co najbardziej cenisz w naszym związku?",
                "Jaki gest miłości najbardziej do Ciebie przemawia?",
                "Kiedy wiedziałeś/aś, że to coś więcej?",
                "Co chciałbyś/chciałabyś robić razem częściej?",
                "Jaka była Twoja pierwsza myśl o mnie?",
                "Co jest dla Ciebie najważniejsze w komunikacji?",
                "Jak lubisz okazywać uczucia?",
                "Co sprawia, że czujesz się bezpiecznie w związku?"
            )

            // Śmieszne & Dziwne 😄 (13 pytań)
            val funnyQuestions = listOf(
                "Jaką najbardziej żenującą rzecz zrobiłeś/aś w życiu?",
                "Gdybyś był/a zwierzęciem, to jakim i dlaczego?",
                "Jaki jest Twój najdziwniejszy nawyk?",
                "Co najbardziej śmiesznego przydarzyło Ci się ostatnio?",
                "Jaką supermoc chciałbyś/chciałabyś mieć i jak byś jej używał/a?",
                "Jaki jest Twój guilty pleasure?",
                "Co jest najbardziej random rzeczą w Twoim telefonie?",
                "Gdybyś mógł/a być celebrytą na jeden dzień, kto to byłby?",
                "Jaki jest Twój najgorszy żart, który uważasz za genialny?",
                "Co robisz, gdy nikt nie patrzy?",
                "Jaka była Twoja najdziwniejsza faza w życiu?",
                "Gdybyś mógł/a mieć nieograniczone zapasy jednej rzeczy, co by to było?",
                "Co jest najbardziej dziwnym komplementem, jaki dostałeś/aś?"
            )

            // Głębokie myśli 🧠 (12 pytań)
            val deepQuestions = listOf(
                "Co jest dla Ciebie najważniejsze w życiu?",
                "Czego nauczyło Cię największe wyzwanie w życiu?",
                "W co głęboko wierzysz?",
                "Co chciałbyś/chciałabyś, żeby ludzie o Tobie pamiętali?",
                "Kiedy ostatnio zmieniłeś/aś zdanie na jakiś ważny temat?",
                "Co daje Ci poczucie spełnienia?",
                "Jaka jest Twoja największa obawa?",
                "Co sprawia, że czujesz się sobą?",
                "Czego żałujesz najbardziej w życiu?",
                "Co byś powiedział/a 18-letniemu sobie?",
                "Kiedy ostatnio czułeś/aś się naprawdę wdzięczny/a?",
                "Co według Ciebie nadaje życiu sens?"
            )

            // Przygoda & Podróże ✈️ (12 pytań)
            val adventureQuestions = listOf(
                "Gdybyś mógł/a teleportować się w jedno miejsce, dokąd byś poszedł/poszła?",
                "Jaka była Twoja największa przygoda?",
                "Gdzie chciałbyś/chciałabyś mieszkać przez rok?",
                "Co jest na Twojej liście rzeczy do zrobienia przed śmiercią?",
                "Gdybyś miał/a nieograniczony budżet na podróż, dokąd byś pojechał/a?",
                "Jakie miejsce najbardziej Cię zaskoczyło?",
                "Wolisz góry czy morze i dlaczego?",
                "Jaki sport ekstremalny chciałbyś/chciałabyś spróbować?",
                "Gdzie było Twoje najpiękniejsze wspomnienie z podróży?",
                "Gdybyś mógł/a mieszkać w van przez rok, dokąd byś pojechał/a?",
                "Jaki był Twój najdziwniejszy posiłek w podróży?",
                "Co jest najodważniejszą rzeczą, jaką kiedykolwiek zrobiłeś/aś?"
            )

            // Flirt 😘 (13 pytań)
            val flirtQuestions = listOf(
                "Co było pierwszą rzeczą, którą we mnie zauważyłeś/aś?",
                "Jaki komplement chciałbyś/chciałabyś ode mnie usłyszeć?",
                "Co sprawia, że czujesz się atrakcyjny/a?",
                "Jaki jest Twój ulubiony sposób na flirt?",
                "Co jest najbardziej seksowną cechą charakteru?",
                "Wolisz randkę przy świecach czy spontaniczną przygodę?",
                "Co sprawia, że ktoś jest dla Ciebie interesujący?",
                "Jaki był Twój najbardziej udany pick-up line?",
                "Co najbardziej Cię pociąga w drugiej osobie?",
                "Jak wygląda Twój idealny pocałunek?",
                "Co sprawia, że jesteś w nastroju na bliskość?",
                "Jaki gest znajdujesz najbardziej uwodzicielski?",
                "Co myślałeś/aś o mnie, gdy pierwszy raz się spotkaliśmy?"
            )

            // Pikantne 🔥 (13 pytań)
            val spicyQuestions = listOf(
                "Jaka jest Twoja tajna fantazja?",
                "Co najbardziej podnieca Twoją wyobraźnię?",
                "Jakie jest najbardziej spontaniczne miejsce, w którym..?",
                "Co jest Twoją ukrytą stroną?",
                "Jaki jest Twój guilty pleasure w łóżku?",
                "Co chciałbyś/chciałabyś spróbować, ale się wahasz?",
                "Jakie jest najbardziej odważne, co kiedykolwiek zrobiłeś/aś?",
                "Co sprawia, że tracisz kontrolę?",
                "Jaki jest Twój idealny scenariusz intymnej bliskości?",
                "Co jest dla Ciebie biggest turn-on?",
                "Jakie słowa lubisz słyszeć w intymnych momentach?",
                "Co jest najbardziej ekscytującą rzeczą w naszym związku?",
                "Jaki jest Twój ulubiony sposób na pokazanie pożądania?"
            )

            // Neuroatypowość 🧩 (12 pytań)
            val neuroQuestions = listOf(
                "W jakim środowisku czujesz się najbardziej komfortowo?",
                "Jakie są Twoje ulubione sposoby na regulację emocji?",
                "Co pomaga Ci, gdy czujesz się przytłoczony/a?",
                "Czy są dźwięki lub tekstury, które szczególnie lubisz lub unikasz?",
                "Jak wygląda Twoja idealna rutyna dnia?",
                "Co sprawia, że czujesz się zrozumianym/ą?",
                "Jakie są Twoje specjalne zainteresowania, które Cię fascynują?",
                "Jak najlepiej mogę Cię wspierać w trudnych momentach?",
                "Co chciałbyś/chciałabyś, żeby ludzie wiedzieli o Twoim sposobie myślenia?",
                "Jakie są Twoje mocne strony wynikające z tego, jak funkcjonujesz?",
                "Co pomaga Ci w komunikacji, gdy jest trudno?",
                "Czy są sytuacje społeczne, które są dla Ciebie łatwiejsze niż inne?"
            )

            // Insert all questions
            categoryIds["Marzenia & Aspiracje"]?.id?.let { catId ->
                dreamsQuestions.forEach { text ->
                    repository.insertQuestion(catId, text, currentTime)
                }
            }

            categoryIds["Miłość & Relacje"]?.id?.let { catId ->
                loveQuestions.forEach { text ->
                    repository.insertQuestion(catId, text, currentTime)
                }
            }

            categoryIds["Śmieszne & Dziwne"]?.id?.let { catId ->
                funnyQuestions.forEach { text ->
                    repository.insertQuestion(catId, text, currentTime)
                }
            }

            categoryIds["Głębokie myśli"]?.id?.let { catId ->
                deepQuestions.forEach { text ->
                    repository.insertQuestion(catId, text, currentTime)
                }
            }

            categoryIds["Przygoda & Podróże"]?.id?.let { catId ->
                adventureQuestions.forEach { text ->
                    repository.insertQuestion(catId, text, currentTime)
                }
            }

            categoryIds["Flirt"]?.id?.let { catId ->
                flirtQuestions.forEach { text ->
                    repository.insertQuestion(catId, text, currentTime)
                }
            }

            categoryIds["Pikantne"]?.id?.let { catId ->
                spicyQuestions.forEach { text ->
                    repository.insertQuestion(catId, text, currentTime)
                }
            }

            categoryIds["Neuroatypowość"]?.id?.let { catId ->
                neuroQuestions.forEach { text ->
                    repository.insertQuestion(catId, text, currentTime)
                }
            }

            println("Database seeded successfully!")
            println("Total categories: ${repository.getAllCategories().size}")
            println("Total questions: ${repository.getAllQuestions().size}")

        } catch (e: Exception) {
            println("Error seeding database: ${e.message}")
            e.printStackTrace()
        }
    }
}
