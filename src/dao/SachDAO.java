/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import config.DatabaseConnection;
import dto.Sach;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author DELL
 */
public class SachDAO {

    public ArrayList<Sach> getAll(){
        ArrayList<Sach> ds = new ArrayList<>();
        String sql = "SELECT * FROM Sach ORDER BY maSach ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()){
                Sach s = new Sach(
                        rs.getString("maSach"),
                        rs.getString("tenSach"),
                        rs.getInt("namXuatBan"),
                        rs.getDouble("giaBan"),
                        rs.getInt("soLuongTon"),
                        rs.getString("maTacGia"),
                        rs.getString("maTheLoai"),
                        rs.getString("maNXB"),
                        rs.getString("trangThai"));
                ds.add(s);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return ds;
    }

    public ArrayList<Sach> getByTrangThai(String trangThai){
        ArrayList<Sach> ds = new ArrayList<>();
        String sql = "SELECT * FROM Sach WHERE trangThai=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Sach s = new Sach(
                        rs.getString("maSach"),
                        rs.getString("tenSach"),
                        rs.getInt("namXuatBan"),
                        rs.getDouble("giaBan"),
                        rs.getInt("soLuongTon"),
                        rs.getString("maTacGia"),
                        rs.getString("maTheLoai"),
                        rs.getString("maNXB"),
                        rs.getString("trangThai"));
                ds.add(s);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return ds;
    }

    public ArrayList<Sach> getByTheLoai(String maTheLoai){
        ArrayList<Sach> ds = new ArrayList<>();
        String sql = "SELECT * FROM Sach WHERE maTheLoai=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maTheLoai);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Sach s = new Sach(
                        rs.getString("maSach"),
                        rs.getString("tenSach"),
                        rs.getInt("namXuatBan"),
                        rs.getDouble("giaBan"),
                        rs.getInt("soLuongTon"),
                        rs.getString("maTacGia"),
                        rs.getString("maTheLoai"),
                        rs.getString("maNXB"),
                        rs.getString("trangThai"));
                ds.add(s);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return ds;
    }

    public ArrayList<Sach> getByTacGia(String maTacGia){
        ArrayList<Sach> ds = new ArrayList<>();
        String sql = "SELECT * FROM Sach WHERE maTacGia=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maTacGia);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Sach s = new Sach(
                        rs.getString("maSach"),
                        rs.getString("tenSach"),
                        rs.getInt("namXuatBan"),
                        rs.getDouble("giaBan"),
                        rs.getInt("soLuongTon"),
                        rs.getString("maTacGia"),
                        rs.getString("maTheLoai"),
                        rs.getString("maNXB"),
                        rs.getString("trangThai"));
                ds.add(s);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return ds;
    }

    public Sach getById(String maSach)  {
        String sql = "SELECT * FROM Sach WHERE maSach=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSach);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return new Sach(
                        rs.getString("maSach"),
                        rs.getString("tenSach"),
                        rs.getInt("namXuatBan"),
                        rs.getDouble("giaBan"),
                        rs.getInt("soLuongTon"),
                        rs.getString("maTacGia"),
                        rs.getString("maTheLoai"),
                        rs.getString("maNXB"),
                        rs.getString("trangThai"));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Sach> search(String keyword){
        ArrayList<Sach> ds = new ArrayList<>();
        String sql = "SELECT * FROM Sach WHERE tenSach LIKE ?";
        String kw  = "%" + keyword + "%";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kw);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Sach s = new Sach(
                        rs.getString("maSach"),
                        rs.getString("tenSach"),
                        rs.getInt("namXuatBan"),
                        rs.getDouble("giaBan"),
                        rs.getInt("soLuongTon"),
                        rs.getString("maTacGia"),
                        rs.getString("maTheLoai"),
                        rs.getString("maNXB"),
                        rs.getString("trangThai"));
                ds.add(s);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return ds;
    }

    public boolean insert(Sach s) {
        String sql = "INSERT INTO Sach(maSach,tenSach,namXuatBan,giaBan,soLuongTon,maTacGia,maTheLoai,maNXB,trangThai) VALUES(?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getMaSach());
            ps.setString(2, s.getTenSach());
            ps.setInt(3, s.getNamXuatBan());
            ps.setDouble(4, s.getGiaBan());
            ps.setInt(5, s.getSoLuongTon());
            ps.setString(6, s.getMaTacGia());
            ps.setString(7, s.getMaTheLoai());
            ps.setString(8, s.getMaNXB());
            ps.setString(9, s.getTrangThai());
            return ps.executeUpdate() > 0;
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Sach s) {
        String sql = "UPDATE Sach SET tenSach=?,namXuatBan=?,giaBan=?,soLuongTon=?,maTacGia=?,maTheLoai=?,maNXB=?,trangThai=? WHERE maSach=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, s.getTenSach());
            ps.setInt(2, s.getNamXuatBan());
            ps.setDouble(3, s.getGiaBan());
            ps.setInt(4, s.getSoLuongTon());
            ps.setString(5, s.getMaTacGia());
            ps.setString(6, s.getMaTheLoai());
            ps.setString(7, s.getMaNXB());
            ps.setString(8, s.getTrangThai());
            ps.setString(9, s.getMaSach());
            return ps.executeUpdate() > 0;
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateSoLuongTon(String maSach, int soLuongTon){
        String sql = "UPDATE Sach SET soLuongTon=soLuongTon+? WHERE maSach=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, soLuongTon);
            ps.setString(2, maSach);
            return ps.executeUpdate() > 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateTrangThai(String maSach, String trangThai) {
        String sql = "UPDATE Sach SET trangThai=? WHERE maSach=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            ps.setString(2, maSach);
            return ps.executeUpdate() > 0;
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(String maSach) {
        String sql = "DELETE FROM Sach WHERE maSach=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSach);
            return ps.executeUpdate() > 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}