<template>
  <div class="layout-container" :class="{ 'dark-mode': themeStore.isDark }">
    <div class="safe-area-top"></div>
    <header class="header">
      <div class="header-content">
        <div class="logo" @click="$router.push('/')">
          <h1>🍵 LinguaLearn</h1>
          <span class="logo-text" v-if="!isMobile">多语种学习平台</span>
        </div>

        <nav class="nav" :class="{ 'nav-mobile': isMobile }">
          <router-link to="/home" class="nav-item">
            <el-icon><HomeFilled /></el-icon>
            <span>首页</span>
          </router-link>
          <router-link to="/course" class="nav-item">
            <el-icon><Reading /></el-icon>
            <span>课程</span>
          </router-link>
          <router-link to="/learning" class="nav-item" v-if="userStore.isLoggedIn">
            <el-icon><Monitor /></el-icon>
            <span>学习</span>
          </router-link>
          <router-link to="/challenge" class="nav-item" v-if="userStore.isLoggedIn">
            <el-icon><Aim /></el-icon>
            <span>天梯</span>
          </router-link>
          <router-link to="/community" class="nav-item">
            <el-icon><ChatDotRound /></el-icon>
            <span>社区</span>
          </router-link>
        </nav>

        <div class="user-area">
          <el-tooltip :content="themeStore.isDark ? '切换到日间模式' : '切换到夜间模式'">
            <el-button 
              :icon="themeStore.isDark ? 'Sunny' : 'Moon'" 
              circle 
              @click="themeStore.toggleTheme()"
              class="theme-btn"
            />
          </el-tooltip>
          
          <template v-if="userStore.isLoggedIn">
            <el-dropdown @command="handleUserCommand">
              <span class="user-info">
                <el-avatar :size="isMobile ? 28 : 32" :src="userStore.avatar">
                  {{ userStore.nickname?.charAt(0) || 'U' }}
                </el-avatar>
                <span class="username" v-if="!isMobile">{{ userStore.nickname || '用户' }}</span>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">
                    <el-icon><User /></el-icon>个人中心
                  </el-dropdown-item>
                  <el-dropdown-item command="settings">
                    <el-icon><Setting /></el-icon>设置
                  </el-dropdown-item>
                  <el-dropdown-item command="logout" divided>
                    <el-icon><SwitchButton /></el-icon>退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <el-button type="primary" @click="$router.push('/login')" class="login-btn">
              <span v-if="isMobile">登</span>
              <span v-else>登录</span>
            </el-button>
            <el-button @click="$router.push('/register')" class="register-btn" v-if="!isMobile">
              注册
            </el-button>
          </template>
        </div>
      </div>
    </header>

    <main class="main-content">
      <router-view />
    </main>

    <footer class="footer">
      <p>🍵 2026 LinguaLearn - 多语种在线教育平台</p>
    </footer>
    <div class="safe-area-bottom"></div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useThemeStore } from '@/stores/theme'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const themeStore = useThemeStore()

const isMobile = ref(false)

const checkMobile = () => {
  isMobile.value = window.innerWidth < 768
}

onMounted(() => {
  checkMobile()
  window.addEventListener('resize', checkMobile)
})

onUnmounted(() => {
  window.removeEventListener('resize', checkMobile)
})

const handleUserCommand = (command) => {
  switch (command) {
    case 'profile':
      router.push('/profile')
      break
    case 'settings':
      router.push('/settings')
      break
    case 'logout':
      userStore.logout()
      ElMessage.success('已退出登录')
      router.push('/login')
      break
  }
}
</script>

<style lang="scss" scoped>
.layout-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  
  &.dark-mode {
    background-color: #1a2a2a;
    color: #d0e0d8;
    
    .header {
      background-color: rgba(26, 42, 42, 0.95);
      box-shadow: 0 4px 16px rgba(0, 0, 0, 0.3);
      backdrop-filter: blur(10px);
      
      .logo h1 {
        color: #98D8C8;
        background: none;
        -webkit-text-fill-color: #98D8C8;
      }
      
      .logo-text {
        color: #98D8C8;
        background: none;
        -webkit-text-fill-color: #98D8C8;
      }
      
      .nav .nav-item {
        color: #b0c0b8;
        
        &:hover {
          color: #98D8C8;
          background-color: rgba(152, 216, 200, 0.1);
        }
        
        &.router-link-active {
          color: #98D8C8;
          background-color: rgba(152, 216, 200, 0.15);
        }
      }
      
      .user-area {
        .theme-btn {
          background: rgba(152, 216, 200, 0.15) !important;
          color: #98D8C8 !important;
        }
        
        .user-info {
          &:hover {
            background-color: rgba(152, 216, 200, 0.1);
          }
          
          .username {
            color: #d0e0d8;
          }
        }
        
        .login-btn {
          background: linear-gradient(135deg, #98D8C8 0%, #6BA899 100%) !important;
          color: white !important;
        }
        
        .register-btn {
          background: #2a3a3a !important;
          color: #98D8C8 !important;
          border: 2px solid #3a4a4a !important;
          
          &:hover {
            background: #354545 !important;
            border-color: #98D8C8 !important;
          }
        }
      }
    }
    
    .main-content {
      background-color: #1a2a2a;
    }
    
    .footer {
      background: linear-gradient(135deg, #1e2e2e 0%, #253535 100%);
      color: #98D8C8;
      box-shadow: 0 -4px 16px rgba(0, 0, 0, 0.3);
    }
    
    .safe-area-top {
      background-color: #1a2a2a;
    }
    
    .safe-area-bottom {
      background: linear-gradient(135deg, #1e2e2e 0%, #253535 100%);
    }
  }
}

.safe-area-top {
  height: env(safe-area-inset-top);
  background-color: #F0FFF5;
  flex-shrink: 0;
}

.safe-area-bottom {
  height: env(safe-area-inset-bottom);
  background: linear-gradient(135deg, #98D8C8 0%, #ADD8E6 100%);
  flex-shrink: 0;
}

.header {
  background: rgba(255, 255, 255, 0.98);
  box-shadow: 0 4px 16px rgba(152, 216, 200, 0.2);
  position: sticky;
  top: env(safe-area-inset-top);
  z-index: 100;
  backdrop-filter: blur(10px);

  .header-content {
    max-width: 1200px;
    margin: 0 auto;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 16px;
    height: 60px;
  }

  .logo {
    cursor: pointer;
    display: flex;
    align-items: center;
    gap: 8px;
    
    h1 {
      font-size: 20px;
      background: linear-gradient(135deg, #98D8C8 0%, #6BA899 100%);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
      margin: 0;
    }
    
    .logo-text {
      font-size: 12px;
      color: #98D8C8;
      background: linear-gradient(135deg, #ADD8E6 0%, #98D8C8 100%);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
    }
  }

  .nav {
    display: flex;
    gap: 8px;

    .nav-item {
      display: flex;
      align-items: center;
      gap: 4px;
      text-decoration: none;
      color: #2D5A4A;
      padding: 8px 16px;
      border-radius: 20px;
      font-size: 14px;
      font-weight: 500;
      transition: all 0.3s ease;

      &:hover {
        color: #6BA899;
        background: rgba(152, 216, 200, 0.15);
        transform: translateY(-2px);
      }

      &.router-link-active {
        color: #6BA899;
        background: rgba(152, 216, 200, 0.2);
        box-shadow: 0 4px 12px rgba(152, 216, 200, 0.2);
      }
    }
  }

  .user-area {
    display: flex;
    align-items: center;
    gap: 8px;

    .theme-btn {
      background: rgba(152, 216, 200, 0.2) !important;
      color: #6BA899 !important;
      border: none !important;
      padding: 4px;
      
      &:hover {
        background: rgba(152, 216, 200, 0.3) !important;
        transform: rotate(15deg);
      }
    }

    .user-info {
      display: flex;
      align-items: center;
      gap: 6px;
      cursor: pointer;
      padding: 6px 12px;
      border-radius: 20px;
      transition: all 0.3s ease;

      &:hover {
        background: rgba(152, 216, 200, 0.15);
      }

      .username {
        color: #2D5A4A;
        font-size: 14px;
        font-weight: 500;
      }
    }
    
    .login-btn {
      background: linear-gradient(135deg, #98D8C8 0%, #6BA899 100%) !important;
      box-shadow: 0 4px 12px rgba(152, 216, 200, 0.3);
      border: none !important;
      color: white !important;
      font-weight: 500;
      
      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 16px rgba(152, 216, 200, 0.4);
      }
    }
    
    .register-btn {
      background: white !important;
      color: #6BA899 !important;
      border: 2px solid #98D8C8 !important;
      font-weight: 500;
      
      &:hover {
        background: rgba(152, 216, 200, 0.1) !important;
        border-color: #6BA899 !important;
        transform: translateY(-2px);
      }
    }
  }
}

.main-content {
  flex: 1;
  max-width: 1200px;
  width: 100%;
  margin: 0 auto;
  padding: 16px;
  background: #F0FFF5;
}

.footer {
  background: linear-gradient(135deg, #98D8C8 0%, #ADD8E6 100%);
  color: #2D5A4A;
  text-align: center;
  padding: 16px;
  margin-top: auto;
  font-size: 12px;
  font-weight: 500;
  box-shadow: 0 -4px 16px rgba(152, 216, 200, 0.2);
}

@media (max-width: 768px) {
  .header {
    .header-content {
      padding: 0 12px;
      height: 50px;
    }

    .logo {
      h1 {
        font-size: 16px;
      }
      
      .logo-text {
        display: none;
      }
    }

    .nav {
      gap: 2px;
      
      .nav-item {
        padding: 6px 10px;
        font-size: 12px;
        border-radius: 16px;
        
        :deep(.el-icon) {
          width: 16px;
          height: 16px;
        }
        
        span {
          font-size: 11px;
        }
      }
    }

    .user-area {
      gap: 4px;
      
      .theme-btn {
        width: 32px;
        height: 32px;
        padding: 0;
      }
      
      .login-btn {
        padding: 4px 8px;
        font-size: 12px;
      }
    }
  }

  .main-content {
    padding: 12px;
  }

  .footer {
    padding: 12px;
    font-size: 11px;
  }
}

@media (max-width: 480px) {
  .header {
    .header-content {
      padding: 0 8px;
    }

    .nav {
      .nav-item span {
        display: none;
      }
      
      .nav-item {
        padding: 6px 8px;
      }
    }

    .user-area {
      .login-btn span {
        font-size: 11px;
      }
    }
  }
}
</style>
