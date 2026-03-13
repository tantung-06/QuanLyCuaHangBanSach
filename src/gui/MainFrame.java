package gui;

import gui.panel.KhachHangPanel;
import gui.panel.KhuyenMaiPanel;
import gui.panel.NhanVienPanel;
import gui.panel.NhomQuyenPanel;
import gui.panel.PhieuNhapPanel;
import gui.panel.PhieuXuatPanel;
import gui.panel.SachPanel;
import gui.panel.TaiKhoanPanel;
import gui.panel.ThongKePanel;
import gui.panel.TrangChuPanel;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JPanel sidebar;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    private JLabel lblUserName;
//    private JLabel lblUserRole;

    public MainFrame() {
        setTitle("Hệ thống quản lý cửa hàng bán sách");
        setSize(1024, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        add(buildSidebar(), BorderLayout.WEST);
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.add(new TrangChuPanel(), "TRANGCHU");
        contentPanel.add(new SachPanel(), "SACH");
        contentPanel.add(new PhieuNhapPanel(), "PHIEUNHAP");
        contentPanel.add(new PhieuXuatPanel(), "PHIEUXUAT");
        contentPanel.add(new KhachHangPanel(), "KHACHHANG");
        contentPanel.add(new NhanVienPanel(), "NHANVIEN");
        contentPanel.add(new TaiKhoanPanel(), "TAIKHOAN");
        contentPanel.add(new NhomQuyenPanel(), "PHANQUYEN");
        contentPanel.add(new KhuyenMaiPanel(), "KHUYENMAI");
        contentPanel.add(new ThongKePanel(), "THONGKE");
        add(contentPanel, BorderLayout.CENTER);
        cardLayout.show(contentPanel, "TRANGCHU");
    }

    private JPanel buildSidebar() {
        sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(170, 600));
        sidebar.setBackground(Color.WHITE);
        sidebar.setLayout(new BorderLayout());

        // User info
        JPanel userPanel = new JPanel();
        userPanel.setBackground(Color.WHITE);
        userPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 15));
        userPanel.setPreferredSize(new Dimension(170, 70));

        JLabel avatar = new JLabel("👤");
        avatar.setFont(new Font("Segoe UI", Font.PLAIN, 28));

        JPanel userInfo = new JPanel(new GridLayout(2, 1));
        userInfo.setBackground(Color.WHITE);
        lblUserName = new JLabel("Admin");
        lblUserName.setForeground(Color.BLACK);
        lblUserName.setFont(new Font("Segoe UI", Font.BOLD, 13));
//        lblUserRole = new JLabel("Quản trị viên");
//        lblUserRole.setForeground(new Color(150, 150, 170));
//        lblUserRole.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        userInfo.add(lblUserName);
//        userInfo.add(lblUserRole);

        userPanel.add(avatar);
        userPanel.add(userInfo);

        // Menu items
        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        String[][] menus = {
            {"🏠", "Trang chủ",  "TRANGCHU"},
            {"📚", "Sách",       "SACH"},
            {"📥", "Phiếu nhập", "PHIEUNHAP"},
            {"📤", "Phiếu xuất", "PHIEUXUAT"},
            {"👥", "Khách hàng", "KHACHHANG"},
            {"👤", "Nhân viên",  "NHANVIEN"},
            {"🔑", "Tài khoản",  "TAIKHOAN"},
            {"🛡", "Phân quyền", "PHANQUYEN"},
            {"🎁", "Khuyến mãi", "KHUYENMAI"},
            {"📊", "Thống kê",   "THONGKE"},
        };

        ButtonGroup bg = new ButtonGroup();
        for (String[] m : menus) {
            JToggleButton btn = createMenuButton(m[0], m[1], m[2]);
            bg.add(btn);
            menuPanel.add(btn);
        }

        // Logout
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 8));
        bottomPanel.setBackground(Color.WHITE);
        JButton btnLogout = new JButton("  🚪  Đăng xuất");
        btnLogout.setForeground(Color.BLACK);
        btnLogout.setBackground(Color.WHITE);
        btnLogout.setBorderPainted(false);
        btnLogout.setFocusPainted(false);
        btnLogout.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogout.setPreferredSize(new Dimension(170, 38));
        btnLogout.addActionListener(e -> {
            dispose();
            new Login().setVisible(true);
        });
        bottomPanel.add(btnLogout);

        sidebar.add(userPanel,  BorderLayout.NORTH);
        sidebar.add(menuPanel,  BorderLayout.CENTER);
        sidebar.add(bottomPanel, BorderLayout.SOUTH);
        return sidebar;
    }

    private JToggleButton createMenuButton(String icon, String label, String card) {
        JToggleButton btn = new JToggleButton(icon + "  " + label);
        btn.setMaximumSize(new Dimension(170, 40));
        btn.setPreferredSize(new Dimension(170, 40));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setForeground(Color.BLACK);
        btn.setBackground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            btn.setBackground(new Color(230, 240, 255));
            btn.setForeground(Color.BLACK);
            cardLayout.show(contentPanel, card);
        });

        btn.addChangeListener(e -> {
            if (!btn.isSelected()) {
                btn.setBackground(Color.WHITE);
                btn.setForeground(Color.BLACK);
            }
        });
        return btn;
    }
    
    public void setUserInfo(String name) {
        lblUserName.setText(name);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}