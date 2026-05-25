# 无人机出租后台管理系统 - 项目结构说明

## 📁 项目目录结构

```
drone-rental-admin/
├── src/
│   ├── main/
│   │   ├── java/com/xxq/dronerent/
│   │   │   ├── DroneRentApplication.java          # 启动类
│   │   │   ├── common/                             # 公共模块
│   │   │   │   ├── Result.java                     # 统一返回结果
│   │   │   │   ├── PageResult.java                 # 分页返回结果
│   │   │   │   ├── BusinessException.java          # 业务异常
│   │   │   │   ├── GlobalExceptionHandler.java     # 全局异常处理器
│   │   │   │   ├── Constants.java                  # 常量类
│   │   │   │   ├── JwtUtil.java                    # JWT工具类
│   │   │   │   └── RedisUtil.java                  # Redis工具类
│   │   │   ├── config/                             # 配置类
│   │   │   │   ├── Knife4jConfig.java              # Knife4j配置
│   │   │   │   ├── MybatisPlusConfig.java          # MyBatis-Plus配置
│   │   │   │   ├── MybatisPlusMetaObjectHandler.java # MyBatis-Plus自动填充
│   │   │   │   └── RedisConfig.java                # Redis配置
│   │   │   ├── entity/                             # 实体类
│   │   │   │   ├── SysUser.java                    # 系统用户
│   │   │   │   ├── Customer.java                   # 客户
│   │   │   │   ├── Drone.java                      # 无人机
│   │   │   │   ├── Orders.java                     # 订单
│   │   │   │   ├── MaintenanceRecord.java          # 维修记录
│   │   │   │   ├── InventoryLog.java               # 库存日志
│   │   │   │   └── FinanceTransaction.java         # 财务流水
│   │   │   ├── mapper/                             # Mapper接口
│   │   │   │   ├── SysUserMapper.java
│   │   │   │   ├── CustomerMapper.java
│   │   │   │   ├── DroneMapper.java
│   │   │   │   ├── OrdersMapper.java
│   │   │   │   ├── MaintenanceRecordMapper.java
│   │   │   │   ├── InventoryLogMapper.java
│   │   │   │   └── FinanceTransactionMapper.java
│   │   │   ├── service/                            # Service接口
│   │   │   │   ├── SysUserService.java
│   │   │   │   ├── CustomerService.java
│   │   │   │   ├── DroneService.java
│   │   │   │   ├── OrdersService.java
│   │   │   │   ├── MaintenanceRecordService.java
│   │   │   │   ├── InventoryLogService.java
│   │   │   │   ├── FinanceTransactionService.java
│   │   │   │   └── impl/                           # Service实现类
│   │   │   │       ├── SysUserServiceImpl.java
│   │   │   │       ├── CustomerServiceImpl.java
│   │   │   │       ├── DroneServiceImpl.java
│   │   │   │       ├── OrdersServiceImpl.java
│   │   │   │       ├── MaintenanceRecordServiceImpl.java
│   │   │   │       ├── InventoryLogServiceImpl.java
│   │   │   │       └── FinanceTransactionServiceImpl.java
│   │   │   ├── controller/                         # 控制器（待开发）
│   │   │   ├── dto/                                # 数据传输对象（待开发）
│   │   │   ├── vo/                                 # 视图对象（待开发）
│   │   │   ├── security/                           # 安全相关（待开发）
│   │   │   └── utils/                              # 工具类（待开发）
│   │   └── resources/
│   │       ├── application.yml                     # 主配置文件
│   │       ├── application-dev.yml                 # 开发环境配置
│   │       ├── application-prod.yml                # 生产环境配置
│   │       ├── db/
│   │       │   ├── schema.sql                      # 建表脚本
│   │       │   └── init.sql                        # 初始化数据脚本
│   │       ├── mapper/                             # MyBatis XML文件（可选）
│   │       ├── static/                             # 静态资源
│   │       └── templates/                          # 模板文件
│   └── test/
│       └── java/com/xxq/dronerent/
│           └── DroneRentApplicationTests.java      # 测试类
├── pom.xml                                         # Maven配置
├── .gitignore                                      # Git忽略配置
├── DATABASE_DESIGN.md                              # 数据库设计文档
└── PROJECT_STRUCTURE.md                            # 项目结构说明（本文件）
```

---

## 📦 核心模块说明

### 1. Common 模块（公共组件）

#### Result<T> - 统一返回结果
- **位置**: `com.xxq.dronerent.common.Result`
- **功能**: 封装所有接口的返回数据
- **主要方法**:
  - `Result.success()` - 成功返回（无数据）
  - `Result.success(data)` - 成功返回（带数据）
  - `Result.success(message, data)` - 成功返回（自定义消息和数据）
  - `Result.error()` - 失败返回
  - `Result.error(message)` - 失败返回（自定义消息）
  - `Result.error(code, message)` - 失败返回（自定义状态码）
  - `Result.badRequest(message)` - 参数校验失败
  - `Result.unauthorized(message)` - 未授权
  - `Result.forbidden(message)` - 禁止访问
  - `Result.notFound(message)` - 资源不存在

#### PageResult<T> - 分页返回结果
- **位置**: `com.xxq.dronerent.common.PageResult`
- **功能**: 封装分页查询结果
- **字段**: total, pageNum, pageSize, pages, list

#### BusinessException - 业务异常
- **位置**: `com.xxq.dronerent.common.BusinessException`
- **功能**: 自定义业务异常类
- **用途**: 在业务逻辑中抛出，由全局异常处理器捕获

#### GlobalExceptionHandler - 全局异常处理器
- **位置**: `com.xxq.dronerent.common.GlobalExceptionHandler`
- **功能**: 统一处理所有异常
- **处理的异常类型**:
  - `MethodArgumentNotValidException` - 参数校验异常
  - `BindException` - 参数绑定异常
  - `BusinessException` - 业务异常
  - `IllegalArgumentException` - 非法参数异常
  - `NullPointerException` - 空指针异常
  - `Exception` - 其他所有异常

#### Constants - 常量类
- **位置**: `com.xxq.dronerent.common.Constants`
- **功能**: 定义系统常量
- **包含**: 
  - 无人机状态常量
  - 订单状态常量
  - Redis Key 前缀
  - JWT 相关常量

#### JwtUtil - JWT 工具类
- **位置**: `com.xxq.dronerent.common.JwtUtil`
- **功能**: JWT Token 的生成、解析和验证
- **主要方法**:
  - `generateToken(userId, username)` - 生成 Token
  - `getUserIdFromToken(token)` - 从 Token 获取用户ID
  - `getUsernameFromToken(token)` - 从 Token 获取用户名
  - `validateToken(token)` - 验证 Token
  - `refreshToken(token)` - 刷新 Token

#### RedisUtil - Redis 工具类
- **位置**: `com.xxq.dronerent.common.RedisUtil`
- **功能**: 封装常用的 Redis 操作
- **主要方法**:
  - `set(key, value)` - 设置值
  - `get(key)` - 获取值
  - `delete(key)` - 删除键
  - `hasKey(key)` - 判断键是否存在
  - `expire(key, timeout, unit)` - 设置过期时间
  - `hSet/hGet/hDelete` - Hash 操作

---

### 2. Config 模块（配置类）

#### Knife4jConfig - Knife4j 配置
- **位置**: `com.xxq.dronerent.config.Knife4jConfig`
- **功能**: 配置 API 文档信息
- **访问地址**: http://localhost:8080/doc.html

#### MybatisPlusConfig - MyBatis-Plus 配置
- **位置**: `com.xxq.dronerent.config.MybatisPlusConfig`
- **功能**: 配置分页插件
- **特性**:
  - MySQL 分页支持
  - 最大单页限制 500 条
  - 防止溢出

#### MybatisPlusMetaObjectHandler - MyBatis-Plus 自动填充
- **位置**: `com.xxq.dronerent.config.MybatisPlusMetaObjectHandler`
- **功能**: 自动填充创建时间和更新时间
- **触发时机**:
  - 插入时：填充 createTime 和 updateTime
  - 更新时：填充 updateTime

#### RedisConfig - Redis 配置
- **位置**: `com.xxq.dronerent.config.RedisConfig`
- **功能**: 配置 RedisTemplate 序列化方式
- **序列化**:
  - Key: String 序列化
  - Value: Jackson JSON 序列化

---

### 3. Entity 模块（实体类）

所有实体类都位于 `com.xxq.dronerent.entity` 包下，使用 MyBatis-Plus 注解：

| 实体类 | 对应表 | 说明 |
|--------|--------|------|
| SysUser | sys_user | 系统用户 |
| Customer | customer | 客户 |
| Drone | drone | 无人机设备 |
| Orders | orders | 订单 |
| MaintenanceRecord | maintenance_record | 维修记录 |
| InventoryLog | inventory_log | 库存变动日志 |
| FinanceTransaction | finance_transaction | 财务流水 |

**共同特性**:
- 使用 `@TableName` 指定表名
- 使用 `@TableId(type = IdType.AUTO)` 配置主键自增
- 使用 `@TableLogic` 配置逻辑删除
- 使用 `@TableField(fill = FieldFill.INSERT/INSERT_UPDATE)` 配置自动填充
- 实现 `Serializable` 接口
- 使用 Lombok `@Data` 注解

---

### 4. Mapper 模块（数据访问层）

所有 Mapper 接口都位于 `com.xxq.dronerent.mapper` 包下，继承 `BaseMapper<T>`：

| Mapper 接口 | 对应实体 |
|------------|---------|
| SysUserMapper | SysUser |
| CustomerMapper | Customer |
| DroneMapper | Drone |
| OrdersMapper | Orders |
| MaintenanceRecordMapper | MaintenanceRecord |
| InventoryLogMapper | InventoryLog |
| FinanceTransactionMapper | FinanceTransaction |

**自动获得的方法**:
- `insert(T entity)` - 插入
- `deleteById(Serializable id)` - 删除
- `updateById(T entity)` - 更新
- `selectById(Serializable id)` - 查询
- `selectList(Wrapper<T> queryWrapper)` - 列表查询
- `selectPage(Page<T> page, Wrapper<T> queryWrapper)` - 分页查询

---

### 5. Service 模块（业务逻辑层）

#### Service 接口
位于 `com.xxq.dronerent.service` 包，继承 `IService<T>`

#### Service 实现类
位于 `com.xxq.dronerent.service.impl` 包，继承 `ServiceImpl<M, T>`

| Service | 实现类 | 对应实体 |
|---------|--------|---------|
| SysUserService | SysUserServiceImpl | SysUser |
| CustomerService | CustomerServiceImpl | Customer |
| DroneService | DroneServiceImpl | Drone |
| OrdersService | OrdersServiceImpl | Orders |
| MaintenanceRecordService | MaintenanceRecordServiceImpl | MaintenanceRecord |
| InventoryLogService | InventoryLogServiceImpl | InventoryLog |
| FinanceTransactionService | FinanceTransactionServiceImpl | FinanceTransaction |

---

## 🔧 配置文件说明

### application.yml - 主配置文件
- **位置**: `src/main/resources/application.yml`
- **内容**:
  - 应用名称
  - 激活的配置文件（dev/prod）
  - 服务器端口
  - MyBatis-Plus 配置
  - Knife4j 配置
  - JWT 配置
  - 日志配置

### application-dev.yml - 开发环境配置
- **位置**: `src/main/resources/application-dev.yml`
- **内容**:
  - 数据库连接（本地）
  - Redis 连接（本地）
  - HikariCP 连接池配置
  - Jedis 连接池配置
  - 调试日志级别

### application-prod.yml - 生产环境配置
- **位置**: `src/main/resources/application-prod.yml`
- **内容**:
  - 数据库连接（使用环境变量）
  - Redis 连接（使用环境变量）
  - 更大的连接池
  - 生产日志配置

---

## 🚀 快速开始

### 1. 环境要求
- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis 7.x+

### 2. 数据库初始化
```bash
# 执行建表脚本
mysql -u root -p < src/main/resources/db/schema.sql
```

### 3. 修改配置
编辑 `application-dev.yml`，修改数据库和 Redis 连接信息。

### 4. 运行项目
```bash
mvn spring-boot:run
```

### 5. 访问接口文档
启动成功后访问：http://localhost:8080/doc.html

---

## 📝 开发规范

### 1. 代码规范
- 所有类和方法都要有中文注释
- 使用 Lombok 简化代码
- 优先使用 MyBatis-Plus 内置方法
- 所有接口符合 RESTful 风格

### 2. 异常处理
- 业务异常使用 `BusinessException`
- 所有异常由 `GlobalExceptionHandler` 统一处理
- 返回统一的 `Result` 格式

### 3. 参数校验
- 使用 `@Valid` 或 `@Validated` 注解
- 在 DTO 中使用 JSR-303 校验注解
- 校验失败由全局异常处理器捕获

### 4. 事务管理
- 在 Service 层使用 `@Transactional` 注解
- 只在需要事务的方法上添加

---

## 🎯 下一步开发计划

1. **创建 DTO 和 VO 类**
2. **开发 Controller 层**
3. **实现 Spring Security + JWT 认证**
4. **开发用户管理模块**
5. **开发无人机管理模块**
6. **开发订单管理模块**
7. **开发其他业务模块**

---

**项目初始化完成！可以开始业务开发了。**
