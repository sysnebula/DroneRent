# 登录问题排查指南

## ❌ 问题：密码 admin123 登录失败

---

## 🔍 可能原因及解决方案

### 原因 1：数据库未初始化（最常见）⭐

**症状**：数据库中没有任何用户数据

**解决方案**：

#### 方法 A：执行完整建表脚本（首次使用）

```bash
# 在命令行执行
mysql -u root -proot < D:\test02\java\DroneRent\src\main\resources\db\schema.sql
```

或者使用 MySQL 客户端工具：
1. 连接到 MySQL（localhost:3306，用户名 root，密码 root）
2. 打开文件：`D:\test02\java\DroneRent\src\main\resources\db\schema.sql`
3. 执行整个脚本

#### 方法 B：仅初始化管理员账号（数据库已存在）

```bash
# 在命令行执行
mysql -u root -proot drone_rental < D:\test02\java\DroneRent\src\main\resources\db\init_admin.sql
```

或者在 MySQL 客户端中执行 `init_admin.sql` 文件。

---

### 原因 2：数据库连接配置错误

**检查配置文件**：`application-dev.yml`

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/drone_rental?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: root
    password: root  # ← 确认这个密码是否正确
```

**验证步骤**：
1. 确认 MySQL 服务已启动
2. 尝试用相同配置连接数据库
3. 确认数据库 `drone_rental` 存在

**如果密码不同**，修改 `application-dev.yml` 中的 `password` 字段。

---

### 原因 3：用户数据损坏或密码加密错误

**验证用户数据**：

```sql
USE drone_rental;
SELECT id, username, password, real_name, role, status FROM sys_user WHERE username = 'admin';
```

**期望结果**：
```
+----+----------+--------------------------------------------------------------+-----------------+-------+--------+
| id | username | password                                                     | real_name       | role  | status |
+----+----------+--------------------------------------------------------------+-----------------+-------+--------+
|  1 | admin    | $2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi | 系统管理员      | ADMIN |      1 |
+----+----------+--------------------------------------------------------------+-----------------+-------+--------+
```

**如果密码字段不正确**，执行修复脚本：

```sql
USE drone_rental;

-- 重置 admin 密码为 admin123
UPDATE sys_user 
SET password = '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi' 
WHERE username = 'admin';
```

**如果用户被禁用**（status = 0）：

```sql
UPDATE sys_user SET status = 1 WHERE username = 'admin';
```

---

### 原因 4：BCrypt 密码不匹配

**验证密码是否正确加密**：

正确的 BCrypt 哈希值（对应明文 `admin123`）：
```
$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi
```

**注意**：BCrypt 每次生成的哈希值都不同，但都能验证同一明文。

**如果需要重新生成密码**，可以使用以下 Java 代码：

```java
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode("admin123");
        System.out.println(encodedPassword);
    }
}
```

---

## 🛠️ 快速修复步骤（推荐）

### 第 1 步：确认 MySQL 正在运行

```bash
# Windows
net start | findstr MySQL

# 或者在任务管理器中查看 MySQL 服务
```

### 第 2 步：执行初始化脚本

```bash
# 进入项目目录
cd D:\test02\java\DroneRent

# 执行管理员初始化脚本
mysql -u root -proot drone_rental < src\main\resources\db\init_admin.sql
```

### 第 3 步：重启后端服务

```bash
# 停止当前运行的服务（Ctrl+C）
# 重新启动
mvn spring-boot:run
```

### 第 4 步：测试登录

访问前端：http://localhost:5173/login

使用凭据：
- **用户名**: `admin`
- **密码**: `admin123`

---

## 📊 验证清单

执行完上述步骤后，确认以下内容：

- [ ] MySQL 服务正在运行
- [ ] 数据库 `drone_rental` 存在
- [ ] 表 `sys_user` 存在且有数据
- [ ] admin 用户的 status = 1（启用状态）
- [ ] admin 用户的 password 字段以 `$2a$10$` 开头
- [ ] 后端服务成功启动（无报错）
- [ ] 前端可以访问 http://localhost:5173

---

## 🔧 常用调试 SQL

### 查看所有用户
```sql
USE drone_rental;
SELECT * FROM sys_user;
```

### 检查 admin 用户状态
```sql
SELECT id, username, status, role, create_time 
FROM sys_user 
WHERE username = 'admin';
```

### 手动创建测试用户
```sql
INSERT INTO sys_user (username, password, real_name, phone, email, role, status) 
VALUES (
    'testuser', 
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 
    '测试用户', 
    '13900000000', 
    'test@example.com', 
    'EMPLOYEE', 
    1
);
```

### 清空所有用户数据（谨慎使用！）
```sql
DELETE FROM sys_user;
```

---

## 💡 提示

1. **默认测试账号**：
   - 管理员：`admin` / `admin123`
   - 员工：`employee001` / `employee123`

2. **BCrypt 密码特点**：
   - 每次加密结果不同，但验证相同明文都通过
   - 格式：`$2a$10$...`（60个字符）
   - 包含盐值和哈希值

3. **常见错误**：
   - ❌ 数据库未创建
   - ❌ SQL 脚本未执行
   - ❌ MySQL 服务未启动
   - ❌ 数据库密码配置错误
   - ❌ 用户被禁用（status = 0）

---

## 🆘 仍然无法解决？

### 检查后端日志

启动后端时查看是否有以下错误：
```
- Cannot connect to database
- Table 'drone_rental.sys_user' doesn't exist
- Access denied for user 'root'@'localhost'
```

### 检查前端控制台

按 F12 打开开发者工具，查看：
- Network 标签页中的请求状态
- Console 标签页中的错误信息

### 测试后端 API

直接使用 curl 或 Postman 测试：

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

**期望响应**：
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
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

---

**祝您问题解决！** 🎉
