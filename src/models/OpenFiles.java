package models;

import util.FileSystemUtil;

import java.util.ArrayList;
import java.util.List;

//用于维护打开文件列表
public class OpenFiles {
    private List<OpenFile> files;
    private int length;

    public OpenFiles() {
        //限定打开文件数
        this.files = new ArrayList(FileSystemUtil.num);
        this.length = 0;
    }

    public void addFile(OpenFile openFile) {
        this.files.add(openFile);
    }

    public void removeFile(OpenFile openFile) {
        this.files.remove(openFile);
    }

    public List<OpenFile> getFiles() {
        return this.files;
    }

    public void setFiles(List<OpenFile> files) {
        this.files = files;
    }

    public int getLength() {
        return this.length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
