import { defineStore } from 'pinia'
import { ref, watch } from 'vue'

export const useThemeStore = defineStore('theme', () => {
  const isDark = ref(localStorage.getItem('theme') === 'dark')
  
  const applyTheme = () => {
    if (isDark.value) {
      document.documentElement.classList.add('dark')
      document.body.classList.add('dark')
      localStorage.setItem('theme', 'dark')
    } else {
      document.documentElement.classList.remove('dark')
      document.body.classList.remove('dark')
      localStorage.setItem('theme', 'light')
    }
  }
  
  const toggleTheme = () => {
    isDark.value = !isDark.value
    applyTheme()
  }
  
  const setDark = (value) => {
    isDark.value = value
    applyTheme()
  }
  
  applyTheme()
  
  return {
    isDark,
    toggleTheme,
    setDark,
    applyTheme
  }
})
