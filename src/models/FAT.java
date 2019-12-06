package models;

public class FAT {
    private int index;    //表示文件终结或者下一块的fat
    private int type;     //管理的是文件还是目录
    private Object object;   //文件或者目录对象

    public FAT(int index, int type, Object object) {
        this.index = index;
        this.type = type;
        this.object = object;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getObject() {
        return this.object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
