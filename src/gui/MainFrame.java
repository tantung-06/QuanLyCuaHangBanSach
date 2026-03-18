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
    private JLabel lblUserRole;

    // Thêm field để lưu vai trò
    private String userRole;

    public MainFrame() {
        setTitle("Hệ thống quản lý cửa hàng bán sách");
        setSize(1920, 1080);
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
        sidebar.setPreferredSize(new Dimension(240, 1080));
        sidebar.setBackground(Color.WHITE);
        sidebar.setLayout(new BorderLayout());
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(220, 220, 220)));

        // ---- User info ----
        JPanel userPanel = new JPanel();
        userPanel.setBackground(Color.WHITE);
        userPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 15));
        userPanel.setPreferredSize(new Dimension(240, 80));
        userPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(220, 220, 220)));

        JLabel avatar = new JLabel("👤");
        avatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        avatar.setPreferredSize(new Dimension(40, 40));
        avatar.setVerticalAlignment(SwingConstants.CENTER);

        JPanel userInfo = new JPanel(new GridLayout(2, 1));
        userInfo.setBackground(Color.WHITE);
        lblUserName = new JLabel("Admin");
        lblUserName.setForeground(Color.BLACK);
        lblUserName.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblUserRole = new JLabel("Quản trị viên");
        lblUserRole.setForeground(new Color(150, 150, 170));
        lblUserRole.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userInfo.add(lblUserName);
        userInfo.add(lblUserRole);

        userPanel.add(avatar);
        userPanel.add(userInfo);

        // ---- Menu ----
        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        String[][] menus = {
                { "🏠", "Trang chủ", "TRANGCHU" },
                { "📚", "Sách", "SACH" },
                { "📥", "Phiếu nhập", "PHIEUNHAP" },
                { "📤", "Phiếu xuất", "PHIEUXUAT" },
                { "👥", "Khách hàng", "KHACHHANG" },
                { "👤", "Nhân viên", "NHANVIEN" },
                { "💳", "Tài khoản", "TAIKHOAN" },
                { "🔑", "Phân quyền", "PHANQUYEN" },
                { "🎁", "Khuyến mãi", "KHUYENMAI" },
                { "📊", "Thống kê", "THONGKE" },
        };

        ButtonGroup bg = new ButtonGroup();
        for (String[] m : menus) {
            JToggleButton btn = createMenuButton(m[0], m[1], m[2]);
            bg.add(btn);
            menuPanel.add(btn);
        }

        // ---- Logout ----
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 8));
        bottomPanel.setBackground(Color.WHITE);

        JButton btnLogout = new JButton();
        btnLogout.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btnLogout.setBackground(Color.WHITE);
        btnLogout.setBorderPainted(false);
        btnLogout.setFocusPainted(false);
        btnLogout.setPreferredSize(new Dimension(240, 50));
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel icLogout = new JLabel("🚪");
        icLogout.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        icLogout.setPreferredSize(new Dimension(26, 50));
        icLogout.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel txLogout = new JLabel("Đăng xuất");
        txLogout.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txLogout.setForeground(Color.BLACK);

        btnLogout.add(icLogout);
        btnLogout.add(txLogout);
        btnLogout.addActionListener(e -> {
            dispose();
            new Login().setVisible(true);
        });
        bottomPanel.add(btnLogout);

        sidebar.add(userPanel, BorderLayout.NORTH);
        sidebar.add(menuPanel, BorderLayout.CENTER);
        sidebar.add(bottomPanel, BorderLayout.SOUTH);
        return sidebar;
    }

    private JToggleButton createMenuButton(String emoji, String label, String card) {
        JToggleButton btn = new JToggleButton();
        btn.setLayout(new FlowLayout(FlowLayout.LEFT, 8, -5));
        btn.setMaximumSize(new Dimension(240, 50));
        btn.setPreferredSize(new Dimension(240, 50));
        btn.setBackground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.setContentAreaFilled(false);
        btn.setOpaque(true);

        JLabel lblIcon = new JLabel(emoji);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        lblIcon.setPreferredSize(new Dimension(26, 50));
        lblIcon.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblText = new JLabel(label);
        lblText.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblText.setForeground(Color.BLACK);

        btn.add(lblIcon);
        btn.add(lblText);

        btn.addActionListener(e -> {
            // Kiểm tra quyền trước khi chuyển panel
            if ("PHIEUNHAP".equals(card) && "Nhân viên bán hàng".equals(userRole)) {
                JOptionPane.showMessageDialog(this, "Bạn không có quyền truy cập phiếu nhập!", "Không có quyền",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            if ("PHIEUXUAT".equals(card) && "Nhân viên kho".equals(userRole)) {
                JOptionPane.showMessageDialog(this, "Bạn không có quyền truy cập phiếu xuất!", "Không có quyền",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            cardLayout.show(contentPanel, card);
        });

        btn.addChangeListener(e -> {
            if (btn.isSelected()) {
                btn.setBackground(new Color(204, 255, 255));
                lblText.setForeground(Color.BLACK);
            } else {
                btn.setBackground(Color.WHITE);
                lblText.setForeground(Color.BLACK);
            }
        });

        return btn;
    }

    public void setUserInfo(String name, String role) {
        lblUserName.setText(name);
        lblUserRole.setText(role == null || role.trim().isEmpty() ? "" : role);
        this.userRole = role;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}