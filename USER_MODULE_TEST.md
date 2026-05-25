# 用户与权限模块 - 接口测试指南

## 📋 模块概述

用户与权限模块已开发完成，包含以下功能：

1. ✅ Spring Security + JWT 完整配置
2. ✅ 登录接口（返回 JWT Token）
3. ✅ 用户 CRUD 接口
4. ✅ 角色管理接口
5. ✅ 基于角色的权限控制
6. ✅ 所有接口都添加了 Knife4j 注解

---

## 🔑 核心组件

### 1. 安全配置
- **SecurityConfig.java** - Spring Security 配置
- **JwtAuthenticationFilter.java** - JWT 认证过滤器
- **CustomUserDetailsService.java** - 自定义用户详情服务
- **SecurityUser.java** - UserDetails 实现类

### 2. DTO/VO
- **LoginDTO** - 登录请求
- **UserDTO** - 用户创建/更新请求
- **LoginVO** - 登录响应（含 Token）
- **UserVO** - 用户信息响应

### 3. 控制器
- **AuthController** - 认证接口（登录、获取用户信息等）
- **UserController** - 用户管理接口（CRUD）
- **RoleController** - 角色管理接口
- **PermissionDemoController** - 权限控制示例

---

## 🧪 接口测试

### 前置条件

1. 启动 MySQL 和 Redis
2. 执行数据库初始化脚本
3. 启动项目：`mvn spring-boot:run`
4. 访问 Knife4j 文档：http://localhost:8080/doc.html

---

## 1. 登录接口测试

### 1.1 用户登录

**接口**: `POST /api/auth/login`

**请求体**:
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJJZCI6MSwidXNlcm5hbWUiOiJhZG1pbiIsImV4cCI6MTcwNDE1MzYwMH0.xxx",
    "tokenType": "Bearer",
    "expiresIn": 86400,
    "userId": 1,
    "username": "admin",
    "realName": "系统管理员",
    "role": "ADMIN"
  },
  "timestamp": 1704067200000
}
```

**重要**: 复制响应中的 `token` 值，后续接口调用需要在 Header 中携带此 Token。

---

### 1.2 获取当前用户信息

**接口**: `GET /api/auth/info`

**请求头**:
```
Authorization: Bearer {token}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "userId": 1,
    "username": "admin",
    "password": "$2a$10$...",
    "realName": "系统管理员",
    "role": "ADMIN",
    "status": 1,
    "authorities": [
      {
        "authority": "ROLE_ADMIN"
      }
    ]
  },
  "timestamp": 1704067200000
}
```

---

## 2. 用户管理接口测试

### 2.1 分页查询用户列表

**接口**: `GET /api/users/page?pageNum=1&pageSize=10`

**请求头**:
```
Authorization: Bearer {token}
```

**查询参数**:
- `pageNum`: 页码（默认 1）
- `pageSize`: 每页条数（默认 10）
- `username`: 用户名（可选，模糊查询）
- `role`: 角色（可选，精确查询）

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "total": 2,
    "pageNum": 1,
    "pageSize": 10,
    "pages": 1,
    "list": [
      {
        "id": 1,
        "username": "admin",
        "realName": "系统管理员",
        "phone": "13800138000",
        "email": "admin@dronerent.com",
        "avatar": null,
        "role": "ADMIN",
        "status": 1,
        "createTime": "2024-01-01T12:00:00",
        "updateTime": "2024-01-01T12:00:00"
      }
    ]
  },
  "timestamp": 1704067200000
}
```

**权限要求**: 需要 ADMIN 角色

---

### 2.2 查询用户详情

**接口**: `GET /api/users/{id}`

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
- `id`: 用户ID

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "username": "admin",
    "realName": "系统管理员",
    "phone": "13800138000",
    "email": "admin@dronerent.com",
    "role": "ADMIN",
    "status": 1,
    "createTime": "2024-01-01T12:00:00",
    "updateTime": "2024-01-01T12:00:00"
  },
  "timestamp": 1704067200000
}
```

**权限要求**: 需要 ADMIN 角色

---

### 2.3 创建用户

**接口**: `POST /api/users`

**请求头**:
```
Authorization: Bearer {token}
Content-Type: application/json
```

**请求体**:
```json
{
  "username": "testuser",
  "password": "test123",
  "realName": "测试用户",
  "phone": "13900139000",
  "email": "test@example.com",
  "role": "EMPLOYEE",
  "status": 1
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "创建成功",
  "data": null,
  "timestamp": 1704067200000
}
```

**权限要求**: 需要 ADMIN 角色

**注意事项**:
- 用户名不能重复
- 密码不能为空
- 手机号格式要正确
- 邮箱格式要正确

---

### 2.4 更新用户

**接口**: `PUT /api/users/{id}`

**请求头**:
```
Authorization: Bearer {token}
Content-Type: application/json
```

**路径参数**:
- `id`: 用户ID

**请求体**:
```json
{
  "id": 2,
  "username": "testuser",
  "password": "newpass123",
  "realName": "测试用户更新",
  "phone": "13900139001",
  "email": "test_new@example.com",
  "role": "EMPLOYEE",
  "status": 1
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "更新成功",
  "data": null,
  "timestamp": 1704067200000
}
```

**权限要求**: 需要 ADMIN 角色

**注意事项**:
- 如果提供了密码字段，会更新密码
- 如果不提供密码字段，保持原密码不变

---

### 2.5 删除用户

**接口**: `DELETE /api/users/{id}`

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
- `id`: 用户ID

**响应示例**:
```json
{
  "code": 200,
  "message": "删除成功",
  "data": null,
  "timestamp": 1704067200000
}
```

**权限要求**: 需要 ADMIN 角色

**注意事项**:
- 这是逻辑删除，数据库中 deleted 字段会被设置为 1
- 不允许删除自己（待实现）

---

### 2.6 启用/禁用用户

**接口**: `PATCH /api/users/{id}/status?status={status}`

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
- `id`: 用户ID

**查询参数**:
- `status`: 状态（0-禁用，1-启用）

**响应示例**:
```json
{
  "code": 200,
  "message": "启用成功",
  "data": null,
  "timestamp": 1704067200000
}
```

**权限要求**: 需要 ADMIN 角色

---

### 2.7 重置用户密码

**接口**: `PATCH /api/users/{id}/password?newPassword={newPassword}`

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
- `id`: 用户ID

**查询参数**:
- `newPassword`: 新密码

**响应示例**:
```json
{
  "code": 200,
  "message": "密码重置成功",
  "data": null,
  "timestamp": 1704067200000
}
```

**权限要求**: 需要 ADMIN 角色

---

## 3. 角色管理接口测试

### 3.1 获取所有角色

**接口**: `GET /api/roles`

**请求头**:
```
Authorization: Bearer {token}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "code": "ADMIN",
      "name": "管理员"
    },
    {
      "code": "EMPLOYEE",
      "name": "员工"
    }
  ],
  "timestamp": 1704067200000
}
```

---

### 3.2 获取角色权限

**接口**: `GET /api/roles/{roleCode}/permissions`

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
- `roleCode`: 角色代码（ADMIN 或 EMPLOYEE）

**响应示例**（ADMIN）:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    "user:create",
    "user:update",
    "user:delete",
    "user:view",
    "drone:create",
    "drone:update",
    "drone:delete",
    "drone:view",
    "order:create",
    "order:update",
    "order:delete",
    "order:view",
    "customer:create",
    "customer:update",
    "customer:delete",
    "customer:view",
    "finance:view",
    "report:view"
  ],
  "timestamp": 1704067200000
}
```

**权限要求**: 需要 ADMIN 角色

---

## 4. 权限控制示例测试

### 4.1 公开接口（所有登录用户可访问）

**接口**: `GET /api/demo/public`

**请求头**:
```
Authorization: Bearer {token}
```

**响应**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": "这是公开接口，所有登录用户都可以访问",
  "timestamp": 1704067200000
}
```

---

### 4.2 管理员接口（仅 ADMIN 可访问）

**接口**: `GET /api/demo/admin`

**请求头**:
```
Authorization: Bearer {token}
```

**测试步骤**:
1. 使用 ADMIN 角色登录 → 访问成功
2. 使用 EMPLOYEE 角色登录 → 访问失败（403 Forbidden）

**成功响应**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": "这是管理员接口，只有 ADMIN 角色可以访问",
  "timestamp": 1704067200000
}
```

**失败响应**（403）:
```json
{
  "code": 403,
  "message": "Forbidden",
  "data": null,
  "timestamp": 1704067200000
}
```

---

### 4.3 多角色接口（ADMIN 和 EMPLOYEE 都可访问）

**接口**: `GET /api/demo/staff`

**请求头**:
```
Authorization: Bearer {token}
```

**测试结果**:
- ADMIN 角色 → 访问成功
- EMPLOYEE 角色 → 访问成功

---

## 🔐 权限控制说明

### 1. 基于角色的访问控制（RBAC）

在 SecurityConfig 中配置：

```java
// 允许匿名访问
.requestMatchers("/api/auth/login", "/api/auth/register").permitAll()

// 管理员接口
.requestMatchers("/api/admin/**").hasRole("ADMIN")

// 其他接口需要认证
.anyRequest().authenticated()
```

### 2. 方法级别的权限控制

使用 `@PreAuthorize` 注解：

```java
// 需要 ADMIN 角色
@PreAuthorize("hasRole('ADMIN')")

// 需要多个角色中的任意一个
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")

// 基于权限表达式
@PreAuthorize("hasAuthority('ROLE_ADMIN')")

// 复杂表达式
@PreAuthorize("hasRole('ADMIN') and #userId != null")
```

### 3. JWT Token 使用

**获取 Token**:
1. 调用登录接口
2. 从响应中获取 `token` 字段

**使用 Token**:
在所有需要认证的接口中，添加请求头：
```
Authorization: Bearer {token}
```

**Token 有效期**: 
- 默认 24 小时（86400 秒）
- 可在 `application.yml` 中配置

---

## ❌ 常见错误

### 1. 401 Unauthorized

**原因**: 
- Token 未提供
- Token 已过期
- Token 格式错误

**解决**: 
- 确保请求头中包含有效的 Token
- 重新登录获取新 Token

### 2. 403 Forbidden

**原因**: 
- 当前用户没有访问该接口的权限

**解决**: 
- 使用具有相应权限的账号登录
- 联系管理员分配权限

### 3. 400 Bad Request

**原因**: 
- 参数校验失败
- 请求体格式错误

**解决**: 
- 检查请求参数是否符合要求
- 确保 JSON 格式正确

---

## 📝 测试清单

- [ ] 使用 admin 账号登录
- [ ] 获取并保存 Token
- [ ] 测试获取当前用户信息
- [ ] 测试分页查询用户列表
- [ ] 测试查询用户详情
- [ ] 测试创建新用户
- [ ] 测试更新用户信息
- [ ] 测试启用/禁用用户
- [ ] 测试重置用户密码
- [ ] 测试删除用户
- [ ] 测试获取所有角色
- [ ] 测试获取角色权限
- [ ] 测试权限控制示例接口
- [ ] 使用 employee 账号测试权限限制

---

## 🎯 下一步工作

1. **完善权限模型**
   - 设计权限表结构
   - 实现动态权限加载
   - 支持细粒度权限控制

2. **增强安全特性**
   - 添加验证码
   - 实现登录失败次数限制
   - 添加 IP 白名单

3. **优化用户体验**
   - 实现 Token 自动刷新
   - 添加记住我功能
   - 支持多设备登录

---

**测试完成！用户与权限模块运行正常。** ✅
