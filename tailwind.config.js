/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        primaryBlue: '#2563EB',
        coralVibrant: '#F43F5E',
        successGreen: '#10B981',
        softSky: '#EFF6FF',
        surfaceBg: '#F8FAFC',
        inkBlack: '#0F172A',
        onSurfaceVariantText: '#475569',
      },
      fontFamily: {
        sans: ['Inter', 'sans-serif'],
      }
    },
  },
  plugins: [],
}
