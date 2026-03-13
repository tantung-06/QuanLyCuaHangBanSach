/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import config.DatabaseConnection;
import java.sql.*;
import dto.NhomQuyen;
import java.util.ArrayList;

/**
 *
 * @author DELL
 */
public class NhomQuyenDAO {
    
    public ArrayList<NhomQuyen> getAll(){
        ArrayList<NhomQuyen> ds = new ArrayList<>();
        String sql = "SELECT * FROM NhomQuyen ORDER BY maNhomQuyen ASC";
        try(Connection  conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){
            while(rs.next()){
                NhomQuyen nq = new NhomQuyen(
                        rs.getString("maNhomQuyen"),
                        rs.getString("tenNhomQuyen"));
                ds.add(nq);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return ds;
    }
   
    public NhomQuyen getByID(String maNhomQuyen){
        String sql = "SELECT * FROM NhomQuyen WHERE maNhomQuyen=?";
        try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, maNhomQuyen);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return new NhomQuyen(
                        rs.getString("maNhomQuyen"),
                        rs.getString("tenNhomQuyen"));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean insert(NhomQuyen nq) {
        String sql = "INSERT INTO NhomQuyen(maNhomQuyen,tenNhomQuyen) VALUES(?,?)";
        try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, nq.getMaNhomQuyen());
            ps.setString(2, nq.getTenNhomQuyen());
            return ps.executeUpdate() > 0;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean update(NhomQuyen nq) {
        String sql = "UPDATE NhomQuyen SET tenNhomQuyen=? WHERE maNhomQuyen=?";
        try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, nq.getTenNhomQuyen());
            ps.setString(2, nq.getMaNhomQuyen());
            return ps.executeUpdate() > 0;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean  delete(String maNhomQuyen) {
        String sql = "DELETE FROM NhomQuyen WHERE maNhomQuyen=?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, maNhomQuyen);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}