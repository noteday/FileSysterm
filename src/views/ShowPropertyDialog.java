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
//文件属性窗口a
public class ShowPropertyDialog extends JDialog {
    private JPanel jp1;
    private JLabel jl1;
    private JLabel jl2;
    private JLabel jl3;
    private JLabel jl4;
    private JLabel jl5;
    private FAT fat;
    private JButton jb1;
    private JButton jb2;
    private JRadioButton jrb1;
    private JRadioButton jrb2;
    private boolean isFile = false;
    private JDialog jd1 = this;

    public ShowPropertyDialog(Component c, FAT f) {
        this.fat = f;
        this.init();
        this.setTitle("属性");
        this.setSize(290, 350);
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
            this.jl4 = new JLabel("占用空间大小:           " + folder.getLength()+"字节");
            this.jl5 = new JLabel("创建日期: " + folder.getCreateTime());
            this.jrb1 = new JRadioButton("只读");
            this.jrb2 = new JRadioButton("读写");
            if (folder.isReadOnly()) {
                System.out.println(2);
                this.jrb1.setSelected(true);
            }else {
                this.jrb2.setSelected(true);
            }
        } else if (this.fat.getType() == FileSystemUtil.FILE) {
            this.isFile = true;
            File file = (File)this.fat.getObject();
            this.jl1 = new JLabel("名字 :                        " + file.getFileName());
            this.jl2 = new JLabel("类型 :                        " + file.getType());
            this.jl3 = new JLabel("路径 :                        " + file.getLocation());
            this.jl4 = new JLabel("占用空间大小:            " + file.getLength()+"字节");
            this.jl5 = new JLabel("创建日期：" + file.getCreateTime());
            this.jrb1 = new JRadioButton("只读");
            this.jrb2 = new JRadioButton("读写");
            if (file.isReadOnly()) {
                System.out.println(2);
                this.jrb1.setSelected(true);
            }else {
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
        this.add(this.jrb1);
        this.add(this.jrb2);
        this.jp1 = new JPanel();
        this.jb1 = new JButton("确定");
        this.jb2 = new JButton("取消");
        this.jp1.add(this.jb1);
        this.jp1.add(this.jb2);
        this.add(this.jp1);
        this.jb1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (jrb1.isSelected()) {
                    if (isFile) {
                        System.out.println(1);
                        ((File)fat.getObject()).setReadOnly(true);
                    } else {
                        ((Folder)fat.getObject()).setReadOnly(true);
                    }
                } else if (isFile) {
                    ((File)fat.getObject()).setReadOnly(false);
                } else {
                    ((Folder)fat.getObject()).setReadOnly(false);
                }

                if (jrb2.isSelected()) {
                    if (isFile) {
                        ((File)fat.getObject()).setReadOnly(false);
                    } else {
                        ((Folder)fat.getObject()).setReadOnly(false);
                    }
                } else if (isFile) {
                    ((File)fat.getObject()).setReadOnly(true);
                } else {
                    ((Folder)fat.getObject()).setReadOnly(true);
                }

                jd1.setVisible(false);
            }
        });
        this.jb2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jd1.setVisible(false);
            }
        });

    }
}
