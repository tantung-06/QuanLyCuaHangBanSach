/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import config.DatabaseConnection;
import dto.ChiTietPhieuNhap;

import java.sql.*;
import java.util.ArrayList;

public class ChiTietPhieuNhapDAO {

    public ArrayList<ChiTietPhieuNhap> getByPhieuNhap(String maPhieuNhap) {
        ArrayList<ChiTietPhieuNhap> ds = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietPhieuNhap WHERE maPhieuNhap=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPhieuNhap);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                ChiTietPhieuNhap ctpn = new ChiTietPhieuNhap(
                        rs.getString("maPhieuNhap"),
                        rs.getString("maSach"),
                        rs.getInt("soLuong"),
                        rs.getDouble("giaNhap"),
                        rs.getDouble("thanhTien"));
                ds.add(ctpn);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return ds;
    }

    public ChiTietPhieuNhap getById(String maPhieuNhap, String maSach) {
        String sql = "SELECT * FROM ChiTietPhieuNhap WHERE maPhieuNhap=? AND maSach=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPhieuNhap);
            ps.setString(2, maSach);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return new ChiTietPhieuNhap(
                        rs.getString("maPhieuNhap"),
                        rs.getString("maSach"),
                        rs.getInt("soLuong"),
                        rs.getDouble("giaNhap"),
                        rs.getDouble("thanhTien"));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean insert(ChiTietPhieuNhap ctpn) {
        String sql = "INSERT INTO ChiTietPhieuNhap(maPhieuNhap,maSach,soLuong,giaNhap,thanhTien) VALUES(?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ctpn.getMaPhieuNhap());
            ps.setString(2, ctpn.getMaSach());
            ps.setInt(3, ctpn.getSoLuong());
            ps.setDouble(4, ctpn.getGiaNhap());
            ps.setDouble(5, ctpn.getThanhTien());
            return ps.executeUpdate() > 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean insertBatch(ArrayList<ChiTietPhieuNhap> ds) {
        String sql = "INSERT INTO ChiTietPhieuNhap(maPhieuNhap,maSach,soLuong,giaNhap,thanhTien) VALUES(?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            for (ChiTietPhieuNhap obj : ds) {
                ps.setString(1, obj.getMaPhieuNhap());
                ps.setString(2, obj.getMaSach());
                ps.setInt(3, obj.getSoLuong());
                ps.setDouble(4, obj.getGiaNhap());
                ps.setDouble(5, obj.getThanhTien());
                ps.addBatch();
            }
            ps.executeBatch();
            conn.commit();
            conn.setAutoCommit(true);
            return true;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(ChiTietPhieuNhap ctpn) {
        String sql = "UPDATE ChiTietPhieuNhap SET soLuong=?,giaNhap=?,thanhTien=? WHERE maPhieuNhap=? AND maSach=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ctpn.getSoLuong());
            ps.setDouble(2, ctpn.getGiaNhap());
            ps.setDouble(3, ctpn.getThanhTien());
            ps.setString(4, ctpn.getMaPhieuNhap());
            ps.setString(5, ctpn.getMaSach());
            return ps.executeUpdate() > 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(String maPhieuNhap, String maSach) {
        String sql = "DELETE FROM ChiTietPhieuNhap WHERE maPhieuNhap=? AND maSach=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPhieuNhap);
            ps.setString(2, maSach);
            return ps.executeUpdate() > 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteByPhieuNhap(String maPhieuNhap) {
        String sql = "DELETE FROM ChiTietPhieuNhap WHERE maPhieuNhap=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPhieuNhap);
            return ps.executeUpdate() > 0;
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}