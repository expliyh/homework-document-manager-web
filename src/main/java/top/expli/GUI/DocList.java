package top.expli.GUI;

import com.formdev.flatlaf.FlatIntelliJLaf;
import top.expli.ClientUser;
import top.expli.ExceptionProcess;
import top.expli.exceptions.*;
import top.expli.webapi.WebAdapter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class DocList {
    private JFrame thisFrame;
    private JButton exitButton;
    private JTable docTable;
    private JButton searchButton;
    private JTextField searchText;
    private JCheckBox isCaps;
    private JPanel panel;
    private JButton uploadButton;

    protected ClientUser me;

    public DocList(JFrame thisFrame, ClientUser me) {
        this.thisFrame = thisFrame;
        this.me = me;
        if (me.getPermissionLevel() > 4) {
            uploadButton.setVisible(false);
        }
        this.refresh();
        docTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() >= 2) {
                    try {
                        DocumentManager.main(thisFrame,me,WebAdapter.getDocumentInfo((String) docTable.getValueAt(docTable.getSelectedRow(),0)));
                    } catch (IOException | KnifeException ex) {
                        ExceptionProcess.process(thisFrame,ex);
                    }
                    refresh();
                }
            }
        });
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                JFileChooser jFileChooser = new JFileChooser();
//                if (jFileChooser.showOpenDialog(thisFrame) == JFileChooser.APPROVE_OPTION) {
//                    try {
//                        Documents.addDocument(jFileChooser.getSelectedFile(), userName, cache_user.GetPermissionLevel(userName));
//                    } catch (KnifeException knifeException) {
//                        ExceptionProcess.process(thisFrame, knifeException);
//                    }
//                }
                UploadDialog.main(me, thisFrame);
                refresh();
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thisFrame.dispose();
            }
        });
    }

    public void refresh() {
        Vector<Vector<String>> list;
        try {
            list = WebAdapter.getDocumentList();
        } catch (IOException | KnifeException e) {
            ExceptionProcess.process(thisFrame, e);
            thisFrame.dispose();
            return;
        }

        Vector<String> head = new Vector<>(4);
        head.add("文档名");
        head.add("作者");
        head.add("权限");
        head.add("简介");
        DefaultTableModel docModel = new DefaultTableModel(list, head) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        docTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        docTable.setModel(docModel);
        docTable.getColumn(head.get(1)).setWidth(50);
    }

//    public static void main(String[] args) {
//        main(args, "admin");
//    }

    public static void main(ClientUser me) {
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (UnsupportedLookAndFeelException e) {
            System.out.println("主题设置失败！");
        }
        JFrame frame = new JFrame("DocList");
        frame.setContentPane(new DocList(frame, me).panel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
