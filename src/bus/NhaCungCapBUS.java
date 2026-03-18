package bus;

import dao.NhaCungCapDAO;
import dto.NhaCungCap;
import java.util.ArrayList;

/**
 *
 * @author DELL
 */
public class NhaCungCapBUS {

    private NhaCungCapDAO nhaCungCapDAO = new NhaCungCapDAO();

    public ArrayList<NhaCungCap> getAll() {
        return nhaCungCapDAO.getAll();
    }

    public ArrayList<NhaCungCap> getHoatDong() {
        return nhaCungCapDAO.getHoatDong();
    }

    public ArrayList<NhaCungCap> getByTrangThai(String trangThai) {
        if (trangThai == null || trangThai.trim().isEmpty())
            return new ArrayList<>();
        return nhaCungCapDAO.getByTrangThai(trangThai);
    }

    public NhaCungCap getById(String maNCC) {
        if (maNCC == null || maNCC.trim().isEmpty())
            return null;
        return nhaCungCapDAO.getById(maNCC);
    }

    public boolean insert(NhaCungCap ncc) {
        if (ncc == null)
            return false;
        if (ncc.getMaNCC() == null || ncc.getMaNCC().trim().isEmpty())
            return false;
        if (ncc.getTenNCC() == null || ncc.getTenNCC().trim().isEmpty())
            return false;
        if (ncc.getSoDienThoai() == null || ncc.getSoDienThoai().trim().isEmpty())
            return false;
        return nhaCungCapDAO.insert(ncc);
    }

    public boolean update(NhaCungCap ncc) {
        if (ncc == null || ncc.getMaNCC() == null)
            return false;
        if (ncc.getTenNCC() == null || ncc.getTenNCC().trim().isEmpty())
            return false;
        if (ncc.getSoDienThoai() == null || ncc.getSoDienThoai().trim().isEmpty())
            return false;
        return nhaCungCapDAO.update(ncc);
    }

    public boolean updateTrangThai(String maNCC, String trangThai) {
        if (maNCC == null || maNCC.trim().isEmpty())
            return false;
        if (trangThai == null || trangThai.trim().isEmpty())
            return false;
        return nhaCungCapDAO.updateTrangThai(maNCC, trangThai);
    }

    public boolean delete(String maNCC) {
        if (maNCC == null || maNCC.trim().isEmpty())
            return false;
        return nhaCungCapDAO.delete(maNCC);
    }

    public String generateMaNCC() {
        return nhaCungCapDAO.generateMaNCC();
    }
}