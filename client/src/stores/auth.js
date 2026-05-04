// 用户登录态：基于 Pinia + localStorage 持久化
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

const TOKEN_KEY = 'huaxin_admin_token'
const USERNAME_KEY = 'huaxin_admin_username'

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem(TOKEN_KEY) || '')
  const username = ref(localStorage.getItem(USERNAME_KEY) || '')

  const isLoggedIn = computed(() => !!token.value)

  /** 保存登录信息 */
  function setLogin(t, u) {
    token.value = t
    username.value = u
    localStorage.setItem(TOKEN_KEY, t)
    localStorage.setItem(USERNAME_KEY, u)
  }

  /** 清除登录信息 */
  function logout() {
    token.value = ''
    username.value = ''
    localStorage.removeItem(TOKEN_KEY)
    localStorage.removeItem(USERNAME_KEY)
  }

  return { token, username, isLoggedIn, setLogin, logout }
})
