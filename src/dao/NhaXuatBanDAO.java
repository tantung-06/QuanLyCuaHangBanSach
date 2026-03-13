/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import config.DatabaseConnection;
import dto.NhaXuatBan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author DELL
 */
public class NhaXuatBanDAO {
    public ArrayList<NhaXuatBan> getAll() {
        ArrayList<NhaXuatBan> ds = new ArrayList<>();
        String sql = "SELECT * FROM NhaXuatBan ORDER BY maNXB ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()){
            while(rs.next()) {
                NhaXuatBan nxb = new NhaXuatBan(
                        rs.getString("maNXB"),
                        rs.getString("tenNXB"),
                        rs.getString("soDienThoai"),
                        rs.getString("diaChi"),
                        rs.getString("email")
                );
                ds.add(nxb);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }
    
    public NhaXuatBan getById(String maNXB){
        String sql = "SELECT * FROM NhaXuatBan WHERE maNXB=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNXB);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return new NhaXuatBan(
                        rs.getString("maNXB"),
                        rs.getString("tenNXB"),
                        rs.getString("soDienThoai"),
                        rs.getString("diaChi"),
                        rs.getString("email")
                );
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean insert(NhaXuatBan nxb) {
        String sql = "INSERT INTO NhaXuatBan(maNXB,tenNXB,soDienThoai,diaChi,email) VALUES(?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nxb.getMaNXB());
            ps.setString(2, nxb.getTenNXB());
            ps.setString(3, nxb.getSoDienThoai());
            ps.setString(4, nxb.getDiaChi());
            ps.setString(5, nxb.getEmail());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean update(NhaXuatBan nxb) {
        String sql = "UPDATE NhaXuatBan SET tenNXB=?,soDienThoai=?,diaChi=?,email=? WHERE maNXB=?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nxb.getTenNXB());
            ps.setString(2, nxb.getSoDienThoai());
            ps.setString(3, nxb.getDiaChi());
            ps.setString(4, nxb.getEmail());
            ps.setString(5, nxb.getMaNXB());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean delete(String maNXB) {
        String sql = "DELETE FROM NhaXuatBan WHERE maNXB=?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNXB);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}