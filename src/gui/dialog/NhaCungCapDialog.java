package gui.dialog;

import bus.NhaCungCapBUS;
import dto.NhaCungCap;

import javax.swing.*;
import java.awt.*;

public class NhaCungCapDialog extends JDialog {

    private final NhaCungCapBUS nccBUS = new NhaCungCapBUS();

    private JTextField txtMa, txtTen, txtSDT, txtDiaChi, txtEmail;
    private JComboBox<String> cboTrangThai;
    private JLabel lblError;
    private JButton btnLuu, btnHuy;

    private NhaCungCap editNCC;
    private boolean saved    = false;
    private boolean viewOnly = false;

    public NhaCungCapDialog(JFrame parent, NhaCungCap ncc) {
        super(parent, ncc == null ? "Thêm Nhà Cung Cấp" : "Sửa Nhà Cung Cấp", true);
        this.editNCC = ncc;
        setSize(480, 380);
        setLocationRelativeTo(parent);
        setResizable(false);
        init();
        if (ncc != null) {
            populateFields(ncc);
        } else {
            txtMa.setText(nccBUS.generateMaNCC());
            txtMa.setEditable(false);
        }
    }

    public void setViewOnly(boolean viewOnly) {
        this.viewOnly = viewOnly;
        setTitle("Chi tiết Nhà Cung Cấp");
        txtMa.setEditable(false);
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
        txtTen    = new JTextField();
        txtSDT    = new JTextField();
        txtDiaChi = new JTextField();
        txtEmail  = new JTextField();
        cboTrangThai = new JComboBox<>(new String[]{"HOAT_DONG", "NGUNG"});

        Object[][] rows = {
            {"Mã NCC *",          txtMa},
            {"Tên NCC *",         txtTen},
            {"Số điện thoại *",   txtSDT},
            {"Địa chỉ",           txtDiaChi},
            {"Email",             txtEmail},
            {"Trạng thái",        cboTrangThai},
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
        String ten = txtTen.getText().trim();
        String sdt = txtSDT.getText().trim();

        if (ma.isEmpty())  { lblError.setText("Mã NCC không được để trống!"); return; }
        if (ten.isEmpty()) { lblError.setText("Tên NCC không được để trống!"); return; }
        if (sdt.isEmpty()) { lblError.setText("Số điện thoại không được để trống!"); return; }

        String diaChi = txtDiaChi.getText().trim();
        String email  = txtEmail.getText().trim();
        String tt     = (String) cboTrangThai.getSelectedItem();

        NhaCungCap ncc = new NhaCungCap(ma, ten, sdt, diaChi, email, tt);

        boolean ok = editNCC == null ? nccBUS.insert(ncc) : nccBUS.update(ncc);
        if (ok) {
            JOptionPane.showMessageDialog(this,
                editNCC == null ? "Thêm nhà cung cấp thành công!" : "Cập nhật thành công!");
            saved = true;
            dispose();
        } else {
            lblError.setText(editNCC == null
                ? "Thêm thất bại! Mã có thể đã tồn tại."
                : "Cập nhật thất bại!");
        }
    }

    private void populateFields(NhaCungCap ncc) {
        txtMa.setText(ncc.getMaNCC());
        txtMa.setEditable(false);
        txtTen.setText(ncc.getTenNCC());
        txtSDT.setText(ncc.getSoDienThoai());
        txtDiaChi.setText(ncc.getDiaChi());
        txtEmail.setText(ncc.getEmail());
        cboTrangThai.setSelectedItem(ncc.getTrangThai());
    }

    public boolean isSaved() { return saved; }
}
