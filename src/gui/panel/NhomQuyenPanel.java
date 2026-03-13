package gui.panel;

import bus.NhomQuyenBUS;
import dto.NhomQuyen;
import gui.dialog.NhomQuyenDialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class NhomQuyenPanel extends JPanel {

    private final NhomQuyenBUS nqBUS = new NhomQuyenBUS();

    private ArrayList<NhomQuyen> danhSach = new ArrayList<>();

    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField txtSearch;
    private JButton btnThem, btnXoa, btnSua, btnChiTiet;

    private static final Color BG = new Color(240, 247, 250);

    public NhomQuyenPanel() {
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

        // --- Phải: tìm kiếm ---
        JPanel searchWrapper = new JPanel(new GridBagLayout());
        searchWrapper.setBackground(Color.WHITE);

        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchRow.setBackground(Color.WHITE);

        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(200, 34));
        txtSearch.setToolTipText("Tìm kiếm mã, tên nhóm quyền...");
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
            loadData();
        });

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
        btn.setPreferredSize(new Dimension(80, 76));
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
                        .getScaledInstance(36, 36, Image.SCALE_SMOOTH)));
            }
        } catch (Exception ignored) {}

        JLabel lblText = new JLabel(text, SwingConstants.CENTER);
        lblText.setFont(new Font("Segoe UI", Font.BOLD, 12));
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

        String[] cols = {"Mã nhóm quyền", "Tên nhóm quyền"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        table.setRowHeight(32);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(230, 235, 240));
        table.setSelectionBackground(new Color(210, 235, 255));
        table.setSelectionForeground(Color.BLACK);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        table.setFocusable(false);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(232, 245, 255));
        header.setForeground(new Color(50, 50, 50));
        header.setPreferredSize(new Dimension(0, 36));

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setHorizontalAlignment(col == 0
                        ? SwingConstants.CENTER : SwingConstants.LEFT);
                setBorder(new EmptyBorder(0, 8, 0, 8));
                if (!sel) setBackground(Color.WHITE);
                setForeground(Color.BLACK);
                return this;
            }
        });

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) handleChiTiet();
            }
        });

        table.getColumnModel().getColumn(1).setPreferredWidth(300);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ==================== LOAD ====================

    public void loadData() {
        danhSach = nqBUS.getAll();
        renderTable(danhSach);
    }

    private void renderTable(ArrayList<NhomQuyen> ds) {
        tableModel.setRowCount(0);
        for (NhomQuyen nq : ds) {
            tableModel.addRow(new Object[]{
                nq.getMaNhomQuyen(),
                nq.getTenNhomQuyen()
            });
        }
    }

    // ==================== SEARCH ====================

    private void handleSearch() {
        String kw = txtSearch.getText().trim().toLowerCase();
        if (kw.isEmpty()) { loadData(); return; }
        ArrayList<NhomQuyen> filtered = new ArrayList<>();
        for (NhomQuyen nq : danhSach) {
            if (nq.getMaNhomQuyen().toLowerCase().contains(kw)
                || nq.getTenNhomQuyen().toLowerCase().contains(kw)) {
                filtered.add(nq);
            }
        }
        renderTable(filtered);
    }

    // ==================== ACTIONS ====================

    private NhomQuyen getSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhóm quyền!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return nqBUS.getById((String) tableModel.getValueAt(row, 0));
    }

    private void handleThem() {
        NhomQuyenDialog d = new NhomQuyenDialog(getFrame(), null);
        d.setVisible(true);
        if (d.isSaved()) loadData();
    }

    private void handleSua() {
        NhomQuyen nq = getSelected();
        if (nq == null) return;
        NhomQuyenDialog d = new NhomQuyenDialog(getFrame(), nq);
        d.setVisible(true);
        if (d.isSaved()) loadData();
    }

    private void handleXoa() {
        NhomQuyen nq = getSelected();
        if (nq == null) return;
        int confirm = JOptionPane.showConfirmDialog(this,
                "Xác nhận xóa nhóm quyền: \"" + nq.getTenNhomQuyen() + "\"?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;
        if (nqBUS.delete(nq.getMaNhomQuyen())) {
            JOptionPane.showMessageDialog(this, "Xóa thành công!");
            loadData();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Xóa thất bại! Nhóm quyền đang được tài khoản sử dụng.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleChiTiet() {
        NhomQuyen nq = getSelected();
        if (nq == null) return;
        NhomQuyenDialog d = new NhomQuyenDialog(getFrame(), nq);
        d.setViewOnly(true);
        d.setVisible(true);
    }

    private JFrame getFrame() {
        return (JFrame) SwingUtilities.getWindowAncestor(this);
    }
}