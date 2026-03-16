package gui.dialog;

import bus.NhomQuyenBUS;
import dto.NhomQuyen;

import javax.swing.*;
import java.awt.*;

public class NhomQuyenDialog extends JDialog {

    private final NhomQuyenBUS nqBUS = new NhomQuyenBUS();

    private JTextField txtMa, txtTen;
    private JLabel lblError;
    private JButton btnLuu, btnHuy;

    private NhomQuyen editNQ;
    private boolean saved    = false;
    private boolean viewOnly = false;

    public NhomQuyenDialog(JFrame parent, NhomQuyen nq) {
        super(parent, nq == null ? "Thêm Nhóm Quyền" : "Sửa Nhóm Quyền", true);
        this.editNQ = nq;
        setSize(400, 260);
        setLocationRelativeTo(parent);
        setResizable(false);
        init();
        if (nq != null) {
            populateFields(nq);
        } else {
            txtMa.setText(nqBUS.generateMaNhomQuyen());
            txtMa.setEditable(false);
        }
        if (nq != null) populateFields(nq);
    }

    public void setViewOnly(boolean viewOnly) {
        this.viewOnly = viewOnly;
        setTitle("Chi tiết Nhóm Quyền");
        txtMa.setEditable(false);
        txtTen.setEditable(false);
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
        p.setBorder(BorderFactory.createEmptyBorder(20, 24, 8, 24));
        p.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(8, 6, 8, 6);
        gbc.fill    = GridBagConstraints.HORIZONTAL;

        txtMa  = new JTextField();
        txtTen = new JTextField();

        Object[][] rows = {
            {"Mã nhóm quyền *", txtMa},
            {"Tên nhóm quyền *", txtTen},
        };

        for (int i = 0; i < rows.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0.4;
            JLabel lbl = new JLabel((String) rows[i][0]);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            p.add(lbl, gbc);

            gbc.gridx = 1; gbc.weightx = 0.6;
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

        if (ma.isEmpty())  { lblError.setText("Mã nhóm quyền không được để trống!"); return; }
        if (ten.isEmpty()) { lblError.setText("Tên nhóm quyền không được để trống!"); return; }

        NhomQuyen nq = new NhomQuyen(ma, ten);

        boolean ok = editNQ == null ? nqBUS.insert(nq) : nqBUS.update(nq);
        if (ok) {
            JOptionPane.showMessageDialog(this,
                editNQ == null ? "Thêm nhóm quyền thành công!" : "Cập nhật thành công!");
            saved = true;
            dispose();
        } else {
            lblError.setText(editNQ == null
                ? "Thêm thất bại! Mã đã tồn tại."
                : "Cập nhật thất bại!");
        }
    }

    private void populateFields(NhomQuyen nq) {
        txtMa.setText(nq.getMaNhomQuyen());
        txtMa.setEditable(false);
        txtTen.setText(nq.getTenNhomQuyen());
    }

    public boolean isSaved() { return saved; }
}