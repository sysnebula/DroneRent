-- ============================================================================
-- 无人机出租后台管理系统 (DroneRent) - 数据库完整脚本
-- 版本: 2.0
-- 数据库: MySQL 8.0+
-- 字符集: utf8mb4 / utf8mb4_unicode_ci
--
-- 用法:
--   mysql -u root -p < schema.sql
-- ============================================================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS drone_rental
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE drone_rental;

-- ============================================================================
-- 1. 系统用户表 (sys_user)
--    管理员和员工账号
-- ============================================================================
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    id              BIGINT       NOT NULL AUTO_INCREMENT  COMMENT '用户ID',
    username        VARCHAR(50)  NOT NULL                 COMMENT '用户名',
    password        VARCHAR(100) NOT NULL                 COMMENT '密码（BCrypt加密）',
    real_name       VARCHAR(50)                           COMMENT '真实姓名',
    phone           VARCHAR(20)                           COMMENT '手机号',
    email           VARCHAR(100)                          COMMENT '邮箱',
    avatar          VARCHAR(255)                          COMMENT '头像URL',
    role            VARCHAR(20)  NOT NULL DEFAULT 'EMPLOYEE' COMMENT '角色: ADMIN/EMPLOYEE',
    status          TINYINT      NOT NULL DEFAULT 1       COMMENT '状态: 0-禁用, 1-启用',
    deleted         TINYINT      NOT NULL DEFAULT 0       COMMENT '逻辑删除: 0-未删除, 1-已删除',
    create_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username),
    KEY idx_phone (phone),
    KEY idx_status (status),
    KEY idx_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统用户表';

-- ============================================================================
-- 2. 客户表 (customer)
--    租赁客户信息
-- ============================================================================
DROP TABLE IF EXISTS customer;
CREATE TABLE customer (
    id               BIGINT       NOT NULL AUTO_INCREMENT COMMENT '客户ID',
    customer_no      VARCHAR(50)  NOT NULL                COMMENT '客户编号',
    name             VARCHAR(50)  NOT NULL                COMMENT '姓名',
    id_card          VARCHAR(18)  NOT NULL                COMMENT '身份证号',
    phone            VARCHAR(20)  NOT NULL                COMMENT '手机号',
    email            VARCHAR(100)                         COMMENT '邮箱',
    address          VARCHAR(255)                         COMMENT '地址',
    driver_license_no VARCHAR(50)                         COMMENT '驾驶证/飞行执照号',
    credit_score     INT          NOT NULL DEFAULT 100    COMMENT '信用分数（满分100）',
    blacklist        TINYINT      NOT NULL DEFAULT 0      COMMENT '黑名单: 0-否, 1-是',
    remark           TEXT                                 COMMENT '备注',
    deleted          TINYINT      NOT NULL DEFAULT 0      COMMENT '逻辑删除',
    create_time      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_customer_no (customer_no),
    UNIQUE KEY uk_id_card (id_card),
    KEY idx_phone (phone),
    KEY idx_blacklist (blacklist),
    KEY idx_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='客户表';

-- ============================================================================
-- 3. 无人机设备表 (drone)
-- ============================================================================
DROP TABLE IF EXISTS drone;
CREATE TABLE drone (
    id                    BIGINT         NOT NULL AUTO_INCREMENT COMMENT '无人机ID',
    drone_no              VARCHAR(50)    NOT NULL                COMMENT '无人机编号',
    brand                 VARCHAR(50)    NOT NULL                COMMENT '品牌',
    model                 VARCHAR(50)    NOT NULL                COMMENT '型号',
    serial_number         VARCHAR(100)                          COMMENT '序列号',
    purchase_date         DATE                                  COMMENT '购买日期',
    purchase_price        DECIMAL(10,2)                         COMMENT '购买价格',
    daily_rental_price    DECIMAL(10,2) NOT NULL                COMMENT '日租金',
    deposit_amount        DECIMAL(10,2) NOT NULL                COMMENT '押金金额',
    status                VARCHAR(20)   NOT NULL DEFAULT 'IDLE' COMMENT '状态: IDLE-空闲, RENTED-出租中, MAINTENANCE-维修中, SCRAPPED-报废',
    flight_hours          DECIMAL(10,2)          DEFAULT 0.00   COMMENT '累计飞行时长(小时)',
    total_flights         INT                    DEFAULT 0      COMMENT '累计飞行次数',
    last_maintenance_date DATE                                  COMMENT '上次维护日期',
    next_maintenance_date DATE                                  COMMENT '下次维护日期',
    description           TEXT                                  COMMENT '描述信息',
    images                TEXT                                  COMMENT '图片URL列表(JSON数组)',
    specs                 JSON                                  COMMENT '规格参数(JSON对象)',
    deleted               TINYINT       NOT NULL DEFAULT 0      COMMENT '逻辑删除',
    create_time           DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time           DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_drone_no (drone_no),
    UNIQUE KEY uk_serial_number (serial_number),
    KEY idx_status (status),
    KEY idx_brand_model (brand, model)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='无人机设备表';

-- ============================================================================
-- 4. 订单表 (orders)
--    租赁订单
-- ============================================================================
DROP TABLE IF EXISTS orders;
CREATE TABLE orders (
    id              BIGINT         NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    order_no        VARCHAR(50)    NOT NULL                COMMENT '订单编号',
    customer_id     BIGINT         NOT NULL                COMMENT '客户ID',
    drone_id        BIGINT         NOT NULL                COMMENT '无人机ID',
    user_id         BIGINT         NOT NULL                COMMENT '经办人ID(员工)',
    start_date      DATE           NOT NULL                COMMENT '租赁开始日期',
    end_date        DATE           NOT NULL                COMMENT '租赁结束日期',
    actual_start_date DATETIME                             COMMENT '实际取机时间',
    actual_end_date   DATETIME                             COMMENT '实际还机时间',
    rental_days     INT            NOT NULL                COMMENT '租赁天数',
    daily_price     DECIMAL(10,2)  NOT NULL                COMMENT '日租金',
    total_amount    DECIMAL(10,2)  NOT NULL                COMMENT '订单总金额',
    deposit_amount  DECIMAL(10,2)  NOT NULL                COMMENT '押金金额',
    paid_amount     DECIMAL(10,2)           DEFAULT 0.00   COMMENT '已支付金额',
    refund_amount   DECIMAL(10,2)           DEFAULT 0.00   COMMENT '已退款金额',
    damage_fee      DECIMAL(10,2)           DEFAULT 0.00   COMMENT '损坏赔偿费',
    overdue_fee     DECIMAL(10,2)           DEFAULT 0.00   COMMENT '逾期费用',
    discount_amount DECIMAL(10,2)           DEFAULT 0.00   COMMENT '优惠金额',
    status          VARCHAR(20)    NOT NULL DEFAULT 'PENDING' COMMENT '状态: PENDING-待支付, PAID-已支付, RENTING-租赁中, COMPLETED-已完成, CANCELLED-已取消, REFUNDED-已退款',
    payment_method  VARCHAR(20)                            COMMENT '支付方式: WECHAT-微信, ALIPAY-支付宝, CASH-现金, CARD-刷卡',
    payment_time    DATETIME                               COMMENT '支付时间',
    pickup_person   VARCHAR(50)                            COMMENT '取机人姓名',
    pickup_phone    VARCHAR(20)                            COMMENT '取机人电话',
    return_person   VARCHAR(50)                            COMMENT '还机人姓名',
    return_phone    VARCHAR(20)                            COMMENT '还机人电话',
    remark          TEXT                                   COMMENT '订单备注',
    cancel_reason   TEXT                                   COMMENT '取消原因',
    deleted         TINYINT       NOT NULL DEFAULT 0       COMMENT '逻辑删除',
    create_time     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_order_no (order_no),
    KEY idx_customer_id (customer_id),
    KEY idx_drone_id (drone_id),
    KEY idx_user_id (user_id),
    KEY idx_status (status),
    KEY idx_start_date (start_date),
    KEY idx_end_date (end_date),
    CONSTRAINT fk_orders_customer FOREIGN KEY (customer_id) REFERENCES customer(id),
    CONSTRAINT fk_orders_drone    FOREIGN KEY (drone_id)    REFERENCES drone(id),
    CONSTRAINT fk_orders_user     FOREIGN KEY (user_id)     REFERENCES sys_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';

-- ============================================================================
-- 5. 维修记录表 (maintenance_record)
-- ============================================================================
DROP TABLE IF EXISTS maintenance_record;
CREATE TABLE maintenance_record (
    id                  BIGINT        NOT NULL AUTO_INCREMENT COMMENT '维修记录ID',
    drone_id            BIGINT        NOT NULL                COMMENT '无人机ID',
    maintenance_type    VARCHAR(20)   NOT NULL                COMMENT '维修类型: ROUTINE-常规保养, REPAIR-故障维修, INSPECTION-检查',
    start_date          DATETIME      NOT NULL                COMMENT '维修开始时间',
    end_date            DATETIME                              COMMENT '维修结束时间',
    maintenance_cost    DECIMAL(10,2)                         COMMENT '维修费用',
    maintenance_content TEXT                                  COMMENT '维修内容',
    fault_description   TEXT                                  COMMENT '故障描述',
    solution            TEXT                                  COMMENT '解决方案',
    parts_replaced      JSON                                  COMMENT '更换零件(JSON数组)',
    technician          VARCHAR(50)                           COMMENT '维修技师',
    status              VARCHAR(20)   NOT NULL DEFAULT 'IN_PROGRESS' COMMENT '状态: IN_PROGRESS-维修中, COMPLETED-已完成, CANCELLED-已取消',
    remark              TEXT                                  COMMENT '备注',
    deleted             TINYINT       NOT NULL DEFAULT 0      COMMENT '逻辑删除',
    create_time         DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time         DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    KEY idx_drone_id (drone_id),
    KEY idx_status (status),
    KEY idx_start_date (start_date),
    CONSTRAINT fk_maintenance_drone FOREIGN KEY (drone_id) REFERENCES drone(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='维修记录表';

-- ============================================================================
-- 6. 库存变动记录表 (inventory_log)
--    记录无人机状态的每次变更
-- ============================================================================
DROP TABLE IF EXISTS inventory_log;
CREATE TABLE inventory_log (
    id               BIGINT       NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    drone_id         BIGINT       NOT NULL                COMMENT '无人机ID',
    change_type      VARCHAR(20)  NOT NULL                COMMENT '变动类型: PURCHASE-采购, RENT_OUT-出租, RETURN-归还, MAINTENANCE-维修, SCRAP-报废, CANCEL_ORDER-取消订单',
    old_status       VARCHAR(20)                          COMMENT '原状态',
    new_status       VARCHAR(20)                          COMMENT '新状态',
    related_order_no VARCHAR(50)                          COMMENT '关联订单号',
    operator_id      BIGINT       NOT NULL                COMMENT '操作人ID',
    operation_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    remark           TEXT                                COMMENT '备注',
    deleted          TINYINT      NOT NULL DEFAULT 0      COMMENT '逻辑删除',
    create_time      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_drone_id (drone_id),
    KEY idx_change_type (change_type),
    KEY idx_operation_time (operation_time),
    CONSTRAINT fk_inventory_drone    FOREIGN KEY (drone_id)    REFERENCES drone(id),
    CONSTRAINT fk_inventory_operator FOREIGN KEY (operator_id) REFERENCES sys_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='库存变动记录表';

-- ============================================================================
-- 7. 财务流水表 (finance_transaction)
--    记录每一笔收支
-- ============================================================================
DROP TABLE IF EXISTS finance_transaction;
CREATE TABLE finance_transaction (
    id               BIGINT         NOT NULL AUTO_INCREMENT COMMENT '流水ID',
    transaction_no   VARCHAR(50)    NOT NULL                COMMENT '流水号',
    order_id         BIGINT                                  COMMENT '关联订单ID',
    transaction_type VARCHAR(20)    NOT NULL                COMMENT '交易类型: RENTAL_FEE-租金, DEPOSIT-押金, DAMAGE_FEE-赔偿费, OVERDUE_FEE-逾期费, REFUND-退款, MAINTENANCE_FEE-维修费',
    amount           DECIMAL(10,2)  NOT NULL                COMMENT '金额',
    direction        VARCHAR(10)    NOT NULL                COMMENT '方向: INCOME-收入, EXPENSE-支出',
    payment_method   VARCHAR(20)                            COMMENT '支付方式: WECHAT-微信, ALIPAY-支付宝, CASH-现金, CARD-刷卡',
    transaction_time DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '交易时间',
    operator_id      BIGINT         NOT NULL                COMMENT '操作人ID',
    remark           TEXT                                  COMMENT '备注',
    deleted          TINYINT        NOT NULL DEFAULT 0      COMMENT '逻辑删除',
    create_time      DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_transaction_no (transaction_no),
    KEY idx_order_id (order_id),
    KEY idx_transaction_type (transaction_type),
    KEY idx_transaction_time (transaction_time),
    KEY idx_direction (direction),
    CONSTRAINT fk_finance_order    FOREIGN KEY (order_id)    REFERENCES orders(id),
    CONSTRAINT fk_finance_operator FOREIGN KEY (operator_id) REFERENCES sys_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='财务流水表';

-- ============================================================================
-- 8. 角色权限表 (sys_role_permission)
--    管理 RBAC 权限分配
-- ============================================================================
DROP TABLE IF EXISTS sys_role_permission;
CREATE TABLE sys_role_permission (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    role_code   VARCHAR(20)  NOT NULL                COMMENT '角色代码: ADMIN/EMPLOYEE',
    permission  VARCHAR(100) NOT NULL                COMMENT '权限标识',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_role_permission (role_code, permission),
    KEY idx_role_code (role_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限表';

-- ============================================================================
-- 种子数据
-- ============================================================================

-- 管理员账号 (admin / admin123)
INSERT INTO sys_user (username, password, real_name, phone, email, role, status)
VALUES ('admin',
        '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi',
        '系统管理员', '13800138000', 'admin@dronerent.com', 'ADMIN', 1);

-- 员工账号 (employee001 / employee123)
INSERT INTO sys_user (username, password, real_name, phone, email, role, status)
VALUES ('employee001',
        '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi',
        '张三', '13800138001', 'zhangsan@dronerent.com', 'EMPLOYEE', 1);

-- 无人机测试数据
INSERT INTO drone (drone_no, brand, model, serial_number, purchase_date, purchase_price,
                   daily_rental_price, deposit_amount, status, flight_hours, total_flights, description)
VALUES
('DRONE001', 'DJI',   'Mavic 3',     'SN2024001', '2024-01-15', 12999.00, 299.00, 3000.00, 'IDLE', 50.50, 120, '大疆 Mavic 3 专业航拍无人机'),
('DRONE002', 'DJI',   'Air 2S',      'SN2024002', '2024-02-20',  7999.00, 199.00, 2000.00, 'IDLE', 30.20,  80, '大疆 Air 2S 轻便航拍无人机'),
('DRONE003', 'Autel', 'EVO II Pro',  'SN2024003', '2024-03-10', 15999.00, 399.00, 4000.00, 'IDLE', 20.00,  50, '道通 EVO II Pro 专业级无人机');

-- 客户测试数据
INSERT INTO customer (customer_no, name, id_card, phone, email, address, driver_license_no, credit_score)
VALUES
('CUST001', '李四', '110101199001011234', '13900139000', 'lisi@example.com',   '北京市朝阳区xxx街道', 'FL2024001', 100),
('CUST002', '王五', '110101199002022345', '13900139001', 'wangwu@example.com', '上海市浦东新区xxx路', 'FL2024002',  95);

-- 默认角色权限
-- ADMIN 拥有全部权限
INSERT INTO sys_role_permission (role_code, permission) VALUES
('ADMIN', 'user:create'), ('ADMIN', 'user:update'), ('ADMIN', 'user:delete'), ('ADMIN', 'user:view'),
('ADMIN', 'drone:create'), ('ADMIN', 'drone:update'), ('ADMIN', 'drone:delete'), ('ADMIN', 'drone:view'),
('ADMIN', 'order:create'), ('ADMIN', 'order:update'), ('ADMIN', 'order:delete'), ('ADMIN', 'order:view'),
('ADMIN', 'customer:create'), ('ADMIN', 'customer:update'), ('ADMIN', 'customer:delete'), ('ADMIN', 'customer:view'),
('ADMIN', 'finance:view'), ('ADMIN', 'report:view');

-- EMPLOYEE 拥有受限权限
INSERT INTO sys_role_permission (role_code, permission) VALUES
('EMPLOYEE', 'user:view'),
('EMPLOYEE', 'drone:view'),
('EMPLOYEE', 'order:create'), ('EMPLOYEE', 'order:view'),
('EMPLOYEE', 'customer:create'), ('EMPLOYEE', 'customer:view');

-- ============================================================================
-- 诊断查询 (可选执行)
-- ============================================================================
-- SELECT CONCAT('数据库: ', DATABASE()) AS info;
-- SELECT CONCAT('用户数: ', COUNT(*)) FROM sys_user WHERE deleted = 0;
-- SELECT CONCAT('客户数: ', COUNT(*)) FROM customer WHERE deleted = 0;
-- SELECT CONCAT('无人机数: ', COUNT(*)) FROM drone WHERE deleted = 0;
-- SELECT CONCAT('订单数: ', COUNT(*)) FROM orders WHERE deleted = 0;
-- SELECT '>>> 数据库初始化完成 <<<' AS result;
