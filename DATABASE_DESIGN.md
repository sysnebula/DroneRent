# 无人机出租后台管理系统 - 数据库设计文档

## 数据库概述

- **数据库名称**: drone_rental
- **数据库版本**: MySQL 8.0
- **字符集**: utf8mb4
- **排序规则**: utf8mb4_unicode_ci
- **存储引擎**: InnoDB

## 数据表清单

| 序号 | 表名 | 中文名 | 说明 |
|------|------|--------|------|
| 1 | sys_user | 系统用户表 | 存储管理员和员工信息 |
| 2 | customer | 客户表 | 存储租赁客户信息 |
| 3 | drone | 无人机设备表 | 存储无人机设备档案 |
| 4 | orders | 订单表 | 存储租赁订单信息 |
| 5 | maintenance_record | 维修记录表 | 存储设备维修历史 |
| 6 | inventory_log | 库存变动记录表 | 记录库存状态变更日志 |
| 7 | finance_transaction | 财务流水表 | 记录所有财务交易 |

---

## 1. sys_user（系统用户表）

### 表说明
存储系统用户（管理员和员工）的基本信息和权限。

### 字段说明

| 字段名 | 数据类型 | 约束 | 默认值 | 说明 |
|--------|---------|------|--------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | - | 用户ID |
| username | VARCHAR(50) | NOT NULL, UNIQUE | - | 用户名 |
| password | VARCHAR(100) | NOT NULL | - | 密码（BCrypt加密） |
| real_name | VARCHAR(50) | - | NULL | 真实姓名 |
| phone | VARCHAR(20) | - | NULL | 手机号 |
| email | VARCHAR(100) | - | NULL | 邮箱 |
| avatar | VARCHAR(255) | - | NULL | 头像URL |
| role | VARCHAR(20) | NOT NULL | 'EMPLOYEE' | 角色（ADMIN/EMPLOYEE） |
| status | TINYINT | NOT NULL | 1 | 状态（0-禁用，1-启用） |
| deleted | TINYINT | NOT NULL | 0 | 逻辑删除（0-未删除，1-已删除） |
| create_time | DATETIME | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |
| update_time | DATETIME | NOT NULL | CURRENT_TIMESTAMP ON UPDATE | 更新时间 |

### 索引
- PRIMARY KEY: `id`
- UNIQUE KEY: `uk_username` (username)
- KEY: `idx_phone` (phone)
- KEY: `idx_status` (status)

---

## 2. customer（客户表）

### 表说明
存储租赁客户的个人信息和信用记录。

### 字段说明

| 字段名 | 数据类型 | 约束 | 默认值 | 说明 |
|--------|---------|------|--------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | - | 客户ID |
| customer_no | VARCHAR(50) | NOT NULL, UNIQUE | - | 客户编号 |
| name | VARCHAR(50) | NOT NULL | - | 姓名 |
| id_card | VARCHAR(18) | NOT NULL, UNIQUE | - | 身份证号 |
| phone | VARCHAR(20) | NOT NULL | - | 手机号 |
| email | VARCHAR(100) | - | NULL | 邮箱 |
| address | VARCHAR(255) | - | NULL | 地址 |
| driver_license_no | VARCHAR(50) | - | NULL | 驾驶证/飞行执照号 |
| credit_score | INT | NOT NULL | 100 | 信用分数（满分100） |
| blacklist | TINYINT | NOT NULL | 0 | 黑名单标识（0-否，1-是） |
| remark | TEXT | - | NULL | 备注 |
| deleted | TINYINT | NOT NULL | 0 | 逻辑删除 |
| create_time | DATETIME | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |
| update_time | DATETIME | NOT NULL | CURRENT_TIMESTAMP ON UPDATE | 更新时间 |

### 索引
- PRIMARY KEY: `id`
- UNIQUE KEY: `uk_customer_no` (customer_no)
- UNIQUE KEY: `uk_id_card` (id_card)
- KEY: `idx_phone` (phone)
- KEY: `idx_blacklist` (blacklist)

---

## 3. drone（无人机设备表）

### 表说明
存储无人机设备的详细信息、状态和统计数据。

### 字段说明

| 字段名 | 数据类型 | 约束 | 默认值 | 说明 |
|--------|---------|------|--------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | - | 无人机ID |
| drone_no | VARCHAR(50) | NOT NULL, UNIQUE | - | 无人机编号 |
| brand | VARCHAR(50) | NOT NULL | - | 品牌 |
| model | VARCHAR(50) | NOT NULL | - | 型号 |
| serial_number | VARCHAR(100) | UNIQUE | NULL | 序列号 |
| purchase_date | DATE | - | NULL | 购买日期 |
| purchase_price | DECIMAL(10,2) | - | NULL | 购买价格 |
| daily_rental_price | DECIMAL(10,2) | NOT NULL | - | 日租金 |
| deposit_amount | DECIMAL(10,2) | NOT NULL | - | 押金金额 |
| status | VARCHAR(20) | NOT NULL | 'IDLE' | 状态（IDLE/RENTED/MAINTENANCE/SCRAPPED） |
| flight_hours | DECIMAL(10,2) | - | 0.00 | 累计飞行时长（小时） |
| total_flights | INT | - | 0 | 累计飞行次数 |
| last_maintenance_date | DATE | - | NULL | 上次维护日期 |
| next_maintenance_date | DATE | - | NULL | 下次维护日期 |
| description | TEXT | - | NULL | 描述信息 |
| images | TEXT | - | NULL | 图片URL列表（JSON数组） |
| specs | JSON | - | NULL | 规格参数（JSON对象） |
| deleted | TINYINT | NOT NULL | 0 | 逻辑删除 |
| create_time | DATETIME | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |
| update_time | DATETIME | NOT NULL | CURRENT_TIMESTAMP ON UPDATE | 更新时间 |

### 索引
- PRIMARY KEY: `id`
- UNIQUE KEY: `uk_drone_no` (drone_no)
- UNIQUE KEY: `uk_serial_number` (serial_number)
- KEY: `idx_status` (status)
- KEY: `idx_brand_model` (brand, model)

---

## 4. orders（订单表）

### 表说明
存储租赁订单的完整信息，包括租期、费用、状态等。

### 字段说明

| 字段名 | 数据类型 | 约束 | 默认值 | 说明 |
|--------|---------|------|--------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | - | 订单ID |
| order_no | VARCHAR(50) | NOT NULL, UNIQUE | - | 订单编号 |
| customer_id | BIGINT | NOT NULL, FOREIGN KEY | - | 客户ID |
| drone_id | BIGINT | NOT NULL, FOREIGN KEY | - | 无人机ID |
| user_id | BIGINT | NOT NULL, FOREIGN KEY | - | 经办人ID（员工） |
| start_date | DATE | NOT NULL | - | 租赁开始日期 |
| end_date | DATE | NOT NULL | - | 租赁结束日期 |
| actual_start_date | DATETIME | - | NULL | 实际取机时间 |
| actual_end_date | DATETIME | - | NULL | 实际还机时间 |
| rental_days | INT | NOT NULL | - | 租赁天数 |
| daily_price | DECIMAL(10,2) | NOT NULL | - | 日租金 |
| total_amount | DECIMAL(10,2) | NOT NULL | - | 订单总金额 |
| deposit_amount | DECIMAL(10,2) | NOT NULL | - | 押金金额 |
| paid_amount | DECIMAL(10,2) | - | 0.00 | 已支付金额 |
| refund_amount | DECIMAL(10,2) | - | 0.00 | 已退款金额 |
| damage_fee | DECIMAL(10,2) | - | 0.00 | 损坏赔偿费 |
| overdue_fee | DECIMAL(10,2) | - | 0.00 | 逾期费用 |
| discount_amount | DECIMAL(10,2) | - | 0.00 | 优惠金额 |
| status | VARCHAR(20) | NOT NULL | 'PENDING' | 订单状态 |
| payment_method | VARCHAR(20) | - | NULL | 支付方式 |
| payment_time | DATETIME | - | NULL | 支付时间 |
| pickup_person | VARCHAR(50) | - | NULL | 取机人姓名 |
| pickup_phone | VARCHAR(20) | - | NULL | 取机人电话 |
| return_person | VARCHAR(50) | - | NULL | 还机人姓名 |
| return_phone | VARCHAR(20) | - | NULL | 还机人电话 |
| remark | TEXT | - | NULL | 订单备注 |
| cancel_reason | TEXT | - | NULL | 取消原因 |
| deleted | TINYINT | NOT NULL | 0 | 逻辑删除 |
| create_time | DATETIME | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |
| update_time | DATETIME | NOT NULL | CURRENT_TIMESTAMP ON UPDATE | 更新时间 |

### 外键约束
- `fk_orders_customer`: customer_id → customer(id)
- `fk_orders_drone`: drone_id → drone(id)
- `fk_orders_user`: user_id → sys_user(id)

### 索引
- PRIMARY KEY: `id`
- UNIQUE KEY: `uk_order_no` (order_no)
- KEY: `idx_customer_id`, `idx_drone_id`, `idx_user_id`
- KEY: `idx_status`, `idx_start_date`, `idx_end_date`

---

## 5. maintenance_record（维修记录表）

### 表说明
记录无人机的维修保养历史。

### 字段说明

| 字段名 | 数据类型 | 约束 | 默认值 | 说明 |
|--------|---------|------|--------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | - | 维修记录ID |
| drone_id | BIGINT | NOT NULL, FOREIGN KEY | - | 无人机ID |
| maintenance_type | VARCHAR(20) | NOT NULL | - | 维修类型（ROUTINE/REPAIR/INSPECTION） |
| start_date | DATETIME | NOT NULL | - | 维修开始时间 |
| end_date | DATETIME | - | NULL | 维修结束时间 |
| maintenance_cost | DECIMAL(10,2) | - | NULL | 维修费用 |
| maintenance_content | TEXT | - | NULL | 维修内容 |
| fault_description | TEXT | - | NULL | 故障描述 |
| solution | TEXT | - | NULL | 解决方案 |
| parts_replaced | JSON | - | NULL | 更换零件（JSON数组） |
| technician | VARCHAR(50) | - | NULL | 维修技师 |
| status | VARCHAR(20) | NOT NULL | 'IN_PROGRESS' | 状态 |
| remark | TEXT | - | NULL | 备注 |
| deleted | TINYINT | NOT NULL | 0 | 逻辑删除 |
| create_time | DATETIME | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |
| update_time | DATETIME | NOT NULL | CURRENT_TIMESTAMP ON UPDATE | 更新时间 |

### 外键约束
- `fk_maintenance_drone`: drone_id → drone(id)

### 索引
- PRIMARY KEY: `id`
- KEY: `idx_drone_id`, `idx_status`, `idx_start_date`

---

## 6. inventory_log（库存变动记录表）

### 表说明
记录无人机库存状态的每次变更。

### 字段说明

| 字段名 | 数据类型 | 约束 | 默认值 | 说明 |
|--------|---------|------|--------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | - | 日志ID |
| drone_id | BIGINT | NOT NULL, FOREIGN KEY | - | 无人机ID |
| change_type | VARCHAR(20) | NOT NULL | - | 变动类型 |
| old_status | VARCHAR(20) | - | NULL | 原状态 |
| new_status | VARCHAR(20) | - | NULL | 新状态 |
| related_order_no | VARCHAR(50) | - | NULL | 关联订单号 |
| operator_id | BIGINT | NOT NULL, FOREIGN KEY | - | 操作人ID |
| operation_time | DATETIME | NOT NULL | CURRENT_TIMESTAMP | 操作时间 |
| remark | TEXT | - | NULL | 备注 |
| deleted | TINYINT | NOT NULL | 0 | 逻辑删除 |
| create_time | DATETIME | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |

### 外键约束
- `fk_inventory_drone`: drone_id → drone(id)
- `fk_inventory_operator`: operator_id → sys_user(id)

### 索引
- PRIMARY KEY: `id`
- KEY: `idx_drone_id`, `idx_change_type`, `idx_operation_time`

---

## 7. finance_transaction（财务流水表）

### 表说明
记录系统中所有的财务交易流水。

### 字段说明

| 字段名 | 数据类型 | 约束 | 默认值 | 说明 |
|--------|---------|------|--------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | - | 流水ID |
| transaction_no | VARCHAR(50) | NOT NULL, UNIQUE | - | 流水号 |
| order_id | BIGINT | FOREIGN KEY | NULL | 关联订单ID |
| transaction_type | VARCHAR(20) | NOT NULL | - | 交易类型 |
| amount | DECIMAL(10,2) | NOT NULL | - | 金额 |
| direction | VARCHAR(10) | NOT NULL | - | 方向（INCOME/EXPENSE） |
| payment_method | VARCHAR(20) | - | NULL | 支付方式 |
| transaction_time | DATETIME | NOT NULL | CURRENT_TIMESTAMP | 交易时间 |
| operator_id | BIGINT | NOT NULL, FOREIGN KEY | - | 操作人ID |
| remark | TEXT | - | NULL | 备注 |
| deleted | TINYINT | NOT NULL | 0 | 逻辑删除 |
| create_time | DATETIME | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |

### 外键约束
- `fk_finance_order`: order_id → orders(id)
- `fk_finance_operator`: operator_id → sys_user(id)

### 索引
- PRIMARY KEY: `id`
- UNIQUE KEY: `uk_transaction_no` (transaction_no)
- KEY: `idx_order_id`, `idx_transaction_type`, `idx_transaction_time`

---

## 枚举值说明

### 用户角色（sys_user.role）
- `ADMIN`: 管理员
- `EMPLOYEE`: 员工

### 无人机状态（drone.status）
- `IDLE`: 空闲
- `RENTED`: 出租中
- `MAINTENANCE`: 维修中
- `SCRAPPED`: 报废

### 订单状态（orders.status）
- `PENDING`: 待支付
- `PAID`: 已支付
- `RENTING`: 租赁中
- `COMPLETED`: 已完成
- `CANCELLED`: 已取消
- `REFUNDED`: 已退款

### 维修类型（maintenance_record.maintenance_type）
- `ROUTINE`: 常规保养
- `REPAIR`: 故障维修
- `INSPECTION`: 检查

### 维修状态（maintenance_record.status）
- `IN_PROGRESS`: 维修中
- `COMPLETED`: 已完成
- `CANCELLED`: 已取消

### 库存变动类型（inventory_log.change_type）
- `PURCHASE`: 采购
- `RENT_OUT`: 出租
- `RETURN`: 归还
- `MAINTENANCE`: 维修
- `SCRAP`: 报废

### 交易类型（finance_transaction.transaction_type）
- `RENTAL_FEE`: 租金
- `DEPOSIT`: 押金
- `DAMAGE_FEE`: 赔偿费
- `OVERDUE_FEE`: 逾期费
- `REFUND`: 退款
- `MAINTENANCE_FEE`: 维修费

### 交易方向（finance_transaction.direction）
- `INCOME`: 收入
- `EXPENSE`: 支出

### 支付方式（payment_method）
- `WECHAT`: 微信
- `ALIPAY`: 支付宝
- `CASH`: 现金
- `CARD`: 刷卡

---

## 实体类清单

所有实体类位于 `com.xxq.dronerent.entity` 包下：

1. **SysUser.java** - 系统用户实体
2. **Customer.java** - 客户实体
3. **Drone.java** - 无人机设备实体
4. **Orders.java** - 订单实体
5. **MaintenanceRecord.java** - 维修记录实体
6. **InventoryLog.java** - 库存变动记录实体
7. **FinanceTransaction.java** - 财务流水实体

## Mapper 接口清单

所有 Mapper 接口位于 `com.xxq.dronerent.mapper` 包下：

1. **SysUserMapper.java**
2. **CustomerMapper.java**
3. **DroneMapper.java**
4. **OrdersMapper.java**
5. **MaintenanceRecordMapper.java**
6. **InventoryLogMapper.java**
7. **FinanceTransactionMapper.java**

## Service 层清单

### Service 接口（com.xxq.dronerent.service）
1. SysUserService
2. CustomerService
3. DroneService
4. OrdersService
5. MaintenanceRecordService
6. InventoryLogService
7. FinanceTransactionService

### Service 实现类（com.xxq.dronerent.service.impl）
1. SysUserServiceImpl
2. CustomerServiceImpl
3. DroneServiceImpl
4. OrdersServiceImpl
5. MaintenanceRecordServiceImpl
6. InventoryLogServiceImpl
7. FinanceTransactionServiceImpl

---

## 使用说明

1. **执行建表脚本**: 
   ```bash
   mysql -u root -p < src/main/resources/db/schema.sql
   ```

2. **所有实体类**都使用了 MyBatis-Plus 注解：
   - `@TableName`: 指定表名
   - `@TableId`: 指定主键及生成策略
   - `@TableField`: 指定字段填充策略
   - `@TableLogic`: 指定逻辑删除字段

3. **所有 Mapper** 都继承了 `BaseMapper<T>`，自动获得 CRUD 方法

4. **所有 Service** 都继承了 `IService<T>`，实现类继承 `ServiceImpl<M, T>`

---

**数据库设计完成！可以开始业务逻辑开发。**
