package views;
import models.FAT;
import service.FATService;
import util.FileSystemUtil;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import javax.swing.JScrollPane;

import javax.swing.JTable;
import javax.swing.JTextField;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;


public class MainFrame extends JFrame {
    protected JPanel jp1;
    protected JPanel jp2;
    protected JPanel jp3;
    protected JPanel jp4;
    protected JPanel jp5;

    protected JTextField jtf1;

    protected FileTree jtr;

    protected JTable jta;
    protected JTable jta2;
    protected JScrollPane jsp1;
    protected JScrollPane jsp2;

    protected JMenuBar jmb;
    protected JMenu jm;
    protected JMenuItem jmi;


    protected JLabel jl1;
    protected JLabel jl2;
    protected JLabel jl3;

    protected JButton jb1;

    protected BlockTableModel tm;
    protected OpenFileTableModel oftm;
    protected FATService fatService;

    protected int n;
    protected boolean isFile;
    protected Map<String, DefaultMutableTreeNode> map = new HashMap();
    protected List<FAT> fatList = new ArrayList();
    protected int fatIndex = 0;

    public MainFrame() {
        this.initService();
        this.initMainFrame();

        //菜单初始化
        this.jm.add(this.jmi);
        this.jmb.add(this.jm);

        //磁盘分析表初始化
        this.tm = new BlockTableModel();
        this.jta = new JTable(this.tm);
        this.jsp1 = new JScrollPane(this.jta);
        this.jsp1.setPreferredSize(new Dimension(300, 355));

        //打开文件表初始化
        this.oftm = new OpenFileTableModel();
        this.jta2 = new JTable(this.oftm);
        this.jsp2 = new JScrollPane(this.jta2);
        this.jsp2.setPreferredSize(new Dimension(300, 100));

        //帮助按钮绑定点击事件
        this.jb1AddListener();

        //路径框和帮助按钮初始化
        this.jtf1.setPreferredSize(new Dimension(450, 20));
        this.jtf1.setText("C:");
        this.jtf1.addKeyListener(new KeyAdapter(){     //绝对路径寻找文件
            public void keyPressed(KeyEvent e){
                if(e.getKeyChar()==KeyEvent.VK_ENTER ) {  //按回车键执行相应操作;
                    String textpath = jtf1.getText();
                    List<FAT> textFatList = new ArrayList();
                    textFatList=fatService.getFATs(textpath);
                    //System.out.println(map.toString());
                   // System.out.println(textFatList.toString());
                    jtr.jp1.removeAll();
                    if (textFatList.size()!=0) {
                        //System.out.println(1);
                        jtr.addJLabel(textFatList, textpath);

                    }
                    jtr.jp1.updateUI();

                }
            }
        });
        this.jl2 = new JLabel("                   路径：");
        this.jp2.add(this.jl2);
        this.jp2.add(this.jtf1);
        this.jp2.add(this.jb1);

        //文件打开表和磁盘分析表的位置
        this.jl1 = new JLabel("磁盘分析");
        this.jp5.add(this.jl1);
        this.jp5.add(this.jsp1, "Center");

        this.jl3 = new JLabel("已打开文件");
        this.jp5.add(this.jl3);
        this.jp5.add(this.jsp2);

        this.jp2.setLayout(new FlowLayout(0));
        this.jp2.setPreferredSize(new Dimension(1000, 30));
        this.jp5.setPreferredSize(new Dimension(300, 500));
        this.jp1.setLayout(new BorderLayout());
        this.jp1.add(this.jp2, "North");
        this.jp1.add(this.jtr, "West");
        this.jp1.add(this.jp5, "East");

        //主界面设置
        this.setTitle("模拟磁盘文件系统");
        this.setSize(1000, 600);
        this.setLocation(200, 100);
        this.setDefaultCloseOperation(3);
        this.setResizable(false);
        this.setLayout(new BorderLayout());
        this.add(this.jmb, "North");
        this.add(this.jp1, "Center");
        ImageIcon image = new ImageIcon(FileSystemUtil.imgPath);
        this.setIconImage(image.getImage());
        this.setVisible(true);
    }

    //初始化fat表维护
    private void initService() {
        this.fatService = new FATService();
        this.fatService.initFAT();
    }

    //初始化主窗口
    public void initMainFrame() {
        this.jp1 = new JPanel();
        this.jp2 = new JPanel();
        this.jp3 = new JPanel();
        this.jp4 = new JPanel();
        this.jp5 = new JPanel();
        this.jtf1 = new JTextField();

        //文件系统目录的树结构
        this.jtr = new FileTree(this);

        this.jmb = new JMenuBar();
        this.jm = new JMenu("系统");
        this.jmi = new JMenuItem("介绍");
        this.jb1 = new JButton("帮助");
    }

    //响应帮助按钮的鼠标点击事件
    private void jb1AddListener() {
        this.jb1.addMouseListener(new MouseListener() {
            public void mouseReleased(MouseEvent e) {
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseClicked(MouseEvent e) {
                new HelpDialog(MainFrame.this.jp1);
            }
        });
    }


    public static void main(String[] args) {
        //System.out.println(this.getClass().getResource(FileSystemUtil.imgPath));
        new MainFrame();
    }
}

