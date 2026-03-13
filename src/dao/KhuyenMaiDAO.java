/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import config.DatabaseConnection;
import java.sql.*;
import dto.KhuyenMai;
import java.util.ArrayList;

/**
 *
 * @author DELL
 */
public class KhuyenMaiDAO {
    public ArrayList<KhuyenMai> getAll(){
        ArrayList<KhuyenMai> ds = new ArrayList<>();
        String sql = "SELECT * FROM KhuyenMai ORDER BY maKhuyenMai ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()){
                KhuyenMai km = new KhuyenMai(
                        rs.getString("maKhuyenMai"),
                        rs.getString("tenKhuyenMai"),
                        rs.getDouble("donHangToiThieu"),
                        rs.getDouble("phanTramGiam"),
                        rs.getDate("ngayBatDau").toLocalDate(),
                        rs.getDate("ngayKetThuc").toLocalDate(),
                        rs.getString("trangThai"));
                ds.add(km);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public ArrayList<KhuyenMai> getApDung(){
        ArrayList<KhuyenMai> ds = new ArrayList<>();
        String sql = "SELECT * FROM KhuyenMai WHERE trangThai='AP_DUNG'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()){
                KhuyenMai km = new KhuyenMai(
                        rs.getString("maKhuyenMai"),
                        rs.getString("tenKhuyenMai"),
                        rs.getDouble("donHangToiThieu"),
                        rs.getDouble("phanTramGiam"),
                        rs.getDate("ngayBatDau").toLocalDate(),
                        rs.getDate("ngayKetThuc").toLocalDate(),
                        rs.getString("trangThai"));
                ds.add(km);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }
    
    public ArrayList<KhuyenMai> getByTrangThai(String trangThai){
        ArrayList<KhuyenMai> ds = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien WHERE trangThai=?";
        try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, trangThai);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                KhuyenMai km = new KhuyenMai(
                        rs.getString("maKhuyenMai"),
                        rs.getString("tenKhuyenMai"),
                        rs.getDouble("donHangToiThieu"),
                        rs.getDouble("phanTramGiam"),
                        rs.getDate("ngayBatDau").toLocalDate(),
                        rs.getDate("ngayKetThuc").toLocalDate(),
                        rs.getString("trangThai"));
                ds.add(km);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return ds;
    }

    public KhuyenMai getById(String maKhuyenMai) {
        String sql = "SELECT * FROM KhuyenMai WHERE maKhuyenMai=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKhuyenMai);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new KhuyenMai(
                        rs.getString("maKhuyenMai"),
                        rs.getString("tenKhuyenMai"),
                        rs.getDouble("donHangToiThieu"),
                        rs.getDouble("phanTramGiam"),
                        rs.getDate("ngayBatDau").toLocalDate(),
                        rs.getDate("ngayKetThuc").toLocalDate(),
                        rs.getString("trangThai"));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insert(KhuyenMai km) {
        String sql = "INSERT INTO KhuyenMai(maKhuyenMai,tenKhuyenMai,donHangToiThieu,phanTramGiam,ngayBatDau,ngayKetThuc,trangThai) VALUES(?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, km.getMaKhuyenMai());
            ps.setString(2, km.getTenKhuyenMai());
            ps.setDouble(3, km.getDonHangToiThieu());
            ps.setDouble(4, km.getPhanTramGiam());
            ps.setDate(5, Date.valueOf(km.getNgayBatDau()));
            ps.setDate(6, Date.valueOf(km.getNgayKetThuc()));
            ps.setString(7, km.getTrangThai());
            return ps.executeUpdate() > 0;
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(KhuyenMai km) {
        String sql = "UPDATE KhuyenMai SET tenKhuyenMai=?,donHangToiThieu=?,phanTramGiam=?,ngayBatDau=?,ngayKetThuc=?,trangThai=? WHERE maKhuyenMai=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, km.getTenKhuyenMai());
            ps.setDouble(2, km.getDonHangToiThieu());
            ps.setDouble(3, km.getPhanTramGiam());
            ps.setDate(4, Date.valueOf(km.getNgayBatDau()));
            ps.setDate(5, Date.valueOf(km.getNgayKetThuc()));
            ps.setString(6, km.getTrangThai());
            ps.setString(7, km.getMaKhuyenMai());
            return ps.executeUpdate() > 0;
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateTrangThai(String maKhuyenMai, String trangThai) {
        String sql = "UPDATE KhuyenMai SET trangThai=? WHERE maKhuyenMai=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            ps.setString(2, maKhuyenMai);
            return ps.executeUpdate() > 0;
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(String maKhuyenMai) {
        String sql = "DELETE FROM KhuyenMai WHERE maKhuyenMai=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKhuyenMai);
            return ps.executeUpdate() > 0;
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}