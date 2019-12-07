package views;

import models.FAT;
import models.File;
import models.OpenFile;
import service.FATService;
import util.FileSystemUtil;
import util.MessageUtil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableModel;

//打开的文件窗口
public class OpenFileJFrame extends JFrame {
    private MainFrame mainFrame;
    private JTextArea jta1;
    private JMenuBar jmb;
    private JMenu jm;
    private JMenuItem jmi1;
    private JMenuItem jmi2;
    private FAT fat;
    private File file;
    private String oldContent;
    private int length;
    private OpenFile openFile;
    private FATService fatService;
    private OpenFileTableModel oftm;
    private JTable jt;
    private JFrame jf = this;
    private BlockTableModel tm;
    private JTable jta;
    private boolean canClose = true;

    public OpenFileJFrame(FAT fat, FATService fatService, OpenFileTableModel oftm, JTable jt, BlockTableModel tm, JTable jta, OpenFile openFile,MainFrame mainFrame) {
        this.fat = fat;
        this.fatService = fatService;
        this.oftm = oftm;
        this.jt = jt;
        this.tm = tm;
        this.jta = jta;
        this.openFile=openFile;
        this.mainFrame=mainFrame;

        this.file = (File)fat.getObject(); //打开的文件

        //菜单
        this.jmb = new JMenuBar();
        this.jm = new JMenu("操作");
        this.jmi1 = new JMenuItem("保存");
        this.jmi2 = new JMenuItem("退出");
        this.jmb.add(this.jm);
        this.jm.add(this.jmi1);
        this.jm.add(this.jmi2);

        //内容显示
        this.jta1 = new JTextArea();
        this.oldContent = this.file.getContent();
        this.jta1.setText(this.oldContent);

        //设置窗口大小
        this.init();
        //监听菜单函数
        this.menuItemAddListener();
    }

    private void init() {
        this.setResizable(false);
        this.setSize(600, 500);
        this.setTitle(this.file.getFileName());
        this.setLocation(200, 150);
        this.add(this.jmb, "North");
        this.add(this.jta1);
        this.addWindowListener(new OpenFileJFrame.WindowClosingListener());
        this.setVisible(true);
    }

    //监听菜单事件
    private void menuItemAddListener() {
        //保存
        this.jmi1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                OpenFileJFrame.this.save();
            }
        });

        //退出
        this.jmi2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                OpenFileJFrame.this.jf.setVisible(false);
                //修改打开文件表
                OpenFileJFrame.this.fatService.removeOpenFile(OpenFileJFrame.this.fat);
                OpenFileJFrame.this.oftm.initData();

                OpenFileJFrame.this.jt.updateUI();
            }
        });
    }

    private void save() {
        this.length = this.jta1.getText().length();
        if (this.length > ((File)this.fat.getObject()).getLength() - 8) {
            //得到修改后的文件占用物理块数，大于1则获取新的物理块
            int num = FileSystemUtil.getNumOfFAT(this.length+8);
            if (num > 1) {
                boolean boo = this.fatService.saveToModifyFATS2(this, num, this.fat);
                if (boo) {
                    this.file.setLength(this.length+8);
                    this.file.setContent(this.jta1.getText());
                }
            } else {
                this.file.setLength(this.length+8);
                this.file.setContent(this.jta1.getText());
            }

            this.tm.initData();
            this.jta.updateUI();

            FATService.getOpenFiles().removeFile(this.openFile);
            int flag;
            if(((File)this.fat.getObject()).isReadOnly()==true){
                flag=FileSystemUtil.flagOnlyRead;
            }else {
                flag=FileSystemUtil.flagWrite;
            }
            this.openFile=this.fatService.addOpenFile(this.fat,flag);
            this.oftm.initData();
            this.mainFrame.jta2.updateUI();
        }

    }

    //关闭窗口询问是否保存已修改
    class WindowClosingListener extends WindowAdapter {
        WindowClosingListener() {
        }

        public void windowClosing(WindowEvent e) {
            if (!OpenFileJFrame.this.jta1.getText().equals(OpenFileJFrame.this.file.getContent())) {
                int ret = MessageUtil.showConfirmMgs(OpenFileJFrame.this.jf, "还没有保存,是否保存");
                if (ret == 0) {
                    OpenFileJFrame.this.save();
                }

                OpenFileJFrame.this.fatService.removeOpenFile(OpenFileJFrame.this.fat);
                OpenFileJFrame.this.oftm.initData();
                OpenFileJFrame.this.jt.updateUI();
            }

            OpenFileJFrame.this.fatService.removeOpenFile(OpenFileJFrame.this.fat);
            OpenFileJFrame.this.oftm.initData();
            OpenFileJFrame.this.jt.updateUI();
        }
    }
}
