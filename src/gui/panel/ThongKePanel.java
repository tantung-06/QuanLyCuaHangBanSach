package gui.panel;

import dao.PhieuXuatDAO;
import dao.SachDAO;
import dao.KhachHangDAO;
import dao.NhanVienDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.geom.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.Locale;

public class ThongKePanel extends JPanel {

    private final PhieuXuatDAO pxDAO = new PhieuXuatDAO();
    private final SachDAO sachDAO = new SachDAO();
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();
    private final NhanVienDAO nhanVienDAO = new NhanVienDAO();
    private static final NumberFormat FMT = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
    private static final Color BG = new Color(235, 252, 255);

    // Summary labels
    private JLabel lblTongSach, lblTongKhach, lblNhanVienHoatDong;

    // Filter
    private JComboBox<String> cboThang, cboNam;

    // Chart
    private LineChartPanel chartPanel;

    // Tables
    private DefaultTableModel modelTopSach, modelTongHop;

    public ThongKePanel() {
        setLayout(new BorderLayout(0, 10));
        setBackground(BG);
        setBorder(new EmptyBorder(12, 14, 12, 14));
        add(buildHeader(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        loadAll();
    }

    // ==================== HEADER (summary + filter) ====================
    private JPanel buildHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(BG);

        JPanel summary = buildSummaryCards();
        summary.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.add(summary);
        header.add(Box.createVerticalStrut(10));

        JPanel filter = buildFilterBar();
        filter.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.add(filter);

        return header;
    }

    private JPanel buildSummaryCards() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 12, 0));
        panel.setBackground(BG);

        lblTongSach = new JLabel("0", SwingConstants.CENTER);
        lblTongKhach = new JLabel("0", SwingConstants.CENTER);
        lblNhanVienHoatDong = new JLabel("0", SwingConstants.CENTER);

        panel.add(createSummaryCard(
                lblTongSach,
                "Sách hiện có trong kho",
                () -> showSachConHangDialog()
        ));
        panel.add(createSummaryCard(
                lblTongKhach,
                "Khách từ trước đến nay",
                () -> showKhachHangDialog()
        ));
        panel.add(createSummaryCard(
                lblNhanVienHoatDong,
                "Nhân viên đang hoạt động",
                () -> showNhanVienHoatDongDialog()
        ));

        return panel;
    }

    private JPanel createSummaryCard(JLabel numberLabel, String subtitle, Runnable onClick) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(new Color(0, 183, 255));
        card.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));

        numberLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        numberLabel.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel(subtitle, SwingConstants.CENTER);
        lblSub.setForeground(Color.WHITE);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        card.add(numberLabel, BorderLayout.CENTER);
        card.add(lblSub, BorderLayout.SOUTH);

        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (onClick != null) {
                    onClick.run();
                }
            }
        });

        return card;
    }

    // ==================== FILTER BAR ====================
    private JPanel buildFilterBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        bar.setBackground(Color.WHITE);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 230, 240)),
                new EmptyBorder(6, 10, 6, 10)));

        bar.add(new JLabel("Tháng:"));
        cboThang = new JComboBox<>();
        for (int i = 1; i <= 12; i++) {
            cboThang.addItem("Tháng " + i);
        }
        cboThang.setSelectedIndex(LocalDate.now().getMonthValue() - 1);
        cboThang.setPreferredSize(new Dimension(110, 30));
        bar.add(cboThang);

        bar.add(Box.createHorizontalStrut(10));
        bar.add(new JLabel("Năm:"));
        cboNam = new JComboBox<>();
        int curYear = LocalDate.now().getYear();
        for (int y = curYear; y >= curYear - 4; y--) {
            cboNam.addItem(String.valueOf(y));
        }
        cboNam.setPreferredSize(new Dimension(80, 30));
        bar.add(cboNam);

        JButton btnLoad = new JButton("Xem");
        btnLoad.setPreferredSize(new Dimension(100, 30));
        btnLoad.setBackground(new Color(0, 120, 200));
        btnLoad.setForeground(Color.WHITE);
        btnLoad.setFocusPainted(false);
        btnLoad.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLoad.addActionListener(e -> loadAll());
        bar.add(Box.createHorizontalStrut(10));
        bar.add(btnLoad);

        return bar;
    }

    // ==================== CENTER (chart + tables) ====================
    private JPanel buildCenter() {
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(BG);

        // Chart
        chartPanel = new LineChartPanel();
        chartPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        chartPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));
        chartPanel.setPreferredSize(new Dimension(800, 260));

        JPanel chartCard = wrapCard(chartPanel, "Doanh thu theo ngày");
        chartCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        center.add(chartCard);
        center.add(Box.createVerticalStrut(12));

        // Bottom tables side by side
        JPanel tableRow = new JPanel(new GridLayout(1, 2, 12, 0));
        tableRow.setBackground(BG);
        tableRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        tableRow.add(wrapCard(buildTopSachTable(), "Top 10 sách bán chạy"));
        tableRow.add(wrapCard(buildTongHopTable(), "Tổng hợp theo tháng trong năm"));

        center.add(tableRow);
        return center;
    }

    private JPanel wrapCard(JComponent content, String title) {
        JPanel card = new JPanel(new BorderLayout(0, 6));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 230, 240)),
                new EmptyBorder(10, 12, 10, 12)));

        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(new Color(50, 80, 120));
        lbl.setBorder(new EmptyBorder(0, 0, 6, 0));

        card.add(lbl, BorderLayout.NORTH);
        card.add(content, BorderLayout.CENTER);
        return card;
    }

    // ==================== TOP SACH TABLE ====================
    private JScrollPane buildTopSachTable() {
        modelTopSach = new DefaultTableModel(
                new String[]{"#", "Tên sách", "Số lượng", "Doanh thu"}, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        return styledTable(modelTopSach, new int[]{30, 260, 70, 110});
    }

    // ==================== TONG HOP TABLE ====================
    private JScrollPane buildTongHopTable() {
        modelTongHop = new DefaultTableModel(
                new String[]{"Tháng", "Số phiếu xuất", "Doanh thu"}, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        return styledTable(modelTongHop, new int[]{70, 110, 130});
    }

    private JScrollPane styledTable(DefaultTableModel model, int[] widths) {
        JTable t = new JTable(model);
        t.setRowHeight(28);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        t.setShowHorizontalLines(true);
        t.setGridColor(new Color(230, 235, 240));
        t.setSelectionBackground(new Color(210, 235, 255));
        t.setFocusable(false);

        JTableHeader h = t.getTableHeader();
        h.setFont(new Font("Segoe UI", Font.BOLD, 12));
        h.setBackground(new Color(234, 234, 234));
        h.setPreferredSize(new Dimension(0, 30));

        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable tbl, Object v,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(tbl, v, sel, foc, row, col);
                setHorizontalAlignment(col == 1 ? LEFT : CENTER);
                setBorder(new EmptyBorder(0, 6, 0, 6));
                if (!sel) {
                    setBackground(Color.WHITE);
                }
                return this;
            }
        });

        for (int i = 0; i < widths.length; i++) {
            t.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        JScrollPane sp = new JScrollPane(t);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.setPreferredSize(new Dimension(0, 220));
        return sp;
    }

    // ==================== LOAD DATA ====================
    private void loadAll() {
        int thang = cboThang.getSelectedIndex() + 1;
        int nam = Integer.parseInt((String) cboNam.getSelectedItem());

        // 0. Summary cards
        updateSummary();

        // 1. Line chart
        ArrayList<Object[]> doanhThuNgay = pxDAO.getDoanhThuTheoNgay(thang, nam);
        chartPanel.setData(doanhThuNgay, thang, nam);

        // 2. Top sách
        modelTopSach.setRowCount(0);
        ArrayList<Object[]> topSach = pxDAO.getTopSachBanChay(10);
        int rank = 1;
        for (Object[] row : topSach) {
            modelTopSach.addRow(new Object[]{
                rank++,
                row[0],
                row[1],
                FMT.format(row[2]) + " ₫"
            });
        }

        // 3. Tổng hợp theo tháng
        modelTongHop.setRowCount(0);
        ArrayList<Object[]> tongHop = pxDAO.getTongHopTheoThang(nam);
        for (Object[] row : tongHop) {
            int thg = (int) row[0];
            modelTongHop.addRow(new Object[]{
                "Tháng " + thg,
                row[1],
                FMT.format(row[2]) + " ₫"
            });
        }
    }

    private void updateSummary() {
        try {
            // Sách còn hàng
            int tongSach = sachDAO.getByTrangThai("CON_HANG").size();
            // Tất cả khách hàng (kể cả ngừng)
            int tongKhach = khachHangDAO.getAll().size();
            // Nhân viên đang hoạt động
            int nvHoatDong = nhanVienDAO.getByTrangThai("HOAT_DONG").size();

            lblTongSach.setText(String.valueOf(tongSach));
            lblTongKhach.setText(String.valueOf(tongKhach));
            lblNhanVienHoatDong.setText(String.valueOf(nvHoatDong));
        } catch (Exception ex) {
            // nếu có lỗi, giữ giá trị cũ để UI không bị crash
        }
    }

    // ==================== DIALOG HIỂN THỊ DANH SÁCH ====================
    private void showSachConHangDialog() {
        java.util.List<dto.Sach> list = sachDAO.getByTrangThai("CON_HANG");
        String[] cols = {"Mã sách", "Tên sách", "Giá bán", "Tồn kho"};
        Object[][] data = new Object[list.size()][cols.length];
        for (int i = 0; i < list.size(); i++) {
            dto.Sach s = list.get(i);
            data[i][0] = s.getMaSach();
            data[i][1] = s.getTenSach();
            data[i][2] = FMT.format(s.getGiaBan()) + " ₫";
            data[i][3] = s.getSoLuongTon();
        }
        showSimpleTableDialog("Sách hiện có trong kho", cols, data);
    }

    private void showKhachHangDialog() {
        java.util.List<dto.KhachHang> list = khachHangDAO.getAll();
        String[] cols = {"Mã KH", "Họ", "Tên", "SĐT", "Email", "Trạng thái"};
        Object[][] data = new Object[list.size()][cols.length];
        for (int i = 0; i < list.size(); i++) {
            dto.KhachHang kh = list.get(i);
            data[i][0] = kh.getMaKhachHang();
            data[i][1] = kh.getHo();
            data[i][2] = kh.getTen();
            data[i][3] = kh.getSoDienThoai();
            data[i][4] = kh.getEmail();
            data[i][5] = kh.getTrangThai();
        }
        showSimpleTableDialog("Khách hàng từ trước đến nay", cols, data);
    }

    private void showNhanVienHoatDongDialog() {
        java.util.List<dto.NhanVien> list = nhanVienDAO.getByTrangThai("HOAT_DONG");
        String[] cols = {"Mã NV", "Họ", "Tên", "SĐT", "Email", "Trạng thái"};
        Object[][] data = new Object[list.size()][cols.length];
        for (int i = 0; i < list.size(); i++) {
            dto.NhanVien nv = list.get(i);
            data[i][0] = nv.getMaNhanVien();
            data[i][1] = nv.getHo();
            data[i][2] = nv.getTen();
            data[i][3] = nv.getSoDienThoai();
            data[i][4] = nv.getEmail();
            data[i][5] = nv.getTrangThai();
        }
        showSimpleTableDialog("Nhân viên đang hoạt động", cols, data);
    }

    private void showSimpleTableDialog(String title, String[] columns, Object[][] data) {
        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dlg = owner instanceof Frame
                ? new JDialog((Frame) owner, title, true)
                : new JDialog((Dialog) owner, title, true);

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setRowHeight(26);
        table.setAutoCreateRowSorter(true);

        dlg.getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
        dlg.setSize(800, 400);
        dlg.setLocationRelativeTo(owner);
        dlg.setVisible(true);
    }

    // ==================== LINE CHART ====================
    private static class LineChartPanel extends JPanel {

        private List<LocalDate> dates = new ArrayList<>();
        private List<Double> values = new ArrayList<>();
        private int thang, nam;

        private static final Color LINE_COLOR = new Color(0, 120, 200);
        private static final Color FILL_COLOR = new Color(0, 120, 200, 40);
        private static final Color GRID_COLOR = new Color(230, 235, 240);
        private static final Color POINT_COLOR = new Color(0, 90, 170);

        public LineChartPanel() {
            setBackground(Color.WHITE);
        }

        public void setData(ArrayList<Object[]> data, int thang, int nam) {
            this.thang = thang;
            this.nam = nam;
            dates.clear();
            values.clear();
            for (Object[] row : data) {
                dates.add((LocalDate) row[0]);
                values.add((Double) row[1]);
            }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int W = getWidth(), H = getHeight();
            int padL = 80, padR = 20, padT = 20, padB = 50;
            int cW = W - padL - padR;
            int cH = H - padT - padB;

            if (dates.isEmpty()) {
                g2.setColor(new Color(160, 160, 180));
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                String msg = "Không có dữ liệu tháng " + thang + "/" + nam;
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(msg,
                        W / 2 - fm.stringWidth(msg) / 2,
                        H / 2);
                return;
            }

            double maxVal = values.stream().mapToDouble(d -> d).max().orElse(1);
            // Round up maxVal for nicer grid
            double niceMax = niceNumber(maxVal * 1.1);
            int gridLines = 5;

            // Draw grid + Y labels
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            g2.setColor(GRID_COLOR);
            for (int i = 0; i <= gridLines; i++) {
                int y = padT + cH - (int) (cH * i / gridLines);
                g2.setColor(GRID_COLOR);
                g2.drawLine(padL, y, padL + cW, y);
                g2.setColor(new Color(100, 100, 120));
                double labelVal = niceMax * i / gridLines;
                String label = formatShort(labelVal);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(label, padL - fm.stringWidth(label) - 6,
                        y + fm.getAscent() / 2);
            }

            // X positions
            int n = dates.size();
            int step = n > 1 ? cW / (n - 1) : cW;

            // Fill area
            GeneralPath fill = new GeneralPath();
            fill.moveTo(padL, padT + cH);
            for (int i = 0; i < n; i++) {
                int x = padL + (n > 1 ? cW * i / (n - 1) : cW / 2);
                int y = padT + cH - (int) (cH * values.get(i) / niceMax);
                if (i == 0) {
                    fill.lineTo(x, y);
                } else {
                    fill.lineTo(x, y);
                }
            }
            fill.lineTo(padL + cW, padT + cH);
            fill.closePath();
            g2.setColor(FILL_COLOR);
            g2.fill(fill);

            // Line
            g2.setColor(LINE_COLOR);
            g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            GeneralPath line = new GeneralPath();
            for (int i = 0; i < n; i++) {
                int x = padL + (n > 1 ? cW * i / (n - 1) : cW / 2);
                int y = padT + cH - (int) (cH * values.get(i) / niceMax);
                if (i == 0) {
                    line.moveTo(x, y);
                } else {
                    line.lineTo(x, y);
                }
            }
            g2.draw(line);

            // Points + X labels
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            for (int i = 0; i < n; i++) {
                int x = padL + (n > 1 ? cW * i / (n - 1) : cW / 2);
                int y = padT + cH - (int) (cH * values.get(i) / niceMax);

                // Point
                g2.setColor(Color.WHITE);
                g2.fillOval(x - 4, y - 4, 8, 8);
                g2.setColor(POINT_COLOR);
                g2.setStroke(new BasicStroke(2f));
                g2.drawOval(x - 4, y - 4, 8, 8);

                // X label — hiện mỗi 3 ngày nếu nhiều
                if (n <= 15 || i % 3 == 0) {
                    String lbl = String.format("%02d/%02d",
                            dates.get(i).getDayOfMonth(),
                            dates.get(i).getMonthValue());
                    FontMetrics fm = g2.getFontMetrics();
                    g2.setColor(new Color(80, 80, 100));
                    g2.drawString(lbl, x - fm.stringWidth(lbl) / 2, padT + cH + 16);
                }
            }

            // Axes
            g2.setColor(new Color(180, 190, 200));
            g2.setStroke(new BasicStroke(1f));
            g2.drawLine(padL, padT, padL, padT + cH);
            g2.drawLine(padL, padT + cH, padL + cW, padT + cH);
        }

        private double niceNumber(double v) {
            if (v <= 0) {
                return 1;
            }
            double exp = Math.floor(Math.log10(v));
            double f = v / Math.pow(10, exp);
            double nice;
            if (f < 1.5) {
                nice = 1;
            } else if (f < 3) {
                nice = 2;
            } else if (f < 7) {
                nice = 5;
            } else {
                nice = 10;
            }
            return nice * Math.pow(10, exp);
        }

        private String formatShort(double v) {
            if (v >= 1_000_000) {
                return String.format("%.1fM", v / 1_000_000);
            }
            if (v >= 1_000) {
                return String.format("%.0fK", v / 1_000);
            }
            return String.format("%.0f", v);
        }
    }
}