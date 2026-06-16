export interface ChatMessage {
  id: number;
  sender: 'tutor' | 'user';
  text: string;
  translation?: string;
  correction?: string;
  correctionLabel?: string;
  timestamp: number;
}

export interface VocabItem {
  id: number;
  word: string;
  pronunciation: string;
  translation: string;
  sentenceKorean: string;
  sentenceIndonesian: string;
  pronunciationGuide: string;
  isMastered: boolean;
  streakCount: number;
}

export interface UserProgress {
  currentDay: number;
  streakDays: number;
  completedDaysCsv: string; // "1,2,3...14"
  score: number; // representation (e.g. 46)
}

export interface TutorResponse {
  replyKorean: string;
  replyIndonesian: string;
  hasCorrection: boolean;
  wrongPart?: string | null;
  correctedPart?: string | null;
  correctionExplanation?: string | null;
}
