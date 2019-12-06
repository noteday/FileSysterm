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
        this.columnNames.add("文件打开方式");
        this.columnNames.add("文件起始盘块号");
        this.columnNames.add("文件路径");
        Vector<String> vc = null;
        this.rowDatas = new Vector();
        OpenFiles openFiles = FATService.getOpenFiles();

        for(int i = 0; i < FileSystemUtil.num; ++i) {
            vc = new Vector();
            if (i < openFiles.getFiles().size()) {
                vc.add(((OpenFile)openFiles.getFiles().get(i)).getFile().getFileName());
                vc.add(((OpenFile)openFiles.getFiles().get(i)).getFlag() == FileSystemUtil.flagRead ? "只读" : "写读");
                vc.add(String.valueOf(((OpenFile)openFiles.getFiles().get(i)).getFile().getDiskNum()));
                vc.add(((OpenFile)openFiles.getFiles().get(i)).getFile().getLocation());
            } else {
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
        return 4;
    }

    public String getColumnName(int column) {
        return (String)this.columnNames.get(column);
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return ((Vector)this.rowDatas.get(rowIndex)).get(columnIndex);
    }
}
