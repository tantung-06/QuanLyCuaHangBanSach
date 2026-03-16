package gui.dialog;

import bus.SachBUS;
import bus.TacGiaBUS;
import bus.NhaXuatBanBUS;
import bus.TheLoaiBUS;
import dto.Sach;

import javax.swing.*;
import java.awt.*;

public class SachDialog extends JDialog {

    private final SachBUS       sachBUS = new SachBUS();
    private final TacGiaBUS     tgBUS   = new TacGiaBUS();
    private final NhaXuatBanBUS nxbBUS  = new NhaXuatBanBUS();
    private final TheLoaiBUS    tlBUS   = new TheLoaiBUS();

    private JTextField txtMaSach, txtTenSach, txtNamXuatBan, txtGiaBan, txtSoLuong;
    private JComboBox<String> cboTacGia, cboNXB, cboTheLoai, cboTrangThai;
    private JLabel lblError;
    private JButton btnLuu, btnHuy;

    private Sach editSach;
    private boolean saved    = false;
    private boolean viewOnly = false;

    public SachDialog(JFrame parent, Sach sach) {
        super(parent, sach == null ? "Thêm Sách" : "Sửa Sách", true);
        this.editSach = sach;
        setSize(500, 480);
        setLocationRelativeTo(parent);
        setResizable(false);
        init();
        if (sach != null) {
            populateFields(sach);
        } else {
            txtMaSach.setText(sachBUS.generateMaSach());
            txtMaSach.setEditable(false);
        }
    }

    public void setViewOnly(boolean viewOnly) {
        this.viewOnly = viewOnly;
        setTitle("Chi tiết Sách");
        txtMaSach.setEditable(false);
        txtTenSach.setEditable(false);
        txtNamXuatBan.setEditable(false);
        txtGiaBan.setEditable(false);
        txtSoLuong.setEditable(false);
        cboTacGia.setEnabled(false);
        cboNXB.setEnabled(false);
        cboTheLoai.setEnabled(false);
        cboTrangThai.setEnabled(false);
        btnLuu.setVisible(false);
    }

    private void init() {
        setLayout(new BorderLayout());
        add(buildForm(),   BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);
    }

    // ==================== FORM ====================

    private JPanel buildForm() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createEmptyBorder(16, 20, 8, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        txtMaSach     = new JTextField();
        txtTenSach    = new JTextField();
        txtNamXuatBan = new JTextField();
        txtGiaBan     = new JTextField();
        txtSoLuong    = new JTextField();

        cboTacGia  = new JComboBox<>();
        tgBUS.getAll().forEach(t ->
            cboTacGia.addItem(t.getMaTacGia() + " - " + t.getTenTacGia()));

        cboNXB = new JComboBox<>();
        nxbBUS.getAll().forEach(n ->
            cboNXB.addItem(n.getMaNXB() + " - " + n.getTenNXB()));

        cboTheLoai = new JComboBox<>();
        tlBUS.getAll().forEach(t ->
            cboTheLoai.addItem(t.getMaTheLoai() + " - " + t.getTenTheLoai()));

        cboTrangThai = new JComboBox<>(new String[]{"CON_HANG", "HET_HANG"});

        Object[][] rows = {
            {"Mã sách *",       txtMaSach},
            {"Tên sách *",      txtTenSach},
            {"Tác giả *",       cboTacGia},
            {"Nhà xuất bản *",  cboNXB},
            {"Thể loại *",      cboTheLoai},
            {"Năm xuất bản",    txtNamXuatBan},
            {"Giá bán *",       txtGiaBan},
            {"Số lượng",    txtSoLuong},
            {"Trạng thái",      cboTrangThai},
        };

        for (int i = 0; i < rows.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0.3;
            JLabel lbl = new JLabel((String) rows[i][0]);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            p.add(lbl, gbc);

            gbc.gridx = 1; gbc.weightx = 0.7;
            p.add((Component) rows[i][1], gbc);
        }

        lblError = new JLabel("");
        lblError.setForeground(new Color(220, 53, 69));
        lblError.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = rows.length;
        gbc.gridwidth = 2;
        p.add(lblError, gbc);

        return p;
    }

    // ==================== FOOTER ====================

    private JPanel buildFooter() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)));

        btnHuy = new JButton("Hủy");
        btnHuy.setPreferredSize(new Dimension(110, 34));
        btnHuy.setBackground(new Color(108, 117, 125));
        btnHuy.setForeground(Color.WHITE);
        btnHuy.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnHuy.setFocusPainted(false);

        btnLuu = new JButton("Lưu");
        btnLuu.setPreferredSize(new Dimension(110, 34));
        btnLuu.setBackground(new Color(40, 167, 69));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLuu.setFocusPainted(false);

        p.add(btnHuy);
        p.add(btnLuu);

        btnHuy.addActionListener(e -> dispose());
        btnLuu.addActionListener(e -> handleLuu());
        return p;
    }

    // ==================== ACTIONS ====================

    private void handleLuu() {
        lblError.setText("");

        String ma  = txtMaSach.getText().trim();
        String ten = txtTenSach.getText().trim();

        if (ma.isEmpty())  { lblError.setText("Mã sách không được để trống!"); return; }
        if (ten.isEmpty()) { lblError.setText("Tên sách không được để trống!"); return; }

        int namXB = 0, soLuong = 0;
        double gia = 0;
        try { namXB = Integer.parseInt(txtNamXuatBan.getText().trim()); }
        catch (Exception e) { lblError.setText("Năm xuất bản không hợp lệ!"); return; }

        try { gia = Double.parseDouble(txtGiaBan.getText().trim().replace(",", "")); }
        catch (Exception e) { lblError.setText("Giá bán không hợp lệ!"); return; }

        try { soLuong = Integer.parseInt(txtSoLuong.getText().trim()); }
        catch (Exception e) { lblError.setText("Số lượng không hợp lệ!"); return; }

        if (gia <= 0)    { lblError.setText("Giá bán phải lớn hơn 0!"); return; }
        if (soLuong < 0) { lblError.setText("Số lượng không được âm!"); return; }

        String maTG  = ((String) cboTacGia.getSelectedItem()).split(" - ")[0];
        String maNXB = ((String) cboNXB.getSelectedItem()).split(" - ")[0];
        String maTL  = ((String) cboTheLoai.getSelectedItem()).split(" - ")[0];
        String tt    = (String) cboTrangThai.getSelectedItem();

        Sach s = new Sach(ma, ten, namXB, gia, soLuong, maTG, maTL, maNXB, tt);

        boolean ok = editSach == null ? sachBUS.insert(s) : sachBUS.update(s);
        if (ok) {
            JOptionPane.showMessageDialog(this,
                editSach == null ? "Thêm sách thành công!" : "Cập nhật thành công!");
            saved = true;
            dispose();
        } else {
            lblError.setText(editSach == null
                ? "Thêm thất bại! Mã sách có thể đã tồn tại."
                : "Cập nhật thất bại!");
        }
    }

    private void populateFields(Sach s) {
        txtMaSach.setText(s.getMaSach());
        txtMaSach.setEditable(false);
        txtTenSach.setText(s.getTenSach());
        txtNamXuatBan.setText(String.valueOf(s.getNamXuatBan()));
        txtGiaBan.setText(String.valueOf((long) s.getGiaBan()));
        txtSoLuong.setText(String.valueOf(s.getSoLuongTon()));
        cboTrangThai.setSelectedItem(s.getTrangThai());

        selectCombo(cboTacGia,  s.getMaTacGia());
        selectCombo(cboNXB,     s.getMaNXB());
        selectCombo(cboTheLoai, s.getMaTheLoai());
    }

    private void selectCombo(JComboBox<String> cbo, String ma) {
        for (int i = 0; i < cbo.getItemCount(); i++) {
            if (cbo.getItemAt(i).startsWith(ma + " - ")) {
                cbo.setSelectedIndex(i);
                return;
            }
        }
    }

    public boolean isSaved() { return saved; }
}