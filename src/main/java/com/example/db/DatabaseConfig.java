package com.example.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseConfig {
    private static volatile DatabaseConfig instance;
    private HikariDataSource dataSource;
    
    private DatabaseConfig() {
        initDataSource();
    }
    
    public static DatabaseConfig getInstance() {
        if (instance == null) {
            synchronized (DatabaseConfig.class) {
                if (instance == null) {
                    instance = new DatabaseConfig();
                }
            }
        }
        return instance;
    }
    
    private void initDataSource() {
        HikariConfig config = new HikariConfig();
        // 使用 AWS JDBC wrapper URL
        String dbHost = System.getenv("DB_HOST");
        String dbPort = System.getenv("DB_PORT");
        String dbName = System.getenv("DB_NAME");
        String dbUrl = String.format("jdbc:aws-wrapper:mysql://%s:%s/%s", 
            dbHost != null ? dbHost : "localhost",
            dbPort != null ? dbPort : "3306",
            dbName != null ? dbName : "test");
        System.out.println("Current DB URL:" + dbUrl);    
        config.setJdbcUrl(dbUrl);
        config.setUsername(System.getenv("DB_USERNAME"));
        config.setPassword(System.getenv("DB_PASSWORD"));
        config.setDriverClassName("software.amazon.jdbc.Driver");

        //  AWS JDBC wrapper logger configuration
        config.addDataSourceProperty("loggerLevel", "TRACE");
        config.addDataSourceProperty("defaultLoggerLevel", "TRACE");
        config.addDataSourceProperty("enableVerboseLogging", "true");
        
        // AWS JDBC wrapper 配置, 如果连接其他的非Aurora Mysql数据库,请注释掉以下所有参数配置.
        config.addDataSourceProperty("wrapperPlugins", "initialConnection,auroraConnectionTracker,failover2,efm2");
        config.addDataSourceProperty("failoverTimeoutMs", "60000");
        config.addDataSourceProperty("verifyOpenedConnectionType", "writer");
        config.addDataSourceProperty("clusterInstanceHostPattern", "?.c8gkhofhtia4.us-east-1.rds.amazonaws.com");
        config.addDataSourceProperty("wrapperDialect", "aurora-mysql");

        // HikariCP 连接池配置
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        config.setIdleTimeout(300000);
        config.setConnectionTimeout(20000);
        
        
        dataSource = new HikariDataSource(config);
    }
    
    public HikariDataSource getDataSource() {
        return dataSource;
    }
}