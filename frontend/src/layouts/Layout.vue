<template>
  <div class="layout-container" :class="{ 'dark-mode': themeStore.isDark }">
    <div class="safe-area-top"></div>
    <header class="header">
      <div class="header-content">
        <div class="logo" @click="$router.push('/')">
          <h1>📚 LinguaLearn</h1>
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
                <el-avatar :size="isMobile ? 28 : 32">{{ userStore.nickname?.charAt(0) || 'U' }}</el-avatar>
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
      <p>&copy; 2026 LinguaLearn - 多语种在线教育平台</p>
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
    background-color: #1a1a1a;
    color: #e0e0e0;
    
    .header {
      background-color: #2d2d2d;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
      
      .logo h1, .logo-text {
        color: #409eff;
      }
      
      .nav .nav-item {
        color: #b0b0b0;
        
        &:hover {
          color: #409eff;
          background-color: #3a3a3a;
        }
        
        &.router-link-active {
          color: #409eff;
          background-color: #3a3a3a;
        }
      }
      
      .user-area .user-info {
        .username {
          color: #b0b0b0;
        }
        
        &:hover {
          background-color: #3a3a3a;
        }
      }
    }
    
    .main-content {
      background-color: #1a1a1a;
    }
    
    .footer {
      background-color: #1a1a1a;
      border-top: 1px solid #3a3a3a;
    }
    
    .safe-area-top, .safe-area-bottom {
      background-color: #1a1a1a;
    }
  }
}

.safe-area-top {
  height: env(safe-area-inset-top);
  background-color: #fff;
  flex-shrink: 0;
}

.safe-area-bottom {
  height: env(safe-area-inset-bottom);
  background-color: #303133;
  flex-shrink: 0;
}

.header {
  background: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: env(safe-area-inset-top);
  z-index: 100;

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
      color: #409eff;
      margin: 0;
    }
    
    .logo-text {
      font-size: 12px;
      color: #999;
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
      color: #606266;
      padding: 6px 12px;
      border-radius: 4px;
      font-size: 14px;
      transition: all 0.3s;

      &:hover {
        color: #409eff;
        background: #ecf5ff;
      }

      &.router-link-active {
        color: #409eff;
        background: #ecf5ff;
      }
    }
  }

  .user-area {
    display: flex;
    align-items: center;
    gap: 8px;

    .theme-btn {
      padding: 4px;
    }

    .user-info {
      display: flex;
      align-items: center;
      gap: 6px;
      cursor: pointer;
      padding: 4px 8px;
      border-radius: 4px;

      &:hover {
        background: #f5f7fa;
      }

      .username {
        color: #606266;
        font-size: 14px;
      }
    }
    
    .login-btn {
      padding: 6px 12px;
      font-size: 14px;
    }
    
    .register-btn {
      padding: 6px 12px;
      font-size: 14px;
    }
  }
}

.main-content {
  flex: 1;
  max-width: 1200px;
  width: 100%;
  margin: 0 auto;
  padding: 16px;
}

.footer {
  background: #303133;
  color: white;
  text-align: center;
  padding: 16px;
  margin-top: auto;
  font-size: 12px;
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
        padding: 4px 8px;
        font-size: 12px;
        
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
        padding: 4px 8px;
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
