package view;

import javax.swing.*;
import model.User;
import org.hibernate.Session;
import utils.HibernateUtil;

public class RegisterForm extends JFrame {
    public RegisterForm() {
        setTitle("Register");
        setSize(300, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        JTextField txtUser = new JTextField(15);
        JPasswordField txtPass = new JPasswordField(15);
        JTextField txtEmail = new JTextField(15);
        JTextField txtQuestion = new JTextField(15);
        JTextField txtAnswer = new JTextField(15);

        JButton btnRegister = new JButton("Register");

        panel.add(new JLabel("Username")); panel.add(txtUser);
        panel.add(new JLabel("Password")); panel.add(txtPass);
        panel.add(new JLabel("Email")); panel.add(txtEmail);
        panel.add(new JLabel("Security Question")); panel.add(txtQuestion);
        panel.add(new JLabel("Answer")); panel.add(txtAnswer);
        panel.add(btnRegister);

        add(panel);

        btnRegister.addActionListener(e -> {
            User user = new User();
            user.setUsername(txtUser.getText());
            user.setPassword(new String(txtPass.getPassword()));
            user.setEmail(txtEmail.getText());
            user.setSecurityQuestion(txtQuestion.getText());
            user.setSecurityAnswer(txtAnswer.getText());

            Session s = HibernateUtil.getSessionFactory().openSession();
            s.beginTransaction();
            s.save(user);
            s.getTransaction().commit();
            s.close();

            JOptionPane.showMessageDialog(this, "Registered!");
            new LoginForm().setVisible(true);
            dispose();
        });
    }
}
