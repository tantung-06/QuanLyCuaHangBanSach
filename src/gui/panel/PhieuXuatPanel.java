package gui.panel;

import gui.dialog.PhieuXuatDialog;
import bus.PhieuXuatBUS;
import bus.KhachHangBUS;
import bus.NhanVienBUS;
import dto.PhieuXuat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PhieuXuatPanel extends JPanel {

    private final PhieuXuatBUS phieuXuatBUS = new PhieuXuatBUS();
    private final KhachHangBUS khBUS        = new KhachHangBUS();
    private final NhanVienBUS  nvBUS        = new NhanVienBUS();

    private JButton btnThem, btnChiTiet, btnHuy;
    private JComboBox<String> cboFilter;
    private JTextField txtSearch;

    private JTable table;
    private DefaultTableModel tableModel;

    private ArrayList<PhieuXuat> danhSach = new ArrayList<>();
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final Color BG = new Color(235, 252, 255);

    public PhieuXuatPanel() {
        setLayout(new BorderLayout(0, 0));
        setBackground(BG);
        initPadding();
        initContent();
        loadData();
    }

    // ==================== PADDING ====================

    private void initPadding() {
        addPad(BorderLayout.NORTH,  0, 10);
        addPad(BorderLayout.SOUTH,  0, 10);
        addPad(BorderLayout.EAST,  10,  0);
        addPad(BorderLayout.WEST,  10,  0);
    }

    private void addPad(String pos, int w, int h) {
        JPanel p = new JPanel();
        p.setBackground(BG);
        p.setPreferredSize(new Dimension(w, h));
        add(p, pos);
    }

    // ==================== CONTENT ====================

    private void initContent() {
        JPanel contentCenter = new JPanel(new BorderLayout(10, 10));
        contentCenter.setBackground(BG);
        add(contentCenter, BorderLayout.CENTER);
        contentCenter.add(buildFunctionBar(), BorderLayout.NORTH);
        contentCenter.add(buildTablePanel(),  BorderLayout.CENTER);
    }

    // ==================== FUNCTION BAR ====================

    private JPanel buildFunctionBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Color.WHITE);
        bar.setPreferredSize(new Dimension(0, 100));
        bar.setBorder(new EmptyBorder(10, 10, 10, 10));

        // --- Trái: nút chức năng ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        btnPanel.setBackground(Color.WHITE);

        btnThem    = makeBtn("ic_them.png",    "Thêm",      e -> openDialog(null));
        btnChiTiet = makeBtn("ic_chitiet.png", "Chi tiết",  e -> handleChiTiet());
        btnHuy     = makeBtn("ic_huy.png",     "Hủy phiếu", e -> handleHuy());

        btnPanel.add(btnThem);
        btnPanel.add(btnChiTiet);
        btnPanel.add(btnHuy);

        // --- Phải: lọc + tìm kiếm, căn giữa dọc ---
        JPanel searchWrapper = new JPanel(new GridBagLayout());
        searchWrapper.setBackground(Color.WHITE);

        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchRow.setBackground(Color.WHITE);

        cboFilter = new JComboBox<>(new String[]{"Tất cả", "DA_XUAT", "HUY"});
        cboFilter.setPreferredSize(new Dimension(130, 34));
        cboFilter.addActionListener(e -> applyFilter());

        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(200, 34));
        txtSearch.setToolTipText("Tìm kiếm mã phiếu, nhân viên, khách hàng...");
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 220), 1, true),
                BorderFactory.createEmptyBorder(2, 8, 2, 8)));
        txtSearch.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { applyFilter(); }
        });

        JButton btnLamMoi = new JButton("Làm mới");
        btnLamMoi.setPreferredSize(new Dimension(90, 34));
        btnLamMoi.setFocusPainted(false);
        btnLamMoi.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLamMoi.addActionListener(e -> {
            txtSearch.setText("");
            cboFilter.setSelectedIndex(0);
            loadData();
        });

        searchRow.add(new JLabel("Lọc:"));
        searchRow.add(cboFilter);
        searchRow.add(txtSearch);
        searchRow.add(btnLamMoi);

        searchWrapper.add(searchRow, new GridBagConstraints());

        bar.add(btnPanel,      BorderLayout.WEST);
        bar.add(searchWrapper, BorderLayout.EAST);
        return bar;
    }

    // ==================== MAKE BUTTON ====================

    private JButton makeBtn(String iconFile, String text,
                             ActionListener action) {
        JButton btn = new JButton();
        btn.setLayout(new BorderLayout());
        btn.setPreferredSize(new Dimension(115, 80));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(action);

        JLabel lblIcon = new JLabel("", SwingConstants.CENTER);
        try {
            java.net.URL url = getClass().getResource("/icon/" + iconFile);
            if (url != null) {
                lblIcon.setIcon(new ImageIcon(
                    new ImageIcon(url).getImage()
                        .getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
            }
        } catch (Exception ignored) {}

        JLabel lblText = new JLabel(text, SwingConstants.CENTER);
        lblText.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblText.setForeground(new Color(50, 50, 50));

        JPanel wrap = new JPanel();
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.setOpaque(false);
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblText.setAlignmentX(Component.CENTER_ALIGNMENT);
        wrap.add(lblIcon);
        wrap.add(Box.createVerticalStrut(4));
        wrap.add(lblText);

        btn.add(wrap, BorderLayout.CENTER);
        return btn;
    }

    // ==================== TABLE ====================

    private JPanel buildTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(220, 230, 240)));

        String[] cols = {"Mã phiếu xuất", "Ngày lập", "Nhân viên",
                         "Khách hàng", "Khuyến mãi", "Tổng tiền", "Trạng thái"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        table.setRowHeight(32);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(230, 235, 240));
        table.setSelectionBackground(new Color(210, 235, 255));
        table.setSelectionForeground(Color.BLACK);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        table.setFocusable(false);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(234, 234, 234));
        header.setForeground(new Color(50, 50, 50));
        header.setPreferredSize(new Dimension(0, 36));

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setHorizontalAlignment(col == 2 ? SwingConstants.LEFT : SwingConstants.CENTER);
                setBorder(new EmptyBorder(0, 8, 0, 8));
                if (!sel) setBackground(Color.WHITE);
                if (col == 6 && v != null) {
                    switch (v.toString()) {
                        case "DA_XUAT":  setForeground(new Color(34, 139, 34)); break;
                        case "HUY":       setForeground(new Color(200, 50, 50));  break;
                        default:          setForeground(Color.BLACK);
                    }
                } else {
                    setForeground(Color.BLACK);
                }
                return this;
            }
        });

        // Double click → chi tiết
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) handleChiTiet();
            }
        });

        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);
        table.setFillsViewportHeight(true);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ==================== LOAD ====================

    public void loadData() {
        try {
            danhSach = phieuXuatBUS.getAll();
            renderTable(danhSach);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void renderTable(ArrayList<PhieuXuat> ds) {
        tableModel.setRowCount(0);
        for (PhieuXuat px : ds) {
            tableModel.addRow(new Object[]{
                px.getMaPhieuXuat(),
                px.getNgayLap() != null ? px.getNgayLap().format(FMT) : "",
                px.getMaNhanVien(),
                px.getMaKhachHang() != null ? px.getMaKhachHang() : "--",
                px.getMaKhuyenMai() != null ? px.getMaKhuyenMai() : "--",
                String.format("%,.0f ₫", px.getTongTien()),
                px.getTrangThai()
            });
        }
    }

    // ==================== FILTER ====================

    private void applyFilter() {
        String kw = txtSearch.getText().trim().toLowerCase();
        String tt = (String) cboFilter.getSelectedItem();

        ArrayList<PhieuXuat> filtered = new ArrayList<>();
        for (PhieuXuat px : danhSach) {
            if (!kw.isEmpty()
                && !px.getMaPhieuXuat().toLowerCase().contains(kw)
                && !px.getMaNhanVien().toLowerCase().contains(kw)
                && (px.getMaKhachHang() == null
                    || !px.getMaKhachHang().toLowerCase().contains(kw))) continue;

            if (tt != null && !"Tất cả".equals(tt)
                && !tt.equals(px.getTrangThai())) continue;

            filtered.add(px);
        }
        renderTable(filtered);
    }

    // ==================== ACTIONS ====================

    private PhieuXuat getSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phiếu xuất!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return phieuXuatBUS.getById((String) tableModel.getValueAt(row, 0));
    }

    private void handleChiTiet() {
        PhieuXuat px = getSelected();
        if (px == null) return;
        PhieuXuatDialog dialog = new PhieuXuatDialog(
                (JFrame) SwingUtilities.getWindowAncestor(this), px);
        dialog.setViewOnly(true);
        dialog.setVisible(true);
    }

    private void handleHuy() {
        PhieuXuat px = getSelected();
        if (px == null) return;

        if ("HUY".equals(px.getTrangThai())) {
            JOptionPane.showMessageDialog(this, "Phiếu này đã bị hủy rồi!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Xác nhận hủy phiếu xuất " + px.getMaPhieuXuat()
                + "?\n(Tồn kho sẽ được hoàn lại)",
                "Xác nhận hủy", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            phieuXuatBUS.updateTrangThai(px.getMaPhieuXuat(), "HUY");
            JOptionPane.showMessageDialog(this, "Hủy phiếu thành công!");
            loadData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openDialog(PhieuXuat px) {
        PhieuXuatDialog dialog = new PhieuXuatDialog(
                (JFrame) SwingUtilities.getWindowAncestor(this), px);
        dialog.setVisible(true);
        if (dialog.isSaved()) loadData();
    }
}