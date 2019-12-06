package models;

import java.text.SimpleDateFormat;
import java.util.Date;

//目录
public class Folder {
    private String folderName;  //目录名
    private String property;    //属性，本来用于区分目录和文件，本实验中拆分2者
    private String type;       //文件类型
    private boolean hasChild;   //目录下是否有子目录或者文件

    private int diskNum;       //目录所在的磁盘
    private int numOfFAT;
    private String location;       //所在的具体目录
    private String size;
    private String space;
    private Date createTime;
    private boolean isReadOnly;
    private boolean isHide;

    public Folder(String folderName) {
        this.folderName = folderName;
    }

    public Folder(String folderName, String location, int diskNum) {
        this.folderName = folderName;
        this.location = location;
        this.size = "100KB";
        this.space = "100KB";
        this.createTime = new Date();
        this.diskNum = diskNum;
        this.type = "Folder";
        this.isReadOnly = false;
        this.isHide = false;
    }

    public String getFolderName() {
        return this.folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getProperty() {
        return this.property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public int getDiskNum() {
        return this.diskNum;
    }

    public void setDiskNum(int diskNum) {
        this.diskNum = diskNum;
    }

    public boolean isHasChild() {
        return this.hasChild;
    }

    public void setHasChild(boolean hasChild) {
        this.hasChild = hasChild;
    }

    public int getNumOfFAT() {
        return this.numOfFAT;
    }

    public void setNumOfFAT(int numOfFAT) {
        this.numOfFAT = numOfFAT;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSize() {
        return this.size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSpace() {
        return this.space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public String getCreateTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");
        return format.format(this.createTime);
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isReadOnly() {
        return this.isReadOnly;
    }

    public void setReadOnly(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
    }

    public boolean isHide() {
        return this.isHide;
    }

    public void setHide(boolean isHide) {
        this.isHide = isHide;
    }

    public String toString() {
        return this.folderName;
    }
}