package gui.panel;

import bus.NhaCungCapBUS;
import dto.NhaCungCap;
import gui.dialog.NhaCungCapDialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class NhaCungCapPanel extends JPanel {

    private final NhaCungCapBUS nccBUS = new NhaCungCapBUS();

    private ArrayList<NhaCungCap> danhSach = new ArrayList<>();

    private JTable table;
    private DefaultTableModel tableModel;

    private JComboBox<String> cboFilter;
    private JTextField txtSearch;

    private JButton btnThem, btnXoa, btnSua, btnChiTiet;

    private static final Color BG = new Color(235, 252, 255);

    public NhaCungCapPanel() {
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

        btnThem    = makeBtn("ic_them.png",    "Thêm",     e -> handleThem());
        btnXoa     = makeBtn("ic_xoa.png",     "Xóa",      e -> handleXoa());
        btnSua     = makeBtn("ic_sua.png",     "Sửa",      e -> handleSua());
        btnChiTiet = makeBtn("ic_chitiet.png", "Chi tiết", e -> handleChiTiet());

        btnPanel.add(btnThem);
        btnPanel.add(btnXoa);
        btnPanel.add(btnSua);
        btnPanel.add(btnChiTiet);

        // --- Phải: lọc + tìm kiếm ---
        JPanel searchWrapper = new JPanel(new GridBagLayout());
        searchWrapper.setBackground(Color.WHITE);

        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchRow.setBackground(Color.WHITE);

        cboFilter = new JComboBox<>(new String[]{"Tất cả", "HOAT_DONG", "NGUNG"});
        cboFilter.setPreferredSize(new Dimension(160, 34));
        cboFilter.addActionListener(e -> loadData());

        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(200, 34));
        txtSearch.setToolTipText("Tìm kiếm tên, số điện thoại, email...");
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 220), 1, true),
                BorderFactory.createEmptyBorder(2, 8, 2, 8)));
        txtSearch.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { handleSearch(); }
        });

        JButton btnReset = new JButton("Làm mới");
        btnReset.setPreferredSize(new Dimension(90, 34));
        btnReset.setFocusPainted(false);
        btnReset.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnReset.addActionListener(e -> {
            txtSearch.setText("");
            cboFilter.setSelectedIndex(0);
            loadData();
        });

        searchRow.add(new JLabel("Lọc:"));
        searchRow.add(cboFilter);
        searchRow.add(txtSearch);
        searchRow.add(btnReset);

        searchWrapper.add(searchRow, new GridBagConstraints());

        bar.add(btnPanel,      BorderLayout.WEST);
        bar.add(searchWrapper, BorderLayout.EAST);
        return bar;
    }

    // ==================== MAKE BUTTON ====================

    private JButton makeBtn(String iconFile, String text, ActionListener action) {
        JButton btn = new JButton();
        btn.setLayout(new BorderLayout());
        btn.setPreferredSize(new Dimension(90, 80));
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

        String[] cols = {"Mã NCC", "Tên NCC", "Số điện thoại",
                         "Địa chỉ", "Email", "Trạng thái"};
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
                setHorizontalAlignment(col == 3 ? SwingConstants.LEFT : SwingConstants.CENTER);
                setBorder(new EmptyBorder(0, 8, 0, 8));
                if (!sel) setBackground(Color.WHITE);
                if (col == 5 && v != null) {
                    setForeground("HOAT_DONG".equals(v.toString())
                            ? new Color(34, 139, 34) : new Color(200, 50, 50));
                } else {
                    setForeground(Color.BLACK);
                }
                return this;
            }
        });

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) handleChiTiet();
            }
        });

        table.getColumnModel().getColumn(3).setPreferredWidth(180);
        table.getColumnModel().getColumn(4).setPreferredWidth(160);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);
        table.setFillsViewportHeight(true);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ==================== LOAD ====================

    public void loadData() {
        danhSach = new ArrayList<>();
        String filter = (String) cboFilter.getSelectedItem();
        if ("Tất cả".equals(filter))
            danhSach = nccBUS.getAll();
        else
            danhSach = nccBUS.getByTrangThai(filter);
        renderTable(danhSach);
    }

    private void renderTable(ArrayList<NhaCungCap> ds) {
        tableModel.setRowCount(0);
        for (NhaCungCap ncc : ds) {
            tableModel.addRow(new Object[]{
                ncc.getMaNCC(), ncc.getTenNCC(), ncc.getSoDienThoai(),
                ncc.getDiaChi(), ncc.getEmail(), ncc.getTrangThai()
            });
        }
    }

    // ==================== SEARCH ====================

    private void handleSearch() {
        String kw = txtSearch.getText().trim().toLowerCase();
        if (kw.isEmpty()) { loadData(); return; }
        ArrayList<NhaCungCap> filtered = new ArrayList<>();
        for (NhaCungCap ncc : danhSach) {
            if (ncc.getTenNCC().toLowerCase().contains(kw)
                || ncc.getMaNCC().toLowerCase().contains(kw)
                || (ncc.getSoDienThoai() != null && ncc.getSoDienThoai().contains(kw))
                || (ncc.getEmail() != null && ncc.getEmail().toLowerCase().contains(kw))) {
                filtered.add(ncc);
            }
        }
        renderTable(filtered);
    }

    // ==================== ACTIONS ====================

    private NhaCungCap getSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhà cung cấp!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return nccBUS.getById((String) tableModel.getValueAt(row, 0));
    }

    private void handleThem() {
        NhaCungCapDialog d = new NhaCungCapDialog(getFrame(), null);
        d.setVisible(true);
        if (d.isSaved()) loadData();
    }

    private void handleSua() {
        NhaCungCap ncc = getSelected();
        if (ncc == null) return;
        NhaCungCapDialog d = new NhaCungCapDialog(getFrame(), ncc);
        d.setVisible(true);
        if (d.isSaved()) loadData();
    }

    private void handleXoa() {
        NhaCungCap ncc = getSelected();
        if (ncc == null) return;
        int confirm = JOptionPane.showConfirmDialog(this,
                "Xác nhận xóa nhà cung cấp: \"" + ncc.getTenNCC() + "\"?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;
        if (nccBUS.delete(ncc.getMaNCC())) {
            JOptionPane.showMessageDialog(this, "Xóa thành công!");
            loadData();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Xóa thất bại! Nhà cung cấp có thể đang có phiếu nhập.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleChiTiet() {
        NhaCungCap ncc = getSelected();
        if (ncc == null) return;
        NhaCungCapDialog d = new NhaCungCapDialog(getFrame(), ncc);
        d.setViewOnly(true);
        d.setVisible(true);
    }

    private JFrame getFrame() {
        return (JFrame) SwingUtilities.getWindowAncestor(this);
    }
}
