# 项目目录树

## Java 源代码结构

```
src/main/java/com/xxq/dronerent/
│
├── DroneRentApplication.java                    # 启动类
│
├── common/                                       # 公共模块
│   ├── Result.java                              # 统一返回结果
│   ├── PageResult.java                          # 分页返回结果
│   ├── BusinessException.java                   # 业务异常
│   ├── GlobalExceptionHandler.java              # 全局异常处理器
│   ├── Constants.java                           # 常量类
│   ├── JwtUtil.java                             # JWT工具类
│   └── RedisUtil.java                           # Redis工具类
│
├── config/                                       # 配置类
│   ├── Knife4jConfig.java                       # Knife4j配置
│   ├── MybatisPlusConfig.java                   # MyBatis-Plus配置
│   ├── MybatisPlusMetaObjectHandler.java        # MyBatis-Plus自动填充
│   └── RedisConfig.java                         # Redis配置
│
├── controller/                                   # 控制器层（待开发）
│   └── TestController.java                      # 测试控制器
│
├── entity/                                       # 实体类
│   ├── SysUser.java                             # 系统用户
│   ├── Customer.java                            # 客户
│   ├── Drone.java                               # 无人机设备
│   ├── Orders.java                              # 订单
│   ├── MaintenanceRecord.java                   # 维修记录
│   ├── InventoryLog.java                        # 库存变动日志
│   └── FinanceTransaction.java                  # 财务流水
│
├── mapper/                                       # Mapper接口
│   ├── SysUserMapper.java
│   ├── CustomerMapper.java
│   ├── DroneMapper.java
│   ├── OrdersMapper.java
│   ├── MaintenanceRecordMapper.java
│   ├── InventoryLogMapper.java
│   └── FinanceTransactionMapper.java
│
├── service/                                      # Service接口
│   ├── SysUserService.java
│   ├── CustomerService.java
│   ├── DroneService.java
│   ├── OrdersService.java
│   ├── MaintenanceRecordService.java
│   ├── InventoryLogService.java
│   ├── FinanceTransactionService.java
│   │
│   └── impl/                                     # Service实现类
│       ├── SysUserServiceImpl.java
│       ├── CustomerServiceImpl.java
│       ├── DroneServiceImpl.java
│       ├── OrdersServiceImpl.java
│       ├── MaintenanceRecordServiceImpl.java
│       ├── InventoryLogServiceImpl.java
│       └── FinanceTransactionServiceImpl.java
│
├── dto/                                          # 数据传输对象（待开发）
├── vo/                                           # 视图对象（待开发）
├── security/                                     # 安全相关（待开发）
└── utils/                                        # 工具类（待开发）
```

## 资源文件结构

```
src/main/resources/
│
├── application.yml                              # 主配置文件
├── application-dev.yml                          # 开发环境配置
├── application-prod.yml                         # 生产环境配置
│
├── db/                                           # 数据库脚本
│   ├── schema.sql                               # 建表脚本
│   └── init.sql                                 # 初始化数据
│
├── mapper/                                       # MyBatis XML（可选）
│
├── static/                                       # 静态资源
└── templates/                                    # 模板文件
```

## 项目根目录

```
drone-rental-admin/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   └── test/
│
├── pom.xml                                       # Maven配置
├── .gitignore                                    # Git忽略配置
│
├── DATABASE_DESIGN.md                            # 数据库设计文档
├── PROJECT_STRUCTURE.md                          # 项目结构说明
├── PROJECT_INIT_SUMMARY.md                       # 项目初始化总结
└── DIRECTORY_TREE.md                             # 本文件
```

## 包说明

| 包名 | 说明 | 主要类 |
|------|------|--------|
| com.xxq.dronerent | 根包 | DroneRentApplication |
| com.xxq.dronerent.common | 公共组件 | Result, BusinessException, JwtUtil, RedisUtil |
| com.xxq.dronerent.config | 配置类 | Knife4jConfig, MybatisPlusConfig, RedisConfig |
| com.xxq.dronerent.controller | 控制器 | TestController（其他待开发） |
| com.xxq.dronerent.entity | 实体类 | SysUser, Customer, Drone, Orders 等 |
| com.xxq.dronerent.mapper | Mapper接口 | 7个Mapper接口 |
| com.xxq.dronerent.service | Service接口 | 7个Service接口 |
| com.xxq.dronerent.service.impl | Service实现 | 7个Service实现类 |

## 文件统计

### Java 文件
- 启动类: 1 个
- 公共类: 7 个
- 配置类: 4 个
- 控制器: 1 个（测试用）
- 实体类: 7 个
- Mapper 接口: 7 个
- Service 接口: 7 个
- Service 实现: 7 个

**合计**: 41 个 Java 文件

### 配置文件
- YAML 配置: 3 个
- 数据库脚本: 2 个（schema.sql, init.sql）

**合计**: 5 个配置文件

### 文档
- DATABASE_DESIGN.md
- PROJECT_STRUCTURE.md
- PROJECT_INIT_SUMMARY.md
- DIRECTORY_TREE.md

**合计**: 4 个文档

---

**总计**: 约 50 个文件
