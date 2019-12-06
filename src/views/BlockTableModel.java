package views;

import models.FAT;
import service.FATService;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;


//磁盘显示情况分析表
public class BlockTableModel extends AbstractTableModel {
    private Vector<String> columnNames;
    private Vector<Vector<Integer>> rowDatas;
    private FATService fatService = new FATService();
    private int index = 0;

    public BlockTableModel() {
        this.initData();
    }

    public void initData() {
        this.columnNames = new Vector();
        this.columnNames.add("磁盘块");
        this.columnNames.add("值");
        this.rowDatas = new Vector();
        Vector<Integer> vs = null;
        FAT[] list = FATService.getMyFAT();

        for(int i = 0; i < 128; ++i) {
            vs = new Vector();
            if (list[i] != null) {
                vs.add(i);
                vs.add(list[i].getIndex());
            } else {
                vs.add(i);
                vs.add(0);
            }

            this.rowDatas.add(vs);
        }

    }

    public int getRowCount() {
        return 128;
    }

    public int getColumnCount() {
        return 2;
    }

    public String getColumnName(int column) {
        return (String)this.columnNames.get(column);
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return ((Vector)this.rowDatas.get(rowIndex)).get(columnIndex);
    }
}
