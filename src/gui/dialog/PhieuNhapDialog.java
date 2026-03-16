package gui.dialog;

import bus.PhieuNhapBUS;
import bus.NhaCungCapBUS;
import bus.NhanVienBUS;
import bus.SachBUS;
import dto.PhieuNhap;
import dto.ChiTietPhieuNhap;
import dto.NhaCungCap;
import dto.NhanVien;
import dto.Sach;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PhieuNhapDialog extends JDialog {

    private final PhieuNhapBUS phieuNhapBUS = new PhieuNhapBUS();
    private final NhaCungCapBUS nccBUS       = new NhaCungCapBUS();
    private final NhanVienBUS   nvBUS        = new NhanVienBUS();
    private final SachBUS       sachBUS      = new SachBUS();

    // Header fields
    private JTextField txtMaPhieu, txtNgayNhap;
    private JComboBox<String> cboNCC, cboNhanVien, cboTrangThai;

    // Chi tiết table
    private JTable tblChiTiet;
    private DefaultTableModel chiTietModel;
    private ArrayList<ChiTietPhieuNhap> dsChiTiet = new ArrayList<>();

    // Chi tiết input
    private JComboBox<String> cboSach;
    private JTextField txtSoLuong, txtGiaNhap;

    // Footer
    private JLabel lblTongTien;
    private JButton btnLuu, btnHuy;

    private PhieuNhap editPhieu;
    private boolean saved = false;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public PhieuNhapDialog(JFrame parent, PhieuNhap pn) {
        super(parent, pn == null ? "Thêm Phiếu Nhập" : "Sửa Phiếu Nhập", true);
        this.editPhieu = pn;
        setSize(820, 620);
        setLocationRelativeTo(parent);
        setResizable(false);
        init();
        if (pn != null) {
            populateFields(pn);
        } else {
            txtMaPhieu.setText(phieuNhapBUS.generateMaPhieuNhap());
            txtMaPhieu.setEditable(false);
        }
        if (pn != null) populateFields(pn);
    }
    
    public void setViewOnly(boolean viewOnly) {

        txtMaPhieu.setEditable(false);
        txtNgayNhap.setEditable(false);

        cboNCC.setEnabled(false);
        cboNhanVien.setEnabled(false);
        cboTrangThai.setEnabled(false);

        cboSach.setEnabled(false);
        txtSoLuong.setEditable(false);
        txtGiaNhap.setEditable(false);

        btnLuu.setVisible(false);

        // Ẩn cột nút Xóa
        tblChiTiet.getColumnModel().getColumn(5).setMaxWidth(0);
        tblChiTiet.getColumnModel().getColumn(5).setMinWidth(0);
        tblChiTiet.getColumnModel().getColumn(5).setPreferredWidth(0);
    }

    private void init() {
        setLayout(new BorderLayout(0, 0));
        add(buildHeader(), BorderLayout.NORTH);
        add(buildChiTietPanel(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);
    }

    private JPanel buildHeader() {
        JPanel p = new JPanel(new GridLayout(2, 4, 10, 8));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)));

        p.add(label("Mã phiếu nhập *"));
        p.add(label("Ngày nhập *"));
        p.add(label("Nhà cung cấp *"));
        p.add(label("Nhân viên *"));

        txtMaPhieu  = new JTextField();
        txtNgayNhap = new JTextField(LocalDate.now().format(FMT));

        cboNCC = new JComboBox<>();
        nccBUS.getHoatDong().forEach(n -> cboNCC.addItem(n.getMaNCC() + " - " + n.getTenNCC()));

        cboNhanVien = new JComboBox<>();
        nvBUS.getAll().forEach(n -> cboNhanVien.addItem(n.getMaNhanVien() + " - " + n.getHo() + " " + n.getTen()));

        p.add(txtMaPhieu);
        p.add(txtNgayNhap);
        p.add(cboNCC);
        p.add(cboNhanVien);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        row2.setBackground(Color.WHITE);
        row2.add(label("Trạng thái:"));
        cboTrangThai = new JComboBox<>(new String[]{"DA_NHAP", "HUY"});
        row2.add(cboTrangThai);

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(Color.WHITE);
        wrap.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(0, 16, 8, 16)));
        wrap.add(p, BorderLayout.NORTH);
        wrap.add(row2, BorderLayout.SOUTH);
        return wrap;
    }

    // chi tiết
    private JPanel buildChiTietPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        panel.setBackground(Color.WHITE);

        JPanel inputRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        inputRow.setBackground(Color.WHITE);

        cboSach = new JComboBox<>();
        sachBUS.getAll().forEach(s -> cboSach.addItem(s.getMaSach() + " - " + s.getTenSach()));
        cboSach.setPreferredSize(new Dimension(240, 30));

        txtSoLuong = new JTextField("1", 6);
        txtGiaNhap = new JTextField("0", 10);

        JButton btnThem = new JButton("Thêm sách");
        btnThem.setBackground(new Color(0, 153, 255));
        btnThem.setForeground(Color.WHITE);
        btnThem.setFont(new Font("Segoe UI", Font.BOLD, 12));

        inputRow.add(new JLabel("Sách:"));
        inputRow.add(cboSach);
        inputRow.add(new JLabel("SL:"));
        inputRow.add(txtSoLuong);
        inputRow.add(new JLabel("Giá nhập:"));
        inputRow.add(txtGiaNhap);
        inputRow.add(btnThem);

        // Table chi tiết
        String[] cols = {"Mã sách", "Tên sách", "Số lượng", "Giá nhập", "Thành tiền", ""};
        chiTietModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblChiTiet = new JTable(chiTietModel);
        tblChiTiet.setRowHeight(28);
        tblChiTiet.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblChiTiet.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tblChiTiet.getTableHeader().setBackground(new Color(234, 234, 234));
        tblChiTiet.getColumnModel().getColumn(5).setMaxWidth(60);

        // Nút xóa dòng
        tblChiTiet.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int r, int c) {
                JButton btn = new JButton("Xóa");
                btn.setBackground(new Color(220, 53, 69));
                btn.setForeground(Color.WHITE);
                btn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                return btn;
            }
        });

        tblChiTiet.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tblChiTiet.getSelectedRow();
                int col = tblChiTiet.getSelectedColumn();
                if (col == 5 && row >= 0 && btnLuu.isVisible()) {
                    dsChiTiet.remove(row);
                    chiTietModel.removeRow(row);
                    updateTongTien();
                }
            }
        });

        btnThem.addActionListener(e -> handleThemSach());

        panel.add(inputRow, BorderLayout.NORTH);
        panel.add(new JScrollPane(tblChiTiet), BorderLayout.CENTER);
        return panel;
    }

    // ===== FOOTER =====
    private JPanel buildFooter() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 16, 10, 16)));

        lblTongTien = new JLabel("Tổng tiền: 0 VNĐ");
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTongTien.setForeground(new Color(220, 53, 69));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setBackground(Color.WHITE);

        btnHuy = new JButton("Hủy");
        btnHuy.setPreferredSize(new Dimension(110, 36));
        btnHuy.setBackground(new Color(108, 117, 125));
        btnHuy.setForeground(Color.WHITE);
        btnHuy.setFont(new Font("Segoe UI", Font.BOLD, 13));

        btnLuu = new JButton("Lưu");
        btnLuu.setPreferredSize(new Dimension(110, 36));
        btnLuu.setBackground(new Color(40, 167, 69));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 13));

        btnPanel.add(btnHuy);
        btnPanel.add(btnLuu);

        p.add(lblTongTien, BorderLayout.WEST);
        p.add(btnPanel, BorderLayout.EAST);

        btnHuy.addActionListener(e -> dispose());
        btnLuu.addActionListener(e -> handleLuu());

        return p;
    }

    // ===== ACTIONS =====

    private void handleThemSach() {
        String sachItem = (String) cboSach.getSelectedItem();
        if (sachItem == null) return;
        String maSach = sachItem.split(" - ")[0];
        Sach s = sachBUS.getById(maSach);
        if (s == null) return;

        int sl;
        double gia;
        try {
            sl  = Integer.parseInt(txtSoLuong.getText().trim());
            gia = Double.parseDouble(txtGiaNhap.getText().trim().replace(",", ""));
            if (sl <= 0) throw new Exception();
            if (gia <= 0) throw new Exception();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Số lượng và giá nhập phải là số dương!");
            return;
        }

        // Kiểm tra đã có chưa
        for (int i = 0; i < dsChiTiet.size(); i++) {
            if (dsChiTiet.get(i).getMaSach().equals(maSach)) {
                ChiTietPhieuNhap ct = dsChiTiet.get(i);
                ct = new ChiTietPhieuNhap(ct.getMaPhieuNhap(), maSach,
                        ct.getSoLuong() + sl, gia, (ct.getSoLuong() + sl) * gia);
                dsChiTiet.set(i, ct);
                chiTietModel.setValueAt(ct.getSoLuong(), i, 2);
                chiTietModel.setValueAt(String.format("%,.0f", ct.getGiaNhap()), i, 3);
                chiTietModel.setValueAt(String.format("%,.0f", ct.getThanhTien()), i, 4);
                updateTongTien();
                return;
            }
        }

        double thanhTien = sl * gia;
        ChiTietPhieuNhap ct = new ChiTietPhieuNhap("", maSach, sl, gia, thanhTien);
        dsChiTiet.add(ct);
        chiTietModel.addRow(new Object[]{
            maSach, s.getTenSach(), sl,
            String.format("%,.0f", gia),
            String.format("%,.0f", thanhTien), "Xóa"
        });
        updateTongTien();
    }

    private void handleLuu() {
        String ma = txtMaPhieu.getText().trim();
        if (ma.isEmpty()) { showError("Vui lòng nhập mã phiếu nhập!"); return; }
        if (dsChiTiet.isEmpty()) { showError("Vui lòng thêm ít nhất 1 sách!"); return; }

        LocalDate ngay;
        try { ngay = LocalDate.parse(txtNgayNhap.getText().trim(), FMT); }
        catch (Exception e) { showError("Ngày nhập không hợp lệ (dd/MM/yyyy)!"); return; }

        String nccItem = (String) cboNCC.getSelectedItem();
        String nvItem  = (String) cboNhanVien.getSelectedItem();
        if (nccItem == null || nvItem == null) { showError("Vui lòng chọn NCC và nhân viên!"); return; }

        String maNCC = nccItem.split(" - ")[0];
        String maNV  = nvItem.split(" - ")[0];
        double tong  = dsChiTiet.stream().mapToDouble(ChiTietPhieuNhap::getThanhTien).sum();
        String tt    = (String) cboTrangThai.getSelectedItem();

        PhieuNhap pn = new PhieuNhap(ma, ngay, maNV, maNCC, tong, tt);
        dsChiTiet.forEach(ct -> {
            // gán maPhieuNhap cho từng chi tiết
            try {
                java.lang.reflect.Field f = ct.getClass().getDeclaredField("maPhieuNhap");
                f.setAccessible(true);
                f.set(ct, ma);
            } catch (Exception ignored) {}
        });

        // Dùng setter nếu có, hoặc tạo lại object
        ArrayList<ChiTietPhieuNhap> chiTietVoiMa = new ArrayList<>();
        for (ChiTietPhieuNhap ct : dsChiTiet) {
            chiTietVoiMa.add(new ChiTietPhieuNhap(ma, ct.getMaSach(),
                    ct.getSoLuong(), ct.getGiaNhap(), ct.getThanhTien()));
        }

        try {
            if (editPhieu == null) {
                phieuNhapBUS.insert(pn, chiTietVoiMa);
                JOptionPane.showMessageDialog(this, "Thêm phiếu nhập thành công!");
            } else {
                phieuNhapBUS.update(pn);
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            }
            saved = true;
            dispose();
        } catch (Exception e) {
            showError("Lỗi: " + e.getMessage());
        }
    }

    private void populateFields(PhieuNhap pn) {
        txtMaPhieu.setText(pn.getMaPhieuNhap());
        txtMaPhieu.setEditable(false);
        txtNgayNhap.setText(pn.getNgayNhap().format(FMT));
        cboTrangThai.setSelectedItem(pn.getTrangThai());

        // Load chi tiết
        dsChiTiet = phieuNhapBUS.getChiTiet(pn.getMaPhieuNhap());
        chiTietModel.setRowCount(0);
        for (ChiTietPhieuNhap ct : dsChiTiet) {
            Sach s = sachBUS.getById(ct.getMaSach());
            chiTietModel.addRow(new Object[]{
                ct.getMaSach(),
                s != null ? s.getTenSach() : "",
                ct.getSoLuong(),
                String.format("%,.0f", ct.getGiaNhap()),
                String.format("%,.0f", ct.getThanhTien()), "Xóa"
            });
        }
        updateTongTien();
    }

    private void updateTongTien() {
        double tong = dsChiTiet.stream().mapToDouble(ChiTietPhieuNhap::getThanhTien).sum();
        lblTongTien.setText("Tổng tiền: " + String.format("%,.0f", tong) + " VNĐ");
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return l;
    }

    public boolean isSaved() { return saved; }
}