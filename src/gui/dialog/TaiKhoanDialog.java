package gui.dialog;

import bus.TaiKhoanBUS;
import bus.NhomQuyenBUS;
import dto.TaiKhoan;

import javax.swing.*;
import java.awt.*;

public class TaiKhoanDialog extends JDialog {

    private final TaiKhoanBUS  tkBUS  = new TaiKhoanBUS();
    private final NhomQuyenBUS nqBUS  = new NhomQuyenBUS();

    private JTextField     txtMa, txtTenDangNhap;
    private JPasswordField txtMatKhau, txtXacNhan;
    private JComboBox<String> cboNhomQuyen, cboTrangThai;
    private JLabel lblError;
    private JButton btnLuu, btnHuy;

    private TaiKhoan editTK;
    private boolean saved    = false;
    private boolean viewOnly = false;

    public TaiKhoanDialog(JFrame parent, TaiKhoan tk) {
        super(parent, tk == null ? "Thêm Tài Khoản" : "Sửa Tài Khoản", true);
        this.editTK = tk;
        setSize(460, 440);
        setLocationRelativeTo(parent);
        setResizable(false);
        init();
        if (tk != null) populateFields(tk);
    }

    public void setViewOnly(boolean viewOnly) {
        this.viewOnly = viewOnly;
        setTitle("Chi tiết Tài Khoản");
        txtMa.setEditable(false);
        txtTenDangNhap.setEditable(false);
        txtMatKhau.setEditable(false);
        txtXacNhan.setEditable(false);
        cboNhomQuyen.setEnabled(false);
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
        p.setBackground(new Color(240, 248, 255));
        p.setBorder(BorderFactory.createEmptyBorder(16, 20, 8, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        txtMa         = new JTextField();
        txtTenDangNhap= new JTextField();
        txtMatKhau    = new JPasswordField();
        txtXacNhan    = new JPasswordField();

        cboNhomQuyen = new JComboBox<>();
        nqBUS.getAll().forEach(nq ->
            cboNhomQuyen.addItem(nq.getMaNhomQuyen() + " - " + nq.getTenNhomQuyen()));

        cboTrangThai = new JComboBox<>(new String[]{"HOAT_DONG", "KHOA"});

        Object[][] rows = {
            {"Mã tài khoản *",  txtMa},
            {"Tên đăng nhập *", txtTenDangNhap},
            {"Mật khẩu *",      txtMatKhau},
            {"Xác nhận MK *",   txtXacNhan},
            {"Nhóm quyền",      cboNhomQuyen},
            {"Trạng thái",      cboTrangThai},
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

    // ==================== FOOTER ====================

    private JPanel buildFooter() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        p.setBackground(new Color(240, 248, 255));
        p.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)));

        btnHuy = new JButton("✕  Hủy");
        btnHuy.setPreferredSize(new Dimension(110, 34));
        btnHuy.setBackground(new Color(108, 117, 125));
        btnHuy.setForeground(Color.WHITE);
        btnHuy.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnHuy.setFocusPainted(false);

        btnLuu = new JButton("💾  Lưu");
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

    // ==================== ACTIONS ====================

    private void handleLuu() {
        lblError.setText("");

        String ma  = txtMa.getText().trim();
        String ten = txtTenDangNhap.getText().trim();
        String mk  = new String(txtMatKhau.getPassword()).trim();
        String xn  = new String(txtXacNhan.getPassword()).trim();

        if (ma.isEmpty())  { lblError.setText("Mã tài khoản không được để trống!"); return; }
        if (ten.isEmpty()) { lblError.setText("Tên đăng nhập không được để trống!"); return; }

        // Khi thêm mới bắt buộc nhập mật khẩu
        if (editTK == null) {
            if (mk.isEmpty()) { lblError.setText("Mật khẩu không được để trống!"); return; }
            if (!mk.equals(xn)) { lblError.setText("Mật khẩu xác nhận không khớp!"); return; }
        } else {
            // Khi sửa: nếu có nhập thì kiểm tra xác nhận
            if (!mk.isEmpty() && !mk.equals(xn)) {
                lblError.setText("Mật khẩu xác nhận không khớp!"); return;
            }
        }

        String nqItem = (String) cboNhomQuyen.getSelectedItem();
        String maNQ   = nqItem != null ? nqItem.split(" - ")[0] : null;
        String tt     = (String) cboTrangThai.getSelectedItem();

        // Khi sửa mà không đổi mật khẩu thì giữ mật khẩu cũ
        String matKhauLuu = mk.isEmpty() && editTK != null ? editTK.getMatKhau() : mk;

        TaiKhoan tk = new TaiKhoan(ma, ten, matKhauLuu, maNQ, tt);

        boolean ok = editTK == null ? tkBUS.insert(tk) : tkBUS.update(tk);
        if (ok) {
            JOptionPane.showMessageDialog(this,
                editTK == null ? "Thêm tài khoản thành công!" : "Cập nhật thành công!");
            saved = true;
            dispose();
        } else {
            lblError.setText(editTK == null
                ? "Thêm thất bại! Mã hoặc tên đăng nhập đã tồn tại."
                : "Cập nhật thất bại!");
        }
    }

    private void populateFields(TaiKhoan tk) {
        txtMa.setText(tk.getMaTaiKhoan());
        txtMa.setEditable(false);
        txtTenDangNhap.setText(tk.getTenDangNhap());
        // Không hiển thị mật khẩu thật
        txtMatKhau.setText("");
        txtXacNhan.setText("");
        cboTrangThai.setSelectedItem(tk.getTrangThai());

        if (tk.getMaNhomQuyen() != null) {
            for (int i = 0; i < cboNhomQuyen.getItemCount(); i++) {
                if (cboNhomQuyen.getItemAt(i).startsWith(tk.getMaNhomQuyen() + " - ")) {
                    cboNhomQuyen.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    public boolean isSaved() { return saved; }
}