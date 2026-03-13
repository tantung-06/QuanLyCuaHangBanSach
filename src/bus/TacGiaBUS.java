package bus;

import dao.TacGiaDAO;
import dto.TacGia;
import java.util.ArrayList;

/**
 *
 * @author DELL
 */
public class TacGiaBUS {

    private TacGiaDAO tacGiaDAO = new TacGiaDAO();

    public ArrayList<TacGia> getAll() {
        return tacGiaDAO.getAll();
    }

    public TacGia getById(String maTacGia) {
        if (maTacGia == null || maTacGia.trim().isEmpty()) return null;
        return tacGiaDAO.getById(maTacGia);
    }

    public boolean insert(TacGia tg) {
        if (tg == null) return false;
        if (tg.getMaTacGia() == null || tg.getMaTacGia().trim().isEmpty()) return false;
        if (tg.getTenTacGia() == null || tg.getTenTacGia().trim().isEmpty()) return false;
        return tacGiaDAO.insert(tg);
    }

    public boolean update(TacGia tg) {
        if (tg == null || tg.getMaTacGia() == null) return false;
        if (tg.getTenTacGia() == null || tg.getTenTacGia().trim().isEmpty()) return false;
        return tacGiaDAO.update(tg);
    }

    public boolean delete(String maTacGia) {
        if (maTacGia == null || maTacGia.trim().isEmpty()) return false;
        return tacGiaDAO.delete(maTacGia);
    }
}