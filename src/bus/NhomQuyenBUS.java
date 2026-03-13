package bus;

import dao.NhomQuyenDAO;
import dto.NhomQuyen;
import java.util.ArrayList;

/**
 *
 * @author DELL
 */
public class NhomQuyenBUS {

    private NhomQuyenDAO nhomQuyenDAO = new NhomQuyenDAO();

    public ArrayList<NhomQuyen> getAll() {
        return nhomQuyenDAO.getAll();
    }

    public NhomQuyen getById(String maNhomQuyen) {
        if (maNhomQuyen == null || maNhomQuyen.trim().isEmpty()) return null;
        return nhomQuyenDAO.getByID(maNhomQuyen);
    }

    public boolean insert(NhomQuyen nq) {
        if (nq == null) return false;
        if (nq.getMaNhomQuyen() == null || nq.getMaNhomQuyen().trim().isEmpty()) return false;
        if (nq.getTenNhomQuyen() == null || nq.getTenNhomQuyen().trim().isEmpty()) return false;
        return nhomQuyenDAO.insert(nq);
    }

    public boolean update(NhomQuyen nq) {
        if (nq == null || nq.getMaNhomQuyen() == null) return false;
        if (nq.getTenNhomQuyen() == null || nq.getTenNhomQuyen().trim().isEmpty()) return false;
        return nhomQuyenDAO.update(nq);
    }

    public boolean delete(String maNhomQuyen) {
        if (maNhomQuyen == null || maNhomQuyen.trim().isEmpty()) return false;
        return nhomQuyenDAO.delete(maNhomQuyen);
    }
}