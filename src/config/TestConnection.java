/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

import java.sql.*;

/**
 *
 * @author DELL
 */
public class TestConnection {
    public static void main(String[] args) {
        try {
            Connection conn = DatabaseConnection.getConnection();

            if (conn != null) {
                System.out.println("Kết nối database thành công!");
            } else {
                System.out.println("Kết nối database thất bại!");
            }

            DatabaseConnection.closeConnection();
        } catch (SQLException e) {
            System.out.println("Lỗi kết nối database!");
            e.printStackTrace();
        }
    }
}