
package views;

import models.*;
import service.FATService;
import util.FileSystemUtil;
import util.MessageUtil;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class FileTree extends JPanel {
    private JTree tree;
    private JScrollPane jsp1;
    private JScrollPane jsp2;

    private JSplitPane jsp;
    protected JPanel jp1;
    private FileJLabel[] jLabel;

    //文件的增删改查，创建文件目录
    private JPopupMenu pm;
    private JPopupMenu pm2;
    private JMenuItem mi1;
    private JMenuItem mi2;
    private JMenuItem mi3;
    private JMenuItem mi4;
    private JMenuItem mi5;
    private JMenuItem mi6;

    private DefaultMutableTreeNode node1;
    protected MainFrame mainFrame;

    public FileTree(MainFrame mainFrame) {

        this.mainFrame=mainFrame;


        this.initMenuItem();
        this.initMenuItenByJLabel();
        this.menuItemAddListener();

        //目录树
        this.initTree();
        this.treeAddListener();
        this.jPanelAddListener();

        this.jp1.setLayout(new FlowLayout(0));
        this.jp1.setBackground(Color.white);
        this.jp1.add(this.pm);

        this.jsp2 = new JScrollPane(this.jp1);
        this.jsp2.setHorizontalScrollBarPolicy(31);
        this.jsp2.setVerticalScrollBarPolicy(22);
        this.jsp2.setPreferredSize(new Dimension(482, 515));
        this.jsp2.setBackground(Color.white);
        this.jsp2.setViewportView(this.jp1);

        this.jsp1.setPreferredSize(new Dimension(200, 515));
        this.jsp = new JSplitPane(1, this.jsp1, this.jsp2);
        this.jsp.setDividerSize(0);
        this.jsp.setDividerLocation(200);
        this.jsp.setEnabled(false);
        this.add(this.jsp);

    }

    //鼠标右建事件，创建文件或目录
    public void initMenuItem() {
        this.pm = new JPopupMenu();
        this.mi1 = new JMenuItem("新建文件");
        this.mi2 = new JMenuItem("新建文件夹");
        this.pm.add(this.mi1);
        this.pm.add(this.mi2);
    }

    //鼠标右建事件菜单，文件的增删改查
    public void initMenuItenByJLabel() {
        this.pm2 = new JPopupMenu();
        this.mi3 = new JMenuItem("打开");
        this.mi4 = new JMenuItem("重命名");
        this.mi5 = new JMenuItem("删除");
        this.mi6 = new JMenuItem("属性");
        this.pm2.add(this.mi3);
        this.pm2.add(this.mi4);
        this.pm2.add(this.mi5);
        this.pm2.add(this.mi6);
    }

    //点击右键显示菜单
    private void jPanelAddListener() {
        this.jp1.addMouseListener(new MouseListener() {
            public void mouseReleased(MouseEvent e) {
            }

            public void mousePressed(MouseEvent e) {
                int mods = e.getModifiers();
                //响应鼠标右键
                if ((mods & 4) != 0) {
                    pm.show(jp1, e.getX(), e.getY());
                }

            }

            public void mouseExited(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseClicked(MouseEvent e) {
            }
        });
    }


    //目录树的第一个磁盘结点"c"
    private void initTree() {
        this.jp1 = new JPanel();

        this.node1 = new DefaultMutableTreeNode(new Disk("C"));
        this.mainFrame.map.put("C:", this.node1);
        this.tree = new JTree(this.node1);

        //动态显示结点目录图标
        this.tree.setCellRenderer(new DefaultTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree tree,
                                                          Object value, boolean selected, boolean expanded,
                                                          boolean isLeaf, int row, boolean focused) {
                Component c = super.getTreeCellRendererComponent(tree, value,
                        selected, expanded, isLeaf, row, focused);
                String path=value.toString();
                if(path.endsWith(".txt")){
                    Icon loadIcon = UIManager.getIcon("FileView.fileIcon");
                    setIcon(loadIcon);
                }else {
                    Icon loadIcon = UIManager.getIcon("FileView.directoryIcon");
                    setIcon(loadIcon);
                }
                return c;
            }
        });

        this.jsp1 = new JScrollPane(this.tree);
    }

    //添加目录点击事件，点击显示目录内容
    private void treeAddListener() {
        this.tree.addMouseListener(new MouseListener() {
            public void mouseReleased(MouseEvent e) {
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseClicked(MouseEvent e) {
                TreePath path = tree.getSelectionPath();
                String[] names= path.toString().replaceAll("]","").split(",");
                String name =names[names.length-1];
                //System.out.println(name);
                if (path != null&&name.endsWith(".txt")==false) {
                    String pathStr = path.toString().replace("[", "").replace("]", "").replace(",", "\\").replace(" ", "").replaceFirst("C", "C:");
                    System.out.println(pathStr);
                    mainFrame.jtf1.setText(pathStr);
                    jp1.removeAll();
                    addJLabel(mainFrame.fatService.getFATs(pathStr), pathStr);
                    jp1.updateUI();
                }

            }
        });
    }

    //文件系统右边的窗口显示
    protected void addJLabel(List<FAT> fats, String path) {
        mainFrame.fatList = fats;
        mainFrame.isFile = true;
        mainFrame.n= fats.size();

        this.jp1.setPreferredSize(new Dimension(482, FileSystemUtil.getHeight(mainFrame.n)));
        this.jLabel = new FileJLabel[mainFrame.n];

        for(int i = 0; i < mainFrame.n; ++i) {
            if ((fats.get(i)).getIndex() == FileSystemUtil.END) {
                if ((fats.get(i)).getType() == FileSystemUtil.FOLDER) {
                    mainFrame.isFile = false;
                    this.jLabel[i] = new FileJLabel(mainFrame.isFile, ((Folder)((FAT)fats.get(i)).getObject()).getFolderName());
                } else {
                    mainFrame.isFile = true;
                    this.jLabel[i] = new FileJLabel(mainFrame.isFile, ((File)((FAT)fats.get(i)).getObject()).getFileName());
                }

                this.jp1.add(this.jLabel[i]);
                this.jLabel[i].add(this.pm2);
                this.jLabel[i].addMouseListener(new MouseListener() {
                    public void mouseReleased(MouseEvent e) {
                    }

                    //右键显示菜单
                    public void mousePressed(MouseEvent e) {
                        for(int j = 0; j < mainFrame.n; ++j) {
                            if (e.getSource() == jLabel[j] && (e.getModifiers() & 4) != 0) {
                                pm2.show(jLabel[j], e.getX(), e.getY());
                            }
                        }

                    }

                    //鼠标离开文件上改变颜色
                    public void mouseExited(MouseEvent e) {
                        for(int j = 0; j < mainFrame.n; ++j) {
                            if (e.getSource() == jLabel[j]) {
                                mainFrame.fatIndex = j;
                                if (jLabel[j].type) {
                                    jLabel[j].setIcon(new ImageIcon(FileSystemUtil.filePath));
                                } else {
                                    jLabel[j].setIcon(new ImageIcon(FileSystemUtil.folderPath));
                                }
                            }
                        }

                    }

                    //鼠标放在文件上改变颜色
                    public void mouseEntered(MouseEvent e) {
                        for(int j = 0; j < mainFrame.n; ++j) {
                            if (e.getSource() == jLabel[j]) {
                                mainFrame.fatIndex = j;
                                if (jLabel[j].type) {
                                    jLabel[j].setIcon(new ImageIcon(FileSystemUtil.file1Path));
                                } else {
                                    jLabel[j].setIcon(new ImageIcon(FileSystemUtil.folder1Path));
                                }
                            }
                        }

                    }

                    //双击打开文件或者目录
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2) {
                            if ((mainFrame.fatList.get(mainFrame.fatIndex)).getType() == FileSystemUtil.FILE) {
                                if (FATService.getOpenFiles().getFiles().size() < FileSystemUtil.num) {
                                    if (mainFrame.fatService.checkOpenFile(mainFrame.fatList.get(mainFrame.fatIndex))) {
                                        MessageUtil.showErrorMgs(jp1, "文件已打开");
                                        return;
                                    }
                                    //更新打开文件表
                                    int flag;
                                    if(((File)mainFrame.fatList.get(mainFrame.fatIndex).getObject()).isReadOnly()==true){
                                        flag=FileSystemUtil.flagOnlyRead;
                                    }else {
                                        flag=FileSystemUtil.flagWrite;
                                    }
                                    OpenFile openFile= mainFrame.fatService.addOpenFile(mainFrame.fatList.get(mainFrame.fatIndex), flag);

                                    mainFrame.oftm.initData();
                                    mainFrame.jta2.updateUI();
                                    //打开文件窗口
                                    new OpenFileJFrame(mainFrame.fatList.get(mainFrame.fatIndex), mainFrame.fatService, mainFrame.oftm, mainFrame.jta2, mainFrame.tm, mainFrame.jta,openFile,mainFrame);
                                } else {
                                    MessageUtil.showErrorMgs(jp1, "已经打开5个文件了，达到上限");
                                }
                            } else {
                                //打开目录
                                Folder folder = (Folder)(mainFrame.fatList.get(mainFrame.fatIndex)).getObject();
                                String path = folder.getLocation() + "\\" + folder.getFolderName();
                                jp1.removeAll();
                                addJLabel(mainFrame.fatService.getFATs(path), path);
                                jp1.updateUI();
                                mainFrame.jtf1.setText(path);
                            }
                        }

                    }
                });
            }
        }

    }

    //鼠标右建点击事件
    public void menuItemAddListener() {

        //创建新的文件
        this.mi1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int index = mainFrame.fatService.createFile(mainFrame.jtf1.getText());
                if (index == FileSystemUtil.ERROR) {
                    MessageUtil.showErrorMgs(jp1, "磁盘已满，无法创建文件");
                } else {

                    FAT fat = FATService.getFAT(index);
                    DefaultMutableTreeNode node = new DefaultMutableTreeNode(((File)fat.getObject()).getFileName()+".txt");
                    mainFrame.map.put(mainFrame.jtf1.getText() + "\\" + ((File)fat.getObject()).getFileName(), node);
                    DefaultMutableTreeNode nodeParent = mainFrame.map.get(mainFrame.jtf1.getText());
                    nodeParent.add(node);

                    tree.updateUI();     //更新目录
                    mainFrame.tm.initData();   //更新磁盘分析表
                    mainFrame.jta.updateUI();  //更新打开文件表
                    jp1.removeAll();
                    addJLabel(mainFrame.fatService.getFATs(mainFrame.jtf1.getText()),mainFrame.jtf1.getText());
                    jp1.updateUI();
                }

            }
        });

        //创建新的目录
        this.mi2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = mainFrame.fatService.createFolder(mainFrame.jtf1.getText());
                if (index == FileSystemUtil.ERROR) {
                    MessageUtil.showErrorMgs(jp1, "磁盘已满，无法创建文件夹");
                } else {
                    FAT fat = FATService.getFAT(index);
                    DefaultMutableTreeNode node = new DefaultMutableTreeNode(((Folder)fat.getObject()).getFolderName());
                    mainFrame.map.put(mainFrame.jtf1.getText() + "\\" + ((Folder)fat.getObject()).getFolderName(), node);
                    DefaultMutableTreeNode nodeParent = mainFrame.map.get(mainFrame.jtf1.getText());
                    nodeParent.add(node);

                    tree.updateUI();
                    mainFrame.tm.initData();
                    mainFrame.jta.updateUI();
                    jp1.removeAll();
                    addJLabel(mainFrame.fatService.getFATs(mainFrame.jtf1.getText()), mainFrame.jtf1.getText());
                    jp1.updateUI();
                }

            }
        });

        //打开文件
        this.mi3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (((FAT)mainFrame.fatList.get(mainFrame.fatIndex)).getType() == FileSystemUtil.FILE) {
                    if (FATService.getOpenFiles().getFiles().size() < FileSystemUtil.num) {
                        if (mainFrame.fatService.checkOpenFile((FAT)mainFrame.fatList.get(mainFrame.fatIndex))) {
                            MessageUtil.showErrorMgs(jp1, "文件已打开");
                            return;
                        }
                        //更新打开文件表
                        int flag;
                        if(((File)mainFrame.fatList.get(mainFrame.fatIndex).getObject()).isReadOnly()==true){
                            flag=FileSystemUtil.flagOnlyRead;
                        }else {
                            flag=FileSystemUtil.flagWrite;
                        }
                        OpenFile openFile= mainFrame.fatService.addOpenFile(mainFrame.fatList.get(mainFrame.fatIndex), flag);

                        mainFrame.oftm.initData();
                        mainFrame.jta2.updateUI();
                        //打开文件窗口
                        new OpenFileJFrame(mainFrame.fatList.get(mainFrame.fatIndex), mainFrame.fatService, mainFrame.oftm, mainFrame.jta2, mainFrame.tm, mainFrame.jta,openFile,mainFrame);

                    } else {
                        MessageUtil.showErrorMgs(jp1, "已经打开5个文件了，达到上限");
                    }
                } else {
                    Folder folder = (Folder)((FAT)mainFrame.fatList.get(mainFrame.fatIndex)).getObject();
                    String path = folder.getLocation() + "\\" + folder.getFolderName();
                    jp1.removeAll();
                    addJLabel(mainFrame.fatService.getFATs(path), path);
                    jp1.updateUI();
                    mainFrame.jtf1.setText(path);
                }

            }
        });

        //重命名文件
        this.mi4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //System.out.println("重命名");
                new ShowRenameDialog(jp1, mainFrame.fatList.get(mainFrame.fatIndex), mainFrame.map, mainFrame.fatService);
                tree.updateUI();
                mainFrame.tm.initData();
                mainFrame.jta.updateUI();
                jp1.removeAll();
                addJLabel(mainFrame.fatService.getFATs(mainFrame.jtf1.getText()), mainFrame.jtf1.getText());
                jp1.updateUI();
            }
        });

        //删除文件
        this.mi5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int i = MessageUtil.showConfirmMgs(jp1, "是否确定要删除该文件？");
                if (i == 0) {
                    mainFrame.fatService.delete(jp1, mainFrame.fatList.get(mainFrame.fatIndex), mainFrame.map);
                    tree.updateUI();
                    mainFrame.tm.initData();
                    mainFrame.jta.updateUI();
                    jp1.removeAll();
                    addJLabel(mainFrame.fatService.getFATs(mainFrame.jtf1.getText()), mainFrame.jtf1.getText());
                    jp1.updateUI();
                }

            }
        });

        //打开文件属性
        this.mi6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //System.out.println("属性");
                new ShowPropertyDialog(jp1, mainFrame.fatList.get(mainFrame.fatIndex));
            }
        });
    }
}
