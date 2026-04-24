# 图书馆管理系统 (Library Management System)

一个基于 Spring Boot 的个人图书管理 API 系统，提供图书的增删改查、借阅归还、用户管理等功能。

## 📋 项目简介

本项目是一个 RESTful API 服务，用于管理个人图书收藏和借阅记录。系统支持用户注册登录、图书管理、借阅记录追踪以及热门图书统计等功能。采用分层架构设计，代码结构清晰，易于扩展和维护。

### 核心功能

- ✅ **图书管理** - 图书的增删改查操作
- ✅ **借阅管理** - 图书借阅、归还及借阅记录查询
- ✅ **用户管理** - 用户注册、登录、CRUD 操作
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

3. 修改配置文件 `src/main/resources/application.yml`：
```yaml
spring:
  profiles:
    active: prod
```

4. 配置 `src/main/resources/application-prod.yml` 中的数据库连接信息：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/librarydb?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_secure_password
```

5. 设置环境变量（推荐）：
```bash
export JWT_SECRET="your-super-secret-key-here"
export DB_PASSWORD="your-database-password"
```

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
├── src/
│   ├── main/
│   │   ├── java/com/kyk/librarymanagement/
│   │   │   ├── config/          # 配置类
│   │   │   │   ├── OpenApiConfig.java
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── JwtConfig.java
│   │   │   ├── controller/      # 控制器层
│   │   │   │   ├── BookController.java
│   │   │   │   └── UserController.java
│   │   │   ├── dto/             # 数据传输对象
│   │   │   │   ├── ApiResponse.java
│   │   │   │   ├── BookRequest.java
│   │   │   │   ├── BorrowRequest.java
│   │   │   │   ├── LoginRequest.java
│   │   │   │   ├── LoginResponse.java
│   │   │   │   ├── PageResponse.java
│   │   │   │   ├── PopularBookDTO.java
│   │   │   │   └── UserRequest.java
│   │   │   ├── entity/          # 实体类
│   │   │   │   ├── Book.java
│   │   │   │   ├── BorrowRecord.java
│   │   │   │   └── User.java
│   │   │   ├── exception/       # 异常处理
│   │   │   │   ├── BusinessException.java
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   └── ResourceNotFoundException.java
│   │   │   ├── repository/      # 数据访问层
│   │   │   │   ├── BookRepository.java
│   │   │   │   ├── BorrowRecordRepository.java
│   │   │   │   └── UserRepository.java
│   │   │   ├── service/         # 业务逻辑层
│   │   │   │   ├── BookService.java
│   │   │   │   ├── BorrowRecordService.java
│   │   │   │   └── UserService.java
│   │   │   ├── util/            # 工具类
│   │   │   │   └── JwtUtil.java
│   │   │   └── LibraryManagementApplication.java
│   │   └── resources/
│   │       ├── application.yml           # 主配置文件
│   │       ├── application-dev.yml       # 开发环境配置
│   │       └── application-prod.yml      # 生产环境配置
│   └── test/
│       └── java/com/kyk/librarymanagement/
│           └── service/
│               └── BookServiceTest.java  # 单元测试
├── build.gradle
└── README.md
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

用户密码使用 BCrypt 算法加密存储，确保即使数据库泄露也无法直接获取明文密码。

### JWT 认证

- Token 有效期：开发环境 24 小时，生产环境 1 小时
- 建议在生产环境中通过环境变量设置 JWT 密钥
- Token 包含用户 ID 和手机号信息

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
