package gui.dialog;

import bus.KhachHangBUS;
import dto.KhachHang;

import javax.swing.*;
import java.awt.*;

public class KhachHangDialog extends JDialog {

    private final KhachHangBUS khBUS = new KhachHangBUS();

    private JTextField txtMa, txtHo, txtTen, txtSDT, txtDiaChi, txtEmail;
    private JComboBox<String> cboTrangThai;
    private JLabel lblError;
    private JButton btnLuu, btnHuy;

    private KhachHang editKH;
    private boolean saved    = false;
    private boolean viewOnly = false;

    public KhachHangDialog(JFrame parent, KhachHang kh) {
        super(parent, kh == null ? "Thêm Khách Hàng" : "Sửa Khách Hàng", true);
        this.editKH = kh;
        setSize(480, 440);
        setLocationRelativeTo(parent);
        setResizable(false);
        init();
        if (kh != null) {
            populateFields(kh);
        } else {
            txtMa.setText(khBUS.generateMaKhachHang());
            txtMa.setEditable(false);
        }
        if (kh != null) populateFields(kh);
    }

    public void setViewOnly(boolean viewOnly) {
        this.viewOnly = viewOnly;
        setTitle("Chi tiết Khách Hàng");
        txtMa.setEditable(false);
        txtHo.setEditable(false);
        txtTen.setEditable(false);
        txtSDT.setEditable(false);
        txtDiaChi.setEditable(false);
        txtEmail.setEditable(false);
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

        txtMa     = new JTextField();
        txtHo     = new JTextField();
        txtTen    = new JTextField();
        txtSDT    = new JTextField();
        txtDiaChi = new JTextField();
        txtEmail  = new JTextField();
        cboTrangThai = new JComboBox<>(new String[]{"HOAT_DONG", "NGUNG"});

        Object[][] rows = {
            {"Mã khách hàng *", txtMa},
            {"Họ *",            txtHo},
            {"Tên *",           txtTen},
            {"Số điện thoại",   txtSDT},
            {"Địa chỉ",         txtDiaChi},
            {"Email",           txtEmail},
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

        if (ma.isEmpty())  { lblError.setText("Mã khách hàng không được để trống!"); return; }
        if (ho.isEmpty())  { lblError.setText("Họ không được để trống!"); return; }
        if (ten.isEmpty()) { lblError.setText("Tên không được để trống!"); return; }

        String sdt    = txtSDT.getText().trim();
        String diaChi = txtDiaChi.getText().trim();
        String email  = txtEmail.getText().trim();
        String tt     = (String) cboTrangThai.getSelectedItem();

        KhachHang kh = new KhachHang(ma, ho, ten, sdt, diaChi, email, tt);

        boolean ok = editKH == null ? khBUS.insert(kh) : khBUS.update(kh);
        if (ok) {
            JOptionPane.showMessageDialog(this,
                editKH == null ? "Thêm khách hàng thành công!" : "Cập nhật thành công!");
            saved = true;
            dispose();
        } else {
            lblError.setText(editKH == null
                ? "Thêm thất bại! Mã có thể đã tồn tại."
                : "Cập nhật thất bại!");
        }
    }

    private void populateFields(KhachHang k) {
        txtMa.setText(k.getMaKhachHang());
        txtMa.setEditable(false);
        txtHo.setText(k.getHo());
        txtTen.setText(k.getTen());
        txtSDT.setText(k.getSoDienThoai());
        txtDiaChi.setText(k.getDiaChi());
        txtEmail.setText(k.getEmail());
        cboTrangThai.setSelectedItem(k.getTrangThai());
    }

    public boolean isSaved() { return saved; }
}