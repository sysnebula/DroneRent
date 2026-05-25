# 前端项目启动指南

## 🎉 前端项目已创建完成！

我已经为您成功创建了一个完整的 Vue 3 + Element Plus 前端项目。

---

## 📂 项目位置

```
D:\test02\java\DroneRent\frontend\
```

---

## 🚀 快速启动

### 1. 进入项目目录

```bash
cd D:\test02\java\DroneRent\frontend
```

### 2. 启动开发服务器

```bash
npm run dev
```

### 3. 访问应用

打开浏览器访问：**http://localhost:5173**

---

## 📋 默认登录信息

- **用户名**: `admin`
- **密码**: `admin123`

（请确保后端服务已启动并且有此测试账号）

---

## ✨ 已实现的功能

### 1. 用户认证 ✅
- [x] 登录页面
- [x] Token 管理
- [x] 自动路由守卫
- [x] 退出登录

### 2. 首页 ✅
- [x] 统计卡片展示
- [x] 系统介绍
- [x] 功能列表

### 3. 无人机管理 ✅
- [x] 分页查询
- [x] 按品牌、型号、状态筛选
- [x] 新增无人机
- [x] 编辑无人机
- [x] 删除无人机
- [x] 状态标签显示

### 4. 订单管理 ✅
- [x] 订单列表
- [x] 按状态筛选
- [x] 查看订单详情
- [x] 归还订单
- [x] 取消订单
- [x] 状态标签显示

### 5. 布局系统 ✅
- [x] 侧边栏导航菜单
- [x] 顶部用户信息栏
- [x] 响应式布局
- [x] 路由跳转

---

## 🎨 技术特性

### 1. 现代化技术栈
- ✅ Vue 3 Composition API
- ✅ TypeScript 类型安全
- ✅ Vite 快速构建
- ✅ Element Plus UI 组件库
- ✅ Pinia 状态管理
- ✅ Vue Router 路由

### 2. HTTP 请求封装
- ✅ Axios 拦截器
- ✅ 自动携带 Token
- ✅ 统一错误处理
- ✅ 401 自动跳转登录

### 3. 代码组织
- ✅ API 接口统一管理
- ✅ 组件化开发
- ✅ 路由懒加载
- ✅ 环境变量配置

---

## 📁 项目结构

```
frontend/
├── src/
│   ├── api/              # API 接口定义
│   │   ├── auth.ts       # 认证接口
│   │   ├── drone.ts      # 无人机接口
│   │   └── order.ts      # 订单接口
│   ├── router/           # 路由配置
│   │   └── index.ts
│   ├── stores/           # 状态管理
│   │   └── user.ts       # 用户状态
│   ├── utils/            # 工具函数
│   │   └── request.ts    # Axios 封装
│   ├── views/            # 页面组件
│   │   ├── Login.vue     # 登录页
│   │   ├── Layout.vue    # 主布局
│   │   ├── HomeView.vue  # 首页
│   │   ├── Drones.vue    # 无人机管理
│   │   └── Orders.vue    # 订单管理
│   ├── App.vue           # 根组件
│   └── main.ts           # 入口文件
├── .env.development      # 开发环境配置
├── .env.production       # 生产环境配置
├── package.json
├── vite.config.ts
└── FRONTEND_README.md
```

---

## 🔧 常用命令

### 开发

```bash
# 启动开发服务器
npm run dev

# 安装依赖
npm install

# 添加新依赖
npm install <package-name>
```

### 构建

```bash
# 构建生产版本
npm run build

# 预览生产构建
npm run preview

# 类型检查
npm run type-check

# 代码格式化
npm run format
```

---

## 🌐 API 配置

### 开发环境

API 地址：`http://localhost:8080/api`

配置文件：`.env.development`

```
VITE_API_BASE_URL=http://localhost:8080/api
```

### 生产环境

配置文件：`.env.production`

```
VITE_API_BASE_URL=/api
```

---

## ⚠️ 注意事项

### 1. 后端服务

前端需要后端 API 支持，请确保：
- ✅ 后端服务已启动（http://localhost:8080）
- ✅ CORS 跨域已配置
- ✅ 数据库中有测试数据

### 2. 端口配置

- 前端默认端口：`5173`
- 后端默认端口：`8080`

如果端口冲突，可以在 `vite.config.ts` 中修改：

```typescript
export default defineConfig({
  server: {
    port: 3000  // 修改端口
  }
})
```

### 3. 浏览器要求

推荐使用现代浏览器：
- Chrome 90+
- Firefox 88+
- Edge 90+
- Safari 14+

---

## 🐛 常见问题

### 1. 登录后无法访问页面

**原因**: Token 未正确保存或后端 API 返回格式不正确

**解决**: 
- 检查浏览器控制台是否有错误
- 确认后端返回的数据格式为 `{ code: 200, data: {...} }`
- 检查 localStorage 中是否有 token

### 2. API 请求失败

**原因**: 后端服务未启动或地址配置错误

**解决**:
- 确认后端服务正在运行
- 检查 `.env.development` 中的 API 地址
- 查看浏览器 Network 面板的请求信息

### 3. 页面空白

**原因**: 编译错误或路由配置问题

**解决**:
- 查看浏览器控制台错误信息
- 检查终端是否有编译错误
- 运行 `npm run type-check` 检查类型错误

---

## 📝 下一步开发建议

### 短期优化
1. 完善表单验证和错误提示
2. 添加 loading 状态
3. 优化用户体验（消息提示、确认对话框）
4. 添加数据字典管理

### 中期目标
1. 实现客户管理模块
2. 实现维修记录模块
3. 实现财务统计模块
4. 添加图表展示（ECharts）

### 长期规划
1. 移动端适配
2. PWA 支持
3. 国际化（i18n）
4. 主题定制
5. 性能优化

---

## 🎯 与后端联调

### 1. 确保后端已启动

```bash
# 在后端项目根目录
cd D:\test02\java\DroneRent
mvn spring-boot:run
```

### 2. 测试登录接口

访问 http://localhost:5173/login

输入：
- 用户名：`admin`
- 密码：`admin123`

### 3. 测试数据接口

登录后应该能够：
- ✅ 查看无人机列表
- ✅ 查看订单列表
- ✅ 执行 CRUD 操作

---

## 📞 技术支持

如有问题，请查看：
- [FRONTEND_README.md](./FRONTEND_README.md) - 详细的项目文档
- [Vue 3 官方文档](https://cn.vuejs.org/)
- [Element Plus 文档](https://element-plus.org/zh-CN/)
- [Vite 官方文档](https://cn.vitejs.dev/)

---

## 🎊 总结

✅ **Vue 3 项目已创建完成**  
✅ **Element Plus 已配置**  
✅ **Axios 已封装**  
✅ **路由已配置**  
✅ **登录页面已完成**  
✅ **无人机管理页面已完成**  
✅ **订单管理页面已完成**  
✅ **状态管理已完成**  

**现在可以启动项目并开始使用了！** 🚀

启动命令：
```bash
cd D:\test02\java\DroneRent\frontend
npm run dev
```

访问：http://localhost:5173
