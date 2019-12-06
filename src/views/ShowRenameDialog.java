package views;
import models.FAT;
import models.File;
import models.Folder;
import service.FATService;
import util.FileSystemUtil;
import util.MessageUtil;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;


//改名字的窗口显示
public class ShowRenameDialog {
    private FAT fat;
    private boolean isFile = false;
    private Component com;
    String oldName = "";
    String rename = "";
    String oldPath = "";
    String path = "";
    Map<String, DefaultMutableTreeNode> map;
    private FATService fatService;

    public ShowRenameDialog(Component c, FAT f, Map<String, DefaultMutableTreeNode> m, FATService fatService) {
        this.fatService = fatService;
        this.map = m;
        this.fat = f;
        this.com = c;
        this.init();
    }

    private void init() {
        String path1;
        if (this.fat.getType() == FileSystemUtil.FILE) {
            this.isFile = true;
            File file = (File)this.fat.getObject();
            this.oldName = file.getFileName();
            this.oldPath = file.getLocation() + "\\" + this.oldName;
            this.path = file.getLocation();
            //得到新的名称
            this.rename = JOptionPane.showInputDialog(this.com, "请输入名称", this.oldName);
            //检查名称是否重复和是否包含非法字符
            if (this.rename != null && this.rename != "" && !this.rename.equals(this.oldName)) {
                path1 = ((File)this.fat.getObject()).getLocation() + "\\" + this.rename;
                if (this.checkHasName(path1, this.isFile)) {
                    MessageUtil.showErrorMgs(this.com, "已有该名字的文件了");
                    return;
                }

                if (this.rename.contains("$") || this.rename.contains(".") || this.rename.contains("/")) {
                    MessageUtil.showErrorMgs(this.com, "文件名包含\"$\",\".\",\"/\" 字符");
                    return;
                }

                ((File)this.fat.getObject()).setFileName(this.rename);
            }
        } else {
            this.isFile = false;
            Folder folder = (Folder)this.fat.getObject();
            this.oldName = folder.getFolderName();
            this.oldPath = folder.getLocation() + "\\" + this.oldName;
            this.path = folder.getLocation();
            this.rename = JOptionPane.showInputDialog(this.com, "请输入名称", this.oldName);
            if (this.rename != null && this.rename != "" && !this.rename.equals(this.oldName)) {
                path1 = ((Folder)this.fat.getObject()).getLocation() + "\\" + this.rename;
                if (this.checkHasName(path1, this.isFile)) {
                    MessageUtil.showErrorMgs(this.com, "已有该名字的文件夹了");
                    return;
                }

                if (this.rename.contains("$") || this.rename.contains(".") || this.rename.contains("/")) {
                    MessageUtil.showErrorMgs(this.com, "文件名包含\"$\",\".\",\"/\" 字符");
                    return;
                }

                ((Folder)this.fat.getObject()).setFolderName(this.rename);
            }
        }

        String newPath = this.path + "\\" + this.rename;

        //修改目录树名称
        this.fatService.modifyLocation(this.oldPath, newPath);
        Set<String> set = this.map.keySet();
        List<String> setStr = new ArrayList();
        setStr.addAll(set);
        Iterator var5 = setStr.iterator();
       // System.out.println(this.map.toString());
        while(var5.hasNext()) {
            String s = (String)var5.next();
            if (s.contains(this.oldPath)) {
                DefaultMutableTreeNode n = this.map.get(s);
                String newPaths = s.replace(this.oldPath, newPath);
                if(this.isFile==false){
                    n.setUserObject(rename);
                }else{
                    n.setUserObject(rename+".txt");
                }
                this.map.remove(s);
                this.map.put(newPaths, n);
            }
        }
        //System.out.println(this.map.toString());

    }


    //检查是否重名
    public boolean checkHasName(String path1, boolean isFile1) {
        for(int i = 2; i < FATService.getMyFAT().length; ++i) {
            if (FATService.getMyFAT()[i] != null) {
                String path2;
                if (isFile1) {
                    if (FATService.getMyFAT()[i].getType() == FileSystemUtil.FILE) {
                        path2 = ((File)FATService.getMyFAT()[i].getObject()).getLocation() + "\\" + ((File)FATService.getMyFAT()[i].getObject()).getFileName();
                        if (path2.equals(path1)) {
                            return true;
                        }
                    }
                } else if (FATService.getMyFAT()[i].getType() == FileSystemUtil.FOLDER) {
                    path2 = ((Folder)FATService.getMyFAT()[i].getObject()).getLocation() + "\\" + ((Folder)FATService.getMyFAT()[i].getObject()).getFolderName();
                    if (path2.equals(path1)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
