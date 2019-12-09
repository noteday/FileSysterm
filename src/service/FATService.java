package service;

import models.*;
import util.FileSystemUtil;
import util.MessageUtil;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;


//用于维护文件分配列表和打开文件列表

public class FATService {
    private static FAT[] myFAT;
    private static OpenFiles openFiles;

    public FATService() {
        openFiles = new OpenFiles();
    }
    public void initFAT() {
        myFAT = new FAT[128];          //本实验只有一个c盘，128个物理块
        myFAT[0] = new FAT(FileSystemUtil.END, FileSystemUtil.DISK, (Object)null);    //前两个块用来存储FAT
        myFAT[1] = new FAT(FileSystemUtil.END, FileSystemUtil.DISK, new Disk("C"));
    }


    //增加打开文件
    public OpenFile addOpenFile(FAT fat, int flag) {
        OpenFile openFile = new OpenFile();
        openFile.setFile((File)fat.getObject());
        openFile.setFlag(flag);
        openFile.setWrite(getWritePoint(new Pointer(),fat));
        openFiles.addFile(openFile);
        return openFile;
    }
    //查找写指针
    public Pointer getWritePoint(Pointer write,FAT fat) {
        int begin = ((File) fat.getObject()).getDiskNum();
        int index = myFAT[begin].getIndex();

        int oldNum;
        //找出文件原本所占有的物理块
        for (oldNum = 1; index != FileSystemUtil.END; index = myFAT[index].getIndex()) {
            ++oldNum;
            if (myFAT[index].getIndex() == FileSystemUtil.END) {  //最后一块物理块下标
                begin = index;
            }
        }
        write.setBnum(oldNum+((File) fat.getObject()).getDiskNum()-1);
        write.setDnum(((File) fat.getObject()).getLength()-(oldNum-1)*64);
        return write;
    }


    //文件关闭的时候减少打开文件列表
    public void removeOpenFile(FAT fat) {
        for(int i = 0; i < openFiles.getFiles().size(); ++i) {
            if ((openFiles.getFiles().get(i)).getFile() == (File)fat.getObject()) {
                openFiles.getFiles().remove(i);
            }
        }

    }

    //检查某文件是否被打开
    public boolean checkOpenFile(FAT fat) {
        for(int i = 0; i < openFiles.getFiles().size(); ++i) {
            if ((openFiles.getFiles().get(i)).getFile() == fat.getObject()) {
                return true;
            }
        }

        return false;
    }


    //创建一个目录
    public int createFolder(String path) {
        String folderName = null;
        boolean canName = true;
        int index = 1;

        int index2;
        Folder folder;

        do {
            folderName = "新建文件夹";
            canName = true;
            folderName = folderName + index;

            //根据路径查找所有文件，避免命名重复和定义正确的默认命名
            for(index2 = 0; index2 < myFAT.length; ++index2) {
                if (myFAT[index2] != null && myFAT[index2].getType() == FileSystemUtil.FOLDER) {
                    folder = (Folder)myFAT[index2].getObject();
                    if (path.equals(folder.getLocation()) && folderName.equals(folder.getFolderName())) {
                        canName = false;
                    }
                }
            }

            ++index;
        } while(!canName);

        //查找到空闲的物理块
        index2 = this.searchEmptyFromMyFAT();
        if (index2 == FileSystemUtil.ERROR) {
            return FileSystemUtil.ERROR;
        } else {
            folder = new Folder(folderName, path, index2);
            myFAT[index2] = new FAT(FileSystemUtil.END, FileSystemUtil.FOLDER, folder);
            return index2;
        }
    }

    public int createFile(String path) {
        String fileName = null;
        boolean canName = true;
        int index = 1;

        int index2;
        File file;
        do {
            fileName = "新建文件";
            canName = true;
            fileName = fileName + index;

            for(index2 = 0; index2 < myFAT.length; ++index2) {
                if (myFAT[index2] != null && myFAT[index2].getType() == FileSystemUtil.FILE) {
                    file = (File)myFAT[index2].getObject();
                    if (path.equals(file.getLocation()) && fileName.equals(file.getFileName())) {
                        canName = false;
                    }
                }
            }

            ++index;
        } while(!canName);

        index2 = this.searchEmptyFromMyFAT();
        if (index2 == FileSystemUtil.ERROR) {
            return FileSystemUtil.ERROR;
        } else {
            file = new File(fileName, path, index2);
            myFAT[index2] = new FAT(FileSystemUtil.END, FileSystemUtil.FILE, file);
            return index2;
        }
    }

    //寻找空闲的物理块
    public int searchEmptyFromMyFAT() {
        for(int i = 2; i < myFAT.length; ++i) {
            if (myFAT[i] == null) {
                return i;
            }
        }

        return FileSystemUtil.ERROR;
    }

    public int getNumOfFAT() {
        int n = 0;

        for(int i = 2; i < myFAT.length; ++i) {
            if (myFAT[i] != null) {
                ++n;
            }
        }

        return n;
    }

    public int getSpaceOfFAT() {
        int n = 0;

        for(int i = 2; i < myFAT.length; ++i) {
            if (myFAT[i] == null) {
                ++n;
            }
        }

        return n;
    }

    //根据num的值为文件分配新的物理块
    public boolean saveToModifyFATS2(Component parent, int num, FAT fat) {
        int begin = ((File)fat.getObject()).getDiskNum();
        int index = myFAT[begin].getIndex();

        int oldNum;
        //找出文件原本所占有的物理块，比较是否需要新的物理块
        for(oldNum = 1; index != FileSystemUtil.END; index = myFAT[index].getIndex()) {
            ++oldNum;
            if (myFAT[index].getIndex() == FileSystemUtil.END) {  //最后一块物理块下标
                begin = index;
            }
        }

        if (num > oldNum) {
            int n = num - oldNum;
            if (this.getSpaceOfFAT() < n) {
                MessageUtil.showErrorMgs(parent, "保存的内容已经超过磁盘的容量");
                return false;
            } else {
                int space = this.searchEmptyFromMyFAT();
                myFAT[begin].setIndex(space);

                //分配和连接物理块
                for(int i = 1; i <= n; ++i) {
                    space = this.searchEmptyFromMyFAT();
                    if (i == n) {
                        myFAT[space] = new FAT(255, FileSystemUtil.FILE, (File)fat.getObject());
                    } else {
                        myFAT[space] = new FAT(20, FileSystemUtil.FILE, (File)fat.getObject());
                        int space2 = this.searchEmptyFromMyFAT();
                        myFAT[space].setIndex(space2);
                    }
                }

                return true;
            }
        } else {
            return true;
        }
    }

    //根据num的值减少文件非配的物理块
    public boolean saveToModifyFATS1(Component parent, int num, FAT fat) {
        int begin = ((File)fat.getObject()).getDiskNum();
        int index = myFAT[begin].getIndex();

        int oldNum;
        //找出文件原本所占有的物理块，比较是否需要新的物理块
        for(oldNum = 1; index != FileSystemUtil.END; index = myFAT[index].getIndex()) {
            ++oldNum;
            if (myFAT[index].getIndex() == FileSystemUtil.END) {  //最后一块物理块下标
                begin = index;
            }
        }
        index=((File)fat.getObject()).getDiskNum();
        if (num < oldNum) {
            for(int i = 1; i <= oldNum; ++i) {
                if(i>num){
                    int temp = myFAT[index].getIndex();
                    myFAT[index]=null;
                    index=temp;
                }else if(i==num){
                    int temp = myFAT[index].getIndex();
                    myFAT[index].setIndex(255);
                    index=temp;

                }else {
                    index=myFAT[index].getIndex();
                }
            }
            return true;
        }
        return true;
    }

    public List<Folder> getFolders(String path) {
        List<Folder> list = new ArrayList();

        for(int i = 0; i < myFAT.length; ++i) {
            if (myFAT[i] != null && myFAT[i].getObject() instanceof Folder && ((Folder)myFAT[i].getObject()).getLocation().equals(path)) {
                list.add((Folder)myFAT[i].getObject());
            }
        }

        return list;
    }

    public List<File> getFiles(String path) {
        List<File> list = new ArrayList();

        for(int i = 0; i < myFAT.length; ++i) {
            if (myFAT[i] != null && myFAT[i].getObject() instanceof File && ((File)myFAT[i].getObject()).getLocation().equals(path)) {
                list.add((File)myFAT[i].getObject());
            }
        }

        return list;
    }

    //根据路径返回所有该路径下的文件和目录所在的fat
    public List<FAT> getFATs(String path) {
        List<FAT> fats = new ArrayList();

        int i;
        for(i = 0; i < myFAT.length; ++i) {
            if (myFAT[i] != null && myFAT[i].getObject() instanceof Folder && ((Folder)myFAT[i].getObject()).getLocation().equals(path) && myFAT[i].getIndex() == FileSystemUtil.END) {
                fats.add(myFAT[i]);
            }
        }

        for(i = 0; i < myFAT.length; ++i) {
            if (myFAT[i] != null && myFAT[i].getObject() instanceof File && ((File)myFAT[i].getObject()).getLocation().equals(path) && myFAT[i].getIndex() == FileSystemUtil.END) {
                fats.add(myFAT[i]);
            }
        }

        return fats;
    }

    //修改名程，查找包含该文件的所有路径，并替换
    public void modifyLocation(String oldPath, String newPath) {
        for(int i = 0; i < myFAT.length; ++i) {
            if (myFAT[i] != null) {
                if (myFAT[i].getType() == FileSystemUtil.FILE) {
                    if (((File)myFAT[i].getObject()).getLocation().contains(oldPath)) {
                        ((File)myFAT[i].getObject()).setLocation(((File)myFAT[i].getObject()).getLocation().replace(oldPath, newPath));
                    }
                } else if (myFAT[i].getType() == FileSystemUtil.FOLDER && ((Folder)myFAT[i].getObject()).getLocation().contains(oldPath)) {
                    ((Folder)myFAT[i].getObject()).setLocation(((Folder)myFAT[i].getObject()).getLocation().replace(oldPath, newPath));
                }
            }
        }

    }


    //删除文件或目录
    public void delete(JPanel jp1, FAT fat, Map<String, DefaultMutableTreeNode> map) {
        if (fat.getType() == FileSystemUtil.FILE) {
            int i;
            for(i = 0; i < openFiles.getFiles().size(); ++i) {
                if (((OpenFile)openFiles.getFiles().get(i)).getFile().equals(fat.getObject())) {
                    MessageUtil.showErrorMgs(jp1, "文件正打开着，不能删除");
                    return;
                }
            }

            for(i = 0; i < myFAT.length; ++i) {
                if (myFAT[i] != null && myFAT[i].getType() == FileSystemUtil.FILE && ((File)myFAT[i].getObject()).equals((File)fat.getObject())) {
                    myFAT[i] = null;
                }
            }
            String path = ((File)fat.getObject()).getLocation();
            String folderPath = ((File)fat.getObject()).getLocation() + "\\" + ((File)fat.getObject()).getFileName();

            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)map.get(path);
            parentNode.remove(map.get(folderPath));
            map.remove(folderPath);
        } else {
            String path = ((Folder)fat.getObject()).getLocation();
            String folderPath = ((Folder)fat.getObject()).getLocation() + "\\" + ((Folder)fat.getObject()).getFolderName();
            System.out.println("路径：" + folderPath);
            int index = 0;

            for(int i = 2; i < myFAT.length; ++i) {
                if (myFAT[i] != null) {
                    Object obj = myFAT[i].getObject();
                    if (myFAT[i].getType() == FileSystemUtil.FOLDER) {
                        if (((Folder)obj).getLocation().equals(folderPath)) {
                            MessageUtil.showErrorMgs(jp1, "文件夹不为空，不能删除");
                            return;
                        }
                    } else if (((File)obj).getLocation().equals(folderPath)) {
                        MessageUtil.showErrorMgs(jp1, "文件夹不为空，不能删除");
                        return;
                    }

                    if (myFAT[i].getType() == FileSystemUtil.FOLDER && ((Folder)myFAT[i].getObject()).equals((Folder)fat.getObject())) {
                        index = i;
                    }
                }
            }

            myFAT[index] = null;
            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)map.get(path);
            parentNode.remove(map.get(folderPath));
            map.remove(folderPath);
        }

    }

    public static FAT[] getMyFAT() {
        return myFAT;
    }

    public static void setMyFAT(FAT[] myFAT) {
        FATService.myFAT = myFAT;
    }

    public static FAT getFAT(int index) {
        return myFAT[index];
    }

    public static OpenFiles getOpenFiles() {
        return openFiles;
    }

    public static void setOpenFiles(OpenFiles openFiles) {
        FATService.openFiles = openFiles;
    }
}
