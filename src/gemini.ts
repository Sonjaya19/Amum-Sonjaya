import { ChatMessage, TutorResponse } from './types';

export async function consultGemini(
  conversationHistory: ChatMessage[],
  currentTopic: string,
  customApiKey: string | null
): Promise<TutorResponse> {
  // Determine API Key from custom/local storage input, or Vite env variable
  const apiKey = customApiKey || (import.meta.env.VITE_GEMINI_API_KEY as string) || '';

  if (!apiKey || apiKey === 'MY_GEMINI_API_KEY') {
    // Return simulator response if API Key is not set, making the trial experience 100% functional
    return getSimulatorResponse(conversationHistory, currentTopic);
  }

  const systemPrompt = `
    Anda adalah Eonni AI, seorang tutor bahasa Korea interaktif yang membimbing pelajar Indonesia secara natural.
    Saat ini topiknya adalah: "${currentTopic}".
    
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
  `.trim();

  // Map history to Gemini contents structure
  const contents = conversationHistory.map((msg) => {
    const textContent =
      msg.sender === 'user' ? msg.text : `${msg.text}\n${msg.translation || ''}`;
    return {
      role: msg.sender === 'user' ? 'user' : 'model',
      parts: [{ text: textContent }],
    };
  });

  const requestBody = {
    contents: contents,
    generationConfig: {
      responseMimeType: 'application/json',
      temperature: 0.7,
      maxOutputTokens: 800,
    },
    systemInstruction: {
      parts: [{ text: systemPrompt }],
    },
  };

  try {
    const response = await fetch(
      `https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=${apiKey}`,
      {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestBody),
      }
    );

    if (!response.ok) {
      throw new Error(`HTTP Error Status: ${response.status}`);
    }

    const data = await response.json();
    const jsonText = data.candidates?.[0]?.content?.parts?.[0]?.text;

    if (jsonText) {
      const parsed: TutorResponse = JSON.parse(jsonText);
      return parsed;
    } else {
      return fallbackTutorResponse('Format tanggapan model kosong.');
    }
  } catch (error: unknown) {
    console.error('Gemini call failed:', error);
    const msg = error instanceof Error ? error.message : String(error);
    return fallbackTutorResponse(`Kesalahan Koneksi API: ${msg}`);
  }
}

function fallbackTutorResponse(errorMessage: string): TutorResponse {
  return {
    replyKorean: '네, 알겠습니다. 다시 말씀해주세요.',
    replyIndonesian: `(Baiklah. Bisakah Anda mengatakannya kembali? - ${errorMessage})`,
    hasCorrection: false,
    wrongPart: null,
    correctedPart: null,
    correctionExplanation: null,
  };
}

// Client-side simulation of conversation responses if Gemini Key is omitted
function getSimulatorResponse(history: ChatMessage[], topic: string): TutorResponse {
  const lastUserMsg = [...history].reverse().find((m) => m.sender === 'user')?.text || '';
  const cleanUserMsg = lastUserMsg.toLowerCase().trim();

  const isCafe = topic.toLowerCase().includes('caf') || topic.toLowerCase().includes('kopi');

  if (isCafe) {
    if (cleanUserMsg.includes('annyeong') || cleanUserMsg.includes('halo') || cleanUserMsg.includes('hi')) {
      return {
        replyKorean: '안녕하세요! 주문하시겠어요? 아메리카노와 라떼가 준비되어 있습니다.',
        replyIndonesian: 'Halo! Apakah Anda ingin memesan? Kami menyediakan americano dan latte.',
        hasCorrection: false,
      };
    }
    if (cleanUserMsg.includes('ice') || cleanUserMsg.includes('es') || cleanUserMsg.includes('아메리카노')) {
      return {
        replyKorean: '아이스 아메리카노 한 잔 맞으신가요? 사이즈는 레귤러, 라지 중 어떤 것으로 드릴까요?',
        replyIndonesian: 'Satu gelas es americano, betul? Untuk ukurannya ingin yang reguler atau large?',
        hasCorrection: cleanUserMsg.includes('ice americano'),
        wrongPart: cleanUserMsg.includes('ice americano') ? 'ice americano' : null,
        correctedPart: '아이스 아메리카노 (A-i-seu A-me-ri-ka-no)',
        correctionExplanation: 'Bahasa Korea menyerap kata "Ice" dalam bentuk Hangul fonetis menjadi "아이스" (A-i-seu). Pengucapannya adalah "Aiseu Americano"!',
      };
    }
    if (cleanVowelTypos(cleanUserMsg)) {
      return {
        replyKorean: '네! 여기 주문하신 시원한 음료와 빵입니다. 맛있게 드세요! 더 필요한 것이 있으신가요?',
        replyIndonesian: 'Baik! Ini minuman dingin dan roti yang Anda pesan. Selamat menikmati! Apakah ada hal lain yang Anda butuhkan?',
        hasCorrection: true,
        wrongPart: 'gamsahamnida',
        correctedPart: '감사합니다 (Gam-sa-ham-ni-da)',
        correctionExplanation: 'Pelafalan fonetik vokal "hamnida" menggunakan dasar Hangul "합니다". Huruf "ㅂ" (b) di "합" berubah bunyi menjadi "m" saat bertemu dengan "ㄴ" (n) di "니". Pengucapan yang tepat: Gam-sa-ham-ni-da!',
      };
    }
    return {
      replyKorean: '네, 알겠습니다. 따뜻하게 준비해 드릴까요, 아니면 시원하게 제공해 드릴까요?',
      replyIndonesian: 'Baik, saya mengerti. Mau disajikan hangat atau dingin?',
      hasCorrection: false,
    };
  } else {
    // Topic: Subject Markers of Korean e.g. -이 / -가
    if (cleanUserMsg.includes('goga') || cleanUserMsg.includes('sec') || cleanUserMsg.includes('bap')) {
      return {
        replyKorean: '정말 잘하셨어요! "밥이 맛있어요" (Nasi enak) menggunakan penanda subjek "이" karena berakhiran konsonan 받침.',
        replyIndonesian: 'Hebat sekali! Kalimat tersebut menggunakan penanda "-이" karena kata benda berakhiran konsonan.',
        hasCorrection: false,
      };
    }
    return {
      replyKorean: '아주 흥미롭네요! 한국어 주격 조사(-이/-가)를 더 연습해보겠습니다. 질문에 맞춰 "가" atau "이"를 넣어보세요.',
      replyIndonesian: 'Menarik sekali! Mari berlatih penanda subjek lagi. Pilih di antara "가" atau "이" yang sesuai.',
      hasCorrection: false,
    };
  }
}

function cleanVowelTypos(text: string): boolean {
  return text.includes('tang') || text.includes('gams') || text.includes('kasih') || text.includes('makasih');
}
