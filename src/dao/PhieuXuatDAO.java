/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import config.DatabaseConnection;
import dto.PhieuXuat;

import java.sql.*;
import java.util.ArrayList;

public class PhieuXuatDAO {

    public ArrayList<PhieuXuat> getAll() {
        ArrayList<PhieuXuat> ds = new ArrayList<>();
        String sql = "SELECT * FROM PhieuXuat ORDER BY ngayLap DESC";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                PhieuXuat px = new PhieuXuat(
                        rs.getString("maPhieuXuat"),
                        rs.getDate("ngayLap").toLocalDate(),
                        rs.getString("maNhanVien"),
                        rs.getString("maKhachHang"),
                        rs.getString("maKhuyenMai"),
                        rs.getDouble("tongTien"),
                        rs.getString("trangThai"));
                ds.add(px);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public ArrayList<PhieuXuat> getByTrangThai(String trangThai) {
        ArrayList<PhieuXuat> ds = new ArrayList<>();
        String sql = "SELECT * FROM PhieuXuat WHERE trangThai=? ORDER BY ngayLap DESC";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PhieuXuat px = new PhieuXuat(
                        rs.getString("maPhieuXuat"),
                        rs.getDate("ngayLap").toLocalDate(),
                        rs.getString("maNhanVien"),
                        rs.getString("maKhachHang"),
                        rs.getString("maKhuyenMai"),
                        rs.getDouble("tongTien"),
                        rs.getString("trangThai"));
                ds.add(px);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public ArrayList<PhieuXuat> getByKhachHang(String maKhachHang) {
        ArrayList<PhieuXuat> ds = new ArrayList<>();
        String sql = "SELECT * FROM PhieuXuat WHERE maKhachHang=? ORDER BY ngayLap DESC";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKhachHang);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PhieuXuat px = new PhieuXuat(
                        rs.getString("maPhieuXuat"),
                        rs.getDate("ngayLap").toLocalDate(),
                        rs.getString("maNhanVien"),
                        rs.getString("maKhachHang"),
                        rs.getString("maKhuyenMai"),
                        rs.getDouble("tongTien"),
                        rs.getString("trangThai"));
                ds.add(px);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public ArrayList<PhieuXuat> getByNhanVien(String maNhanVien) {
        ArrayList<PhieuXuat> ds = new ArrayList<>();
        String sql = "SELECT * FROM PhieuXuat WHERE maNhanVien=? ORDER BY ngayLap DESC";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNhanVien);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PhieuXuat px = new PhieuXuat(
                        rs.getString("maPhieuXuat"),
                        rs.getDate("ngayLap").toLocalDate(),
                        rs.getString("maNhanVien"),
                        rs.getString("maKhachHang"),
                        rs.getString("maKhuyenMai"),
                        rs.getDouble("tongTien"),
                        rs.getString("trangThai"));
                ds.add(px);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public PhieuXuat getById(String maPhieuXuat) {
        String sql = "SELECT * FROM PhieuXuat WHERE maPhieuXuat=?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPhieuXuat);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new PhieuXuat(
                        rs.getString("maPhieuXuat"),
                        rs.getDate("ngayLap").toLocalDate(),
                        rs.getString("maNhanVien"),
                        rs.getString("maKhachHang"),
                        rs.getString("maKhuyenMai"),
                        rs.getDouble("tongTien"),
                        rs.getString("trangThai"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insert(PhieuXuat px) {
        String sql = "INSERT INTO PhieuXuat(maPhieuXuat,ngayLap,maNhanVien,maKhachHang,maKhuyenMai,tongTien,trangThai) VALUES(?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, px.getMaPhieuXuat());
            ps.setDate(2, Date.valueOf(px.getNgayLap()));
            ps.setString(3, px.getMaNhanVien());
            ps.setString(4, px.getMaKhachHang());
            if (px.getMaKhuyenMai() != null)
                ps.setString(5, px.getMaKhuyenMai());
            else
                ps.setNull(5, Types.VARCHAR);
            ps.setDouble(6, px.getTongTien());
            ps.setString(7, px.getTrangThai());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(PhieuXuat px) {
        String sql = "UPDATE PhieuXuat SET ngayLap=?,maNhanVien=?,maKhachHang=?,maKhuyenMai=?,tongTien=?,trangThai=? WHERE maPhieuXuat=?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(px.getNgayLap()));
            ps.setString(2, px.getMaNhanVien());
            ps.setString(3, px.getMaKhachHang());
            if (px.getMaKhuyenMai() != null)
                ps.setString(4, px.getMaKhuyenMai());
            else
                ps.setNull(4, Types.VARCHAR);
            ps.setDouble(5, px.getTongTien());
            ps.setString(6, px.getTrangThai());
            ps.setString(7, px.getMaPhieuXuat());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateTrangThai(String maPhieuXuat, String trangThai) {
        String sql = "UPDATE PhieuXuat SET trangThai=? WHERE maPhieuXuat=?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            ps.setString(2, maPhieuXuat);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(String maPhieuXuat) {
        String sql = "DELETE FROM PhieuXuat WHERE maPhieuXuat=?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPhieuXuat);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String generateMaPhieuXuat() {
        String sql = "SELECT maPhieuXuat FROM PhieuXuat ORDER BY maPhieuXuat DESC LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int num = Integer.parseInt(rs.getString("maPhieuXuat").replaceAll("[^0-9]", ""));
                return String.format("PX%03d", num + 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "PX001";
    }

    // Thống kê vốn - doanh thu - lợi nhuận theo ngày (8 ngày gần nhất)
    public ArrayList<Object[]> getThongKeVonDoanhThu8NgayGanNhat() {
        ArrayList<Object[]> ds = new ArrayList<>();
        String sql = "SELECT d.ngay, " +
                "       IFNULL(v.von, 0) AS von, " +
                "       IFNULL(dt.doanh_thu, 0) AS doanh_thu, " +
                "       IFNULL(dt.doanh_thu, 0) - IFNULL(v.von, 0) AS loi_nhuan " +
                "FROM ( " +
                "    SELECT ngayNhap AS ngay FROM PhieuNhap " +
                "    UNION " +
                "    SELECT ngayLap  AS ngay FROM PhieuXuat " +
                ") d " +
                "LEFT JOIN ( " +
                "    SELECT ngayNhap AS ngay, SUM(tongTien) AS von " +
                "    FROM PhieuNhap " +
                "    WHERE trangThai = 'DA_NHAP' " +
                "    GROUP BY ngayNhap " +
                ") v ON v.ngay = d.ngay " +
                "LEFT JOIN ( " +
                "    SELECT ngayLap AS ngay, SUM(tongTien) AS doanh_thu " +
                "    FROM PhieuXuat " +
                "    WHERE trangThai = 'DA_XUAT' " +
                "    GROUP BY ngayLap " +
                ") dt ON dt.ngay = d.ngay " +
                "ORDER BY d.ngay DESC " +
                "LIMIT 8";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                java.time.LocalDate ngay = rs.getDate("ngay").toLocalDate();
                double von = rs.getDouble("von");
                double doanhThu = rs.getDouble("doanh_thu");
                double loiNhuan = rs.getDouble("loi_nhuan");
                ds.add(new Object[] { ngay, von, doanhThu, loiNhuan });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    // Doanh thu theo ngày trong tháng/năm chỉ định
    public ArrayList<Object[]> getDoanhThuTheoNgay(int thang, int nam) {
        ArrayList<Object[]> ds = new ArrayList<>();
        String sql = "SELECT ngayLap, SUM(tongTien) " +
                "FROM PhieuXuat " +
                "WHERE trangThai='DA_XUAT' " +
                "  AND MONTH(ngayLap)=? AND YEAR(ngayLap)=? " +
                "GROUP BY ngayLap ORDER BY ngayLap ASC";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, thang);
            ps.setInt(2, nam);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ds.add(new Object[] {
                        rs.getDate(1).toLocalDate(),
                        rs.getDouble(2)
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    // Top sách bán chạy
    public ArrayList<Object[]> getTopSachBanChay(int limit) {
        ArrayList<Object[]> ds = new ArrayList<>();
        String sql = "SELECT s.tenSach, SUM(ct.soLuong) AS tongSL, SUM(ct.thanhTien) AS tongTien " +
                "FROM ChiTietPhieuXuat ct " +
                "JOIN Sach s ON ct.maSach = s.maSach " +
                "JOIN PhieuXuat px ON ct.maPhieuXuat = px.maPhieuXuat " +
                "WHERE px.trangThai='DA_XUAT' " +
                "GROUP BY s.maSach, s.tenSach " +
                "ORDER BY tongSL DESC LIMIT ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ds.add(new Object[] {
                        rs.getString("tenSach"),
                        rs.getInt("tongSL"),
                        rs.getDouble("tongTien")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    // Tổng hợp theo tháng trong năm
    public ArrayList<Object[]> getTongHopTheoThang(int nam) {
        ArrayList<Object[]> ds = new ArrayList<>();
        String sql = "SELECT MONTH(ngayLap) AS thang, " +
                "       COUNT(*) AS soPhieu, " +
                "       SUM(tongTien) AS doanhThu " +
                "FROM PhieuXuat " +
                "WHERE trangThai='DA_XUAT' AND YEAR(ngayLap)=? " +
                "GROUP BY MONTH(ngayLap) ORDER BY thang ASC";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, nam);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ds.add(new Object[] {
                        rs.getInt("thang"),
                        rs.getInt("soPhieu"),
                        rs.getDouble("doanhThu")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }
}