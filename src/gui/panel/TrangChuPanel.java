//package gui;
//
//import javax.swing.*;
//import java.awt.*;
//
//public class TrangChuPanel extends JPanel {
//
//    public TrangChuPanel() {
//        setLayout(new BorderLayout());
//        setBackground(Color.WHITE);
//        init();
//    }
//
//    private void init() {
//        // Left content
//        JPanel left = new JPanel();
//        left.setBackground(Color.WHITE);
//        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
//        left.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 10));
//
//        String[][] features = {
//            {"🛡", "TÍNH BẢO MẬT",   "Tăng cường bảo mật cho các hoạt động quản lý."},
//            {"⚡", "TÍNH CHÍNH XÁC", "Đảm bảo tính chính xác và độ tin cậy cho các hoạt động."},
//            {"🚀", "TÍNH HIỆU QUẢ",  "Dễ dàng truy cập thông tin bán sách nhanh chóng."},
//            {"🔒", "TÍNH ỔN ĐỊNH",   "Hệ thống hoạt động ổn định theo thời gian."},
//        };
//
//        for (String[] f : features) {
//            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
//            row.setBackground(Color.WHITE);
//            row.setMaximumSize(new Dimension(450, 80));
//
//            JLabel ico = new JLabel(f[0]);
//            ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
//            ico.setPreferredSize(new Dimension(48, 48));
//            ico.setHorizontalAlignment(SwingConstants.CENTER);
//
//            JPanel txt = new JPanel(new GridLayout(2, 1));
//            txt.setBackground(Color.WHITE);
//            JLabel title = new JLabel(f[1]);
//            title.setFont(new Font("Segoe UI", Font.BOLD, 13));
//            JLabel desc = new JLabel("<html><p style='width:260px'>" + f[2] + "</p></html>");
//            desc.setFont(new Font("Segoe UI", Font.PLAIN, 11));
//            desc.setForeground(Color.GRAY);
//            txt.add(title);
//            txt.add(desc);
//
//            row.add(ico);
//            row.add(txt);
//            left.add(row);
//        }
//
//        // Right banner
//        JPanel right = new JPanel() {
//            @Override
//            protected void paintComponent(Graphics g) {
//                super.paintComponent(g);
//                Graphics2D g2 = (Graphics2D) g;
//                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//                GradientPaint gp = new GradientPaint(0, 0,
//                        new Color(0, 180, 220), getWidth(), getHeight(), new Color(0, 120, 160));
//                g2.setPaint(gp);
//                g2.fillRect(0, 0, getWidth(), getHeight());
//
//                g2.setColor(new Color(255, 255, 255, 40));
//                g2.setFont(new Font("Segoe UI", Font.BOLD, 42));
//                g2.drawString("HỆ THỐNG", 40, 150);
//                g2.drawString("QUẢN LÝ", 40, 210);
//                g2.drawString("CỬA HÀNG", 40, 270);
//                g2.drawString("SÁCH", 40, 330);
//
//                // Book icon
//                g2.setColor(Color.WHITE);
//                g2.fillRoundRect(320, 100, 130, 170, 16, 16);
//                g2.setColor(new Color(0, 153, 200));
//                g2.setFont(new Font("Segoe UI", Font.BOLD, 22));
//                g2.drawString("SÁCH", 345, 195);
//            }
//        };
//        right.setPreferredSize(new Dimension(520, 400));
//
//        add(left,  BorderLayout.CENTER);
//        add(right, BorderLayout.EAST);
//    }
//}

package gui.panel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TrangChuPanel extends JPanel {
    private BufferedImage backgroundImage;

    public TrangChuPanel() {
        try {
            backgroundImage = ImageIO.read(new File("src/img/trangchu.png"));
        } catch (IOException e) {
            System.err.println("Không tìm thấy file ảnh: " + e.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}