package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.data.ChatMessage
import com.example.data.VocabItem
import com.example.ui.theme.*
import com.example.viewmodel.KoreakuViewModel
import com.example.viewmodel.Tab
import kotlinx.coroutines.delay

// --- Image URL Resources from HTML Specification ---
const val URL_EONNI_MAIN = "https://lh3.googleusercontent.com/aida-public/AB6AXuBn59HU0EPAFvOWM44hrJCqDhSNUMBfQviAD0zK1FjDf7hMC-ADf_LYTzMKl4FwXRM7WGVcHVyATnQql_DM9TNnkJ6BHs8H3iXxtjx-Y5mkbriWC2OGO4H2MWAusQForVfwiTVF6yutHsqNmZHIFkCxClcXoA0kE_oGxwQpGv89nV46A3QQDJYRHafJnh-bBOztVoS8sK7PjukCo2VR5waI-xImCh_cJNkDTrZuAyZia0In6YU3hKG7p8iJoce6DzU_PdwwRiXcR3E"
const val URL_EONNI_AVATAR = "https://lh3.googleusercontent.com/aida-public/AB6AXuAXnkQDSVxN0E-ExMnzNY_s1uMqDn5qcQd8wMNd_313hbO1n0htY2xyoERrl746U7XNxCeUn3znnQQOnk0FNproTUxVm4eiVYSAEbgXsV5HUIWLsYiHfiFKdVl-n17PQIvTUu0O8A9OULHf4AbCn7FW_FaGYIb1O2m6Y-BwZrrWQGReGbo9namTs8f7CziqQt7CMJgTzqpM3k9Ch5OGiAEosfnygQzqLYf2LVVdDB3ahUKdNrYIHjGaPNsOKinxoJNX2Bmuu7H5VXg"
const val URL_JIWOO_AVATAR = "https://lh3.googleusercontent.com/aida-public/AB6AXuAfa68DnVfxLeZlDYIV_0x_1yO_bNKlvk0hoMtTVganRBLFb0_yu4Tw5ty4ZJnp7iUYoWH40k4QdTPZxtVhluvBz_ElReHb46nEmEBcB5vhNS9EZ2F2pvM4_5mGGsKukvcrV98CnKdb8LPr_7gIRICIE3kTbFtxFHuQizDsD1beI-lGwDeZXV3d4s7T9h5FgW36tPWsKmNvE42GKF4uaf2Qyn7-0BncxyuwJsaE-dcgYh4lk07cc40-iD9sziMy31gPyYBDkoo85as"
const val URL_STUDENT_DEO = "https://lh3.googleusercontent.com/aida-public/AB6AXuDEO_WxmQeqwBKUEL2NZ_DfKk-YSpXb2c5RXBL3-M6MWOQxsT80ErOW_Amrd-ySTdUxLg0avYMCqD8IbZYk5HP-XCmdTUEMK5YSU5wUqwzYJ2o732_SQqVojYE_k9DlnNBjympeNqNNfgJOkR8pMUdr74XEiPRbRmPEWslVgaFQMnZEEV0GtSeWAjgzyChuD9kQqKmLWbLWK2OhPuDKn6MleVK7Xl6t_H8mP8dV1NUII-xGs2oIvRw2sKToOwzFUYUhdtV6Zh5A8Fk"
const val URL_STUDENT_ABB = "https://lh3.googleusercontent.com/aida-public/AB6AXuAbbBlLb1m6hTXAvZ6ohM_j36itzFcf9anzfb4PDOA79kABiqGiRxokD454peCb81Yh6YWg6NTDn5melqGMz2HUBKSyUeUAfr_uGROvNqZxvn7WW1OBqQVdGVDDwJuYx0kXY1KK9VO39A-1Eh2c3IfoLeIjsJZg_dRGgEWHVJgdmyr8ztxsqSdGKGbJ5aPBVO07Z8RhQaIEwShtc1Yo2BezfjwIimNmNaJD53dvd-RB6abAqDPMwT-jO9O961KOwzAi8UQsZGG_4yw"
const val URL_STUDENT_FE = "https://lh3.googleusercontent.com/aida-public/AB6AXuAjmFe_klgleYMyES5o9L8I-4_n4dKoV5kaQ9ps-DO_sWYLwlq4t-tMKSEF3W_1vJZNPMW9Kb3SZmJvJTdkw4oyI9cNl88izUvof1HwDlBYhKkDMGZRfYwPJ9m7VPHkyuc_kGHSE7qIG1GIjV0dwDE_wjTizaI-qVgkdGDRgKEtQULzfx1M1dY3XM0GhNHeHSjKSzfdHnXicittkaQeMBhmuT6KPW_b0VR0cOUxxrylZ0kKuZ155HaoIxHwU3-pJJOpztKd-Q7FYLg"
const val URL_STUDENT_MALE = "https://lh3.googleusercontent.com/aida-public/AB6AXuD8JWpgY-FX810ZrzDSkiEP8UmUPzeXzznPFf0qYidOsW27Ygn0MjEBUGfo_ooNgFh626hgPuwEF1ZJrS_glKT6ZvwEyp4Z27_Rv7cWRU98cikDdS8rfMzo7UHzWR0e-pBxkpe3nVzBQNrEiBjY75HDn468JLk2GmtOj0ZI-czYuR1oettaID9tG_EqtC0gK80IUrDCSy7ngTnMh_YMAtQvzp-ow4XiEIautuOAaGFks84YwzO9pPiD5LiRZNn2hTWyRFROn0QIMek"
const val URL_STUDENT_DESK = "https://lh3.googleusercontent.com/aida-public/AB6AXuC7EJL4390o3QzoYpbi6L0iUIp_8PUv2fPUdQu-8W4tB00fiomENMrQHgizQqj6UPHgcEJGX6RqFnaF8WwsqseGKQLxtfzEcfIERe5MiAIIvGk_SOIJCtrEfsOCgQObvvzsmi3OQ_uc_PzJd_40XxkolcFyYggniHskyC2E7-T7ofJXIJ5N5bJ2QI6MkmfRiW5Eyc8mlOAc0Xh9zQr42gemGYmuoNuxIfLgMitOXSSdQHxD_8DW3PoAgYW1kLUqW8uY_qE8MAuvh6w"

// Shared Top App Bar Component
@Composable
fun TopAppBarComponent(streakDays: Int, onTabSwitch: (Tab) -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding(),
        shape = RoundedCornerShape(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onTabSwitch(Tab.PROFILE) }
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(PrimaryBlue.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = URL_STUDENT_MALE,
                        contentDescription = "Profil Siswa",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Koreaku.AI",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PrimaryBlue,
                    fontFamily = FontFamily.SansSerif
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(PrimaryBlue.copy(alpha = 0.08f))
                    .clickable { onTabSwitch(Tab.PRACTICE) }
                    .padding(horizontal = 12.dp, vertical = 5.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Streak Belajar",
                    tint = CoralVibrant,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "$streakDays Hari",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue
                )
            }
        }
    }
}

// ==========================================
// 1. SCREEN: Profile & Promo Landing Screen
// ==========================================
@Composable
fun LandingScreen(onStartLearning: () -> Unit) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceBg)
            .verticalScroll(scrollState)
            .testTag("landing_screen")
    ) {
        // Hero Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    // Badge Announcement
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(PrimaryBlue.copy(alpha = 0.1f))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "AI",
                            tint = PrimaryBlue,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "AI Tutor Pribadi 24/7",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlue
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Kuasai Bahasa Korea",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 36.sp,
                        color = InkBlack
                    )
                    Text(
                        text = "Tanpa Harus",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 36.sp,
                        color = CoralVibrant
                    )
                    Text(
                        text = "Masuk Kelas Kursus",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 36.sp,
                        color = InkBlack
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Eonni & Oppa AI siap membantumu lancar bercakap Hangul kapan saja, di mana saja. Belajar efektif dengan metode AI modern yang disesuaikan untuk pelajar Indonesia.",
                        fontSize = 14.sp,
                        color = OnSurfaceVariantText,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = onStartLearning,
                        colors = ButtonDefaults.buttonColors(containerColor = CoralVibrant),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag("discount_button"),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "Mulai Belajar Sekarang",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Rp 250.000",
                            style = LocalTextStyle.current.copy(textDecoration = TextDecoration.LineThrough),
                            color = OnSurfaceVariantText.copy(alpha = 0.5f),
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Cuma Rp 99.000",
                            color = PrimaryBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Social Proof avatars pile
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy((-10).dp)) {
                            listOf(URL_STUDENT_FE, URL_STUDENT_ABB, URL_STUDENT_DEO).forEach { url ->
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .border(2.dp, Color.White, CircleShape)
                                ) {
                                    AsyncImage(
                                        model = url,
                                        contentDescription = "Student Pile",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "1,200+ Siswa telah bergabung",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceVariantText
                        )
                    }
                }
            }
        }

        // Live Mentor Avatar section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Tutor AI Terinteraktif Anda",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = InkBlack,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .clip(RoundedCornerShape(24.dp))
            ) {
                AsyncImage(
                    model = URL_EONNI_MAIN,
                    contentDescription = "Eonni AI Tutor",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Bottom gradient scrim
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.82f))
                            )
                        )
                )

                // Conversation Box Tooltip Overlay Left
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(14.dp)
                        .width(180.dp)
                        .align(Alignment.TopStart)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "Eonni AI",
                            color = PrimaryBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Coba ucapkan:\n\"Gamsahamnida\"",
                            color = InkBlack,
                            fontSize = 12.sp,
                            lineHeight = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Pelafalan Success Badge right
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.92f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(14.dp)
                        .align(Alignment.CenterEnd)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Pelafalan OK",
                            tint = SuccessGreen,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Column {
                            Text(text = "Rasio 98%", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = InkBlack)
                            Text(text = "Sempurna!", fontSize = 10.sp, fontStyle = FontStyle.Italic, color = OnSurfaceVariantText)
                        }
                    }
                }

                // Greeting at the bottom of portrait
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "안녕하세요! 👋",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = "\"Mari belajar bicara Korea dengan asyik bersama saya!\"",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }

        // Bento Grid Card Details
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Mengapa Memilih Kami?",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = InkBlack,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            // Features Bento
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(PrimaryBlue.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.PlayArrow, contentDescription = "Play", tint = PrimaryBlue)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(text = "Speaking Practice Real-time", fontWeight = FontWeight.Bold, color = InkBlack, fontSize = 14.sp)
                        Text(text = "Koreksi pelafalan secara langsung seperti guru native.", color = OnSurfaceVariantText, fontSize = 12.sp)
                    }
                }
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(CoralVibrant.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.List, contentDescription = "Hangul", tint = CoralVibrant)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(text = "Master Hangul Cepat", fontWeight = FontWeight.Bold, color = InkBlack, fontSize = 14.sp)
                        Text(text = "Sistem flashcard kosa-kata interaktif dalam hitungan hari.", color = OnSurfaceVariantText, fontSize = 12.sp)
                    }
                }
            }
        }

        // Program and course pathway footnotes
        Card(
            colors = CardDefaults.cardColors(containerColor = InkBlack),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Koreaku.AI",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Kami membantu ribuan orang Indonesia mewujudkan mimpi fasih bahasa Korea melalui teknologi AI tercanggih dan kurikulum yang menyenangkan.",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(text = "• Kelas Hangul", fontSize = 11.sp, color = Color.White.copy(alpha = 0.6f))
                    Text(text = "• Daily Talk", fontSize = 11.sp, color = Color.White.copy(alpha = 0.6f))
                    Text(text = "• TOPIK Prep", fontSize = 11.sp, color = Color.White.copy(alpha = 0.6f))
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "© 2026 Koreaku.AI. Terpercaya seluruh Indonesia.",
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.4f)
                )
                Spacer(modifier = Modifier.height(48.dp)) // padding for bottom navigation
            }
        }
    }
}

// ==========================================
// 2. SCREEN: Study Practice Dashboard Screen
// ==========================================
@Composable
fun DashboardScreen(
    progressDays: Int,
    completedDaysList: List<Int>,
    onTabSwitch: (Tab) -> Unit,
    onTopicSelected: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceBg)
            .verticalScroll(scrollState)
            .testTag("dashboard_screen")
    ) {
        // Today's Mission Bento (Blue Gradient)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = PrimaryBlue),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "MISI HARI INI",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White.copy(alpha = 0.15f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "Estimasi", fontSize = 10.sp, color = Color.White.copy(alpha = 0.7f))
                            Text(text = "15 Menit", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Mastering Subject Markers\n(-이 / -가)",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Pelajari bagaimana mengaplikasikan penanda subjek layaknya native speaker melalui latihan simulasi interaktif.",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.75f),
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                onTopicSelected("Subject Markers (-이 / -가)")
                                onTabSwitch(Tab.LESSONS)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CoralVibrant),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.PlayArrow, contentDescription = "Play", modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Mulai Misi", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // Avatar Stack + Counter
                        Row(horizontalArrangement = Arrangement.spacedBy((-6).dp)) {
                            listOf(URL_STUDENT_ABB, URL_STUDENT_DEO).forEach { url ->
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .border(1.dp, Color.White, CircleShape)
                                ) {
                                    AsyncImage(
                                        model = url,
                                        contentDescription = "User",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "+12 sedang belajar",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        // 30 Days Challenge calendar Tracker card
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tantangan 30 Hari",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = InkBlack
                    )
                    Text(
                        text = "Selesai: ${completedDaysList.size}/30 Hari",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Custom 3x7 Grid of Days (Index 1 to 21 shown dynamically)
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    for (row in 0 until 3) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            for (col in 1..7) {
                                val dayNum = row * 7 + col
                                val isDone = completedDaysList.contains(dayNum)
                                val isActive = dayNum == 15

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            when {
                                                isDone -> PrimaryBlue
                                                isActive -> Color.White
                                                else -> SoftSky
                                            }
                                        )
                                        .border(
                                            width = if (isActive) 1.5.dp else 0.dp,
                                            color = if (isActive) CoralVibrant else Color.Transparent,
                                            shape = RoundedCornerShape(8.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isDone) {
                                        Icon(
                                            imageVector = Icons.Filled.Check,
                                            contentDescription = "Day Done",
                                            tint = Color.White,
                                            modifier = Modifier.size(12.dp)
                                        )
                                    } else {
                                        Text(
                                            text = "$dayNum",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isActive) CoralVibrant else OnSurfaceVariantText
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Progress Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Rasio Progres", fontSize = 11.sp, color = OnSurfaceVariantText)
                    Text(text = "46%", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                }
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { 0.46f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = PrimaryBlue,
                    trackColor = SoftSky
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Hebat! Anda tinggal sedikit lagi menuju Lencana Linguis Korea.",
                    fontSize = 11.sp,
                    color = OnSurfaceVariantText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Learning Paths Bento Column
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Modul Belajar Anda",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = InkBlack,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Dynamic Path rows
            pathwayCard(
                title = "Speaking (Percakapan)",
                desc = "Latihan artikulasi fonetis dan pelafalan langsung berbekal asisten robot AI.",
                stat = "Level 3",
                completedProgress = 3,
                icon = Icons.Filled.Person,
                containerColor = Color.White,
                iconTintColor = PrimaryBlue,
                onCardClick = { onTabSwitch(Tab.LESSONS) }
            )

            pathwayCard(
                title = "Vocabulary (Bank Kosakata)",
                desc = "Kuasai 1.000+ kosa-kata ujian TOPIK berbekal kartu hafalan pengulangan berspasi.",
                stat = "Level 8",
                completedProgress = 4,
                icon = Icons.Filled.List,
                containerColor = Color.White,
                iconTintColor = Color(0xFFF59E0B),
                onCardClick = { onTabSwitch(Tab.VOCAB) }
            )

            pathwayCard(
                title = "Listening (Pendengaran)",
                desc = "Pahami dialog-dialog native berkecepatan normal beserta klip audio drama.",
                stat = "Level 2",
                completedProgress = 2,
                icon = Icons.Filled.PlayArrow,
                containerColor = Color.White,
                iconTintColor = Color(0xFF8B5CF6),
                onCardClick = { }
            )

            // Featured Conversation Card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.15f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable {
                        onTopicSelected("Ordering at a Café")
                        onTabSwitch(Tab.LESSONS)
                    }
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(CoralVibrant.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.Star, contentDescription = "Forum", tint = CoralVibrant)
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "Percakapan Sosial", fontWeight = FontWeight.Bold, color = InkBlack, fontSize = 15.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(CoralVibrant)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(text = "Rekomendasi", fontSize = 8.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Bercakap-cakap langsung dengan Eonni AI dalam skenario dunia nyata (Cafe, Mall, Bandara, Kelas).",
                                fontSize = 12.sp,
                                color = OnSurfaceVariantText,
                                lineHeight = 16.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = "Go",
                            tint = PrimaryBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // Floating Tutor prompt overlay simulation at bottom of scroll
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = SoftSky),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.2f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    ) {
                        AsyncImage(
                            model = URL_JIWOO_AVATAR,
                            contentDescription = "Jiwo",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = "Ji-woo (Tutor AI)", fontWeight = FontWeight.Bold, color = PrimaryBlue, fontSize = 12.sp)
                        Text(
                            text = "\"Siap melakukan latihan interaktif tentang penanda subjek (subject markers) hari ini?\"",
                            fontSize = 13.sp,
                            color = InkBlack,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = {
                                    onTopicSelected("Subject Markers (-이 / -가)")
                                    onTabSwitch(Tab.LESSONS)
                                },
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text(text = "Ayo belajar!", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            OutlinedButton(
                                onClick = { },
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                border = BorderStroke(1.dp, OnSurfaceVariantText.copy(alpha = 0.4f)),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text(text = "Nanti dulu", fontSize = 11.sp, color = OnSurfaceVariantText)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(64.dp))
    }
}

@Composable
fun pathwayCard(
    title: String,
    desc: String,
    stat: String,
    completedProgress: Int,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    containerColor: Color,
    iconTintColor: Color,
    onCardClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onCardClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconTintColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = title, tint = iconTintColor, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Bold, color = InkBlack, fontSize = 14.sp)
                Text(text = desc, color = OnSurfaceVariantText, fontSize = 11.sp, lineHeight = 15.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = stat, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = iconTintColor)
                    Spacer(modifier = Modifier.width(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        for (dot in 1..4) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (dot <= completedProgress) iconTintColor else iconTintColor.copy(
                                            alpha = 0.2f
                                        )
                                    )
                            )
                        }
                    }
                }
            }
            Icon(
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = null,
                tint = OnSurfaceVariantText.copy(alpha = 0.6f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// ==========================================
// 3. SCREEN: Live Speaking Chat Lesson Screen
// ==========================================
@Composable
fun LiveChatScreen(
    viewModel: KoreakuViewModel,
    chatMessages: List<ChatMessage>,
    isLoading: Boolean,
    onTopicSwitch: (String) -> Unit
) {
    val listState = rememberLazyListState()
    var currentTopicInput by remember { mutableStateOf("Ordering at a Café") }
    var userTextInputDialog by remember { mutableStateOf("") }
    var showTypeDialog by remember { mutableStateOf(false) }

    // Auto scroll chat to the bottom on new message insertion
    LaunchedEffect(chatMessages.size, isLoading) {
        if (chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(chatMessages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceBg)
            .testTag("chat_screen")
    ) {
        // Chat Section Header Toolbar
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(0.dp),
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Color.Red)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "SESI LIVE SPEECH",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = InkBlack
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(SuccessGreen)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "ONLINE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = SuccessGreen
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Café Topic Info card
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(PrimaryBlue.copy(alpha = 0.08f))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                    ) {
                        AsyncImage(
                            model = URL_EONNI_AVATAR,
                            contentDescription = "Eonni",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Topik Belajar Hari Ini",
                            fontSize = 10.sp,
                            color = PrimaryBlue,
                            fontWeight = FontWeight.Bold
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = currentTopicInput,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = InkBlack
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "Edit Topik",
                                tint = PrimaryBlue,
                                modifier = Modifier
                                    .size(14.dp)
                                    .clickable {
                                        // Simple cycle of educational learning conversational modules
                                        val next = when (currentTopicInput) {
                                            "Ordering at a Café" -> "Subject Markers (-이 / -가)"
                                            "Subject Markers (-이 / -가)" -> "Greeting Friends"
                                            "Greeting Friends" -> "Buying Goods at Mall"
                                            else -> "Ordering at a Café"
                                        }
                                        currentTopicInput = next
                                        onTopicSwitch(next)
                                    }
                            )
                        }
                    }
                }
            }
        }

        // Live Chat Feed canvas area
        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp),
                contentPadding = PaddingValues(top = 10.dp, bottom = 20.dp)
            ) {
                items(chatMessages) { message ->
                    if (message.sender == "tutor") {
                        TutorBubbleLayout(message) {
                            viewModel.speakWord(message.text)
                        }
                    } else {
                        UserBubbleLayout(message)
                    }
                }

                if (isLoading) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = SoftSky),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "Eonni sedang memikirkan respon...",
                                    fontSize = 11.sp,
                                    fontStyle = FontStyle.Italic,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    color = PrimaryBlue
                                )
                            }
                        }
                    }
                }
            }
        }

        // Voice Controls Footer Panel
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Topic response fast chips suggestions
                Text(
                    text = "Ketuk Kalimat Alternatif Kami:",
                    fontSize = 11.sp,
                    color = OnSurfaceVariantText,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(4.dp))

                val chipsList = when (currentTopicInput) {
                    "Ordering at a Café" -> listOf(
                        "라지 사이즈로 주세요 (Give me a Large size)",
                        "아이스 아메리카노 하나 주세요 (Iced Americano, please)"
                    )
                    "Subject Markers (-이 / -가)" -> listOf(
                        "사과가 맛있습니다 (Apple is delicious)",
                        "물이 좋습니다 (Water is good)"
                    )
                    else -> listOf(
                        "안녕하세요! 반가워요 (Hello, glad to meet you)",
                        "감사합니다 (Thank you very much)"
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    chipsList.forEach { rawText ->
                        val parts = rawText.split(" (")
                        val speakKorean = parts[0]
                        val speakLabel = parts.getOrNull(1)?.removeSuffix(")") ?: ""

                        AssistChip(
                            onClick = {
                                viewModel.sendMessageToTutor(speakKorean, currentTopicInput)
                            },
                            label = {
                                Column {
                                    Text(text = speakKorean, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                                    if (speakLabel.isNotEmpty()) {
                                        Text(text = speakLabel, fontSize = 9.sp, color = OnSurfaceVariantText)
                                    }
                                }
                            },
                            colors = AssistChipDefaults.assistChipColors(containerColor = SoftSky)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Microphone record trigger row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    // Type Keyboard Fallback
                    IconButton(
                        onClick = { showTypeDialog = true },
                        modifier = Modifier
                            .size(46.dp)
                            .border(1.dp, DividerColor, CircleShape)
                    ) {
                        Icon(imageVector = Icons.Filled.Edit, contentDescription = "Tulis Teks", tint = PrimaryBlue)
                    }

                    Spacer(modifier = Modifier.width(28.dp))

                    // Pulse outer ring Mic Button
                    Box(
                        modifier = Modifier.size(76.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(PrimaryBlue.copy(alpha = 0.15f))
                        )
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(PrimaryBlue)
                                .clickable {
                                    // Simulated high-fidelity speech dictation trigger helper
                                    val randomPhrases = when (currentTopicInput) {
                                        "Ordering at a Café" -> listOf(
                                            "아이스 아메리카노 하나 있어요.", // error-prone phrase
                                            "카페라떼 두 잔 주세요.",
                                            "따뜻한 우유 있어요." // error-prone phrase
                                        )
                                        else -> listOf(
                                            "감사합니다.",
                                            "한국어가 어렵지만 재미있어요."
                                        )
                                    }
                                    val randomUserSend = randomPhrases.random()
                                    viewModel.sendMessageToTutor(randomUserSend, currentTopicInput)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PlayArrow,
                                contentDescription = "Bicara",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(28.dp))

                    // Clear Chat Logs button
                    IconButton(
                        onClick = { viewModel.clearChatHistory() },
                        modifier = Modifier
                            .size(46.dp)
                            .border(1.dp, DividerColor, CircleShape)
                    ) {
                        Icon(imageVector = Icons.Filled.Refresh, contentDescription = "Reset Chat", tint = CoralVibrant)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Breathing dynamic wave visualization
                Row(
                    modifier = Modifier.height(24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val transition = rememberInfiniteTransition()
                    for (barIndex in 1..5) {
                        val duration = 800 + (barIndex * 150)
                        val barHeight by transition.animateFloat(
                            initialValue = 4f,
                            targetValue = 20f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(duration, easing = LinearEasing),
                                repeatMode = RepeatMode.Reverse
                            )
                        )
                        Box(
                            modifier = Modifier
                                .width(4.dp)
                                .height(barHeight.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(if (isLoading) CoralVibrant else PrimaryBlue)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(48.dp)) // padding for navigation
            }
        }
    }

    // Modal dialog to type custom Korean phrases
    if (showTypeDialog) {
        AlertDialog(
            onDismissRequest = { showTypeDialog = false },
            title = { Text(text = "Ketik Teks Bahasa Korea", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
            text = {
                Column {
                    Text(
                        text = "Masukkan teks atau kalimat Anda dalam Bahasa Korea. Eonni AI siap meralat tata bahasa yang kurang pas.",
                        fontSize = 11.sp,
                        color = OnSurfaceVariantText,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = userTextInputDialog,
                        onValueChange = { userTextInputDialog = it },
                        label = { Text("Kalimat Bahasa Korea") },
                        placeholder = { Text("Contoh: 사과 가 먹어요 (Subject marker salah)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (userTextInputDialog.isNotBlank()) {
                            viewModel.sendMessageToTutor(userTextInputDialog, currentTopicInput)
                            userTextInputDialog = ""
                        }
                        showTypeDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Text("Kirim")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTypeDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}

@Composable
fun TutorBubbleLayout(message: ChatMessage, onSpeakClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(PrimaryBlue)
                .clickable { onSpeakClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Speak",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(0.85f)) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(topStart = 0.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = message.text,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = InkBlack,
                        fontFamily = FontFamily.SansSerif
                    )
                    if (message.translation != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = message.translation,
                            fontSize = 12.sp,
                            color = OnSurfaceVariantText,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UserBubbleLayout(message: ChatMessage) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.End
    ) {
        Box(modifier = Modifier.fillMaxWidth(0.82f), contentAlignment = Alignment.TopEnd) {
            Card(
                colors = CardDefaults.cardColors(containerColor = PrimaryBlue),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 0.dp, bottomEnd = 16.dp, bottomStart = 16.dp)
            ) {
                Text(
                    text = message.text,
                    fontSize = 15.sp,
                    color = Color.White,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        // Real-time grammar correction notification card below
        if (message.correction != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = SoftPink),
                border = BorderStroke(1.dp, CoralVibrant.copy(alpha = 0.4f)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(0.82f)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Correction",
                            tint = DarkAccent,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Rekomendasi Koreksi Tutor",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkAccent
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Seharusnya ditulis: ${message.correction}",
                        fontSize = 13.sp,
                        color = InkBlack,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (message.correctionLabel != null) {
                        Text(
                            text = "Hindari kata atau pola pengikat: \"${message.correctionLabel}\" untuk kelancaran bercakap Anda.",
                            fontSize = 11.sp,
                            color = OnSurfaceVariantText,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// 4. SCREEN: Vocabulary Mastery Flashcards
// ==========================================
@Composable
fun VocabScreen(
    viewModel: KoreakuViewModel,
    vocabList: List<VocabItem>,
    isFlipped: Boolean,
    selectedIdx: Int
) {
    var modeFlashcard by remember { mutableStateOf(true) }
    val matchSuccessList by viewModel.matchingSuccessList.collectAsStateWithLifecycle()
    val krSel by viewModel.selectedKoreanWord.collectAsStateWithLifecycle()
    val indSel by viewModel.selectedIndonesianWord.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceBg)
            .testTag("vocab_screen")
    ) {
        // Mode Selector Tab Row layout
        TabRow(
            selectedTabIndex = if (modeFlashcard) 0 else 1,
            containerColor = Color.White,
            contentColor = PrimaryBlue
        ) {
            Tab(
                selected = modeFlashcard,
                onClick = { modeFlashcard = true },
                text = { Text("Kartu Hafalan (Flashcards)", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = !modeFlashcard,
                onClick = { modeFlashcard = false },
                text = { Text("Cari Pasangan (Match)", fontWeight = FontWeight.Bold) }
            )
        }

        if (modeFlashcard) {
            if (vocabList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PrimaryBlue)
                }
            } else {
                val currentItem = vocabList.getOrElse(selectedIdx) { vocabList[0] }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(top = 16.dp, bottom = 48.dp)
                ) {
                    item {
                        // Day challenge label Info
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(PrimaryBlue.copy(alpha = 0.08f))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "",
                                tint = PrimaryBlue,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "HAFALAN HARI KE-12",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryBlue
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Penguasaan Kosa-Kata Dasar",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = InkBlack
                        )
                        Text(
                            text = "Hafalkan kata-kata di bawah ini berbekal suara dan tips native tutor.",
                            fontSize = 12.sp,
                            color = OnSurfaceVariantText,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Flip Card layout
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .shadow(2.dp, RoundedCornerShape(24.dp))
                                .clip(RoundedCornerShape(24.dp))
                                .background(if (isFlipped) PrimaryBlue else Color.White)
                                .clickable { viewModel.toggleFlashcardFlip() }
                                .padding(20.dp)
                        ) {
                            // Top controls volume speaker
                            IconButton(
                                onClick = {
                                    // Stop clicking events propagation to block card flipping
                                    viewModel.speakWord(currentItem.word)
                                },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(if (isFlipped) Color.White.copy(alpha = 0.15f) else SoftSky)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.PlayArrow,
                                    contentDescription = "Speak",
                                    tint = if (isFlipped) Color.White else PrimaryBlue,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            if (!isFlipped) {
                                // Front Card layout (Korean Word)
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = currentItem.word,
                                        fontSize = 46.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryBlue,
                                        fontFamily = FontFamily.SansSerif
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = currentItem.pronunciation.uppercase(),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = OnSurfaceVariantText,
                                        letterSpacing = 1.5.sp
                                    )
                                    Spacer(modifier = Modifier.height(20.dp))
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Refresh,
                                            contentDescription = null,
                                            tint = OnSurfaceVariantText.copy(alpha = 0.5f),
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(text = "Ketuk kartu untuk jawaban", fontSize = 11.sp, color = OnSurfaceVariantText.copy(alpha = 0.6f))
                                    }
                                }
                            } else {
                                // Back Card layout (Translation & Sample sentence)
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = currentItem.translation,
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = currentItem.sentenceKorean,
                                        fontSize = 12.sp,
                                        color = Color.White.copy(alpha = 0.9f),
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        text = "\"${currentItem.sentenceIndonesian}\"",
                                        fontSize = 11.sp,
                                        color = Color.White.copy(alpha = 0.7f),
                                        textAlign = TextAlign.Center,
                                        fontStyle = FontStyle.Italic
                                    )
                                }
                            }
                        }

                        // Navigation arrows and counters
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { viewModel.prevFlashcard(vocabList.size) },
                                modifier = Modifier
                                    .size(44.dp)
                                    .border(1.dp, DividerColor, CircleShape)
                            ) {
                                Icon(Icons.Filled.ArrowBack, contentDescription = "Prev", tint = PrimaryBlue)
                            }

                            Text(
                                text = "${selectedIdx + 1} / ${vocabList.size}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = InkBlack
                            )

                            IconButton(
                                onClick = { viewModel.nextFlashcard(vocabList.size) },
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(PrimaryBlue, CircleShape)
                            ) {
                                Icon(Icons.Filled.ArrowForward, contentDescription = "Next", tint = Color.White)
                            }
                        }

                        // Knowing controls evaluation status
                        if (isFlipped) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = { viewModel.submitCardRating(currentItem, "known") },
                                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("Saya Sudah Tahu", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                                Button(
                                    onClick = { viewModel.submitCardRating(currentItem, "need_practice") },
                                    colors = ButtonDefaults.buttonColors(containerColor = CoralVibrant),
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("Perlu Latihan", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Tutor Guide Side Note
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.08f)),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(CoralVibrant.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Filled.Person, contentDescription = "", tint = CoralVibrant, modifier = Modifier.size(20.dp))
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(text = "Rekomendasi Tutor AI", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = currentItem.pronunciationGuide,
                                        fontSize = 12.sp,
                                        color = InkBlack,
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Upcoming Words 목록
                        Text(
                            text = "Kosa-Kata Lainnya",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = InkBlack,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        vocabList.forEachIndexed { idx, it ->
                            if (idx != selectedIdx) {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clickable {
                                            viewModel.speakWord(it.word)
                                        },
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = it.word, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(text = "${it.pronunciation} (${it.translation})", fontSize = 11.sp, color = OnSurfaceVariantText)
                                        Spacer(modifier = Modifier.weight(1f))
                                        Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = "Play", tint = OnSurfaceVariantText.copy(alpha = 0.5f), modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // WORD MATCH GAME BOARD LAYOUT
            val isCleared = matchSuccessList.size == viewModel.matchingKorean.size

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 48.dp)
            ) {
                item {
                    Text(
                        text = "Temukan Padanan Arti",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = InkBlack
                    )
                    Text(
                        text = "Ketuk kotak kata Korea lalu carilah pasangannya dalam terjemahan bahasa Indonesianya.",
                        fontSize = 12.sp,
                        color = OnSurfaceVariantText,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (isCleared) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SoftPink),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Filled.Star, contentDescription = null, tint = CoralVibrant, modifier = Modifier.size(36.dp))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Hore! Berhasil Terhubung Semua!", fontWeight = FontWeight.Bold, color = InkBlack)
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = { viewModel.resetMatchingGame() },
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                                ) {
                                    Text("Main Lagi", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Korean Word list Column
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            viewModel.matchingKorean.forEach { krWord ->
                                val isFound = matchSuccessList.contains(krWord)
                                val isSelected = krSel == krWord

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(60.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            when {
                                                isFound -> SuccessGreen
                                                isSelected -> PrimaryBlue
                                                else -> Color.White
                                            }
                                        )
                                        .clickable { viewModel.selectKoreanWord(krWord) }
                                        .border(
                                            width = if (isSelected) 2.dp else 0.dp,
                                            color = if (isSelected) CoralVibrant else Color.Transparent,
                                            shape = RoundedCornerShape(12.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = krWord,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isFound || isSelected) Color.White else PrimaryBlue
                                    )
                                }
                            }
                        }

                        // Indonesian Words Columns
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            viewModel.matchingIndonesian.forEach { indWord ->
                                // Match checks
                                val isSelected = indSel == indWord
                                val isKoreanCounterpartFound = matchSuccessList.any { matched ->
                                    val matchName = when (matched) {
                                        "우유" -> "Susu"
                                        "물" -> "Air"
                                        "사과" -> "Apel"
                                        "빵" -> "Roti"
                                        else -> ""
                                    }
                                    matchName == indWord
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(60.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            when {
                                                isKoreanCounterpartFound -> SuccessGreen
                                                isSelected -> PrimaryBlue
                                                else -> Color.White
                                            }
                                        )
                                        .clickable { viewModel.selectIndonesianWord(indWord) }
                                        .border(
                                            width = if (isSelected) 2.dp else 0.dp,
                                            color = if (isSelected) CoralVibrant else Color.Transparent,
                                            shape = RoundedCornerShape(12.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = indWord,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isKoreanCounterpartFound || isSelected) Color.White else InkBlack
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Study Desk decorative banner mockup
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(20.dp))
                    ) {
                        AsyncImage(
                            model = URL_STUDENT_DESK,
                            contentDescription = "Meja Belajar",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                                    )
                                )
                        )
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp)
                        ) {
                            Text(text = "Pembelajaran Interaktif", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text(text = "Kaitkan istilah bahasa ke terjemahannya guna merangsang ingatan visual.", fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                        }
                    }
                }
            }
        }
    }
}
