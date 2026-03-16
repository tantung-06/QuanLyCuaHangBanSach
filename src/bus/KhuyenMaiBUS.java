package bus;

import dao.KhuyenMaiDAO;
import dto.KhuyenMai;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author DELL
 */
public class KhuyenMaiBUS {

    private KhuyenMaiDAO khuyenMaiDAO = new KhuyenMaiDAO();

    public ArrayList<KhuyenMai> getAll() {
        return khuyenMaiDAO.getAll();
    }

    public ArrayList<KhuyenMai> getApDung() {
        return khuyenMaiDAO.getApDung();
    }
    
    public ArrayList<KhuyenMai> getByTrangThai(String trangThai) {
        if (trangThai == null || trangThai.trim().isEmpty()) return new ArrayList<>();
        return khuyenMaiDAO.getByTrangThai(trangThai);
    }

    public KhuyenMai getById(String maKhuyenMai) {
        if (maKhuyenMai == null || maKhuyenMai.trim().isEmpty()) return null;
        return khuyenMaiDAO.getById(maKhuyenMai);
    }

    public boolean insert(KhuyenMai km) {
        if (km == null) return false;
        if (km.getMaKhuyenMai() == null || km.getMaKhuyenMai().trim().isEmpty()) return false;
        if (km.getTenKhuyenMai() == null || km.getTenKhuyenMai().trim().isEmpty()) return false;
        if (km.getDonHangToiThieu() < 0) return false;
        if (km.getPhanTramGiam() <= 0 || km.getPhanTramGiam() > 100) return false;
        if (km.getNgayBatDau() == null || km.getNgayKetThuc() == null) return false;
        if (km.getNgayKetThuc().isBefore(km.getNgayBatDau())) return false;
        return khuyenMaiDAO.insert(km);
    }

    public boolean update(KhuyenMai km) {
        if (km == null || km.getMaKhuyenMai() == null) return false;
        if (km.getTenKhuyenMai() == null || km.getTenKhuyenMai().trim().isEmpty()) return false;
        if (km.getDonHangToiThieu() < 0) return false;
        if (km.getPhanTramGiam() <= 0 || km.getPhanTramGiam() > 100) return false;
        if (km.getNgayBatDau() == null || km.getNgayKetThuc() == null) return false;
        if (km.getNgayKetThuc().isBefore(km.getNgayBatDau())) return false;
        return khuyenMaiDAO.update(km);
    }

    public boolean updateTrangThai(String maKhuyenMai, String trangThai) {
        if (maKhuyenMai == null || maKhuyenMai.trim().isEmpty()) return false;
        if (trangThai == null || trangThai.trim().isEmpty()) return false;
        return khuyenMaiDAO.updateTrangThai(maKhuyenMai, trangThai);
    }

    public boolean delete(String maKhuyenMai) {
        if (maKhuyenMai == null || maKhuyenMai.trim().isEmpty()) return false;
        return khuyenMaiDAO.delete(maKhuyenMai);
    }

    // Tìm khuyến mãi phù hợp với tổng đơn hàng
    public KhuyenMai timKhuyenMaiPhuHop(double tongDonHang) {
        ArrayList<KhuyenMai> dsApDung = khuyenMaiDAO.getApDung();
        LocalDate homNay = LocalDate.now();
        KhuyenMai tot = null;
        for (KhuyenMai km : dsApDung) {
            if (tongDonHang >= km.getDonHangToiThieu()
                    && !homNay.isBefore(km.getNgayBatDau())
                    && !homNay.isAfter(km.getNgayKetThuc())) {
                if (tot == null || km.getPhanTramGiam() > tot.getPhanTramGiam()) {
                    tot = km;
                }
            }
        }
        return tot; // null nếu không có KM phù hợp
    }
    
    public ArrayList<KhuyenMai> getKhuyenMaiConHieuLuc() {
        LocalDate homNay = LocalDate.now();
        ArrayList<KhuyenMai> ds = new ArrayList<>();
        for (KhuyenMai km : khuyenMaiDAO.getApDung()) {
            if (!homNay.isBefore(km.getNgayBatDau())
                    && !homNay.isAfter(km.getNgayKetThuc())) {
                ds.add(km);
            }
        }
        return ds;
    }
    
    public String generateMaKhuyenMai() {
        return khuyenMaiDAO.generateMaKhuyenMai();
    }
}