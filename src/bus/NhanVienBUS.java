package bus;

import dao.NhanVienDAO;
import dto.NhanVien;
import java.util.ArrayList;

/**
 *
 * @author DELL
 */
public class NhanVienBUS {

    private NhanVienDAO nhanVienDAO = new NhanVienDAO();

    public ArrayList<NhanVien> getAll() {
        return nhanVienDAO.getAll();
    }

    public ArrayList<NhanVien> getByTrangThai(String trangThai) {
        if (trangThai == null || trangThai.trim().isEmpty()) return new ArrayList<>();
        return nhanVienDAO.getByTrangThai(trangThai);
    }

    public NhanVien getById(String maNhanVien) {
        if (maNhanVien == null || maNhanVien.trim().isEmpty()) return null;
        return nhanVienDAO.getById(maNhanVien);
    }

    public NhanVien getByTaiKhoan(String maTaiKhoan) {
        if (maTaiKhoan == null || maTaiKhoan.trim().isEmpty()) return null;
        return nhanVienDAO.getByTaiKhoan(maTaiKhoan);
    }

    public ArrayList<NhanVien> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return getAll();
        return nhanVienDAO.search(keyword.trim());
    }

    public boolean insert(NhanVien nv) {
        if (nv == null) return false;
        if (nv.getMaNhanVien() == null || nv.getMaNhanVien().trim().isEmpty()) return false;
        if (nv.getHo() == null || nv.getHo().trim().isEmpty()) return false;
        if (nv.getTen() == null || nv.getTen().trim().isEmpty()) return false;
        if (nv.getSoDienThoai() == null || nv.getSoDienThoai().trim().isEmpty()) return false;
        return nhanVienDAO.insert(nv);
    }

    public boolean update(NhanVien nv) {
        if (nv == null || nv.getMaNhanVien() == null) return false;
        if (nv.getHo() == null || nv.getHo().trim().isEmpty()) return false;
        if (nv.getTen() == null || nv.getTen().trim().isEmpty()) return false;
        if (nv.getSoDienThoai() == null || nv.getSoDienThoai().trim().isEmpty()) return false;
        return nhanVienDAO.update(nv);
    }

    public boolean updateTrangThai(String maNhanVien, String trangThai) {
        if (maNhanVien == null || maNhanVien.trim().isEmpty()) return false;
        if (trangThai == null || trangThai.trim().isEmpty()) return false;
        return nhanVienDAO.updateTrangThai(maNhanVien, trangThai);
    }

    public boolean delete(String maNhanVien) {
        if (maNhanVien == null || maNhanVien.trim().isEmpty()) return false;
        return nhanVienDAO.delete(maNhanVien);
    }
}