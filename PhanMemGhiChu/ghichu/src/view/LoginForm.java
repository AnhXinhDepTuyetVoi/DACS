package view;

import javax.swing.*;
import model.User;
import org.hibernate.Session;
import org.hibernate.query.Query;
import utils.HibernateUtil;
import utils.RememberMeUtil;
import java.awt.event.*;

public class LoginForm extends JFrame {
    private JTextField txtUser;
    private JPasswordField txtPass;
    private JCheckBox chkRemember;

    public LoginForm() {
        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        txtUser = new JTextField(15);
        txtPass = new JPasswordField(15);
        chkRemember = new JCheckBox("Remember Me");

        JButton btnLogin = new JButton("Login");
        JButton btnRegister = new JButton("Register");
        JButton btnForgot = new JButton("Forgot Password");

        panel.add(new JLabel("Username"));
        panel.add(txtUser);
        panel.add(new JLabel("Password"));
        panel.add(txtPass);
        panel.add(chkRemember);
        panel.add(btnLogin);
        panel.add(btnRegister);
        panel.add(btnForgot);
        add(panel);

        // Load remember data
        String[] remembered = RememberMeUtil.load();
        txtUser.setText(remembered[0]);
        txtPass.setText(remembered[1]);
        chkRemember.setSelected(!remembered[0].isEmpty());

        btnLogin.addActionListener(e -> {
            Session s = HibernateUtil.getSessionFactory().openSession();
            Query<User> q = s.createQuery("from User where username = :u and password = :p", User.class);
            q.setParameter("u", txtUser.getText());
            q.setParameter("p", new String(txtPass.getPassword()));
            User user = q.uniqueResult();
            s.close();

            if (user != null) {
                if (chkRemember.isSelected()) RememberMeUtil.save(txtUser.getText(), new String(txtPass.getPassword()));
                else RememberMeUtil.clear();
                JOptionPane.showMessageDialog(this, "Login successful!");
                // open main app...
            } else {
                JOptionPane.showMessageDialog(this, "Invalid login!");
            }
        });

        btnRegister.addActionListener(e -> {
            new RegisterForm().setVisible(true);
            dispose();
        });

        btnForgot.addActionListener(e -> {
            String username = JOptionPane.showInputDialog(this, "Enter username:");
            Session s = HibernateUtil.getSessionFactory().openSession();
            Query<User> q = s.createQuery("from User where username = :u", User.class);
            q.setParameter("u", username);
            User user = q.uniqueResult();
            s.close();

            if (user != null) {
                String answer = JOptionPane.showInputDialog("Security Question: " + user.getSecurityQuestion());
                if (answer != null && answer.equals(user.getSecurityAnswer())) {
                    JOptionPane.showMessageDialog(this, "Your password: " + user.getPassword());
                } else {
                    JOptionPane.showMessageDialog(this, "Wrong answer!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "User not found!");
            }
        });
    }
}
