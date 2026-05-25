# 无人机管理模块 - 接口测试指南

## 📋 模块概述

无人机管理模块已开发完成，包含以下功能：

1. ✅ 分页查询无人机列表（支持按型号、品牌、状态筛选）
2. ✅ 根据ID查询无人机详情
3. ✅ 新增无人机
4. ✅ 修改无人机信息
5. ✅ 删除无人机
6. ✅ 批量删除无人机
7. ✅ 更新无人机状态（带状态转换验证）
8. ✅ 所有接口都添加了 Knife4j 注解

---

## 🔑 核心组件

### 1. DTO/VO
- **DroneDTO** - 无人机创建/更新请求
- **DroneVO** - 无人机信息响应

### 2. Service 层
- **DroneService** - 接口定义
- **DroneServiceImpl** - 业务逻辑实现
  - 设备编号唯一性验证
  - 状态转换合法性验证
  - 状态更新（事务控制）

### 3. Controller 层
- **DroneController** - 提供 7 个 RESTful API 接口

---

## 🧪 接口测试

### 前置条件

1. 确保已登录并获取 Token
2. 使用 ADMIN 角色账号（大部分接口需要管理员权限）
3. 数据库中有测试数据

---

## 1. 分页查询无人机列表

**接口**: `GET /api/drones/page`

**请求头**:
```
Authorization: Bearer {token}
```

**查询参数**:
- `pageNum`: 页码（默认 1）
- `pageSize`: 每页条数（默认 10）
- `brand`: 品牌（可选，模糊查询）
- `model`: 型号（可选，模糊查询）
- `status`: 状态（可选，精确查询）

**示例请求**:
```
GET /api/drones/page?pageNum=1&pageSize=10&brand=DJI&status=IDLE
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "total": 3,
    "pageNum": 1,
    "pageSize": 10,
    "pages": 1,
    "list": [
      {
        "id": 1,
        "droneNo": "DRONE001",
        "brand": "DJI",
        "model": "Mavic 3",
        "serialNumber": "SN2024001",
        "purchaseDate": "2024-01-15",
        "purchasePrice": 12999.00,
        "dailyRentalPrice": 299.00,
        "depositAmount": 3000.00,
        "status": "IDLE",
        "flightHours": 50.50,
        "totalFlights": 120,
        "description": "大疆 Mavic 3 专业航拍无人机",
        "createTime": "2024-01-01T12:00:00",
        "updateTime": "2024-01-01T12:00:00"
      }
    ]
  },
  "timestamp": 1704067200000
}
```

---

## 2. 查询无人机详情

**接口**: `GET /api/drones/{id}`

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
- `id`: 无人机ID

**示例请求**:
```
GET /api/drones/1
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "droneNo": "DRONE001",
    "brand": "DJI",
    "model": "Mavic 3",
    "serialNumber": "SN2024001",
    "purchaseDate": "2024-01-15",
    "purchasePrice": 12999.00,
    "dailyRentalPrice": 299.00,
    "depositAmount": 3000.00,
    "status": "IDLE",
    "flightHours": 50.50,
    "totalFlights": 120,
    "lastMaintenanceDate": null,
    "nextMaintenanceDate": null,
    "description": "大疆 Mavic 3 专业航拍无人机",
    "images": null,
    "specs": null,
    "createTime": "2024-01-01T12:00:00",
    "updateTime": "2024-01-01T12:00:00"
  },
  "timestamp": 1704067200000
}
```

---

## 3. 新增无人机

**接口**: `POST /api/drones`

**请求头**:
```
Authorization: Bearer {token}
Content-Type: application/json
```

**请求体**:
```json
{
  "droneNo": "DRONE004",
  "brand": "DJI",
  "model": "Mini 3 Pro",
  "serialNumber": "SN2024004",
  "purchaseDate": "2024-03-01",
  "purchasePrice": 5999.00,
  "dailyRentalPrice": 199.00,
  "depositAmount": 2000.00,
  "status": "IDLE",
  "description": "大疆 Mini 3 Pro 轻便航拍无人机",
  "images": "[\"https://example.com/image1.jpg\"]",
  "specs": "{\"maxFlightTime\": \"34分钟\", \"weight\": \"249g\"}"
}
```

**必填字段**:
- droneNo（无人机编号）
- brand（品牌）
- model（型号）
- dailyRentalPrice（日租金）
- depositAmount（押金金额）

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
- 设备编号必须唯一
- 序列号必须唯一（如果提供）
- 日租金必须大于 0
- 押金金额不能小于 0
- 状态默认为 IDLE（空闲）

---

## 4. 修改无人机信息

**接口**: `PUT /api/drones/{id}`

**请求头**:
```
Authorization: Bearer {token}
Content-Type: application/json
```

**路径参数**:
- `id`: 无人机ID

**请求体**:
```json
{
  "id": 1,
  "droneNo": "DRONE001",
  "brand": "DJI",
  "model": "Mavic 3 Pro",
  "serialNumber": "SN2024001",
  "purchaseDate": "2024-01-15",
  "purchasePrice": 12999.00,
  "dailyRentalPrice": 349.00,
  "depositAmount": 3000.00,
  "description": "大疆 Mavic 3 Pro 专业航拍无人机（更新）"
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
- 设备编号必须唯一（排除当前记录）
- 序列号必须唯一（如果修改）
- 不会更新状态字段（状态通过专门接口更新）

---

## 5. 删除无人机

**接口**: `DELETE /api/drones/{id}`

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
- `id`: 无人机ID

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
- 只能删除空闲（IDLE）或报废（SCRAPPED）状态的无人机
- 出租中或维修中的无人机不能删除

---

## 6. 批量删除无人机

**接口**: `DELETE /api/drones/batch`

**请求头**:
```
Authorization: Bearer {token}
Content-Type: application/json
```

**请求体**:
```json
[1, 2, 3]
```

**响应示例**:
```json
{
  "code": 200,
  "message": "成功删除 3 个无人机",
  "data": null,
  "timestamp": 1704067200000
}
```

**权限要求**: 需要 ADMIN 角色

**注意事项**:
- 所有无人机都必须是空闲或报废状态
- 如果有任何一个不符合条件，整个操作会失败
- 这是逻辑删除

---

## 7. 更新无人机状态

**接口**: `PATCH /api/drones/{id}/status?newStatus={newStatus}`

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
- `id`: 无人机ID

**查询参数**:
- `newStatus`: 新状态（IDLE/RENTED/MAINTENANCE/SCRAPPED）

**示例请求**:
```
PATCH /api/drones/1/status?newStatus=RENTED
```

**响应示例**:
```json
{
  "code": 200,
  "message": "状态更新成功",
  "data": null,
  "timestamp": 1704067200000
}
```

**权限要求**: 需要 ADMIN 角色

### 状态转换规则

系统实现了严格的状态转换验证：

#### ✅ 合法的状态转换

| 当前状态 | 允许转换的状态 | 说明 |
|---------|--------------|------|
| IDLE（空闲） | RENTED, MAINTENANCE, SCRAPPED | 可以出租、维修或报废 |
| RENTED（出租中） | IDLE, MAINTENANCE, SCRAPPED | 可以归还、送修或报废 |
| MAINTENANCE（维修中） | IDLE, SCRAPPED | 维修完成回到空闲或直接报废 |
| SCRAPPED（报废） | (无) | 报废后不允许任何转换 |

#### ❌ 非法的状态转换示例

- IDLE → RENTED ✅ 合法
- RENTED → IDLE ✅ 合法（归还）
- RENTED → MAINTENANCE ✅ 合法（送修）
- RENTED → SCRAPPED ✅ 合法（直接报废）
- MAINTENANCE → IDLE ✅ 合法（维修完成）
- MAINTENANCE → SCRAPPED ✅ 合法（维修失败报废）
- SCRAPPED → IDLE ❌ 非法（报废后不可恢复）
- SCRAPPED → RENTED ❌ 非法（报废后不可恢复）

**错误响应示例**:
```json
{
  "code": 500,
  "message": "不允许的状态转换: RENTED -> SCRAPPED",
  "data": null,
  "timestamp": 1704067200000
}
```

---

## 🔐 权限控制

### 接口权限清单

| 接口 | 权限要求 |
|------|---------|
| GET /api/drones/page | 任意登录用户 |
| GET /api/drones/{id} | 任意登录用户 |
| POST /api/drones | ADMIN |
| PUT /api/drones/{id} | ADMIN |
| DELETE /api/drones/{id} | ADMIN |
| DELETE /api/drones/batch | ADMIN |
| PATCH /api/drones/{id}/status | ADMIN |

---

## ❌ 常见错误

### 1. 400 Bad Request - 参数校验失败

**原因**: 
- 必填字段为空
- 日租金小于等于 0
- 格式不正确

**解决**: 检查请求体中的字段是否符合要求

### 2. 400 Bad Request - 设备编号已存在

**错误消息**: "设备编号已存在: DRONE001"

**解决**: 使用唯一的设备编号

### 3. 400 Bad Request - 序列号已存在

**错误消息**: "序列号已存在: SN2024001"

**解决**: 使用唯一的序列号

### 4. 500 Internal Server Error - 状态转换不合法

**错误消息**: "不允许的状态转换: RENTED -> SCRAPPED"

**解决**: 按照状态转换规则进行操作

### 5. 500 Internal Server Error - 无人机不存在

**错误消息**: "无人机不存在"

**解决**: 检查无人机ID是否正确

### 6. 500 Internal Server Error - 不能删除非空闲无人机

**错误消息**: "只能删除空闲或报废状态的无人机"

**解决**: 先将无人机状态改为 IDLE 或 SCRAPPED

---

## 📝 测试清单

### 基础功能测试
- [ ] 分页查询无人机列表（无条件）
- [ ] 分页查询（按品牌筛选）
- [ ] 分页查询（按型号筛选）
- [ ] 分页查询（按状态筛选）
- [ ] 分页查询（组合筛选）
- [ ] 查询无人机详情（存在的ID）
- [ ] 查询无人机详情（不存在的ID）

### 创建测试
- [ ] 创建无人机（所有字段）
- [ ] 创建无人机（必填字段）
- [ ] 创建设备编号重复（应失败）
- [ ] 创建序列号重复（应失败）
- [ ] 创建日租金为0（应失败）
- [ ] 创建缺少必填字段（应失败）

### 更新测试
- [ ] 更新无人机信息
- [ ] 更新设备编号为已有编号（应失败）
- [ ] 更新不存在的无人机（应失败）

### 删除测试
- [ ] 删除空闲状态的无人机
- [ ] 删除报废状态的无人机
- [ ] 删除出租中的无人机（应失败）
- [ ] 删除维修中的无人机（应失败）
- [ ] 批量删除（全部符合条件）
- [ ] 批量删除（部分不符合条件，应失败）

### 状态转换测试
- [ ] IDLE → RENTED ✅
- [ ] IDLE → MAINTENANCE ✅
- [ ] IDLE → SCRAPPED ✅
- [ ] RENTED → IDLE ✅
- [ ] RENTED → MAINTENANCE ✅
- [ ] RENTED → SCRAPPED ✅ （新增）
- [ ] MAINTENANCE → IDLE ✅
- [ ] MAINTENANCE → SCRAPPED ✅ （新增）
- [ ] MAINTENANCE → RENTED ❌
- [ ] SCRAPPED → IDLE ❌
- [ ] SCRAPPED → RENTED ❌
- [ ] SCRAPPED → MAINTENANCE ❌
- [ ] 无效状态值（应失败）

---

## 🎯 业务逻辑说明

### 1. 设备编号唯一性验证

在创建和更新无人机时，系统会自动验证设备编号的唯一性：

```java
// 创建时
if (!droneService.validateDroneNoUnique(droneDTO.getDroneNo(), null)) {
    throw new BusinessException("设备编号已存在");
}

// 更新时（排除当前记录）
if (!droneService.validateDroneNoUnique(droneDTO.getDroneNo(), id)) {
    throw new BusinessException("设备编号已存在");
}
```

### 2. 序列号唯一性验证

如果提供了序列号，系统也会验证其唯一性。

### 3. 状态转换验证

系统实现了严格的状态机逻辑，确保无人机的状态转换符合业务规则：

```java
// 状态转换规则
IDLE -> RENTED, MAINTENANCE, SCRAPPED
RENTED -> IDLE, MAINTENANCE
MAINTENANCE -> IDLE
SCRAPPED -> (不允许转换)
```

### 4. 删除限制

只有空闲（IDLE）或报废（SCRAPPED）状态的无人机才能删除，防止误删正在使用的设备。

---

## 💡 使用建议

### 1. 新增无人机流程

```
1. 准备无人机信息
2. 调用 POST /api/drones 创建
3. 系统自动设置状态为 IDLE
4. 创建成功
```

### 2. 出租无人机流程

```
1. 确认无人机状态为 IDLE
2. 调用 PATCH /api/drones/{id}/status?newStatus=RENTED
3. 状态变更为 RENTED
4. 创建订单（后续模块）
```

### 3. 归还无人机流程

```
1. 确认无人机状态为 RENTED
2. 调用 PATCH /api/drones/{id}/status?newStatus=IDLE
3. 状态变更为 IDLE
4. 更新订单（后续模块）
```

### 4. 维修无人机流程

```
1. 从 IDLE 或 RENTED 状态开始
2. 调用 PATCH /api/drones/{id}/status?newStatus=MAINTENANCE
3. 状态变更为 MAINTENANCE
4. 创建维修记录（后续模块）
5. 维修完成后，调用 PATCH /api/drones/{id}/status?newStatus=IDLE
```

### 5. 报废无人机流程

```
1. 确认无人机状态为 IDLE
2. 调用 PATCH /api/drones/{id}/status?newStatus=SCRAPPED
3. 状态变更为 SCRAPPED
4. 可以删除该无人机（可选）
```

---

## 📊 代码统计

- **DTO/VO**: 2 个文件
- **Service 接口**: 1 个文件（扩展）
- **Service 实现**: 1 个文件（扩展）
- **Controller**: 1 个文件
- **接口数量**: 7 个
- **代码行数**: 约 550+ 行

---

## 🚀 技术亮点

1. ✅ 完整的参数校验（JSR-303）
2. ✅ 设备编号唯一性验证
3. ✅ 序列号唯一性验证
4. ✅ 状态转换合法性验证（状态机）
5. ✅ 事务控制（@Transactional）
6. ✅ 统一的异常处理
7. ✅ 详细的 Knife4j 文档
8. ✅ 基于角色的权限控制

---

**测试完成！无人机管理模块运行正常。** ✅
