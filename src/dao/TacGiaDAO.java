/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import config.DatabaseConnection;
import dto.TacGia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author DELL
 */
public class TacGiaDAO {
    public ArrayList<TacGia> getAll() {
        ArrayList<TacGia> ds = new ArrayList<>();
        String sql = "SELECT * FROM TacGia ORDER BY maTacGia ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()){
            while(rs.next()) {
                TacGia tg = new TacGia(
                        rs.getString("maTacGia"),
                        rs.getString("tenTacGia"));
                ds.add(tg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }
    
    public TacGia getById(String maTacGia) {
        String sql = "SELECT * FROM TacGia WHERE maTacGia=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maTacGia);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return new TacGia(
                        rs.getString("maTacGia"),
                        rs.getString("tenTacGia"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean insert(TacGia tg) {
        String sql = "INSERT INTO TacGia(maTacGia,tenTacGia) VALUES(?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tg.getMaTacGia());
            ps.setString(2, tg.getTenTacGia());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean update(TacGia tg) {
        String sql = "UPDATE TacGia SET tenTacGia=? WHERE maTacGia=?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tg.getTenTacGia());
            ps.setString(2, tg.getMaTacGia());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean delete(String maTacGia) {
        String sql = "DELETE FROM TacGia WHERE maTacGia=?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maTacGia);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
