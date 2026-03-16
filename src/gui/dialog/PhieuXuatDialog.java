package gui.dialog;

import bus.PhieuXuatBUS;
import bus.KhachHangBUS;
import bus.NhanVienBUS;
import bus.SachBUS;
import bus.KhuyenMaiBUS;
import dto.PhieuXuat;
import dto.ChiTietPhieuXuat;
import dto.KhuyenMai;
import dto.Sach;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PhieuXuatDialog extends JDialog {

    private final PhieuXuatBUS  pxBUS   = new PhieuXuatBUS();
    private final KhachHangBUS  khBUS   = new KhachHangBUS();
    private final NhanVienBUS   nvBUS   = new NhanVienBUS();
    private final SachBUS       sachBUS = new SachBUS();
    private final KhuyenMaiBUS  kmBUS   = new KhuyenMaiBUS();

    // Header
    private JTextField txtMaPX, txtNgayLap;
    private JComboBox<String> cboKH, cboNV, cboKhuyenMai, cboTrangThai;

    // Chi tiết
    private JTable tblChiTiet;
    private DefaultTableModel chiTietModel;
    private ArrayList<ChiTietPhieuXuat> dsChiTiet = new ArrayList<>();

    // Input thêm sách
    private JComboBox<String> cboSach;
    private JTextField txtSoLuong, txtGiaBan;

    // Footer
    private JLabel lblTongTien;
    private JButton btnLuu, btnHuy;

    private PhieuXuat editPX;
    private boolean saved    = false;
    private boolean viewOnly = false;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public PhieuXuatDialog(JFrame parent, PhieuXuat px) {
        super(parent, px == null ? "Thêm Phiếu Xuất" : "Chi tiết Phiếu Xuất", true);
        this.editPX = px;
        setSize(860, 640);
        setLocationRelativeTo(parent);
        setResizable(false);
        init();
        if (px != null) {
            populateFields(px);
        } else {
            txtMaPX.setText(pxBUS.generateMaPhieuXuat());
            txtMaPX.setEditable(false);
        }
        if (px != null) populateFields(px);
    }

    public void setViewOnly(boolean viewOnly) {
        this.viewOnly = viewOnly;
        setTitle("Chi tiết Phiếu Xuất");
        txtMaPX.setEditable(false);
        txtNgayLap.setEditable(false);
        cboKH.setEnabled(false);
        cboNV.setEnabled(false);
        cboKhuyenMai.setEnabled(false);
        cboTrangThai.setEnabled(false);
        cboSach.setEnabled(false);
        txtSoLuong.setEditable(false);
        txtGiaBan.setEditable(false);
        btnLuu.setVisible(false);
        // Ẩn nút Xóa trong bảng chi tiết
        tblChiTiet.getColumnModel().getColumn(5).setMaxWidth(0);
        tblChiTiet.getColumnModel().getColumn(5).setMinWidth(0);
    }

    private void init() {
        setLayout(new BorderLayout(0, 0));
        add(buildHeader(),      BorderLayout.NORTH);
        add(buildChiTiet(),     BorderLayout.CENTER);
        add(buildFooter(),      BorderLayout.SOUTH);
    }

    // ==================== HEADER ====================

    private JPanel buildHeader() {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(Color.WHITE);
        wrap.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(12, 16, 8, 16)));

        // Row 1: mã, ngày, KH, NV
        JPanel row1 = new JPanel(new GridLayout(2, 4, 10, 6));
        row1.setBackground(Color.WHITE);

        row1.add(lbl("Mã phiếu xuất *"));
        row1.add(lbl("Ngày lập *"));
        row1.add(lbl("Khách hàng"));
        row1.add(lbl("Nhân viên *"));

        txtMaPX    = new JTextField();
        txtNgayLap = new JTextField(LocalDate.now().format(FMT));

        cboKH = new JComboBox<>();
        cboKH.addItem("-- Không có --");
        khBUS.getAll().forEach(k ->
            cboKH.addItem(k.getMaKhachHang() + " - " + k.getHo() + " " + k.getTen()));

        cboNV = new JComboBox<>();
        nvBUS.getAll().forEach(n ->
            cboNV.addItem(n.getMaNhanVien() + " - " + n.getHo() + " " + n.getTen()));

        row1.add(txtMaPX);
        row1.add(txtNgayLap);
        row1.add(cboKH);
        row1.add(cboNV);

        // Row 2: khuyến mãi, trạng thái
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        row2.setBackground(Color.WHITE);

        row2.add(lbl("Khuyến mãi:"));
        cboKhuyenMai = new JComboBox<>();
        cboKhuyenMai.addItem("-- Không áp dụng --");
        kmBUS.getKhuyenMaiConHieuLuc().forEach(km ->
            cboKhuyenMai.addItem(km.getMaKhuyenMai() + " - " + km.getTenKhuyenMai()
                + " (Giảm " + (int) km.getPhanTramGiam() + "%)"));
        cboKhuyenMai.setPreferredSize(new Dimension(260, 28));
        cboKhuyenMai.addActionListener(e -> updateTongTien());
        row2.add(cboKhuyenMai);

        row2.add(Box.createHorizontalStrut(20));
        row2.add(lbl("Trạng thái:"));
        cboTrangThai = new JComboBox<>(new String[]{"DA_XUAT", "HUY"});
        row2.add(cboTrangThai);

        wrap.add(row1, BorderLayout.NORTH);
        wrap.add(row2, BorderLayout.SOUTH);
        return wrap;
    }

    // ==================== CHI TIẾT ====================

    private JPanel buildChiTiet() {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(8, 16, 8, 16));

        // Input row
        JPanel inputRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        inputRow.setBackground(Color.WHITE);

        cboSach = new JComboBox<>();
        sachBUS.getByTrangThai("CON_HANG").forEach(s ->
            cboSach.addItem(s.getMaSach() + " - " + s.getTenSach()
                + " (Tồn: " + s.getSoLuongTon() + ")"));
        cboSach.setPreferredSize(new Dimension(280, 30));

        txtSoLuong = new JTextField("1", 5);
        txtGiaBan  = new JTextField("0", 10);

        // Auto điền giá khi chọn sách
        cboSach.addActionListener(e -> {
            String item = (String) cboSach.getSelectedItem();
            if (item == null) return;
            Sach s = sachBUS.getById(item.split(" - ")[0]);
            if (s != null) txtGiaBan.setText(String.valueOf((long) s.getGiaBan()));
        });

        JButton btnThemSach = new JButton("Thêm sách");
        btnThemSach.setBackground(new Color(0, 153, 255));
        btnThemSach.setForeground(Color.WHITE);
        btnThemSach.setFocusPainted(false);
        btnThemSach.addActionListener(e -> handleThemSach());

        inputRow.add(lbl("Sách:"));    inputRow.add(cboSach);
        inputRow.add(lbl("SL:"));      inputRow.add(txtSoLuong);
        inputRow.add(lbl("Giá bán:")); inputRow.add(txtGiaBan);
        inputRow.add(btnThemSach);

        // Table
        String[] cols = {"Mã sách", "Tên sách", "Số lượng", "Giá bán", "Thành tiền", ""};
        chiTietModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblChiTiet = new JTable(chiTietModel);
        tblChiTiet.setRowHeight(28);
        tblChiTiet.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblChiTiet.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tblChiTiet.getTableHeader().setBackground(new Color(234, 234, 234));
        tblChiTiet.getColumnModel().getColumn(5).setMaxWidth(60);

        tblChiTiet.getColumnModel().getColumn(5).setCellRenderer(
            (t, v, sel, foc, r, c) -> {
                JButton b = new JButton("Xóa");
                b.setBackground(new Color(220, 53, 69));
                b.setForeground(Color.WHITE);
                b.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                return b;
            });

        tblChiTiet.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tblChiTiet.getSelectedRow();
                int col = tblChiTiet.getSelectedColumn();
                if (col == 5 && row >= 0 && !viewOnly) {
                    dsChiTiet.remove(row);
                    chiTietModel.removeRow(row);
                    updateTongTien();
                }
            }
        });

        panel.add(inputRow, BorderLayout.NORTH);
        panel.add(new JScrollPane(tblChiTiet), BorderLayout.CENTER);
        return panel;
    }

    // ==================== FOOTER ====================

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
        btnHuy.setFocusPainted(false);

        btnLuu = new JButton("Lưu");
        btnLuu.setPreferredSize(new Dimension(110, 36));
        btnLuu.setBackground(new Color(40, 167, 69));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLuu.setFocusPainted(false);

        btnPanel.add(btnHuy);
        btnPanel.add(btnLuu);
        p.add(lblTongTien, BorderLayout.WEST);
        p.add(btnPanel,    BorderLayout.EAST);

        btnHuy.addActionListener(e -> dispose());
        btnLuu.addActionListener(e -> handleLuu());
        return p;
    }

    // ==================== ACTIONS ====================

    private void handleThemSach() {
        String sItem = (String) cboSach.getSelectedItem();
        if (sItem == null) return;
        String maSach = sItem.split(" - ")[0];
        Sach s = sachBUS.getById(maSach);
        if (s == null) return;

        int sl;
        double gia;
        try {
            sl  = Integer.parseInt(txtSoLuong.getText().trim());
            gia = Double.parseDouble(txtGiaBan.getText().trim().replace(",", ""));
            if (sl <= 0 || gia <= 0) throw new Exception();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Số lượng và giá phải là số dương!");
            return;
        }

        // Kiểm tra tồn kho
        int tongDat = sl;
        for (ChiTietPhieuXuat ct : dsChiTiet)
            if (ct.getMaSach().equals(maSach)) tongDat += ct.getSoLuong();
        if (tongDat > s.getSoLuongTon()) {
            JOptionPane.showMessageDialog(this,
                "Không đủ tồn kho! Hiện còn: " + s.getSoLuongTon(),
                "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Cập nhật nếu đã có
        for (int i = 0; i < dsChiTiet.size(); i++) {
            if (dsChiTiet.get(i).getMaSach().equals(maSach)) {
                ChiTietPhieuXuat ct = dsChiTiet.get(i);
                int newSl = ct.getSoLuong() + sl;
                double tt = newSl * gia;
                dsChiTiet.set(i, new ChiTietPhieuXuat(ct.getMaPhieuXuat(), maSach, newSl, gia, tt));
                chiTietModel.setValueAt(newSl, i, 2);
                chiTietModel.setValueAt(String.format("%,.0f", gia), i, 3);
                chiTietModel.setValueAt(String.format("%,.0f", tt),  i, 4);
                updateTongTien();
                return;
            }
        }

        double thanhTien = sl * gia;
        dsChiTiet.add(new ChiTietPhieuXuat("", maSach, sl, gia, thanhTien));
        chiTietModel.addRow(new Object[]{
            maSach, s.getTenSach(), sl,
            String.format("%,.0f", gia),
            String.format("%,.0f", thanhTien), "Xóa"
        });
        updateTongTien();
    }

    private void handleLuu() {
        String ma = txtMaPX.getText().trim();
        if (ma.isEmpty()) { showErr("Vui lòng nhập mã phiếu xuất!"); return; }
        if (dsChiTiet.isEmpty()) { showErr("Vui lòng thêm ít nhất 1 sách!"); return; }

        LocalDate ngay;
        try { ngay = LocalDate.parse(txtNgayLap.getText().trim(), FMT); }
        catch (Exception e) { showErr("Ngày lập không hợp lệ (dd/MM/yyyy)!"); return; }

        String nvItem = (String) cboNV.getSelectedItem();
        if (nvItem == null) { showErr("Vui lòng chọn nhân viên!"); return; }
        String maNV = nvItem.split(" - ")[0];

        String khItem = (String) cboKH.getSelectedItem();
        String maKH = (khItem == null || khItem.startsWith("--")) ? null : khItem.split(" - ")[0];

        String kmItem = (String) cboKhuyenMai.getSelectedItem();
        String maKM   = (kmItem == null || kmItem.startsWith("--")) ? null : kmItem.split(" - ")[0];

        double tong = tinhTongSauKM();
        String tt   = (String) cboTrangThai.getSelectedItem();

        PhieuXuat px = new PhieuXuat(ma, ngay, maNV, maKH, maKM, tong, tt);

        // Gán mã phiếu cho chi tiết
        ArrayList<ChiTietPhieuXuat> ctVoiMa = new ArrayList<>();
        for (ChiTietPhieuXuat ct : dsChiTiet)
            ctVoiMa.add(new ChiTietPhieuXuat(ma, ct.getMaSach(),
                    ct.getSoLuong(), ct.getDonGia(), ct.getThanhTien()));

        try {
            if (editPX == null) {
                pxBUS.insert(px, ctVoiMa);
                JOptionPane.showMessageDialog(this, "Thêm phiếu xuất thành công!");
            } else {
                pxBUS.update(px);
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            }
            saved = true;
            dispose();
        } catch (Exception e) {
            showErr("Lỗi: " + e.getMessage());
        }
    }

    private void populateFields(PhieuXuat px) {
        txtMaPX.setText(px.getMaPhieuXuat());
        txtMaPX.setEditable(false);
        txtNgayLap.setText(px.getNgayLap().format(FMT));
        cboTrangThai.setSelectedItem(px.getTrangThai());

        // Chọn khách hàng
        if (px.getMaKhachHang() != null) {
            for (int i = 0; i < cboKH.getItemCount(); i++)
                if (cboKH.getItemAt(i).startsWith(px.getMaKhachHang() + " - "))
                    { cboKH.setSelectedIndex(i); break; }
        }

        // Chọn nhân viên
        for (int i = 0; i < cboNV.getItemCount(); i++)
            if (cboNV.getItemAt(i).startsWith(px.getMaNhanVien() + " - "))
                { cboNV.setSelectedIndex(i); break; }

        // Chọn khuyến mãi
        if (px.getMaKhuyenMai() != null) {
            for (int i = 0; i < cboKhuyenMai.getItemCount(); i++)
                if (cboKhuyenMai.getItemAt(i).startsWith(px.getMaKhuyenMai() + " - "))
                    { cboKhuyenMai.setSelectedIndex(i); break; }
        }

        // Load chi tiết
        dsChiTiet = pxBUS.getChiTiet(px.getMaPhieuXuat());
        chiTietModel.setRowCount(0);
        for (ChiTietPhieuXuat ct : dsChiTiet) {
            Sach s = sachBUS.getById(ct.getMaSach());
            chiTietModel.addRow(new Object[]{
                ct.getMaSach(),
                s != null ? s.getTenSach() : "",
                ct.getSoLuong(),
                String.format("%,.0f", ct.getDonGia()),
                String.format("%,.0f", ct.getThanhTien()), "Xóa"
            });
        }
        updateTongTien();
    }

    // ==================== HELPERS ====================

    private double tinhTongSauKM() {
        double subtotal = dsChiTiet.stream()
                .mapToDouble(ChiTietPhieuXuat::getThanhTien).sum();
        String kmItem = (String) cboKhuyenMai.getSelectedItem();
        if (kmItem == null || kmItem.startsWith("--")) return subtotal;
        String maKM = kmItem.split(" - ")[0];
        KhuyenMai km = kmBUS.getById(maKM);
        if (km == null || subtotal < km.getDonHangToiThieu()) return subtotal;
        return subtotal * (1 - km.getPhanTramGiam() / 100.0);
    }

    private void updateTongTien() {
        double tong = tinhTongSauKM();
        lblTongTien.setText("Tổng tiền: " + String.format("%,.0f", tong) + " VNĐ");
    }

    private void showErr(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    private JLabel lbl(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return l;
    }

    public boolean isSaved() { return saved; }
}