# 无人机租赁管理系统 - 前端

这是一个基于 Vue 3 + Element Plus 的无人机租赁管理前端项目。

## 📦 技术栈

- **Vue 3** - 渐进式 JavaScript 框架
- **TypeScript** - JavaScript 的超集
- **Element Plus** - Vue 3 组件库
- **Pinia** - Vue 状态管理
- **Vue Router** - Vue 官方路由
- **Axios** - HTTP 客户端
- **Vite** - 下一代前端构建工具

## 🚀 快速开始

### 安装依赖

```bash
npm install
```

### 启动开发服务器

```bash
npm run dev
```

访问 http://localhost:5173

### 构建生产版本

```bash
npm run build
```

### 预览生产构建

```bash
npm run preview
```

## 📁 项目结构

```
frontend/
├── src/
│   ├── api/              # API 接口
│   │   ├── auth.ts       # 认证相关接口
│   │   ├── drone.ts      # 无人机相关接口
│   │   └── order.ts      # 订单相关接口
│   ├── router/           # 路由配置
│   │   └── index.ts
│   ├── stores/           # Pinia 状态管理
│   │   └── user.ts       # 用户状态
│   ├── utils/            # 工具函数
│   │   └── request.ts    # Axios 封装
│   ├── views/            # 页面组件
│   │   ├── Login.vue     # 登录页
│   │   ├── Layout.vue    # 布局页
│   │   ├── HomeView.vue  # 首页
│   │   ├── Drones.vue    # 无人机管理
│   │   └── Orders.vue    # 订单管理
│   ├── App.vue           # 根组件
│   └── main.ts           # 入口文件
├── .env.development      # 开发环境变量
├── .env.production       # 生产环境变量
├── package.json
└── vite.config.ts
```

## 🔧 配置

### 环境变量

**开发环境** (`.env.development`):
```
VITE_API_BASE_URL=http://localhost:8080/api
```

**生产环境** (`.env.production`):
```
VITE_API_BASE_URL=/api
```

### API 代理

在 `vite.config.ts` 中配置代理（如果需要）：

```typescript
export default defineConfig({
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})
```

## 📝 功能模块

### 1. 认证模块
- ✅ 用户登录
- ✅ Token 管理
- ✅ 自动跳转

### 2. 无人机管理
- ✅ 列表查询（支持筛选）
- ✅ 新增无人机
- ✅ 编辑无人机
- ✅ 删除无人机
- ✅ 分页显示

### 3. 订单管理
- ✅ 订单列表
- ✅ 订单详情
- ✅ 归还订单
- ✅ 取消订单
- ✅ 分页显示

## 🎨 界面截图

### 登录页
![登录页](./screenshots/login.png)

### 首页
![首页](./screenshots/home.png)

### 无人机管理
![无人机管理](./screenshots/drones.png)

### 订单管理
![订单管理](./screenshots/orders.png)

## 🔐 权限控制

系统使用 JWT Token 进行身份验证：

1. 用户登录后获取 Token
2. Token 存储在 localStorage 中
3. 每次请求自动携带 Token
4. Token 过期自动跳转到登录页

## 🌐 API 接口

所有 API 接口定义在 `src/api/` 目录下：

- `auth.ts` - 认证相关（登录、登出、获取用户信息）
- `drone.ts` - 无人机管理（CRUD、状态更新）
- `order.ts` - 订单管理（CRUD、续租、归还、取消）

## 📦 部署

### Nginx 部署示例

```nginx
server {
    listen 80;
    server_name your-domain.com;
    
    root /usr/share/nginx/html;
    index index.html;
    
    location / {
        try_files $uri $uri/ /index.html;
    }
    
    location /api {
        proxy_pass http://backend-server:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

## 🛠️ 开发指南

### 添加新页面

1. 在 `src/views/` 下创建新组件
2. 在 `src/router/index.ts` 中添加路由
3. 在 `src/views/Layout.vue` 中添加菜单项

### 添加新 API

1. 在 `src/api/` 下创建新的 API 文件
2. 使用 `request.ts` 中的 axios 实例
3. 在组件中导入并使用

### 状态管理

使用 Pinia 进行状态管理：

```typescript
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
userStore.login(username, password)
```

## 📄 许可证

MIT License

## 👥 贡献

欢迎提交 Issue 和 Pull Request！

---

**开发完成时间**: 2024-01-01  
**版本**: 1.0.0
