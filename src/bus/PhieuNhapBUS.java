package bus;

import dao.PhieuNhapDAO;
import dao.ChiTietPhieuNhapDAO;
import dao.SachDAO;
import dto.PhieuNhap;
import dto.ChiTietPhieuNhap;
import java.util.ArrayList;

public class PhieuNhapBUS {

    private PhieuNhapDAO phieuNhapDAO = new PhieuNhapDAO();
    private ChiTietPhieuNhapDAO chiTietDAO = new ChiTietPhieuNhapDAO();
    private SachDAO sachDAO = new SachDAO();

    //Phieu nhap
    public ArrayList<PhieuNhap> getAll() {
        return phieuNhapDAO.getAll();
    }

    public ArrayList<PhieuNhap> getByTrangThai(String trangThai) {
        if (trangThai == null || trangThai.trim().isEmpty()) return new ArrayList<>();
        return phieuNhapDAO.getByTrangThai(trangThai);
    }

    public ArrayList<PhieuNhap> getByNhanVien(String maNhanVien) {
        if (maNhanVien == null || maNhanVien.trim().isEmpty()) return new ArrayList<>();
        return phieuNhapDAO.getByNhanVien(maNhanVien);
    }

    public PhieuNhap getById(String maPhieuNhap) {
        if (maPhieuNhap == null || maPhieuNhap.trim().isEmpty()) return null;
        return phieuNhapDAO.getById(maPhieuNhap);
    }

    // Thêm phiếu nhập + chi tiết + cộng tồn kho
    public boolean insert(PhieuNhap pn, ArrayList<ChiTietPhieuNhap> chiTiet) throws Exception {
        validatePhieuNhap(pn);
        if (chiTiet == null || chiTiet.isEmpty())
            throw new Exception("Phiếu nhập phải có ít nhất 1 sách");

        boolean okPn = phieuNhapDAO.insert(pn);
        if (!okPn) throw new Exception("Lỗi khi lưu phiếu nhập");

        boolean okCt = chiTietDAO.insertBatch(chiTiet);
        if (!okCt) throw new Exception("Lỗi khi lưu chi tiết phiếu nhập");

        // Cộng tồn kho
        for (ChiTietPhieuNhap ct : chiTiet) {
            sachDAO.updateSoLuongTon(ct.getMaSach(), ct.getSoLuong());
        }
        return true;
    }

    public boolean update(PhieuNhap pn) throws Exception {
        if (pn == null || pn.getMaPhieuNhap() == null)
            throw new Exception("Phiếu nhập không hợp lệ");
        return phieuNhapDAO.update(pn);
    }

    public boolean updateTrangThai(String maPhieuNhap, String trangThai) throws Exception {
        if (maPhieuNhap == null || maPhieuNhap.trim().isEmpty())
            throw new Exception("Mã phiếu nhập không hợp lệ");
        if (trangThai == null || trangThai.trim().isEmpty())
            throw new Exception("Trạng thái không hợp lệ");

        // Nếu hủy phiếu nhập thì trừ tồn kho theo chi tiết phiếu
        PhieuNhap existing = phieuNhapDAO.getById(maPhieuNhap);
        if (existing == null) throw new Exception("Không tìm thấy phiếu nhập");

        if (!"HUY".equals(existing.getTrangThai()) && "HUY".equals(trangThai)) {
            ArrayList<ChiTietPhieuNhap> chiTiet = chiTietDAO.getByPhieuNhap(maPhieuNhap);
            for (ChiTietPhieuNhap ct : chiTiet) {
                sachDAO.updateSoLuongTon(ct.getMaSach(), -ct.getSoLuong());
            }
        }

        return phieuNhapDAO.updateTrangThai(maPhieuNhap, trangThai);
    }

    // Xóa phiếu nhập + hoàn tồn kho
    public boolean delete(String maPhieuNhap) throws Exception {
        if (maPhieuNhap == null || maPhieuNhap.trim().isEmpty())
            throw new Exception("Mã phiếu nhập không hợp lệ");

        // Hoàn lại tồn kho trước khi xóa
        ArrayList<ChiTietPhieuNhap> chiTiet = chiTietDAO.getByPhieuNhap(maPhieuNhap);
        for (ChiTietPhieuNhap ct : chiTiet) {
            sachDAO.updateSoLuongTon(ct.getMaSach(), -ct.getSoLuong());
        }

        chiTietDAO.deleteByPhieuNhap(maPhieuNhap);
        return phieuNhapDAO.delete(maPhieuNhap);
    }

    //chi tiet phieu nhap
    public ArrayList<ChiTietPhieuNhap> getChiTiet(String maPhieuNhap) {
        if (maPhieuNhap == null || maPhieuNhap.trim().isEmpty()) return new ArrayList<>();
        return chiTietDAO.getByPhieuNhap(maPhieuNhap);
    }

    public boolean insertChiTiet(ChiTietPhieuNhap ct) throws Exception {
        if (ct == null) throw new Exception("Chi tiết không hợp lệ");
        if (ct.getSoLuong() <= 0) throw new Exception("Số lượng phải lớn hơn 0");
        if (ct.getGiaNhap() <= 0) throw new Exception("Giá nhập phải lớn hơn 0");
        return chiTietDAO.insert(ct);
    }

    public boolean updateChiTiet(ChiTietPhieuNhap ct) throws Exception {
        if (ct == null) throw new Exception("Chi tiết không hợp lệ");
        if (ct.getSoLuong() <= 0) throw new Exception("Số lượng phải lớn hơn 0");
        if (ct.getGiaNhap() <= 0) throw new Exception("Giá nhập phải lớn hơn 0");
        return chiTietDAO.update(ct);
    }

    public boolean deleteChiTiet(String maPhieuNhap, String maSach) throws Exception {
        if (maPhieuNhap == null || maSach == null)
            throw new Exception("Thông tin không hợp lệ");
        return chiTietDAO.delete(maPhieuNhap, maSach);
    }
    
    public String generateMaPhieuNhap() {
        return phieuNhapDAO.generateMaPhieuNhap();
    }

    //validate
    private void validatePhieuNhap(PhieuNhap pn) throws Exception {
        if (pn == null) throw new Exception("Phiếu nhập không hợp lệ");
        if (pn.getMaPhieuNhap() == null || pn.getMaPhieuNhap().trim().isEmpty())
            throw new Exception("Mã phiếu nhập không được để trống");
        if (pn.getMaNhanVien() == null || pn.getMaNhanVien().trim().isEmpty())
            throw new Exception("Nhân viên không được để trống");
        if (pn.getMaNCC() == null || pn.getMaNCC().trim().isEmpty())
            throw new Exception("Nhà cung cấp không được để trống");
        if (pn.getNgayNhap() == null)
            throw new Exception("Ngày nhập không được để trống");
        if (pn.getTongTien() < 0)
            throw new Exception("Tổng tiền không hợp lệ");
    }
}