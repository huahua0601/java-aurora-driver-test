package com.example.db;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class DataWriterTask implements Runnable {
    private final String data;
    
    public DataWriterTask(String data) {
        this.data = data;
    }
    
    @Override
    public void run() {
        try (Connection conn = DatabaseConfig.getInstance().getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 "INSERT INTO test_data (data_column) VALUES (?)"
             )) {
            
            pstmt.setString(1, data);
            pstmt.executeUpdate();
            
            System.out.println("Thread " + Thread.currentThread().getName() + " inserted data: " + data);
        } catch (Exception e) {
            System.err.println("Error writing data: " + e.getMessage());
        }
    }
}