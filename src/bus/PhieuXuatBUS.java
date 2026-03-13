package bus;

import dao.PhieuXuatDAO;
import dao.ChiTietPhieuXuatDAO;
import dao.SachDAO;
import dto.PhieuXuat;
import dto.ChiTietPhieuXuat;
import java.util.ArrayList;

public class PhieuXuatBUS {

    private PhieuXuatDAO phieuXuatDAO = new PhieuXuatDAO();
    private ChiTietPhieuXuatDAO chiTietDAO = new ChiTietPhieuXuatDAO();
    private SachDAO sachDAO = new SachDAO();

    // Phieu xuat
    public ArrayList<PhieuXuat> getAll() {
        return phieuXuatDAO.getAll();
    }

    public ArrayList<PhieuXuat> getByTrangThai(String trangThai) {
        if (trangThai == null || trangThai.trim().isEmpty()) return new ArrayList<>();
        return phieuXuatDAO.getByTrangThai(trangThai);
    }

    public ArrayList<PhieuXuat> getByKhachHang(String maKhachHang) {
        if (maKhachHang == null || maKhachHang.trim().isEmpty()) return new ArrayList<>();
        return phieuXuatDAO.getByKhachHang(maKhachHang);
    }

    public ArrayList<PhieuXuat> getByNhanVien(String maNhanVien) {
        if (maNhanVien == null || maNhanVien.trim().isEmpty()) return new ArrayList<>();
        return phieuXuatDAO.getByNhanVien(maNhanVien);
    }

    public PhieuXuat getById(String maPhieuXuat) {
        if (maPhieuXuat == null || maPhieuXuat.trim().isEmpty()) return null;
        return phieuXuatDAO.getById(maPhieuXuat);
    }

    // Thêm phiếu xuất + chi tiết + trừ tồn kho (transaction)
    public boolean insert(PhieuXuat px, ArrayList<ChiTietPhieuXuat> chiTiet) throws Exception {
        validatePhieuXuat(px);
        if (chiTiet == null || chiTiet.isEmpty())
            throw new Exception("Phiếu xuất phải có ít nhất 1 sách");

        // Kiểm tra tồn kho trước khi lưu
        for (ChiTietPhieuXuat ct : chiTiet) {
            var sach = sachDAO.getById(ct.getMaSach());
            if (sach == null)
                throw new Exception("Không tìm thấy sách: " + ct.getMaSach());
            if (sach.getSoLuongTon() < ct.getSoLuong())
                throw new Exception("Sách '" + sach.getTenSach() + "' không đủ tồn kho. Còn: " + sach.getSoLuongTon());
        }

        boolean okPx = phieuXuatDAO.insert(px);
        if (!okPx) throw new Exception("Lỗi khi lưu phiếu xuất");

        boolean okCt = chiTietDAO.insertBatch(chiTiet);
        if (!okCt) throw new Exception("Lỗi khi lưu chi tiết phiếu xuất");

        // Trừ tồn kho
        for (ChiTietPhieuXuat ct : chiTiet) {
            sachDAO.updateSoLuongTon(ct.getMaSach(), -ct.getSoLuong());
        }

        return true;
    }

    public boolean update(PhieuXuat px) throws Exception {
        if (px == null || px.getMaPhieuXuat() == null) throw new Exception("Phiếu xuất không hợp lệ");
        return phieuXuatDAO.update(px);
    }

    public boolean updateTrangThai(String maPhieuXuat, String trangThai) throws Exception {
        if (maPhieuXuat == null || maPhieuXuat.trim().isEmpty()) throw new Exception("Mã phiếu xuất không hợp lệ");
        if (trangThai == null || trangThai.trim().isEmpty()) throw new Exception("Trạng thái không hợp lệ");
        return phieuXuatDAO.updateTrangThai(maPhieuXuat, trangThai);
    }

    public boolean delete(String maPhieuXuat) throws Exception {
        if (maPhieuXuat == null || maPhieuXuat.trim().isEmpty()) throw new Exception("Mã phiếu xuất không hợp lệ");
        chiTietDAO.deleteByPhieuXuat(maPhieuXuat); // xóa chi tiết trước
        return phieuXuatDAO.delete(maPhieuXuat);
    }

    // Chi tiet phieu xuat
    public ArrayList<ChiTietPhieuXuat> getChiTiet(String maPhieuXuat) {
        if (maPhieuXuat == null || maPhieuXuat.trim().isEmpty()) return new ArrayList<>();
        return chiTietDAO.getByPhieuXuat(maPhieuXuat);
    }

    public boolean insertChiTiet(ChiTietPhieuXuat ct) throws Exception {
        if (ct == null) throw new Exception("Chi tiết không hợp lệ");
        if (ct.getSoLuong() <= 0) throw new Exception("Số lượng phải lớn hơn 0");
        if (ct.getDonGia() <= 0) throw new Exception("Đơn giá phải lớn hơn 0");
        return chiTietDAO.insert(ct);
    }

    public boolean updateChiTiet(ChiTietPhieuXuat ct) throws Exception {
        if (ct == null) throw new Exception("Chi tiết không hợp lệ");
        if (ct.getSoLuong() <= 0) throw new Exception("Số lượng phải lớn hơn 0");
        return chiTietDAO.update(ct);
    }

    public boolean deleteChiTiet(String maPhieuXuat, String maSach) throws Exception {
        if (maPhieuXuat == null || maSach == null) throw new Exception("Thông tin không hợp lệ");
        return chiTietDAO.delete(maPhieuXuat, maSach);
    }

    // validate
    private void validatePhieuXuat(PhieuXuat px) throws Exception {
        if (px == null) throw new Exception("Phiếu xuất không hợp lệ");
        if (px.getMaPhieuXuat() == null || px.getMaPhieuXuat().trim().isEmpty())
            throw new Exception("Mã phiếu xuất không được để trống");
        if (px.getMaNhanVien() == null || px.getMaNhanVien().trim().isEmpty())
            throw new Exception("Nhân viên không được để trống");
        if (px.getNgayLap() == null)
            throw new Exception("Ngày lập không được để trống");
        if (px.getTongTien() < 0)
            throw new Exception("Tổng tiền không hợp lệ");
    }
}