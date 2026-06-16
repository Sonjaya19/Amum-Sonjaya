package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.PrimaryBlue
import com.example.viewmodel.KoreakuViewModel
import com.example.viewmodel.Tab

class MainActivity : ComponentActivity() {

  // Load Koreaku ViewModel with Android Context
  private val viewModel: KoreakuViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      MyApplicationTheme {
        // Safe drawing outer container
        Scaffold(
          modifier = Modifier
            .fillMaxSize()
            .testTag("main_scaffold"),
          bottomBar = {
            KoreakuBottomBar(viewModel)
          }
        ) { innerPadding ->
          MainContent(
            viewModel = viewModel,
            modifier = Modifier.padding(innerPadding)
          )
        }
      }
    }
  }
}

@Composable
fun MainContent(
  viewModel: KoreakuViewModel,
  modifier: Modifier = Modifier
) {
  val currentTab by viewModel.currentTab.collectAsState()
  val progress by viewModel.progress.collectAsState()
  val chatMessages by viewModel.chatHistory.collectAsState()
  val vocabList by viewModel.vocabList.collectAsState()
  val isAILoading by viewModel.isAILoading.collectAsState()

  val selectedVocabIdx by viewModel.selectedVocabIndex.collectAsState()
  val isFlashcardFlipped by viewModel.flashcardFlipped.collectAsState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    // Show top App bar section for active learning screens
    if (currentTab != Tab.PROFILE) {
      TopAppBarComponent(
        streakDays = progress.streakDays,
        onTabSwitch = { viewModel.switchTab(it) }
      )
    }

    // Switch screen layouts dynamically
    Box(modifier = Modifier.weight(1f)) {
      when (currentTab) {
        Tab.PRACTICE -> {
          DashboardScreen(
            progressDays = progress.score,
            completedDaysList = progress.completedDaysCsv.split(",")
              .mapNotNull { it.trim().toIntOrNull() },
            onTabSwitch = { viewModel.switchTab(it) },
            onTopicSelected = { ttsText ->
              // Pre-fill active tutor topic easily
            }
          )
        }
        Tab.LESSONS -> {
          LiveChatScreen(
            viewModel = viewModel,
            chatMessages = chatMessages,
            isLoading = isAILoading,
            onTopicSwitch = { }
          )
        }
        Tab.VOCAB -> {
          VocabScreen(
            viewModel = viewModel,
            vocabList = vocabList,
            isFlipped = isFlashcardFlipped,
            selectedIdx = selectedVocabIdx
          )
        }
        Tab.PROFILE -> {
          LandingScreen(
            onStartLearning = {
              viewModel.switchTab(Tab.PRACTICE)
            }
          )
        }
      }
    }
  }
}

@Composable
fun KoreakuBottomBar(viewModel: KoreakuViewModel) {
  val currentTab by viewModel.currentTab.collectAsState()

  NavigationBar(
    containerColor = MaterialTheme.colorScheme.surface,
    tonalElevation = 8.dp,
    modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
  ) {
    // 1. PRACTICE Tab
    NavigationBarItem(
      selected = currentTab == Tab.PRACTICE,
      onClick = { viewModel.switchTab(Tab.PRACTICE) },
      icon = {
        Icon(
          imageVector = if (currentTab == Tab.PRACTICE) Icons.Filled.Home else Icons.Outlined.Home,
          contentDescription = "Practice",
          tint = if (currentTab == Tab.PRACTICE) PrimaryBlue else MaterialTheme.colorScheme.onSurfaceVariant
        )
      },
      label = { Text("Practice") },
      colors = NavigationBarItemDefaults.colors(
        indicatorColor = PrimaryBlue.copy(alpha = 0.12f)
      ),
      modifier = Modifier.testTag("tab_practice")
    )

    // 2. LESSONS Tab
    NavigationBarItem(
      selected = currentTab == Tab.LESSONS,
      onClick = { viewModel.switchTab(Tab.LESSONS) },
      icon = {
        Icon(
          imageVector = if (currentTab == Tab.LESSONS) Icons.Filled.Star else Icons.Outlined.Star,
          contentDescription = "Lessons",
          tint = if (currentTab == Tab.LESSONS) PrimaryBlue else MaterialTheme.colorScheme.onSurfaceVariant
        )
      },
      label = { Text("Lessons") },
      colors = NavigationBarItemDefaults.colors(
        indicatorColor = PrimaryBlue.copy(alpha = 0.12f)
      ),
      modifier = Modifier.testTag("tab_lessons")
    )

    // 3. VOCAB Tab
    NavigationBarItem(
      selected = currentTab == Tab.VOCAB,
      onClick = { viewModel.switchTab(Tab.VOCAB) },
      icon = {
        Icon(
          imageVector = if (currentTab == Tab.VOCAB) Icons.Filled.List else Icons.Outlined.List,
          contentDescription = "Vocab",
          tint = if (currentTab == Tab.VOCAB) PrimaryBlue else MaterialTheme.colorScheme.onSurfaceVariant
        )
      },
      label = { Text("Vocab") },
      colors = NavigationBarItemDefaults.colors(
        indicatorColor = PrimaryBlue.copy(alpha = 0.12f)
      ),
      modifier = Modifier.testTag("tab_vocab")
    )

    // 4. PROFILE Tab (Original Landing Promo Hero Page)
    NavigationBarItem(
      selected = currentTab == Tab.PROFILE,
      onClick = { viewModel.switchTab(Tab.PROFILE) },
      icon = {
        Icon(
          imageVector = if (currentTab == Tab.PROFILE) Icons.Filled.Person else Icons.Outlined.Person,
          contentDescription = "Profile",
          tint = if (currentTab == Tab.PROFILE) PrimaryBlue else MaterialTheme.colorScheme.onSurfaceVariant
        )
      },
      label = { Text("Profile") },
      colors = NavigationBarItemDefaults.colors(
        indicatorColor = PrimaryBlue.copy(alpha = 0.12f)
      ),
      modifier = Modifier.testTag("tab_profile")
    )
  }
}
