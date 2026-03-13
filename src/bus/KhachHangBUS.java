package bus;

import dao.KhachHangDAO;
import dto.KhachHang;
import java.util.ArrayList;

/**
 *
 * @author DELL
 */
public class KhachHangBUS {

    private KhachHangDAO khachHangDAO = new KhachHangDAO();

    public ArrayList<KhachHang> getAll() {
        return khachHangDAO.getAll();
    }

    public ArrayList<KhachHang> getByTrangThai(String trangThai) {
        if (trangThai == null || trangThai.trim().isEmpty()) return new ArrayList<>();
        return khachHangDAO.getByTrangThai(trangThai);
    }

    public KhachHang getById(String maKhachHang) {
        if (maKhachHang == null || maKhachHang.trim().isEmpty()) return null;
        return khachHangDAO.getById(maKhachHang);
    }

    public ArrayList<KhachHang> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return getAll();
        return khachHangDAO.search(keyword.trim());
    }

    public boolean insert(KhachHang kh) {
        if (kh == null) return false;
        if (kh.getMaKhachHang() == null || kh.getMaKhachHang().trim().isEmpty()) return false;
        if (kh.getHo() == null || kh.getHo().trim().isEmpty()) return false;
        if (kh.getTen() == null || kh.getTen().trim().isEmpty()) return false;
        if (kh.getSoDienThoai() == null || kh.getSoDienThoai().trim().isEmpty()) return false;
        return khachHangDAO.insert(kh);
    }

    public boolean update(KhachHang kh) {
        if (kh == null || kh.getMaKhachHang() == null) return false;
        if (kh.getHo() == null || kh.getHo().trim().isEmpty()) return false;
        if (kh.getTen() == null || kh.getTen().trim().isEmpty()) return false;
        if (kh.getSoDienThoai() == null || kh.getSoDienThoai().trim().isEmpty()) return false;
        return khachHangDAO.update(kh);
    }

    public boolean updateTrangThai(String maKhachHang, String trangThai) {
        if (maKhachHang == null || maKhachHang.trim().isEmpty()) return false;
        if (trangThai == null || trangThai.trim().isEmpty()) return false;
        return khachHangDAO.updateTrangThai(maKhachHang, trangThai);
    }

    public boolean delete(String maKhachHang) {
        if (maKhachHang == null || maKhachHang.trim().isEmpty()) return false;
        return khachHangDAO.delete(maKhachHang);
    }
}