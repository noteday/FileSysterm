package models;

//用于维护打开文件
public class OpenFile {
    private int flag;       //区分文件权限：只读或读写
    private Pointer read;   //读指针
    private Pointer write;   //写指针
    private File file;

    public OpenFile() {
    }

    public int getFlag() {
        return this.flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public Pointer getRead() {
        return this.read;
    }

    public void setRead(Pointer read) {
        this.read = read;
    }

    public Pointer getWrite() {
        return this.write;
    }

    public void setWrite(Pointer write) {
        this.write = write;
    }

    public File getFile() {
        return this.file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
