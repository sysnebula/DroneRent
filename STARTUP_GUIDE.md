# 项目启动指南

## 问题已修复 ✅

已将启动类路径从错误的 `com.xxq.DroneRentApplication` 修正为正确的 `com.xxq.dronerent.DroneRentApplication`。

---

## 启动方式（三选一）

### 方式1：使用批处理脚本启动（推荐新手）

在项目根目录双击运行：
```
run.bat
```

该脚本会自动：
1. 清理项目（mvn clean）
2. 编译项目（mvn compile）
3. 启动应用（mvn spring-boot:run）

---

### 方式2：使用 Maven 命令启动

在项目根目录打开命令行，执行：

```cmd
mvn clean compile spring-boot:run
```

或者分步执行：
```cmd
mvn clean
mvn compile
mvn spring-boot:run
```

---

### 方式3：在 IntelliJ IDEA 中启动

1. **刷新 Maven 项目**
   - 打开右侧 Maven 面板
   - 点击刷新按钮（Reload All Maven Projects）

2. **选择运行配置**
   - 点击右上角运行配置下拉框
   - 选择 "DroneRentApplication"

3. **启动项目**
   - 点击绿色运行按钮（Shift + F10）
   - 或直接右键点击 `DroneRentApplication.java` → Run

---

## 验证启动成功

看到以下输出表示启动成功：

```
===========================================
   无人机出租后台管理系统启动成功！
   Knife4j接口文档: http://localhost:8080/doc.html
===========================================
```

访问接口文档：
- Knife4j: http://localhost:8080/doc.html
- Swagger UI: http://localhost:8080/swagger-ui.html

---

## 常见问题

### 1. 端口被占用

**错误信息：** `Port 8080 was already in use`

**解决方案：** 
- 关闭占用 8080 端口的程序
- 或修改 `application.properties` 中的端口：
  ```properties
  server.port=8081
  ```

### 2. 数据库连接失败

**错误信息：** `Communications link failure`

**解决方案：**
- 确保 MySQL 服务已启动
- 检查 `application.properties` 中的数据库配置
- 确保数据库 `drone_rental` 已创建（执行 `schema.sql`）

### 3. Redis 连接失败

**错误信息：** `Cannot get Jedis connection`

**解决方案：**
- 确保 Redis 服务已启动
- 检查 `application.properties` 中的 Redis 配置
- Windows 可下载 Redis for Windows 或使用 WSL

### 4. IDE 仍然报错找不到主类

**解决方案：**
- File → Invalidate Caches / Restart
- 选择 "Invalidate and Restart"
- 等待 IDE 重新索引完成

---

## 项目信息

- **启动类：** com.xxq.dronerent.DroneRentApplication
- **包名：** com.xxq.dronerent
- **端口：** 8080
- **技术栈：** Spring Boot 3.2.0 + MyBatis-Plus 3.5.5 + MySQL 8.0 + Redis 7.x

---

**祝您开发顺利！** 🚀
