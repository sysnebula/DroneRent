# 无人机出租后台管理系统 - 项目初始化完成

## 项目概述

本项目是一个基于 Spring Boot 3.2.x 的无人机出租后台管理系统，提供完整的无人机租赁业务管理功能。

## 技术栈

- **后端框架**: Spring Boot 3.2.0
- **ORM框架**: MyBatis-Plus 3.5.5
- **安全框架**: Spring Security 6.x + JWT
- **缓存**: Redis 7.x
- **数据库**: MySQL 8.0
- **接口文档**: Knife4j 4.4.0
- **工具库**: Hutool 5.8.24
- **构建工具**: Maven
- **JDK版本**: 17

## 项目结构

```
drone-rental-admin/
├── src/main/java/com/yourcompany/dronerent/
│   ├── common/                    # 公共类
│   │   ├── Result.java           # 统一返回结果
│   │   ├── PageResult.java       # 分页返回对象
│   │   ├── BusinessException.java # 业务异常
│   │   ├── GlobalExceptionHandler.java # 全局异常处理
│   │   ├── Constants.java        # 常量类
│   │   ├── JwtUtil.java          # JWT工具类
│   │   └── RedisUtil.java        # Redis工具类
│   ├── config/                    # 配置类
│   │   ├── Knife4jConfig.java    # Knife4j配置
│   │   ├── MybatisPlusConfig.java # MyBatis-Plus配置
│   │   └── RedisConfig.java      # Redis配置
│   └── DroneRentApplication.java  # 启动类
├── src/main/resources/
│   ├── db/
│   │   └── init.sql              # 数据库初始化脚本
│   └── application.properties     # 配置文件
└── pom.xml                        # Maven配置
```

## 已完成的工作

### 1. 项目基础配置
- ✅ 更新 pom.xml，添加所有必要依赖
- ✅ 配置 application.properties（数据库、Redis、JWT、MyBatis-Plus等）
- ✅ 更新启动类，添加 @MapperScan 注解

### 2. 通用组件
- ✅ 统一返回结果类 `Result<T>`
- ✅ 分页返回对象 `PageResult<T>`
- ✅ 自定义业务异常 `BusinessException`
- ✅ 全局异常处理器 `GlobalExceptionHandler`
- ✅ 系统常量类 `Constants`

### 3. 工具类
- ✅ JWT 工具类 `JwtUtil`
- ✅ Redis 工具类 `RedisUtil`

### 4. 配置类
- ✅ Knife4j 配置 `Knife4jConfig`
- ✅ MyBatis-Plus 配置 `MybatisPlusConfig`（含分页插件）
- ✅ Redis 配置 `RedisConfig`

### 5. 数据库设计
- ✅ 创建数据库初始化脚本 `init.sql`
- ✅ 设计并创建以下核心表：
  - sys_user（用户表）
  - drone（无人机设备表）
  - customer（客户表）
  - orders（订单表）
  - maintenance_record（维修记录表）
  - inventory_log（库存变动记录表）
  - finance_transaction（财务流水表）
- ✅ 插入测试数据

## 下一步工作

接下来我们可以开始开发具体的业务模块，建议按以下顺序进行：

1. **用户与权限管理模块**
   - 实体类、Mapper、Service、Controller
   - Spring Security 配置
   - JWT 认证过滤器
   - 登录接口

2. **无人机设备管理模块**
   - CRUD 操作
   - 状态管理
   - 图片上传

3. **客户管理模块**
   - 客户信息维护
   - 信用评分管理

4. **订单管理模块**
   - 订单创建、修改、取消
   - 租期计算
   - 押金管理

5. **租赁流程模块**
   - 取机登记
   - 还机验收
   - 损坏赔偿

6. **库存与维修管理模块**
   - 库存查询
   - 维修记录管理

7. **财务统计与报表模块**
   - 收入统计
   - 财务报表

## 快速启动

### 1. 环境准备
- 安装 JDK 17
- 安装 MySQL 8.0
- 安装 Redis 7.x
- 安装 Maven 3.6+

### 2. 数据库初始化
```bash
# 执行数据库初始化脚本
mysql -u root -p < src/main/resources/db/init.sql
```

### 3. 修改配置
编辑 `application.properties`，修改数据库和 Redis 连接信息：
```properties
spring.datasource.username=你的数据库用户名
spring.datasource.password=你的数据库密码
spring.data.redis.host=你的Redis主机
spring.data.redis.port=你的Redis端口
```

### 4. 运行项目
```bash
mvn spring-boot:run
```

### 5. 访问接口文档
启动成功后，访问 Knife4j 接口文档：
```
http://localhost:8080/doc.html
```

## 默认账号

- 管理员账号：admin / admin123
- 员工账号：employee001 / employee123

## 注意事项

1. 所有代码都遵循项目规范，包含详细的中文注释
2. 优先使用 MyBatis-Plus 内置方法，减少手写 SQL
3. 所有接口符合 RESTful 风格
4. 所有参数都进行校验
5. 所有异常都被全局异常处理器捕获

---

**项目已初始化完成，可以开始具体业务模块的开发！**
