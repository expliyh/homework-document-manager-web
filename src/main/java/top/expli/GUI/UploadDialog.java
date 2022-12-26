package top.expli.GUI;

import com.formdev.flatlaf.FlatIntelliJLaf;
import top.expli.ClientDocument;
import top.expli.ClientUser;
import top.expli.ExceptionProcess;
import top.expli.Permissions;
import top.expli.exceptions.KnifeException;
import top.expli.exceptions.NotAFile;
import top.expli.webapi.WebAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;

public class UploadDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField 随机生成TextField;
    private JTextField docNameField;
    private JButton uploadButton;
    private JTextArea descriptionArea;
    private JTextField timeField;
    private JTextField lastEditField;
    private JCheckBox setPublicCheckBox;

    private ClientUser me;

    private File toUpload;
    private BasicFileAttributes attributes;

    public UploadDialog(ClientUser me) {
        super();
        toUpload = null;
        this.me = me;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // 点击 X 时调用 onCancel()
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // 遇到 ESCAPE 时调用 onCancel()
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                if (jFileChooser.showOpenDialog(getRootPane()) == JFileChooser.APPROVE_OPTION) {
                    toUpload = jFileChooser.getSelectedFile();
                    try {
                        attributes = Files.readAttributes(toUpload.toPath(), BasicFileAttributes.class);
                    } catch (IOException ex) {
                        ExceptionProcess.process(getRootPane(), ex);
                    }
                    docNameField.setText(toUpload.getName());
                    docNameField.setEditable(true);
                    timeField.setText(new SimpleDateFormat("yyyy/MM/dd").format(attributes.creationTime().toMillis()));
                    lastEditField.setText(new SimpleDateFormat("yyyy/MM/dd").format(attributes.lastModifiedTime().toMillis()));
                }
            }
        });
    }

    private void onOK() {
        if (toUpload == null) {
            JOptionPane.showMessageDialog(getRootPane(), "请先选择文件", "未选择文件！", JOptionPane.WARNING_MESSAGE);
            return;
        } else if (!toUpload.isFile()) {
            ExceptionProcess.process(getRootPane(), new NotAFile());
        }
        try (FileInputStream inputStream = new FileInputStream(toUpload)) {
            ClientDocument clientDocument = new ClientDocument(docNameField.getText());
            if (setPublicCheckBox.isSelected()) {
                clientDocument.setPermissionLevel(Permissions.PUBLIC);
            } else {
                clientDocument.setPermissionLevel(Math.max(Permissions.ADMINONLY, me.getPermissionLevel()));
            }
            clientDocument.setOwner(me.getUserName());
            clientDocument.setFileName(toUpload.getName());
            clientDocument.setFile(inputStream.readAllBytes());
            clientDocument.setDescription(descriptionArea.getText());
            WebAdapter.uploadDocument(clientDocument);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (KnifeException e) {
            ExceptionProcess.process(getRootPane(), e);
            return;
        }
//        try {
//            Documents.addDocument(toUpload, userName, cache_user.GetPermissionLevel(userName), docNameField.getText(), descriptionArea.getText(), attributes);
//        } catch (Exception exception) {
//            ExceptionProcess.process(getRootPane(), exception);
//        }
        // 在此处添加您的代码
        dispose();
    }

    private void onCancel() {
        // 必要时在此处添加您的代码
        dispose();
    }

//    public static void main(String[] args) {
//        main("demo", null);
//    }

    public static void main(ClientUser me, Component parent) {
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
        UploadDialog dialog = new UploadDialog(me);
        dialog.setTitle("上传档案");
        dialog.setLocationRelativeTo(parent);
        dialog.pack();
        dialog.setVisible(true);
        //System.exit(0);
    }
}
