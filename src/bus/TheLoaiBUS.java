package bus;

import dao.TheLoaiDao;
import dto.TheLoai;
import java.util.ArrayList;

/**
 *
 * @author DELL
 */
public class TheLoaiBUS {

    private TheLoaiDao theLoaiDao = new TheLoaiDao();

    public ArrayList<TheLoai> getAll() {
        return theLoaiDao.getAll();
    }

    public boolean insert(TheLoai tl) {
        if (tl == null) return false;
        if (tl.getMaTheLoai() == null || tl.getMaTheLoai().trim().isEmpty()) return false;
        if (tl.getTenTheLoai() == null || tl.getTenTheLoai().trim().isEmpty()) return false;
        return theLoaiDao.insert(tl);
    }

    public boolean update(TheLoai tl) {
        if (tl == null || tl.getMaTheLoai() == null) return false;
        if (tl.getTenTheLoai() == null || tl.getTenTheLoai().trim().isEmpty()) return false;
        return theLoaiDao.update(tl);
    }

    public boolean delete(String maTheLoai) {
        if (maTheLoai == null || maTheLoai.trim().isEmpty()) return false;
        return theLoaiDao.delete(maTheLoai);
    }
}