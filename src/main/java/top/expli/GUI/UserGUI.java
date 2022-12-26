package top.expli.GUI;

import com.formdev.flatlaf.FlatIntelliJLaf;
import top.expli.ClientUser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserGUI {
    private JPanel mainPanel;
    private JButton docListButton;
    private JButton uploadButton;
    private JButton exitButton;
    private JButton passwdChangeButton;
    private JFrame thisFrame;
    private ClientUser me;

    protected UserGUI(JFrame thisFrame, ClientUser me) {
        this.thisFrame = thisFrame;
        this.me = me;

        docListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DocList.main(me);
            }
        });
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UploadDialog.main(me, thisFrame);
//                JFileChooser jFileChooser = new JFileChooser();
//                if (jFileChooser.showOpenDialog(thisFrame) == JFileChooser.APPROVE_OPTION) {
//
//                    try {
//                        Documents.addDocument(jFileChooser.getSelectedFile(), userName, cache_user.GetPermissionLevel(userName));
//                    } catch (KnifeException knifeException) {
//                        ExceptionProcess.process(thisFrame, knifeException);
//                    }
//                }
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }


//    public static void main(String[] args) {
//        main(args, "demo");
//    }

    public static void main(Component parent,ClientUser me) {
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
        JFrame frame = new JFrame("档案管理系统-用户界面");
        frame.setContentPane(new UserGUI(frame, me).mainPanel);
        frame.setLocationRelativeTo(parent);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
