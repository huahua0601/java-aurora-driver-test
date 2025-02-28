package com.example;

import com.example.db.DataWriterTask;
import com.example.db.DatabaseConfig;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        
        // 创建并提交20个写入任务
        for (int i = 0; i < 20000; i++) {
            executor.submit(new DataWriterTask("Data-" + i));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        // 关闭线程池
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
        
        // 关闭数据源
        DatabaseConfig.getInstance().getDataSource().close();
    }
}