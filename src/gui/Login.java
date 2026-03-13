package gui;

import bus.TaiKhoanBUS;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 *
 * @author DELL
 */

public class Login extends JFrame {

    JPanel left, right;
    JLabel lbTitle, lbUser, lbPass, lbWelcome, lbSub;
    JTextField txtUser;
    JPasswordField txtPass;
    JButton btnLogin, btnExit;
    JCheckBox showPass;

    public Login() {

        setTitle("Đăng nhập");
        setSize(820,480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        init();
    }

    public void init(){

        setLayout(null);

        // ================= PANEL TRÁI =================
        left = new JPanel();
        left.setBounds(0,0,360,480);
        left.setBackground(new Color(0,153,255));
        left.setLayout(null);

        lbWelcome = new JLabel("WELCOME!");
        lbWelcome.setForeground(Color.white);
        lbWelcome.setFont(new Font("Segoe UI",Font.BOLD,34));

        // Căn giữa horizontal
        lbWelcome.setBounds(0,180,360,40);
        lbWelcome.setHorizontalAlignment(SwingConstants.CENTER);

        // Dòng phụ dùng HTML để xuống dòng nếu dài
        lbSub = new JLabel("<html><center>Hệ thống quản lý<br>cửa hàng bán sách</center></html>");
        lbSub.setForeground(Color.white);
        lbSub.setFont(new Font("Segoe UI",Font.PLAIN,18));
        lbSub.setBounds(20,240,320,50);
        lbSub.setHorizontalAlignment(SwingConstants.CENTER);

        left.add(lbWelcome);
        left.add(lbSub);

        // ================= PANEL PHẢI =================
        right = new JPanel();
        right.setBounds(360,0,460,480);
        right.setLayout(null);
        right.setBackground(Color.white);

        lbTitle = new JLabel("Đăng nhập");
        lbTitle.setFont(new Font("Segoe UI",Font.BOLD,24));
        lbTitle.setBounds(170,70,200,40);

        lbUser = new JLabel("Tên đăng nhập");
        lbUser.setBounds(100,150,200,25);

        txtUser = new JTextField();
        txtUser.setBounds(100,175,260,35);

        lbPass = new JLabel("Mật khẩu");
        lbPass.setBounds(100,220,200,25);

        txtPass = new JPasswordField();
        txtPass.setBounds(100,245,260,35);

        showPass = new JCheckBox("Hiện mật khẩu");
        showPass.setBounds(100,285,150,25);
        showPass.setBackground(Color.white);

        showPass.addActionListener(e -> {
            if(showPass.isSelected())
                txtPass.setEchoChar((char)0);
            else
                txtPass.setEchoChar('*');
        });

        btnLogin = new JButton("Đăng nhập");
        btnLogin.setBounds(100,330,120,40);
        btnLogin.setBackground(new Color(0,153,255));
        btnLogin.setForeground(Color.white);
        btnLogin.setFont(new Font("Segoe UI",Font.BOLD,14));

        btnExit = new JButton("Thoát");
        btnExit.setBounds(240,330,120,40);
        btnExit.setBackground(new Color(220,53,69));
        btnExit.setForeground(Color.white);
        btnExit.setFont(new Font("Segoe UI",Font.BOLD,14));

        btnExit.addActionListener(e -> System.exit(0));

        // ENTER chuyển ô
        txtUser.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent evt){
                if(evt.getKeyCode()==KeyEvent.VK_ENTER)
                    txtPass.requestFocus();
            }
        });

        // ENTER đăng nhập
        txtPass.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent evt){
                if(evt.getKeyCode()==KeyEvent.VK_ENTER)
                    btnLogin.doClick();
            }
        });

        right.add(lbTitle);
        right.add(lbUser);
        right.add(txtUser);
        right.add(lbPass);
        right.add(txtPass);
        right.add(showPass);
        right.add(btnLogin);
        right.add(btnExit);

        btnLogin.addActionListener(e -> {
            String user = txtUser.getText().trim();
            String pass = new String(txtPass.getPassword());

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập tên đăng nhập và mật khẩu!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            TaiKhoanBUS bus = new TaiKhoanBUS();
            dto.TaiKhoan tk = bus.login(user, pass);

            if (tk != null) {
                JOptionPane.showMessageDialog(this,
                    "Đăng nhập thành công! Xin chào " + tk.getTenDangNhap(),
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
                MainFrame mainJFrame = new MainFrame();
                mainJFrame.setUserInfo(tk.getTenDangNhap());
                mainJFrame.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Sai tên đăng nhập hoặc mật khẩu, hoặc tài khoản đã bị khoá!",
                    "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
                txtPass.setText("");
                txtUser.requestFocus();
            }
        });
        
        add(left);
        add(right);
    }

    public static void main(String[] args) {
        new Login().setVisible(true);
    }
}