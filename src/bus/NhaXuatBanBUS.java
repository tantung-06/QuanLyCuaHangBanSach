package bus;

import dao.NhaXuatBanDAO;
import dto.NhaXuatBan;
import java.util.ArrayList;

/**
 *
 * @author DELL
 */
public class NhaXuatBanBUS {

    private NhaXuatBanDAO nhaXuatBanDAO = new NhaXuatBanDAO();

    public ArrayList<NhaXuatBan> getAll() {
        return nhaXuatBanDAO.getAll();
    }

    public NhaXuatBan getById(String maNXB) {
        if (maNXB == null || maNXB.trim().isEmpty()) return null;
        return nhaXuatBanDAO.getById(maNXB);
    }

    public boolean insert(NhaXuatBan nxb) {
        if (nxb == null) return false;
        if (nxb.getMaNXB() == null || nxb.getMaNXB().trim().isEmpty()) return false;
        if (nxb.getTenNXB() == null || nxb.getTenNXB().trim().isEmpty()) return false;
        return nhaXuatBanDAO.insert(nxb);
    }

    public boolean update(NhaXuatBan nxb) {
        if (nxb == null || nxb.getMaNXB() == null) return false;
        if (nxb.getTenNXB() == null || nxb.getTenNXB().trim().isEmpty()) return false;
        return nhaXuatBanDAO.update(nxb);
    }

    public boolean delete(String maNXB) {
        if (maNXB == null || maNXB.trim().isEmpty()) return false;
        return nhaXuatBanDAO.delete(maNXB);
    }
}