/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.*;
import config.DatabaseConnection;
import dto.TheLoai;
import java.util.ArrayList;

/**
 *
 * @author DELL
 */
public class TheLoaiDao {
    public ArrayList<TheLoai> getAll() {
        ArrayList<TheLoai> ds = new ArrayList<>();
        String sql = "SELECT * FROM TheLoai ORDER BY maTheLoai ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()){
            while(rs.next()) {
                TheLoai tl = new TheLoai(
                        rs.getString("maTheLoai"),
                        rs.getString("tenTheLoai"));
                ds.add(tl);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }
    
    public boolean insert(TheLoai tl) {
        String sql = "INSERT INTO TheLoai(maTheLoai,tenTheLoai) VALUES(?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tl.getMaTheLoai());
            ps.setString(2, tl.getTenTheLoai());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean update(TheLoai tl) {
        String sql = "UPDATE TheLoai SET tenTheLoai=? WHERE maTheLoai=?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tl.getTenTheLoai());
            ps.setString(2, tl.getMaTheLoai());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean delete(String maTheLoai) {
        String sql = "DELETE FROM TheLoai WHERE maTheLoai=?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maTheLoai);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}