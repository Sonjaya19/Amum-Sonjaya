import React, { useState, useEffect, useRef } from 'react';
import {
  Compass,
  BookOpen,
  User,
  Flame,
  Volume2,
  Sparkles,
  Send,
  Mic,
  MicOff,
  RefreshCw,
  Star,
  Info,
  ChevronRight,
  X,
  Lock,
  Check,
  Eye,
  EyeOff,
  Trash2,
  ArrowRight,
  TrendingUp,
  VolumeX,
  Play
} from 'lucide-react';
import { ChatMessage, VocabItem, UserProgress, TutorResponse } from './types';
import { consultGemini } from './gemini';

// --- Image URL Resources preserved from original layout ---
const URL_EONNI_MAIN = "https://lh3.googleusercontent.com/aida-public/AB6AXuBn59HU0EPAFvOWM44hrJCqDhSNUMBfQviAD0zK1FjDf7hMC-ADf_LYTzMKl4FwXRM7WGVcHVyATnQql_DM9TNnkJ6BHs8H3iXxtjx-Y5mkbriWC2OGO4H2MWAusQForVfwiTVF6yutHsqNmZHIFkCxClcXoA0kE_oGxwQpGv89nV46A3QQDJYRHafJnh-bBOztVoS8sK7PjukCo2VR5waI-xImCh_cJNkDTrZuAyZia0In6YU3hKG7p8iJoce6DzU_PdwwRiXcR3E";
const URL_JIWOO_AVATAR = "https://lh3.googleusercontent.com/aida-public/AB6AXuAfa68DnVfxLeZlDYIV_0x_1yO_bNKlvk0hoMtTVganRBLFb0_yu4Tw5ty4ZJnp7iUYoWH40k4QdTPZxtVhluvBz_ElReHb46nEmEBcB5vhNS9EZ2F2pvM4_5mGGsKukvcrV98CnKdb8LPr_7gIRICIE3kTbFtxFHuQizDsD1beI-lGwDeZXV3d4s7T9h5FgW36tPWsKmNvE42GKF4uaf2Qyn7-0BncxyuwJsaE-dcgYh4lk07cc40-iD9sziMy31gPyYBDkoo85as";
const URL_STUDENT_DEO = "https://lh3.googleusercontent.com/aida-public/AB6AXuDEO_WxmQeqwBKUEL2NZ_DfKk-YSpXb2c5RXBL3-M6MWOQxsT80ErOW_Amrd-ySTdUxLg0avYMCqD8IbZYk5HP-XCmdTUEMK5YSU5wUqwzYJ2o732_SQqVojYE_k9DlnNBjympeNqNNfgJOkR8pMUdr74XEiPRbRmPEWslVgaFQMnZEEV0GtSeWAjgzyChuD9kQqKmLWbLWK2OhPuDKn6MleVK7Xl6t_H8mP8dV1NUII-xGs2oIvRw2sKToOwzFUYUhdtV6Zh5A8Fk";
const URL_STUDENT_ABB = "https://lh3.googleusercontent.com/aida-public/AB6AXuAbbBlLb1m6hTXAvZ6ohM_j36itzFcf9anzfb4PDOA79kABiqGiRxokD454peCb81Yh6YWg6NTDn5melqGMz2HUBKSyUeUAfr_uGROvNqZxvn7WW1OBqQVdGVDDwJuYx0kXY1KK9VO39A-1Eh2c3IfoLeIjsJZg_dRGgEWHVJgdmyr8ztxsqSdGKGbJ5aPBVO07Z8RhQaIEwShtc1Yo2BezfjwIimNmNaJD53dvd-RB6abAqDPMwT-jO9O961KOwzAi8UQsZGG_4yw";
const URL_STUDENT_FE = "https://lh3.googleusercontent.com/aida-public/AB6AXuAjmFe_klgleYMyES5o9L8I-4_n4dKoV5kaQ9ps-DO_sWYLwlq4t-tMKSEF3W_1vJZNPMW9Kb3SZmJvJTdkw4oyI9cNl88izUvof1HwDlBYhKkDMGZRfYwPJ9m7VPHkyuc_kGHSE7qIG1GIjV0dwDE_wjTizaI-qVgkdGDRgKEtQULzfx1M1dY3XM0GhNHeHSjKSzfdHnXicittkaQeMBhmuT6KPW_b0VR0cOUxxrylZ0kKuZ155HaoIxHwU3-pJJOpztKd-Q7FYLg";
const URL_STUDENT_MALE = "https://lh3.googleusercontent.com/aida-public/AB6AXuD8JWpgY-FX810ZrzDSkiEP8UmUPzeXzznPFf0qYidOsW27Ygn0MjEBUGfo_ooNgFh626hgPuwEF1ZJrS_glKT6ZvwEyp4Z27_Rv7cWRU98cikDdS8rfMzo7UHzWR0e-pBxpe3nVzBQNrEiBjY75HDn468JLk2GmtOj0ZI-czYuR1oettaID9tG_EqtC0gK80IUrDCSy7ngTnMh_YMAtQvzp-ow4XiEIautuOAaGFks84YwzO9pPiD5LiRZNn2hTWyRFROn0QIMek";

// Default Seed Vocabulary
const SEED_VOCAB: VocabItem[] = [
  {
    id: 1,
    word: "사과",
    pronunciation: "Sa-gwa",
    translation: "Apel",
    sentenceKorean: "저는 매일 아침 사과를 먹어요.",
    sentenceIndonesian: "Saya memakan sebuah apel setiap pagi.",
    pronunciationGuide: "Huruf 'G' dalam kata 'Sa-gwa' diucapkan lebih lembut dibanding 'G' bahasa Inggris. Coba tempelkan bagian belakang lidah tipis di langit-langit mulut.",
    isMastered: false,
    streakCount: 0
  },
  {
    id: 2,
    word: "물",
    pronunciation: "Mul",
    translation: "Air",
    sentenceKorean: "물을 많이 마시는 것이 건강에 중요해요.",
    sentenceIndonesian: "Banyak minum air bersih merupakan kunci menjaga kesehatan jasmani.",
    pronunciationGuide: "Bunyi konsonan akhir 'L' (ㄹ 받침) dibaca dengan menyentuhkan ujung lidah ke langit-langit mulut depan secara lembut.",
    isMastered: false,
    streakCount: 0
  },
  {
    id: 3,
    word: "빵",
    pronunciation: "Ppang",
    translation: "Roti",
    sentenceKorean: "아침에 따뜻한 빵을 구워서 먹었습니다.",
    sentenceIndonesian: "Saya memanggang roti hangat di pagi hari lalu memakannya.",
    pronunciationGuide: "Konsonan ganda 'Pp' (ㅃ) merupakan konsonan tegang. Katupkan bibir secara rapat sebelum meluncurkan bunyi pelepasan tajam.",
    isMastered: false,
    streakCount: 0
  },
  {
    id: 4,
    word: "우유",
    pronunciation: "U-yu",
    translation: "Susu",
    sentenceKorean: "우유 한 잔을 마시니 기분이 상쾌해집니다.",
    sentenceIndonesian: "Meminum secangkir susu segar membuat mood saya segar gembira kembali.",
    pronunciationGuide: "Bunyi vokal 'U' diucapkan bulat dengan memajukan bibir ke depan agar vokal terartikulasi secara matang.",
    isMastered: false,
    streakCount: 0
  },
  {
    id: 5,
    word: "안녕하세요",
    pronunciation: "An-nyeong-ha-se-yo",
    translation: "Halo",
    sentenceKorean: "선생님, 안녕하세요!",
    sentenceIndonesian: "Halo bapak ibu guru yang saya hormati!",
    pronunciationGuide: "Ucapkan salam ini dengan membungkukkan badan sedikit sebagai tanda hormat sopan santun khas budaya Timur.",
    isMastered: false,
    streakCount: 0
  },
  {
    id: 6,
    word: "고맙습니다",
    pronunciation: "Go-map-seum-ni-da",
    translation: "Terima Kasih",
    sentenceKorean: "도와주셔서 대단히 고맙습니다.",
    sentenceIndonesian: "Terima kasih banyak atas segala pertolongan yang diberikan.",
    pronunciationGuide: "Vokal ganda 'wa' di 'dowa' dilantunkan membulat di awal, dan konsonan 'ㅂ' sebelum 'ㄴ' membaur lembut menjadi bunyi 'm' sengau.",
    isMastered: false,
    streakCount: 0
  },
  {
    id: 7,
    word: "친구",
    pronunciation: "Chin-gu",
    translation: "Teman",
    sentenceKorean: "우리는 오래된 친한 친구입니다.",
    sentenceIndonesian: "Kami bertumpu sebagai sahabat dekat yang sudah lama bersama.",
    pronunciationGuide: "Suku kata 'Chin' (친) menggunakan hembusan napas aspirasi kuat. Berikan udara ekstra di awal pelepasan lidah.",
    isMastered: false,
    streakCount: 0
  }
];

// Default welcome chats
const INITIAL_CHAT = (topic: string): ChatMessage => ({
  id: Date.now(),
  sender: 'tutor',
  text: "안녕하세요! 'Koreaku.AI' Live Speak에 오신 것을 환영합니다! 👋 저는 오늘 공부를 도와줄 Eonni AI입니다.",
  translation: `(Halo! Selamat datang di sesi Live Speak Koreaku.AI! Saya Eonni AI yang siap membimbing belajar Anda tentang tema "${topic}" hari ini.)`,
  timestamp: Date.now()
});

export default function App() {
  const [activeTab, setActiveTab] = useState<'PRACTICE' | 'LESSONS' | 'VOCAB' | 'PROFILE'>('PRACTICE');

  // --- Persistent Storage State Initializers ---
  const [apiKey, setApiKey] = useState<string>(() => {
    return localStorage.getItem('GEMINI_API_KEY') || '';
  });

  const [vocabList, setVocabList] = useState<VocabItem[]>(() => {
    const saved = localStorage.getItem('KOREAKU_VOCAB');
    if (saved) {
      try { return JSON.parse(saved); } catch (e) { console.error(e); }
    }
    return SEED_VOCAB;
  });

  const [chatHistory, setChatHistory] = useState<ChatMessage[]>(() => {
    const saved = localStorage.getItem('KOREAKU_CHATS');
    if (saved) {
      try { return JSON.parse(saved); } catch (e) { console.error(e); }
    }
    return [INITIAL_CHAT('Ordering at a Café')];
  });

  const [userProgress, setUserProgress] = useState<UserProgress>(() => {
    const saved = localStorage.getItem('KOREAKU_PROGRESS');
    if (saved) {
      try { return JSON.parse(saved); } catch (e) { console.error(e); }
    }
    return {
      currentDay: 15,
      streakDays: 12,
      completedDaysCsv: "1,2,3,4,5,6,7,8,9,10,11,12,13,14",
      score: 46
    };
  });

  // Save to LocalStorage side-effects
  useEffect(() => {
    localStorage.setItem('KOREAKU_VOCAB', JSON.stringify(vocabList));
  }, [vocabList]);

  useEffect(() => {
    localStorage.setItem('KOREAKU_CHATS', JSON.stringify(chatHistory));
  }, [chatHistory]);

  useEffect(() => {
    localStorage.setItem('KOREAKU_PROGRESS', JSON.stringify(userProgress));
  }, [userProgress]);

  // --- Dynamic System State ---
  const [currentTopic, setCurrentTopic] = useState<string>('Ordering at a Café');
  const [aiLoading, setAiLoading] = useState<boolean>(false);
  const [textInput, setTextInput] = useState<string>('');
  const [isRecording, setIsRecording] = useState<boolean>(false);

  // --- Flashcard Game State ---
  const [flashcardIdx, setFlashcardIdx] = useState<number>(0);
  const [flashcardFlipped, setFlashcardFlipped] = useState<boolean>(false);

  // --- Word-Matching Game State ---
  const matchingKorean = ["사과", "물", "빵", "우유"];
  const matchingIndonesian = ["Susu", "Air", "Apel", "Roti"];
  const [selectedKorean, setSelectedKorean] = useState<string | null>(null);
  const [selectedIndonesian, setSelectedIndonesian] = useState<string | null>(null);
  const [matchingSuccess, setMatchingSuccess] = useState<string[]>([]);

  // Sound TTS Handler using HTML SpeechSynthesis
  const speakKorean = (text: string) => {
    if ('speechSynthesis' in window) {
      window.speechSynthesis.cancel(); // clear queue
      const cleanText = text.replace(/[()a-zA-Z]/g, '');
      const utterance = new SpeechSynthesisUtterance(cleanText);
      utterance.lang = 'ko-KR';

      // Load Korean native voice if supported
      const voices = window.speechSynthesis.getVoices();
      const koVoice = voices.find(v => v.lang.startsWith('ko') || v.lang.includes('Korean'));
      if (koVoice) {
        utterance.voice = koVoice;
      }
      utterance.rate = 0.9; // clear educational rhythm
      window.speechSynthesis.speak(utterance);
    } else {
      alert('Text to speech tidak didukung pada browser Anda.');
    }
  };

  // Trigger TTS load on startup
  useEffect(() => {
    if ('speechSynthesis' in window) {
      window.speechSynthesis.getVoices();
    }
  }, []);

  // --- AI Send Logic ---
  const handleSendMessage = async (inputStr: string) => {
    const trimmed = inputStr.trim();
    if (!trimmed) return;

    const userMsg: ChatMessage = {
      id: Date.now(),
      sender: 'user',
      text: trimmed,
      timestamp: Date.now()
    };

    const updatedChats = [...chatHistory, userMsg];
    setChatHistory(updatedChats);
    setTextInput('');
    setAiLoading(true);

    try {
      const tutorReply = await consultGemini(updatedChats, currentTopic, apiKey);
      const assistantMsg: ChatMessage = {
        id: Date.now() + 1,
        sender: 'tutor',
        text: tutorReply.replyKorean,
        translation: `(${tutorReply.replyIndonesian})`,
        correction: tutorReply.correctedPart || undefined,
        correctionLabel: tutorReply.wrongPart || undefined,
        timestamp: Date.now() + 1
      };

      setChatHistory(prev => [...prev, assistantMsg]);
      speakKorean(tutorReply.replyKorean);
    } catch (error) {
      console.error(error);
    } finally {
      setAiLoading(false);
    }
  };

  // --- Speech Input Voice Simulator with real mic indicator ---
  const handleToggleVoiceRecord = () => {
    if (isRecording) {
      setIsRecording(false);
      // Simulate speaking or triggering AI
      const mockVoicePrompts = [
        "안녕하세요 (An-nyeong-ha-se-yo)",
        "아이스 아메리카노 주세요 (A-i-seu A-me-ri-ka-no ju-se-yo)",
        "감사합니다 (Gram-sa-ham-ni-da)",
        "이것은 얼마입니까? (I-geot-eun ol-ma-im-ni-ka?)"
      ];
      const randomPrompt = mockVoicePrompts[Math.floor(Math.random() * mockVoicePrompts.length)];
      setTextInput(randomPrompt);
    } else {
      setIsRecording(true);
      // Web speech recognition fallback if available
      const SpeechRecognition = (window as any).SpeechRecognition || (window as any).webkitSpeechRecognition;
      if (SpeechRecognition) {
        const rec = new SpeechRecognition();
        rec.lang = 'ko-KR';
        rec.onresult = (event: any) => {
          const resultText = event.results[0][0].transcript;
          if (resultText) {
            setTextInput(resultText);
          }
          setIsRecording(false);
        };
        rec.onerror = () => {
          setIsRecording(false);
        };
        rec.onend = () => {
          setIsRecording(false);
        };
        rec.start();
      } else {
        // simulation timed auto fallback
        setTimeout(() => {
          setIsRecording(false);
          setTextInput("아이스 아메리카노 하나 주세요");
        }, 3000);
      }
    }
  };

  const handleClearChatHistory = () => {
    if (confirm("Hapus semua riwayat percakapan Anda dengan Eonni AI?")) {
      const cleanHistory = [INITIAL_CHAT(currentTopic)];
      setChatHistory(cleanHistory);
    }
  };

  // --- Word Match Handler ---
  const handleKoreanMatchSelect = (word: string) => {
    if (matchingSuccess.includes(word)) return;
    setSelectedKorean(word);
    checkMatch(word, selectedIndonesian);
  };

  const handleIndonesianMatchSelect = (word: string) => {
    // Find counterpart
    const correspondingKorean = {
      "Susu": "우유",
      "Air": "물",
      "Apel": "사과",
      "Roti": "빵"
    }[word] || "";

    if (matchingSuccess.includes(correspondingKorean)) return;
    setSelectedIndonesian(word);
    checkMatch(selectedKorean, word);
  };

  const checkMatch = (koreanStr: string | null, indonesianStr: string | null) => {
    if (koreanStr && indonesianStr) {
      const matches: Record<string, string> = {
        "우유": "Susu",
        "물": "Air",
        "사과": "Apel",
        "빵": "Roti"
      };

      if (matches[koreanStr] === indonesianStr) {
        const updated = [...matchingSuccess, koreanStr];
        setMatchingSuccess(updated);
        speakKorean(koreanStr);

        // Achievement: increment score metric slightly
        if (updated.length === 4) {
          // Play complete sound or reinforce Progress
          setUserProgress(prev => ({
            ...prev,
            score: Math.min(100, prev.score + 2)
          }));
        }
      }
      setSelectedKorean(null);
      setSelectedIndonesian(null);
    }
  };

  const handleResetMatchingGame = () => {
    setMatchingSuccess([]);
    setSelectedKorean(null);
    setSelectedIndonesian(null);
  };

  // --- Flashcard Rating Handler ---
  const handleFlashcardRating = (vocab: VocabItem, mastered: boolean) => {
    setVocabList(prev =>
      prev.map(item =>
        item.id === vocab.id
          ? {
              ...item,
              isMastered: mastered,
              streakCount: item.streakCount + (mastered ? 1 : 0)
            }
          : item
      )
    );
    // Move to next card
    setFlashcardFlipped(false);
    setTimeout(() => {
      setFlashcardIdx(prev => (prev + 1) % vocabList.length);
    }, 200);
  };

  // Switch Chat Scenario Topic
  const handleSwitchTopic = (topic: string) => {
    setCurrentTopic(topic);
    const newPrompt = INITIAL_CHAT(topic);
    setChatHistory([newPrompt]);
  };

  // Mark calendar day completion
  const handleToggleCalendarDay = (day: number) => {
    const list = userProgress.completedDaysCsv
      .split(',')
      .map(d => d.trim())
      .filter(d => d.length > 0)
      .map(Number);

    let updatedList;
    if (list.includes(day)) {
      updatedList = list.filter(d => d !== day);
    } else {
      updatedList = [...list, day].sort((a,b) => a - b);
    }

    const calculatedProgress = Math.round((updatedList.length / 30) * 100);

    setUserProgress(prev => ({
      ...prev,
      completedDaysCsv: updatedList.join(','),
      score: calculatedProgress,
      streakDays: updatedList.length > list.length ? prev.streakDays + 1 : Math.max(0, prev.streakDays - 1)
    }));
  };

  const completedDaysList = userProgress.completedDaysCsv
    .split(',')
    .map(d => d.trim())
    .filter(d => d.length > 0)
    .map(Number);

  return (
    <div className="min-h-screen bg-slate-50 flex flex-col max-w-md mx-auto shadow-2xl relative border-x border-slate-200">
      {/* 1. TOP APP BAR */}
      <header className="sticky top-0 z-40 bg-white border-b border-slate-100 px-5 py-3 flex items-center justify-between">
        <button
          onClick={() => setActiveTab('PROFILE')}
          className="flex items-center gap-2.5 active:scale-95 transition-transform text-left"
        >
          <img
            src={URL_STUDENT_MALE}
            alt="Profil"
            className="w-9 h-9 rounded-full object-cover border-2 border-primaryBlue/20"
          />
          <div>
            <h1 className="text-lg font-extrabold text-primaryBlue leading-none font-sans">Koreaku.AI</h1>
            <span className="text-xs text-slate-400">Belajar AI 24/7</span>
          </div>
        </button>

        <button
          onClick={() => setActiveTab('PRACTICE')}
          className="flex items-center gap-1.5 bg-rose-50 px-3 py-1.5 rounded-full border border-rose-100 hover:bg-rose-100 active:scale-95 transition-all text-xs font-bold text-coralVibrant"
        >
          <Flame className="w-4 h-4 fill-coralVibrant text-coralVibrant" />
          <span>{userProgress.streakDays} Hari</span>
        </button>
      </header>

      {/* 2. MAIN BODY SCROLL & SWITCH CONTROLS */}
      <main className="flex-1 overflow-y-auto pb-24">
        {activeTab === 'PRACTICE' && (
          <div className="p-4 space-y-4 animate-fade-in">
            {/* Today's Misi Challenge Banner */}
            <div className="bg-primaryBlue text-white p-5 rounded-2xl shadow-lg relative overflow-hidden">
              <div className="flex items-center justify-between mb-4">
                <span className="bg-white/20 text-white text-[10px] uppercase font-bold tracking-wider px-2.5 py-1 rounded-md">
                  Misi Hari Ini
                </span>
                <span className="text-xs text-white/80 flex items-center gap-1">
                  <span className="w-1.5 h-1.5 rounded-full bg-orange-400 animate-pulse"></span>
                  Est. 15 Menit
                </span>
              </div>

              <h2 className="text-lg font-extrabold mb-1.5 leading-snug">
                Mastering Subject Markers (-이 / -가)
              </h2>
              <p className="text-sm text-white/80 leading-relaxed mb-5">
                Kuasai cara memasangkan akhiran penanda subjek layaknya pembicara asli melalui latihan live simulasi.
              </p>

              <div className="flex items-center justify-between">
                <button
                  onClick={() => {
                    handleSwitchTopic('Subject Markers (-이 / -가)');
                    setActiveTab('LESSONS');
                  }}
                  className="bg-coralVibrant text-white font-bold text-sm px-4 py-2.5 rounded-xl hover:bg-coralVibrant/90 transition-all shadow-md active:scale-95 flex items-center gap-1.5"
                >
                  <Play className="w-4 h-4 fill-white" />
                  Mulai Belajar
                </button>

                <div className="flex items-center gap-2">
                  <div className="flex -space-x-2">
                    <img src={URL_STUDENT_DEO} className="w-6 h-6 rounded-full border-2 border-white object-cover" />
                    <img src={URL_STUDENT_ABB} className="w-6 h-6 rounded-full border-2 border-white object-cover" />
                    <img src={URL_STUDENT_FE} className="w-6 h-6 rounded-full border-2 border-white object-cover" />
                  </div>
                  <span className="text-[11px] text-white/70 font-semibold">+12 sedang belajar</span>
                </div>
              </div>
            </div>

            {/* Program Challenge 30 Days tracker */}
            <div className="bg-white p-4 rounded-2xl border border-slate-100 shadow-sm">
              <div className="flex items-center justify-between mb-3">
                <h3 className="font-bold text-slate-800 flex items-center gap-1.5 text-sm uppercase tracking-wide">
                  Tantangan 30 Hari Belajar
                </h3>
                <span className="text-xs font-bold text-primaryBlue">
                  {completedDaysList.length} / 30 Hari
                </span>
              </div>

              {/* Day matrix map 1 to 21 */}
              <div className="grid grid-cols-7 gap-2 my-4">
                {Array.from({ length: 21 }, (_, index) => {
                  const day = index + 1;
                  const isDone = completedDaysList.includes(day);
                  const isCurrent = day === userProgress.currentDay;

                  return (
                    <button
                      key={day}
                      onClick={() => handleToggleCalendarDay(day)}
                      style={{ contentVisibility: 'auto' }}
                      className={`aspect-square rounded-xl text-xs font-bold transition-all relative flex flex-col items-center justify-center border ${
                        isDone
                          ? 'bg-primaryBlue border-primaryBlue text-white'
                          : isCurrent
                          ? 'bg-white border-coralVibrant text-coralVibrant shadow-inner'
                          : 'bg-slate-50 border-slate-100 hover:bg-slate-100 text-slate-600'
                      }`}
                    >
                      {isDone ? (
                        <Check className="w-4 h-4 text-white stroke-[3px]" />
                      ) : (
                        <span>{day}</span>
                      )}
                      {isCurrent && !isDone && (
                        <span className="absolute bottom-1 w-1 h-1 rounded-full bg-coralVibrant"></span>
                      )}
                    </button>
                  );
                })}
              </div>

              {/* Dynamic ProgressBar */}
              <div className="space-y-1.5 pt-1 border-t border-slate-50">
                <div className="flex justify-between text-xs font-semibold text-slate-500">
                  <span>Progres Kurikulum</span>
                  <span className="text-primaryBlue font-bold">{userProgress.score}% Selesai</span>
                </div>
                <div className="w-full bg-slate-100 h-2.5 rounded-full overflow-hidden">
                  <div
                    className="bg-primaryBlue h-full rounded-full transition-all duration-500"
                    style={{ width: `${userProgress.score}%` }}
                  ></div>
                </div>
                <p className="text-[11px] text-slate-400 text-center leading-relaxed pt-1">
                  Hebat! Klik tanggal di atas untuk menandai progres harian Anda secara live.
                </p>
              </div>
            </div>

            {/* Native Pathway Modules list */}
            <div className="space-y-2.5">
              <h3 className="font-extrabold text-slate-800 text-base">Modul Kurikulum Utama</h3>

              <div
                onClick={() => {
                  handleSwitchTopic('Ordering at a Café');
                  setActiveTab('LESSONS');
                }}
                className="bg-white p-4 rounded-xl border border-slate-100 hover:border-primaryBlue/20 cursor-pointer shadow-sm transition-all flex items-center justify-between"
              >
                <div className="flex items-center gap-3.5">
                  <div className="w-10 h-10 rounded-xl bg-blue-50 flex items-center justify-center text-primaryBlue">
                    <Compass className="w-5 h-5" />
                  </div>
                  <div>
                    <h4 className="font-bold text-slate-800 text-sm">Speaking: Daily Practice</h4>
                    <p className="text-xs text-slate-400 leading-normal">
                      Koreksi artikulasi langsung berbekal asisten tutor AI.
                    </p>
                  </div>
                </div>
                <ChevronRight className="w-5 h-5 text-slate-300" />
              </div>

              <div
                onClick={() => setActiveTab('VOCAB')}
                className="bg-white p-4 rounded-xl border border-slate-100 hover:border-primaryBlue/20 cursor-pointer shadow-sm transition-all flex items-center justify-between"
              >
                <div className="flex items-center gap-3.5">
                  <div className="w-10 h-10 rounded-xl bg-amber-50 flex items-center justify-center text-amber-500">
                    <BookOpen className="w-5 h-5" />
                  </div>
                  <div>
                    <h4 className="font-bold text-slate-800 text-sm">Vocabulary Bank</h4>
                    <p className="text-xs text-slate-400 leading-normal">
                      Hafalan kosakata TOPIK sistem flashcards otomatis.
                    </p>
                  </div>
                </div>
                <ChevronRight className="w-5 h-5 text-slate-300" />
              </div>

              {/* Special café scenario card */}
              <div
                onClick={() => {
                  handleSwitchTopic('Ordering at a Café');
                  setActiveTab('LESSONS');
                }}
                className="bg-white border border-blue-100 rounded-2xl hover:border-primaryBlue/30 shadow-sm overflow-hidden cursor-pointer transition-all p-4 flex gap-4"
              >
                <div className="w-12 h-12 rounded-xl bg-rose-50 flex items-center justify-center text-coralVibrant flex-shrink-0">
                  <Sparkles className="w-6 h-6 fill-rose-50/10" />
                </div>
                <div className="flex-1 space-y-1">
                  <div className="flex items-center gap-1.5">
                    <h4 className="font-extrabold text-slate-800 text-sm">Kemampuan Bersosialisasi</h4>
                    <span className="bg-rose-100 text-coralVibrant text-[9px] font-bold px-2 py-0.5 rounded-full">
                      Populer
                    </span>
                  </div>
                  <p className="text-xs text-slate-500 leading-relaxed">
                    Skenario praktis memesan kopi, bertanya arah jalan di Hongdae, dan mengobrol santai layaknya warga lokal di Korea.
                  </p>
                </div>
              </div>
            </div>

            {/* Embedded Recommended Tutor Panel */}
            <div className="bg-sky-50 border border-sky-100 rounded-2xl p-4 flex gap-3.5">
              <img
                src={URL_JIWOO_AVATAR}
                alt="Jiwoo Tutor"
                className="w-10 h-10 rounded-full object-cover border border-sky-200"
              />
              <div className="flex-1 space-y-2">
                <div>
                  <h4 className="text-xs font-bold text-primaryBlue">Ji-woo (Tutor AI Koreaku)</h4>
                  <p className="text-xs text-slate-700 leading-relaxed italic">
                    "Siap melakukan pendalaman materi subjek marker (이/가) dan memesan kopi hari ini bersama saya?"
                  </p>
                </div>
                <div className="flex gap-2">
                  <button
                    onClick={() => {
                      handleSwitchTopic('Subject Markers (-이 / -가)');
                      setActiveTab('LESSONS');
                    }}
                    className="bg-primaryBlue text-white text-xs font-bold px-3 py-1.5 rounded-lg active:scale-95 transition-transform"
                  >
                    Ayo Masuk Kelas!
                  </button>
                  <button
                    onClick={() => setActiveTab('VOCAB')}
                    className="border border-slate-200 bg-white text-slate-500 text-xs font-semibold px-3 py-1.5 rounded-lg active:scale-95 transition-transform"
                  >
                    Hafalkan Kosakata
                  </button>
                </div>
              </div>
            </div>
          </div>
        )}

        {activeTab === 'LESSONS' && (
          <div className="flex flex-col h-[calc(100vh-140px)] bg-slate-50 animate-fade-in relative">
            {/* Live Chat Topic Subheader */}
            <div className="bg-white border-b border-slate-100 p-3.5 flex items-center justify-between gap-3 shadow-xs">
              <div className="flex-1">
                <div className="flex items-center gap-1.5 mb-1.5">
                  <span className="w-1.5 h-1.5 bg-rose-600 rounded-full animate-ping"></span>
                  <span className="text-[10px] font-extrabold text-slate-400 uppercase tracking-wider">
                    Sesi Roleplay AI Live
                  </span>
                </div>
                <select
                  value={currentTopic}
                  onChange={(e) => handleSwitchTopic(e.target.value)}
                  className="bg-slate-50 border border-slate-200 text-xs font-extrabold text-slate-700 rounded-lg py-1 px-2 focus:outline-none focus:border-primaryBlue"
                >
                  <option value="Ordering at a Café">Skenario: Memesan Kopi di Café (-아메리카노 주세요)</option>
                  <option value="Subject Markers (-이 / -가)">Tata Bahasa: Subject Markers (-이 / -가)</option>
                  <option value="Shopping in Hongdae">Skenario: Membeli Pakaian di Hongdae (-얼마예요?)</option>
                  <option value="Asking for Directions">Skenario: Bertanya Arah Stasiun Kereta (-어디에 있어요?)</option>
                </select>
              </div>

              <button
                onClick={handleClearChatHistory}
                title="Reset Obrolan"
                className="p-2 rounded-lg text-slate-400 hover:text-red-500 hover:bg-red-50 active:scale-95 transition-all flex-shrink-0"
              >
                <Trash2 className="w-4 h-4" />
              </button>
            </div>

            {/* Chat Messages Feed List */}
            <div className="flex-1 overflow-y-auto p-4 space-y-4">
              {chatHistory.map((msg) => (
                <div
                  key={msg.id}
                  className={`flex gap-2.5 ${msg.sender === 'user' ? 'justify-end' : 'justify-start'}`}
                >
                  {msg.sender === 'tutor' && (
                    <img
                      src={URL_EONNI_MAIN}
                      alt="Eonni Tutor avatar"
                      className="w-8 h-8 rounded-full object-cover mt-1.5 border border-slate-200"
                    />
                  )}

                  <div className="space-y-1.5 max-w-[80%]">
                    <div
                      className={`p-3 rounded-2xl shadow-xs text-sm leading-relaxed relative ${
                        msg.sender === 'user'
                          ? 'bg-primaryBlue text-white rounded-tr-none'
                          : 'bg-white text-slate-800 rounded-tl-none border border-slate-100'
                      }`}
                    >
                      {/* Speaker Read Aloud for Korean */}
                      {msg.sender === 'tutor' && (
                        <button
                          onClick={() => speakKorean(msg.text)}
                          className="absolute right-2 top-2 p-1 bg-slate-50 rounded text-slate-400 hover:text-primaryBlue transition-all"
                          title="Putar Audio"
                        >
                          <Volume2 className="w-3.5 h-3.5" />
                        </button>
                      )}

                      <p className={`font-semibold ${msg.sender === 'tutor' ? 'text-slate-900 pr-5' : 'text-white'}`}>
                        {msg.text}
                      </p>

                      {msg.translation && (
                        <p className={`text-xs mt-1.5 italic ${msg.sender === 'tutor' ? 'text-slate-400' : 'text-white/80'}`}>
                          {msg.translation}
                        </p>
                      )}
                    </div>

                    {/* AI Syntax Grammar Corrections Alert Panel */}
                    {msg.sender === 'tutor' && msg.correction && (
                      <div className="bg-amber-50 border border-amber-200 text-slate-700 p-3 rounded-xl space-y-1 my-1">
                        <span className="inline-block bg-amber-100 text-amber-800 text-[9px] uppercase font-extrabold px-2 py-0.5 rounded">
                          Rekomendasi Koreksi AI
                        </span>
                        {msg.correctionLabel && (
                          <div className="text-xs pt-1">
                            <span className="text-red-500 line-through mr-1 font-bold">{msg.correctionLabel}</span>
                            <span className="text-slate-400">➔</span>
                            <span className="text-emerald-600 font-bold ml-1">{msg.correction}</span>
                          </div>
                        )}
                        <p className="text-[11px] text-slate-500 italic mt-0.5 leading-normal">
                          {msg.text.includes('죄송합니다') ? 'Harap atur API Key di tab profile untuk mengaktifkan koreksi interaktif' : 'Pastikan pengucapan Hangul sesuai vokal penekanan.'}
                        </p>
                      </div>
                    )}
                  </div>
                </div>
              ))}

              {aiLoading && (
                <div className="flex gap-2.5 justify-start items-center">
                  <img
                    src={URL_EONNI_MAIN}
                    alt="Eonni Loading"
                    className="w-8 h-8 rounded-full object-cover animate-spin border border-slate-200"
                  />
                  <div className="bg-white border border-slate-100 p-3.5 rounded-2xl rounded-tl-none flex items-center gap-1.5">
                    <span className="w-1.5 h-1.5 bg-primaryBlue rounded-full animate-bounce"></span>
                    <span className="w-1.5 h-1.5 bg-primaryBlue rounded-full animate-bounce delay-100"></span>
                    <span className="w-1.5 h-1.5 bg-primaryBlue rounded-full animate-bounce delay-200"></span>
                    <span className="text-xs text-slate-400 font-semibold ml-2">Eonni AI sedang mengetik...</span>
                  </div>
                </div>
              )}
            </div>

            {/* Chat Compose Keyboard Controls */}
            <div className="bg-white border-t border-slate-200 p-3 space-y-2 flex-shrink-0">
              <div className="flex items-center gap-2">
                <button
                  onClick={handleToggleVoiceRecord}
                  title="Tekan untuk Bicara (Mic)"
                  className={`p-3.5 rounded-xl text-white transition-all active:scale-95 relative flex-shrink-0 ${
                    isRecording ? 'bg-rose-500 animate-pulse' : 'bg-primaryBlue hover:bg-primaryBlue/95'
                  }`}
                >
                  {isRecording ? <MicOff className="w-5 h-5" /> : <Mic className="w-5 h-5" />}
                  {isRecording && (
                    <span className="absolute -top-1 -right-1 flex h-3.5 w-3.5">
                      <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-rose-400 opacity-75"></span>
                      <span className="relative inline-flex rounded-full h-3.5 w-3.5 bg-rose-600"></span>
                    </span>
                  )}
                </button>

                <input
                  type="text"
                  placeholder={isRecording ? "Mendengarkan suara Anda..." : "Balas dalam Bahasa Korea..."}
                  disabled={isRecording}
                  value={textInput}
                  onChange={(e) => setTextInput(e.target.value)}
                  onKeyDown={(e) => {
                    if (e.key === 'Enter') handleSendMessage(textInput);
                  }}
                  className="flex-1 bg-slate-50 border border-slate-200 rounded-xl px-3.5 py-3 text-sm focus:outline-none focus:border-primaryBlue disabled:bg-slate-100"
                />

                <button
                  onClick={() => handleSendMessage(textInput)}
                  disabled={!textInput.trim() || aiLoading}
                  className="bg-coralVibrant text-white p-3.5 rounded-xl disabled:bg-slate-100 disabled:text-slate-300 transition-all active:scale-95 flex-shrink-0"
                >
                  <Send className="w-5 h-5" />
                </button>
              </div>

              {!apiKey && (
                <div className="text-center">
                  <span className="inline-flex items-center gap-1 bg-amber-50 border border-amber-100 text-[10px] font-bold text-amber-700 px-2.5 py-1 rounded-full">
                    <Info className="w-3.5 h-3.5" />
                    Menggunakan Simulator. Pasang Gemini API Key Anda di tab 'Profile' untuk berkirim pesan live AI!
                  </span>
                </div>
              )}
            </div>
          </div>
        )}

        {activeTab === 'VOCAB' && (
          <div className="p-4 space-y-5 animate-fade-in">
            {/* 3D Flashcard Deck Container */}
            <div className="space-y-2">
              <div className="flex items-center justify-between">
                <h3 className="font-extrabold text-slate-800 text-sm uppercase tracking-wider">
                  Kartu Hafalan Interaktif
                </h3>
                <span className="text-xs font-bold text-slate-400">
                  Kosa kata: {flashcardIdx + 1} / {vocabList.length}
                </span>
              </div>

              {/* Card container with flip support */}
              <div
                onClick={() => setFlashcardFlipped(!flashcardFlipped)}
                style={{ contentVisibility: 'auto' }}
                className="perspective-1000 w-full h-64 cursor-pointer relative"
              >
                <div
                  className={`w-full h-full duration-500 transform-style-3d relative ${
                    flashcardFlipped ? 'rotate-y-180' : ''
                  }`}
                >
                  {/* CARD FRONT SIDE */}
                  <div className="absolute inset-0 backface-hidden bg-white border border-slate-100 rounded-3xl p-6 shadow-md flex flex-col justify-between items-center text-center">
                    <div className="flex items-center justify-between w-full">
                      <span className="text-[10px] font-extrabold text-primaryBlue bg-blue-50 px-3 py-1 rounded-full uppercase tracking-wider">
                        Tembak Hangul
                      </span>
                      <button
                        onClick={(e) => {
                          e.stopPropagation();
                          speakKorean(vocabList[flashcardIdx]?.word);
                        }}
                        className="p-2.5 bg-slate-50 text-slate-500 rounded-full hover:bg-slate-100 active:scale-90 transition-transform"
                        title="Pelafalan Audio"
                      >
                        <Volume2 className="w-4 h-4" />
                      </button>
                    </div>

                    <div className="space-y-2">
                      <h2 className="text-4xl font-black text-slate-900 tracking-wide font-sans">
                        {vocabList[flashcardIdx]?.word}
                      </h2>
                      <p className="text-sm font-bold text-slate-400 italic">
                        {vocabList[flashcardIdx]?.pronunciation}
                      </p>
                    </div>

                    <p className="text-xs text-slate-400 font-semibold animate-pulse">
                      Tap untuk perlihatkan makna
                    </p>
                  </div>

                  {/* CARD BACK SIDE */}
                  <div className="absolute inset-0 backface-hidden rotate-y-180 bg-slate-900 border border-slate-800 text-white rounded-3xl p-5 shadow-lg flex flex-col justify-between">
                    <div>
                      <div className="flex justify-between items-start mb-2">
                        <span className="bg-coralVibrant text-[10px] font-extrabold uppercase px-2.5 py-1 rounded-full">
                          Terjemahan & Contoh
                        </span>
                        <div className="text-right">
                          <span className="text-[10px] text-slate-400 block font-bold">Akurasi Hafalan</span>
                          <span className="text-xs font-bold text-amber-400">Streak: {vocabList[flashcardIdx]?.streakCount}x</span>
                        </div>
                      </div>

                      <div className="space-y-1 mt-2">
                        <h4 className="text-base font-black text-amber-300">
                          {vocabList[flashcardIdx]?.translation}
                        </h4>
                        <p className="text-[11px] text-slate-300 leading-normal border-l-2 border-slate-700 pl-2">
                          <strong className="block text-slate-400 font-normal">Panduan pengucapan:</strong>
                          {vocabList[flashcardIdx]?.pronunciationGuide}
                        </p>
                      </div>
                    </div>

                    {/* Example sentence section */}
                    <div className="bg-slate-800/60 p-2.5 rounded-xl space-y-0.5">
                      <div className="flex justify-between items-center text-[10px] text-slate-400">
                        <span>Contoh Penggunaan:</span>
                        <button
                          onClick={(e) => {
                            e.stopPropagation();
                            speakKorean(vocabList[flashcardIdx]?.sentenceKorean);
                          }}
                          className="text-slate-400 hover:text-white transition-all"
                        >
                          <Volume2 className="w-3.5 h-3.5" />
                        </button>
                      </div>
                      <p className="text-xs text-white font-extrabold pr-4">{vocabList[flashcardIdx]?.sentenceKorean}</p>
                      <p className="text-[10px] text-slate-400 italic font-semibold">{vocabList[flashcardIdx]?.sentenceIndonesian}</p>
                    </div>

                    <div className="flex gap-2 justify-end">
                      <button
                        onClick={(e) => {
                          e.stopPropagation();
                          handleFlashcardRating(vocabList[flashcardIdx], false);
                        }}
                        className="bg-slate-800 hover:bg-slate-700 text-[10px] font-bold px-3 py-2 rounded-lg text-rose-400"
                      >
                        Pelajari Lagi
                      </button>
                      <button
                        onClick={(e) => {
                          e.stopPropagation();
                          handleFlashcardRating(vocabList[flashcardIdx], true);
                        }}
                        className="bg-primaryBlue hover:bg-primaryBlue/90 text-[10px] font-bold px-3 py-2 rounded-lg text-white"
                      >
                        Sudah Hafal ✓
                      </button>
                    </div>
                  </div>
                </div>
              </div>

              {/* Slider Controller buttons */}
              <div className="grid grid-cols-2 gap-3">
                <button
                  onClick={() => {
                    setFlashcardFlipped(false);
                    setFlashcardIdx(prev => (prev - 1 + vocabList.length) % vocabList.length);
                  }}
                  className="bg-white border border-slate-100 hover:bg-slate-50 text-slate-600 font-bold py-2.5 rounded-xl text-xs active:scale-95 transition-all text-center"
                >
                  Kembali
                </button>
                <button
                  onClick={() => {
                    setFlashcardFlipped(false);
                    setFlashcardIdx(prev => (prev + 1) % vocabList.length);
                  }}
                  className="bg-white border border-slate-100 hover:bg-slate-50 text-slate-600 font-bold py-2.5 rounded-xl text-xs active:scale-95 transition-all text-center"
                >
                  Kartu Lanjutan
                </button>
              </div>
            </div>

            {/* Word Association Matching Game Panel */}
            <div className="bg-white border border-slate-100 rounded-3xl p-4.5 space-y-4 shadow-xs">
              <div className="flex items-center justify-between">
                <div>
                  <h3 className="font-extrabold text-slate-800 text-sm uppercase tracking-wide">
                    Latihan Hubung Kata
                  </h3>
                  <span className="text-[11px] text-slate-400">Hubungkan kata Korea & Indonesia</span>
                </div>
                {matchingSuccess.length > 0 && (
                  <button
                    onClick={handleResetMatchingGame}
                    className="flex items-center gap-1 text-[11px] font-bold text-slate-400 hover:text-primaryBlue transition-colors"
                  >
                    <RefreshCw className="w-3.5 h-3.5" />
                    Reset
                  </button>
                )}
              </div>

              <div className="grid grid-cols-2 gap-3">
                {/* Korean Words Column */}
                <div className="space-y-2">
                  <span className="text-[10px] font-bold text-slate-400 uppercase tracking-wider block text-center mb-1">
                    Bahasa Korea (Hangul)
                  </span>
                  {matchingKorean.map((word) => {
                    const isMatched = matchingSuccess.includes(word);
                    const isSelected = selectedKorean === word;

                    return (
                      <button
                        key={word}
                        onClick={() => handleKoreanMatchSelect(word)}
                        className={`w-full py-3 px-2 rounded-xl text-xs font-bold transition-all border text-center ${
                          isMatched
                            ? 'bg-emerald-50 border-emerald-100 text-emerald-600 cursor-not-allowed opacity-60'
                            : isSelected
                            ? 'bg-primaryBlue/12 border-primaryBlue text-primaryBlue scale-[0.98]'
                            : 'bg-slate-50 border-slate-100 hover:bg-slate-100/80 text-slate-700'
                        }`}
                        disabled={isMatched}
                      >
                        {word}
                        {isMatched && <span className="block text-[8px] text-emerald-500 font-extrabold">Cocok ✓</span>}
                      </button>
                    );
                  })}
                </div>

                {/* Indonesian Meaning Correspondings */}
                <div className="space-y-2">
                  <span className="text-[10px] font-bold text-slate-400 uppercase tracking-wider block text-center mb-1">
                    Makna Indonesian
                  </span>
                  {matchingIndonesian.map((word) => {
                    const mappedKorean = {
                      "Susu": "우유",
                      "Air": "물",
                      "Apel": "사과",
                      "Roti": "빵"
                    }[word] || "";

                    const isMatched = matchingSuccess.includes(mappedKorean);
                    const isSelected = selectedIndonesian === word;

                    return (
                      <button
                        key={word}
                        onClick={() => handleIndonesianMatchSelect(word)}
                        className={`w-full py-3 px-2 rounded-xl text-xs font-bold transition-all border text-center ${
                          isMatched
                            ? 'bg-emerald-50 border-emerald-100 text-emerald-600 cursor-not-allowed opacity-60'
                            : isSelected
                            ? 'bg-primaryBlue/12 border-primaryBlue text-primaryBlue scale-[0.98]'
                            : 'bg-slate-50 border-slate-100 hover:bg-slate-100/80 text-slate-700'
                        }`}
                        disabled={isMatched}
                      >
                        {word}
                        {isMatched && <span className="block text-[8px] text-emerald-500 font-extrabold">Cocok ✓</span>}
                      </button>
                    );
                  })}
                </div>
              </div>

              {matchingSuccess.length === 4 && (
                <div className="bg-emerald-50 border border-emerald-100 p-3.5 rounded-2xl flex items-center gap-2.5 text-center justify-center animate-bounce">
                  <Sparkles className="w-5 h-5 text-emerald-600 fill-emerald-50" />
                  <span className="text-xs font-bold text-emerald-700">
                    Sempurna! Anda berhasil memasangkan seluruh kata dengan tepat.
                  </span>
                </div>
              )}
            </div>

            {/* Vocab Summary List view */}
            <div className="space-y-2.5">
              <h3 className="font-extrabold text-slate-800 text-sm uppercase tracking-wider">
                Bank Kosakata Saya
              </h3>

              <div className="bg-white border border-slate-100 rounded-2xl overflow-hidden shadow-xs divide-y divide-slate-50">
                {vocabList.map((item) => (
                  <div
                    key={item.id}
                    className="p-3.5 flex items-center justify-between hover:bg-slate-50 transition-colors"
                  >
                    <div className="flex items-center gap-3">
                      <button
                        onClick={() => speakKorean(item.word)}
                        className="p-2 rounded-full bg-slate-50 text-slate-400 hover:text-primaryBlue hover:bg-blue-50 transition-all flex-shrink-0"
                      >
                        <Volume2 className="w-3.5 h-3.5" />
                      </button>
                      <div>
                        <div className="flex items-center gap-1.5">
                          <h4 className="font-extrabold text-slate-900 text-sm tracking-wide">{item.word}</h4>
                          <span className="text-[10px] text-slate-400 font-semibold">({item.pronunciation})</span>
                        </div>
                        <p className="text-xs text-slate-500 font-medium">{item.translation}</p>
                      </div>
                    </div>

                    <div className="flex items-center gap-2">
                      <span className={`text-[10px] font-bold px-2 py-0.5 rounded-full ${
                        item.isMastered
                          ? 'bg-emerald-100 text-emerald-700'
                          : 'bg-slate-100 text-slate-500'
                      }`}>
                        {item.isMastered ? 'Hafal ✓' : 'Belajar'}
                      </span>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          </div>
        )}

        {activeTab === 'PROFILE' && (
          <div className="p-4 space-y-5 animate-fade-in">
            {/* Landing Hero Card */}
            <div className="bg-white border border-slate-100 rounded-3xl p-5.5 shadow-sm space-y-4">
              <span className="inline-flex items-center gap-1 bg-blue-50 text-primaryBlue text-[10px] uppercase font-extrabold px-3 py-1 rounded-full tracking-wider">
                <Star className="w-3.5 h-3.5 fill-blue-50" />
                AI Tutor Pribadi 24/7
              </span>

              <div className="space-y-1">
                <h2 className="text-2xl font-black text-slate-900 leading-tight">
                  Kuasai Bahasa Korea
                </h2>
                <h2 className="text-2xl font-black text-coralVibrant leading-tight">
                  Tanpa Harus
                </h2>
                <h2 className="text-2xl font-black text-slate-900 leading-tight">
                  Masuk Kelas Kursus!
                </h2>
              </div>

              <p className="text-xs text-slate-500 leading-relaxed">
                Eonni & Oppa AI siap membantumu lancar bercakap Hangul kapan saja, di mana saja. Belajar efektif dengan metode AI modern yang disesuaikan untuk pelajar Indonesia.
              </p>

              <div>
                <button
                  onClick={() => setActiveTab('PRACTICE')}
                  className="w-full bg-coralVibrant text-white font-extrabold text-sm py-3.5 rounded-2xl hover:bg-coralVibrant/90 transition-all shadow-md active:scale-95 text-center block"
                >
                  Mulai Belajar Sekarang
                </button>

                <div className="flex justify-center items-center gap-1.5 mt-2.5">
                  <span className="text-xs text-slate-300 line-through">Rp 250.000</span>
                  <span className="text-sm font-black text-primaryBlue">Cuma Rp 99.000</span>
                </div>
              </div>

              {/* Student Count stack representation */}
              <div className="flex items-center gap-2.5 justify-center border-t border-slate-50 pt-3">
                <div className="flex -space-x-2">
                  <img src={URL_STUDENT_FE} className="w-7 h-7 rounded-full border-2 border-white object-cover" />
                  <img src={URL_STUDENT_ABB} className="w-7 h-7 rounded-full border-2 border-white object-cover" />
                  <img src={URL_STUDENT_DEO} className="w-7 h-7 rounded-full border-2 border-white object-cover" />
                </div>
                <span className="text-xs text-slate-500 font-bold">1,200+ Siswa telah bergabung</span>
              </div>
            </div>

            {/* Static Snapshot decoration matching HTML specs */}
            <div className="bg-white border border-slate-100 rounded-3xl overflow-hidden shadow-xs relative h-72">
              <img
                src={URL_EONNI_MAIN}
                alt="Portrait Eonni"
                className="w-full h-full object-cover"
              />
              <div className="absolute inset-0 bg-gradient-to-t from-black/85 via-black/20 to-transparent flex flex-col justify-between p-4">
                {/* Floating tooltips */}
                <div className="bg-white/85 backdrop-blur-md p-2.5 rounded-xl border border-white/20 w-44">
                  <span className="text-[10px] text-primaryBlue font-bold block">Eonni AI Tutor</span>
                  <span className="text-xs text-slate-800 font-extrabold">Coba ucapkan:<br />"Gamsahamnida"</span>
                </div>

                <div className="bg-white/90 p-2.5 rounded-xl border border-white/20 flex items-center gap-1.5 self-end">
                  <div className="w-4 h-4 rounded-full bg-emerald-50 flex items-center justify-center">
                    <Check className="w-3 h-3 text-emerald-600 stroke-[3px]" />
                  </div>
                  <div>
                    <span className="text-[10px] uppercase font-extrabold text-slate-500 block">Pelafalan</span>
                    <span className="text-xs font-bold text-slate-900">Rasio 98%: Sempurna</span>
                  </div>
                </div>

                <div>
                  <h3 className="text-lg font-black text-white leading-tight">안녕하세요! 👋</h3>
                  <p className="text-xs text-white/80 uppercase tracking-wide">"Mari belajar bicara Korea asyik bersama saya!"</p>
                </div>
              </div>
            </div>

            {/* 3. GEMINI AI CREDENTIAL SERVICES CONFIG (Secrets Integration Panel) */}
            <div className="bg-white border border-slate-100 rounded-3xl p-5 shadow-sm space-y-4">
              <div className="flex items-center gap-2">
                <div className="w-9 h-9 rounded-xl bg-orange-50 text-orange-500 flex items-center justify-center">
                  <Sparkles className="w-5 h-5" />
                </div>
                <div>
                  <h3 className="font-extrabold text-slate-800 text-sm uppercase tracking-wide">
                    Pengaturan AI Credentials
                  </h3>
                  <span className="text-[11px] text-slate-400">Hubungkan kunci API Gemini Anda</span>
                </div>
              </div>

              <div className="space-y-3">
                <div className="space-y-1.5">
                  <label className="text-xs font-bold text-slate-500 uppercase tracking-wider block">
                    Gemini API Key
                  </label>
                  <div className="relative">
                    <input
                      type="password"
                      placeholder="AI_STUDIO_SECRETS_KEY_123..."
                      value={apiKey}
                      onChange={(e) => {
                        const val = e.target.value;
                        setApiKey(val);
                        localStorage.setItem('GEMINI_API_KEY', val);
                      }}
                      className="w-full bg-slate-50 border border-slate-100 rounded-xl px-3.5 py-2.5 text-xs text-slate-700 font-mono focus:outline-none focus:border-primaryBlue"
                    />
                    {apiKey ? (
                      <Check className="w-4 h-4 text-emerald-500 absolute right-3 top-3" />
                    ) : (
                      <X className="w-4 h-4 text-amber-500 absolute right-3 top-3" />
                    )}
                  </div>
                </div>

                <div className="bg-slate-50 border border-slate-100 p-3 rounded-xl space-y-1">
                  <h4 className="text-xs font-extrabold text-slate-700">Metode Pengujian Keandalan</h4>
                  <p className="text-[11px] text-slate-400 leading-relaxed text-slate-500">
                    Jika kunci API kosong, kami telah menyediakan robot simulator bawaan yang siap mendampingi Anda di tab "Speak" dengan dialog skenario cerdas otomatis!
                  </p>
                </div>
              </div>
            </div>

            {/* Footer copyrights */}
            <div className="text-center pt-4 pb-6 border-t border-slate-100 space-y-1">
              <h4 className="text-xs text-slate-400 font-extrabold">Koreaku.AI Web Applet</h4>
              <p className="text-[10px] text-slate-400 font-medium">© 2026 Koreaku.AI. Terpercaya di Seluruh Indonesia.</p>
            </div>
          </div>
        )}
      </main>

      {/* 4. NAVIGATION BAR AT THE BOTTOM */}
      <nav className="fixed bottom-0 z-40 bg-white border-t border-slate-100 max-w-md w-full mx-auto flex items-center justify-around py-3.5 shadow-xl">
        <button
          onClick={() => setActiveTab('PRACTICE')}
          className={`flex flex-col items-center gap-1 transition-all active:scale-90 ${
            activeTab === 'PRACTICE' ? 'text-primaryBlue' : 'text-slate-400'
          }`}
        >
          <Compass className="w-5 h-5" />
          <span className="text-[10px] font-bold">Misi</span>
        </button>

        <button
          onClick={() => setActiveTab('LESSONS')}
          className={`flex flex-col items-center gap-1 transition-all active:scale-90 ${
            activeTab === 'LESSONS' ? 'text-primaryBlue' : 'text-slate-400'
          }`}
        >
          <Sparkles className="w-5 h-5" />
          <span className="text-[10px] font-bold">Speak Live</span>
        </button>

        <button
          onClick={() => setActiveTab('VOCAB')}
          className={`flex flex-col items-center gap-1 transition-all active:scale-90 ${
            activeTab === 'VOCAB' ? 'text-primaryBlue' : 'text-slate-400'
          }`}
        >
          <BookOpen className="w-5 h-5" />
          <span className="text-[10px] font-bold">Hafalan</span>
        </button>

        <button
          onClick={() => setActiveTab('PROFILE')}
          className={`flex flex-col items-center gap-1 transition-all active:scale-90 ${
            activeTab === 'PROFILE' ? 'text-primaryBlue' : 'text-slate-400'
          }`}
        >
          <User className="w-5 h-5" />
          <span className="text-[10px] font-bold">Landing</span>
        </button>
      </nav>
    </div>
  );
}
