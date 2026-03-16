package gui.dialog;

import bus.NhanVienBUS;
import dto.NhanVien;

import javax.swing.*;
import java.awt.*;

public class NhanVienDialog extends JDialog {

    private final NhanVienBUS nvBUS = new NhanVienBUS();

    private JTextField txtMa, txtHo, txtTen, txtSDT, txtDiaChi, txtEmail, txtMaTaiKhoan;
    private JComboBox<String> cboTrangThai;
    private JLabel lblError;
    private JButton btnLuu, btnHuy;

    private NhanVien editNV;
    private boolean saved    = false;
    private boolean viewOnly = false;

    public NhanVienDialog(JFrame parent, NhanVien nv) {
        super(parent, nv == null ? "Thêm Nhân Viên" : "Sửa Nhân Viên", true);
        this.editNV = nv;
        setSize(480, 500);
        setLocationRelativeTo(parent);
        setResizable(false);
        init();
        if (nv != null) {
            populateFields(nv);
        } else {
            txtMa.setText(nvBUS.generateMaNhanVien());
            txtMa.setEditable(false);
        }
        if (nv != null) populateFields(nv);
    }

    public void setViewOnly(boolean viewOnly) {
        this.viewOnly = viewOnly;
        setTitle("Chi tiết Nhân Viên");
        txtMa.setEditable(false);
        txtHo.setEditable(false);
        txtTen.setEditable(false);
        txtSDT.setEditable(false);
        txtDiaChi.setEditable(false);
        txtEmail.setEditable(false);
        txtMaTaiKhoan.setEditable(false);
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

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        txtMa         = new JTextField();
        txtHo         = new JTextField();
        txtTen        = new JTextField();
        txtSDT        = new JTextField();
        txtDiaChi     = new JTextField();
        txtEmail      = new JTextField();
        txtMaTaiKhoan = new JTextField();
        cboTrangThai  = new JComboBox<>(new String[]{"HOAT_DONG", "NGUNG"});

        Object[][] rows = {
            {"Mã nhân viên *", txtMa},
            {"Họ *",           txtHo},
            {"Tên *",          txtTen},
            {"Số điện thoại *",txtSDT},
            {"Địa chỉ",        txtDiaChi},
            {"Email",          txtEmail},
            {"Mã tài khoản",   txtMaTaiKhoan},
            {"Trạng thái",     cboTrangThai},
        };

        for (int i = 0; i < rows.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0.35;
            JLabel lbl = new JLabel((String) rows[i][0]);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            p.add(lbl, gbc);

            gbc.gridx = 1; gbc.weightx = 0.65;
            p.add((Component) rows[i][1], gbc);
        }

        lblError = new JLabel("");
        lblError.setForeground(new Color(220, 53, 69));
        lblError.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 0; gbc.gridy = rows.length;
        gbc.gridwidth = 2;
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
        String ho  = txtHo.getText().trim();
        String ten = txtTen.getText().trim();
        String sdt = txtSDT.getText().trim();

        if (ma.isEmpty())  { lblError.setText("Mã nhân viên không được để trống!"); return; }
        if (ho.isEmpty())  { lblError.setText("Họ không được để trống!"); return; }
        if (ten.isEmpty()) { lblError.setText("Tên không được để trống!"); return; }
        if (sdt.isEmpty()) { lblError.setText("Số điện thoại không được để trống!"); return; }

        String diaChi    = txtDiaChi.getText().trim();
        String email     = txtEmail.getText().trim();
        String maTK      = txtMaTaiKhoan.getText().trim();
        String tt        = (String) cboTrangThai.getSelectedItem();

        NhanVien nv = new NhanVien(ma, ho, ten, sdt, diaChi, email,
                maTK.isEmpty() ? null : maTK, tt);

        boolean ok = editNV == null ? nvBUS.insert(nv) : nvBUS.update(nv);
        if (ok) {
            JOptionPane.showMessageDialog(this,
                editNV == null ? "Thêm nhân viên thành công!" : "Cập nhật thành công!");
            saved = true;
            dispose();
        } else {
            lblError.setText(editNV == null
                ? "Thêm thất bại! Mã có thể đã tồn tại."
                : "Cập nhật thất bại!");
        }
    }

    private void populateFields(NhanVien n) {
        txtMa.setText(n.getMaNhanVien());
        txtMa.setEditable(false);
        txtHo.setText(n.getHo());
        txtTen.setText(n.getTen());
        txtSDT.setText(n.getSoDienThoai());
        txtDiaChi.setText(n.getDiaChi() != null ? n.getDiaChi() : "");
        txtEmail.setText(n.getEmail() != null ? n.getEmail() : "");
        txtMaTaiKhoan.setText(n.getMaTaiKhoan() != null ? n.getMaTaiKhoan() : "");
        cboTrangThai.setSelectedItem(n.getTrangThai());
    }

    public boolean isSaved() { return saved; }
}