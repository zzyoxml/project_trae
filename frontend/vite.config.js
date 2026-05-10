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
      '/api': {
        target: 'http://0.0.0.0:3001',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      },
      '/courses': {
        target: 'http://0.0.0.0:3001',
        changeOrigin: true
      },
      '/vocabulary': {
        target: 'http://0.0.0.0:3001',
        changeOrigin: true
      },
      '/grammar': {
        target: 'http://0.0.0.0:3001',
        changeOrigin: true
      },
      '/achievements': {
        target: 'http://0.0.0.0:3001',
        changeOrigin: true
      },
      '/badges': {
        target: 'http://0.0.0.0:3001',
        changeOrigin: true
      },
      '/posts': {
        target: 'http://0.0.0.0:3001',
        changeOrigin: true
      },
      '/users': {
        target: 'http://0.0.0.0:3001',
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
