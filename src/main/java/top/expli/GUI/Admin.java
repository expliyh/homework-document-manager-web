package top.expli.GUI;

import com.formdev.flatlaf.FlatIntelliJLaf;
import top.expli.ClientUser;
import top.expli.ConsoleLog;
import top.expli.ExceptionProcess;
import top.expli.exceptions.KnifeException;
import top.expli.exceptions.UserNotFound;
import top.expli.webapi.WebAdapter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Objects;

public class Admin {
    private JPanel panel1;
    private JButton auserButton;
    private JButton adocButton;
    private JButton execButton;
    private JTextField docField;
    private JTextField userField;
    private JTextField textField3;
    private ClientUser me;

    public Admin(ClientUser usr) {
        this.me = usr;
        userField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                whenChange(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                whenChange(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                whenChange(e);
            }

            private void whenChange(DocumentEvent e) {
                if (Objects.equals(userField.getText(), "")) {
                    if (!Objects.equals(auserButton.getText(), "用户列表")) {
                        auserButton.setText("用户列表");
                    }
                } else {
                    if (!Objects.equals(auserButton.getText(), "编辑用户")) {
                        auserButton.setText("编辑用户");
                    }
                }
            }
        });
        docField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                whenChange(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                whenChange(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                whenChange(e);
            }

            private void whenChange(DocumentEvent e) {
                if (Objects.equals(docField.getText(), "")) {
                    if (!Objects.equals(adocButton.getText(), "档案列表")) {
                        adocButton.setText("档案列表");
                    }
                } else {
                    if (!Objects.equals(adocButton.getText(), "处理档案")) {
                        adocButton.setText("处理档案");
                    }
                }
            }
        });
        auserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Objects.equals(userField.getText(), "")) {
                    UserList.main(usr);
                } else {
                    try {
                        UserAdmin.main(WebAdapter.getUserInfo(userField.getText()), me);
                    } catch (IOException | KnifeException ex) {
                        ExceptionProcess.process(panel1.getComponent(0), ex);
                    }
                }
            }
        });
        adocButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DocList.main(usr);
            }
        });
    }

//    public static void main(String[] args) {
//        main(args, new User("admin", "12345678"));
//    }

    public static void main(Component parent, ClientUser usr) {
        ConsoleLog.log("Open admin panel!");
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
        JFrame frame = new JFrame("Admin");
        frame.setContentPane(new Admin(usr).panel1);
        frame.setLocationRelativeTo(parent);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
