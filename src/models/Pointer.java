package models;

//指针类
public class Pointer {
    private int dnum;  //磁盘盘块号
    private int bnum;       //磁盘盘块内第几个字节

    public Pointer() {
    }

    public int getDnum() {
        return this.dnum;
    }

    public void setDnum(int dnum) {
        this.dnum = dnum;
    }

    public int getBnum() {
        return this.bnum;
    }

    public void setBnum(int bnum) {
        this.bnum = bnum;
    }
}
