package com.example.viewmodel

import android.app.Application
import android.speech.tts.TextToSpeech
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

enum class Tab {
    PRACTICE, // Educational Dashboard
    LESSONS,  // Live AI Speaking session chat workspace
    VOCAB,    // Vocabulary mastery and word association game
    PROFILE   // Original Landing/Promo page of Koreaku.AI
}

class KoreakuViewModel(application: Application) : AndroidViewModel(application), TextToSpeech.OnInitListener {

    private val db = KoreakuDatabase.getDatabase(application)
    private val geminiRepository = GeminiRepository()

    // --- Tab state ---
    private val _currentTab = MutableStateFlow(Tab.PRACTICE)
    val currentTab: StateFlow<Tab> = _currentTab.asStateFlow()

    fun switchTab(tab: Tab) {
        _currentTab.value = tab
    }

    // --- Persisted Database Flows ---
    val chatHistory: StateFlow<List<ChatMessage>> = db.chatDao().getChatHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val vocabList: StateFlow<List<VocabItem>> = db.vocabDao().getAllVocab()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val progress: StateFlow<UserProgress> = db.progressDao().getProgressFlow()
        .map { it ?: UserProgress() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserProgress())

    // --- UI Interactive Game States ---
    private val _selectedVocabIndex = MutableStateFlow(0)
    val selectedVocabIndex: StateFlow<Int> = _selectedVocabIndex.asStateFlow()

    private val _flashcardFlipped = MutableStateFlow(false)
    val flashcardFlipped: StateFlow<Boolean> = _flashcardFlipped.asStateFlow()

    // Match word game matching state lists
    val matchingKorean = listOf("사과", "물", "빵", "우유")
    val matchingIndonesian = listOf("Susu", "Air", "Apel", "Roti")

    private val _selectedKoreanWord = MutableStateFlow<String?>(null)
    val selectedKoreanWord: StateFlow<String?> = _selectedKoreanWord.asStateFlow()

    private val _selectedIndonesianWord = MutableStateFlow<String?>(null)
    val selectedIndonesianWord: StateFlow<String?> = _selectedIndonesianWord.asStateFlow()

    private val _matchingSuccessList = MutableStateFlow<Set<String>>(emptySet())
    val matchingSuccessList: StateFlow<Set<String>> = _matchingSuccessList.asStateFlow()

    private val _isAILoading = MutableStateFlow(false)
    val isAILoading: StateFlow<Boolean> = _isAILoading.asStateFlow()

    // --- Speech TTS System engine ---
    private var tts: TextToSpeech? = null
    private var isTtsInitialized = false

    init {
        // Initialize Android Local Text-To-Speech engine
        tts = TextToSpeech(application, this)

        viewModelScope.launch(Dispatchers.IO) {
            // Seed default vocab and challenge details on first setup
            if (db.vocabDao().getCount() == 0) {
                seedDatabase()
            }
            // Seed a initial chat prompt from Eonni AI if workspace conversation is empty
            db.chatDao().getChatHistory().first().let { history ->
                if (history.isEmpty()) {
                    db.chatDao().insertMessage(
                        ChatMessage(
                            sender = "tutor",
                            text = "안녕하세요! 카페에 오신 것을 환영합니다. 무엇을 주문하시겠어요?",
                            translation = "(Halo! Selamat datang di café. Anda bersedia memesan hidangan apa?)",
                            timestamp = System.currentTimeMillis()
                        )
                    )
                }
            }
            if (db.progressDao().getProgressDirect() == null) {
                db.progressDao().saveProgress(UserProgress())
            }
        }
    }

    private suspend fun seedDatabase() {
        val list = listOf(
            VocabItem(
                word = "사과",
                pronunciation = "Sa-gwa",
                translation = "Apel",
                sentenceKorean = "저는 매일 아침 사과를 먹어요.",
                sentenceIndonesian = "Saya memakan sebuah apel setiap pagi.",
                pronunciationGuide = "Huruf 'G' dalam kata 'Sa-gwa' diucapkan lebih lembut dibanding 'G' bahasa Inggris. Coba tempelkan bagian belakang lidah tipis di langit-langit mulut."
            ),
            VocabItem(
                word = "물",
                pronunciation = "Mul",
                translation = "Air",
                sentenceKorean = "물을 많이 마시는 것이 건강에 중요해요.",
                sentenceIndonesian = "Banyak minum air bersih merupakan kunci menjaga kesehatan jasmani.",
                pronunciationGuide = "Bunyi konsonan akhir 'L' (ㄹ 받침) dibaca dengan menyentuhkan ujung lidah ke langit-langit mulut depan secara lembut."
            ),
            VocabItem(
                word = "빵",
                pronunciation = "Ppang",
                translation = "Roti",
                sentenceKorean = "아침에 따뜻한 빵을 구워서 먹었습니다.",
                sentenceIndonesian = "Saya memanggang roti hangat di pagi hari lalu memakannya.",
                pronunciationGuide = "Konsonan ganda 'Pp' (ㅃ) merupakan konsonan tegang. Katupkan bibir secara rapat sebelum meluncurkan bunyi pelepasan tajam."
            ),
            VocabItem(
                word = "우유",
                pronunciation = "U-yu",
                translation = "Susu",
                sentenceKorean = "우유 한 잔을 마시니 기분이 상쾌해집니다.",
                sentenceIndonesian = "Meminum secangkir susu segar membuat mood saya segar gembira kembali.",
                pronunciationGuide = "Bunyi vokal 'U' diucapkan bulat dengan memajukan bibir ke depan agar vokal terartikulasi secara matang."
            ),
            VocabItem(
                word = "안녕하세요",
                pronunciation = "An-nyeong-ha-se-yo",
                translation = "Halo",
                sentenceKorean = "선생님, 안녕하세요!",
                sentenceIndonesian = "Halo bapak ibu guru yang saya hormati!",
                pronunciationGuide = "Ucapkan salam ini dengan membungkukkan badan sedikit sebagai tanda hormat sopan santun khas budaya Timur."
            )
        )
        db.vocabDao().insertAll(list)
    }

    // Switch flashcards index
    fun nextFlashcard(count: Int) {
        if (count > 0) {
            _selectedVocabIndex.value = (_selectedVocabIndex.value + 1) % count
            _flashcardFlipped.value = false
        }
    }

    fun prevFlashcard(count: Int) {
        if (count > 0) {
            _selectedVocabIndex.value = if (_selectedVocabIndex.value - 1 < 0) count - 1 else _selectedVocabIndex.value - 1
            _flashcardFlipped.value = false
        }
    }

    fun toggleFlashcardFlip() {
        _flashcardFlipped.value = !_flashcardFlipped.value
    }

    // Word matching logic
    fun selectKoreanWord(word: String) {
        if (_matchingSuccessList.value.contains(word)) return
        _selectedKoreanWord.value = word
        checkMatch()
    }

    fun selectIndonesianWord(word: String) {
        // Find matching Korean counterpart
        val correspondingKorean = when (word) {
            "Susu" -> "우유"
            "Air" -> "물"
            "Apel" -> "사과"
            "Roti" -> "빵"
            else -> ""
        }
        if (_matchingSuccessList.value.contains(correspondingKorean)) return
        _selectedIndonesianWord.value = word
        checkMatch()
    }

    private fun checkMatch() {
        val kr = _selectedKoreanWord.value
        val ind = _selectedIndonesianWord.value
        if (kr != null && ind != null) {
            val isCorrect = when (kr) {
                "우유" -> ind == "Susu"
                "물" -> ind == "Air"
                "사과" -> ind == "Apel"
                "빵" -> ind == "Roti"
                else -> false
            }
            if (isCorrect) {
                _matchingSuccessList.value = _matchingSuccessList.value + kr
                speakWord(kr)
            }
            _selectedKoreanWord.value = null
            _selectedIndonesianWord.value = null
        }
    }

    fun resetMatchingGame() {
        _matchingSuccessList.value = emptySet()
        _selectedKoreanWord.value = null
        _selectedIndonesianWord.value = null
    }

    // --- Chat logic with Gemini API ---
    fun sendMessageToTutor(userText: String, topic: String = "Ordering at a Café") {
        if (userText.isBlank()) return

        val userMessage = ChatMessage(sender = "user", text = userText)
        viewModelScope.launch {
            db.chatDao().insertMessage(userMessage)
            _isAILoading.value = true

            // Gather recent screen chat history for conversation flow
            val history = chatHistory.value
            val response = geminiRepository.consultTutor(history, topic)

            val tutorMessage = ChatMessage(
                sender = "tutor",
                text = response.replyKorean,
                translation = "(${response.replyIndonesian})",
                correction = if (response.hasCorrection) response.correctedPart else null,
                correctionLabel = if (response.hasCorrection) response.wrongPart else null,
                timestamp = System.currentTimeMillis()
            )
            db.chatDao().insertMessage(tutorMessage)
            _isAILoading.value = false

            // Voice out the reply
            speakWord(response.replyKorean)
        }
    }

    fun clearChatHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            db.chatDao().clearChat()
            db.chatDao().insertMessage(
                ChatMessage(
                    sender = "tutor",
                    text = "안녕하세요! 다시 대화를 시작해봅시다. 무엇을 공부하고 싶으세요?",
                    translation = "(Halo! Mari kita mulai percakapan kembali. Topik apa yang ingin Anda pelajari?)",
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }

    // --- Pronunciation card rating ---
    fun submitCardRating(vocabItem: VocabItem, rating: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val increment = if (rating == "known") 1 else 0
            val mastered = rating == "known"
            db.vocabDao().updateMastery(vocabItem.id, mastered, vocabItem.streakCount + increment)
        }
        _flashcardFlipped.value = false
    }

    // TextToSpeech initialization handler
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.KOREAN)
            if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                isTtsInitialized = true
            }
        }
    }

    fun speakWord(text: String) {
        if (isTtsInitialized && !text.isNullOrBlank()) {
            val speakText = text.replace(Regex("[()a-zA-Z]"), "") // clear brackets
            tts?.speak(speakText, TextToSpeech.QUEUE_FLUSH, null, "KoreakuTTS")
        }
    }

    override fun onCleared() {
        super.onCleared()
        tts?.stop()
        tts?.shutdown()
    }
}
