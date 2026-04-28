-- ============================================
-- 图书馆管理系统 - 管理员表初始化脚本
-- ============================================

-- 创建管理员表
CREATE TABLE IF NOT EXISTS admins (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '管理员ID',
    admin_name VARCHAR(50) NOT NULL COMMENT '管理员姓名',
    admin_phone VARCHAR(20) NOT NULL UNIQUE COMMENT '管理员手机号',
    password VARCHAR(255) NOT NULL COMMENT '密码（BCrypt加密）',
    user_role TINYINT NOT NULL DEFAULT 2 COMMENT '角色：1-超级管理员，2-普通管理员',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_admin_phone (admin_phone),
    INDEX idx_user_role (user_role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- 插入初始管理员账号
-- 手机号: 18392462574
-- 密码: admin123 (BCrypt加密后的值)
-- 角色: 1 (超级管理员)
-- 注意：如果登录失败，请通过 POST /api/admin 接口创建新管理员，或使用下面的测试接口生成新密码
-- 测试接口: GET /api/test/bcrypt?password=admin123
INSERT INTO admins (admin_name, admin_phone, password, user_role) 
VALUES ('超级管理员', '18392462574', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 1);

-- 如果上面的密码不正确，可以先删除这条数据，然后通过接口创建：
-- DELETE FROM admins WHERE admin_phone = '18392462574';
-- 然后调用 POST /api/admin 创建新管理员

-- 可选：插入一个普通管理员用于测试
-- 手机号: 13900139000
-- 密码: admin123
-- 角色: 2 (普通管理员，只有查询权限)
-- INSERT INTO admins (admin_name, admin_phone, password, user_role) 
-- VALUES ('普通管理员', '13900139000', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 2);
