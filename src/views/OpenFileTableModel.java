package views;

import models.OpenFile;
import models.OpenFiles;
import service.FATService;
import util.FileSystemUtil;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;
//文件打开表显示
public class OpenFileTableModel extends AbstractTableModel {
    private Vector<String> columnNames;
    private Vector<Vector<String>> rowDatas;
    private FATService fatService = new FATService();

    public OpenFileTableModel() {
        this.initData();
    }

    public void initData() {
        //设置属性
        this.columnNames = new Vector();
        this.columnNames.add("文件名称");
        this.columnNames.add("文件路径名");
        this.columnNames.add("文件属性");
        this.columnNames.add("文件起始盘块号");
        this.columnNames.add("文件长度");
        this.columnNames.add("操作类型");
        this.columnNames.add("写指针块号");
        this.columnNames.add("写指针块号地址");


        Vector<String> vc = null;
        this.rowDatas = new Vector();
        OpenFiles openFiles = FATService.getOpenFiles();

        for(int i = 0; i < FileSystemUtil.num; ++i) {
            vc = new Vector();
            if (i < openFiles.getFiles().size()) {
                vc.add((openFiles.getFiles().get(i)).getFile().getFileName());
                vc.add((openFiles.getFiles().get(i)).getFile().getLocation());
                vc.add((openFiles.getFiles().get(i)).getFlag() == FileSystemUtil.flagOnlyRead ? "系统文件" : "普通文件");
                vc.add(String.valueOf((openFiles.getFiles().get(i)).getFile().getDiskNum()));
                vc.add(String.valueOf((openFiles.getFiles().get(i)).getFile().getLength())+"字节");
                vc.add((openFiles.getFiles().get(i)).getFlag() == FileSystemUtil.flagOnlyRead ? "只读" : "写读");
                vc.add(String.valueOf((openFiles.getFiles().get(i)).getWrite().getBnum()));
                vc.add(String.valueOf((openFiles.getFiles().get(i)).getWrite().getDnum()));
            } else {
                vc.add("");
                vc.add("");
                vc.add("");
                vc.add("");
                vc.add("");
                vc.add("");
                vc.add("");
                vc.add("");
            }

            this.rowDatas.add(vc);
        }

    }

    public int getRowCount() {
        return 5;
    }

    public int getColumnCount() {
        return 8;
    }

    public String getColumnName(int column) {
        return (String)this.columnNames.get(column);
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return ((Vector)this.rowDatas.get(rowIndex)).get(columnIndex);
    }
}
