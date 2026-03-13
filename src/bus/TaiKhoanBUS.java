package bus;

import dao.TaiKhoanDAO;
import dto.TaiKhoan;
import java.util.ArrayList;

/**
 *
 * @author DELL
 */
public class TaiKhoanBUS {

    private TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();

    public ArrayList<TaiKhoan> getAll() {
        return taiKhoanDAO.getAll();
    }
    
    public TaiKhoan getByID(String maTaiKhoan){
        return taiKhoanDAO.getById(maTaiKhoan);
    }
    
    public ArrayList<TaiKhoan> getByTrangThai(String trangThai) {
        if (trangThai == null || trangThai.trim().isEmpty()) return new ArrayList<>();
        return taiKhoanDAO.getByTrangThai(trangThai);
    }

    // Đăng nhập - trả về TaiKhoan nếu hợp lệ, null nếu sai
    public TaiKhoan login(String tenDangNhap, String matKhau) {
        if (tenDangNhap == null || tenDangNhap.trim().isEmpty()) return null;
        if (matKhau == null || matKhau.trim().isEmpty()) return null;
        return taiKhoanDAO.login(tenDangNhap.trim(), matKhau);
    }

    public boolean insert(TaiKhoan tk) {
        if (tk == null) return false;
        if (tk.getTenDangNhap() == null || tk.getTenDangNhap().trim().isEmpty()) return false;
        if (tk.getMatKhau() == null || tk.getMatKhau().trim().isEmpty()) return false;
        return taiKhoanDAO.insert(tk);
    }

    public boolean update(TaiKhoan tk) {
        if (tk == null || tk.getMaTaiKhoan() == null) return false;
        return taiKhoanDAO.update(tk);
    }

    // Cập nhật trạng thái (khoá / mở khoá)
    public boolean updateTrangThai(String maTaiKhoan, String trangThai) {
        if (maTaiKhoan == null || trangThai == null) return false;
        return taiKhoanDAO.updateTrangThai(maTaiKhoan, trangThai);
    }

    public boolean delete(String maTaiKhoan) {
        if (maTaiKhoan == null || maTaiKhoan.trim().isEmpty()) return false;
        return taiKhoanDAO.delete(maTaiKhoan);
    }
}