package gui.dialog;

import bus.KhuyenMaiBUS;
import dto.KhuyenMai;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class KhuyenMaiDialog extends JDialog {

    private final KhuyenMaiBUS kmBUS = new KhuyenMaiBUS();
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private JTextField txtMa, txtTen, txtDonToiThieu, txtPhanTram;
    private JTextField txtNgayBD, txtNgayKT;
    private JComboBox<String> cboTrangThai;
    private JLabel lblError;
    private JButton btnLuu, btnHuy;

    private KhuyenMai editKM;
    private boolean saved    = false;
    private boolean viewOnly = false;

    public KhuyenMaiDialog(JFrame parent, KhuyenMai km) {
        super(parent, km == null ? "Thêm Khuyến Mãi" : "Sửa Khuyến Mãi", true);
        this.editKM = km;
        setSize(480, 420);
        setLocationRelativeTo(parent);
        setResizable(false);
        init();
        if (km != null) {
            populateFields(km);
        } else {
            txtMa.setText(kmBUS.generateMaKhuyenMai());
            txtMa.setEditable(false);
        }
        if (km != null) populateFields(km);
    }

    public void setViewOnly(boolean viewOnly) {
        this.viewOnly = viewOnly;
        setTitle("Chi tiết Khuyến Mãi");
        txtMa.setEditable(false);
        txtTen.setEditable(false);
        txtDonToiThieu.setEditable(false);
        txtPhanTram.setEditable(false);
        txtNgayBD.setEditable(false);
        txtNgayKT.setEditable(false);
        cboTrangThai.setEnabled(false);
        btnLuu.setVisible(false);
    }

    private void init() {
        setLayout(new BorderLayout());
        add(buildForm(),   BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);
    }

    private JPanel buildForm() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createEmptyBorder(16, 20, 8, 20));
        p.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        txtMa          = new JTextField();
        txtTen         = new JTextField();
        txtDonToiThieu = new JTextField();
        txtPhanTram    = new JTextField();
        txtNgayBD      = new JTextField();
        txtNgayBD.setToolTipText("Định dạng: dd/MM/yyyy");
        txtNgayKT      = new JTextField();
        txtNgayKT.setToolTipText("Định dạng: dd/MM/yyyy");
        cboTrangThai   = new JComboBox<>(new String[]{"AP_DUNG", "HET_HAN"});

        Object[][] rows = {
            {"Mã khuyến mãi *",      txtMa},
            {"Tên khuyến mãi *",     txtTen},
            {"Đơn hàng tối thiểu *", txtDonToiThieu},
            {"Phần trăm giảm (%) *",             txtPhanTram},
            {"Ngày bắt đầu *",       txtNgayBD},
            {"Ngày kết thúc *",      txtNgayKT},
            {"Trạng thái",           cboTrangThai},
        };

        for (int i = 0; i < rows.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0.38;
            JLabel lbl = new JLabel((String) rows[i][0]);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            p.add(lbl, gbc);

            gbc.gridx = 1; gbc.weightx = 0.62;
            p.add((Component) rows[i][1], gbc);
        }

        JLabel hint = new JLabel("  * Ngày nhập định dạng: dd/MM/yyyy");
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hint.setForeground(new Color(120, 120, 140));
        gbc.gridx = 0; gbc.gridy = rows.length;
        gbc.gridwidth = 2; gbc.weightx = 1;
        p.add(hint, gbc);

        lblError = new JLabel("");
        lblError.setForeground(new Color(220, 53, 69));
        lblError.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridy = rows.length + 1;
        p.add(lblError, gbc);

        return p;
    }

    private JPanel buildFooter() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)));

        btnHuy = new JButton("Hủy");
        btnHuy.setPreferredSize(new Dimension(110, 34));
        btnHuy.setBackground(new Color(108, 117, 125));
        btnHuy.setForeground(Color.WHITE);
        btnHuy.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnHuy.setFocusPainted(false);

        btnLuu = new JButton("Lưu");
        btnLuu.setPreferredSize(new Dimension(110, 34));
        btnLuu.setBackground(new Color(40, 167, 69));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLuu.setFocusPainted(false);

        p.add(btnHuy);
        p.add(btnLuu);

        btnHuy.addActionListener(e -> dispose());
        btnLuu.addActionListener(e -> handleLuu());
        return p;
    }

    private void handleLuu() {
        lblError.setText("");

        String ma  = txtMa.getText().trim();
        String ten = txtTen.getText().trim();
        String dtStr = txtDonToiThieu.getText().trim();
        String ptStr = txtPhanTram.getText().trim();
        String bdStr = txtNgayBD.getText().trim();
        String ktStr = txtNgayKT.getText().trim();

        if (ma.isEmpty())   { lblError.setText("Mã khuyến mãi không được để trống!"); return; }
        if (ten.isEmpty())  { lblError.setText("Tên khuyến mãi không được để trống!"); return; }
        if (dtStr.isEmpty()){ lblError.setText("Đơn hàng tối thiểu không được để trống!"); return; }
        if (ptStr.isEmpty()){ lblError.setText("% Giảm không được để trống!"); return; }
        if (bdStr.isEmpty()){ lblError.setText("Ngày bắt đầu không được để trống!"); return; }
        if (ktStr.isEmpty()){ lblError.setText("Ngày kết thúc không được để trống!"); return; }

        double donToiThieu, phanTram;
        try {
            donToiThieu = Double.parseDouble(dtStr.replace(",", "."));
            if (donToiThieu < 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            lblError.setText("Đơn hàng tối thiểu phải là số không âm!"); return;
        }

        try {
            phanTram = Double.parseDouble(ptStr.replace(",", "."));
            if (phanTram < 0 || phanTram > 100) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            lblError.setText("% Giảm phải là số từ 0 đến 100!"); return;
        }

        LocalDate ngayBD, ngayKT;
        try {
            ngayBD = LocalDate.parse(bdStr, FMT);
        } catch (DateTimeParseException ex) {
            lblError.setText("Ngày bắt đầu không đúng định dạng dd/MM/yyyy!"); return;
        }
        try {
            ngayKT = LocalDate.parse(ktStr, FMT);
        } catch (DateTimeParseException ex) {
            lblError.setText("Ngày kết thúc không đúng định dạng dd/MM/yyyy!"); return;
        }

        if (!ngayKT.isAfter(ngayBD)) {
            lblError.setText("Ngày kết thúc phải sau ngày bắt đầu!"); return;
        }

        String tt = (String) cboTrangThai.getSelectedItem();
        KhuyenMai km = new KhuyenMai(ma, ten, donToiThieu, phanTram, ngayBD, ngayKT, tt);

        boolean ok = editKM == null ? kmBUS.insert(km) : kmBUS.update(km);
        if (ok) {
            JOptionPane.showMessageDialog(this,
                editKM == null ? "Thêm khuyến mãi thành công!" : "Cập nhật thành công!");
            saved = true;
            dispose();
        } else {
            lblError.setText(editKM == null
                ? "Thêm thất bại! Mã có thể đã tồn tại."
                : "Cập nhật thất bại!");
        }
    }

    private void populateFields(KhuyenMai km) {
        txtMa.setText(km.getMaKhuyenMai());
        txtMa.setEditable(false);
        txtTen.setText(km.getTenKhuyenMai());
        txtDonToiThieu.setText(String.valueOf(km.getDonHangToiThieu()));
        txtPhanTram.setText(String.valueOf(km.getPhanTramGiam()));
        txtNgayBD.setText(km.getNgayBatDau() != null ? km.getNgayBatDau().format(FMT) : "");
        txtNgayKT.setText(km.getNgayKetThuc() != null ? km.getNgayKetThuc().format(FMT) : "");
        cboTrangThai.setSelectedItem(km.getTrangThai());
    }

    public boolean isSaved() { return saved; }
}