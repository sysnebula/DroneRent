# 快速启动指南

## 🚀 5分钟快速启动

### 前置条件

确保以下软件已安装并运行：

1. **JDK 17+**
   ```bash
   java -version
   ```

2. **Maven 3.6+**
   ```bash
   mvn -version
   ```

3. **MySQL 8.0+**
   ```bash
   mysql --version
   ```

4. **Redis 7.x+**
   ```bash
   redis-cli --version
   ```

---

## 步骤一：初始化数据库

```bash
# 登录 MySQL
mysql -u root -p

# 执行建表脚本
mysql -u root -p < src/main/resources/db/schema.sql
```

或者直接执行：
```bash
mysql -u root -p drone_rental < src/main/resources/db/schema.sql
```

---

## 步骤二：修改配置文件

编辑 `src/main/resources/application-dev.yml`：

```yaml
spring:
  datasource:
    username: root          # 修改为你的数据库用户名
    password: root          # 修改为你的数据库密码
  
  data:
    redis:
      host: localhost       # 修改为你的 Redis 主机
      port: 6379           # 修改为你的 Redis 端口
      password:            # 如果 Redis 有密码，请填写
```

---

## 步骤三：编译项目

```bash
# 清理并编译
mvn clean compile

# 或者跳过测试编译
mvn clean compile -DskipTests
```

---

## 步骤四：运行项目

### 方式一：使用 Maven
```bash
mvn spring-boot:run
```

### 方式二：使用 IDE
在 IDE 中运行 `DroneRentApplication` 类的 main 方法

### 方式三：打包后运行
```bash
# 打包
mvn clean package -DskipTests

# 运行 JAR
java -jar target/drone-rental-admin-1.0.0-SNAPSHOT.jar
```

---

## 步骤五：验证运行

### 1. 查看启动日志

成功启动后会看到：
```
===========================================
   无人机出租后台管理系统启动成功！
   Knife4j接口文档: http://localhost:8080/doc.html
===========================================
```

### 2. 访问健康检查接口

浏览器访问：http://localhost:8080/api/test/health

应该看到：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "status": "UP",
    "timestamp": "2024-01-01T12:00:00",
    "message": "系统运行正常"
  },
  "timestamp": 1704067200000
}
```

### 3. 访问 API 文档

浏览器访问：http://localhost:8080/doc.html

应该能看到 Knife4j 接口文档页面。

---

## 🔍 常见问题

### 问题1：端口被占用

**错误信息**: `Port 8080 was already in use`

**解决方案**:
1. 修改 `application.yml` 中的端口：
   ```yaml
   server:
     port: 8081
   ```
2. 或者关闭占用 8080 端口的程序

### 问题2：数据库连接失败

**错误信息**: `Communications link failure`

**检查清单**:
- [ ] MySQL 是否已启动
- [ ] 数据库 `drone_rental` 是否已创建
- [ ] 用户名和密码是否正确
- [ ] 数据库 URL 是否正确

**解决方案**:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/drone_rental?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

### 问题3：Redis 连接失败

**错误信息**: `Cannot get Jedis connection`

**检查清单**:
- [ ] Redis 是否已启动
- [ ] Redis 主机和端口是否正确
- [ ] 是否需要密码

**解决方案**:
```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
      password: your_password  # 如果有密码
```

**临时方案**: 如果暂时不需要 Redis，可以注释掉相关依赖或使用嵌入式 Redis

### 问题4：MyBatis-Plus 启动错误

**错误信息**: `Invalid value type for attribute 'factoryBeanObjectType'`

**原因**: MyBatis-Plus 版本与 Spring Boot 3 不兼容

**解决方案**: 
✅ 已修复！项目已使用 `mybatis-plus-spring-boot3-starter 3.5.10.1`

如果仍有问题，检查 pom.xml：
```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
    <version>3.5.10.1</version>
</dependency>
```

### 问题5：Lombok 不生效

**症状**: IDE 提示找不到 getter/setter 方法

**解决方案**:
1. IDE 安装 Lombok 插件
2. 启用注解处理器：
   - IntelliJ IDEA: Settings → Build → Compiler → Annotation Processors → Enable
   - Eclipse: 确保安装了 Lombok

### 问题6：中文乱码

**解决方案**:
1. 确保数据库字符集为 `utf8mb4`
2. 确保 JDBC URL 包含 `characterEncoding=utf8`
3. 确保 IDE 文件编码为 UTF-8

---

## 🧪 测试接口

项目提供了一个测试控制器，包含以下接口：

### 1. 健康检查
```bash
GET http://localhost:8080/api/test/health
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "status": "UP",
    "timestamp": "2024-01-01T12:00:00",
    "message": "系统运行正常"
  },
  "timestamp": 1704067200000
}
```

### 2. 测试成功返回
```bash
GET http://localhost:8080/api/test/success
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": "操作成功！",
  "timestamp": 1704067200000
}
```

### 3. 测试带数据返回
```bash
GET http://localhost:8080/api/test/data
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "name": "无人机出租系统",
    "version": "1.0.0",
    "time": "2024-01-01T12:00:00"
  },
  "timestamp": 1704067200000
}
```

---

## 📚 下一步

项目启动成功后，可以开始开发业务模块：

### 推荐开发顺序

1. **用户与权限管理** ⭐ 优先
   - Spring Security 配置
   - JWT 认证
   - 登录/登出接口
   - 用户 CRUD

2. **无人机设备管理**
   - 设备 CRUD
   - 状态管理
   - 图片上传

3. **客户管理**
   - 客户 CRUD
   - 信用评分

4. **订单管理**
   - 订单 CRUD
   - 租期计算
   - 押金管理

5. **租赁流程**
   - 取机登记
   - 还机验收
   - 损坏赔偿

6. **库存与维修**
   - 库存查询
   - 维修记录

7. **财务统计**
   - 收入统计
   - 财务报表

---

## 💡 开发提示

### 1. 使用 MyBatis-Plus 简化开发

```java
// 查询所有
List<SysUser> users = sysUserService.list();

// 条件查询
LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
wrapper.eq(SysUser::getStatus, 1);
List<SysUser> users = sysUserService.list(wrapper);

// 分页查询
Page<SysUser> page = new Page<>(1, 10);
sysUserService.page(page, wrapper);

// 插入
sysUserService.save(user);

// 更新
sysUserService.updateById(user);

// 删除
sysUserService.removeById(id);
```

### 2. 统一返回格式

```java
@GetMapping("/users")
public Result<List<SysUser>> list() {
    List<SysUser> users = sysUserService.list();
    return Result.success(users);
}

@PostMapping("/users")
public Result<Void> add(@RequestBody @Valid SysUser user) {
    sysUserService.save(user);
    return Result.success();
}
```

### 3. 异常处理

```java
// 抛出业务异常
if (user == null) {
    throw new BusinessException("用户不存在");
}

// 全局异常处理器会自动捕获并返回统一格式
```

### 4. 参数校验

```java
@PostMapping("/users")
public Result<Void> add(@RequestBody @Valid UserDTO dto) {
    // DTO 中使用 JSR-303 注解
    // @NotBlank, @Email, @Size 等
}
```

---

## 📞 需要帮助？

如果遇到问题：

1. 查看日志文件：`logs/drone-rental.log`
2. 检查控制台输出
3. 查阅文档：
   - [DATABASE_DESIGN.md](DATABASE_DESIGN.md)
   - [PROJECT_STRUCTURE.md](PROJECT_STRUCTURE.md)
   - [PROJECT_INIT_SUMMARY.md](PROJECT_INIT_SUMMARY.md)

---

**祝您开发顺利！** 🎉
