import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'
import { resolve } from 'path'

export default defineConfig({
  base: '/',
  
  plugins: [
    vue(),
    AutoImport({
      imports: ['vue', 'vue-router', 'pinia'],
      resolvers: [ElementPlusResolver()],
      dts: 'src/auto-imports.d.ts'
    }),
    Components({
      resolvers: [ElementPlusResolver()],
      dts: 'src/components.d.ts'
    })
  ],
  
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src'),
      '~': resolve(__dirname, 'src')
    },
    extensions: ['.js', '.ts', '.jsx', '.tsx', '.json', '.vue']
  },
  
  css: {
    preprocessorOptions: {
      scss: {
        additionalData: `@use "@/styles/variables.scss" as *;`
      }
    }
  },
  
  server: {
    port: 3000,
    host: '0.0.0.0',
    open: false,
    cors: true,
    allowedHosts: true,
    proxy: {
      '/ruoyi-api': {
        target: 'http://localhost:6666',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/ruoyi-api/, '')
      },
      '/login': {
        target: 'http://localhost:6666',
        changeOrigin: true
      },
      '/register': {
        target: 'http://localhost:6666',
        changeOrigin: true
      },
      '/logout': {
        target: 'http://localhost:6666',
        changeOrigin: true
      },
      '/getInfo': {
        target: 'http://localhost:6666',
        changeOrigin: true
      },
      '/profile': {
        target: 'http://localhost:6666',
        changeOrigin: true
      },
      '/edu': {
        target: 'http://localhost:6666',
        changeOrigin: true
      },
      '/api': {
        target: 'http://localhost:3002',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      },
      '/courses': {
        target: 'http://localhost:3002',
        changeOrigin: true
      },
      '/vocabulary': {
        target: 'http://localhost:3002',
        changeOrigin: true
      },
      '/grammar': {
        target: 'http://localhost:3002',
        changeOrigin: true
      },
      '/achievements': {
        target: 'http://localhost:3002',
        changeOrigin: true
      },
      '/badges': {
        target: 'http://localhost:3002',
        changeOrigin: true
      },
      '/posts': {
        target: 'http://localhost:3002',
        changeOrigin: true
      },
      '/users': {
        target: 'http://localhost:3002',
        changeOrigin: true
      }
    }
  },
  
  build: {
    outDir: 'dist',
    assetsDir: 'static',
    sourcemap: false,
    chunkSizeWarningLimit: 1500,
    rollupOptions: {
      output: {
        manualChunks: {
          'element-plus': ['element-plus'],
          'echarts': ['echarts'],
          'vendor': ['vue', 'vue-router', 'pinia', 'axios']
        }
      }
    }
  },
  
  optimizeDeps: {
    include: ['vue', 'vue-router', 'pinia', 'axios', 'element-plus', 'echarts']
  }
})
