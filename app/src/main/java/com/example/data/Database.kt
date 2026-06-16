package com.example.data

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sender: String, // "tutor" or "user"
    val text: String,
    val translation: String? = null,
    val correction: String? = null,
    val correctionLabel: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "vocab_items")
data class VocabItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val word: String,
    val pronunciation: String,
    val translation: String,
    val sentenceKorean: String,
    val sentenceIndonesian: String,
    val pronunciationGuide: String,
    val isMastered: Boolean = false,
    val streakCount: Int = 0,
    val audioRes: String? = null
)

@Entity(tableName = "user_progress")
data class UserProgress(
    @PrimaryKey val id: Int = 1,
    val currentDay: Int = 12,
    val streakDays: Int = 12,
    val completedDaysCsv: String = "1,2,3,4,5,6,7,8,9,10,11,12,13,14",
    val score: Int = 46 // representing 46% completion
)

@Dao
interface ChatDao {
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getChatHistory(): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessage)

    @Query("DELETE FROM chat_messages")
    suspend fun clearChat()
}

@Dao
interface VocabDao {
    @Query("SELECT * FROM vocab_items")
    fun getAllVocab(): Flow<List<VocabItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVocab(item: VocabItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<VocabItem>)

    @Query("UPDATE vocab_items SET isMastered = :isMastered, streakCount = :streak WHERE id = :id")
    suspend fun updateMastery(id: Int, isMastered: Boolean, streak: Int)

    @Query("SELECT COUNT(*) FROM vocab_items")
    suspend fun getCount(): Int
}

@Dao
interface ProgressDao {
    @Query("SELECT * FROM user_progress WHERE id = 1 LIMIT 1")
    fun getProgressFlow(): Flow<UserProgress?>

    @Query("SELECT * FROM user_progress WHERE id = 1 LIMIT 1")
    suspend fun getProgressDirect(): UserProgress?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProgress(progress: UserProgress)
}

@Database(entities = [ChatMessage::class, VocabItem::class, UserProgress::class], version = 1, exportSchema = false)
abstract class KoreakuDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
    abstract fun vocabDao(): VocabDao
    abstract fun progressDao(): ProgressDao

    companion object {
        @Volatile
        private var INSTANCE: KoreakuDatabase? = null

        fun getDatabase(context: Context): KoreakuDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KoreakuDatabase::class.java,
                    "koreaku_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
