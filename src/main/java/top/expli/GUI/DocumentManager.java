package top.expli.GUI;

import com.formdev.flatlaf.FlatIntelliJLaf;
import top.expli.ClientDocument;
import top.expli.ClientUser;
import top.expli.ExceptionProcess;
import top.expli.Permissions;
import top.expli.exceptions.FileNotFound;
import top.expli.exceptions.KnifeException;
import top.expli.webapi.WebAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class DocumentManager extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField docNameField;
    private JTextField fileNameField;
    private JTextField ownerField;
    private JTextArea descriptionArea;
    private JComboBox<String> permissionBox;
    private JButton downloadButton;
    private JButton deleteButton;
    private ClientUser me;
    protected ClientDocument document;
    int permissionMove;

    public DocumentManager(ClientUser me, ClientDocument document) {
        this.me = me;
        this.document = document;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        fileNameField.setEditable(false);
        fileNameField.setEnabled(false);
        permissionMove = 4;
        String[] permissionList = new String[]{"私有", "公开"};
        if (me.getPermissionLevel() == 3 && Objects.equals(document.getOwner(), me.getUserName())) {
            permissionMove = 3;
            permissionList = new String[]{"私有", "仅管理员可见", "公开"};
        } else if (me.getPermissionLevel() < 3) {
            permissionMove = 3;
            permissionList = new String[]{"私有", "仅管理员可见", "公开"};
        }
        if (me.getPermissionLevel() >= Permissions.GUEST) {
            docNameField.setEditable(false);
            docNameField.setEnabled(false);
            ownerField.setEnabled(false);
            deleteButton.setEnabled(false);
            deleteButton.setVisible(false);
            buttonOK.setEnabled(false);
            buttonOK.setVisible(false);
            permissionBox.setEnabled(false);
            descriptionArea.setEnabled(false);
        }
        ComboBoxModel<String> model = new DefaultComboBoxModel<>(permissionList);
        model.setSelectedItem(permissionList[document.getPermissionLevel() - permissionMove]);
        permissionBox.setModel(model);
        docNameField.setText(document.getDocName());
        fileNameField.setText("");
        ownerField.setText(document.getOwner());
        descriptionArea.setText(document.getDescription());

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
        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ClientDocument docToDownload = WebAdapter.downloadDocument(document.getDocName());
                    JFileChooser jFileChooser = new JFileChooser();
                    jFileChooser.setSelectedFile(new File(docToDownload.getFileName()));
                    if (jFileChooser.showSaveDialog(getRootPane()) == JFileChooser.APPROVE_OPTION) {
                        try (FileOutputStream outputStream = new FileOutputStream(jFileChooser.getSelectedFile())) {
                            outputStream.write(docToDownload.getFile());
                        } catch (IOException ex) {
                            throw new FileNotFound();
                        }
                    }
                } catch (IOException | KnifeException ex) {
                    ExceptionProcess.process(getRootPane(), ex);
                }
            }
        });
    }

    private void onOK() {
        // 在此处添加您的代码
        dispose();
    }

    private void onCancel() {
        // 必要时在此处添加您的代码
        dispose();
    }

//    public static void main(String[] args) {
//        main(new ClientUser("aaa"));
//    }

    public static void main(Component parent, ClientUser me, ClientDocument document) {
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
        DocumentManager dialog = new DocumentManager(me, document);
        dialog.setLocationRelativeTo(parent);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
