package com.example.data

import com.example.BuildConfig
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

// --- Gemini API Contracts ---

@JsonClass(generateAdapter = true)
data class GenerateContentRequest(
    val contents: List<Content>,
    val generationConfig: GenerationConfig? = null,
    val systemInstruction: Content? = null
)

@JsonClass(generateAdapter = true)
data class Content(
    val parts: List<Part>
)

@JsonClass(generateAdapter = true)
data class Part(
    val text: String? = null
)

@JsonClass(generateAdapter = true)
data class GenerationConfig(
    val responseMimeType: String? = null,
    val temperature: Float? = null,
    val maxOutputTokens: Int? = null
)

@JsonClass(generateAdapter = true)
data class GenerateContentResponse(
    val candidates: List<Candidate>?
)

@JsonClass(generateAdapter = true)
data class Candidate(
    val content: Content?
)

// --- Parsed Custom AI Tutor Speech Response ---

@JsonClass(generateAdapter = true)
data class TutorResponse(
    val replyKorean: String,
    val replyIndonesian: String,
    val hasCorrection: Boolean,
    val wrongPart: String? = null,
    val correctedPart: String? = null,
    val correctionExplanation: String? = null
)

interface GeminiApiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GenerateContentRequest
    ): GenerateContentResponse
}

class GeminiRepository {
    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://generativelanguage.googleapis.com/")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    private val apiService = retrofit.create(GeminiApiService::class.java)

    suspend fun consultTutor(conversationHistory: List<ChatMessage>, currentTopic: String): TutorResponse {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            // Graceful fallback for missing keys
            return TutorResponse(
                replyKorean = "죄송합니다, AI 기능은 API 키를 설정해야 작동합니다.",
                replyIndonesian = "(Maaf, fitur AI baru bekerja setelah Anda memasang API Key di Secrets Panel.)",
                hasCorrection = false
            )
        }

        // Build system prompt role instructions
        val systemPrompt = """
            Anda adalah Eonni AI, seorang tutor bahasa Korea interaktif yang membimbing pelajar Indonesia secara natural.
            Saat ini topiknya adalah: "$currentTopic".
            
            Lakukan percakapan roleplay yang interaktif. Anda harus membalas dalam bahasa Korea dasar beserta terjemahan bahasa Indonesianya.
            Koreksilah kesalahan tata bahasa, kosakata, atau kesopanan yang dilakukan oleh user jika ada.
            
            Anda WAJIB memberikan jawaban selalu berbentuk JSON yang valid dengan struktur key-value persis seperti skema berikut ini:
            {
              "replyKorean": "Pesan balasan Anda dalam bahasa Korea (misal: '네, 알겠습니다. 사이즈는 어떻게 해드릴까요?')",
              "replyIndonesian": "Terjemahan bahasa Indonesia dari replyKorean",
              "hasCorrection": true atau false (apakah chat terakhir user mengandung kesalahan bahasa Korea?)
              "wrongPart": "Frasa/kata bahasa Korea yang salah yang dimasukkan user (null bila hasCorrection adalah false)",
              "correctedPart": "Rekomendasi kata/frasa yang benar untuk menggantikan wrongPart (null bila hasCorrection adalah false)",
              "correctionExplanation": "Penjelasan koreksi dalam Bahasa Indonesia, jelaskan mengapa salah dan cara pakainya (null bila hasCorrection adalah false)"
            }
            Ensure no other characters or outer texts are outputted, only return valid raw JSON matching the schema of TutorResponse.
        """.trimIndent()

        // Map ChatMessage history to contents for Gemini context
        val contents = conversationHistory.map { msg ->
            val textContent = if (msg.sender == "user") {
                msg.text
            } else {
                msg.text + "\n" + (msg.translation ?: "")
            }
            Content(parts = listOf(Part(text = textContent)))
        }

        val request = GenerateContentRequest(
            contents = contents,
            generationConfig = GenerationConfig(
                responseMimeType = "application/json",
                temperature = 0.7f,
                maxOutputTokens = 800
            ),
            systemInstruction = Content(parts = listOf(Part(text = systemPrompt)))
        )

        return try {
            val response = apiService.generateContent(apiKey, request)
            val jsonText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            if (jsonText != null) {
                // Try to parse the received JSON
                moshi.adapter(TutorResponse::class.java).fromJson(jsonText) ?: fallbackTutorResponse()
            } else {
                fallbackTutorResponse("Maaf, format balasan AI kosong.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            fallbackTutorResponse("Koneksi Error: ${e.localizedMessage}")
        }
    }

    private fun fallbackTutorResponse(errorMessage: String = "Maaf, terjadi kesalahan koneksi."): TutorResponse {
        return TutorResponse(
            replyKorean = "네, 알겠습니다. 다시 말씀해주세요.",
            replyIndonesian = "($errorMessage)",
            hasCorrection = false
        )
    }
}
