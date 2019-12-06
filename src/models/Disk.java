package models;

//磁盘类
public class Disk {
    private String diskName;

    public Disk(String diskName) {
        this.diskName = diskName;
    }

    public String getDiskName() {
        return this.diskName;
    }

    public void setDiskName(String diskName) {
        this.diskName = diskName;
    }

    public String toString() {
        return this.diskName;
    }
}