package top.expli.GUI;

import com.formdev.flatlaf.FlatIntelliJLaf;
import top.expli.ClientUser;
import top.expli.User;
import top.expli.cache_user;
import top.expli.documents.Documents;
import top.expli.exceptions.KnifeException;
import top.expli.webapi.WebAdapter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Login {
    private JPanel panel;
    private JTextField usernameInput;
    private JTextField passwdInput;
    private JButton buttonLogin;
    private JButton buttonExit;
    private JLabel knife;
    private JPanel buttonPanel;
    private JPasswordField passwordField;

    private JFrame thisFrame;

    private String[] knivesString;

//    private cache_user users;

    public Login(JFrame frame) {
        thisFrame = frame;
        //this.knivesString = knives.random().split("(?<=\\G.{11})");
        knife.setText("用户登录");
        buttonLogin.addActionListener(new LoginListener());
//        buttonExit.addActionListener(new ExitListener());

    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (UnsupportedLookAndFeelException e) {
            System.out.println("FUCK");
        }

        JFrame frame = new JFrame("Login");
        Login login = new Login(frame);
        login.thisFrame = frame;
        frame.setContentPane(login.panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getRootPane().setDefaultButton(login.buttonLogin);
        frame.pack();
        frame.setVisible(true);
    }


    private class LoginListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String user_name = usernameInput.getText();
            String passwd = String.valueOf(passwordField.getPassword());
            System.out.println(user_name);
            System.out.println(passwd);
            ClientUser usr;
            try {
                usr = WebAdapter.login(user_name, passwd);
            } catch (KnifeException | IOException ex) {
                ErrorMessage.main(new String[0], ex);
                return;
            }
            if (usr.getPermissionLevel() <= 3) {
                thisFrame.setVisible(false);
                Admin.main(new String[0]);
            } else if (usr.getPermissionLevel() == 4) {
                thisFrame.setVisible(false);
                UserGUI.main(new String[]{},usr.getUserName());
            }
        }
    }

//    private class ExitListener implements ActionListener {
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            cache_user.writeData();
//            Documents.writeData();
//            System.exit(0);
//        }
//    }
}
