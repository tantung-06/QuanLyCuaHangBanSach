package bus;

import dao.SachDAO;
import dto.Sach;
import java.util.ArrayList;

/**
 *
 * @author DELL
 */
public class SachBUS {

    private SachDAO sachDAO = new SachDAO();

    public ArrayList<Sach> getAll() {
        return sachDAO.getAll();
    }

    public Sach getById(String maSach) {
        if (maSach == null || maSach.trim().isEmpty()) return null;
        return sachDAO.getById(maSach);
    }

    public ArrayList<Sach> getByTrangThai(String trangThai) {
        if (trangThai == null || trangThai.trim().isEmpty()) return new ArrayList<>();
        return sachDAO.getByTrangThai(trangThai);
    }

    public ArrayList<Sach> getByTheLoai(String maTheLoai) {
        if (maTheLoai == null || maTheLoai.trim().isEmpty()) return new ArrayList<>();
        return sachDAO.getByTheLoai(maTheLoai);
    }

    public ArrayList<Sach> getByTacGia(String maTacGia) {
        if (maTacGia == null || maTacGia.trim().isEmpty()) return new ArrayList<>();
        return sachDAO.getByTacGia(maTacGia);
    }

    public ArrayList<Sach> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return getAll();
        return sachDAO.search(keyword.trim());
    }

    public boolean insert(Sach s) {
        if (s == null) return false;
        if (s.getMaSach() == null || s.getMaSach().trim().isEmpty()) return false;
        if (s.getTenSach() == null || s.getTenSach().trim().isEmpty()) return false;
        if (s.getGiaBan() < 0) return false;
        if (s.getSoLuongTon() < 0) return false;
        return sachDAO.insert(s);
    }

    public boolean update(Sach s) {
        if (s == null || s.getMaSach() == null) return false;
        if (s.getTenSach() == null || s.getTenSach().trim().isEmpty()) return false;
        if (s.getGiaBan() < 0) return false;
        if (s.getSoLuongTon() < 0) return false;
        return sachDAO.update(s);
    }

    public boolean updateSoLuongTon(String maSach, int soLuong) {
        if (maSach == null || maSach.trim().isEmpty()) return false;
        return sachDAO.updateSoLuongTon(maSach, soLuong);
    }

    public boolean updateTrangThai(String maSach, String trangThai) {
        if (maSach == null || trangThai == null) return false;
        return sachDAO.updateTrangThai(maSach, trangThai);
    }

    public boolean delete(String maSach) {
        if (maSach == null || maSach.trim().isEmpty()) return false;
        return sachDAO.delete(maSach);
    }
    
    public String generateMaSach() {
        return sachDAO.generateMaSach();
    }
}