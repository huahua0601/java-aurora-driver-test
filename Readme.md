# Aurora 数据库连接池示例

这是一个使用 HikariCP 连接池和 AWS JDBC Driver 实现的 Aurora 数据库多线程写入示例程序。

## 环境要求

- Java 11 或更高版本
- Gradle
- AWS Aurora MySQL 数据库实例

## 环境变量配置

运行程序前需要设置以下环境变量：

```bash
export DB_HOST=your-aurora-endpoint    # Aurora 数据库终端节点
export DB_PORT=3306                    # 数据库端口
export DB_NAME=your_database           # 数据库名称
export DB_USERNAME=your_username       # 数据库用户名
export DB_PASSWORD=your_password       # 数据库密码
```

## 数据库表结构

在运行程序前，请先创建测试表：

```sql
CREATE DATABASE testdb
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE test_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    data_column VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

## 数据库配置说明

程序使用了以下连接池配置：

- 最大连接数：10
- 最小空闲连接：5
- 空闲超时：300000ms
- 连接超时：20000ms

AWS JDBC Driver 配置：
- 启用故障转移插件
- 故障转移超时：10000ms
- 读取器主机模式配置

## 运行程序

### 方式一：使用 Gradle 运行
1. 设置环境变量
2. 在项目根目录执行：
```bash
./gradlew run
```
### 方式二：构建并运行 JAR 包
1. 构建 JAR 包：
```bash
./gradlew clean build
 ```

2. 运行 JAR 包：
```bash
java -jar build/libs/java-aurora-driver-test-1.0-SNAPSHOT.jar
```

## 程序说明

- 程序会创建一个大小为 5 的线程池
- 默认执行 20 个数据写入任务
- 每个任务会向数据库写入一条测试数据
- 程序执行完成后会自动关闭连接池和线程池

## 代码结构

- `DatabaseConfig.java`: 数据库连接池配置（单例模式）
- `DataWriterTask.java`: 数据写入任务实现
- `Main.java`: 程序入口和多线程任务调度

## 注意事项

1. 确保 Aurora 数据库实例正常运行且可访问
2. 检查数据库用户权限是否正确
3. 根据实际需求调整连接池和线程池参数
4. 建议在生产环境中根据实际负载调整配置参数

