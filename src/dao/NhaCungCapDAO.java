/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.*;
import config.DatabaseConnection;
import dto.NhaCungCap;
import java.util.ArrayList;

/**
 *
 * @author DELL
 */
public class NhaCungCapDAO {
    public ArrayList<NhaCungCap> getAll(){
        ArrayList<NhaCungCap> ds = new ArrayList<>();
        String sql = "SELECT * FROM NhaCungCap ORDER BY maNCC ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()){
                NhaCungCap ncc = new NhaCungCap(
                        rs.getString("maNCC"),
                        rs.getString("tenNCC"),
                        rs.getString("soDienThoai"),
                        rs.getString("diaChi"),
                        rs.getString("email"),
                        rs.getString("trangThai"));
                ds.add(ncc);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return ds;
    }

    public ArrayList<NhaCungCap> getHoatDong() {
        ArrayList<NhaCungCap> ds = new ArrayList<>();
        String sql = "SELECT * FROM NhaCungCap WHERE trangThai='HOAT_DONG'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()){
                NhaCungCap ncc = new NhaCungCap(
                        rs.getString("maNCC"),
                        rs.getString("tenNCC"),
                        rs.getString("soDienThoai"),
                        rs.getString("diaChi"),
                        rs.getString("email"),
                        rs.getString("trangThai"));
                ds.add(ncc);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return ds;
    }

    public NhaCungCap getById(String maNCC) {
        String sql = "SELECT * FROM NhaCungCap WHERE maNCC=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNCC);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new NhaCungCap(
                        rs.getString("maNCC"),
                        rs.getString("tenNCC"),
                        rs.getString("soDienThoai"),
                        rs.getString("diaChi"),
                        rs.getString("email"),
                        rs.getString("trangThai"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insert(NhaCungCap ncc) {
        String sql = "INSERT INTO NhaCungCap(maNCC,tenNCC,soDienThoai,diaChi,email,trangThai) VALUES(?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ncc.getMaNCC());
            ps.setString(2, ncc.getTenNCC());
            ps.setString(3, ncc.getSoDienThoai());
            ps.setString(4, ncc.getDiaChi());
            ps.setString(5, ncc.getEmail());
            ps.setString(6, ncc.getTrangThai());
            return ps.executeUpdate() > 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(NhaCungCap ncc){
        String sql = "UPDATE NhaCungCap SET tenNCC=?,soDienThoai=?,diaChi=?,email=?,trangThai=? WHERE maNCC=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ncc.getTenNCC());
            ps.setString(2, ncc.getSoDienThoai());
            ps.setString(3, ncc.getDiaChi());
            ps.setString(4, ncc.getEmail());
            ps.setString(5, ncc.getTrangThai());
            ps.setString(6, ncc.getMaNCC());
            return ps.executeUpdate() > 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateTrangThai(String maNCC, String trangThai){
        String sql = "UPDATE NhaCungCap SET trangThai=? WHERE maNCC=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            ps.setString(2, maNCC);
            return ps.executeUpdate() > 0;
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(String maNCC){
        String sql = "DELETE FROM NhaCungCap WHERE maNCC=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNCC);
            return ps.executeUpdate() > 0;
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}