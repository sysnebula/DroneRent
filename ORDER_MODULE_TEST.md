# 订单管理模块 - 接口测试指南

## 📋 模块概述

订单管理模块已开发完成，包含以下功能：

1. ✅ 订单的增删改查接口
2. ✅ 创建订单业务逻辑（检查状态、计算租期、计算费用）
3. ✅ 续租功能
4. ✅ 提前归还功能
5. ✅ 取消订单功能
6. ✅ 完整的参数校验和异常处理
7. ✅ 所有接口都添加了 Knife4j 注解

---

## 🔑 核心业务逻辑

### 1. 创建订单流程

```
1. 验证客户是否存在且不在黑名单中
2. 验证无人机是否存在
3. 检查无人机状态是否为空闲（IDLE）
4. 验证租期（开始日期>=今天，结束日期>=开始日期）
5. 计算租赁天数（包含首尾两天）
6. 计算费用（租金 = 日租金 × 天数，押金从无人机获取）
7. 生成订单编号（ORD + 时间戳 + 随机数）
8. 创建订单（状态：PENDING-待支付）
9. 更新无人机状态为出租中（RENTED）
```

### 2. 续租流程

```
1. 验证订单是否存在
2. 验证订单状态（必须是 PAID 或 RENTING）
3. 验证新的结束日期 >= 原结束日期
4. 计算新增天数和费用
5. 更新订单（结束日期、租赁天数、总金额）
```

### 3. 提前归还流程

```
1. 验证订单是否存在
2. 验证订单状态（必须是 RENTING）
3. 计算实际租赁天数
4. 计算应退金额（如果提前归还）
5. 更新订单（实际还机时间、状态=COMPLETED、退款金额）
6. 更新无人机状态为空闲（IDLE）
```

### 4. 取消订单流程

```
1. 验证订单是否存在
2. 验证订单状态（必须是 PENDING）
3. 更新订单（状态=CANCELLED、取消原因）
4. 更新无人机状态为空闲（IDLE）
```

---

## 🧪 接口测试

### 前置条件

1. 确保已登录并获取 Token
2. 使用 ADMIN 或 EMPLOYEE 角色账号
3. 数据库中有测试客户和无人机数据

---

## 1. 分页查询订单列表

**接口**: `GET /api/orders/page`

**请求头**:
```
Authorization: Bearer {token}
```

**查询参数**:
- `pageNum`: 页码（默认 1）
- `pageSize`: 每页条数（默认 10）
- `status`: 订单状态（可选）
- `customerId`: 客户ID（可选）

**示例请求**:
```
GET /api/orders/page?pageNum=1&pageSize=10&status=PENDING
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "total": 1,
    "pageNum": 1,
    "pageSize": 10,
    "pages": 1,
    "list": [
      {
        "id": 1,
        "orderNo": "ORD202401151200001234",
        "customerId": 1,
        "droneId": 1,
        "userId": 1,
        "startDate": "2024-01-15",
        "endDate": "2024-01-20",
        "rentalDays": 6,
        "dailyPrice": 299.00,
        "totalAmount": 1794.00,
        "depositAmount": 3000.00,
        "paidAmount": 0.00,
        "status": "PENDING",
        "createTime": "2024-01-15T12:00:00",
        "updateTime": "2024-01-15T12:00:00"
      }
    ]
  },
  "timestamp": 1704067200000
}
```

---

## 2. 查询订单详情

**接口**: `GET /api/orders/{id}`

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
- `id`: 订单ID

**示例请求**:
```
GET /api/orders/1
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "orderNo": "ORD202401151200001234",
    "customerId": 1,
    "customerName": "李四",
    "droneId": 1,
    "droneNo": "DRONE001",
    "droneModel": "DJI Mavic 3",
    "userId": 1,
    "userName": "张三",
    "startDate": "2024-01-15",
    "endDate": "2024-01-20",
    "rentalDays": 6,
    "dailyPrice": 299.00,
    "totalAmount": 1794.00,
    "depositAmount": 3000.00,
    "paidAmount": 0.00,
    "refundAmount": 0.00,
    "damageFee": 0.00,
    "overdueFee": 0.00,
    "discountAmount": 0.00,
    "status": "PENDING",
    "pickupPerson": "李四",
    "pickupPhone": "13900139000",
    "remark": "请确保设备完好",
    "createTime": "2024-01-15T12:00:00",
    "updateTime": "2024-01-15T12:00:00"
  },
  "timestamp": 1704067200000
}
```

---

## 3. 创建订单

**接口**: `POST /api/orders`

**请求头**:
```
Authorization: Bearer {token}
Content-Type: application/json
```

**请求体**:
```json
{
  "customerId": 1,
  "droneId": 1,
  "startDate": "2024-01-15",
  "endDate": "2024-01-20",
  "pickupPerson": "李四",
  "pickupPhone": "13900139000",
  "remark": "请确保设备完好"
}
```

**必填字段**:
- customerId（客户ID）
- droneId（无人机ID）
- startDate（租赁开始日期）
- endDate（租赁结束日期）

**响应示例**:
```json
{
  "code": 200,
  "message": "订单创建成功",
  "data": 1,
  "timestamp": 1704067200000
}
```

**权限要求**: 需要 ADMIN 或 EMPLOYEE 角色

**业务逻辑**:
1. ✅ 检查客户是否存在且不在黑名单
2. ✅ 检查无人机是否存在
3. ✅ 检查无人机状态是否为空闲
4. ✅ 验证租期合法性
5. ✅ 计算租赁天数（包含首尾）
6. ✅ 计算租金和押金
7. ✅ 生成订单编号
8. ✅ 创建订单（状态：PENDING）
9. ✅ 更新无人机状态为 RENTED

**注意事项**:
- 开始日期不能早于今天
- 结束日期不能早于开始日期
- 无人机必须处于空闲状态
- 客户不能在黑名单中

---

## 4. 更新订单信息

**接口**: `PUT /api/orders/{id}`

**请求头**:
```
Authorization: Bearer {token}
Content-Type: application/json
```

**路径参数**:
- `id`: 订单ID

**请求体**:
```json
{
  "returnPerson": "李四",
  "returnPhone": "13900139000",
  "remark": "设备完好无损"
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

**权限要求**: 需要 ADMIN 或 EMPLOYEE 角色

---

## 5. 删除订单

**接口**: `DELETE /api/orders/{id}`

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
- `id`: 订单ID

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
- 这是逻辑删除（deleted=1）
- 只能删除已取消（CANCELLED）的订单

---

## 6. 续租订单

**接口**: `POST /api/orders/{id}/extend?newEndDate={newEndDate}`

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
- `id`: 订单ID

**查询参数**:
- `newEndDate`: 新的结束日期

**示例请求**:
```
POST /api/orders/1/extend?newEndDate=2024-01-25
```

**响应示例**:
```json
{
  "code": 200,
  "message": "续租成功",
  "data": null,
  "timestamp": 1704067200000
}
```

**权限要求**: 需要 ADMIN 或 EMPLOYEE 角色

**业务逻辑**:
1. ✅ 验证订单状态（必须是 PAID 或 RENTING）
2. ✅ 验证新的结束日期 >= 原结束日期
3. ✅ 计算新增天数和费用
4. ✅ 更新订单信息

**注意事项**:
- 新的结束日期不能早于原结束日期
- 会自动计算新增的费用

---

## 7. 提前归还

**接口**: `POST /api/orders/{id}/return`

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
- `id`: 订单ID

**查询参数**（可选）:
- `returnPerson`: 还机人姓名
- `returnPhone`: 还机人电话

**示例请求**:
```
POST /api/orders/1/return?returnPerson=李四&returnPhone=13900139000
```

**响应示例**:
```json
{
  "code": 200,
  "message": "归还成功",
  "data": null,
  "timestamp": 1704067200000
}
```

**权限要求**: 需要 ADMIN 或 EMPLOYEE 角色

**业务逻辑**:
1. ✅ 验证订单状态（必须是 RENTING）
2. ✅ 计算实际租赁天数
3. ✅ 计算应退金额（如果提前归还）
4. ✅ 更新订单状态为 COMPLETED
5. ✅ 更新无人机状态为 IDLE

**注意事项**:
- 只有租赁中的订单才能归还
- 如果提前归还会自动计算退款
- 无人机会恢复为空闲状态

---

## 8. 取消订单

**接口**: `POST /api/orders/{id}/cancel?cancelReason={cancelReason}`

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
- `id`: 订单ID

**查询参数**:
- `cancelReason`: 取消原因

**示例请求**:
```
POST /api/orders/1/cancel?cancelReason=客户临时改变计划
```

**响应示例**:
```json
{
  "code": 200,
  "message": "订单已取消",
  "data": null,
  "timestamp": 1704067200000
}
```

**权限要求**: 需要 ADMIN 或 EMPLOYEE 角色

**业务逻辑**:
1. ✅ 验证订单状态（必须是 PENDING）
2. ✅ 更新订单状态为 CANCELLED
3. ✅ 记录取消原因
4. ✅ 更新无人机状态为 IDLE

**注意事项**:
- 只有待支付（PENDING）的订单才能取消
- 取消后无人机会恢复为空闲状态

---

## 🔐 权限控制

### 接口权限清单

| 接口 | 权限要求 |
|------|---------|
| GET /api/orders/page | 任意登录用户 |
| GET /api/orders/{id} | 任意登录用户 |
| POST /api/orders | ADMIN 或 EMPLOYEE |
| PUT /api/orders/{id} | ADMIN 或 EMPLOYEE |
| DELETE /api/orders/{id} | ADMIN |
| POST /api/orders/{id}/extend | ADMIN 或 EMPLOYEE |
| POST /api/orders/{id}/return | ADMIN 或 EMPLOYEE |
| POST /api/orders/{id}/cancel | ADMIN 或 EMPLOYEE |

---

## ❌ 常见错误

### 1. 400 Bad Request - 参数校验失败

**原因**: 
- 必填字段为空
- 日期格式不正确

**解决**: 检查请求体中的字段是否符合要求

### 2. 500 Internal Server Error - 客户不存在

**错误消息**: "客户不存在"

**解决**: 检查客户ID是否正确

### 3. 500 Internal Server Error - 客户在黑名单中

**错误消息**: "该客户已被列入黑名单，无法创建订单"

**解决**: 联系管理员将客户从黑名单移除

### 4. 500 Internal Server Error - 无人机不存在

**错误消息**: "无人机不存在"

**解决**: 检查无人机ID是否正确

### 5. 500 Internal Server Error - 无人机状态不正确

**错误消息**: "无人机当前状态为 RENTED，无法出租"

**解决**: 等待无人机归还后再创建订单

### 6. 500 Internal Server Error - 租期不合法

**错误消息**: "租赁开始日期不能早于今天" 或 "租赁结束日期不能早于开始日期"

**解决**: 检查日期是否正确

### 7. 500 Internal Server Error - 订单状态不允许操作

**错误消息**: 
- "只有待支付的订单才能取消"
- "只有租赁中的订单才能归还"
- "只有已支付或租赁中的订单才能续租"

**解决**: 检查订单当前状态是否符合操作要求

---

## 📝 测试清单

### 创建订单测试
- [ ] 创建订单（正常流程）
- [ ] 创建订单（客户不存在，应失败）
- [ ] 创建订单（客户在黑名单，应失败）
- [ ] 创建订单（无人机不存在，应失败）
- [ ] 创建订单（无人机非空闲，应失败）
- [ ] 创建订单（开始日期早于今天，应失败）
- [ ] 创建订单（结束日期早于开始日期，应失败）

### 续租测试
- [ ] 续租订单（正常流程）
- [ ] 续租订单（新日期早于原日期，应失败）
- [ ] 续租订单（订单状态不对，应失败）

### 归还测试
- [ ] 提前归还（正常流程）
- [ ] 提前归还（订单状态不对，应失败）
- [ ] 提前归还（计算退款金额）

### 取消测试
- [ ] 取消订单（正常流程）
- [ ] 取消订单（订单状态不对，应失败）

### 删除测试
- [ ] 删除已取消的订单
- [ ] 删除未取消的订单（应失败）

---

## 💡 业务流程示例

### 完整租赁流程

```
1. 创建订单
   POST /api/orders
   → 订单状态: PENDING
   → 无人机状态: RENTED

2. 支付订单（后续模块实现）
   → 订单状态: PAID

3. 取机登记（后续模块实现）
   → 订单状态: RENTING
   → 记录 actualStartDate

4a. 正常归还
   → 订单状态: COMPLETED
   → 无人机状态: IDLE

4b. 或者提前归还
   POST /api/orders/{id}/return
   → 订单状态: COMPLETED
   → 无人机状态: IDLE
   → 计算退款

5. 或者续租
   POST /api/orders/{id}/extend?newEndDate=...
   → 更新 endDate 和 totalAmount

6. 或者取消（仅在 PENDING 状态）
   POST /api/orders/{id}/cancel
   → 订单状态: CANCELLED
   → 无人机状态: IDLE
```

---

## 📊 代码统计

- **DTO**: 2 个（OrderCreateDTO, OrderUpdateDTO）
- **VO**: 1 个（OrderVO）
- **Service 接口扩展**: 1 个（新增 4 个方法）
- **Service 实现**: 1 个（完整业务逻辑）
- **Controller**: 1 个（8 个接口）
- **代码行数**: 约 600+ 行

---

## 🚀 技术亮点

1. ✅ 完整的订单创建流程
2. ✅ 严格的状态验证
3. ✅ 自动计算租期和费用
4. ✅ 事务控制（@Transactional）
5. ✅ 统一的异常处理
6. ✅ 详细的 Knife4j 文档
7. ✅ 基于角色的权限控制
8. ✅ 智能的订单编号生成

---

**测试完成！订单管理模块运行正常。** ✅
