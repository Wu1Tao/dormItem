 # 宿舍物品管理系统 (Dormitory Item Management)
 
 基于 Spring Boot 的宿舍物品管理平台，支持 Web 和控制台双 UI 模式，提供物品库存管理、学生管理、物品借出与归还、用户权限控制等功能。
 
 ## 技术栈
 
 | 分类 | 技术 |
 |---|---|
 | 语言 | Java 8 |
 | 框架 | Spring Boot 2.6.13, Spring MVC, Spring Data JDBC |
 | 数据库 | PostgreSQL |
 | 视图 | Thymeleaf, HTML/CSS |
 | 构建 | Maven 多模块, Launch4j (EXE 打包) |
 | 工具 | Lombok, SLF4J |
 
 ## 项目结构
 
 ```
 dormItem-multi/
 ├── common/               # 公共层：枚举、DTO
 ├── data-access/          # 数据访问层：领域模型、Repository 接口、数据库实现
 │   └── postgres/         #   └── PostgreSQL 具体实现（Mapper + 自定义 SQL）
 ├── business-logic/       # 业务逻辑层：Service 接口与实现
 ├── tech-ui/              # 控制台 UI 模块（终端菜单交互）
 ├── web-gui/              # Web UI 模块（Spring MVC + Thymeleaf 页面）
 ├── application/          # 应用入口 + 配置
 ├── build-exe.bat         # Windows 一键构建脚本
 └── pom.xml               # 父 POM
 ```
 
 ### 模块依赖关系
 
 ```
 application
  ├── tech-ui  ─── business-logic ─── data-access ─── common
  └── web-gui  ─── business-logic ─── data-access ─── common
 ```
 
 各模块职责独立，依赖单向，下层不反向依赖上层。
 
 ## 功能特性
 
 - **用户管理**：注册 / 登录 / 注销，会话管理，用户状态控制（启用/禁用/删除）
 - **角色权限**：普通用户 (USER) 与超级管理员 (ADMIN) 两级角色，页面级拦截
 - **物品管理**：多类别物品库存管理，支持查看、添加入库、扣减出库，库存安全校验
 - **借出管理**：学生借出物品登记，单人借出数量上限控制，借出状态跟踪
 - **归还管理**：借出物品归还登记，归还记录与借出记录联动
 - **学生管理**：学生信息维护（含专业、性别、年级、状态等）
 - **宿舍/房间管理**：宿舍楼与房间信息维护
 - **操作审计**：系统日志与用户操作日志分离记录
 - **双 UI 模式**：通过配置切换 Web 界面或控制台界面
 
 ## 快速开始
 
 ### 环境要求
 
 - JDK 1.8+
 - Maven 3.6+
 - PostgreSQL (推荐 12+)
 
 ### 数据库配置
 
 1. 创建 schema：
 
 ```sql
 CREATE SCHEMA IF NOT EXISTS dorm_items;
 ```
 
 2. 项目默认连接配置（在 `application.yml` 中）：
 
 ```
 主机: localhost
 端口: 5432
 数据库: postgres
 schema: dorm_items
 用户名: postgres
 密码: 123456
 ```
 
 > 如需修改数据库配置，编辑 `application/src/main/resources/application.yml` 中的 `spring.datasource` 部分。
 
 ### 构建与运行
 
 **方式一：Maven 打包运行**
 
 ```bash
 cd dormItem-multi
 mvn clean package -DskipTests
 java -jar application/target/application-1.0.0.jar
 ```
 
 浏览器打开 `http://localhost:8080` 即可访问 Web 界面。
 
 **方式二：Windows EXE 一键构建**
 
 双击 `build-exe.bat`，或命令行执行：
 
 ```bash
 .\build-exe.bat
 ```
 
 构建产物：
 - `application/target/application-1.0.0.jar` — fat JAR，可直接 `java -jar` 运行
 - `application/target/DormitoryManagement.exe` — Windows 可执行文件，双击启动
 
 ### 配置说明
 
 | 配置项 | 说明 | 默认值 |
 |---|---|---|
 | `server.port` | Web 服务端口 | `8080` |
 | `app.ui.mode` | UI 模式：`gui`（Web 界面）或 `tech`（控制台界面） | `gui` |
 | `app.business.max-issue-quantity` | 单人最大借出物品数量 | `10` |
 | `spring.datasource.*` | PostgreSQL 连接配置 | 见 `application.yml` |
 
 开发环境默认开启 DEBUG 级别的应用日志（由 `application-dev.yml` 激活）。
 
 ## 设计要点
 
 - **分层架构**：common → data-access → business-logic → ui，依赖单向，模块间通过接口解耦
 - **Repository 模式**：数据访问层定义通用接口，具体实现在 `postgres` 子包中，便于切换数据库
 - **条件装配**：通过 `@ConditionalOnProperty` 实现 UI 模式的动态切换，两种界面共享同一业务层
 - **枚举体系**：物品类别、物品状况、借出状态、学生状态、用户角色等均使用枚举，避免硬编码
 - **日志分层**：系统日志记录技术细节（`log.info`），用户操作日志独立记录（`USER_ACTIONS` Logger），便于审计追溯
 - **库存校验**：借出时校验库存充足性和单人数量上限，保证业务数据一致性
 - **构建即用**：集成 Launch4j Maven 插件，可一键生成 Windows EXE，无需手动配置启动脚本
 
 ## 许可证
 
 MIT
