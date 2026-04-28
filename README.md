# 图书馆管理系统 (Library Management System)

一个基于 Spring Boot 的个人图书管理 API 系统，提供图书的增删改查、借阅归还、用户管理等功能。

## 📋 项目简介

本项目是一个 RESTful API 服务，用于管理个人图书收藏和借阅记录。系统支持用户注册登录、图书管理、借阅记录追踪、管理员后台管理以及热门图书统计等功能。采用分层架构设计，代码结构清晰，易于扩展和维护。

### 系统组成

- **后端 API** (本项目) - Spring Boot 构建的 RESTful API 服务
- **前端后台管理系统** (library-admin) - Vue3 + Element Plus 构建的管理员操作界面

### 核心功能

- ✅ **图书管理** - 图书的增删改查操作
- ✅ **借阅管理** - 图书借阅、归还及借阅记录查询
- ✅ **用户管理** - 用户注册、登录、CRUD 操作
- ✅ **管理员管理** - 管理员登录、CRUD、批量删除、角色权限控制
- ✅ **认证授权** - JWT Token 认证，BCrypt 密码加密
- ✅ **数据统计** - 热门图书统计（借阅次数排行）
- ✅ **分页查询** - 支持分页的图书列表和借阅记录查询
- ✅ **数据校验** - 请求参数自动校验
- ✅ **统一异常处理** - 标准化的错误响应格式
- ✅ **API 文档** - 集成 Swagger UI，自动生成接口文档

## 🛠️ 技术栈

### 后端框架
- **Spring Boot 3.4.5** - Web 应用框架
- **Spring Data JPA** - 数据持久化层
- **Spring Security** - 安全框架
- **Spring Validation** - 数据校验

### 数据库
- **H2 Database** - 开发环境内存数据库
- **MySQL** - 生产环境关系型数据库（可选）

### 认证与安全
- **JWT (jjwt 0.11.5)** - JSON Web Token 认证
- **BCrypt** - 密码加密算法

### API 文档
- **SpringDoc OpenAPI 3 (2.7.0)** - API 文档生成
- **Swagger UI** - 交互式 API 文档界面

### 构建工具
- **Gradle 8.x** - 项目构建和依赖管理
- **Java 17** - 编程语言版本

### 测试框架
- **JUnit 5** - 单元测试框架
- **Mockito** - Mock 测试框架

## 🚀 快速开始

### 前置要求

- JDK 17 或更高版本
- Gradle 8.x（或使用项目自带的 Gradle Wrapper）
- MySQL 8.0+（生产环境可选）

### 克隆项目

```bash
git clone <repository-url>
cd LibraryManagement
```

### 配置环境

项目支持多环境配置，默认使用开发环境（dev）。

#### 开发环境（默认）

使用 H2 内存数据库，无需额外配置：

```bash
# 直接启动即可
./gradlew bootRun
```

访问 H2 Console：`http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:librarydb`
- Username: `sa`
- Password: （留空）

#### 生产环境

1. 安装并配置 MySQL 数据库
2. 创建数据库：
```sql
CREATE DATABASE librarydb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. 初始化管理员表（可选，如需使用后台管理系统）：
```bash
# 执行管理员表初始化脚本
mysql -u root -p librarydb < init_admin.sql
```

4. 修改配置文件 `src/main/resources/application.yml`：
```yaml
spring:
  profiles:
    active: prod
```

5. 配置 `src/main/resources/application-prod.yml` 中的数据库连接信息：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/librarydb?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_secure_password
```

6. 设置环境变量（推荐）：
```bash
export JWT_SECRET="your-super-secret-key-here"
export DB_PASSWORD="your-database-password"
```

## 🗄️ 数据库设计

### MySQL 建表语句

如果需要使用 MySQL 数据库，可以执行以下 SQL 语句创建所有数据表：

#### 1. 用户表 (users)

```sql
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    name VARCHAR(50) NOT NULL COMMENT '用户姓名',
    phone VARCHAR(20) NOT NULL UNIQUE COMMENT '手机号',
    password VARCHAR(255) NOT NULL COMMENT '密码（BCrypt加密）',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
```

**字段说明：**
- `id`: 主键，自增
- `name`: 用户姓名，最大50字符
- `phone`: 手机号，唯一索引，最大20字符
- `password`: BCrypt加密后的密码
- `created_at`: 创建时间，自动生成

#### 2. 图书表 (books)

```sql
CREATE TABLE IF NOT EXISTS books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '图书ID',
    title VARCHAR(200) NOT NULL COMMENT '图书标题',
    author VARCHAR(100) NOT NULL COMMENT '作者',
    is_borrowed TINYINT(1) DEFAULT 0 COMMENT '是否已借阅：0-未借阅，1-已借阅',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_title (title),
    INDEX idx_author (author)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='图书表';
```

**字段说明：**
- `id`: 主键，自增
- `title`: 图书标题，最大200字符
- `author`: 作者，最大100字符
- `is_borrowed`: 借阅状态，0表示未借阅，1表示已借阅
- `created_at`: 创建时间，自动生成
- `updated_at`: 更新时间，自动更新

#### 3. 借阅记录表 (borrow_records)

```sql
CREATE TABLE IF NOT EXISTS borrow_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '记录ID',
    book_id BIGINT NOT NULL COMMENT '图书ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    borrow_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '借阅时间',
    return_time DATETIME DEFAULT NULL COMMENT '归还时间',
    INDEX idx_book_id (book_id),
    INDEX idx_user_id (user_id),
    INDEX idx_borrow_time (borrow_time),
    CONSTRAINT fk_borrow_book FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE,
    CONSTRAINT fk_borrow_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='借阅记录表';
```

**字段说明：**
- `id`: 主键，自增
- `book_id`: 图书ID，外键关联books表
- `user_id`: 用户ID，外键关联users表
- `borrow_time`: 借阅时间
- `return_time`: 归还时间，NULL表示未归还
- 外键约束：删除图书或用户时，自动删除相关借阅记录

#### 4. 管理员表 (admins)

```sql
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
```

**字段说明：**
- `id`: 主键，自增
- `admin_name`: 管理员姓名，最大50字符
- `admin_phone`: 管理员手机号，唯一索引
- `password`: BCrypt加密后的密码
- `user_role`: 角色标识，1为超级管理员，2为普通管理员
- `created_at`: 创建时间，自动生成

### 初始化数据

#### 初始化管理员账号

项目提供了 `init_admin.sql` 脚本用于初始化管理员表和数据：

```bash
# 在 MySQL 中执行
mysql -u root -p librarydb < init_admin.sql
```

或者手动插入初始管理员：

```sql
-- 超级管理员
-- 手机号: 18392462574
-- 密码: admin123 (BCrypt加密)
INSERT INTO admins (admin_name, admin_phone, password, user_role) 
VALUES ('超级管理员', '18392462574', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 1);

-- 普通管理员（可选）
-- 手机号: 13900139000
-- 密码: admin123
INSERT INTO admins (admin_name, admin_phone, password, user_role) 
VALUES ('普通管理员', '13900139000', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 2);
```

### 数据库关系图

```
┌─────────────┐         ┌──────────────────┐         ┌─────────────┐
│   users     │         │ borrow_records   │         │   books     │
├─────────────┤         ├──────────────────┤         ├─────────────┤
│ id (PK)     │◄────────│ user_id (FK)     │         │ id (PK)     │
│ name        │         │ book_id (FK)     │────────►│ title       │
│ phone       │         │ borrow_time      │         │ author      │
│ password    │         │ return_time      │         │ is_borrowed │
│ created_at  │         └──────────────────┘         │ created_at  │
└─────────────┘                                      │ updated_at  │
                                                     └─────────────┘

┌─────────────┐
│   admins    │
├─────────────┤
│ id (PK)     │
│ admin_name  │
│ admin_phone │
│ password    │
│ user_role   │
│ created_at  │
└─────────────┘
```

**关系说明：**
- `users` 和 `books` 通过 `borrow_records` 建立多对多关系
- 一个用户可以借阅多本图书
- 一本图书可以被多个用户借阅（历史记录）
- `admins` 表独立，用于后台管理系统

### 运行项目

#### 方式 1：使用 Gradle（开发模式）

```bash
# Windows
gradlew.bat bootRun

# Linux/Mac
./gradlew bootRun
```

#### 方式 2：打包后运行

```bash
# 构建项目
./gradlew build

# 运行 jar 包
java -jar build/libs/LibraryManagement-0.0.1-SNAPSHOT.jar

# 指定环境
java -jar build/libs/LibraryManagement-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

#### 方式 3：IDE 中运行

在 IntelliJ IDEA 或 Eclipse 中直接运行 `LibraryManagementApplication.java`

### 验证安装

启动成功后，访问以下地址：

- **应用首页**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API 文档 JSON**: http://localhost:8080/v3/api-docs
- **H2 Console** (仅 dev): http://localhost:8080/h2-console

**测试管理员登录：**
```bash
curl -X POST http://localhost:8080/api/admin/login \
  -H "Content-Type: application/json" \
  -d '{
    "adminPhone": "18392462574",
    "password": "admin123"
  }'
```

## 📖 API 文档

### 在线文档

启动应用后访问 [Swagger UI](http://localhost:8080/swagger-ui.html) 查看完整的 API 文档并进行在线测试。

### API 概览

#### 1. 用户管理

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/api/users/register` | 用户注册 |
| POST | `/api/users/login` | 用户登录 |
| GET | `/api/users` | 查询所有用户 |
| GET | `/api/users/{id}` | 查询单个用户 |
| PUT | `/api/users/{id}` | 更新用户信息 |
| DELETE | `/api/users/{id}` | 删除用户 |

#### 2. 图书管理

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/api/books` | 创建图书 |
| GET | `/api/books` | 查询所有图书（分页） |
| GET | `/api/books/{id}` | 查询单个图书 |
| PUT | `/api/books/{id}` | 更新图书 |
| DELETE | `/api/books/{id}` | 删除图书 |
| POST | `/api/books/{bookId}/borrow` | 借阅图书 |
| POST | `/api/books/{bookId}/return` | 归还图书 |
| GET | `/api/books/{bookId}/records` | 查询借阅记录（分页） |
| GET | `/api/books/stat/popular` | 查询热门图书 |

#### 3. 管理员管理

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/api/admin/login` | 管理员登录 |
| GET | `/api/admin/list?page=0&size=10` | 分页查询管理员 |
| POST | `/api/admin` | 创建管理员 |
| PUT | `/api/admin/{id}` | 更新管理员 |
| DELETE | `/api/admin/{id}` | 删除管理员 |
| DELETE | `/api/admin/batch` | 批量删除管理员 |

### 完整 API 接口清单

#### 用户管理接口

| # | 方法 | 路径 | 说明 | 需要认证 |
|---|------|------|------|----------|
| 1 | POST | `/api/users/register` | 用户注册 | ❌ |
| 2 | POST | `/api/users/login` | 用户登录 | ❌ |
| 3 | GET | `/api/users` | 查询所有用户 | ✅ |
| 4 | GET | `/api/users/{id}` | 查询单个用户 | ✅ |
| 5 | PUT | `/api/users/{id}` | 更新用户信息 | ✅ |
| 6 | DELETE | `/api/users/{id}` | 删除用户 | ✅ |
| 7 | DELETE | `/api/users/batch` | 批量删除用户 | ✅ |

#### 图书管理接口

| # | 方法 | 路径 | 说明 | 需要认证 |
|---|------|------|------|----------|
| 8 | POST | `/api/books` | 创建图书 | ✅ |
| 9 | GET | `/api/books?page=0&size=10` | 分页查询图书 | ✅ |
| 10 | GET | `/api/books/{id}` | 查询单个图书 | ✅ |
| 11 | PUT | `/api/books/{id}` | 更新图书 | ✅ |
| 12 | DELETE | `/api/books/{id}` | 删除图书 | ✅ |
| 13 | DELETE | `/api/books/batch` | 批量删除图书 | ✅ |
| 14 | POST | `/api/books/{bookId}/borrow` | 借阅图书 | ✅ |
| 15 | POST | `/api/books/{bookId}/return` | 归还图书 | ✅ |
| 16 | GET | `/api/books/{bookId}/records?page=0&size=10` | 查询借阅记录 | ✅ |
| 17 | GET | `/api/books/{bookId}/borrower-info` | 查询当前借阅人 | ✅ |
| 18 | GET | `/api/books/stat/popular?limit=10` | 查询热门图书 | ✅ |

#### 管理员管理接口

| # | 方法 | 路径 | 说明 | 需要认证 |
|---|------|------|------|----------|
| 19 | POST | `/api/admin/login` | 管理员登录 | ❌ |
| 20 | GET | `/api/admin/list?page=0&size=10` | 分页查询管理员 | ✅ |
| 21 | POST | `/api/admin` | 创建管理员 | ✅ |
| 22 | PUT | `/api/admin/{id}` | 更新管理员 | ✅ |
| 23 | DELETE | `/api/admin/{id}` | 删除管理员 | ✅ |
| 24 | DELETE | `/api/admin/batch` | 批量删除管理员 | ✅ |

### API 示例

#### 1. 用户注册

```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "张三",
    "phone": "13800138000",
    "password": "password123"
  }'
```

**响应：**
```json
{
  "code": 200,
  "message": "用户注册成功",
  "data": {
    "id": 1,
    "name": "张三",
    "phone": "13800138000",
    "createdAt": "2026-04-24T10:30:00"
  }
}
```

#### 2. 用户登录

```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "phone": "13800138000",
    "password": "password123"
  }'
```

**响应：**
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "userId": 1,
    "name": "张三",
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicGhvbmUiOiIxMzgwMDEzODAwMCIsImlhdCI6MTYxNjIzOTAyMiwiZXhwIjoxNjE2MzI1NDIyfQ.xxx"
  }
}
```

#### 3. 创建图书

```bash
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Java编程思想",
    "author": "Bruce Eckel"
  }'
```

**响应：**
```json
{
  "code": 200,
  "message": "图书创建成功",
  "data": {
    "id": 1,
    "title": "Java编程思想",
    "author": "Bruce Eckel",
    "isBorrowed": false
  }
}
```

#### 4. 查询所有图书（分页）

```bash
curl -X GET "http://localhost:8080/api/books?page=0&size=10"
```

**响应：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "content": [
      {
        "id": 1,
        "title": "Java编程思想",
        "author": "Bruce Eckel",
        "isBorrowed": false
      }
    ],
    "totalPages": 1,
    "totalElements": 1
  }
}
```

#### 5. 借阅图书

```bash
curl -X POST http://localhost:8080/api/books/1/borrow \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1
  }'
```

**响应：**
```json
{
  "code": 200,
  "message": "借阅成功",
  "data": {
    "id": 1,
    "title": "Java编程思想",
    "author": "Bruce Eckel",
    "isBorrowed": true
  }
}
```

#### 6. 归还图书

```bash
curl -X POST http://localhost:8080/api/books/1/return \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1
  }'
```

**响应：**
```json
{
  "code": 200,
  "message": "归还成功",
  "data": {
    "id": 1,
    "title": "Java编程思想",
    "author": "Bruce Eckel",
    "isBorrowed": false
  }
}
```

#### 7. 查询热门图书

```bash
curl -X GET "http://localhost:8080/api/books/stat/popular?limit=3"
```

**响应：**
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "title": "Java编程思想",
      "borrowCount": 10
    },
    {
      "id": 2,
      "title": "Spring实战",
      "borrowCount": 8
    },
    {
      "id": 3,
      "title": "深入理解JVM",
      "borrowCount": 5
    }
  ]
}
```

#### 8. 管理员登录

```bash
curl -X POST http://localhost:8080/api/admin/login \
  -H "Content-Type: application/json" \
  -d '{
    "adminPhone": "18392462574",
    "password": "admin123"
  }'
```

**响应：**
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "adminId": 1,
    "adminName": "超级管理员",
    "userRole": 1,
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicGhvbmUiOiIxODM5MjQ2MjU3NCIsInJvbGUiOjEsImlhdCI6MTYxNjIzOTAyMiwiZXhwIjoxNjE2ODQzODIyfQ.xxx"
  }
}
```

#### 9. 查询所有用户

```bash
curl -X GET http://localhost:8080/api/users \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**响应：**
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "张三",
      "phone": "13800138000",
      "createdAt": "2026-04-24T10:30:00"
    },
    {
      "id": 2,
      "name": "李四",
      "phone": "13900139000",
      "createdAt": "2026-04-25T11:00:00"
    }
  ]
}
```

#### 10. 更新用户信息

```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "name": "张三丰",
    "phone": "13800138000"
  }'
```

**响应：**
```json
{
  "code": 200,
  "message": "用户更新成功",
  "data": {
    "id": 1,
    "name": "张三丰",
    "phone": "13800138000",
    "createdAt": "2026-04-24T10:30:00"
  }
}
```

#### 11. 删除用户

```bash
curl -X DELETE http://localhost:8080/api/users/1 \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**响应：**
```json
{
  "code": 200,
  "message": "用户删除成功",
  "data": null
}
```

#### 12. 批量删除用户

```bash
curl -X DELETE http://localhost:8080/api/users/batch \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '[1, 2, 3]'
```

**响应：**
```json
{
  "code": 200,
  "message": "批量删除成功",
  "data": null
}
```

#### 13. 更新图书信息

```bash
curl -X PUT http://localhost:8080/api/books/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "title": "Java编程思想（第4版）",
    "author": "Bruce Eckel"
  }'
```

**响应：**
```json
{
  "code": 200,
  "message": "图书更新成功",
  "data": {
    "id": 1,
    "title": "Java编程思想（第4版）",
    "author": "Bruce Eckel",
    "isBorrowed": false
  }
}
```

#### 14. 删除图书

```bash
curl -X DELETE http://localhost:8080/api/books/1 \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**响应：**
```json
{
  "code": 200,
  "message": "图书删除成功",
  "data": null
}
```

#### 15. 批量删除图书

```bash
curl -X DELETE http://localhost:8080/api/books/batch \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '[1, 2, 3]'
```

**响应：**
```json
{
  "code": 200,
  "message": "批量删除成功",
  "data": null
}
```

#### 16. 查询借阅记录

```bash
curl -X GET "http://localhost:8080/api/books/1/records?page=0&size=10" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**响应：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "content": [
      {
        "id": 1,
        "bookId": 1,
        "userId": 1,
        "borrowTime": "2026-04-20T10:00:00",
        "returnTime": "2026-04-25T15:30:00"
      }
    ],
    "totalPages": 1,
    "totalElements": 1
  }
}
```

#### 17. 查询当前借阅人信息

```bash
curl -X GET http://localhost:8080/api/books/1/borrower-info \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**响应：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "userId": 1,
    "userName": "张三",
    "phone": "13800138000",
    "borrowTime": "2026-04-20T10:00:00"
  }
}
```

#### 18. 分页查询管理员

```bash
curl -X GET "http://localhost:8080/api/admin/list?page=0&size=10" \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN"
```

**响应：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "content": [
      {
        "id": 1,
        "adminName": "超级管理员",
        "adminPhone": "18392462574",
        "userRole": 1,
        "createdAt": "2026-04-24T10:00:00"
      }
    ],
    "totalPages": 1,
    "totalElements": 1
  }
}
```

#### 19. 创建管理员

```bash
curl -X POST http://localhost:8080/api/admin \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN" \
  -d '{
    "adminName": "普通管理员",
    "adminPhone": "13900139000",
    "password": "admin123",
    "userRole": 2
  }'
```

**响应：**
```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": 2,
    "adminName": "普通管理员",
    "adminPhone": "13900139000",
    "userRole": 2,
    "createdAt": "2026-04-28T10:00:00"
  }
}
```

#### 20. 更新管理员信息

```bash
curl -X PUT http://localhost:8080/api/admin/2 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN" \
  -d '{
    "adminName": "高级管理员",
    "userRole": 1
  }'
```

**响应：**
```json
{
  "code": 200,
  "message": "更新成功",
  "data": {
    "id": 2,
    "adminName": "高级管理员",
    "adminPhone": "13900139000",
    "userRole": 1,
    "createdAt": "2026-04-28T10:00:00"
  }
}
```

#### 21. 删除管理员

```bash
curl -X DELETE http://localhost:8080/api/admin/2 \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN"
```

**响应：**
```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

#### 22. 批量删除管理员

```bash
curl -X DELETE http://localhost:8080/api/admin/batch \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN" \
  -d '[2, 3, 4]'
```

**响应：**
```json
{
  "code": 200,
  "message": "批量删除成功",
  "data": null
}
```

### 错误响应格式

所有错误响应遵循统一格式：

```json
{
  "code": 400,
  "message": "错误描述信息",
  "data": null
}
```

常见错误码：
- `400` - 请求参数错误或业务逻辑错误
- `404` - 资源不存在
- `405` - 请求方法不支持
- `500` - 服务器内部错误

## 🏗️ 项目结构

```
LibraryManagement/
├── init_admin.sql                    # 管理员表初始化脚本
├── src/
│   ├── main/
│   │   ├── java/com/kyk/librarymanagement/
│   │   │   ├── config/               # 配置类
│   │   │   │   ├── JwtConfig.java           # JWT配置
│   │   │   │   ├── OpenApiConfig.java       # Swagger配置
│   │   │   │   ├── RedisConfig.java         # Redis配置
│   │   │   │   ├── SecurityConfig.java      # 安全配置
│   │   │   │   └── WebMvcConfig.java        # Web MVC配置
│   │   │   ├── controller/           # 控制器层
│   │   │   │   ├── AdminController.java     # 管理员接口
│   │   │   │   ├── BookController.java      # 图书接口
│   │   │   │   ├── TestController.java      # 测试接口
│   │   │   │   └── UserController.java      # 用户接口
│   │   │   ├── dto/                  # 数据传输对象
│   │   │   │   ├── AdminLoginRequest.java
│   │   │   │   ├── AdminLoginResponse.java
│   │   │   │   ├── AdminPageResponse.java
│   │   │   │   ├── ApiResponse.java
│   │   │   │   ├── BookRequest.java
│   │   │   │   ├── BorrowRequest.java
│   │   │   │   ├── BorrowerInfoDTO.java
│   │   │   │   ├── LoginRequest.java
│   │   │   │   ├── LoginResponse.java
│   │   │   │   ├── PageResponse.java
│   │   │   │   ├── PopularBookDTO.java
│   │   │   │   └── UserRequest.java
│   │   │   ├── entity/               # 实体类
│   │   │   │   ├── Admin.java
│   │   │   │   ├── Book.java
│   │   │   │   ├── BorrowRecord.java
│   │   │   │   └── User.java
│   │   │   ├── exception/            # 异常处理
│   │   │   │   ├── AuthenticationException.java
│   │   │   │   ├── BusinessException.java
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   └── ResourceNotFoundException.java
│   │   │   ├── interceptor/          # 拦截器
│   │   │   │   └── JwtAuthenticationInterceptor.java
│   │   │   ├── repository/           # 数据访问层
│   │   │   │   ├── AdminRepository.java
│   │   │   │   ├── BookRepository.java
│   │   │   │   ├── BorrowRecordRepository.java
│   │   │   │   └── UserRepository.java
│   │   │   ├── service/              # 业务逻辑层
│   │   │   │   ├── AdminService.java
│   │   │   │   ├── BookService.java
│   │   │   │   ├── BorrowRecordService.java
│   │   │   │   └── UserService.java
│   │   │   ├── util/                 # 工具类
│   │   │   │   └── JwtUtil.java
│   │   │   └── LibraryManagementApplication.java  # 启动类
│   │   └── resources/
│   │       ├── application.yml           # 主配置文件
│   │       ├── application-dev.yml       # 开发环境配置
│   │       └── application-prod.yml      # 生产环境配置
│   └── test/
│       └── java/com/kyk/librarymanagement/
│           ├── service/
│           │   └── BookServiceTest.java  # 单元测试
│           └── LibraryManagementApplicationTests.java
├── build.gradle                      # Gradle构建文件
├── README.md                         # 项目说明文档
└── ADMIN_README.md                   # 管理员功能说明
```

## 🧪 运行测试

```bash
# 运行所有测试
./gradlew test

# 运行特定测试类
./gradlew test --tests BookServiceTest

# 生成测试报告
./gradlew test jacocoTestReport
```

## ⚙️ 配置说明

### 多环境配置

项目支持 `dev`（开发）和 `prod`（生产）两种环境：

| 配置项 | 开发环境 (dev) | 生产环境 (prod) |
|--------|---------------|----------------|
| 数据库 | H2 内存数据库 | MySQL |
| ddl-auto | create-drop | validate |
| SQL 显示 | ✅ 启用 | ❌ 禁用 |
| H2 Console | ✅ 启用 | ❌ 禁用 |
| 日志级别 | DEBUG | WARN/INFO |
| Swagger UI | ✅ 启用 | ❌ 禁用 |
| JWT 密钥 | 硬编码 | 环境变量 |

切换环境：
```yaml
# application.yml
spring:
  profiles:
    active: dev  # 或 prod
```

### 主要配置项

```yaml
# 服务器端口
server:
  port: 8080

# 数据库配置
spring:
  datasource:
    url: jdbc:h2:mem:librarydb
    username: sa
    password: 

# JPA 配置
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true

# JWT 配置
jwt:
  secret: your-secret-key
  expiration: 86400000  # 24小时
```

## 🔒 安全说明

### 密码加密

用户密码和管理员密码均使用 BCrypt 算法加密存储，确保即使数据库泄露也无法直接获取明文密码。

### JWT 认证

- **用户 Token 有效期**：开发环境 24 小时，生产环境 1 小时
- **管理员 Token 有效期**：7 天（604800秒）
- 建议在生产环境中通过环境变量设置 JWT 密钥
- Token 包含用户/管理员 ID、手机号和角色信息

### 管理员权限控制

- **超级管理员** (user_role = 1)：拥有全部权限，可进行增删改查操作
- **普通管理员** (user_role = 2)：只读权限，仅可查询数据

### 初始化管理员账号

项目提供了 `init_admin.sql` 脚本用于初始化管理员表和数据：

```bash
# 在 MySQL 中执行
mysql -u root -p your_database < init_admin.sql
```

**默认管理员账号：**
- 手机号：18392462574
- 密码：admin123
- 角色：超级管理员

### 生产环境建议

1. **使用强密码** - 数据库密码和 JWT 密钥应使用强随机字符串
2. **启用 HTTPS** - 配置 SSL 证书，启用 HTTPS 传输
3. **禁用 Swagger** - 生产环境应禁用 API 文档，避免信息泄露
4. **限制 CORS** - 配置跨域访问策略，只允许信任的域名
5. **定期更新依赖** - 保持 Spring Boot 和其他依赖的最新版本

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request！

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件

## 📞 联系方式

如有问题或建议，请通过以下方式联系：

- 提交 Issue
- 发送邮件至：dev@example.com

---

**Made with ❤️ using Spring Boot**
