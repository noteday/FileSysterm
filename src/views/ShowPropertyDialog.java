package views;

import models.FAT;
import models.File;
import models.Folder;
import util.FileSystemUtil;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
//文件属性窗口
public class ShowPropertyDialog extends JDialog {
    private JPanel jp1;
    private JLabel jl1;
    private JLabel jl2;
    private JLabel jl3;
    private JLabel jl4;
    private JLabel jl5;
    private JLabel jl6;
    private FAT fat;
    private JButton jb1;
    private JButton jb2;
    private JButton jb3;
    private JRadioButton jrb1;
    private JRadioButton jrb2;
    private boolean isFile = false;
    private JDialog jd1 = this;

    public ShowPropertyDialog(Component c, FAT f) {
        this.fat = f;
        this.init();
        this.setTitle("属性");
        this.setSize(280, 330);
        this.setLayout(new FlowLayout());
        this.setModal(true);
        this.setLocationRelativeTo(c);
        this.setVisible(true);
    }

    private void init() {
        if (this.fat.getType() == FileSystemUtil.FOLDER) {
            Folder folder = (Folder)this.fat.getObject();
            this.jl1 = new JLabel("名字 :                        " + folder.getFolderName());
            this.jl2 = new JLabel("类型 :                        " + folder.getType());
            this.jl3 = new JLabel("路径 :                        " + folder.getLocation());
            this.jl4 = new JLabel("占用空间大小:           " + folder.getSize());
            this.jl5 = new JLabel("创建日期: " + folder.getCreateTime());
            this.jl6 = new JLabel("属性");
            this.jrb1 = new JRadioButton("只读");
            this.jrb2 = new JRadioButton("隐藏");
            if (folder.isReadOnly()) {
                this.jrb1.setSelected(true);
            }

            if (folder.isHide()) {
                this.jrb2.setSelected(true);
            }
        } else if (this.fat.getType() == FileSystemUtil.FILE) {
            this.isFile = true;
            File file = (File)this.fat.getObject();
            this.jl1 = new JLabel("名字 :                        " + file.getFileName());
            this.jl2 = new JLabel("类型 :                        " + file.getType());
            this.jl3 = new JLabel("路径 :                        " + file.getLocation());
            this.jl4 = new JLabel("占用空间大小:            " + file.getSize());
            this.jl5 = new JLabel("创建日期：" + file.getCreateTime());
            this.jl6 = new JLabel("属性");
            this.jrb1 = new JRadioButton("只读");
            this.jrb2 = new JRadioButton("隐藏");
            if (file.isReadOnly()) {
                this.jrb1.setSelected(true);
            }

            if (file.isHide()) {
                this.jrb2.setSelected(true);
            }
        }

        this.jl1.setPreferredSize(new Dimension(230, 40));
        this.jl2.setPreferredSize(new Dimension(230, 40));
        this.jl3.setPreferredSize(new Dimension(230, 40));
        this.jl4.setPreferredSize(new Dimension(230, 40));
        this.jl5.setPreferredSize(new Dimension(230, 40));
        this.add(this.jl1);
        this.add(this.jl2);
        this.add(this.jl3);
        this.add(this.jl4);
        this.add(this.jl5);
        this.add(this.jl6);
        this.add(this.jrb1);
        this.add(this.jrb2);
        this.jp1 = new JPanel();
        this.jb1 = new JButton("确定");
        this.jb2 = new JButton("取消");
        this.jb3 = new JButton("应用");
        this.jp1.add(this.jb1);
        this.jp1.add(this.jb2);
        this.jp1.add(this.jb3);
        this.add(this.jp1);
        this.jb1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (ShowPropertyDialog.this.jrb1.isSelected()) {
                    if (ShowPropertyDialog.this.isFile) {
                        ((File)ShowPropertyDialog.this.fat.getObject()).setReadOnly(true);
                    } else {
                        ((Folder)ShowPropertyDialog.this.fat.getObject()).setReadOnly(true);
                    }
                } else if (ShowPropertyDialog.this.isFile) {
                    ((File)ShowPropertyDialog.this.fat.getObject()).setReadOnly(false);
                } else {
                    ((Folder)ShowPropertyDialog.this.fat.getObject()).setReadOnly(false);
                }

                if (ShowPropertyDialog.this.jrb2.isSelected()) {
                    if (ShowPropertyDialog.this.isFile) {
                        ((File)ShowPropertyDialog.this.fat.getObject()).setHide(true);
                    } else {
                        ((Folder)ShowPropertyDialog.this.fat.getObject()).setHide(true);
                    }
                } else if (ShowPropertyDialog.this.isFile) {
                    ((File)ShowPropertyDialog.this.fat.getObject()).setHide(false);
                } else {
                    ((Folder)ShowPropertyDialog.this.fat.getObject()).setHide(false);
                }

                ShowPropertyDialog.this.jd1.setVisible(false);
            }
        });
        this.jb2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ShowPropertyDialog.this.jd1.setVisible(false);
            }
        });
        this.jb3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (ShowPropertyDialog.this.jrb1.isSelected()) {
                    if (ShowPropertyDialog.this.isFile) {
                        ((File)ShowPropertyDialog.this.fat.getObject()).setReadOnly(true);
                    } else {
                        ((Folder)ShowPropertyDialog.this.fat.getObject()).setReadOnly(true);
                    }
                } else if (ShowPropertyDialog.this.isFile) {
                    ((File)ShowPropertyDialog.this.fat.getObject()).setReadOnly(false);
                } else {
                    ((Folder)ShowPropertyDialog.this.fat.getObject()).setReadOnly(false);
                }

                if (ShowPropertyDialog.this.jrb2.isSelected()) {
                    if (ShowPropertyDialog.this.isFile) {
                        ((File)ShowPropertyDialog.this.fat.getObject()).setHide(true);
                    } else {
                        ((Folder)ShowPropertyDialog.this.fat.getObject()).setHide(true);
                    }
                } else if (ShowPropertyDialog.this.isFile) {
                    ((File)ShowPropertyDialog.this.fat.getObject()).setHide(false);
                } else {
                    ((Folder)ShowPropertyDialog.this.fat.getObject()).setHide(false);
                }

            }
        });
    }
}
