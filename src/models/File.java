package models;

//文件的目录项
import java.text.SimpleDateFormat;
import java.util.Date;

public class File {
    private String fileName;   //文件名
    private String type;       //文件类型
    private String property;    //文件属性
    private int diskNum;        //文件起始盘块号
    private int length;           //文件长度（字节数）
    private String content;       //文件内容
    private Folder parent;        //文件的父目录

    private int numOfFAT;           //
    private String location;        //文件路径
    private String size;
    private String space;
    private Date createTime;
    private boolean isReadOnly;
    private boolean isHide;

    public File(String fileName) {
        this.fileName = fileName;
    }

    public File(String fileName, String location, int diskNum) {
        this.fileName = fileName;
        this.location = location;
        this.size = "100KB";
        this.space = "100KB";
        this.diskNum = diskNum;
        this.type = "File";
        this.isReadOnly = false;
        this.isHide = false;
        this.length = 8;
        this.content = "";
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
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

    public int getLength() {
        return this.length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
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
        return this.fileName;
    }
}
